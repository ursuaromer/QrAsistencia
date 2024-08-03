package Reporte;

import MenuInicio.MenuInicio;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import generadordeqr.DatabaseConnection;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Reporte extends javax.swing.JFrame {

    public Reporte() {
        initComponents();
        setLocationRelativeTo(null);
        //Primero, agrega un ActionListener al JComboBox "ciclo" en el método initComponents():
        ciclo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCiclo = (String) ciclo.getSelectedItem();
                updateCursoOptions(selectedCiclo);
            }
        });
        //Finalmente, llama a updateCursoOptions() en el constructor de la clase Reporte, justo después de initComponents(), para configurar las opciones iniciales:
        // En el constructor o en un método de inicialización
        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedReport = (String) jComboBox1.getSelectedItem();
                if ("Resumen General".equals(selectedReport)) {
                    curso.setEnabled(false);
                } else {
                    curso.setEnabled(true);
                }
            }
        });
    }

    //Luego, crea el método cicloActionPerformed():
    private void cicloActionPerformed(java.awt.event.ActionEvent evt) {
        String selectedCiclo = (String) ciclo.getSelectedItem();
        updateCursoOptions(selectedCiclo);
    }

    //rea un método updateCursoOptions() para actualizar las opciones del JComboBox "curso":
    private void updateCursoOptions(String selectedCiclo) {
        curso.removeAllItems();
        String sql = "SELECT nombre FROM Curso WHERE ciclo_id = (SELECT id FROM Ciclo WHERE nombre = ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selectedCiclo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                curso.addItem(rs.getString("nombre"));
            }

            if (curso.getItemCount() == 0) {
                curso.addItem("NO HAY CURSOS DISPONIBLES PARA ESTE CICLO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los cursos: " + e.getMessage());
        }
    }

    //Generar Reporte 
    private void generarReporte() {
        String dni = jTextField1.getText();
        String tipoReporte = (String) jComboBox1.getSelectedItem();
        String cursoSeleccionado = (String) curso.getSelectedItem();
        String cicloSeleccionado = (String) ciclo.getSelectedItem();
        java.util.Date fechaInicio = jDateChooser1.getDate();
        java.util.Date fechaTermino = jDateChooser2.getDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "";
            PreparedStatement pstmt;
            switch (tipoReporte) {
                case "Asistencia Diaria":
                    sql = "SELECT e.nombre, e.apellido, c.nombre as curso, a.fecha, a.hora "
                            + "FROM Asistencia a "
                            + "JOIN Estudiante e ON a.estudiante_codigo = e.codigo "
                            + "JOIN Curso c ON a.curso_id = c.id "
                            + "WHERE e.dni = ? AND c.nombre = ? AND a.fecha BETWEEN ? AND ? "
                            + "ORDER BY a.fecha, a.hora";
                    break;
                case "Resumen General":
                    // SQL para obtener el resumen de asistencias por curso para el ciclo del estudiante
                    sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ci.nombre as ciclo, COUNT(a.id) as total_asistencias "
                            + "FROM Estudiante e "
                            + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                            + "JOIN Curso c ON c.ciclo_id = ci.id "
                            + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id "
                            + "WHERE e.dni = ? AND ci.nombre = ? AND (a.fecha BETWEEN ? AND ? OR a.fecha IS NULL) "
                            + "GROUP BY e.codigo, c.id "
                            + "ORDER BY c.nombre";

                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, dni);
                    pstmt.setString(2, cicloSeleccionado);
                    pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                    pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));
                    break;
                case "Reporte de Faltas":
                    sql = "SELECT e.nombre, e.apellido, c.nombre as curso, "
                            + "COUNT(DISTINCT DATE(a.fecha)) as dias_asistidos, "
                            + "DATEDIFF(?, ?) + 1 - COUNT(DISTINCT DATE(a.fecha)) as dias_faltados "
                            + "FROM Estudiante e "
                            + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo "
                            + "JOIN Curso c ON a.curso_id = c.id "
                            + "WHERE e.dni = ? AND c.nombre = ? AND a.fecha BETWEEN ? AND ? "
                            + "GROUP BY e.codigo, c.id";
                    break;

                default:
                    throw new IllegalArgumentException("Tipo de reporte no válido");
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dni);
            pstmt.setString(2, cursoSeleccionado);
            pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
            pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));

            if (tipoReporte.equals("Reporte de Faltas")) {
                pstmt.setDate(1, new java.sql.Date(fechaTermino.getTime()));
                pstmt.setDate(2, new java.sql.Date(fechaInicio.getTime()));
                pstmt.setString(3, dni);
                pstmt.setString(4, cursoSeleccionado);
                pstmt.setDate(5, new java.sql.Date(fechaInicio.getTime()));
                pstmt.setDate(6, new java.sql.Date(fechaTermino.getTime()));
            }

            ResultSet rs = pstmt.executeQuery();

            // Aquí podrías procesar los resultados si lo necesitas para algo más
            JOptionPane.showMessageDialog(this, "Reporte generado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*private void generarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            Document document = new Document(PageSize.A4, 36, 36, 90, 36);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Añadir encabezado
                addHeader(writer);

                // Añadir título del reporte
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(44, 62, 80));
                String tipoReporte = (String) jComboBox1.getSelectedItem();
                Paragraph title = new Paragraph("Reporte de " + tipoReporte, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Información del estudiante
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingAfter(20);

                Font infoBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);

                String dni = jTextField1.getText();
                String cicloSeleccionado = (String) ciclo.getSelectedItem();
                java.util.Date fechaInicio = jDateChooser1.getDate();
                java.util.Date fechaTermino = jDateChooser2.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Connection conn = DatabaseConnection.getConnection();
                String sql = "";
                PreparedStatement pstmt;
                ResultSet rs = null;

                switch (tipoReporte) {
                    case "Asistencia Diaria":
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, a.fecha, a.hora "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id "
                                + "WHERE e.dni = ? AND ci.nombre = ? AND a.fecha BETWEEN ? AND ? "
                                + "ORDER BY a.fecha, a.hora, c.nombre";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setString(1, dni);
                        pstmt.setString(2, cicloSeleccionado);
                        pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                    case "Resumen General":
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ci.nombre as ciclo, "
                                + "COUNT(a.id) as total_asistencias, "
                                + "(SELECT COUNT(DISTINCT fecha) FROM Asistencia WHERE fecha BETWEEN ? AND ?) - COUNT(DISTINCT a.fecha) as total_faltas "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id AND a.fecha BETWEEN ? AND ? "
                                + "WHERE e.dni = ? AND ci.nombre = ? "
                                + "GROUP BY e.codigo, c.id "
                                + "ORDER BY c.nombre";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(2, new java.sql.Date(fechaTermino.getTime()));
                        pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));
                        pstmt.setString(5, dni);
                        pstmt.setString(6, cicloSeleccionado);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                    case "Reporte de Faltas":
                        String cursoSeleccionado = (String) curso.getSelectedItem();
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ci.nombre as ciclo, "
                                + "COALESCE(a.fecha, 'Sin faltas') as fecha, COALESCE(a.hora, 'Sin faltas') as hora "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id "
                                + "WHERE e.dni = ? AND ci.nombre = ? AND c.nombre = ? "
                                + "AND (a.fecha BETWEEN ? AND ? OR a.fecha IS NULL) "
                                + "ORDER BY a.fecha, a.hora";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setString(1, dni);
                        pstmt.setString(2, cicloSeleccionado);
                        pstmt.setString(3, cursoSeleccionado);
                        pstmt.setDate(4, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(5, new java.sql.Date(fechaTermino.getTime()));
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Curso:", cursoSeleccionado, infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                }

                document.add(infoTable);

                // Crear y añadir la tabla de datos
                PdfPTable dataTable = createDataTable(tipoReporte, rs);
                document.add(dataTable);

                // Añadir pie de página
                Phrase footer = new Phrase("Reporte generado el " + sdf.format(new Date()), new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC));
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer, 300, 20, 0);

                document.close();
                JOptionPane.showMessageDialog(this, "PDF generado con éxito en: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
            }
        }
    }
     */
    private void generarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            Document document = new Document(PageSize.A4, 36, 36, 90, 36);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Añadir encabezado
                addHeader(writer);

                // Añadir título del reporte
                Font titleFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(44, 62, 80));
                String tipoReporte = (String) jComboBox1.getSelectedItem();
                Paragraph title = new Paragraph("Reporte de " + tipoReporte, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                title.setSpacingAfter(20);
                document.add(title);

                // Información del estudiante
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingAfter(20);

                Font infoBoldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font infoFont = new Font(Font.FontFamily.HELVETICA, 12);

                String dni = jTextField1.getText();
                String cicloSeleccionado = (String) ciclo.getSelectedItem();
                java.util.Date fechaInicio = jDateChooser1.getDate();
                java.util.Date fechaTermino = jDateChooser2.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Connection conn = DatabaseConnection.getConnection();
                String sql = "";
                PreparedStatement pstmt;
                ResultSet rs = null;

                switch (tipoReporte) {
                    case "Asistencia Diaria":
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ca.nombre as carrera, a.fecha, a.hora "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Carrera ca ON e.carrera_id = ca.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id "
                                + "WHERE e.dni = ? AND ci.nombre = ? AND a.fecha BETWEEN ? AND ? "
                                + "ORDER BY a.fecha, a.hora, c.nombre";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setString(1, dni);
                        pstmt.setString(2, cicloSeleccionado);
                        pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Carrera:", rs.getString("carrera"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Curso:", rs.getString("curso"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                    case "Resumen General":
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ci.nombre as ciclo, ca.nombre as carrera, "
                                + "COUNT(a.id) as total_asistencias, "
                                + "(SELECT COUNT(DISTINCT fecha) FROM Asistencia WHERE fecha BETWEEN ? AND ?) - COUNT(DISTINCT a.fecha) as total_faltas "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Carrera ca ON e.carrera_id = ca.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id AND a.fecha BETWEEN ? AND ? "
                                + "WHERE e.dni = ? AND ci.nombre = ? "
                                + "GROUP BY e.codigo, c.id "
                                + "ORDER BY c.nombre";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setDate(1, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(2, new java.sql.Date(fechaTermino.getTime()));
                        pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(4, new java.sql.Date(fechaTermino.getTime()));
                        pstmt.setString(5, dni);
                        pstmt.setString(6, cicloSeleccionado);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Carrera:", rs.getString("carrera"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                    case "Reporte de Faltas":
                        String cursoSeleccionado = (String) curso.getSelectedItem();
                        sql = "SELECT e.nombre, e.apellido, c.nombre as curso, ci.nombre as ciclo, ca.nombre as carrera, "
                                + "DATE_FORMAT(a.fecha, '%Y-%m-%d') as fecha, TIME_FORMAT(a.hora, '%H:%i:%s') as hora "
                                + "FROM Estudiante e "
                                + "JOIN Ciclo ci ON e.ciclo_id = ci.id "
                                + "JOIN Carrera ca ON e.carrera_id = ca.id "
                                + "JOIN Curso c ON c.ciclo_id = ci.id "
                                + "LEFT JOIN Asistencia a ON e.codigo = a.estudiante_codigo AND a.curso_id = c.id "
                                + "WHERE e.dni = ? AND ci.nombre = ? AND c.nombre = ? "
                                + "AND (a.fecha BETWEEN ? AND ? OR a.fecha IS NULL) "
                                + "ORDER BY a.fecha, a.hora";
                        pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        pstmt.setString(1, dni);
                        pstmt.setString(2, cicloSeleccionado);
                        pstmt.setString(3, cursoSeleccionado);
                        pstmt.setDate(4, new java.sql.Date(fechaInicio.getTime()));
                        pstmt.setDate(5, new java.sql.Date(fechaTermino.getTime()));
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            addInfoRow(infoTable, "DNI:", dni, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Nombre:", rs.getString("nombre") + " " + rs.getString("apellido"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Carrera:", rs.getString("carrera"), infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Ciclo:", cicloSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Curso:", cursoSeleccionado, infoBoldFont, infoFont);
                            addInfoRow(infoTable, "Periodo:", sdf.format(fechaInicio) + " - " + sdf.format(fechaTermino), infoBoldFont, infoFont);
                            rs.beforeFirst();
                        }
                        break;
                }

                document.add(infoTable);

                // Crear y añadir la tabla de datos
                PdfPTable dataTable = createDataTable(tipoReporte, rs, fechaInicio, fechaTermino);
                document.add(dataTable);

                // Añadir pie de página
                Phrase footer = new Phrase("Reporte generado el " + sdf.format(new Date()), new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC));
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, footer, 300, 20, 0);

                document.close();
                JOptionPane.showMessageDialog(this, "PDF generado con éxito en: " + filePath);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al generar PDF: " + e.getMessage());
            }
        }
    }

    private void addHeader(PdfWriter writer) throws IOException, DocumentException, BadElementException {
        PdfPTable header = new PdfPTable(2);
        header.setTotalWidth(527);
        header.setLockedWidth(true);
        header.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        Image logo = Image.getInstance(getClass().getResource("/img/Logo-suiza.png"));
        logo.scaleToFit(70, 70);
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setPadding(5);
        header.addCell(logoCell);

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, new BaseColor(44, 62, 80));
        PdfPCell text = new PdfPCell();
        text.setPaddingBottom(15);
        text.setPaddingLeft(10);
        text.setBorder(Rectangle.NO_BORDER);
        text.addElement(new Phrase("Sistema de Asistencia", headerFont));
        header.addCell(text);

        header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
    }

    private PdfPTable createDataTable(String tipoReporte, ResultSet rs, Date fechaInicio, Date fechaTermino) throws SQLException, com.itextpdf.text.DocumentException {
        PdfPTable table;
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
        BaseColor headerColor = new BaseColor(44, 62, 80);
        BaseColor lightGray = new BaseColor(242, 242, 242);

        switch (tipoReporte) {
            case "Asistencia Diaria":
                table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 1, 1});
                addTableHeader(table, new String[]{"Curso", "Fecha", "Hora"}, headerFont, headerColor);
                break;
            case "Resumen General":
                table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 1, 1});
                addTableHeader(table, new String[]{"Curso", "Total Asistencias", "Total Faltas"}, headerFont, headerColor);
                break;
            case "Reporte de Faltas":
                table = new PdfPTable(2);
                table.setWidthPercentage(100);
                addTableHeader(table, new String[]{"Fecha", "Hora"}, headerFont, headerColor);

                boolean hasFaltas = false;
                while (rs.next()) {
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");
                    if (!"Sin faltas".equals(fecha) && !"Sin faltas".equals(hora)) {
                        addCell(table, fecha, dataFont, BaseColor.WHITE);
                        addCell(table, hora, dataFont, BaseColor.WHITE);
                        hasFaltas = true;
                    }
                }

                if (!hasFaltas) {
                    PdfPCell cell = new PdfPCell(new Phrase("No se registraron faltas en el período seleccionado", dataFont));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.WHITE);
                    cell.setPadding(10);
                    table.addCell(cell);
                }
                break;

            default:
                throw new IllegalArgumentException("Tipo de reporte no válido");
        }

        boolean alternate = false;
        while (rs.next()) {
            alternate = !alternate;
            BaseColor rowColor = alternate ? lightGray : BaseColor.WHITE;
            switch (tipoReporte) {
                case "Asistencia Diaria":
                    addCell(table, rs.getString("curso"), dataFont, rowColor);
                    addCell(table, rs.getDate("fecha").toString(), dataFont, rowColor);
                    addCell(table, rs.getTime("hora").toString(), dataFont, rowColor);
                    break;
                case "Resumen General":
                    addCell(table, rs.getString("curso"), dataFont, rowColor);
                    addCell(table, String.valueOf(rs.getInt("total_asistencias")), dataFont, rowColor);
                    addCell(table, String.valueOf(rs.getInt("total_faltas")), dataFont, rowColor);
                    break;
                case "Reporte de Faltas":
                    addCell(table, rs.getDate("fecha").toString(), dataFont, rowColor);
                    addCell(table, rs.getTime("hora").toString(), dataFont, rowColor);
                    break;
            }
        }

        return table;
    }

    private List<Date> obtenerDiasHabiles(Date fechaInicio, Date fechaTermino) {
        List<Date> diasHabiles = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);

        while (!cal.getTime().after(fechaTermino)) {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                diasHabiles.add(cal.getTime());
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return diasHabiles;
    }

    private void addTableHeader(PdfPTable table, String[] headers, Font font, BaseColor color) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, font));
            cell.setBackgroundColor(color);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private void addCell(PdfPTable table, String text, Font font, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addInfoRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    //Crea un método updateCursoOptions() para actualizar las opciones del JComboBox "curso":
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton4 = new javax.swing.JButton();
        loginBtn = new javax.swing.JPanel();
        loginBtnTxt1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        curso = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        ciclo = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 102, 255));

        jPanel1.setBackground(new java.awt.Color(43, 77, 106));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("TIPO DE REPORTE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 120, 0, 0);
        jPanel1.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.ipadx = 186;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 144, 0, 0);
        jPanel1.add(jTextField1, gridBagConstraints);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/printing.png"))); // NOI18N
        jButton2.setText("PDF");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(131, 87, 0, 0);
        jPanel1.add(jButton2, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("REPORTE");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 124, 0, 0);
        jPanel1.add(jLabel1, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(60, 50, 236));
        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("FECHA DE INICIO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 144, 0, 0);
        jPanel1.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("FECHA DE TERMINO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 120, 0, 0);
        jPanel1.add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 5, 0, 0);
        jPanel1.add(jDateChooser1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 23;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(74, 1, 0, 0);
        jPanel1.add(jDateChooser2, gridBagConstraints);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(141, 40, 69, 0);
        jPanel1.add(jButton4, gridBagConstraints);

        loginBtn.setBackground(new java.awt.Color(0, 134, 190));

        loginBtnTxt1.setFont(new java.awt.Font("Roboto Condensed", 1, 14)); // NOI18N
        loginBtnTxt1.setForeground(new java.awt.Color(255, 255, 255));
        loginBtnTxt1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginBtnTxt1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagen/magnifying-glass.png"))); // NOI18N
        loginBtnTxt1.setText("GENERAR");
        loginBtnTxt1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginBtnTxt1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginBtnTxt1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtnTxt1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtnTxt1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout loginBtnLayout = new javax.swing.GroupLayout(loginBtn);
        loginBtn.setLayout(loginBtnLayout);
        loginBtnLayout.setHorizontalGroup(
            loginBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginBtnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(loginBtnTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        loginBtnLayout.setVerticalGroup(
            loginBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(loginBtnTxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(121, 16, 0, 0);
        jPanel1.add(loginBtn, gridBagConstraints);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Asistencia Diaria", "Resumen General", "Reporte de Faltas" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 68;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 120, 0, 0);
        jPanel1.add(jComboBox1, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DNI :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 144, 0, 0);
        jPanel1.add(jLabel6, gridBagConstraints);

        curso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INGLES PARA LA COMUNICACION ORAL", "PROGRAMACION DISTRIBUIDA", "PROGRAMACION CONCURRENTE", "PROGRAMACION ORIENTADA A OBJETOS", "INVESTIGACION TECNOLOGICA", "EXPERIENCIA FORMATICAS SIT. REAL. TRAB.", "MODELAMIENTO DE SOFTWARE", "ARQUITECTURA DE BASE DE DATOS" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = -90;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 144, 0, 0);
        jPanel1.add(curso, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("CURSO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 144, 0, 0);
        jPanel1.add(jLabel7, gridBagConstraints);

        ciclo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "I", "II", "III", "IV", "V", "VI", " " }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 120, 0, 0);
        jPanel1.add(ciclo, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("CICLO");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 120, 0, 0);
        jPanel1.add(jLabel8, gridBagConstraints);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 26;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 1249;
        gridBagConstraints.ipady = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 0, 0, 2);
        jPanel1.add(jSeparator1, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        MenuInicio menu = new MenuInicio();
        menu.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void loginBtnTxt1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginBtnTxt1MouseClicked
        generarReporte();
    }//GEN-LAST:event_loginBtnTxt1MouseClicked

    private void loginBtnTxt1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginBtnTxt1MouseEntered
        loginBtn.setBackground(new Color(0, 156, 223));
    }//GEN-LAST:event_loginBtnTxt1MouseEntered

    private void loginBtnTxt1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginBtnTxt1MouseExited
        loginBtn.setBackground(new Color(0, 134, 190));
    }//GEN-LAST:event_loginBtnTxt1MouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        generarPDF();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        com.sun.javafx.application.PlatformImpl.startup(() -> {
        });
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reporte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ciclo;
    private javax.swing.JComboBox<String> curso;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel loginBtn;
    private javax.swing.JLabel loginBtnTxt1;
    // End of variables declaration//GEN-END:variables
}
