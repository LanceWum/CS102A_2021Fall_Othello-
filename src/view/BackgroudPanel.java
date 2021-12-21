package view;

import javax.swing.*;
import java.awt.*;

public class BackgroudPanel extends JPanel {
    Image im;

    public BackgroudPanel(Image im)
    {
        this.im=im;
        this.setOpaque(true);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        g.drawImage(im,0,0,this.getWidth(),this.getHeight(),this);
    }
}
