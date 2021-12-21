package view;

import components.ChessGridPanel;
import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class HistoryGameDialog extends JDialog {
    private Game game;
    private static ChessGridPanel[][] chessGrids;
    private static ArrayList<int[][]> boardList = new ArrayList<>();
    private static int[][] board;// = new int[8][8];
    private static Timer timer;
    private static int boardIndex = 0;
    private static JButton btnAuto;


    public HistoryGameDialog(int gid){
        this.game = GameFrame.controller.getGameSystem().getGame(gid);
        initBoardList();
        //GameFrame.controller.getGameSystem().setCurrentGame(gid)
        String strTitle = "";
        strTitle = this.game.getName() + "    "
                + this.game.getPlayTime() + "    "
                + "black:  " + this.game.getBlackPlayer().getName() + "    "
                + "white:  " + this.game.getWhitePlayer().getName() + "    ";
        switch (this.game.getWinnerColor())
        {
            case -1:
                strTitle += "winner: white";
                break;
            case 0:
                strTitle += "winner: draw";
                break;
            case 1:
                strTitle += "winner: black";
                break;
            default:
                strTitle += "unfinish";
        }
        this.setTitle(strTitle);

        this.setSize(600,640);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width-this.getWidth())/2,(int)(height-this.getHeight())/2-15);
        this.setModal(true);
        this.setLayout(null);
        Container con = getContentPane();

        JPanel boardPanel = new JPanel();
        boardPanel.setSize(522,522);
        boardPanel.setLocation(30,5);
        boardPanel.setLayout(new GridLayout(8,8));
        con.add(boardPanel);

        chessGrids = new ChessGridPanel[8][8];

        //draw all chess grids
        int i,j;
        for ( i = 0; i < 8; i++) {
            for ( j = 0; j < 8; j++) {
                ChessGridPanel gridComponent = new ChessGridPanel(i, j);
                gridComponent.setSize(65,65);
                //gridComponent.setLocation(j * ChessGridPanel.gridSize, i * ChessGridPanel.gridSize);
                chessGrids[i][j] = gridComponent;
                boardPanel.add(chessGrids[i][j]);
                chessGrids[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ChessGridPanel p = (ChessGridPanel) e.getComponent();
                        int row = p.getRow();
                        int col = p.getCol();
                        System.out.printf("grid %2d,%2d clicked!\n",row,col);
                        //GameFrame.controller.doClick(row,col);
                        //GameFrame.controller.alphaGo();
                    }
                });
            }
        }

        JButton btnFirst = new JButton();
        btnFirst.setSize(30,30);
        btnFirst.setLocation(35,550);
        ImageIcon ico=new ImageIcon("src\\resource\\image\\first.png");
        Image temp=ico.getImage().getScaledInstance(btnFirst.getWidth(),btnFirst.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico = new ImageIcon(temp);
        btnFirst.setIcon(ico);
        btnFirst.addActionListener(e -> {
            boardIndex = 0;
            showBoard(boardIndex);
        });
        con.add(btnFirst);

        JButton btnPre = new JButton();
        btnPre.setSize(30,30);
        btnPre.setLocation(70,550);
        ico=new ImageIcon("src\\resource\\image\\pre.png");
        temp=ico.getImage().getScaledInstance(btnFirst.getWidth(),btnFirst.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico = new ImageIcon(temp);
        btnPre.setIcon(ico);
        btnPre.addActionListener(e -> {
            if (boardIndex > 0)
                boardIndex--;
            showBoard(boardIndex);
        });
        con.add(btnPre);

        JButton btnNext = new JButton();
        btnNext.setSize(30,30);
        btnNext.setLocation(105,550);
        ico=new ImageIcon("src\\resource\\image\\next.png");
        temp=ico.getImage().getScaledInstance(btnFirst.getWidth(),btnFirst.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico = new ImageIcon(temp);
        btnNext.setIcon(ico);
        btnNext.addActionListener(e -> {
            if(boardIndex == (boardList.size() - 1))
                return;
            boardIndex++;
            showBoard(boardIndex);

        });
        con.add(btnNext);

        JButton btnLast = new JButton();
        btnLast.setSize(30,30);
        btnLast.setLocation(140,550);
        ico=new ImageIcon("src\\resource\\image\\last.png");
        temp=ico.getImage().getScaledInstance(btnFirst.getWidth(),btnFirst.getHeight(),ico.getImage().SCALE_DEFAULT);
        ico = new ImageIcon(temp);
        btnLast.setIcon(ico);
        btnLast.addActionListener(e -> {
            boardIndex = boardList.size()-1;
            if (boardIndex < 0)
                boardIndex = 0;
            showBoard(boardIndex);
        });
        con.add(btnLast);



        btnAuto = new JButton("AutoPlay");
        btnAuto.setSize(100,30);
        btnAuto.setLocation(190,550);
        btnAuto.addActionListener(e -> {
            autoPlayGame();
            btnAuto.setEnabled(false);
        });
        con.add(btnAuto);


        JButton btnClose = new JButton("Close");
        btnClose.setSize(100,30);
        btnClose.setLocation(450,550);
        btnClose.addActionListener(e -> {
            timer.stop();
            boardIndex = 0;
            this.dispose();
        });
        con.add(btnClose);

        int index = boardList.size() - 1;
        showBoard(index);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.printf("-----timer task-----\n");
                HistoryGameDialog.showBoard(boardIndex++);
            }
        });
    }
    public void initBoardList(){
        boardList.clear();
        board = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,-1,0,0,0},
                {0,0,0,-1,1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};
        int[][] tb = new int[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                tb[i][j] = board[i][j];
            }
        }
        boardList.add(tb);

        for (int i = 0; i < game.getStepList().size(); i++){
            int row = game.getStepList().get(i).getRowIndex();
            int col = game.getStepList().get(i).getColumnIndex();
            int color = game.getStepList().get(i).getColor();
            this.addStep(row,col,color);
            tb = new int[8][8];
            for (int m = 0; m < 8; m++){
                for (int n = 0; n < 8; n++){
                    tb[m][n] = board[m][n];
                }
            }
            boardList.add(tb);
        }
    }

    public static void showBoard(int index){
        if (index < 0)
            return;
        if(index == boardList.size())
        {
            timer.stop();
            boardIndex = 0;
            btnAuto.setEnabled(true);
            return;
        }

        for(int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int color = boardList.get(index)[i][j];
                chessGrids[i][j].setChessColor(color);
            }
        }
    }

    public void addStep(int row,int col,int color){
        int[] dr = new int[]{ 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] dc = new int[]{ -1, -1, 0, 1, 1, 1, 0, -1 };
        int i,j,flag = 0,count =0;
        int oppColor = color * (-1);

        if (board[row][col] != 0)
            return;

        for (int d = 0; d < 8; d++){ //遍历8个方向
            flag = 0;
            i = row + dr[d];
            j = col + dc[d];
            while(i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j] == oppColor ){
                i = i + dr[d];
                j = j + dc[d];
                flag = 1;
            }
            if( flag == 1  && i >= 0 && i < 8 && j >= 0 && j < 8 && board[i][j]==color){
                //翻转
                int m = row + dr[d];
                int n = col + dc[d];
                while(m >= 0 && m < 8 && n >= 0 && n < 8 && board[m][n] == oppColor ){
                    board[m][n] = color;
                    m = m + dr[d];
                    n = n + dc[d];
                }
                count++;
            }
        }
        if(count > 0) {
            board[row][col] = color;
        }
    }

    public void autoPlayGame(){
        timer.stop();
        boardIndex = 0;
        timer.start();
    }

}


