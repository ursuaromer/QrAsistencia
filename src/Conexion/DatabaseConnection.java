package generadordeqr;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/qrasistenciaapp";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void guardarEstudiante(String codigo, String nombre, String apellido,
            String dni, String email, int carreraId,
            int cicloId, byte[] qrCode) {
        String sql = "INSERT INTO estudiante (codigo, nombre, apellido, dni, email, carrera_id, ciclo_id, qr_code) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, dni);
            pstmt.setString(5, email);
            pstmt.setInt(6, carreraId);
            pstmt.setInt(7, cicloId);
            pstmt.setBytes(8, qrCode);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Estudiante guardado exitosamente.");
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo guardar el estudiante.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ESTUDIANTE REGISTRADO: ");
            e.printStackTrace();
        }
    }

    public static int obtenerCarreraId(String nombreCarrera) {
        String sql = "SELECT id FROM carrera WHERE nombre = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreCarrera);
            System.out.println("Buscando carrera: " + nombreCarrera);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Carrera encontrada con id: " + id);
                return id;
            } else {
                System.out.println("No se encontró la carrera: " + nombreCarrera);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar carrera: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public static int obtenerCicloId(String nombreCarrera) {
        String sql = "SELECT id FROM ciclo WHERE nombre = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombreCarrera);
            System.out.println("Buscando carrera: " + nombreCarrera);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                System.out.println("Carrera encontrada con id: " + id);
                return id;
            } else {
                System.out.println("No se encontró la carrera: " + nombreCarrera);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar carrera: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    //VERIFICAR SI EL ALUMNO EXISTE
    public static boolean verificarEstudiante(String codigo) {
        String sql = "SELECT * FROM estudiante WHERE codigo = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar estudiante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //REGISTRA ALUMNOS
    public static boolean registrarAsistencia(String codigoEstudiante, int cursoId) {
        // Primero, verificar si ya existe una asistencia para hoy
        String sqlCheck = "SELECT COUNT(*) FROM asistencia WHERE estudiante_codigo = ? AND curso_id = ? AND DATE(fecha) = CURRENT_DATE()";
        try (Connection conn = getConnection();
                PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setString(1, codigoEstudiante);
            pstmtCheck.setInt(2, cursoId);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Ya existe una asistencia registrada para hoy");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar asistencia existente: " + e.getMessage());
            return false;
        }

        // Si no existe, registrar la nueva asistencia
        String sqlInsert = "INSERT INTO asistencia (estudiante_codigo, curso_id, fecha, hora) VALUES (?, ?, CURRENT_DATE(), CURRENT_TIME())";
        try (Connection conn = getConnection();
                PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
            pstmtInsert.setString(1, codigoEstudiante);
            pstmtInsert.setInt(2, cursoId);
            int affectedRows = pstmtInsert.executeUpdate();
            System.out.println("Filas afectadas al registrar asistencia: " + affectedRows);
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar asistencia: " + e.getMessage());
            return false;
        }
    }

    //OBTENERDATOSDELALUMNO
    public static String[] obtenerDatosEstudiante(String codigo) {
        String sql = "SELECT e.codigo, e.nombre, e.apellido, e.dni, e.email, c.nombre as carrera, ci.nombre as ciclo "
                + "FROM estudiante e "
                + "JOIN carrera c ON e.carrera_id = c.id "
                + "JOIN ciclo ci ON e.ciclo_id = ci.id "
                + "WHERE e.codigo = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getString("email"),
                        rs.getString("carrera"),
                        rs.getString("ciclo")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener datos del estudiante: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //validacion de datos
    // Método para validar los datos de inicio de sesión
    public static boolean validarLogeo(String usuario, String contraseña) {
        String sql = "SELECT * FROM Administrador WHERE usuario = ? AND contraseña = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario);
            pstmt.setString(2, contraseña);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Si existe una fila, los datos son válidos
            }
        } catch (SQLException e) {
            System.out.println("Error al validar logeo: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Retorna false si no se encuentra una coincidencia o si ocurre un error
    }

    // Método para actualizar la contraseña de un administrador
    // Método para actualizar la contraseña de un administrador
    public static boolean actualizarContrasena(String usuario, String nuevaContrasena, String confirmarContrasena) {
        // Verificar que las nuevas contraseñas coinciden
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            System.out.println("Las nuevas contraseñas no coinciden.");
            return false;
        }

        // Actualizar la contraseña en la base de datos
        String sqlActualizar = "UPDATE Administrador SET contraseña = ? WHERE usuario = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmtActualizar = conn.prepareStatement(sqlActualizar)) {

            pstmtActualizar.setString(1, nuevaContrasena);
            pstmtActualizar.setString(2, usuario);

            int affectedRows = pstmtActualizar.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Contraseña actualizada exitosamente.");
                return true;
            } else {
                System.out.println("No se pudo actualizar la contraseña.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //*Metodo para actualizar Estudiante*
    public static boolean actualizarEstudiante(String codigo, String nombre, String apellido,
            String dni, String email, int carreraId,
            int cicloId, byte[] qrCode) {
        String sql = "UPDATE estudiante SET nombre = ?, apellido = ?, dni = ?, email = ?, "
                + "carrera_id = ?, ciclo_id = ?, qr_code = ? WHERE codigo = ?";

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, dni);
            pstmt.setString(4, email);
            pstmt.setInt(5, carreraId);
            pstmt.setInt(6, cicloId);
            pstmt.setBytes(7, qrCode);
            pstmt.setString(8, codigo);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Estudiante actualizado exitosamente.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el estudiante. Verifique el código.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar estudiante: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, Object> buscarEstudiante(String codigoODni) {
        String sql = "SELECT e.*, c.nombre as carrera_nombre, ci.nombre as ciclo_nombre "
                + "FROM estudiante e "
                + "JOIN carrera c ON e.carrera_id = c.id "
                + "JOIN ciclo ci ON e.ciclo_id = ci.id "
                + "WHERE e.codigo = ? OR e.dni = ?";

        Map<String, Object> estudianteData = new HashMap<>();

        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoODni);
            pstmt.setString(2, codigoODni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    estudianteData.put("codigo", rs.getString("codigo"));
                    estudianteData.put("nombre", rs.getString("nombre"));
                    estudianteData.put("apellido", rs.getString("apellido"));
                    estudianteData.put("dni", rs.getString("dni"));
                    estudianteData.put("email", rs.getString("email"));
                    estudianteData.put("carrera", rs.getString("carrera_nombre"));
                    estudianteData.put("ciclo", rs.getString("ciclo_nombre"));
                    estudianteData.put("qr_code", rs.getBytes("qr_code"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return estudianteData;
    }

}
