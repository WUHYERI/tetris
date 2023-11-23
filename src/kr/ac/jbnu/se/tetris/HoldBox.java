package kr.ac.jbnu.se.tetris;

import javax.swing.*;
import java.awt.*;

public class HoldBox extends JPanel {

    public void paint (Graphics g) {
        super.paint(g);



        int holdBoxTop = 50;
        int holdBoxWidth = 120;
        int holdBoxHeight = 120;

        g.setColor(Color.BLUE);
        g.drawRect(0,holdBoxTop, holdBoxWidth, holdBoxHeight);

    }





}
