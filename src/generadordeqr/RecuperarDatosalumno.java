/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordeqr;

import MenuInicio.MenuInicio;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import generadordeqr.DatabaseConnection;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author ursua
 */
public class RecuperarDatosalumno extends javax.swing.JFrame {

    private DatabaseConnection data;
    private List<String> qrCodesGenerated = new ArrayList<>();

    public RecuperarDatosalumno() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Actualizacion de datos del estudiante");
        setResizable(false);
    }

    private void limpiarCampos() {
        // Limpiar JTextFields
        codEstudiante.setText("");
        nombreEstudiante.setText("");
        apellidoEstudiante.setText("");
        dniEstudiante.setText("");
        emailEstudiante.setText("");

        // Resetear JComboBoxes
        comboCarrera.setSelectedIndex(0);
        comboCiclo.setSelectedIndex(0);

        // Limpiar el JLabel del QR
        qr.setIcon(null);

        // Si tienes algún otro componente, limpiarlo aquí
        // Opcionalmente, dar foco al primer campo
        codEstudiante.requestFocus();
    }

    //Method for find student
    private void buscarEstudiante() {
        String codigoODni = codEstudiante.getText().trim();
        if (codigoODni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un código o DNI para buscar.");
            return;
        }

        Map<String, Object> estudianteData = DatabaseConnection.buscarEstudiante(codigoODni);

        if (estudianteData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ningún estudiante con ese código o DNI.");
            return;
        }

        // Actualizar los campos de texto
        codEstudiante.setText((String) estudianteData.get("codigo"));
        nombreEstudiante.setText((String) estudianteData.get("nombre"));
        apellidoEstudiante.setText((String) estudianteData.get("apellido"));
        dniEstudiante.setText((String) estudianteData.get("dni"));
        emailEstudiante.setText((String) estudianteData.get("email"));

        // Actualizar los JComboBox
        comboCarrera.setSelectedItem(estudianteData.get("carrera"));
        comboCiclo.setSelectedItem(estudianteData.get("ciclo"));

        // Mostrar el QR
        byte[] qrCodeBytes = (byte[]) estudianteData.get("qr_code");
        if (qrCodeBytes != null) {
            ImageIcon icon = new ImageIcon(qrCodeBytes);
            qr.setIcon(icon);
        } else {
            qr.setIcon(null);
        }
    }

    private void guardarQR() {
        Icon icon = qr.getIcon();
        if (icon == null) {
            JOptionPane.showMessageDialog(this, "No hay QR para guardar.");
            return;
        }

        BufferedImage bufferedImage = iconToBufferedImage(icon);
        if (bufferedImage == null) {
            JOptionPane.showMessageDialog(this, "No se pudo procesar la imagen del QR.");
            return;
        }

        // Obtener el nombre y apellido del estudiante
        String nombre = nombreEstudiante.getText().trim();
        String apellido = apellidoEstudiante.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre o apellido del estudiante no disponible.");
            return;
        }

        // Crear el nombre del archivo
        String fileName = nombre + "_" + apellido + "_QR.png";
        // Reemplazar caracteres no válidos para nombres de archivo
        fileName = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar QR");
        fileChooser.setSelectedFile(new File(fileName));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".png")) {
                filePath += ".png";
            }

            try {
                ImageIO.write(bufferedImage, "png", new File(filePath));
                JOptionPane.showMessageDialog(this, "QR guardado exitosamente como: " + fileToSave.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el QR: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private BufferedImage iconToBufferedImage(Icon icon) {
        if (icon instanceof ImageIcon) {
            Image image = ((ImageIcon) icon).getImage();
            BufferedImage bimage = new BufferedImage(
                    image.getWidth(null),
                    image.getHeight(null),
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();

            return bimage;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        comboCarrera = new javax.swing.JComboBox<>();
        comboCiclo = new javax.swing.JComboBox<>();
        dniEstudiante = new javax.swing.JTextField();
        codEstudiante = new javax.swing.JTextField();
        nombreEstudiante = new javax.swing.JTextField();
        apellidoEstudiante = new javax.swing.JTextField();
        emailEstudiante = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        qr = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(43, 77, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Dubai", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("ACTUALIZACIÓN DE DATOS DE ALUMNOS ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 31;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 167, 0, 0);
        jPanel1.add(jLabel9, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nombre");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Apellido");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Dni");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 60, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Email");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 60, 0, 0);
        jPanel1.add(jLabel2, gridBagConstraints);

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Carrera");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 60, 0, 0);
        jPanel1.add(jLabel7, gridBagConstraints);

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Ciclo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(38, 60, 0, 0);
        jPanel1.add(jLabel6, gridBagConstraints);

        comboCarrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESARROLLO DE SISTEMAS DE INFORMACIÓN", "ENFERMERÍA TÉCNICA", "PRODUCCIÓN AGROPECUARIA", "CONTABILIDAD", "MECÁNICA AUTOMOTRIZ", "ADMINISTRACIÓN DE EMPRESAS", "CONSTRUCCIÓN CIVIL", "ELECTRICIDAD INDUSTRIAL", "TECNOLOGÍA DE ALIMENTOS", "GUÍA OFICIAL DE TURISMO" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 0, 0);
        jPanel1.add(comboCarrera, gridBagConstraints);

        comboCiclo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III", "IV", "V", "VI" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 249;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(38, 24, 0, 0);
        jPanel1.add(comboCiclo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 286;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 67, 0, 0);
        jPanel1.add(dniEstudiante, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 286;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 67, 0, 0);
        jPanel1.add(codEstudiante, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 286;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 67, 0, 0);
        jPanel1.add(nombreEstudiante, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 286;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 67, 0, 0);
        jPanel1.add(apellidoEstudiante, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 19;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 286;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 67, 0, 0);
        jPanel1.add(emailEstudiante, gridBagConstraints);

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Dubai", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Código estudiante");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 60, 0, 0);
        jPanel1.add(jLabel10, gridBagConstraints);

        qr.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 49;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 32;
        gridBagConstraints.gridheight = 12;
        gridBagConstraints.ipadx = 308;
        gridBagConstraints.ipady = 298;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 50, 0, 112);
        jPanel1.add(qr, gridBagConstraints);

        jButton2.setBackground(new java.awt.Color(0, 134, 190));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Guardar imágen");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 79;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 49;
        gridBagConstraints.ipady = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 20, 0, 0);
        jPanel1.add(jButton2, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(51, 255, 51));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ACTUALIZAR DATOS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 33;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 100, 107, 0);
        jPanel1.add(jButton1, gridBagConstraints);

        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 46;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 30, 0, 0);
        jPanel1.add(jButton3, gridBagConstraints);

        jButton4.setText("Limpiar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 46;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 30, 0, 0);
        jPanel1.add(jButton4, gridBagConstraints);

        jButton5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/atras.png"))); // NOI18N
        jButton5.setText("REGRESAR");
        jButton5.setContentAreaFilled(false);
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(jButton5, gridBagConstraints);

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        guardarQR();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// Obtener los datos desde los JTextField correspondientes
        String codestu = codEstudiante.getText().trim();
        String nombre = this.nombreEstudiante.getText().trim();
        String apellido = this.apellidoEstudiante.getText().trim();
        String dni = this.dniEstudiante.getText().trim();
        String mail = this.emailEstudiante.getText().trim();
        String carrera = this.comboCarrera.getSelectedItem().toString().trim();
        String ciclo = this.comboCiclo.getSelectedItem().toString().trim();
        int carreraId = DatabaseConnection.obtenerCarreraId(carrera);
        int cicloId = DatabaseConnection.obtenerCicloId(ciclo);

        if (codestu.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || mail.isEmpty() || carrera.isEmpty() || ciclo.isEmpty()) {
            // Mostrar un mensaje de error si algún campo está vacío
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.");
            return;
        }

// Verificar si el estudiante existe
        if (!DatabaseConnection.verificarEstudiante(codestu)) {
            JOptionPane.showMessageDialog(this, "El estudiante con código " + codestu + " no existe.");
            return;
        }

        if (carreraId == -1 || cicloId == -1) {
            JOptionPane.showMessageDialog(this, "Error: Carrera o Ciclo no encontrado.");
            return;
        }

// Obtener la fecha y hora actual
        Date fecha = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = formatoFecha.format(fecha);
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        String horaStr = formatoHora.format(fecha);

// Concatenar todos los datos en una sola cadena
        String texto = "CODIGO ESTUDIANTE: " + codestu + "\n"
                + "NOMBRES: " + nombre + "\n"
                + "APELLIDOS: " + apellido + "\n"
                + "DNI: " + dni + "\n"
                + "EMAIL: " + mail + "\n"
                + "CARRERA :" + carrera + "\n"
                + "CICLO:" + ciclo + "\n"
                + "FECHA DE ACTUALIZACIÓN: " + fechaStr + "\n"
                + "HORA DE ACTUALIZACIÓN: " + horaStr;

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

// Actualizar el estudiante en la base de datos
        boolean actualizado = DatabaseConnection.actualizarEstudiante(codestu, nombre, apellido, dni, mail, carreraId, cicloId, qrCodeBytes);

        if (actualizado) {
            // Mostrar la imagen en el JLabel
            ImageIcon icon = new ImageIcon(image);
            qr.setIcon(icon);

            // Actualizar la lista de QR generados si es necesario
            qrCodesGenerated.remove(codestu + nombre + apellido + dni + mail + carrera); // Eliminar el antiguo
            qrCodesGenerated.add(codestu + nombre + apellido + dni + mail + carrera); // Añadir el nuevo

            JOptionPane.showMessageDialog(this, "Estudiante actualizado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el estudiante. Por favor, intente de nuevo.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        buscarEstudiante();


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        limpiarCampos();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        MenuInicio menu = new MenuInicio();
        menu.setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(RecuperarDatosalumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecuperarDatosalumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecuperarDatosalumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecuperarDatosalumno.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecuperarDatosalumno().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoEstudiante;
    private javax.swing.JTextField codEstudiante;
    private javax.swing.JComboBox<String> comboCarrera;
    private javax.swing.JComboBox<String> comboCiclo;
    private javax.swing.JTextField dniEstudiante;
    private javax.swing.JTextField emailEstudiante;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField nombreEstudiante;
    private javax.swing.JLabel qr;
    // End of variables declaration//GEN-END:variables
}
