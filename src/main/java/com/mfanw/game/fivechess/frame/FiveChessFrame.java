package com.mfanw.game.fivechess.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

public class FiveChessFrame extends JFrame {

    public static final int PADDING_SIZE = 50;

    public static final int CELL_SIZE = 40;

    public static final int CHESS_SIZE = 32;

    private static final long serialVersionUID = -3127709948201127296L;

    public static final int TURN_X = PADDING_SIZE + CELL_SIZE * 7 - CHESS_SIZE / 2;

    public static final int TURN_Y = CHESS_SIZE / 2 - 10;

    private String title = "五子棋";

    private JPanel panel;

    /**
     * 当前顺序，true=黑旗，false=白旗
     */
    private boolean trun = true;

    /**
     * 当前棋盘，-1=空白，1=黑旗，0=白旗
     */
    private int[][] chesses = new int[15][15];

    /**
     * 当前胜利者，-1=空白，1=黑旗，0=白旗
     */
    private int success = -1;

    private List<Point> steps = Lists.newArrayList();

    public FiveChessFrame() {
        this.setTitle(title);
        this.setBounds(300, 100, 1000, 800);
        this.setLayout(null);
        panel = new FiveChessPanel(this);
        this.add(panel);
        // Add Button
        JButton startGameBtn = new JButton("开始游戏");
        startGameBtn.setBounds(800, 100, 100, 30);
        startGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }

        });
        this.add(startGameBtn);
        // Add Button
        JButton backupGameBtn = new JButton("保存进度");
        backupGameBtn.setBounds(800, 150, 100, 30);
        backupGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                backupGame(null);
            }

        });
        this.add(backupGameBtn);
        // Add Button
        JButton loadGameBtn = new JButton("加载进度");
        loadGameBtn.setBounds(800, 200, 100, 30);
        loadGameBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fd = new JFileChooser("D:/AAAAA/");
                fd.showOpenDialog(null);
                File f = fd.getSelectedFile();
                if (f != null) {
                    try {
                        String content = FileUtils.readFileToString(f);
                        if (content != null && !content.isEmpty()) {
                            List<Point> ps = JSON.parseArray(content, Point.class);
                            if (ps != null && !ps.isEmpty()) {
                                for (Point p : ps) {
                                    paintChessByIndex(p.x, p.y);
                                    Thread.sleep(1000);
                                }
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

        });
        this.add(loadGameBtn);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void startGame() {
        Graphics g = panel.getGraphics();
        g.setColor(new Color(205, 173, 0));
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        g.setColor(Color.BLACK);
        for (int i = 0; i < 15; i++) {
            g.drawLine(PADDING_SIZE + i * CELL_SIZE, PADDING_SIZE, PADDING_SIZE + i * CELL_SIZE, PADDING_SIZE + CELL_SIZE * 14);
            g.drawLine(PADDING_SIZE, PADDING_SIZE + i * CELL_SIZE, PADDING_SIZE + CELL_SIZE * 14, PADDING_SIZE + i * CELL_SIZE);
        }
        // chess初始化
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                chesses[x][y] = -1;
            }
        }
        // paint turn
        g.fillOval(TURN_X, TURN_Y, CHESS_SIZE, CHESS_SIZE);
        success = -1;
        steps = Lists.newArrayList();
    }

    public void paintChess(int x, int y) {
        // 获取chesses的index
        int indexX = (x - PADDING_SIZE + CELL_SIZE / 2 + 1) / CELL_SIZE;
        int indexY = (y - PADDING_SIZE + CELL_SIZE / 2 + 1) / CELL_SIZE;
        paintChessByIndex(indexX, indexY);
    }

    public void paintChessByIndex(int indexX, int indexY) {
        if (success > -1) {
            showWinDialog();
            return;
        }
        if (chesses[indexX][indexY] != -1) {
            return;
        }
        steps.add(new Point(indexX, indexY));
        // 获取棋盘中的point
        int pointX = indexX * CELL_SIZE - CHESS_SIZE / 2 + PADDING_SIZE;
        int pointY = indexY * CELL_SIZE - CHESS_SIZE / 2 + PADDING_SIZE;
        Graphics g = panel.getGraphics();
        Color tempColor = g.getColor();
        if (trun) {
            g.setColor(Color.BLACK);
            g.fillOval(pointX, pointY, CHESS_SIZE, CHESS_SIZE);
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(pointX, pointY, CHESS_SIZE, CHESS_SIZE);
            g.setColor(Color.BLACK);
        }
        // paint turn
        g.fillOval(TURN_X, TURN_Y, CHESS_SIZE, CHESS_SIZE);
        g.setColor(tempColor);
        chesses[indexX][indexY] = trun ? 1 : 0;
        if (checkWinner()) {
            success = trun ? 1 : 0;
            showWinDialog();
            return;
        }
        trun = !trun;
    }

    private void showWinDialog() {
        String winner = success == 1 ? "黑旗" : "白旗";
        int back = JOptionPane.showConfirmDialog(this, winner + "胜利！点击是保存棋局并重新游戏，点击否重新开始游戏，点击取消返回棋局~");
        if (back == JOptionPane.YES_OPTION) {
            backupGame(winner);
            startGame();
        } else if (back == JOptionPane.NO_OPTION) {
            startGame();
        }
    }

    private void backupGame(String winner) {
        DateFormat dateForamt = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File("D:/AAAAA/" + dateForamt.format(new Date()) + (winner == null ? "" : "_" + winner) + ".fcd");
        try {
            FileUtils.write(file, JSON.toJSONString(steps));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkWinner() {
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                if (chesses[x][y] < 0 || x > 10) {
                    continue;
                }
                int xCount = 1;
                int yCount = 1;
                int xyAACount = 1;
                int xyAMCount = 1;
                for (int p = 1; p < 5; p++) {
                    if (x + p < 15 && chesses[x + p][y] == chesses[x][y]) {
                        xCount++;
                    }
                    if (y + p < 15 && chesses[x][y + p] == chesses[x][y]) {
                        yCount++;
                    }
                    if (x + p < 15 && y + p < 15 && chesses[x + p][y + p] == chesses[x][y]) {
                        xyAACount++;
                    }
                    if (x + p < 15 && y - p > -1 && chesses[x + p][y - p] == chesses[x][y]) {
                        xyAMCount++;
                    }
                }
                if (xCount >= 5 || yCount >= 5 || xyAACount >= 5 || xyAMCount >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printChesses() {
        for (int y = 0; y < 15; y++) {
            for (int x = 0; x < 15; x++) {
                System.out.print((chesses[x][y] == -1 ? " " : chesses[x][y]) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        FiveChessFrame game = new FiveChessFrame();
        Thread.sleep(100);
        game.startGame();
    }

}
