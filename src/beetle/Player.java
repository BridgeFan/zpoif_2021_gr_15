package beetle;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Player extends Movable {
    private Image image, healthImage, emptyHealthImage;
    private final Board board;
    private int health;
    private int protectionTimer=0;
    private int points;
    final private int maxHealth;
    final private Map<Character, Integer> weaponState = new TreeMap<>();
    static Font font = new Font("Serif", Font.PLAIN, 32);
    static Font boldFont = new Font("Serif", Font.BOLD, 32);
    char actualWeapon;

    Player(int x, int y, Board board) {
        super(x,y);
        this.board=board;
        board.setActualX(x);
        board.setActualY(y);
        loadImage(board.sizeX, board.sizeY);
        health=maxHealth=8;
        weaponState.put('n', 8);
        actualWeapon='n';
    }
    void removeTime() {protectionTimer--;}
    void loadImage(int windowX, int windowY) {
        ImageIcon ii = new ImageIcon("src/resources/player.png");
        image = ii.getImage();
        image = image.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
        ImageIcon hi = new ImageIcon("src/resources/heart.png");
        healthImage = hi.getImage();
        healthImage = healthImage.getScaledInstance((int)(.04f*windowX),(int)(.04f*windowX),Image.SCALE_DEFAULT);
        ImageIcon ei = new ImageIcon("src/resources/empty-heart.png");
        emptyHealthImage = ei.getImage();
        emptyHealthImage = emptyHealthImage.getScaledInstance((int)(.04f*windowX),(int)(.04f*windowX),Image.SCALE_DEFAULT);
    }
    void draw(GameMap map, Graphics g) {
        g.drawImage(image, Math.round((x-board.getActualX())*64.f), Math.round((y-board.getActualY())*64.f), null);
        for(int i=0;i<health;i++) {
            g.drawImage(healthImage, 640+Math.round((i%5)*32.f), 20+Math.round((float)(i/5)*32.f), null);
        }
        for(int i=health;i<maxHealth;i++) {
            g.drawImage(emptyHealthImage, 640+Math.round((i%5)*32.f), 20+Math.round((float)(i/5)*32.f), null);
        }
        g.setFont(font);
        g.drawString("Points: "+points, 640, 148);
        int index=0;
        for(var a: weaponState.entrySet()) {
            g.drawImage(map.getObjectImage(a.getKey()), 640, 180+Math.round(index*64.f), null);
            if(a.getKey()==actualWeapon)
                g.setFont(boldFont);
            else
                g.setFont(font);
            g.drawString(a.getValue().toString(), 720, 212+Math.round(index*64.f));
            //
            index++;
        }
    }
    void dealDamage(int hp) {
        if(protectionTimer>0)
            return;
        health-=hp;
        protectionTimer=100;
        if(health<=0)
            board.passFinishResult( false );
    }

    public static int findIndex(Character[] arr, char a)
    {
        return IntStream.range(0, arr.length)
                .filter(i -> a == arr[i])
                .findFirst() // first occurrence
                .orElse(-1);
    }

    void createWeapon(GameMap map) {
        if(!weaponState.containsKey(actualWeapon) || weaponState.get(actualWeapon)<=0)
            return;
        int wx = x;
        int wy = y;
        switch(direction) {
            case Left:
                wx--;
                break;
            case Right:
                wx++;
                break;
            case Up:
                wy--;
                break;
            case Down:
                wy++;
                break;
        }
        if(map.createObject(wx, wy, actualWeapon, true,direction))
            weaponState.put(actualWeapon, weaponState.get(actualWeapon)-1);
    }
    void changeWeapon() {
        var array = weaponState.keySet().toArray(new Character[0]);
        int index = findIndex(array, actualWeapon);
        index = (index + 1) % array.length;
        actualWeapon = array[ index ];

    }
    void addWeapon(char type, int size) {
        if(weaponState.containsKey( type ))
            weaponState.replace( type, weaponState.get(type)+size );
        else
            weaponState.put(type, size);

    }
    int getPoints() {
        return points;
    }
    void addPoints(int value) {
        points += value;
    }

    @Override
    public boolean move(Direction direction) {
        boolean result = super.move(direction);
        if(result) {
            board.setActualX( x );
            board.setActualY( y );
        }
        return result;
    }
}
