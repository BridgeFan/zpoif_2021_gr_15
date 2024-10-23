package beetle;

import java.awt.*;

public class Tile extends GameObject {
    final private boolean isPassable, isDestructable;

    public boolean IsPassable() {
        return isPassable;
    }

    public boolean IsDestructable() {
        return isDestructable;
    }

    Tile(int windowX, int windowY, String imagePath, boolean isPassable, boolean isDestructable) {
        super(windowX, windowY, imagePath);
        this.isPassable=isPassable;
        this.isDestructable=isDestructable;
    }
    Tile(int windowX, int windowY, String imagePath) {
        super(windowX, windowY, imagePath);
        this.isPassable=true;
        this.isDestructable=false;
    }
    Tile(boolean isPassable, boolean isDestructable, Image img) {
        super(img);
        this.isPassable=isPassable;
        this.isDestructable=isDestructable;
    }
    @Override
    public GameObject clone() {
        return new Tile(isPassable,isDestructable,getImage());
    }
}
