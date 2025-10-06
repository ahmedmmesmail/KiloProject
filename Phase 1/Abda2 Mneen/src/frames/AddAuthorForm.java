package frames;

import sql.SqlCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddAuthorForm extends BaseFrame {

    public AddAuthorForm() {
        super("Add Author", true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;


        // Labels + Fields
        JLabel idLabel = new JLabel("Author ID:");
        JTextField idField = new JTextField(15);

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField(15);

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField(15);

        JLabel cityLabel = new JLabel("City:");
        JTextField cityField = new JTextField(15);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);

        RoundedButton saveButton = new RoundedButton("Save Author");
        styleButton(saveButton);

        JLabel[] labels = {idLabel, firstNameLabel, lastNameLabel, cityLabel, emailLabel};
        for (JLabel lbl : labels) styleLabel(lbl);


        JTextField[] tf = {idField, firstNameField, lastNameField, cityField, emailField};
        for (JTextField fld : tf) styleField(fld);

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
        mainPanel.add(firstNameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(firstNameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(lastNameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(lastNameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(cityLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(cityField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(saveButton, gbc);

        idField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ea) {
                char c = ea.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
                    ea.consume();
                }
            }
        });

        saveButton.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String city = cityField.getText().trim();
            String email = emailField.getText().trim();
            String id = idField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || id.isEmpty() || city.isEmpty()) {
                JOptionPane.showMessageDialog(AddAuthorForm.this, "Please enter all data", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (Integer.parseInt(id) < 1) {
                JOptionPane.showMessageDialog(AddAuthorForm.this, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean authorExists = false;
                try {
                    SqlCon.open();
                    PreparedStatement ps = SqlCon.con.prepareStatement("SELECT COUNT(*) FROM author WHERE author_id = ?");
                    ps.setObject(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        authorExists = rs.getInt(1) > 0;
                    }
                    SqlCon.close();
                } catch (Exception eb) {
                    SqlCon.lastError = eb.getMessage();
                    System.out.println(eb);
                    authorExists = false;
                }

                if (authorExists) {
                    JOptionPane.showMessageDialog(AddAuthorForm.this, "This author already exists", "Duplicate author", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[] authorParameters = {id, city, firstName, lastName, email};
                    boolean result = SqlCon.Execute("INSERT INTO author VALUES(?, ?, ?, ?, ?)", authorParameters);
                    if (!result) {
                        JOptionPane.showMessageDialog(AddAuthorForm.this, "Error in data entry:\n" + SqlCon.lastError, "Database Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AddAuthorForm.this, firstName + " was added successfully!");
                        dispose();
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void openForm() {
        SwingUtilities.invokeLater(() -> new AddAuthorForm());
    }
}
