package view;

import components.ChessGridPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChessPanel extends JPanel {

    private final int CHESS_COUNT = 8;
    private ChessGridPanel[][] chessGrids;
    private GamePanel gamePanel;
    private boolean isShowValidSteps;

    public ChessPanel(int width, int height,GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        isShowValidSteps = false;
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(new GridLayout(8,8));

        //this.setBackground(Color.BLUE);
        int length = Math.min(width, height);
        this.setSize(length, length);
        //this.setLocation(25,0);
        ChessGridPanel.gridSize = length / CHESS_COUNT;
        //ChessGridComponent.chessSize = (int) (ChessGridComponent.gridSize * 0.8);
        initialChessGrids();//return empty chessboard
        //initialGame();//add initial four chess

        repaint();
    }

    /**
     * set an empty chessboard
     */
    public void initialChessGrids() {
        chessGrids = new ChessGridPanel[CHESS_COUNT][CHESS_COUNT];

        //draw all chess grids
        int i,j;
        for ( i = 0; i < CHESS_COUNT; i++) {
            for ( j = 0; j < CHESS_COUNT; j++) {
                ChessGridPanel gridComponent = new ChessGridPanel(i, j);
                //gridComponent.setLocation(j * ChessGridPanel.gridSize, i * ChessGridPanel.gridSize);
                chessGrids[i][j] = gridComponent;
                this.add(chessGrids[i][j]);
                chessGrids[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ChessGridPanel p = (ChessGridPanel) e.getComponent();
                        int row = p.getRow();
                        int col = p.getCol();
                        System.out.printf("grid %2d,%2d clicked!\n",row,col);
                        GameFrame.controller.doClick(row,col);
                        //GameFrame.controller.alphaGo();
                    }
                });
            }
        }
    }

    /**
     * initial origin four chess
     */
    public void initialGame() {
        chessGrids[3][3].setChessColor(1);
        chessGrids[3][4].setChessColor(-1);
        chessGrids[4][3].setChessColor(-1);
        chessGrids[4][4].setChessColor(1);
    }

    public void updateChessPanel(int[][] board){
        for (int i = 0; i < CHESS_COUNT; i++){
            for (int j = 0; j < CHESS_COUNT; j++){
                this.chessGrids[i][j].setChessColor(board[i][j]);
            }
        }

        if (this.gamePanel.getGame()!= null && this.isShowValidSteps)
        {
            showValidSteps(this.gamePanel.getGame().getPlayTurn());
        }
    }

    public void updateInfo(){}

    public void nullBoard(){
        for (int i = 0; i < CHESS_COUNT; i++){
            for (int j = 0; j < CHESS_COUNT; j++){
                this.chessGrids[i][j].setChessColor(0);
            }
        }
    }

    public void showValidSteps(int color){
        if (null == this.gamePanel.getGame())
            return;

        ArrayList<int[]> validSteps;

        if(color == 1){
            validSteps = this.gamePanel.getGame().getBlackValidGrids();
            for (int i = 0; i < validSteps.size(); i++){
                int row = validSteps.get(i)[0];
                int col = validSteps.get(i)[1];
                this.chessGrids[row][col].setChessColor(100);
            }
        }
        else
        {
            validSteps = this.gamePanel.getGame().getWhiteValidGrids();
            for (int i = 0; i < validSteps.size(); i++){
                int row = validSteps.get(i)[0];
                int col = validSteps.get(i)[1];
                this.chessGrids[row][col].setChessColor(100);
            }
        }
        updateUI();
    }

    public void hideValidSteps(){
        if (null == this.gamePanel.getGame())
            return;
        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if(this.chessGrids[i][j].getColor() == 100)
                    this.chessGrids[i][j].setChessColor(0);
            }
        }
        updateUI();
    }

    public void setShowValidSteps(boolean b){
        this.isShowValidSteps = b;
    }

}
