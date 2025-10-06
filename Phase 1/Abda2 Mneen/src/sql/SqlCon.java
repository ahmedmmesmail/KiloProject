package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlCon {

    public static Connection con;

    public static void open() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=ebook;encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2;";
            String username = "book-store";
            String password = "4444";
            con = DriverManager.getConnection(url, username, password);
            System.out.println("CONNECTED");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void close() {
        try {
            con.close();
        } catch (SQLException ex) {
            System.out.println("ERROR Connection");
        }
    }

    public static String lastError = "";

    public static boolean Execute(String sqlStatement, Object[] parameters) { //to update,  delete, insert
        try {
            open();
            PreparedStatement ps = con.prepareStatement(sqlStatement);
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }
            ps.execute();
            close();
            return true;
        } catch (Exception e) {
            lastError = e.getMessage();
            System.out.println(e);
            //JOptionPane.showMessageDialog(null,e,"Erorr",JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static int ExecuteEdit(String sqlStatement, Object[] parameters) {
        int affectedRows = -1;
        try {
            open();
            PreparedStatement ps = con.prepareStatement(sqlStatement);

            // نمر على كل البراميترز ونحطها مكان علامات الاستفهام
            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            affectedRows = ps.executeUpdate();
            close();
        } catch (Exception e) {
            lastError = e.getMessage();
            System.out.println(e);
            affectedRows = -1;
        }
        return affectedRows;
    }
}
