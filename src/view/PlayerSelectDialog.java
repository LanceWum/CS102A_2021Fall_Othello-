package view;
import model.Game;
import model.GameSystem;
import model.Player;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PlayerSelectDialog extends JDialog {
    JTable table;
    JButton avatarLabel;
    PlayerPanel playerPanel;
    JLabel nameLabel;

    public PlayerSelectDialog(PlayerPanel playerPanel){
        int gap = 10;
        this.playerPanel = playerPanel;
        this.setTitle("Player Select Dialog");
        this.setSize(560,250);
        double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((int)(width-this.getWidth())/2,(int)(height-this.getHeight())/2-15);
        this.setModal(true);
        this.setLayout(null);

        Container con = getContentPane();

        avatarLabel = new JButton("");
        avatarLabel.setSize(120,150);

        JLabel label_01 = new JLabel("Player List:");
        label_01.setSize(100,30);
        label_01.setLocation(gap,gap);
        con.add(label_01);

        JPanel tablePanle = new JPanel();
        tablePanle.setLocation(gap,40);
        tablePanle.setSize(280,150);
        tablePanle.setBackground(Color.DARK_GRAY);

        String[] columnNames = { "id","Name", "Type","Rank","Score" }; // 定义表格列名数组
        // 定义表格数据数组
        String[][] tableValues = new String[0][5];// { { "1","AlphaGo-I", "AI-normal","9","--" }};

        // 创建指定列名和数据的表格
        DefaultTableModel model = new DefaultTableModel(tableValues, columnNames);

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //table.setColumnModel(m);
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
        scrollPane.setPreferredSize(new Dimension(275,145));//.setSize(280,190);
        scrollPane.setViewportView(table);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row=table.getSelectedRow();//选中行
                    int pid = ((Integer)table.getModel().getValueAt(row,0)).intValue();
                    Player p = GameFrame.controller.getGameSystem().getPlayer(pid);
                    if(p != null){
                        ImageIcon tmpIcon = p.getAvartar();
                        tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(avatarLabel.getWidth(), avatarLabel.getHeight(), Image.SCALE_DEFAULT));
                        avatarLabel.setIcon(tmpIcon);
                    }
                }
            }
        });

        // 将滚动面板添加到边界布局的中间

        tablePanle.setLayout(new BorderLayout());
        tablePanle.add(table.getTableHeader(), BorderLayout.PAGE_START);
        tablePanle.add(scrollPane);
        con.add(tablePanle);

        loadPlayer();

        avatarLabel.setLocation(gap+tablePanle.getWidth()+gap,40);
        ImageIcon icon = GameSystem.getPlayerList().get(0).getAvartar();
        icon.setImage(icon.getImage().getScaledInstance(avatarLabel.getWidth(), avatarLabel.getHeight(), Image.SCALE_DEFAULT));
        avatarLabel.setIcon(icon);

        avatarLabel.addActionListener(e -> {
            //System.out.println("avatarLabel click!");
            JFileChooser jfc=new JFileChooser("src\\resource\\image\\avartar");
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
            jfc.showDialog(new JLabel(), "select avatar file");
            File file=jfc.getSelectedFile();
            if(file.isDirectory()){
                System.out.println("文件夹:"+file.getAbsolutePath());
            }else if(file.isFile()){
                System.out.println("文件:"+file.getAbsolutePath());
            }
            System.out.println(jfc.getSelectedFile().getName());
            String newAvatarFileName = jfc.getSelectedFile().getName();
            int index = table.getSelectedRow();
            int pid = ((Integer)table.getModel().getValueAt(index,0)).intValue();
            Player p = GameFrame.controller.getGameSystem().getPlayer(pid);
            if (p != null){
                p.setAvartar(newAvatarFileName);
                ImageIcon tmpIcon = p.getAvartar();
                tmpIcon.setImage(tmpIcon.getImage().getScaledInstance(avatarLabel.getWidth(), avatarLabel.getHeight(), Image.SCALE_DEFAULT));
                avatarLabel.setIcon(tmpIcon);
            }
        });

        avatarLabel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        con.add(avatarLabel);

        JButton newBtn = new JButton("new");
        newBtn.setSize(100,30);
        newBtn.setLocation(gap + tablePanle.getWidth() + gap+avatarLabel.getWidth()+gap,40);
        con.add(newBtn);
        newBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "input player name:");
            Player p = new Player(name);
            GameSystem.addPlayer(p);
            addPlayerToTable(p);
            table.setRowSelectionInterval(table.getModel().getRowCount()-1,table.getModel().getRowCount()-1);
        });

        JButton okBtn = new JButton("OK");
        okBtn.setSize(100,30);
        okBtn.setLocation(gap + tablePanle.getWidth() + gap+avatarLabel.getWidth()+gap,120);
        con.add(okBtn);
        okBtn.addActionListener(e -> {
            System.out.println("OK btn click!");
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            //System.out.println(playerList.getSelectedValue().toString());
            //this.playerPanel.updateGame((Player)playerList.getSelectedValue());
            int row = table.getSelectedRow();
            int pid = ((Integer)table.getValueAt(row,0)).intValue();
            this.playerPanel.setPlayer(GameFrame.controller.getGameSystem().getPlayer(pid));
            GameFrame.controller.initGame();
            //this.playerPanel.updateInfo();
            this.setVisible(false);
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setSize(100,30);
        cancelBtn.setLocation(gap + tablePanle.getWidth() + gap+avatarLabel.getWidth()+gap,160);
        con.add(cancelBtn);
        cancelBtn.addActionListener(e -> {
            System.out.println("cancel btn click!");
            this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            this.setVisible(false);
        });
    }

    public void loadPlayer(){
        ArrayList<Player> pl = GameFrame.controller.getGameSystem().getRankList();
        DefaultTableModel dtm=(DefaultTableModel)table.getModel();

        switch (GameFrame.controller.getGameMode()) {
            case "HvsH":
                for (int i = 0; i < pl.size(); i++) {
                    Player p = pl.get(i);
                    if (p.getPlayerType() == 0) {
                        Vector v = new Vector();
                        v.add(p.getPid());
                        v.add(p.getName());
                        v.add(p.getPlayerTypeName());
                        v.add(p.getRank());
                        v.add(p.getPoints());
                        dtm.addRow(v);
                    }
                }
                break;
            case "HvsM":
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
                break;
            case "MvsM":
                for (int i = 0; i < pl.size(); i++) {
                    Player p = pl.get(i);
                    if (p.getPlayerType() > 0) {
                        Vector v = new Vector();
                        v.add(p.getPid());
                        v.add(p.getName());
                        v.add(p.getPlayerTypeName());
                        v.add(p.getRank());
                        v.add(p.getPoints());
                        dtm.addRow(v);
                    }
                }
                break;
            default:
                break;
        }

        if(table.getModel().getRowCount() > 0)
            this.table.setRowSelectionInterval(0,0);
    }

    public void addPlayerToTable(Player p){
        DefaultTableModel dtm=(DefaultTableModel)table.getModel();
        Vector v = new Vector();
        v.add(p.getPid());
        v.add(p.getName());
        v.add(p.getPlayerTypeName());
        v.add(p.getRank());
        v.add(p.getPoints());
        dtm.addRow(v);
    }
}
