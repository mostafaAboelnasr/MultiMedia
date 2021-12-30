/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimedia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Mostafa Aboelnasr
 */
public class ThresholdDithering {

    static BufferedImage bufferedImage;
    static BufferedImage dietered;

    public ThresholdDithering() {
        try {
            bufferedImage = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
            dietered = floydSteinbergDithering(ImageIO.read(getClass().getResource("/multimedia/src/park.jpg"))).getSubimage(100, 100, 300, 300);

//            System.out.println(Color.BLACK);
//            System.out.println(Color.WHITE);
        } catch (IOException ex) {
        }
    }

    private static BufferedImage floydSteinbergDithering(BufferedImage img) {

        int w = img.getWidth();
            int h = img.getHeight();

        int[][] d = new int[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                d[y][x] = img.getRGB(x, y);
            }
        }
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if (d[y][x] < -16777216 / 5) {
                    img.setRGB(x, y, -16777216);
                } else {
                    img.setRGB(x, y, -1);
                }

            }
        }
//        System.out.println("Color.HSBtoRGB(255, 255, 255) " + Color.HSBtoRGB(0, 0, 0));
//        System.out.println(Color.BLACK.getRGB());
//        System.out.println(Color.WHITE.getRGB());

        return img;
    }

    public static void main(String[] args) throws IOException {

        new ThresholdDithering();

        JFrame frame = new JFrame("Test");
        frame.setSize(bufferedImage.getWidth(), bufferedImage.getHeight());
        frame.setLayout(new GridLayout(1, 2));

        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bufferedImage, 0, 0, this);
            }
        });
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(dietered, 0, 0, this);
            }
        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}
