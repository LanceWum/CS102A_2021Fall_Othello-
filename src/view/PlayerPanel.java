package view;

import model.ChessPiece;
import model.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel{
    private int gap = 5;
    private JLabel avatarLabel;
    private JLabel chessColor;
    private JLabel playerName;
    private JLabel chessCount;
    private int chessNumber = 0;
    private Player player;
    private GamePanel gamePanel;
    JCheckBox cheatMode;

    public PlayerPanel(int width, int height,GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.player = null;
        this.setSize(width,height);
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        int h = gap;
        JButton btnPlayerSelect = new JButton("Player...");
        btnPlayerSelect.setSize((int)(0.9*this.getWidth()),30);
        btnPlayerSelect.setLocation(h,0);
        this.add(btnPlayerSelect);
        btnPlayerSelect.addActionListener(e -> {
            PlayerSelectDialog d = new PlayerSelectDialog(this);
            d.setVisible(true);
        });

        h += btnPlayerSelect.getHeight() + gap;
        avatarLabel = new JLabel("");
        avatarLabel.setSize((int)(0.9*this.getWidth()),(int)(1*this.getWidth()));
        avatarLabel.setLocation(gap,h);
        ImageIcon colorIcon = new ImageIcon("src\\resource\\image\\avartar\\defaultavatar.png");
        colorIcon.setImage(colorIcon.getImage().getScaledInstance((int)(0.9*this.getWidth()),(int)(1*this.getWidth()), Image.SCALE_DEFAULT));//宽高根据需要设定
        avatarLabel.setIcon(colorIcon);
        avatarLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.add(avatarLabel);

        h += avatarLabel.getHeight() + gap;
        playerName = new JLabel("",JLabel.CENTER);
        playerName.setFont(new Font("Calibri", Font.BOLD, 25));
        playerName.setSize((int)(0.9*this.getWidth()),30);
        playerName.setLocation(gap,h);
        this.add(playerName);

        h += playerName.getHeight() + gap;
        chessColor = new JLabel("");
        chessColor.setSize(50,50);
        chessColor.setLocation((int)(this.getWidth()/2-chessColor.getWidth()/2)-gap,h);
        ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\defaultchess.png");
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
        chessColor.setIcon(chessColorIcon);
        this.add(chessColor);

        h += chessColor.getHeight()+gap+gap+gap+gap;
        JLabel chessCountlabel = new JLabel("ChessNumber",JLabel.CENTER);
        chessCountlabel.setFont(new Font("Calibri", Font.BOLD, 15));
        chessCountlabel.setSize((int)(0.9*this.getWidth()),30);
        chessCountlabel.setLocation(0,h);
        this.add(chessCountlabel);

        h += chessCountlabel.getHeight()+gap;
        chessCount = new JLabel("",JLabel.CENTER);
        chessCount.setFont(new Font("Calibri", Font.BOLD, 30));
        chessCount.setSize((int)(0.9*this.getWidth()),30);
        chessCount.setLocation(0,h);
        this.add(chessCount);

        h += chessCountlabel.getHeight()+gap+50;

        JButton btnRetract = new JButton("Retract");
        btnRetract.setSize((int)(0.9*this.getWidth()),30);
        btnRetract.setLocation(0,h);
        this.add(btnRetract);
        btnRetract.addActionListener(e -> {
            if (this.player != null){
                this.gamePanel.retract(this.player);
            }
        });

        h += btnRetract.getHeight()+gap+gap;
        cheatMode = new JCheckBox("CheatMode");
        cheatMode.setSize(120,30);

        cheatMode.setLocation(0,h);
        cheatMode.addActionListener(e -> {
            this.player.setCheatMode(this.cheatMode.isSelected());
            if(this.gamePanel.getGame() != null)
                this.gamePanel.getGame().updateCheatModeInfo(this.player);
        });
        this.add(cheatMode);

        repaint();
    }

    public Player getPlayer(){
        return this.player;
    }
    public void setPlayer(Player p){
     this.player = p;
    }

    public void updateInfo(){
        ImageIcon colorIcon = this.player.getAvartar();
        colorIcon.setImage(colorIcon.getImage().getScaledInstance((int)(0.9*this.getWidth()),(int)(1*this.getWidth()), Image.SCALE_DEFAULT));//宽高根据需要设定
        avatarLabel.setIcon(colorIcon);
        avatarLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        String strFileName = "";

        if (this.player.getColor() == 1){
            strFileName = "src\\resource\\image\\blackchess.png";
        }
        else if (this.player.getColor() == -1){
            strFileName = "src\\resource\\image\\whitechess.png";
        }
        else
            strFileName = "src\\resource\\image\\defaultchess.png";

        ImageIcon chessColorIcon = new ImageIcon(strFileName);
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
        chessColor.setIcon(chessColorIcon);
        playerName.setText(this.player.getName());
        updateChessCount();
        chessCount.setText(Integer.toString(this.chessNumber));
    }

    public void updateGame(Player p){

    }

    public void resetInfo(){
        if (player == null)
            return;

        ImageIcon colorIcon = player.getAvartar();
        colorIcon.setImage(colorIcon.getImage().getScaledInstance((int)(0.9*this.getWidth()),(int)(1*this.getWidth()), Image.SCALE_DEFAULT));//宽高根据需要设定
        avatarLabel.setIcon(colorIcon);
        avatarLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\defaultchess.png");
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
        chessColor.setIcon(chessColorIcon);
        playerName.setText(player.getName());
        chessCount.setText("");
    }

    public void setChessColor(ChessPiece color){
        if (color == ChessPiece.BLACK){
            ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\blackchess.png");
            chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
            chessColor.setIcon(chessColorIcon);
        }
        else
        {
            ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\whitechess.png");
            chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
            chessColor.setIcon(chessColorIcon);
        }
    }

    public void updateChessCount(){
        if (null != this.gamePanel.getGame()){
            this.chessNumber = this.gamePanel.getGame().getChessCount(this.player.getColor());
        }
    }

}
