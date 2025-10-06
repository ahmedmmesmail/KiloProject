package frames;

import sql.SqlCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UpdateBookForm extends BaseFrame {

    public UpdateBookForm() {
        super("Update Book", true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel isbnLabel = new JLabel("ISBN:");
        JTextField isbnField = new JTextField();
        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        JTextField typeField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel pagesLabel = new JLabel("Pages:");
        JTextField pagesField = new JTextField();
        RoundedButton saveButton = new RoundedButton("Save");
        styleButton(saveButton);

        JLabel[] labels = {isbnLabel, titleLabel, typeLabel, pagesLabel, priceLabel};
        for (JLabel lbl : labels) styleLabel(lbl);

        JTextField[] tf = {isbnField, titleField, typeField, pagesField, priceField};
        for (JTextField fld : tf) styleField(fld);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(isbnLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(isbnField, gbc);

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
        mainPanel.add(typeLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(typeField, gbc);

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

        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ea) {
                char c = ea.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
                    ea.consume();
                }
                if (c == '.' && priceField.getText().contains(".")) {
                    ea.consume();
                }
            }
        });

        pagesField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ea) {
                char c = ea.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    ea.consume();
                }
            }
        });

        saveButton.addActionListener(e -> {
            String isbn = isbnField.getText().trim();
            String title = titleField.getText().trim();
            String type = typeField.getText().trim();
            String pages = pagesField.getText().trim();
            String price = priceField.getText().trim();

            if (isbn.isEmpty() || title.isEmpty() || type.isEmpty() || pages.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all data", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (Integer.parseInt(pages) < 9) {
                JOptionPane.showMessageDialog(this, "invalid pages count", "Erorr", JOptionPane.ERROR_MESSAGE);
            } else if (Float.parseFloat(price) <= 0) {
                JOptionPane.showMessageDialog(this, "invalid price", "Erorr", JOptionPane.ERROR_MESSAGE);
            } else {
                String sql = "UPDATE books SET title = ?, type = ?, page_count = ?, price = ? WHERE isbn = ?";
                Object[] params = {title, type, pages, price, isbn};

                int affectedRows = SqlCon.ExecuteEdit(sql, params);

                if (affectedRows == 0) {
                    JOptionPane.showMessageDialog(this, "No book found with this ISBN. Update failed.", "Update Error", JOptionPane.ERROR_MESSAGE);
                } else if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, title + " was updated successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Error in data entry:\n" + SqlCon.lastError, "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });
        setVisible(true);

    }


    public static void openForm() {
        SwingUtilities.invokeLater(() -> new UpdateBookForm());
    }
}
