package model;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Player {
    private static int playerCnt = 1;
    private int pid;
    private int playerType;//0: human; 1: alphaGo-1; 2:alphgGo-2
    private String name;
    private String createTime;
    private ImageIcon avartar;
    private String avartarName;
    private int gameCnt;
    private int points;
    private int rank;
    private int color; //1:black player; 0: init -1: white player
    private boolean isCheatMode;

    public Player(String name){
        this.pid = playerCnt++;
        this.playerType = 0;
        this.name = name;
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.createTime = format.format(date);
        this.avartarName = "defaultavatar.png";
        this.avartar = new ImageIcon("src/resource/image/avartar/defaultavatar.png");
        this.gameCnt = 0;
        this.points = 0;
        this.rank = 0;
        this.color = 0;
        this.isCheatMode = false;
    }

    public Player(int pid,int playerType,String name, String createTime,String avartarName){
        this.pid = pid;
        this.playerType = playerType;
        this.name = name;
        this.createTime = createTime;
        this.avartarName = avartarName;
        this.avartar = new ImageIcon("src/resource/image/avartar/"+avartarName);
        this.points = 0;
        this.gameCnt = 0;
        this.rank = 0;
        this.color = 0;
        this.isCheatMode = false;
    }

    public static int getPlayerCnt() {
        return playerCnt;
    }
    public static void setPlayerCnt(int n) {
        playerCnt = n;
    }
    public int getPid() {
        return this.pid;
    }
    public int getPlayerType(){return this.playerType;}
    public String getPlayerTypeName(){
        if (this.playerType == 0)
            return "Human";
        else if (this.playerType == 1)
            return "AI-normal";
        else
            return "AI-hard";
    }
    public void setPlayerType(int n){this.playerType = n;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCreateTime() {
        return createTime;
    }
    public ImageIcon getAvartar() {
        return avartar;
    }
    public void setAvartar(String filename){
        this.avartarName = filename;
        String f = "src/resource/image/avartar/" + filename;
        avartar = new ImageIcon(f);
        save();
    }
    public int getGameCnt() {
        return gameCnt;
    }
    public void addGameCnt(){ this.gameCnt++;}
    public int getPoints(){return  this.points;}
    public void updatePoints(int n){ this.points += n;}
    public int getRank(){return this.rank;}
    public void setRank(int n){ this.rank = n;}
    public int getColor(){return color;}
    public void setColor(int color){this.color = color;}
    public boolean isCheatMode(){
        return this.isCheatMode;
    }
    public void setCheatMode(boolean b){ this.isCheatMode = b;}
    public String toString(){
        String s = "Player:" + this.name + "; pid:" + String.valueOf(pid);
        System.out.println(s);
        return s;
    }

    public void save(){
        String key = "pid:"+Integer.toString(this.pid)+",";
        String newValue = key + "playerType:0,"
                + "name:" + this.name + ","
                + "time:" + this.createTime + ","
                + "avartar:" + this.avartarName;

        boolean isNewItem = true;
        String temp = "";
        try {
            File file = new File("src/data/playerlist.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer buf = new StringBuffer();

            // 保存该行前面的内容
            while ( (temp = br.readLine()) != null) {
                int  index = temp.indexOf(key);
                if(index >= 0){
                    buf = buf.append(newValue);
                    isNewItem = false;
                }
                else{
                    buf = buf.append(temp);
                }
                buf = buf.append(System.getProperty("line.separator"));
            }
            if (isNewItem)
                buf = buf.append(newValue);

            br.close();
            FileOutputStream fos = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.write(buf.toString().toCharArray());
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
