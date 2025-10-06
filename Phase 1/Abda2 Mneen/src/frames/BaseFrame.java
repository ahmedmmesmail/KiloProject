package frames;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class BaseFrame extends JFrame {

    protected JPanel mainPanel;

    public BaseFrame(String title, boolean showBackButton) {
        super(title);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/icon.png")));

        ImageIcon backIcon = new ImageIcon(getClass().getResource("/images/back.png"));



        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(new Color(0x1E1E1E));
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        toolbar.setLayout(new BorderLayout());
        toolbar.setPreferredSize(new Dimension(0, 60));

        // أيقونة + عنوان
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(new Color(0x1E1E1E));
        JLabel iconLabel = new JLabel(icon);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        // زر رجوع
        JButton backButton = new JButton(backIcon);
        backButton.setPreferredSize(new Dimension(50, 40));
        backButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        backButton.setFocusPainted(false);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x3FB8FF));
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            dispose(); // يقفل الفريم الحالي
            new MainFrame(); // يفتح المين
        });

        backButton.setVisible(showBackButton);

        // زر خروج
        JButton exitButton = new JButton("X");
        exitButton.setPreferredSize(new Dimension(50, 40));
        exitButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exitButton.setFocusPainted(false);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(new Color(0xFF5B4B));
        exitButton.setBorderPainted(false);
        exitButton.addActionListener(e -> System.exit(0));


        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);
        leftPanel.add(backButton);


        // إضافة العناصر للتولبار
        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(exitButton, BorderLayout.EAST);

        add(toolbar, BorderLayout.NORTH);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0x121212));
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    protected void styleButton(RoundedButton btn) {
        btn.setBackground(new Color(0x2563EB));
        btn.setForeground(new Color(0xf3f8ed));
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setPreferredSize(new Dimension(200, 35));
        btn.setFocusPainted(false);
    }
    protected void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(new Color(0x2563EB));
    }

    protected void styleField(JTextField tf) {
        tf.setPreferredSize(new Dimension(200, 35));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 16));

    }

}
