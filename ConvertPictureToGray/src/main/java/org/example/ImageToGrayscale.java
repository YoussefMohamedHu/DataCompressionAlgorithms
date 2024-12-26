package org.example;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageToGrayscale {

    public static void main(String[] args) {
        try {
            // Load the image
            File input = new File("1.jpg"); // Replace with your image path
            BufferedImage image = ImageIO.read(input);

            // Get image dimensions
            int width = image.getWidth();
            int height = image.getHeight();

            // Create a new BufferedImage for grayscale
            BufferedImage grayscaleImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

            // Iterate through each pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    // Get the RGB values of the current pixel
                    Color color = new Color(image.getRGB(x, y));
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    // Calculate grayscale value using weighted average (more accurate)
                    int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                    // Or a simpler average:
                    // int gray = (red + green + blue) / 3;


                    // Set the grayscale value in the new image
                    grayscaleImage.setRGB(x, y, new Color(gray, gray, gray).getRGB());

                }
            }

            // Save the grayscale image
            File output = new File("out.jpg"); // Replace with desired output path
            ImageIO.write(grayscaleImage, "jpg", output);

            System.out.println("Image converted to grayscale successfully!");

        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }
}