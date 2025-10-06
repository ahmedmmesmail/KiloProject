package frames;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed()) {
            g2.setColor(getBackground().darker());
        } else {
            g2.setColor(getBackground());
        }

        int arc = 25;
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = new Rectangle(0, 0, getWidth(), getHeight());
        int x = r.x + (r.width - fm.stringWidth(getText())) / 2;
        int y = r.y + ((r.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
