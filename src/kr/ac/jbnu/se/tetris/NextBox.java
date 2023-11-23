package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;

public class NextBox extends JPanel {
    public void paint (Graphics g) {
        super.paint(g);


        int NextBoxTop = 50;
        int NextBoxWidth = 120;
        int NextBoxHeight = 120;

        g.setColor(Color.BLUE);
        g.drawRect(0, NextBoxTop, NextBoxWidth, NextBoxHeight);

    }
}
