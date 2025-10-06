package frames;

import sql.SqlCon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AddPubForm extends BaseFrame {

    public AddPubForm() {
        super("Add Publisher", true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel codeLabel = new JLabel("Code:");
        JTextField codeField = new JTextField();

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();

        JLabel cityLabel = new JLabel("City:");
        JTextField cityField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        RoundedButton saveButton = new RoundedButton("Save");
        styleButton(saveButton);

        JLabel[] labels = {codeLabel, nameLabel, phoneLabel, cityLabel, emailLabel};
        for (JLabel lbl : labels) styleLabel(lbl);

        JTextField[] tf = {codeField, nameField, phoneField, cityField, emailField};
        for (JTextField fld : tf) styleField(fld);

        // إضافة العناصر
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(codeLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(codeField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(phoneLabel, gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(phoneField, gbc);

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

        // phone validation
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ea) {
                char c = ea.getKeyChar();
                if (!Character.isDigit(c) && c != '+' && c != KeyEvent.VK_BACK_SPACE) {
                    ea.consume();
                }
            }
        });

        // زر الحفظ
        saveButton.addActionListener(e -> {
            String code = codeField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String city = cityField.getText().trim();
            String email = emailField.getText().trim();

            if (code.isEmpty() || name.isEmpty() || phone.isEmpty() || city.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all data", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (phone.length() > 15 || phone.length() < 9) {
                JOptionPane.showMessageDialog(this, "Invalid phone number", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                boolean publisherExists = false;
                try {
                    SqlCon.open();
                    PreparedStatement ps = SqlCon.con.prepareStatement("SELECT COUNT(*) FROM publisher WHERE publisher_code = ?");
                    ps.setObject(1, code);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        publisherExists = rs.getInt(1) > 0;
                    }
                    SqlCon.close();
                } catch (Exception eb) {
                    SqlCon.lastError = eb.getMessage();
                    publisherExists = false;
                }

                if (publisherExists) {
                    JOptionPane.showMessageDialog(this, "This publisher already exists", "Duplicate Publisher", JOptionPane.ERROR_MESSAGE);
                } else {
                    Object[] pubParameters = {code, name, city, phone, email};
                    boolean result = SqlCon.Execute("insert into publisher values(?, ?, ?, ?, ?)", pubParameters);
                    if (!result) {
                        JOptionPane.showMessageDialog(this, "Error in data entry:\n" + SqlCon.lastError, "Database Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, name + " was added successfully!");
                        dispose();
                    }
                }
            }
        });

        setVisible(true);
    }


    public static void openForm() {
        SwingUtilities.invokeLater(() -> new AddPubForm());
    }

}
