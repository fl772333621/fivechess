package com.mfanw.game.fivechess.frame;

public class FiveChessMain {

    private FiveChessFrame game = new FiveChessFrame();

    public void startGame() {
        game.startGame();
    }

    public void paintChess(int x, int y) {
        game.paintChess(x, y);
    }

    public void paintChessByIndex(int indexX, int indexY) {
        game.paintChessByIndex(indexX, indexY);
    }

    /**
     * 当前顺序，true=黑旗，false=白旗
     */
    public boolean getTurn() {
        return game.getTurn();
    }

    /**
     * 判断是否是自动模式
     */
    public boolean isAutoType() {
        return game.isAutoType();
    }

    /**
     * 设置自动模式
     */
    public void setAutoType(boolean autoType) {
        game.setAutoType(autoType);
    }

    /**
     * 获取成功状态
     */
    public int getSuccess() {
        return game.getSuccess();
    }

    public int[][] getChesses() {
        return game.getChesses();
    }

    public static void main(String[] args) throws Exception {
        FiveChessMain main = new FiveChessMain();
        Thread.sleep(100);
        main.startGame();
    }

}
