package com.elega9t.tofu.common;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;

/**
 *
 * @author elega9t
 */
public class ImageUtils {

    public static BufferedImage load(String path) {
        return load(path, 0);
    }
    
    public static BufferedImage load(String path, Integer targetDimension) {
        try {
            BufferedImage image = ImageIO.read(ImageUtils.class.getResourceAsStream("/com/elega9t/tofu/icon/" + path + ".png"));
            if (targetDimension > 0) {
                Image resultingImage = image.getScaledInstance(targetDimension, targetDimension, Image.SCALE_DEFAULT);
                BufferedImage outputImage = new BufferedImage(targetDimension, targetDimension, BufferedImage.TYPE_INT_ARGB);
                outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
                image = outputImage;
            }
            return image;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ImageIcon loadIcon(String path) {
        return loadIcon(path, 24);
    }
    
    public static ImageIcon loadIcon(String path, Integer targetDimension) {
        return new ImageIcon(load(path, targetDimension));
    }
    
}
