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
        this.setSize(880,580);

        statusPanel = new StatusPanel(880,58,this);
        statusPanel.setLocation(0,0);

        leftPlayerPanel = new PlayerPanel(170,522,this,0);
        leftPlayerPanel.setLocation(0,58);

        chessBoardPanel = new ChessPanel(540,522,this);
        chessBoardPanel.setLocation(170,58);

        rightPlayerPanel = new PlayerPanel(172,522,this,1);
        rightPlayerPanel.setLocation(leftPlayerPanel.getWidth()+chessBoardPanel.getWidth(),58);

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
        this.leftPlayerPanel.updateInfo();
        this.rightPlayerPanel.updateInfo();
        this.chessBoardPanel.updateChessPanel(this.game.getBoard());
        this.statusPanel.updateInfo();
    }

    public boolean canClick(int row, int col,int a){
        return true;
    }

    public void initGame(){
        this.statusPanel.resetInfo();
        this.chessBoardPanel.nullBoard();
        this.leftPlayerPanel.resetInfo();
        this.rightPlayerPanel.resetInfo();
    }
    public void clearGame(){
        this.statusPanel.resetInfo();
        this.chessBoardPanel.nullBoard();
        this.leftPlayerPanel.setPlayer(null);
        this.leftPlayerPanel.resetInfo();
        this.rightPlayerPanel.setPlayer(null);
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
