package context;

import java.sql.Connection;
import java.sql.DriverManager;

public class Nga_DBContext {
    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://localhost;instanceName=SQLEXPRESS;databaseName=CHGiaDung;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String pass = "15082005";

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, pass);
    }
}