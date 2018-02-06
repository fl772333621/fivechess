package com.mfanw.game.fivechess.frame;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class FiveChessPanel extends JPanel {

    private static final long serialVersionUID = -2678297380541475893L;

    public FiveChessPanel(FiveChessFrame fiveChessFrame) {
        this.setBounds(50, 50, FiveChessFrame.CELL_SIZE * 14 + 1, FiveChessFrame.CELL_SIZE * 14 + 1);
        this.setBackground(Color.yellow);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fiveChessFrame.paintChess(e.getX(), e.getY());
                fiveChessFrame.setTitle("五子棋(" + e.getX() + "," + e.getY() + ")");
            }

        });
        this.setVisible(true);
    }


}
