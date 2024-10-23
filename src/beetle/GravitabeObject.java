package beetle;

import java.awt.*;

public class GravitabeObject extends GameObject {

    protected boolean isCollectible, isExplosive;
    protected int points, damagePoints;
    public boolean isFalling=false;
    protected char weaponChar;
    protected int givenWeaponCount;

    public GravitabeObject(Image img,boolean isCollectible, boolean isExplosive, int points, int damagePoints, char weaponChar, int givenWeaponCount) {
        super(img);
        this.isCollectible = isCollectible;
        this.isExplosive = isExplosive;
        this.points = points;
        this.damagePoints = damagePoints;
        this.weaponChar = weaponChar;
        this.givenWeaponCount = givenWeaponCount;
        this.isFalling = false;
    }

    GravitabeObject(int windowX, int windowY, String imagePath, int collectPoints, int explosionDamage) {
        super(windowX, windowY, imagePath);
        this.isCollectible=false;
        this.isExplosive=false;
        this.isFalling=false;
        this.points=collectPoints;
        this.damagePoints=explosionDamage;
        if(collectPoints>0)
            this.isCollectible=true;
        if(explosionDamage>0)
            this.isExplosive=true;
        givenWeaponCount=0;
        weaponChar='\0';
    }
    GravitabeObject(int windowX, int windowY, String imagePath, int collectPoints, int explosionDamage, char weaponChar, int weaponCount) {
        super(windowX, windowY, imagePath);
        this.isCollectible=false;
        this.isExplosive=false;
        this.isFalling=false;
        this.points=collectPoints;
        this.damagePoints=explosionDamage;
        this.weaponChar=weaponChar;
        this.givenWeaponCount = weaponCount;
        if(collectPoints>0)
            this.isCollectible=true;
        if(explosionDamage>0)
            this.isExplosive=true;
    }
    GravitabeObject(int windowX, int windowY, String imagePath) {
        super(windowX, windowY, imagePath);
        this.isCollectible=false;
        this.isExplosive=false;
        this.points=0;
        this.damagePoints=0;
    }
    public boolean IsCollectible() {
        return isCollectible;
    }
    public boolean IsExplosive() {
        return isExplosive;
    }
    public int GetCollectionPoints()
    {
        return points;
    }
    public int GetExplosionDamage()
    {
        return damagePoints;
    }
    public int GetWeaponCount() {return givenWeaponCount;}
    public char GetWeaponChar() {return weaponChar;}
    public boolean IsWeapon() {return givenWeaponCount>0;}

    @Override
    public GameObject clone() {
        return new GravitabeObject(getImage(),isCollectible, isExplosive, points, damagePoints, weaponChar, givenWeaponCount);
    }
}
