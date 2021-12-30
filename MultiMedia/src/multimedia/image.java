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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Mostafa Aboelnasr
 */
public class image extends javax.swing.JFrame {
//              new javax.swing.ImageIcon(getClass().getResource("/multimedia/src/dema.jpg"))
//    ImageIO.read(getClass().getResource(this.path))

//    private BufferedImage bufferedImage;
    static BufferedImage bufferedImage1, bufferedImage2;

    /**
     * Creates new form image
     *
     * @param g
     */
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

    public static void Cross(BufferedImage bufferedImage) {

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
        System.out.println("h2 = " + h2);
        for (int y = 0; y < h2; y++) {
            for (int x = 0; x < w2; x++) {

                C3 newColor = (d2[y][x]);
//                C3 newColor = (d1[y][x]);
//                C3 newColor = d1[y][x].add(d2[y][x]);
//                result[x][y] = newColor;
                bufferedImage.setRGB(x, y, newColor.toColor().getRGB());
//                result[y][x] = new C3(bufferedImage1.getRGB(x, y));

            }
//            try {
//                Thread.sleep(100);
//                System.out.println("10");
//                jLabel1.setIcon(new javax.swing.ImageIcon(bufferedImage));
//                jLabel1.repaint();
//            } catch (InterruptedException ex) {
//                Thread.currentThread().interrupt();
//            }
//            jLabel1.setIcon(new javax.swing.ImageIcon(bufferedImage)); 

        }
        return bufferedImage;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

//        g.drawImage(Dither(bufferedImage1), 0, 0, this);
    }

    public image() throws FileNotFoundException, IOException, URISyntaxException {
        initComponents();
//        bufferedImage = ImageIO.read(getClass().getResource("/multimedia/src/dema.jpg"));
        try {
            bufferedImage1 = ImageIO.read(getClass().getResource("/multimedia/src/dema.jpg")).getSubimage(100, 100, 300, 300);
            bufferedImage2 = ImageIO.read(getClass().getResource("/multimedia/src/park.jpg")).getSubimage(100, 100, 300, 300);
            jLabel1.setIcon(new javax.swing.ImageIcon(bufferedImage1)); // NOI18N

        } catch (IOException ex) {
            Logger.getLogger(DitherDissolve.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static class TestPane extends JPanel {

        public static final long RUNNING_TIME = 2000;

        private BufferedImage inImage;
        private BufferedImage outImage;

        private float alpha = 0f;
        private long startTime = -1;

        public TestPane() {
            inImage = bufferedImage1;
            outImage = bufferedImage2;

            final Timer timer = new Timer(40, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    } else {

                        long time = System.currentTimeMillis();
                        long duration = time - startTime;
                        if (duration >= RUNNING_TIME) {
                            startTime = -1;
                            ((Timer) e.getSource()).stop();
                            alpha = 0f;
                        } else {
                            alpha = 1f - ((float) duration / (float) RUNNING_TIME);
                        }
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    alpha = 0f;
                    BufferedImage tmp = inImage;
                    inImage = outImage;
                    outImage = tmp;
                    timer.start();
                }

            });
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(
                    Math.max(inImage.getWidth(), outImage.getWidth()),
                    Math.max(inImage.getHeight(), outImage.getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
            int x = (getWidth() - inImage.getWidth()) / 2;
            int y = (getHeight() - inImage.getHeight()) / 2;
            g2d.drawImage(inImage, x, y, this);

            g2d.setComposite(AlphaComposite.SrcOver.derive(1f - alpha));
            x = (getWidth() - outImage.getWidth()) / 2;
            y = (getHeight() - outImage.getHeight()) / 2;
            g2d.drawImage(outImage, x, y, this);
            g2d.dispose();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Dither");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/multimedia/src/dema.jpg"))); // NOI18N
        jLabel1.setVisible(false);

        jButton2.setText("Cross");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jLabel1.setIcon(new javax.swing.ImageIcon(Dither(bufferedImage1))); // NOI18N
//        Dither(bufferedImage1);

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(image.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            try {
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                            }

                            image i = new image();
                            i.setVisible(true);
//                JFrame frame = new JFrame("Testing");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            i.add(new TestPane());
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
                        } catch (IOException ex) {
                            Logger.getLogger(image.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(image.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private static javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
