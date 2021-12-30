/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimedia;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Mostafa Aboelnasr
 */
public class DitherDissolve {

    static BufferedImage bufferedImage1, bufferedImage2;

    public DitherDissolve() {
        try {
            bufferedImage1 = ImageIO.read(getClass().getResource("/multimedia/src/dema.jpg")).getSubimage(100, 100, 300, 300);
            bufferedImage2 = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
        } catch (IOException ex) {
            Logger.getLogger(DitherDissolve.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static BufferedImage Dither(BufferedImage bufferedImage) {

        int w1 = bufferedImage1.getWidth();
        int h1 = bufferedImage1.getHeight();
        C3[][] d1 = new C3[h1][w1];
        for (int y = 0; y < h1; y++) {
            for (int x = 0; x < w1; x++) {
                d1[y][x] = new C3(bufferedImage1.getRGB(x, y));
            }
        }

        int w2 = bufferedImage2.getWidth();
        int h2 = bufferedImage2.getHeight();
        C3[][] d2 = new C3[h2][w2];
        for (int y = 0; y < h2; y++) {
            for (int x = 0; x < w2; x++) {
                d2[y][x] = new C3(bufferedImage2.getRGB(x, y));
            }
        }

        C3[][] result = new C3[h2][w2];
        for (int y = 0; y < h2; y++) {
            for (int x = 0; x < w2; x++) {

                C3 newColor = d1[y][x].add(d2[y][x]);
//                result[x][y] = newColor;
                bufferedImage.setRGB(x, y, newColor.toColor().getRGB());
//                result[y][x] = new C3(bufferedImage1.getRGB(x, y));
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
        }
        return bufferedImage;
    }

    public static void main(String[] args) {
        new DitherDissolve();
        JButton button = new JButton();
        button.setBounds(100, 0, 50, 50);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("bufferedImage2");
                BufferedImage Dither = Dither(bufferedImage2);

            }
        });
        JFrame frame = new JFrame("Test");
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Dither(bufferedImage1), 0, 0, this);
//                g.drawImage(bufferedImage1, 0, 0, this);
            }
        });

//        frame.add(button);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

//    private static C3 findClosestPaletteColor(C3 c, C3[] palette) {
//        C3 closest = palette[0];
//
//        for (C3 n : palette) {
//            if (n.diff(c) < closest.diff(c)) {
//                closest = n;
//            }
//        }
//
//        return closest;
//    }
    static class C3 {

        int r, g, b;

        public C3(int c) {
            Color color = new Color(c);
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
        }

        public C3(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public C3 add(C3 o) {
            return new C3(r + o.r, g + o.g, b + o.b);
        }

        public C3 sub(C3 o) {
            return new C3(r - o.r, g - o.g, b - o.b);
        }

        public C3 mul(double d) {
            return new C3((int) (d * r), (int) (d * g), (int) (d * b));
        }

        public int diff(C3 o) {
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
