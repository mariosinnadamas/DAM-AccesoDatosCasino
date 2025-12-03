package casino.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    public String URL = "jdbc:postgresql://localhost:5432/aprendizaje";
    String USER = "alumno";
    String PWD = "alumno";
    String schema = "";

    public ConexionDB() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public ConexionDB(String schema) {
        this.schema = schema;
    }

    public Connection conectarBaseDatos() throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USER,PWD);
        if (schema.isEmpty()){
            return connection;
        } else {
            var stmt = connection.createStatement();
            stmt.execute("SET search_path TO " + this.schema);
            return connection;
        }
    }
}
