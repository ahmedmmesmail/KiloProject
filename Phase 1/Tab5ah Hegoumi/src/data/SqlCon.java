package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SqlCon {

    public static Connection con;

    public static void open() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=menu;encrypt=true;trustServerCertificate=true;sslProtocol=TLSv1.2;";
            String username = "restaurant";
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

    public static ObservableList<Item> getAllItems() throws SQLException {
        open();
        ObservableList<Item> items = FXCollections.observableArrayList();
        String sql = "SELECT * FROM food";
        ResultSet rs = con.createStatement().executeQuery(sql);
        while (rs.next()) {
            int code = rs.getInt("code");
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            items.add(new Item(code, name, price));
        }
        return items;
    }

    public static void addItem(Item item) throws SQLException {
        open();
        String sql = "INSERT INTO food (code, name, price) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, item.getCode());
        stmt.setString(2, item.getName());
        stmt.setDouble(3, item.getPrice());
        stmt.executeUpdate();
        close();
    }

    public static void deleteItem(int code) throws SQLException {
        open();
        String sql = "DELETE FROM food WHERE code = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, code);
        int rowsAffected = stmt.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);
        close();
    }

    public static void updateItem(int code, String newName, double newPrice) throws SQLException {
        open();
        String sql = "UPDATE food SET name = ?, price = ? WHERE code = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, newName);
        stmt.setDouble(2, newPrice);
        stmt.setInt(3, code);
        stmt.executeUpdate();
        close();
    }

    public static Item getItemByCode(int code) throws SQLException {
        open();
        String sql = "SELECT * FROM food WHERE code = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, code);
        ResultSet rs = stmt.executeQuery();

        Item item = null;
        if (rs.next()) {
            String name = rs.getString("name");
            double price = rs.getDouble("price");
            item = new Item(code, name, price);
        }

        close();
        return item;
    }

    public static boolean itemExists(int code) throws SQLException {
        open();
        String query = "SELECT COUNT(*) FROM food WHERE code = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, code);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }


}