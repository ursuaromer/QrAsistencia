/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordeqr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import MenuInicio.MenuInicio;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author ursua
 */
public class InterfazderegistrodeQR extends javax.swing.JFrame {

    private Point initialClick;
    private List<String> qrCodesGenerated = new ArrayList<>();
    private Timer emailValidationTimer;

    /**
     * Creates new form InterfazderegistrodeQR
     */
    public InterfazderegistrodeQR() {
        setUndecorated(true); // Esto oculta la barra de título
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);

        jButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton1.setBackground(new Color(0, 102, 51));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton1.setBackground(new Color(0, 102, 51));
            }
        });

        jButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton2.setBackground(new Color(0, 134, 190));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton2.setBackground(new Color(0, 134, 190));
            }
        });

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

        // Configuración del DocumentListener para cada campo
        apellido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarNombreApellido(apellido.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                validarNombreApellido(apellido.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                validarNombreApellido(apellido.getText());
            }
        });

        nombre.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarNombreApellido(nombre.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                validarNombreApellido(nombre.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                validarNombreApellido(nombre.getText());
            }
        });

        dni.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarDNI(dni.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                validarDNI(dni.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                validarDNI(dni.getText());
            }
        });

        emailValidationTimer = new Timer(1000, e -> validarEmail(email1.getText()));
        emailValidationTimer.setRepeats(false);
        email1.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                emailValidationTimer.restart();
            }

            public void removeUpdate(DocumentEvent e) {
                emailValidationTimer.restart();
            }

            public void changedUpdate(DocumentEvent e) {
                emailValidationTimer.restart();
            }
        });

        codigoEstu.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarCodigoEstu(codigoEstu.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                validarCodigoEstu(codigoEstu.getText());
            }

            public void changedUpdate(DocumentEvent e) {
                validarCodigoEstu(codigoEstu.getText());
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        apellido = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        dni = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        email1 = new javax.swing.JTextField();
        carrera = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        ciclos = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        codigoEstu = new javax.swing.JTextField();
        exitBtn = new javax.swing.JPanel();
        exitTxt = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setAutoscrolls(true);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("DNI");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, 153, -1));
        jPanel1.add(apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 260, -1));
        jPanel1.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 260, -1));

        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 290, 274));
        jPanel1.add(dni, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 260, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("EMAIL");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 100, -1));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("NOMBRE");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 153, -1));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("APELLIDO");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 110, -1));
        jPanel1.add(email1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 260, -1));

        carrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESARROLLO DE SISTEMAS DE INFORMACIÓN" }));
        jPanel1.add(carrera, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 260, -1));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CICLO");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 153, -1));

        jButton2.setBackground(new java.awt.Color(0, 134, 190));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("GUARDAR IMAGEN");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 290, 170, 50));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CARRERA");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 153, -1));

        ciclos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III", "IV", "V", "VI" }));
        jPanel1.add(ciclos, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 290, 150, -1));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CODIGO ESTUDIANTE");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 220, -1));
        jPanel1.add(codigoEstu, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 260, -1));

        exitBtn.setBackground(new java.awt.Color(255, 255, 255));

        exitTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        exitTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        exitTxt.setText("X");
        exitTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        exitTxt.setPreferredSize(new java.awt.Dimension(40, 40));
        exitTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exitTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                exitTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                exitTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout exitBtnLayout = new javax.swing.GroupLayout(exitBtn);
        exitBtn.setLayout(exitBtnLayout);
        exitBtnLayout.setHorizontalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exitBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        exitBtnLayout.setVerticalGroup(
            exitBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, exitBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(exitTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(exitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jButton1.setBackground(new java.awt.Color(0, 102, 51));
        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("REGISTRAR ALUMNO");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton1MouseEntered(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, 200, 50));

        jButton4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/atras.png"))); // NOI18N
        jButton4.setText("RETROCESO");
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 360, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 995, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Verificar si el JLabel está vacío
        if (jLabel1.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "RELLENAR CAMPOS ANTES DE GUARDAR");
            return; // Salir del método si no hay imagen
        }

        // Obtener la imagen del JLabel
        BufferedImage image = (BufferedImage) ((ImageIcon) jLabel1.getIcon()).getImage();

        // Obtener el nombre y apellido de la persona
        String nombre = this.nombre.getText().trim();
        String apellido = this.apellido.getText().trim();
        //GURADAR IMAGEN SI LOS JTEX ESTAN VACIOS

        // Guardar la imagen en el escritorio dentro de una carpeta llamada "QR"
        String userHome = System.getProperty("user.home");
        File directory = new File(userHome + "\\Desktop\\QR");
        if (!directory.exists()) {
            directory.mkdir();
        }

        String fileName = nombre + "_" + apellido + ".png";
        File file = new File(directory, fileName);
        try {

            ImageIO.write(image, "png", file);
            JOptionPane.showMessageDialog(this, "Imagen guardada con el nombre: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar la imagen.");
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void exitTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseClicked
        System.exit(0);
    }//GEN-LAST:event_exitTxtMouseClicked

    private void exitTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseEntered
        exitBtn.setBackground(Color.red);
        exitTxt.setForeground(Color.white);
    }//GEN-LAST:event_exitTxtMouseEntered

    private void exitTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exitTxtMouseExited
        exitBtn.setBackground(Color.white);
        exitTxt.setForeground(Color.black);
    }//GEN-LAST:event_exitTxtMouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// Obtener los datos desde los JTextField correspondientes
        String codestu = codigoEstu.getText().trim();
        String nombre = this.nombre.getText().trim();
        String apellido = this.apellido.getText().trim();
        String dni = this.dni.getText().trim();
        String mail = this.email1.getText().trim();
        String carrera = this.carrera.getSelectedItem().toString().trim();
        String ciclo = this.ciclos.getSelectedItem().toString().trim();

        if (codestu.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || mail.isEmpty() || carrera.isEmpty() || ciclo.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }
        // Obtener la fecha y hora actual
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = formatoFecha.format(fecha);

        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        String horaStr = formatoHora.format(fecha);

        // Concatenar todos los datos en una sola cadena
        String texto
                = "CODIGO ESTUDIANTE: " + codestu + "\n"
                + "NOMBRES: " + nombre + "\n"
                + "APELLIDOS: " + apellido + "\n"
                + "DNI: " + dni + "\n"
                + "EMAIL: " + mail + "\n"
                + "CARRERA :" + carrera + "\n"
                + "CICLO:" + ciclo + "\n"
                + "FECHA: " + fechaStr + "\n"
                + "HORA: " + horaStr;

        // Crear el código QR
        BitMatrix bitMatrix;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 300, 300);
        } catch (WriterException e) {
            e.printStackTrace();
            return;
        }

        // Convertir el BitMatrix a una imagen BufferedImage
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        // Mostrar la imagen en el JLabel
        ImageIcon icon = new ImageIcon(image);
        jLabel1.setIcon(icon);
        // Agregar el QR generado a la lista de QR generados
        qrCodesGenerated.add(codestu + nombre + apellido + dni + mail + carrera);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseEntered

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        MenuInicio menu = new MenuInicio();
        menu.setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton4ActionPerformed
    private void validarDNI(String text) {
        if (!text.matches("\\d*")) { // Solo números
            JOptionPane.showMessageDialog(this, "Ingrese solo números en el campo DNI.");
        }
    }

    private boolean esEmailValido(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }

