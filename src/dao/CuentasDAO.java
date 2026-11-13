package dao;

import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import conexion.Conexion;

public class CuentasDAO {

    private static final int ITERACIONES = 120_000;
    private static final int LONGITUD_SALT = 16;
    private static final int LONGITUD_HASH_BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    public boolean registrar(String correo, char[] contrasena) throws SQLException {
        crearTablaSiNoExiste();
        byte[] salt = new byte[LONGITUD_SALT];
        ALEATORIO.nextBytes(salt);
        byte[] hash = derivarHash(contrasena, salt);
        String sql = "INSERT INTO cuentas (correo, salt, hash_contrasena) VALUES (?, ?, ?)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setBytes(2, salt);
            ps.setBytes(3, hash);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState()) || "23505".equals(e.getSQLState())) {
                return false;
            }
            throw e;
        } finally {
            Arrays.fill(salt, (byte) 0);
            Arrays.fill(hash, (byte) 0);
        }
    }

    public boolean autenticar(String correo, char[] contrasena) throws SQLException {
        crearTablaSiNoExiste();
        String sql = "SELECT salt, hash_contrasena FROM cuentas WHERE correo = ?";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                byte[] salt = rs.getBytes("salt");
                byte[] hashGuardado = rs.getBytes("hash_contrasena");
                byte[] hashIngresado = derivarHash(contrasena, salt);
                try {
                    return MessageDigest.isEqual(hashGuardado, hashIngresado);
                } finally {
                    Arrays.fill(salt, (byte) 0);
                    Arrays.fill(hashGuardado, (byte) 0);
                    Arrays.fill(hashIngresado, (byte) 0);
                }
            }
        }
    }

    private void crearTablaSiNoExiste() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS cuentas ("
                + "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "correo VARCHAR(254) NOT NULL UNIQUE, "
                + "salt VARBINARY(16) NOT NULL, "
                + "hash_contrasena VARBINARY(32) NOT NULL)";

        try (Connection cn = Conexion.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private byte[] derivarHash(char[] contrasena, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(contrasena, salt, ITERACIONES, LONGITUD_HASH_BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo proteger la contraseña.", e);
        } finally {
            spec.clearPassword();
        }
    }
}
