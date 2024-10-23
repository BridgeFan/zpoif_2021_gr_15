package beetle;

public class MoveChecker {
    static private Board board;
    static void setBoard(Board b) {
        board=b;
    }
    static boolean canBeMovedTo(Movable movable, Direction direction) {
        if(board == null || movable.getX()<0 || movable.getY()<0 || movable.getX()>=board.gameMap.getSizeX() || movable.getY()>=board.gameMap.getSizeY())
            return false;
        for(Enemy enemy: board.enemies) {
            if(!enemy.equals(movable) && enemy.getX()==movable.getX() && enemy.getY()==movable.getY())
                return false;
        }
        if(!board.player.equals(movable) && board.player.getX()==movable.getX() && board.player.getY()==movable.getY()) {
            board.player.dealDamage(1);
            return false;
        }
        if(board.gameMap.getTile(movable.getX(),movable.getY()).type instanceof Tile) {
            if(board.gameMap.getTile(movable.getX(),movable.getY()).type instanceof EmptyTile)
                return true;
            else if(((Tile)board.gameMap.getTile(movable.getX(),movable.getY()).type).IsPassable() && movable instanceof Player) {
                board.gameMap.removeTile(movable.getX(),movable.getY());
                board.repaint();
                return true;
            }
        }
        else if(movable instanceof Player && board.gameMap.getTile(movable.getX(),movable.getY()).type instanceof GravitabeObject) {
            var player = (Player)movable;
            var object = (GravitabeObject)board.gameMap.getTile(movable.getX(),movable.getY()).type;
            if(object.IsCollectible()) {
                if(!board.gameMap.getTile(movable.getX(),movable.getY()).wasPutByUser)
                    player.addPoints(((GravitabeObject) board.gameMap.getTile( movable.getX(), movable.getY() ).type).GetCollectionPoints());
                if(object.IsWeapon())
                    player.addWeapon(object.GetWeaponChar(),object.GetWeaponCount());
                board.gameMap.removeTile(movable.getX(), movable.getY());
                return true;
            }
            if(direction==Direction.Left)
                return tryMove( board.gameMap.getTile( movable.getX(), movable.getY() ), movable.getX() - 1, movable.getY() );
            if(direction==Direction.Right)
                return tryMove( board.gameMap.getTile( movable.getX(), movable.getY() ), movable.getX() + 1, movable.getY() );
        }
        return false;
    }

    static boolean tryMove(ObjectInstance instance, int x, int y) {
        if(instance.getX()==x && instance.getY()==y)
            return false;
        if(!(instance.type instanceof GravitabeObject))
            return false;
        if(board == null || x<0 || y<0 || x>=board.gameMap.getSizeX() || y>=board.gameMap.getSizeY())
            return false;
        //if falls onto enemy, destroy it
        for (Enemy enemy : board.enemies) {
            if (enemy.getX() == x && enemy.getY() == y) {
                if(y>instance.getY() && ((GravitabeObject)instance.type).isFalling)
                    board.enemies.remove(enemy);
                return false;
            }
        }
        if (board.player.getX() == x && board.player.getY() == y) {
            if(((GravitabeObject)instance.type).isFalling)
                if(y>instance.getY() && !((GravitabeObject)instance.type).isExplosive)
                    board.player.dealDamage(1);
            return false;
        }
        if(board.gameMap.getTile(x,y).type instanceof EmptyTile) {
            board.gameMap.swapTiles(x,y,instance.getX(),instance.getY());
            return true;
        }
        else
            return false;


    }
}
