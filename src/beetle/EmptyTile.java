package beetle;

import java.awt.*;

public class EmptyTile extends Tile{
    public EmptyTile(int windowX, int windowY) {
        super(windowX, windowY, "src/resources/tile.png");
    }
    EmptyTile(boolean isPassable, boolean isDestructable, Image img) {
        super(isPassable, isDestructable, img);
    }
    @Override
    public GameObject clone() {
        return new EmptyTile(IsPassable(), IsDestructable(), getImage());
    }
}
