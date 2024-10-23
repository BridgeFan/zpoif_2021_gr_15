package beetle;

import java.awt.*;

public abstract class Enemy extends Movable {
    static int moveTime = 25;
    protected final Board board;
    Enemy(Board board, int x, int y) {
        super(x,y);
        this.board = board;
    }
    abstract void draw(Graphics g);
    abstract void makeMove();
}
