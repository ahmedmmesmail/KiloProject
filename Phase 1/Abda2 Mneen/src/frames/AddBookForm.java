package frames;

import sql.SqlCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddBookForm extends BaseFrame {

    public AddBookForm() {
        super("Add Book", true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("monospace", Font.BOLD, 16);
        Color labelColor = new Color(0x2563EB);
        Font fieldFont = new Font("monospace", Font.BOLD, 16);

        JLabel idLabel = new JLabel("Book ID:");
        JTextField idField = new JTextField(15);

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField(15);

        JLabel publisherLabel = new JLabel("Publisher ID:");
        JTextField publisherField = new JTextField(15);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(15);

        RoundedButton saveButton = new RoundedButton("Save");
        styleButton(saveButton);

        for (JLabel lbl : new JLabel[]{idLabel, titleLabel, publisherLabel, priceLabel}) {
            lbl.setFont(labelFont);
            lbl.setForeground(labelColor);
        }

        for (JTextField tf : new JTextField[]{idField, titleField, publisherField, priceField}) {
            tf.setFont(fieldFont);
            tf.setPreferredSize(new Dimension(200, 35));
            tf.setBorder(null);
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(idField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(titleLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(titleField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(publisherLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(publisherField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(priceField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(saveButton, gbc);

        idField.addKeyListener(onlyDigits());
        publisherField.addKeyListener(onlyDigits());

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        saveButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String publisher = publisherField.getText().trim();
            String price = priceField.getText().trim();

            if (id.isEmpty() || title.isEmpty() || publisher.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(AddBookForm.this, "Please enter all data", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean bookExists = false;
                try {
                    SqlCon.open();
                    PreparedStatement ps = SqlCon.con.prepareStatement("SELECT COUNT(*) FROM book WHERE book_id = ?");
                    ps.setObject(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        bookExists = rs.getInt(1) > 0;
                    }
                    SqlCon.close();
                } catch (Exception ex) {
                    SqlCon.lastError = ex.getMessage();
                    System.out.println(ex);
                    bookExists = false;
                }

                if (bookExists) {
                    JOptionPane.showMessageDialog(AddBookForm.this, "This book already exists", "Duplicate book", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[] bookParameters = {id, title, publisher, price};
                    boolean result = SqlCon.Execute("INSERT INTO book VALUES(?, ?, ?, ?)", bookParameters);
                    if (!result) {
                        JOptionPane.showMessageDialog(AddBookForm.this, "Error in data entry:\n" + SqlCon.lastError, "Database Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AddBookForm.this, title + " was added successfully!");
                        dispose();
                    }
                }
            }
        });

        setVisible(true);
    }

    private KeyAdapter onlyDigits() {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        };
    }

    public static void openForm() {
        SwingUtilities.invokeLater(AddBookForm::new);
    }
}