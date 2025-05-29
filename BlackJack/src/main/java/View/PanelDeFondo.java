package View;

import java.awt.*;
import javax.swing.*;

public class MyBackgroundPanel extends JPanel {
    private Image background;

    public MyBackgroundPanel(Image background) {
        this.background = background;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Uso de interpolación bicubica para mayor calidad en el escalado
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}


