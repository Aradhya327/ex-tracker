import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Experiment 2 - Insert one row into MySQL through JDBC.
 *
 * The id column is AUTO_INCREMENT, so it is left out of the INSERT and MySQL
 * assigns the next value. Running this twice therefore inserts two rows
 * instead of failing on a duplicate primary key.
 *
 * Connection settings can be overridden with the DB_URL, DB_USER and
 * DB_PASSWORD environment variables.
 */
public class App {

    private static final String DB_URL =
            System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college");
    private static final String DB_USER =
            System.getenv().getOrDefault("DB_USER", "root");
    private static final String DB_PASSWORD =
            System.getenv().getOrDefault("DB_PASSWORD", "luffy");

    public static void main(String[] args) {

        String sql = "INSERT INTO student (name, roll_number, branch, gender) VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = con.prepareStatement(sql)) {

            System.out.println("Connected to " + DB_URL);

            ps.setString(1, "luffy");
            ps.setString(2, "999");
            ps.setString(3, "Cyber");
            ps.setString(4, "Male");

            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
