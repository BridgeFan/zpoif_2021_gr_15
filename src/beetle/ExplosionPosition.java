package beetle;

import javax.swing.*;
import java.awt.*;

public class ExplosionPosition {
    static Image image;
    private static Board board;
    static {
        ImageIcon ii = new ImageIcon("src/resources/explosion.png");
        image = ii.getImage();
    }
    static void setWindowScale(Board board, int windowX, int windowY) {
        ExplosionPosition.board =board;
        image = image.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
    }
    int x;
    int y;
    int decay;
    int radius;

    private int sqrDistance(int x, int y) {
        return x*x+y*y;
    }
    private boolean isInRange(int x, int y, int xc, int yc, int radius) { // kwadrat
        return Math.abs(x-xc)<=radius && Math.abs(y-yc)<=radius;
    }
    private boolean isInRange(int x, int y, int xc, int yc, float radius) { // niekoniecznie kwadrat
        return sqrDistance(x-xc, y-yc)<=radius*radius;
    }

    public ExplosionPosition(int x, int y, int damage, int sqrRange, int decay) {
        this.x = x;
        this.y = y;
        this.decay = decay;
        this.radius = sqrRange;
        if(isInRange(board.player.x,board.player.y,x,y,sqrRange))
            board.player.dealDamage(damage);
        for (int i = 0; i < board.enemies.size(); i++) {
            Enemy enemy = board.enemies.get(i);
            if (isInRange(enemy.x,enemy.y,x,y,sqrRange)) {
                board.enemies.remove(enemy);
                i--;
            }
        }
        for(int i=Math.max(0, x-sqrRange);i<=Math.min(x+sqrRange, board.gameMap.getSizeX()-1);i++) {
            for (int j = Math.max( 0, y - sqrRange); j <= Math.min( y + sqrRange, board.gameMap.getSizeY()-1 ); j++) {
                if (isInRange(i,j,x,y,sqrRange)) {
                    var tile = board.gameMap.getTile(i,j).type;
                    board.gameMap.removeTile(i, j);
                    if(tile instanceof GravitabeObject){
                        var grav = (GravitabeObject)tile;
                        if (grav.isExplosive)
                            board.explosionPositions.add(new ExplosionPosition(i, j, grav.GetExplosionDamage()));
                    }
                    else board.gameMap.removeTile(i, j);
                }
            }
        }
        /*for(int i=Math.max(0, x-(int)Math.sqrt(sqrRange));i<Math.min(x+(int)Math.sqrt(sqrRange), board.gameMap.getSizeX());i++) {
            for (int j = Math.max( 0, y - (int)Math.sqrt(sqrRange) ); j < Math.min( y + (int)Math.sqrt(sqrRange), board.gameMap.getSizeY() ); j++) {
                if (sqrDistance(i-x, j-y)<=sqrRange*sqrRange) {
                    board.gameMap.removeTile( i, j );
                }
            }
        }*/
    }

    public ExplosionPosition(int x, int y, int damage, int sqrRange) {
        this(x,y,damage,sqrRange,150);
    }

    public ExplosionPosition(int x, int y, int damage) {
        this(x,y,damage,1,150);
        }

    public void draw(Graphics g) {
        for(int i=Math.max(0, x-radius);i<=Math.min(x+radius, board.gameMap.getSizeX()-1);i++)
            for (int j = Math.max( 0, y - radius); j <= Math.min( y + radius, board.gameMap.getSizeY()-1 ); j++)
                if(i-board.getActualX()>=0 && j-board.getActualY()>=0 && i-board.getActualX()<9 && j-board.getActualY()<9)
                    g.drawImage(image, Math.round((i-board.getActualX())*64.f), Math.round((j-board.getActualY())*64.f), null);

        /*g.drawImage(image, Math.round(x*64.f), Math.round(y*64.f), null);
        if(x>0)
            g.drawImage(image, Math.round((x-1)*64.f), Math.round(y*64.f), null);
        if(x<sizeX-1)
            g.drawImage(image, Math.round((x+1)*64.f), Math.round(y*64.f), null);
        if(y>0)
            g.drawImage(image, Math.round(x*64.f), Math.round((y-1)*64.f), null);
        if(y<sizeY-1)
            g.drawImage(image, Math.round(x*64.f), Math.round((y+1)*64.f), null);
        if(x>0 && y>0)
            g.drawImage(image, Math.round((x-1)*64.f), Math.round((y-1)*64.f), null);
        if(x<sizeX-1 && y>0)
            g.drawImage(image, Math.round((x+1)*64.f), Math.round((y-1)*64.f), null);
        if(x>0 && y<sizeY-1)
            g.drawImage(image, Math.round((x-1)*64.f), Math.round((y+1)*64.f), null);
        if(x<sizeX-1 && y<sizeY-1)
            g.drawImage(image, Math.round((x+1)*64.f), Math.round((y+1)*64.f), null);*/

    }
}
