package com.mfanw.game.fivechess.frame;

public class FiveChessMain {

    public static void main(String[] args) throws Exception {
        FiveChessFrame game = new FiveChessFrame();
        Thread.sleep(100);
        game.startGame();
    }

}
