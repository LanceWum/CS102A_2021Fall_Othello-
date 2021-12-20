package components;

import model.ChessPiece;
import view.ChessPanel;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class ChessGridPanel extends JLabel {
    public static int gridSize;
    private int row;
    private int col;
    private int color;

    public ChessGridPanel(int row, int col){
        this.row = row;
        this.col = col;
        this.setSize(gridSize,gridSize);
        ImageIcon chessColorIcon = new ImageIcon("src/resource/image/basegrid.png");
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(gridSize,gridSize, Image.SCALE_DEFAULT));//宽高根据需要设定
        this.setIcon(chessColorIcon);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void setChessColor(int color) {
        this.color = color;
        ImageIcon chessColorIcon;

        switch (color){
            case 0:
                chessColorIcon = new ImageIcon("src/resource/image/basegrid.png");
                break;
            case 1:
                chessColorIcon = new ImageIcon("src/resource/image/blackgrid.png");
                break;
            case -1:
                chessColorIcon = new ImageIcon("src/resource/image/whitegrid.png");
                break;
            case 100:
                chessColorIcon = new ImageIcon("src/resource/image/validgridtips.png");
                break;
            default:
                chessColorIcon = new ImageIcon("src/resource/image/basegrid.png");
                break;
        }

        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(gridSize,gridSize, Image.SCALE_DEFAULT));//宽高根据需要设定
        this.setIcon(chessColorIcon);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
}
