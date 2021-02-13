package fr.pierre.launcher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Utils {
    public static Image loadImage(String image) {
        try {
            return ImageIO.read(Main.class.getResource("/" + image));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("null");

        }
        System.out.println("null");
        return null;
    }
}
