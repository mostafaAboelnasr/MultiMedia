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
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author Mostafa Aboelnasr
 */
public class quantization {

    static BufferedImage bufferedImage1;
    static BufferedImage bufferedImage2;

    public quantization() {
        try {
            bufferedImage1 = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
            bufferedImage2 = ImageIO.read(getClass().getResource("/multimedia/src/dema.jpg")).getSubimage(100, 100, 300, 300);

//            System.out.println(Color.BLACK);
//            System.out.println(Color.WHITE);
        } catch (IOException ex) {
        }
    }

    private static BufferedImage quentizationOperation(BufferedImage img) {
// new quantization();
        int w = bufferedImage1.getWidth();
        int h = bufferedImage1.getHeight();

        int[][] d = new int[h][w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                d[y][x] = bufferedImage1.getRGB(x, y);
            }
        }

        int w2 = bufferedImage2.getWidth();
        int h2 = bufferedImage2.getHeight();

        int[][] d2 = new int[h2][w2];

        for (int y2 = 50; y2 < h2 / 2; y2++) {
            for (int x2 = 50; x2 < w2 / 2; x2++) {
                d2[y2][x2] = bufferedImage2.getRGB(x2, y2);
            }
        }

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, d[y][x] + d2[y][x]);

            }
        }
//        System.out.println("Color.HSBtoRGB(255, 255, 255) " + Color.HSBtoRGB(0, 0, 0));
//        System.out.println(Color.BLACK.getRGB());
//        System.out.println(Color.WHITE.getRGB());

        return img;
    }

    public static void main(String[] args) throws IOException {

        new quantization();

        JFrame frame = new JFrame("Test");
        frame.setSize(bufferedImage1.getWidth(), bufferedImage1.getHeight());
        frame.setLayout(new GridLayout(1, 2));

        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bufferedImage1, 0, 0, this);
            }
        });
        frame.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(quentizationOperation(bufferedImage2), 0, 0, this);
            }
        });
//         frame.add(new JComponent() {
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                g.drawImage(quentizationOperation(bufferedImage1), 0, 0, this);
//            }
//        });

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

}
