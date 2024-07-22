/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MenuInicio;

import Animaciones.BlurEffect;
import Reporte.Reporte;
import generadordeqr.InterfazderegistrodeQR;
import ScannerdeQr.Scaneador;
import Animaciones.Cargando;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import com.jhlabs.image.GaussianFilter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.time.Clock;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import loginproyectoruber.Ingreso;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MenuInicio extends javax.swing.JFrame {

    private Point initialClick;

    private int labelY; // Posición inicial fuera de la pantalla
    private final int LABEL_SPEED = 2; // Velocidad de la animación
    private Timer timer;

    public MenuInicio() {

        setUndecorated(true); // Esto oculta la barra de título
        initComponents();
        //setSize(2000, 1000);
        setLocationRelativeTo(null);
        setResizable(false);
        startLabelAnimation();

        //rsscalelabel.RSScaleLabel.setScaleLabel(jLabel6,"src/imagen/reg.png");
        //rsscalelabel.RSScaleLabel.setScaleLabel(jLabel2, "src/imagen/file.png");

        // Agregar el listener de mouse para mover la ventana
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int newX = thisX + xMoved;
                int newY = thisY + yMoved;

                setLocation(newX, newY);
            }
        });
    }
// Método para reproducir el sonido

    private void startLabelAnimation() {
        // Inicializar la posición Y del JLabel fuera de la pantalla (arriba)
        labelY = -jLabel1.getHeight();
        jLabel1.setLocation(jLabel1.getX(), labelY);

        timer = new Timer(3, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mover el JLabel de arriba hacia abajo (pero mantenerlo en la parte superior de la ventana)
                if (labelY < 20) { // Cambia 50 a la posición Y deseada en la parte superior
                    labelY += LABEL_SPEED;
                    jLabel1.setLocation(jLabel1.getX(), labelY);
                } else {
                    timer.stop();
                }
            }
        });
        timer.start();
    }

    private void mostrarCargando(ActionEvent evt, JFrame nuevaInterfaz) {
        // Crear una imagen del MenuInicio actual
        BufferedImage screenshot = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paint(screenshot.getGraphics());

        // Aplicar efecto de desenfoque
        BufferedImage blurredScreenshot = BlurEffect.applyBlur(screenshot, 10);

        // Crear un JLabel con la imagen desenfocada
        JLabel blurredBackground = new JLabel(new ImageIcon(blurredScreenshot));
        blurredBackground.setBounds(0, 0, getWidth(), getHeight());

        // Agregar el fondo desenfocado al contenido del MenuInicio
        JLayeredPane layeredPane = getLayeredPane();
        layeredPane.add(blurredBackground, JLayeredPane.PALETTE_LAYER);

        Cargando cargando = new Cargando(this);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(2000); // Simula un tiempo de carga
                return null;
            }

            @Override
            protected void done() {
                layeredPane.remove(blurredBackground);
                layeredPane.revalidate();
                layeredPane.repaint();
                cargando.dispose();
                nuevaInterfaz.setVisible(true);
                dispose();
            }
        };
        worker.execute();
        cargando.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1000, 900));
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setForeground(new java.awt.Color(0, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setBackground(new java.awt.Color(102, 0, 255));
        jButton1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/inmigracion.png"))); // NOI18N
        jButton1.setText("REPORTES");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 480, 300, 120));

        jButton2.setBackground(new java.awt.Color(0, 0, 255));
        jButton2.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/codigo-qr.png"))); // NOI18N
        jButton2.setText("SCANEAR ASISTENCIA");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 80, 300, 130));

        jButton3.setBackground(new java.awt.Color(0, 102, 51));
        jButton3.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/registro.png"))); // NOI18N
        jButton3.setText("REGISTRAR ALUMNO");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 280, 300, 130));

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/close.png"))); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 80, 50));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("CONTROL DE ASISTENCIA");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 490, 50));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/hand-mockup-designify.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 640));

        getContentPane().add(jPanel2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //Reporte repor = new Reporte();
        Ingreso ingre = new Ingreso();
        ingre.setVisible(true);
        dispose();
        //mostrarCargando(evt, repor);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        InterfazderegistrodeQR inter = new InterfazderegistrodeQR();
        mostrarCargando(evt, inter);


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        Scaneador sca = new Scaneador();
        //mostrarCargando(evt, sca);
        sca.setVisible(true);
        dispose();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        //CERRAR EL PROGRAMA
        System.exit(0);


    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     *
     *
     *
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuInicio().setVisible(true);
            }

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
