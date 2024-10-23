package beetle;

import javax.swing.*;
import java.awt.*;

public class BlueEnemy extends Enemy {
    private static Image imageup, imageright, imagedown, imageleft;
    static {
        ImageIcon iiu = new ImageIcon("src/resources/enemy-blue-up.png");
        imageup = iiu.getImage();
        ImageIcon iir = new ImageIcon("src/resources/enemy-blue-right.png");
        imageright = iir.getImage();
        ImageIcon iid = new ImageIcon("src/resources/enemy-blue-down.png");
        imagedown = iid.getImage();
        ImageIcon iil = new ImageIcon("src/resources/enemy-blue-left.png");
        imageleft = iil.getImage();
    }

    static void setWindowScale(int windowX, int windowY) {
        imageup = imageup.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
        imageright = imageright.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
        imagedown = imagedown.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
        imageleft = imageleft.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
    }

    BlueEnemy(Board board, int x, int y) {
        super(board, x, y);
    }

    @Override
    void draw(Graphics g) {
        int ax=x-board.getActualX();
        int ay=y-board.getActualY();
        if(ax<0 || ay<0 || ax>=9 || ay>=9)
            return;
        switch(direction) {
            case Left:
                g.drawImage(imageleft, Math.round(ax*64.f), Math.round(ay*64.f), null);
                break;
            case Right:
                g.drawImage(imageright, Math.round(ax*64.f), Math.round(ay*64.f), null);
                break;
            case Up:
                g.drawImage(imageup, Math.round(ax*64.f), Math.round(ay*64.f), null);
                break;
            case Down:
                g.drawImage(imagedown, Math.round(ax*64.f), Math.round(ay*64.f), null);
                break;
        }
    }


    @Override
    void makeMove() {
        //check explosive - attack
        int ex = x;
        int ey = y;
        switch (direction) {
            case Left:
                ex--;
                break;
            case Up:
                ey--;
                break;
            case Right:
                ex++;
                break;
            case Down:
                ey++;
                break;
        }
        if(ex>0 && ey>0 && ex<board.gameMap.getSizeX() && ey<board.gameMap.getSizeY()) {
            var type = board.gameMap.getTile(ex, ey).type;
            if(type instanceof GravitabeObject && ((GravitabeObject)type).IsExplosive()) {
                board.explosionPositions.add(new ExplosionPosition( ex, ey, ((GravitabeObject) type).GetExplosionDamage()));
                return;
            }
        }
        //otherwise
        /*for(int i=1;i<4;i++)*/ {
            if (move(direction))
                return;
            switch (direction) {
                case Left:
                    direction = Direction.Down;
                    break;
                case Up:
                    direction = Direction.Left;
                    break;
                case Right:
                    direction = Direction.Up;
                    break;
                case Down:
                    direction = Direction.Right;
                    break;
            }
        }
    }
}
