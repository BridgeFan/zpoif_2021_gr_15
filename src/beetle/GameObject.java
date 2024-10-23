package beetle;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public abstract class GameObject {
    private Image image;
    Image getImage() {
        return image;
    }
    GameObject(int windowX, int windowY, String imagePath) {
        loadImage(windowX,windowY, imagePath);
    }
    void loadImage(int windowX, int windowY, String path) {
        ImageIcon ii = new ImageIcon(path);
        image = ii.getImage();
        if(image.getWidth(null)<0)
            return;
        image = image.getScaledInstance((int)(.08f*windowX),(int)(.08f*windowX),Image.SCALE_DEFAULT);
    }
    void draw(Graphics g, int x, int y) {
        g.drawImage(image, Math.round(x*64.f), Math.round(y*64.f), null);
    }

    GameObject(Image img) {
        this.image = img;
    }
    public abstract GameObject clone();
}
