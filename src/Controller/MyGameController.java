package Controller;

import model.ChessPiece;
import model.Game;
import model.GameSystem;


import model.Player;
import view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MyGameController {
    private GameSystem gameSystem;
    private GamePanel gamePanel;
    private Timer aiTimer;
    private Timer autoPlayTimer;
    private String strGameMode;
    private int currentGameType; //0: realtime; 1: history
    private static int currentGameStepIndex = 0;
    private int currentGameStepCnt;

    public MyGameController(GameSystem gameSystem,GamePanel gamePanel){
        this.gameSystem = gameSystem;
        this.gamePanel = gamePanel;
        this.strGameMode = "PvP";
        this.currentGameType = 0;

        this.aiTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != GameFrame.controller)
                    GameFrame.controller.alphaGo();
            }
        });

        this.autoPlayTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameFrame.controller.doAutoPlay();
            }
        });
    }
    public GamePanel getGamePanel(){return this.gamePanel;}
    public GameSystem getGameSystem(){return  this.gameSystem;}
    public String getGameMode(){return this.strGameMode;}
    public void setGameMode(String s){ this.strGameMode = s;}

    public int newGame(String gameName){
        Player leftPlayer = this.gamePanel.getLeftPlayer();
        Player rightPlayer = this.gamePanel.getRightPlayer();

        if (leftPlayer == null || rightPlayer == null){
            return 1;
        }
        if(leftPlayer.getPid() == rightPlayer.getPid()){
            return 2;
        }

        Game g = this.gameSystem.newGame(gameName,leftPlayer,rightPlayer);
        currentGameStepCnt = g.getStepList().size();
        this.gamePanel.newGame(g);
        if(g.getWhitePlayer().getPlayerType() != 0 || g.getBlackPlayer().getPlayerType() != 0)
            this.aiTimer.start();
        else
            this.aiTimer.stop();

        return 0;
    }

    public void initGame(){
        this.gameSystem.clearGame();
        this.gamePanel.initGame();
    }

    public void doClick(int row, int col) {
        if(gameSystem.getCurrentGame()==null)
            return;

        if(gameSystem.getCurrentGame().canClick(row,col)){
            getGameSystem().getCurrentGame().addStep(row,col);

            int ret = gameSystem.getCurrentGame().updateGameStatus();
            if (ret == 0){
                this.gameSystem.getCurrentGame().endGame();
            }
            else if (ret == 2){
                gameSystem.getCurrentGame().swapTurn();
            }
            this.gamePanel.updateInfo();
        }
    }
    public void updateChessPanel(){

    }

    public void alphaGo(){
        if (this.gameSystem.getCurrentGame() != null){
            ArrayList<Integer> step = this.gameSystem.getCurrentGame().getAlphaGoStep();
            if (step.size() > 0)
            {
                int row = step.get(0).intValue();
                int col = step.get(1).intValue();
                this.doAlphaGoClick(row,col);
            }
        }
    }

    public void doAlphaGoClick(int row, int col) {
        if(gameSystem.getCurrentGame()==null)
            return;

        getGameSystem().getCurrentGame().addStep(row,col);

        int ret = gameSystem.getCurrentGame().updateGameStatus();
        if (ret == 0){
            this.gameSystem.getCurrentGame().endGame();
        }
        else if (ret == 2){
            gameSystem.getCurrentGame().swapTurn();
        }
        this.gamePanel.updateInfo();
    }
    public void saveGame(){
        if(this.gameSystem.getCurrentGame() != null)
            this.gameSystem.getCurrentGame().saveGame();
    }

    public void setGame(int gid){
        if(this.gameSystem.setCurrentGame(gid)){
            this.gamePanel.setGame(this.gameSystem.getCurrentGame());
            this.gamePanel.updateInfo();
        }
    }

    public void loadGame(int gid)
    {
        Game g = gameSystem.getGame(gid);
        if(g != null){
            currentGameStepCnt = g.getStepList().size();
            int steps = g.getStepList().size();
            g.loadHistoryGame(steps);
            this.gameSystem.setCurrentGame(gid);
            this.gamePanel.setGame(this.gameSystem.getCurrentGame());
            this.gamePanel.updateInfo();
        }
    }

    public void startAutoPlay()
    {
        GameFrame.btnAutoPlay.setEnabled(false);
        autoPlayTimer.start();
    }

    public void doAutoPlay(){
        Game g = gameSystem.getCurrentGame();
        g.loadHistoryGame(currentGameStepIndex++);
        this.gameSystem.setCurrentGame(g.getGid());
        this.gamePanel.setGame(this.gameSystem.getCurrentGame());
        this.gamePanel.updateInfo();
        if(currentGameStepIndex > currentGameStepCnt) {
            autoPlayTimer.stop();
            currentGameStepIndex = 0;
            GameFrame.btnAutoPlay.setEnabled(true);
        }
    }
}
