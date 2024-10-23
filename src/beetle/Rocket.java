package beetle;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Rocket extends GravitabeObject {
    public Direction direction;
    Rocket(Direction dir, int windowX, int windowY, String imagePath, int collectPoints) {
        super( windowX, windowY, imagePath, collectPoints, 0 );
        this.direction = dir;
        this.isFalling=true;
    }
    Rocket(Direction dir, int windowX, int windowY, String imagePath, int collectPoints, int explosionDamage, char weaponChar, int weaponCount) {
        super(windowX, windowY, imagePath, collectPoints, explosionDamage, weaponChar, weaponCount);
        this.direction = dir;
        this.isFalling=true;
    }
    Rocket(Direction dir, Image img,boolean isCollectible, boolean isExplosive, int points, int damagePoints, char weaponChar, int givenWeaponCount) {
        super(img,isCollectible,isExplosive, points, damagePoints, weaponChar, givenWeaponCount);
        this.direction = dir;
        this.isFalling=true;
    }
    @Override
    public GameObject clone() {
        return new Rocket(direction, getImage(),isCollectible, isExplosive, points, damagePoints, weaponChar, givenWeaponCount);
    }
    @Override
    void draw(Graphics g, int x, int y) {
        int degress = 0;
        Image image = getImage();
        switch (direction)
        {
            case Left: degress = 180; break;
            case Right: degress = 0; break;
            case Up: degress = 270; break;
            case Down: degress = 90; break;
        }
        double rotationRequired = Math.toRadians (degress);
        double locationX = image.getWidth(null) / 2;
        double locationY = image.getHeight(null) / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = newImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();
        g.drawImage(op.filter(newImage,null), Math.round(x*64.f), Math.round(y*64.f), null);
    }
}
