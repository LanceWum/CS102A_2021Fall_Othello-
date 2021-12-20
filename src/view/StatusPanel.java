package view;

import model.ChessPiece;
import model.GameStatus;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel leftTipsLabel,rightTipsLabel;
    private JLabel gameNameLabel;
    private JLabel gameTimeLabel;
    private GamePanel gamePanel;
    private ImageIcon winIcon;
    private ImageIcon arrowIcon;

    public StatusPanel(int width, int height,GamePanel gamePanel) {
        int gap = 5;
        int w = 0;
        this.gamePanel = gamePanel;
        this.setSize(width, height);
        this.setLayout(null);
        this.setVisible(true);

        this.leftTipsLabel = new JLabel("");
        this.leftTipsLabel.setLocation(10, 10);
        this.leftTipsLabel.setSize((int) (width * 0.12), height);
        this.add(leftTipsLabel);
        w += leftTipsLabel.getWidth()+gap;
        this.gameNameLabel = new JLabel("",JLabel.RIGHT);
        this.gameNameLabel.setLocation(w, 10);
        this.gameNameLabel.setSize((int)(width*0.4), height);
        this.gameNameLabel.setFont(new Font("Calibri", Font.ITALIC, 25));
        this.add(gameNameLabel);

        w += gameNameLabel.getWidth()+gap;
        this.gameTimeLabel = new JLabel("",JLabel.RIGHT);
        this.gameTimeLabel.setLocation(w, 10);
        this.gameTimeLabel.setSize((int)(width*0.3), height);
        this.gameTimeLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        this.add(gameTimeLabel);

        w += gameTimeLabel.getWidth()+gap;
        this.rightTipsLabel = new JLabel("",JLabel.CENTER);
        this.rightTipsLabel.setLocation(w+30, 10);
        this.rightTipsLabel.setSize((int)(width*0.15), height);
        this.add(rightTipsLabel);

        this.winIcon = new ImageIcon("src/resource/image/win.png");
        this.arrowIcon = new ImageIcon("src/resource/image/arrow.png");
    }


    public void setPlayerText(String playerText) {
        this.leftTipsLabel.setText(playerText);
    }

    public void setGameName(String name){
        this.gameNameLabel.setText(name);
    }
    public void setGameTime(String time) {this.gameTimeLabel.setText(time);}

    public void updateInfo(){
        if (this.gamePanel.getGame().getGameStatus() == GameStatus.OVER){
            String strTips = "";
            if (this.gamePanel.getGame().getWinnerColor() == -1){
                if(this.gamePanel.getLeftPlayer().getColor() == -1){
                    this.leftTipsLabel.setIcon(winIcon);
                    this.rightTipsLabel.setIcon(null);
                }
                else
                {
                    this.rightTipsLabel.setIcon(new ImageIcon("src/resource/image/win.png"));
                    this.leftTipsLabel.setIcon(null);
                }
            }
            else if (this.gamePanel.getGame().getWinnerColor() == 1){
                if(this.gamePanel.getLeftPlayer().getColor() == 1){
                    this.leftTipsLabel.setIcon(new ImageIcon("src/resource/image/win.png"));
                    this.rightTipsLabel.setIcon(null);
                }
                else
                {
                    this.rightTipsLabel.setIcon(new ImageIcon("src/resource/image/win.png"));
                    this.leftTipsLabel.setIcon(null);
                }
            }
            else
            {
                this.leftTipsLabel.setIcon(new ImageIcon("src/resource/image/win.png"));
                this.rightTipsLabel.setIcon(new ImageIcon("src/resource/image/win.png"));
            }
            return;
        }

        if (1 == this.gamePanel.getGame().getPlayTurn()){//black turn
            if (this.gamePanel.getLeftPlayer().getColor() == 1){//left is black
                this.leftTipsLabel.setIcon(new ImageIcon("src/resource/image/arrow.png"));
                this.rightTipsLabel.setIcon(null);
            }
            else{//left is white
                this.rightTipsLabel.setIcon(new ImageIcon("src/resource/image/arrow.png"));
                this.leftTipsLabel.setIcon(null);
            }
        }
        else//white turn
            if (this.gamePanel.getLeftPlayer().getColor() == 1){//left is black
                this.rightTipsLabel.setIcon(new ImageIcon("src/resource/image/arrow.png"));
                this.leftTipsLabel.setIcon(null);
            }
            else{//left is white
                this.leftTipsLabel.setIcon(new ImageIcon("src/resource/image/arrow.png"));
                this.rightTipsLabel.setIcon(null);
            }

        this.gameNameLabel.setText(this.gamePanel.getGame().getName());
        this.gameTimeLabel.setText(this.gamePanel.getGame().getPlayTime());
    }
}
