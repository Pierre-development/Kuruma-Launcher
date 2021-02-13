package fr.pierre.launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main extends JFrame {

    private static Main instance;
    private Panel panel;
    private Image logo = Utils.loadImage("Logo.png");

    public static void main(String[] args) throws IOException {
        instance = new Main();
    }

    public Main() throws IOException {
        this.setTitle("ShurikenWorld-Launcher");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(logo);
        this.setResizable(false);
        this.setSize(900, 600);
        this.setContentPane(panel = new Panel());
        this.setVisible(true);

    }

    public static Main getInstance() {
        return instance;
    }

    public Panel getPanel() {
        return panel;
    }
}