// En jButton1ActionPerformed:
    private void validarEmail(String text) {
        if (text.isEmpty() || !text.contains("@")) {
            return; // No validamos si está vacío o no tiene @
        }

        String[] partes = text.split("@");
        if (partes.length <= 1 || partes[1].isEmpty()) {
            return; // No hay nada después del @ o está escribiendo justo después del @
        }

        String dominio = partes[1];
        StringBuilder mensaje = new StringBuilder();

        // Solo validamos si ya se ha escrito algo después del @
        if (dominio.length() > 3) {
            if (!dominio.contains(".")) {
                // No mostramos mensaje, el usuario podría estar aún escribiendo
                return;
            } else {
                String[] partesDominio = dominio.split("\\.");
                if (partesDominio.length > 1 && !partesDominio[1].isEmpty()) {
                    String extension = partesDominio[1];
                    // Solo validamos la extensión si tiene 2 o más caracteres
                    if (extension.length() >= 2
                            && !extension.equalsIgnoreCase("com")
                            && !extension.equalsIgnoreCase("org")
                            && !extension.equalsIgnoreCase("net")) {
                        mensaje.append("Extensión de dominio no común. Las más comunes son .com, .org, .net\n");
                    }
                }
            }

            // Verificar uso incorrecto de coma
            if (dominio.contains(",")) {
                mensaje.append("Uso incorrecto de coma en el dominio. Use punto.\n");
            }
        }

        // Mostrar mensaje si hay errores
        if (mensaje.length() > 0) {
            JOptionPane.showMessageDialog(null, mensaje.toString(), "Sugerencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void validarNombreApellido(String text) {
        if (!text.matches("[a-zA-Z ]*")) { // Solo letras y espacios
            JOptionPane.showMessageDialog(this, "Ingrese solo letras en el campo de Nombre/Apellido.");
        }
    }

    private void validarCodigoEstu(String text) {
        if (!text.matches("\\d*")) { // Solo números
            JOptionPane.showMessageDialog(this, "Ingrese solo números en el campo Código Estudiante.");
        }
    }

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
            java.util.logging.Logger.getLogger(InterfazderegistrodeQR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazderegistrodeQR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazderegistrodeQR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazderegistrodeQR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazderegistrodeQR().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellido;
    private javax.swing.JComboBox<String> carrera;
    private javax.swing.JComboBox<String> ciclos;
    private javax.swing.JTextField codigoEstu;
    private javax.swing.JTextField dni;
    private javax.swing.JTextField email1;
    private javax.swing.JPanel exitBtn;
    private javax.swing.JLabel exitTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nombre;
    // End of variables declaration//GEN-END:variables
}
