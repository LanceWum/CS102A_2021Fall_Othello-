package view;

import controller.MyGameController;
import model.ChessPiece;
import model.Game;
import model.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    int gap = 8;
    private Game game;
    //private ChessBoardPanel chessBoardPanel;
    private StatusPanel statusPanel;
    private PlayerPanel leftPlayerPanel,rightPlayerPanel;
    private ChessPanel chessBoardPanel;

    public GamePanel(int size){
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        //this.setBorder(BorderFactory.createRaisedBevelBorder());
        this.setSize(850,580);

        statusPanel = new StatusPanel((int)(0.9*this.getWidth()),(int)(0.1*this.getHeight()),this);
        statusPanel.setLocation((int)(0.05*this.getWidth()),0);

        leftPlayerPanel = new PlayerPanel((int)(0.18*this.getWidth()),(int)(0.9*this.getHeight()),this);
        leftPlayerPanel.setLocation(gap,statusPanel.getHeight());

        chessBoardPanel = new ChessPanel((int)(0.64*this.getWidth()),(int)(0.9*this.getHeight()),this);
        chessBoardPanel.setLocation(gap+leftPlayerPanel.getWidth()+gap,statusPanel.getHeight());

        rightPlayerPanel = new PlayerPanel((int)(0.18*this.getWidth()),(int)(0.9*this.getHeight()),this);
        rightPlayerPanel.setLocation(gap+leftPlayerPanel.getWidth()+gap+chessBoardPanel.getWidth()+gap,
                statusPanel.getHeight());

        this.add(statusPanel);
        this.add(leftPlayerPanel);
        this.add(chessBoardPanel);
        this.add(rightPlayerPanel);
        statusPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //panels[row][col].setBorder(.....);
                System.out.println("aaaa click!");
            }
            // ..... more
        });
    }


    public void resetGame(){
        statusPanel.updateInfo();
        leftPlayerPanel.resetInfo();
        //chessBoardPanel.resetInfo();
        rightPlayerPanel.resetInfo();
        //this.getController().setGameStatus(0);
    }

    public Player getLeftPlayer(){ return this.leftPlayerPanel.getPlayer();}
    public Player getRightPlayer(){ return this.rightPlayerPanel.getPlayer();}
    public PlayerPanel getLeftPlayerPanel(){return this.leftPlayerPanel;}
    public PlayerPanel getRightPlayerPanel(){return this.rightPlayerPanel;}
    public StatusPanel getStatusPanel(){return this.statusPanel;}
    public Game getGame(){return this.game;}
    public ChessPanel getChessBoardPanel(){return this.chessBoardPanel;}

    public void newGame(Game g){
        this.game = g;
        if (g.getWhitePlayer().getPid() == this.leftPlayerPanel.getPlayer().getPid()){
            this.leftPlayerPanel.setChessColor(ChessPiece.WHITE);
            this.rightPlayerPanel.setChessColor(ChessPiece.BLACK);
        }
        else
        {
            this.leftPlayerPanel.setChessColor(ChessPiece.BLACK);
            this.rightPlayerPanel.setChessColor(ChessPiece.WHITE);
        }
        this.statusPanel.updateInfo();
        this.leftPlayerPanel.updateInfo();
        this.rightPlayerPanel.updateInfo();
        this.chessBoardPanel.updateChessPanel(g.getBoard());
    }

    public void updateInfo(){
        this.statusPanel.updateInfo();
        this.leftPlayerPanel.updateInfo();
        this.rightPlayerPanel.updateInfo();
        this.chessBoardPanel.updateChessPanel(this.game.getBoard());
    }

    public boolean canClick(int row, int col,int a){
        return true;
    }

    public void initGame(){
        this.statusPanel.setGameName("");
        this.statusPanel.setPlayerText("");
        this.statusPanel.setGameTime("");
        this.chessBoardPanel.nullBoard();
        this.leftPlayerPanel.resetInfo();
        this.rightPlayerPanel.resetInfo();
    }

    public void retract(Player p){
        if (null == this.getGame())
            return;
        if (this.getGame().retract(p.getColor()))
            this.updateInfo();
    }

    public void setGame(Game g){
        this.game = g;
        this.leftPlayerPanel.setPlayer(g.getWhitePlayer());
        this.rightPlayerPanel.setPlayer(g.getBlackPlayer());
        //this.chessBoardPanel.
    }
}
