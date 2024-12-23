package ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

//Represents the image panel
public class ImagePanel extends JPanel {
    private Image backgroundImage;

    // EFFECTS: constructs the loaded image
    public ImagePanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("No such file!");
        }

    }

    @Override
    // EFFECTS:draws the background image if the backgroundImage is not null
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}