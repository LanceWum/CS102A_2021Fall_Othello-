package view;

import model.ChessPiece;
import model.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel{
    private int gap = 8;
    int position; //0: left, 1:right
    private JLabel avatarLabel;
    private JLabel chessColor;
    private JLabel playerName;
    private JLabel chessCount;
    private int chessNumber = 0;
    private Player player;
    private GamePanel gamePanel;
    JCheckBox cheatMode;
    JLabel chessCountlabel;

    public PlayerPanel(int width, int height,GamePanel gamePanel,int pos) {
        this.gamePanel = gamePanel;
        this.position = pos;
        this.player = null;
        this.setSize(width,height);
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setBorder(BorderFactory.createRaisedBevelBorder());

        int h = gap;
        JButton btnPlayerSelect = new JButton("Select Player");
        btnPlayerSelect.setSize((int)(0.9*this.getWidth()),30);
        btnPlayerSelect.setLocation(h,5);
        this.add(btnPlayerSelect);
        btnPlayerSelect.addActionListener(e -> {
            PlayerSelectDialog d;
            if(GameFrame.controller.getGameMode().equals("HvsH"))
                d = new PlayerSelectDialog(this,0);
            else if (GameFrame.controller.getGameMode().equals("HvsM"))
            {
                if(this.position == 0)//left
                    d = new PlayerSelectDialog(this,0);
                else
                    d = new PlayerSelectDialog(this,1);
            }
            else
                d = new PlayerSelectDialog(this,1);

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
        playerName.setForeground(Color.WHITE);
        playerName.setSize((int)(0.9*this.getWidth()),30);
        playerName.setLocation(gap,h);
        this.add(playerName);

        h += playerName.getHeight() + gap;
        chessColor = new JLabel("");
        chessColor.setSize(50,50);
        chessColor.setLocation((int)(this.getWidth()/2-chessColor.getWidth()/2),h);
        ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\defaultchess.png");
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
        chessColor.setIcon(chessColorIcon);
        this.add(chessColor);

        h += chessColor.getHeight()+gap+gap+gap+gap;
        chessCountlabel = new JLabel("ChessNumber",JLabel.CENTER);
        chessCountlabel.setFont(new Font("Calibri", Font.BOLD, 20));
        chessCountlabel.setForeground(Color.WHITE);
        chessCountlabel.setSize(this.getWidth(),30);
        chessCountlabel.setLocation(0,h);
        chessCountlabel.setVisible(false);
        this.add(chessCountlabel);

        h += chessCountlabel.getHeight()+gap;
        chessCount = new JLabel("",JLabel.CENTER);
        chessCount.setFont(new Font("Calibri", Font.BOLD, 30));
        chessCount.setSize(this.getWidth(),30);
        chessCount.setForeground(Color.WHITE);
        chessCount.setLocation(0,h);
        this.add(chessCount);

        h += chessCountlabel.getHeight()+gap+20;

        JButton btnRetract = new JButton("Retract");
        btnRetract.setSize((int)(0.9*this.getWidth()),30);
        btnRetract.setLocation(gap,h);
        this.add(btnRetract);
        btnRetract.addActionListener(e -> {
            if (this.player != null){
                this.gamePanel.retract(this.player);
            }
        });

        h += btnRetract.getHeight()+gap+gap;
        cheatMode = new JCheckBox("CheatMode");
        cheatMode.setSize(120,30);
        cheatMode.setOpaque(false);
        cheatMode.setForeground(Color.white);

        cheatMode.setLocation(gap,h);
        cheatMode.addActionListener(e -> {
            if(this.player == null)
                return;
            this.player.setCheatMode(this.cheatMode.isSelected());
            if(this.gamePanel.getGame() != null)
                this.gamePanel.getGame().updateCheatModeInfo(this.player);
        });
        cheatMode.setEnabled(false);
        this.add(cheatMode);

        BackgroudPanel bgp = new BackgroudPanel(new ImageIcon("src\\resource\\image\\zbq.png").getImage());
        bgp.setBounds(0,0,width,height);
        this.add(bgp);

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
        cheatMode.setEnabled(true);
        chessCountlabel.setVisible(true);
    }

    public void updateGame(Player p){

    }

    public void resetInfo(){
        ImageIcon colorIcon;
        if( player == null)
            colorIcon = new ImageIcon("src\\resource\\image\\defaultavartar.png");
        else
            colorIcon = player.getAvartar();

        colorIcon.setImage(colorIcon.getImage().getScaledInstance((int)(0.9*this.getWidth()),(int)(1*this.getWidth()), Image.SCALE_DEFAULT));//宽高根据需要设定
        avatarLabel.setIcon(colorIcon);
        avatarLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        ImageIcon chessColorIcon = new ImageIcon("src\\resource\\image\\defaultchess.png");
        chessColorIcon.setImage(chessColorIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT));//宽高根据需要设定
        chessColor.setIcon(chessColorIcon);
        if(player == null)
            playerName.setText("");
        else
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
