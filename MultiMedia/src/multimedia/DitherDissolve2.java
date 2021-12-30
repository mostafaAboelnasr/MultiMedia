/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package multimedia;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Mostafa Aboelnasr
 */
public class DitherDissolve2 {

    public static void main(String[] args) {
        new DitherDissolve2();
    }

    public DitherDissolve2() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public static class TestPane extends JPanel {

        public static final long RUNNING_TIME = 3000;

        private static BufferedImage startImage;
        private static BufferedImage secondImage;

//        private float alpha = 0f;
        private long startTime = -1;
        long time;

        public TestPane() {
            try {
                startImage = ImageIO.read(getClass().getResource("/multimedia/src/dema.jpg")).getSubimage(100, 100, 300, 300);
                secondImage = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
            } catch (IOException exp) {
            }

            final Timer timer = new Timer(20, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    } else {

                        time = System.currentTimeMillis();
                        long duration = time - startTime;
                        if (duration >= RUNNING_TIME) {
                            startTime = -1;
                            ((Timer) e.getSource()).stop();
//                            alpha = 0f;
                            time++;
                        } else {
//                            alpha = 1f - ((float) duration / (float) RUNNING_TIME);
                        }
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
//                    alpha = 0f;
                    BufferedImage tmp = startImage;
                    startImage = secondImage;
                    secondImage = tmp;
                    timer.start();
                }

            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(
                    Math.max(startImage.getWidth(), secondImage.getWidth()),
                    Math.max(startImage.getHeight(), secondImage.getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
//            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
            int x = (getWidth() - startImage.getWidth());
            int y = (getHeight() - startImage.getHeight());
            g2d.drawImage(Dither(startImage, 20), x, y, this);

//            g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
//            x = (getWidth() - secondImage.getWidth());
//            y = (getHeight() - secondImage.getHeight());
//            g2d.drawImage(secondImage, x, y, this);
//            g2d.dispose();
        }

        static BufferedImage Dither(BufferedImage bufferedImage, int time) {

            int w1 = startImage.getWidth();
            int h1 = startImage.getHeight();
            C3[][] d1 = new C3[h1][w1];
            for (int y = 0; y < h1; y++) {
                for (int x = 0; x < w1; x++) {
                    d1[y][x] = new C3(startImage.getRGB(x, y));
                }
            }

            int w2 = secondImage.getWidth();
            int h2 = secondImage.getHeight();
            C3[][] d2 = new C3[h2][w2];
            for (int y = 0; y < h2; y++) {
                for (int x = 0; x < w2; x++) {
                    d2[y][x] = new C3(secondImage.getRGB(x, y));
                }
            }

            C3[][] result = new C3[h2][w2];
//            for (int y = time; y < h2; y++) {
            for (int x = 0; x < w2; x++) {
                for (int i = 0; i < 10; i++) {
                    C3 newColor = d2[time + i][x];
                    bufferedImage.setRGB(x, time, newColor.toColor().getRGB());
                }

            }
//            }
            int i = 0;
            return bufferedImage;
        }

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

}
