package beetle;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

public class GameMap {
    private ObjectInstance[][] tiles;
    private final Map<Character, GameObject> objectTypes = new TreeMap<>();
    private final Board board;
    //private static GameObject emptyTile;
    GameMap(int windowX, int windowY, String path, Board board) {
        //emptyTile = new Tile(windowX, windowY, "resources/tile.png");
        //set tile types
        this.board=board;
        loadTypesFromFile(windowX, windowY);
        loadFromFile(path);
    }

    void loadTypesFromFile(int windowX, int windowY) {
        JSONObject json = new JSONObject();
        try {
            InputStream is = new FileInputStream( "src/types.json" );
            String jsonTxt = IOUtils.toString( is, "UTF-8" );
            json = new JSONObject( jsonTxt );
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        objectTypes.clear();
        objectTypes.put(' ',new EmptyTile(windowX, windowY)); //special case
        if(json.has("Tile")) {
            var tiles = json.getJSONArray( "Tile" );
            for(var o: tiles) {
                if(o instanceof JSONObject) {
                    var obj = (JSONObject)o;
                    if(!obj.has("char") && !obj.has("path"))
                        continue;
                    char a = obj.getString("char").charAt( 0 );
                    String tmpPath = obj.getString("path");
                    boolean isPassable = obj.has( "is_passable" ) && obj.getBoolean( "is_passable" );
                    boolean isDestructible = obj.has( "is_destructible" ) && obj.getBoolean( "is_destructible" );
                    objectTypes.put(a, new Tile(windowX, windowY, tmpPath, isPassable, isDestructible));
                }
            }
        }
        if(json.has("GravitabeObject")) {
            var objs = json.getJSONArray( "GravitabeObject" );
            for(var o: objs) {
                if(!(o instanceof JSONObject))
                    continue;
                var obj = (JSONObject)o;
                if(!obj.has("char") && !obj.has("path"))
                    continue;
                char a = obj.getString("char").charAt( 0 );
                String tmpPath = obj.getString("path");
                int collectPoints = obj.has("collect_points") ? obj.getInt("collect_points") : 0;
                int explosionDamage = obj.has("explosion_damage") ? obj.getInt("explosion_damage") : 0;
                char weaponChar = obj.has("weapon_char") ? obj.getString("weapon_char").charAt( 0 ) : '\0';
                int weaponCount = obj.has("weapon_count") ? obj.getInt("weapon_count") : 0;
                if(obj.has("direction"))
                {
                     String dir = obj.getString("direction");
                     Direction direction = Direction.Down;
                     switch (dir)
                     {
                         case "left": direction = Direction.Left; break;
                         case "right": direction = Direction.Right; break;
                         case "up": direction = Direction.Up; break;
                         case "down": direction = Direction.Down; break;
                     }
                    objectTypes.put(a, new Rocket(direction, windowX, windowY, tmpPath, collectPoints, explosionDamage, weaponChar, weaponCount ));
                }
                else
                    objectTypes.put(a, new GravitabeObject(windowX, windowY, tmpPath, collectPoints, explosionDamage, weaponChar, weaponCount ));
            }
        }
    }

    boolean loadFromFile(String path) {
        String[] lines;
        try {
            lines = Files.readAllLines( Path.of( path ) ).toArray(new String[0]);
        } catch (IOException e) {
            return false;
        }
        if(lines.length<2) {
            return false;
        }
        int sx,sy;
        try {
            sx = Integer.parseInt( lines[0] );
            sy = Integer.parseInt( lines[1] );
        } catch (NumberFormatException e) {
            return false;
        }
        //assumes correctness
        board.enemies.clear();
        board.explosionPositions.clear();
        tiles=new ObjectInstance[sx][];
        for(int i=0;i<sx;i++) {
            tiles[i] = new ObjectInstance[sy];
            for(int j=0;j<sy;j++)
                tiles[i][j] = new ObjectInstance( i,j,objectTypes.get(' '));
        }
        for(int i=0;i<Math.min(sy,lines.length-2);i++) {
            char[] line = lines[i+2].toCharArray();
            for(int j=0;j<Math.min(sx,line.length);j++) {
                if(line[j]=='S') {
                    board.player = new Player(j,i, board);
                    tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(' '));
                }
                else if(line[j]=='G') {
                    board.enemies.add(new GreenEnemy(board,j,i));
                    tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(' '));
                }
                else if(line[j]=='B') {
                    board.enemies.add(new BlueEnemy(board,j,i));
                    tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(' '));
                }
                else {
                    if(objectTypes.containsKey(line[j]))
                        tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(line[j]).clone());
                    else
                        tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(' '));
                }
            }
            for(int j=line.length;j<sx;j++) {
                tiles[i][j] = new ObjectInstance(i, j, objectTypes.get(' '));
            }
        }
        for(int i=lines.length-2;i<sy;i++) {
            tiles[i]=new ObjectInstance[sy];
            for(int j=0;j<sx;j++) {
                tiles[j][i] = new ObjectInstance(j, i, objectTypes.get(' '));
            }
        }
        return true;
    }

    int getSizeX() {return tiles.length;}
    int getSizeY() {return tiles[0].length;}

    Image getObjectImage(char a) {
        return objectTypes.get(a).getImage();
    }

    ObjectInstance getTile(int x, int y) {
        return tiles[x][y];
    }
    void swapTiles(int x1, int y1, int x2, int y2) {
        //swap tiles
        var t = tiles[x1][y1].type;
        tiles[x1][y1].type = tiles[x2][y2].type;
        tiles[x2][y2].type = t;
        //swap wasPutByUser
        boolean w = tiles[x1][y1].wasPutByUser;
        tiles[x1][y1].wasPutByUser = tiles[x2][y2].wasPutByUser;
        tiles[x2][y2].wasPutByUser = w;
    }
    void removeTile(int x, int y) {
        if(x>=getSizeX() || y>=getSizeY() || x<0 || y<0)
            return;
        if(tiles[x][y].type instanceof Tile && !((Tile)tiles[x][y].type).IsDestructable())
            return;
        tiles[x][y].type=objectTypes.get(' ');
    }
    boolean createObject(int x, int y, char c, boolean wasPutByUser, Direction direction) {
        if(x>=getSizeX() || y>=getSizeY() || x<0 || y<0)
            return false;
        if(tiles[x][y].type instanceof EmptyTile) {
            tiles[x][y].type = objectTypes.get(c).clone();
            tiles[x][y].wasPutByUser=wasPutByUser;
            if(tiles[x][y].type instanceof Rocket)
                ((Rocket)tiles[x][y].type).direction = direction;
            return true;
        }
        return false;
    }
    boolean createObject(int x, int y, char c) {
        return createObject( x,y,c,true, Direction.Down );
    }

    public void moveGravitables() {
        boolean moved[][] = new boolean[getSizeX()][getSizeY()];
        for(int j=getSizeY()-1;j>=0;j--) {
            for(int i=0;i<getSizeX();i++) {
                if(!(tiles[i][j].type instanceof GravitabeObject))
                    continue;
                if(moved[i][j]) continue;

                int dx = 0, dy = 1;
                if (tiles[i][j].type instanceof Rocket)
                {
                    var rock = (Rocket) tiles[i][j].type;
                    dx = 0; dy = 0;
                    switch (rock.direction)
                    {
                        case Left: dx = -1; break;
                        case Right: dx = 1; break;
                        case Up: dy = -1; break;
                        case Down: dy = 1; break;
                    }
                }
                boolean result = MoveChecker.tryMove(tiles[i][j],i+dx,j+dy);

                if((tiles[i][j].type instanceof GravitabeObject)) {

                    var grav = (GravitabeObject) tiles[i][j].type;
                    if (grav.isFalling && grav.isExplosive) {
                        board.explosionPositions.add(new ExplosionPosition(i, j, ((GravitabeObject) tiles[i][j].type).GetExplosionDamage()));
                    }
                    grav.isFalling = false;
                }
                if(result) {
                    moved[i+dx][j+dy]=true;
                    ((GravitabeObject) tiles[i + dx][j + dy].type).isFalling = true;
                }
            }
        }
    }

}
