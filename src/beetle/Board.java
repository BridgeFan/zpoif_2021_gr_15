package beetle;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Optional;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    private LevelManager manager;
    Player player;
    java.util.ArrayList<Enemy> enemies = new java.util.ArrayList<>();
    ArrayList<ExplosionPosition> explosionPositions = new java.util.ArrayList<>();
    Timer timer;
    float scale;
    GameMap gameMap;
    MoveChecker moveChecker;
    private int timerCount=0;
    private int lastPlayerMove=0;
    private final int gravitableMoveTime=18;
    int sizeX, sizeY;

    public int getActualX() {
        return actualX;
    }

    public void setActualX(int actualX) {
        if(actualX-4<0 || gameMap.getSizeX()<=9)
            this.actualX=0;
        else if(actualX-4>gameMap.getSizeX()-9)
            this.actualX=gameMap.getSizeX()-9;
        else
            this.actualX = actualX-4;
    }

    public int getActualY() {
        return actualY;
    }

    public void setActualY(int actualY) {
        if(actualY-4<0 || gameMap.getSizeY()<=8)
            this.actualY=0;
        else if(actualY-4>gameMap.getSizeY()-8)
            this.actualY=gameMap.getSizeY()-8;
        else
            this.actualY = actualY-4;
    }

    private int actualX, actualY;
    Optional<Direction> moveDirection = Optional.empty();

    public Board(int sizeX, int sizeY) {
        this.sizeX=sizeX;
        this.sizeY=sizeY;
        scale = sizeX/800.f;
        MoveChecker.setBoard(this);
        timer = new Timer(10, this);
        timer.start();
        BlueEnemy.setWindowScale(sizeX, sizeY);
        GreenEnemy.setWindowScale(sizeX, sizeY);
        ExplosionPosition.setWindowScale(this, sizeX, sizeY);
        gameMap = new GameMap(sizeX,sizeY,"src/levels/001.txt", this);
    }

    void setManager(LevelManager lm) {
        manager=lm;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i=0;i<gameMap.getSizeX();i++)
            for(int j=0;j<gameMap.getSizeY();j++)
                gameMap.getTile(i,j).draw(g, actualX, actualY);
        for (Enemy enemy: enemies)
            enemy.draw(g);
        player.draw(gameMap, g);
        for(var e: explosionPositions) {
            e.draw(g);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean shouldRepaint=false;
        //remove unused explosions
        for (int i = 0; i < explosionPositions.size(); i++) {
            ExplosionPosition a = explosionPositions.get(i);
            a.decay--;
            if(a.decay<=0) {
                shouldRepaint = true;
                explosionPositions.remove(a);
                i--;
            }
        }
        //check win
        if(enemies.size()==0) //VICTORY
            manager.passFinishResult( true );
        //apply
        player.removeTime();
        timerCount++;
        lastPlayerMove = lastPlayerMove>0 ? lastPlayerMove-1 : 0;
        for (int i = 0; i < enemies.size() ; i++) {
            Enemy enemy = enemies.get(i);
            if(timerCount%Enemy.moveTime == 0) {
                shouldRepaint=true; //bugogenne
                enemy.makeMove();// potencjalnie możemy tutaj pominąć turę części przeciwników gdyby ruch przeciwnika spowodował śmierć innych przeciwników
            }
        }
        if(lastPlayerMove==0 && moveDirection.isPresent()) {
            shouldRepaint=true;
            player.move(moveDirection.get());
            lastPlayerMove=22;
        }
        if(timerCount%gravitableMoveTime == 0) {
            shouldRepaint=true;
            gameMap.moveGravitables();
        }
        if(shouldRepaint)
            repaint();
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if(moveDirection.isEmpty())
                lastPlayerMove=10;
            moveDirection= Optional.of(Direction.Left);
            player.direction=moveDirection.get();
            repaint();
        }

        if (key == KeyEvent.VK_RIGHT) {
            if(moveDirection.isEmpty())
                lastPlayerMove=10;
            moveDirection= Optional.of(Direction.Right);
            player.direction=moveDirection.get();
            repaint();
        }
        if (key == KeyEvent.VK_UP) {
            if(moveDirection.isEmpty())
                lastPlayerMove=10;
            moveDirection= Optional.of(Direction.Up);
            player.direction=moveDirection.get();
            repaint();
        }
        if (key == KeyEvent.VK_DOWN) {
            if(moveDirection.isEmpty())
                lastPlayerMove=10;
            moveDirection= Optional.of(Direction.Down);
            player.direction=moveDirection.get();
            repaint();
        }
        if (key == KeyEvent.VK_SPACE) {
            player.createWeapon(gameMap);
            repaint();
        }
        if (key == KeyEvent.VK_C) {
            player.changeWeapon();
            repaint();
        }
        if (key == KeyEvent.VK_R) {
            manager.passFinishResult( false );
        }
        if (key == KeyEvent.VK_ESCAPE) {
            manager.setMoveToNextLevel( false );
            manager.passFinishResult( false );
        }
    }

    public void passFinishResult(boolean result) {
        manager.passFinishResult( result );
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            moveDirection = Optional.empty();
        }
    }
}
