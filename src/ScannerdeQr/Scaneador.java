package ScannerdeQr;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.Dimension;
import java.util.Arrays;
import MenuInicio.MenuInicio;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import java.io.IOException;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.awt.Frame;
import generadordeqr.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;

public class Scaneador extends javax.swing.JFrame {

    private String lastProcessedQR = "";
    private long lastProcessTime = 0;
    private static final long PROCESS_COOLDOWN = 3000; // 500 ms cooldown
    private static final long SCAN_INTERVAL = 500;
    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private ScheduledExecutorService executor;
    private String lastScannedQR = "";
    private Map<String, Integer> cursoIdMap = new HashMap<>();

    public Scaneador() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("SCANEADOR");
        playMP3("/Sonidos/piti.mp3");  // Nota la barra al principio
        initWebcam();
        startScan();
        setFullscreen();
        // Añade esta línea para inicializar las opciones del curso
        updateCursoOptions((String) ciclo.getSelectedItem());

        //rsscalelabel.RSScaleLabel.setScaleLabel(jLabel2,"src/imagen/escanear.png");
    }

    //MOSTRAR UN MENSAJE(VENTA EMEGENET QUE SALE CUANDO ESCANEA ,ADVIERTE,O NULA)
    private void showTemporaryMessage(String message, int messageType) {
        JDialog dialog = new JDialog(this);
        dialog.setUndecorated(true);

        JLabel label = new JLabel(message);
        // Usar el método setFont sin crear un nuevo objeto Font
        label.setFont(label.getFont().deriveFont(javax.swing.UIManager.getFont("Label.font").getStyle() | java.awt.Font.BOLD, 18f));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        java.awt.Color backgroundColor;
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                backgroundColor = new java.awt.Color(255, 200, 200);
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                backgroundColor = new java.awt.Color(200, 255, 200);
                break;
            default:
                backgroundColor = new java.awt.Color(255, 255, 200);
        }

        label.setOpaque(true);
        label.setBackground(backgroundColor);

        dialog.add(label);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        new javax.swing.Timer(1000, e -> {
            dialog.dispose();
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }

    //REPRODUCIR AUDIO
    private void playMP3(String mp3File) {
        try {
            System.out.println("Intentando reproducir: " + mp3File);
            URL resource = getClass().getResource(mp3File);
            if (resource == null) {
                System.out.println("No se pudo encontrar el archivo de sonido: " + mp3File);
                return;
            }
            System.out.println("Recurso encontrado: " + resource);

            String path = resource.toURI().toString();
            System.out.println("Path del archivo: " + path);
            Media media = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                System.out.println("Audio listo para reproducir");
                mediaPlayer.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                System.out.println("Reproducción finalizada");
                mediaPlayer.dispose();
            });

            mediaPlayer.setOnError(() -> {
                System.out.println("Error en la reproducción: " + mediaPlayer.getError());
            });

        } catch (Exception e) {
            System.out.println("Error al configurar la reproducción: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //GENERA EL PDF 
    private String generatePDF(String[] data, String formattedDate, String formattedTime) {
        String fileName = "asistencia_" + System.currentTimeMillis() + ".pdf";
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Añadir un encabezado
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph header = new Paragraph("Registro de Asistencia", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            header.setSpacingAfter(20);
            document.add(header);

            // Crear la tabla
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(90);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Estilo para las celdas de encabezado
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(new BaseColor(240, 240, 240));
            headerCell.setPadding(5);
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Estilo para las celdas de contenido
            PdfPCell contentCell = new PdfPCell();
            contentCell.setPadding(5);
            contentCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // Fuentes
            Font fieldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11);

            String[] fields = {
                "Código de Estudiante", "Nombre", "Apellido", "DNI",
                "Carrera", "Ciclo", "Curso", "Fecha de registro", "Hora de registro"
            };

            for (int i = 0; i < fields.length; i++) {
                headerCell.setPhrase(new Phrase(fields[i], fieldFont));
                table.addCell(headerCell);

                String value = "";
                switch (i) {
                    case 0: // Código de Estudiante
                    case 1: // Nombre
                    case 2: // Apellido
                    case 3: // DNI
                        value = data[i];
                        break;
                    case 4: // Carrera
                        value = data[5]; // Asumiendo que la carrera está en el índice 5
                        break;
                    case 5: // Ciclo
                        value = data[6]; // Asumiendo que el ciclo está en el índice 6
                        break;
                    case 6: // Curso
                        value = data[7]; // Asumiendo que el curso está en el índice 7
                        break;
                    case 7: // Fecha de registro
                        value = formattedDate;
                        break;
                    case 8: // Hora de registro
                        value = formattedTime;
                        break;
                }
                contentCell.setPhrase(new Phrase(value, valueFont));
                table.addCell(contentCell);
            }

            document.add(table);

            // Añadir un pie de página
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Este documento es una confirmación oficial de asistencia.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            System.out.println("PDF generado: " + fileName);
            return new File(fileName).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al generar el PDF: " + e.getMessage());
            return null;
        }
    }

    //ENVIA AL CORREO DE LA PERSONA
    private void sendEmail(String toEmail, String[] data) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        final String username = "qrsuizacorporationlasbqr@gmail.com";
        final String password = "w l k t e m b h d q h r z e t p";
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Confirmación de Asistencia");

            // Obtener la fecha y hora actual
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = currentDate.format(dateFormatter);

            LocalTime currentTime = LocalTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(timeFormatter);

            String emailContent = "Se ha registrado su asistencia. Por favor, revise el archivo PDF adjunto para más detalles.";

            // Generar el PDF
            String pdfFileName = generatePDF(data, formattedDate, formattedTime);

            // Crear el mensaje multipart
            Multipart multipart = new MimeMultipart();

            // Parte del texto del mensaje
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(emailContent);
            multipart.addBodyPart(textPart);

            // Parte del archivo adjunto
            if (pdfFileName != null) {
                MimeBodyPart pdfPart = new MimeBodyPart();
                FileDataSource source = new FileDataSource(pdfFileName);
                pdfPart.setDataHandler(new DataHandler(source));
                pdfPart.setFileName("Confirmacion_Asistencia.pdf");
                multipart.addBodyPart(pdfPart);
            }

            // Establecer el contenido del mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);
            System.out.println("Correo enviado exitosamente a " + toEmail);

            // Borrar el archivo PDF temporal
            if (pdfFileName != null) {
                File pdfFile = new File(pdfFileName);
                if (pdfFile.exists()) {
                    if (pdfFile.delete()) {
                        System.out.println("Archivo PDF temporal eliminado: " + pdfFileName);
                    } else {
                        System.out.println("No se pudo eliminar el archivo PDF temporal: " + pdfFileName);
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    private void playSuccessSound() {
        try {
            // Parámetros del tono
            int sampleRate = 44100;  // 44100 muestras por segundo
            double durationSeconds = 0.1;  // Duración del tono en segundos
            double frequency = 1000;  // Frecuencia del tono en Hz

            // Generar el tono
            byte[] buf = new byte[(int) (sampleRate * durationSeconds)];
            for (int i = 0; i < buf.length; i++) {
                double angle = i / (sampleRate / frequency) * 2.0 * Math.PI;
                buf[i] = (byte) (Math.sin(angle) * 127.0 * 0.5);
            }

            // Configurar el formato de audio
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);

            // Crear y reproducir el sonido
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            sdl.write(buf, 0, buf.length);
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (Exception e) {
            System.out.println("Error al reproducir el sonido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //INICIA LA CAMARA DE SCANEO
    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam == null) {
            System.out.println("No se detectó ninguna cámara.");
            return;
        }

        // Establecer la resolución de la cámara a HD (1280x720)
        webcam.setViewSize(WebcamResolution.QVGA.getSize());

        webcamPanel = new WebcamPanel(webcam);
        webcamPanel.setFPSDisplayed(true);
        webcamPanel.setDisplayDebugInfo(true);
        webcamPanel.setImageSizeDisplayed(true);
        webcamPanel.setMirrored(true);

        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(webcamPanel, BorderLayout.CENTER);
        jPanel2.revalidate();
        jPanel2.repaint();

        System.out.println("Cámara iniciada con resolución: " + webcam.getViewSize().width + "x" + webcam.getViewSize().height);

        // Asegurarse de que la cámara esté abierta
        if (!webcam.isOpen()) {
            webcam.open();
        }
    }

    //COMIENZA EL SCAENO
    private void startScan() {
        if (webcam != null) {
            if (!webcam.isOpen()) {
                webcam.open();
            }

            if (webcam.isOpen()) {
                executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(this::scanQR, 0, 500, TimeUnit.MILLISECONDS);
                System.out.println("Escaneo iniciado.");
            } else {
                System.out.println("No se pudo iniciar el escaneo. La cámara no está disponible.");
            }
        } else {
            System.out.println("No se pudo iniciar el escaneo. La cámara no está inicializada.");
        }
    }

    //SCANEER
    private void scanQR() {
        if (webcam.isOpen()) {
            try {
                BufferedImage image = webcam.getImage();
                if (image != null) {
                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    Result result = new MultiFormatReader().decode(bitmap);

                    if (result != null) {
                        String scannedText = result.getText();
                        long currentTime = System.currentTimeMillis();

                        // Verificar si es un nuevo QR o si ha pasado suficiente tiempo desde el último procesamiento
                        if (!scannedText.equals(lastProcessedQR) || (currentTime - lastProcessTime) > PROCESS_COOLDOWN) {
                            lastProcessedQR = scannedText;
                            lastProcessTime = currentTime;

                            processQRCode(scannedText);
                        }
                    }
                }
            } catch (NotFoundException ignored) {
                // No QR code found in this frame
            } catch (Exception e) {
                System.out.println("Error al escanear: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("La cámara no está abierta. Intentando abrir...");
            webcam.open();
        }
    }

    /*private void processQRCode(String scannedText) {
        // Procesar el código QR en un hilo separado
        new Thread(() -> {
            String[] data = parseQRData(scannedText);
            if (data != null) {
                SwingUtilities.invokeLater(() -> showData(data));
                playSuccessSound();
                sendEmailAsync(data[4], data);
            }
        }).start();
    }*/
    private class CursoItem {

        private int id;
        private String nombre;
        private int cicloId;

        public CursoItem(int id, String nombre, int cicloId) {
            this.id = id;
            this.nombre = nombre;
            this.cicloId = cicloId;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCicloId() {
            return cicloId;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
    //PROCESA EL QR 

    private void processQRCode(String scannedText) {
        new Thread(() -> {
            System.out.println("Procesando código QR: " + scannedText);
            String[] data = parseQRData(scannedText);
            if (data != null && data.length >= 1) {
                String codigoEstudiante = data[0];
                String cicloSeleccionado = (String) ciclo.getSelectedItem();
                String cursoSeleccionado = (String) curso.getSelectedItem();

                System.out.println("Código estudiante: " + codigoEstudiante);
                System.out.println("Ciclo seleccionado: " + cicloSeleccionado);
                System.out.println("Curso seleccionado: " + cursoSeleccionado);

                Integer cursoId = cursoIdMap.get(cursoSeleccionado);
                if (cursoId == null) {
                    System.out.println("Error: No se encontró ID para el curso " + cursoSeleccionado);
                    SwingUtilities.invokeLater(()
                            -> showTemporaryMessage("Curso no válido seleccionado", JOptionPane.ERROR_MESSAGE)
                    );
                    return;
                }
                System.out.println("ID del curso: " + cursoId);

                String[] datosEstudiante = DatabaseConnection.obtenerDatosEstudiante(codigoEstudiante);
                if (datosEstudiante != null && datosEstudiante.length >= 7) {
                    String cicloEstudiante = datosEstudiante[6]; // Asumiendo que el ciclo está en el índice 6
                    System.out.println("Ciclo del estudiante: " + cicloEstudiante);

                    if (!cicloEstudiante.equals(cicloSeleccionado)) {
                        System.out.println("Error: Ciclo no coincide");
                        SwingUtilities.invokeLater(()
                                -> showTemporaryMessage("El ciclo del estudiante (" + cicloEstudiante
                                        + ") no coincide con el ciclo seleccionado ("
                                        + cicloSeleccionado + ")", JOptionPane.ERROR_MESSAGE)
                        );
                        return;
                    }

                    boolean asistenciaRegistrada = DatabaseConnection.registrarAsistencia(codigoEstudiante, cursoId);
                    System.out.println("Resultado de registrarAsistencia: " + asistenciaRegistrada);
                    if (asistenciaRegistrada) {
                        SwingUtilities.invokeLater(() -> {
                            playSuccessSound();
                            showTemporaryMessage("ASISTENCIA GUARDADA", JOptionPane.INFORMATION_MESSAGE);
                        });

                        // Preparar datos para PDF y email
                        LocalDateTime now = LocalDateTime.now();
                        String formattedDate = now.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                        String[] datosCompletos = new String[datosEstudiante.length + 3];
                        System.arraycopy(datosEstudiante, 0, datosCompletos, 0, datosEstudiante.length);
                        datosCompletos[datosCompletos.length - 3] = cursoSeleccionado;
                        datosCompletos[datosCompletos.length - 2] = formattedDate;
                        datosCompletos[datosCompletos.length - 1] = formattedTime;

                        String pdfPath = generatePDF(datosCompletos, formattedDate, formattedTime);
                        if (pdfPath != null) {
                            sendEmailAsync(datosEstudiante[4], datosCompletos, pdfPath);
                        } else {
                            System.out.println("Error al generar el PDF");
                        }
                    } else {
                        SwingUtilities.invokeLater(()
                                -> showTemporaryMessage("YA SE REGISTRÓ TU ASISTENCIA", JOptionPane.INFORMATION_MESSAGE)
                        );
                    }
                } else {
                    System.out.println("Error: Datos del estudiante no encontrados o incompletos");
                    SwingUtilities.invokeLater(()
                            -> showTemporaryMessage("Estudiante no encontrado o datos incompletos", JOptionPane.ERROR_MESSAGE)
                    );
                }
            } else {
                System.out.println("Error: Datos del QR inválidos o incompletos");
            }
        }).start();
    }

    //PARSEA EL QR
    private String[] parseQRData(String scannedText) {
        String[] lines = scannedText.split("\n");
        if (lines.length >= 7) {
            String[] data = new String[9];
            for (int i = 0; i < 7; i++) {
                String[] parts = lines[i].split(": ", 2);
                if (parts.length == 2) {
                    data[i] = parts[1];
                }
            }
            // Procesar la carrera separadamente
            if (lines.length > 6) {
                String[] carreraParts = lines[5].split(":", 2);
                if (carreraParts.length == 2) {
                    data[5] = carreraParts[1].trim();
                }
                String[] cicloParts = lines[6].split(":", 2);
                if (cicloParts.length == 2) {
                    data[6] = cicloParts[1].trim();
                }
            }
            data[7] = (String) curso.getSelectedItem().toString();
            return data;
        }
        return null;
    }

    //ENVIE EL QR
    private void sendEmailAsync(String toEmail, String[] data, String pdfPath) {
        new Thread(() -> {
            sendEmail(toEmail, data);
        }).start();
    }

    //EMPTY
    private void showData(String[] data) {
    }

    //ABRE 
    private void setFullscreen() {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        device.setFullScreenWindow(this);
    }

    private void updateCursoOptions(String selectedCiclo) {
        curso.removeAllItems();
        cursoIdMap.clear();
        String sql = "SELECT c.id, c.nombre FROM Curso c "
                + "JOIN Ciclo ci ON c.ciclo_id = ci.id "
                + "WHERE ci.nombre = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selectedCiclo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int cursoId = rs.getInt("id");
                String cursoNombre = rs.getString("nombre");
                curso.addItem(cursoNombre);
                cursoIdMap.put(cursoNombre, cursoId);
            }

            if (curso.getItemCount() == 0) {
                curso.addItem("NO HAY CURSOS DISPONIBLES PARA ESTE CICLO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos: " + e.getMessage());
            curso.addItem("ERROR AL CARGAR CURSOS");
        }
    }

    /* private void resetDataAfterDelay() {
        Timer timer = new Timer(5000, e -> {
            jTextField1.setText("");
            jTextField2.setText("");
            jTextField3.setText("");
            jTextField4.setText("");
            jTextField5.setText("");
            jTextField6.setText("");

            lastScannedQR = "";
        });
        timer.setRepeats(false);
        timer.start();
    }*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        curso = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        ciclo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(0, 255, 255));
        jPanel1.setAutoscrolls(true);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.ipady = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 190, 0, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/atras.png"))); // NOI18N
        jButton2.setText("RETROCESO");
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 43;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 29, 38, 0);
        jPanel1.add(jButton2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/inmigracion.png"))); // NOI18N
        jLabel3.setText("QR SCANNER");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jLabel3, gridBagConstraints);

        curso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INGLES PARA LA COMUNICACION ORAL", "PROGRAMACION DISTRIBUIDA", "PROGRAMACION CONCURRENTE", "PROGRAMACION ORIENTADA A OBJETOS", "INVESTIGACION TECNOLOGICA", "EXPERIENCIA FORMATICAS SIT. REAL. TRAB.", "MODELAMIENTO DE SOFTWARE", "ARQUITECTURA DE BASE DE DATOS" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(15, 137, 0, 0);
        jPanel1.add(curso, gridBagConstraints);

        jSeparator1.setBackground(new java.awt.Color(255, 255, 255));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx = 1338;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 0, 0, 18);
        jPanel1.add(jSeparator1, gridBagConstraints);

        ciclo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III", "IV", "V", "VI" }));
        ciclo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cicloActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel1.add(ciclo, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("SELECCIONE EL CURSO");
        jLabel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 137, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("CICLO");
        jLabel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 56;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 137, 0, 0);
        jPanel1.add(jLabel6, gridBagConstraints);

        getContentPane().add(jPanel1, "card2");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        MenuInicio menu = new MenuInicio();
        menu.setVisible(true);
        dispose();


    }//GEN-LAST:event_jButton2ActionPerformed

    private void cicloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cicloActionPerformed
// Añade aquí el código para actualizar el combobox de cursos
        String selectedCiclo = (String) ciclo.getSelectedItem();
        updateCursoOptions(selectedCiclo);

    }//GEN-LAST:event_cicloActionPerformed
    @Override
    public void dispose() {
        if (executor != null) {
            executor.shutdown();
        }
        if (webcam != null) {
            webcam.close();
        }
        super.dispose();
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
            java.util.logging.Logger.getLogger(Scaneador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Scaneador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Scaneador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Scaneador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Scaneador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ciclo;
    private javax.swing.JComboBox<String> curso;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
