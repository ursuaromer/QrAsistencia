/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordeqr;

import generadordeqr.DatabaseConnection;
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
import java.io.ByteArrayOutputStream;
import javax.swing.JFileChooser;
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
    private DatabaseConnection data;

    /**
     * Creates new form InterfazderegistrodeQR
     */
    public InterfazderegistrodeQR() {
        setUndecorated(false); // Esto oculta la barra de título
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        new DatabaseConnection();

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
        jPanel1.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        jPanel1.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                // Obtener la localización actual de la ventana
                int thisX = getLocation().x;
                int thisY = getLocation().y;

                // Determinar cuánto se movió el mouse desde el clic inicial
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Mover la ventana
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
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
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        exitBtn = new javax.swing.JPanel();
        exitTxt = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        codigoEstu = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        apellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        dni = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        email1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        carrera = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        ciclos = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        qr = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(43, 77, 106));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setBackground(new java.awt.Color(0, 134, 190));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Guardar imágen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 480, 158, 50));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Dubai", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Registro de Estudiante");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 50, 248, -1));

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

        jButton4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/atras.png"))); // NOI18N
        jButton4.setText("Retroceder");
        jButton4.setContentAreaFilled(false);
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 560, -1, -1));

        jPanel2.setBackground(new java.awt.Color(43, 77, 106));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel2.setAutoscrolls(true);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Código estudiante");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido");

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dni");

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Email");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Carrera");

        carrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESARROLLO DE SISTEMAS DE INFORMACIÓN", "ENFERMERÍA TÉCNICA", "PRODUCCIÓN AGROPECUARIA", "CONTABILIDAD", "MECÁNICA AUTOMOTRIZ", "ADMINISTRACIÓN DE EMPRESAS", "CONSTRUCCIÓN CIVIL", "ELECTRICIDAD INDUSTRIAL", "TECNOLOGÍA DE ALIMENTOS", "GUÍA OFICIAL DE TURISMO" }));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Ciclo");

        ciclos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III", "IV", "V", "VI" }));

        jButton1.setBackground(new java.awt.Color(0, 102, 51));
        jButton1.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Registrar alumno");
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(email1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                            .addComponent(codigoEstu, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(apellido, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(108, 108, 108)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dni, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(carrera, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(47, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ciclos, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codigoEstu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(apellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(carrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(ciclos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(64, 90, -1, -1));

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        qr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(qr, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 290, 274));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("QR CODE");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 316, 168, -1));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Scan this QR code to access student information.");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 311, 39));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 60, 400, 400));

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setForeground(new java.awt.Color(0, 0, 0));
        jButton3.setText("LIMPIAR");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 480, 200, 40));

        jButton5.setBackground(new java.awt.Color(255, 51, 51));
        jButton5.setForeground(new java.awt.Color(0, 0, 0));
        jButton5.setText("RESETEAR IMAGEN");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 480, 150, 50));

        jButton6.setBackground(new java.awt.Color(102, 102, 255));
        jButton6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("ACTUALIZAR DATOS");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 610, 210, 40));

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        /*// Verificar si el JLabel está vacío
        if (qr.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "RELLENAR CAMPOS ANTES DE GUARDAR");
            return; // Salir del método si no hay imagen
        }

        // Obtener la imagen del JLabel
        BufferedImage image = (BufferedImage) ((ImageIcon) qr.getIcon()).getImage();

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
        }*/
        // Verificar si el JLabel está vacío
        // Verificar si el JLabel está vacío
        if (qr.getIcon() == null) {
            JOptionPane.showMessageDialog(this, "RELLENAR CAMPOS ANTES DE GUARDAR");
            return; // Salir del método si no hay imagen
        }

        // Obtener la imagen del JLabel
        BufferedImage image = (BufferedImage) ((ImageIcon) qr.getIcon()).getImage();

        // Obtener el nombre y apellido de la persona
        String nombre = this.nombre.getText().trim();
        String apellido = this.apellido.getText().trim();

        // Crear un JFileChooser para seleccionar la ruta de guardado
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar imagen");
        fileChooser.setSelectedFile(new File(nombre + "_" + apellido + ".png"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG Images", "png"));

        // Mostrar el diálogo de guardar
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Asegurarse de que la extensión sea .png
            if (!selectedFile.getAbsolutePath().endsWith(".png")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".png");
            }

            try {
                ImageIO.write(image, "png", selectedFile);
                JOptionPane.showMessageDialog(this, "Imagen guardada en: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen.");
            }
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

        int carreraId = data.obtenerCarreraId(carrera);
        int cicloId = data.obtenerCicloId(ciclo);

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

        if (carreraId == -1 || cicloId == -1) {
            JOptionPane.showMessageDialog(this, "Error: Carrera o Ciclo no encontrado.");
            return;
        }

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
        // Convertir la imagen a un array de bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] qrCodeBytes = baos.toByteArray();
        DatabaseConnection.guardarEstudiante(codestu, nombre, apellido, dni, mail, carreraId, cicloId, qrCodeBytes);
        // Mostrar la imagen en el JLabel
        ImageIcon icon = new ImageIcon(image);
        qr.setIcon(icon);
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        codigoEstu.setText("");
        nombre.setText("");
        apellido.setText("");
        dni.setText("");
        email1.setText("");




    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        qr.setIcon(null);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        RecuperarDatosalumno dats= new RecuperarDatosalumno();
        dats.setVisible(true);
        dispose();




    }//GEN-LAST:event_jButton6ActionPerformed
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
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel qr;
    // End of variables declaration//GEN-END:variables
}
