package view;


import controller.MyGameController;
import model.GameSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {

    public static MyGameController controller;
    JCheckBox showValidStep;
    public static JButton btnAutoPlay,btnReStart;

    public GameFrame(int frameSize) {
        GameSystem gameSystem = new GameSystem();
        this.setTitle("2021F CS102A Project Reversi");
        this.setLayout(null);
        this.setSize(880,680);
        GamePanel gamePanel = new GamePanel(880);
        gamePanel.setLocation(0,0);

        controller = new MyGameController(gameSystem,gamePanel);
        StartFrame startFrame = new StartFrame(this);

        btnReStart = new JButton("ReStart");
        btnReStart.setSize(80,30);
        btnReStart.setLocation((int)(0.195*this.getWidth()),gamePanel.getHeight()+15);
        btnReStart.addActionListener(e -> {
            controller.restartGame();
        });
        btnReStart.setEnabled(false);

        JButton btnStart = new JButton("NewGame");
        btnStart.setSize(96,30);
        btnStart.setLocation((int)(0.33*this.getWidth()),gamePanel.getHeight()+15);
        btnStart.addActionListener(e -> {
            if (gamePanel.getLeftPlayer() == null || gamePanel.getRightPlayer() == null){
                JOptionPane.showMessageDialog(this,"Please select player!");
                return;
            }
            if(gamePanel.getLeftPlayer().getPid() == gamePanel.getRightPlayer().getPid()){
                JOptionPane.showMessageDialog(this,"Player is the same!");
                return;
            }
            String gameName = JOptionPane.showInputDialog(this, "input the game name here");
            if(gameName == null || gameName.equals("")) {
                JOptionPane.showMessageDialog(this, "Please input game name!");
                return;
            }
            controller.newGame(gameName);
            btnAutoPlay.setVisible(false);
            btnReStart.setEnabled(true);
            //gamePanel.getStatusPanel().clearWinTips();
        });
        JButton btnLoad = new JButton("Load");
        btnLoad.setSize(80,30);
        btnLoad.setLocation((int)(0.46*this.getWidth()),gamePanel.getHeight()+15);
        btnLoad.addActionListener(e -> {
            LoadGameDialog d = new LoadGameDialog();
            d.setVisible(true);
        });

        btnAutoPlay = new JButton("AutoPlay");
        btnAutoPlay.setSize(100,30);
        btnAutoPlay.setLocation((int)(0.45*this.getWidth())+105,gamePanel.getHeight()+15);
        btnAutoPlay.setVisible(false);
        btnAutoPlay.addActionListener(e -> {
            GameFrame.controller.startAutoPlay();
        });

        JButton btnSave = new JButton("Save");
        btnSave.setSize(80,30);
        btnSave.setLocation((int)(0.68*this.getWidth())+10,gamePanel.getHeight()+15);
        btnSave.addActionListener(e -> {
            //System.out.println("save  click!");
            controller.saveGame();
        });
        JButton btnGameMode = new JButton("GameMode");
        btnGameMode.setSize(110,30);
        btnGameMode.setLocation((int)(0.84*this.getWidth()),gamePanel.getHeight()+15);
        btnGameMode.addActionListener(e -> {
            startFrame.setVisible(true);
            //gamePanel.initGame();
        });

        showValidStep = new JCheckBox("ShowValidStep");
        showValidStep.setSize(120,30);
        showValidStep.setLocation(10,gamePanel.getHeight()+15);
        showValidStep.addActionListener(e -> {
            if(gamePanel.getGame() == null)
                return;
            GameFrame.controller.getGamePanel().getChessBoardPanel().setShowValidSteps(this.showValidStep.isSelected());
            if (this.showValidStep.isSelected())
                GameFrame.controller.getGamePanel().getChessBoardPanel().showValidSteps(controller.getGamePanel().getGame().getPlayTurn());
            else{
                controller.getGamePanel().getChessBoardPanel().hideValidSteps();
            }
        });
        //showValidStep.setEnabled(false);

        this.add(gamePanel);
        this.add(btnReStart);
        this.add(btnStart);
        this.add(btnLoad);
        this.add(btnAutoPlay);
        this.add(btnSave);
        this.add(btnGameMode);
        this.add(showValidStep);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width-this.getWidth())/2,(int)(height-this.getHeight())/2-15);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        startFrame.setVisible(true);
    }

    public static void setAutoPlay(boolean b){
        btnAutoPlay.setVisible(b);
    }
    public static void setRestartButton(boolean b){
        btnReStart.setEnabled(b);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            GameFrame mainFrame = new GameFrame(600);
            mainFrame.setVisible(true);
            MusicPlay.playBGMusic();
        });
    }
}