package view;

import model.Game;
import model.GameSystem;
import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class LoadGameDialog extends JDialog {
    JTable table;

    public LoadGameDialog(){
        this.setTitle("Select Game");
        this.setSize(520,320);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width-this.getWidth())/2,(int)(height-this.getHeight())/2-15);
        this.setModal(true);
        this.setLayout(null);
        Container con = getContentPane();

        JPanel tablePanle = new JPanel();
        tablePanle.setLocation(15,5);
        tablePanle.setSize(480,220);
        //tablePanle.setBorder(BorderFactory.createRaisedBevelBorder());
        tablePanle.setBackground(Color.DARK_GRAY);


        String[] columnNames = { "id","Name", "Time","Player(Black)","Player(White","Winner" }; // 定义表格列名数组
        // 定义表格数据数组
        String[][] tableValues = new String[0][6];/* { { "1","game001", "2021-12-12 02:11:23","Tom","Jack","Tom" },
                { "2","game002", "2021-12-12 02:21:23","Tom","Jack","Tom" },
                { "3","game007", "2021-12-11 04:11:23","Tom","Jack","Jack" } };*/
        // 创建指定列名和数据的表格
        DefaultTableModel model = new DefaultTableModel(tableValues, columnNames);

        table = new JTable(model){ public boolean isCellEditable(int row, int column) { return false; }};;
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //table.setColumnModel(m);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);


        DefaultTableCellRenderer dc=new DefaultTableCellRenderer();
        dc.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, dc);
        table.setSize(450,200);
        table.setLocation(0,0);
        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(470,215));//.setSize(280,190);
        scrollPane.setViewportView(table);

        // 将滚动面板添加到边界布局的中间

        tablePanle.setLayout(new BorderLayout());
        tablePanle.add(table.getTableHeader(), BorderLayout.PAGE_START);
        tablePanle.add(scrollPane);
        con.add(tablePanle);

        JButton okBtn = new JButton("OK");
        okBtn.setSize(100,30);
        okBtn.setLocation(270,240);
        con.add(okBtn);
        okBtn.addActionListener(e -> {
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            int row = this.table.getSelectedRow();
            if (row >= 0) {
                Integer strGid = (Integer) this.table.getValueAt(row, 0);
                int gid = strGid.intValue();
                GameFrame.controller.loadGame(gid);
                if(gid > 0) {
                    GameFrame.setAutoPlay(true);
                    GameFrame.setRestartButton(false);
                }
            }
            setVisible(false);
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setSize(100,30);
        cancelBtn.setLocation(395,240);
        con.add(cancelBtn);
        cancelBtn.addActionListener(e -> {
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
            this.setVisible(false);
        });
        loadGame();
    }

    public void loadGame(){
        ArrayList<Game> gameList =  GameFrame.controller.getGameSystem().getGameList();
        if (gameList == null)
            return;
        DefaultTableModel dtm=(DefaultTableModel)table.getModel();
        for (int i = 0; i < gameList.size(); i++){
            Game g = gameList.get(i);
            Vector v = new Vector();
            v.add(g.getGid());
            v.add(g.getName());
            v.add(g.getPlayTime());
            v.add(g.getBlackPlayer().getName());
            v.add(g.getWhitePlayer().getName());
            String winner = "";
            if (g.getWinnerColor() == 1){
                winner = g.getBlackPlayer().getName();
            }
            else if(g.getWinnerColor() == 0){
                winner = "draw";
            }
            else if(g.getWinnerColor() == -1){
                winner = g.getWhitePlayer().getName();
            }
            else
                winner = " ";
            v.add(winner);
            dtm.addRow(v);
        }
    }
}
