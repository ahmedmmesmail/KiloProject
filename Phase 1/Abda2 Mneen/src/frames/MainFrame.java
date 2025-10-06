package frames;

import sql.SqlCon;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainFrame extends BaseFrame {

    public MainFrame() {
        super("Book Store", false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel isbnLabel = new JLabel("Enter book ISBN:");
        isbnLabel.setFont(new Font("monospace", Font.BOLD, 18));
        isbnLabel.setForeground(new Color(0x2563EB));

        JTextField isbnField = new JTextField(15);
        isbnField.setPreferredSize(new Dimension(200, 35));
        isbnField.setFont(new Font("monospace", Font.BOLD, 16));
        isbnField.setBorder(null);

        RoundedButton searchButton = new RoundedButton("Search for book");
        RoundedButton deleteButton = new RoundedButton("Delete book");
        RoundedButton updateButton = new RoundedButton("Update book");
        RoundedButton addAuthorButton = new RoundedButton("Add author");
        RoundedButton addBookButton = new RoundedButton("Add book");
        RoundedButton addPubButton = new RoundedButton("Add publisher");

        RoundedButton[] buttons = {searchButton, deleteButton, updateButton, addAuthorButton, addBookButton, addPubButton};
        for (RoundedButton btn : buttons) styleButton(btn);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(isbnLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(isbnField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(deleteButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(searchButton, gbc);
        gbc.gridx = 2;
        mainPanel.add(updateButton, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(addAuthorButton, gbc);
        gbc.gridx = 1;
        mainPanel.add(addBookButton, gbc);
        gbc.gridx = 2;
        mainPanel.add(addPubButton, gbc);

        setVisible(true);

        addAuthorButton.addActionListener(e -> AddAuthorForm.openForm());
        addBookButton.addActionListener(e -> AddBookForm.openForm());
        addPubButton.addActionListener(e -> AddPubForm.openForm());
        updateButton.addActionListener(e -> UpdateBookForm.openForm());
        searchButton.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "The ISBN field is empty! Please enter the book ISBN", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    SqlCon.open();
                    Connection con = SqlCon.con;
                    String sql = "SELECT b.title, b.type, b.price, b.page_count, " + "p.name AS publisher_name, p.city AS publisher_city, p.phone AS publisher_phone, p.p_email, " + "a.first_name, a.last_name, a.city AS author_city, a.a_email " + "FROM books b " + "JOIN publisher p ON b.Publisher_code = p.publisher_code " + "JOIN author_books ab ON b.isbn = ab.isbn " + "JOIN author a ON ab.author_id = a.author_id " + "WHERE b.isbn = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, isbn);
                    ResultSet rs = stmt.executeQuery();
                    StringBuilder authorsInfo = new StringBuilder();
                    String title = "", type = "", price = "", pages = "";
                    String publisherName = "", publisherCity = "", publisherPhone = "", publisherEmail = "";
                    boolean found = false;
                    while (rs.next()) {
                        found = true;
                        if (title.isEmpty()) {
                            title = rs.getString("title");
                            type = rs.getString("type");
                            price = rs.getString("price");
                            pages = rs.getString("page_count");
                            publisherName = rs.getString("publisher_name");
                            publisherCity = rs.getString("publisher_city");
                            publisherPhone = rs.getString("publisher_phone");
                            publisherEmail = rs.getString("p_email");
                        }
                        String authorName = rs.getString("first_name") + " " + rs.getString("last_name");
                        String authorCity = rs.getString("author_city");
                        String authorEmail = rs.getString("a_email");
                        authorsInfo.append("\n\nAuthor info:").append("\nName: ").append(authorName).append("\nCity: ").append(authorCity).append("\nEmail: ").append(authorEmail);
                    }
                    if (found) {
                        String message = "ISBN: " + isbn + "\nTitle: " + title + "\nType: " + type + "\nPrice: " + price + "\nPages: " + pages + "\n\nPublisher Info:" + "\nName: " + publisherName + "\nCity: " + publisherCity + "\nPhone: " + publisherPhone + "\nEmail: " + publisherEmail + "\n" + authorsInfo.toString();
                        JOptionPane.showMessageDialog(this, message, "Book Found", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No book found with ISBN: " + isbn, "Not Found", JOptionPane.ERROR_MESSAGE);
                    }
                    rs.close();
                    stmt.close();
                    SqlCon.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteButton.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            if (isbnField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "The ISBN field is empty! Please enter the book ISBN", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String sql = "DELETE FROM author_books WHERE isbn = ?";
                String sql2 = "DELETE FROM books WHERE isbn = ?";
                Object[] params = {isbn};
                int affectedRows = SqlCon.ExecuteEdit(sql, params);
                int affectedRows2 = SqlCon.ExecuteEdit(sql2, params);
                if (affectedRows == 0 && affectedRows2 == 0) {
                    JOptionPane.showMessageDialog(this, "No book found with this ISBN. delete failed.", "delete Error", JOptionPane.ERROR_MESSAGE);
                } else if (affectedRows > 0 && affectedRows2 > 0) {
                    JOptionPane.showMessageDialog(this, isbn + " was successfully deleted!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error in data entry:\n" + SqlCon.lastError, "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}

