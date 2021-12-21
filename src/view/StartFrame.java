package view;

import model.Player;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class StartFrame extends JFrame {
    GameFrame mf;
    JTable table;

    public StartFrame(GameFrame mf){
        //this.setTitle("2021F CS102A Project Reversi");
        this.mf = mf;
        this.setLayout(null);
        this.setSize(460,300);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width-this.getWidth())/2,(int)(height-this.getHeight())/2-15);

        BackgroudPanel bgp = new BackgroudPanel(new ImageIcon("src\\resource\\image\\taiji.jpg").getImage());
        bgp.setBounds(0,0,460,300);

        Container con = this.getContentPane();


        JLabel label_01 = new JLabel("player list",JLabel.CENTER);
        label_01.setSize(280,30);
        label_01.setLocation(0,5);
        label_01.setFont(new Font("Calibri", Font.ITALIC, 25));
        label_01.setForeground(Color.WHITE);
        con.add(label_01);

        JPanel tablePanle = new JPanel();
        tablePanle.setLocation(10,40);
        tablePanle.setSize(290,200);
        tablePanle.setBorder(BorderFactory.createEtchedBorder());

        String[] columnNames = { "id","Name", "Type","Rank","Score" }; // 定义表格列名数组
        // 定义表格数据数组
        String[][] tableValues = new String[0][5];// { { "1","AlphaGo-I", "AI-normal","9","--" }};
        // 创建指定列名和数据的表格
        DefaultTableModel model = new DefaultTableModel(tableValues, columnNames);
        table = new JTable(model){ public boolean isCellEditable(int row, int column) { return false; }};;
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setPreferredWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(30);
        table.getColumnModel().getColumn(4).setPreferredWidth(30);
        table.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(0);
        DefaultTableCellRenderer dc=new DefaultTableCellRenderer();
        dc.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, dc);


        // 创建显示表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(280,190));//.setSize(280,190);
        scrollPane.setViewportView(table);
        // 将滚动面板添加到边界布局的中间
        tablePanle.setLayout(new BorderLayout());
        tablePanle.add(table.getTableHeader(), BorderLayout.PAGE_START);
        tablePanle.add(scrollPane);
        con.add(tablePanle);


        JLabel label_02 = new JLabel("game mode",JLabel.CENTER);
        label_02.setSize(190,30);
        label_02.setLocation(275,5);
        label_02.setFont(new Font("Calibri", Font.ITALIC, 20));
        label_02.setForeground(Color.WHITE);
        con.add(label_02);
        JRadioButton h2hMode = new JRadioButton("H vs H",true);
        h2hMode.setSize(80,30);
        //h2hMode.setLocation(350,60);
        h2hMode.setActionCommand("HvsH");
        JRadioButton h2mMode = new JRadioButton("H vs M");
        h2mMode.setSize(80,30);
        //h2mMode.setLocation(350,100);
        h2mMode.setActionCommand("HvsM");
        JRadioButton m2mMode = new JRadioButton("M vs M");
        m2mMode.setSize(80,30);
        //m2mMode.setLocation(350,140);
        m2mMode.setActionCommand("MvsM");
        ButtonGroup group = new ButtonGroup();
        group.add(h2hMode);
        group.add(h2mMode);
        group.add(m2mMode);
        JPanel radioPanel = new JPanel();
        //radioPanel.setBackground(Color.DARK_GRAY);
        radioPanel.setSize(100,150);
        radioPanel.setLocation(320,40);
        radioPanel.setLayout(new GridLayout(3, 1));
        radioPanel.add(h2hMode);
        radioPanel.add(h2mMode);
        radioPanel.add(m2mMode);
        radioPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), ""));
        con.add(radioPanel);
        JButton okBtn = new JButton("OK");
        okBtn.setSize(100,30);
        okBtn.setLocation(320,210);
        con.add(okBtn);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = group.getSelection().getActionCommand();
                GameFrame.controller.setGameMode(s);
                setVisible(false);
                mf.setVisible(true);
                GameFrame.controller.getGamePanel().clearGame();
            }
        });
        loadPlayerList();
        con.add(bgp);
        this.setVisible(true);

    }

    public void loadPlayerList(){
        ArrayList<Player> pl = GameFrame.controller.getGameSystem().getRankList();
        DefaultTableModel dtm=(DefaultTableModel)table.getModel();

        for (int i = 0; i < pl.size(); i++) {
            Player p = pl.get(i);
            Vector v = new Vector();
            v.add(p.getPid());
            v.add(p.getName());
            v.add(p.getPlayerTypeName());
            v.add(p.getRank());
            v.add(p.getPoints());
            dtm.addRow(v);
        }
    }
}
