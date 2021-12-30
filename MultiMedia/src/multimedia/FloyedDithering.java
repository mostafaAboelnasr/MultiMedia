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
public class FloyedDithering {

    static BufferedImage bufferedImage;
    static BufferedImage dietered;

    public FloyedDithering() {
        try {
            bufferedImage = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
            dietered = floydSteinbergDithering(ImageIO.read(getClass().getResource("/multimedia/src/park.jpg"))).getSubimage(100, 100, 300, 300);

        } catch (IOException ex) {
            Logger.getLogger(FloyedDithering.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static BufferedImage floydSteinbergDithering(BufferedImage img) {

        Colors[] palette = new Colors[]{
           new Colors(0, 255, 0),
            new Colors(0, 0, 255),
            new Colors(0, 255, 0),
            new Colors(0, 255, 255),
            new Colors(255, 0, 255),
            new Colors(255, 0, 255),
            new Colors(255, 255, 0),
            new Colors(255, 255, 255)
        };
 
        int w = img.getWidth();
        int h = img.getHeight();

        Colors[][] d = new Colors[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                d[y][x] = new Colors(img.getRGB(x, y));
            }
        }

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {

                Colors PreviosColor = d[y][x];
                Colors newColor = findClosestPaletteColor(PreviosColor, palette);
                img.setRGB(x, y, newColor.toColor().getRGB());

                Colors err = PreviosColor.sub(newColor);

                if (x + 1 < w) {
                    d[y][x + 1] = d[y][x + 1].add(err.mul(7. / 16));
                }
                if (x - 1 >= 0 && y + 1 < h) {
                    d[y + 1][x - 1] = d[y + 1][x - 1].add(err.mul(3. / 16));
                }
                if (y + 1 < h) {
                    d[y + 1][x] = d[y + 1][x].add(err.mul(5. / 16));
                }
                if (x + 1 < w && y + 1 < h) {
                    d[y + 1][x + 1] = d[y + 1][x + 1].add(err.mul(1. / 16));
                }
            }
        }

        return img;
    }

    private static Colors findClosestPaletteColor(Colors c, Colors[] palette) {
        Colors closest = palette[0];

        for (Colors n : palette) {
            if (n.diff(c) < closest.diff(c)) {
                closest = n;
            }
        }

        return closest;
    }

    public static void main(String[] args) throws IOException {

        new FloyedDithering();

        JFrame frame = new JFrame("Test");
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

    static class Colors {

        int r, g, b;

        public Colors(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        public Colors(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public Colors add(Colors o) {
            return new Colors(r + o.r, g + o.g, b + o.b);
        }

        public Colors sub(Colors o) {
            return new Colors(r - o.r, g - o.g, b - o.b);
        }

        public Colors mul(double d) {
            return new Colors((int) (d * r), (int) (d * g), (int) (d * b));
        }

        public int diff(Colors o) {
            return Math.abs(r - o.r) + Math.abs(g - o.g) + Math.abs(b - o.b);
        }

        public int toRGB() {
            return toColor().getRGB();
        }

        public Color toColor() {
            return new Color(clamp(r), clamp(g), clamp(b));
        }

        public int clamp(int c) {
            return Math.max(0, Math.min(255, c));
        }
    }
}
