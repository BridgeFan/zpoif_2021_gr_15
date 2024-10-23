package beetle;

import javax.swing.*;
import java.awt.*;

public class ObjectInstance {
    private int x,y;
    public boolean wasPutByUser;
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public GameObject type;
    ObjectInstance(int x, int y, GameObject type, boolean wasPutByUser) {
        this.type=type;
        this.x=x;
        this.y=y;
        this.wasPutByUser=wasPutByUser;
    }
    ObjectInstance(int x, int y) {
        this(x,y,null,false);
    }
    ObjectInstance(int x, int y, boolean wasPutByUser) {
        this(x,y,null,wasPutByUser);
    }
    ObjectInstance(int x, int y, GameObject type) {
        this(x,y,type,false);
    }

    void draw(Graphics g, int actualX, int actualY) {
        if(x-actualX<0 || y-actualY<0 || x-actualX>=9 || y-actualY>=9)
            return;
        type.draw(g, x-actualX, y-actualY);
    }
    public boolean move(Direction direction) {
        if(direction==null)
            return false;
        int newX=this.x;
        int newY=this.y;
        switch(direction) {
            case Left:
                newX--;
                break;
            case Right:
                newX++;
                break;
            case Up:
                newY--;
                break;
            case Down:
                newY++;
                break;
        }
        if(MoveChecker.tryMove(this,newX,newY)) {
            this.x=newX;
            this.y=newY;
            return true;
        }
        return true;
    }
}
