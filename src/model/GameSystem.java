package model;

import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Random;

public class GameSystem {
    private static ArrayList<Player> playerList;
    private static ArrayList<Game> gameList;
    private Game currentGame;
    private int[][] stepWeights;

    public GameSystem(){
        //load playlist from playerlist.data
        playerList = new ArrayList<Player>();
        //load gamelist from gamelist.data
        gameList = new ArrayList<Game>();
        int result = loadPlayerList();
        System.out.printf("load player list, errorcode:%d\n",result);
        result = loadGameList();
        System.out.printf("load game list, errorcode:%d\n",result);
        result = loadStepWeights();
        System.out.printf("load Step Weights, errorcode:%d\n",result);
        this.currentGame = null;
    }

    public int loadPlayerList()
    {
        int errorCode = 0;
        String pathname = "src/data/playerlist.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        int latestPid = 0;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                System.out.println(line);
                //get pid,name,time from the line
                int pid = 0;
                int playerType = 0;
                String name = "";
                String time = "";
                String avartar = "";
                String[] s = line.split(",");
                for (int i = 0; i < s.length; i++){
                    String first = s[i].substring(0,s[i].indexOf(":"));
                    String second = s[i].substring(s[i].indexOf(":")+1);
                    switch (first){
                        case "pid":
                            pid =  Integer.parseInt(second);
                            if (pid <= 0)
                                errorCode = 601;
                            break;
                        case "playerType":
                            playerType = Integer.parseInt(second);
                            if(playerType < 0 || playerType > 2)
                                errorCode = 602;
                            break;
                        case "name":
                            name =  second;
                            break;
                        case "time":
                            time = second;
                            break;
                        case "avartar":
                            avartar = second;
                            break;
                        default:
                            errorCode = 600;
                            break;
                    }
                }

                if (errorCode > 0)
                    return errorCode;

                //构造Player对象并加入数组中
                Player p = new Player(pid,playerType,name,time,avartar);
                playerList.add(p);
                if(pid > latestPid)
                    latestPid = pid;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Player.setPlayerCnt(latestPid+1);
        return  errorCode;
    }

    public int loadGameList(){
        int errorCode = 0;
        String  pathname = "src/data/gamelist.txt"; // 绝对路径或相对路径都可以，写入文件时演示相对路径,读取以上路径的input.txt文件
        int latestGid = 0;

        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                System.out.println(line);
                //get pid,name,time from the line
                int gid = 0;
                String name = "";
                String time = "";
                int whitePlayerId = 0;
                int blackPlayerId = 0;
                int winnerColor = 0;

                String beforeSid = line.substring(0,line.indexOf("stepList"));
                String afterSid = line.substring(line.indexOf("stepList:")+9);

                String[] s = beforeSid.split(",");
                for (int i = 0; i < s.length; i++){
                    String first = s[i].substring(0,s[i].indexOf(":"));
                    String second = s[i].substring(s[i].indexOf(":")+1);
                    switch (first){
                        case "gid":
                            gid =  Integer.parseInt(second);
                            if (gid <= 0)
                                errorCode = 701;
                            break;
                        case "name":
                            name =  second;
                            break;
                        case "time":
                            time = second;
                            break;
                        case "whitePlayerId":
                            whitePlayerId = Integer.parseInt(second);
                            break;
                        case "blackPlayerId":
                            blackPlayerId = Integer.parseInt(second);
                            break;
                        case "winnerColor":
                            winnerColor = Integer.parseInt(second);
                            if(winnerColor != -100
                                    && winnerColor != -1
                                    && winnerColor != 0
                                    && winnerColor != 1)
                                errorCode = 702;
                            break;
                        default:
                            errorCode = 700;
                            break;
                    }
                }

                // load steplist
                String afterSid01 = afterSid.substring(afterSid.indexOf("[")+1,afterSid.lastIndexOf("]"));
                s = afterSid01.split(",");

                ArrayList<Step> stepList = new ArrayList<>();
                int sid = 0;
                int rowIndex = -1;
                int columnIndex = -1;
                int color = 0;
                for (int i = 0; i < s.length; i++){
                    String first = s[i].substring(0,s[i].indexOf(":"));
                    String second = s[i].substring(s[i].indexOf(":")+1);

                    switch (first){
                        case "sid":
                            sid =  Integer.parseInt(second);
                            if(sid <= 0)
                                errorCode = 703;
                            break;
                        case "rowIndex":
                            rowIndex =  Integer.parseInt(second);
                            if(rowIndex<0 || rowIndex > 7)
                                errorCode = 704;
                            break;
                        case "columnIndex":
                            columnIndex = Integer.parseInt(second);
                            if(columnIndex < 0 || columnIndex > 7)
                                errorCode = 704;
                            break;
                        case "color":
                            color = Integer.parseInt(second);
                            if(color != -1 && color != 1)
                                errorCode = 705;
                            break;
                        default:
                            errorCode = 700;
                            break;
                    }
                    if(errorCode != 0)
                        return errorCode;

                    if (sid != 0 && rowIndex != -1 && columnIndex != -1 && color !=0){
                        Step step = new Step(sid,rowIndex,columnIndex,color);
                        stepList.add(step);
                        sid = 0;
                        rowIndex = -1;
                        columnIndex = -1;
                        color = 0;
                    }
                }

                Player whitePlayer = null;
                Player blackPlayer = null;
                for (int i = 0; i < playerList.size();i++){
                    if ( playerList.get(i).getPid() == whitePlayerId){
                        whitePlayer = playerList.get(i);
                    }
                    else if(playerList.get(i).getPid() == blackPlayerId){
                        blackPlayer = playerList.get(i);
                    }
                }

                if(whitePlayer == null || blackPlayer == null){
                    errorCode = 706;
                    return errorCode;
                }

                if (winnerColor == -1){
                    whitePlayer.updatePoints(2);
                }
                else if (winnerColor == 0){
                    blackPlayer.updatePoints(1);
                    whitePlayer.updatePoints(1);
                }
                else if(winnerColor == 1)
                    blackPlayer.updatePoints(2);

                Game game = new Game(gid,name,time,whitePlayer,blackPlayer,winnerColor,stepList,this.stepWeights);
                gameList.add(game);
                if (gid > latestGid)
                    latestGid = gid;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game.setGameCnt(latestGid);
        return errorCode;
    }
    public int loadStepWeights(){
        int errorCode = 0;
        this.stepWeights = new int[8][8];
        String pathname = "src/data/stepweight.txt";
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                //get 8 steps weight from the line
                String[] s = line.split(",");
                if(s.length != 8)
                    errorCode = 901;
                for (int j = 0; j < s.length; j++){
                    this.stepWeights[i][j] = Integer.parseInt(s[j]);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return errorCode;
    }

    public static ArrayList<Player> getPlayerList() {
        return playerList;
    }

    public ArrayList<Game> getGameList() {
        return gameList;
    }
    public Game getCurrentGame(){return this.currentGame;}
    public boolean checkPlayer(int pid){
        return true;
    }
    public boolean checkGame(int gid){
        return true;
    }
    public static  boolean addPlayer(Player p){
        playerList.add(p);
        p.save();
        return true;
    }

    public boolean addGame(Game game){
        return true;
    }

    public Game newGame(String gameName,Player leftPlayer, Player rightPlayer){
        Random r = new Random();
        Game g = null;
        if (r.nextBoolean()){
            g = new Game(gameName,leftPlayer,rightPlayer,this.stepWeights);
        }
        else {
            g = new Game(gameName,rightPlayer,leftPlayer,this.stepWeights);
        }
        g.initBoard();
        gameList.add(g);
        this.currentGame = g;
        return g;
    }

    public void clearGame(){
        this.currentGame = null;
    }

    public static ArrayList<Game> listPlayerGame(int pid){
        return null;
    }

    public void showPlayer(){
        for (int i = 0; i < playerList.size(); i++)
            playerList.get(i).toString();
    }



    public boolean setCurrentGame(int gid){
        for(int i = 0; i < this.getGameList().size(); i++){
            if (this.getGameList().get(i).getGid() == gid){
                this.currentGame = this.getGameList().get(i);
                return true;
            }
        }
        return false;
    }

    public Game getGame(int gid){
        for(int i = 0; i < this.getGameList().size(); i++){
            if (this.getGameList().get(i).getGid() == gid){
                return this.getGameList().get(i);
            }
        }
        return null;
    }

    public ArrayList<Player> getRankList(){
        if (playerList.size() == 0)
            return null;

        for(int i = 0; i < playerList.size(); i++){
            int rank = 0;
            if(playerList.get(i).getPlayerType() == 2)
                rank = 10;
            else if (playerList.get(i).getPlayerType() == 1)
                rank = 9;
            else{
                if (playerList.get(i).getGameCnt() == 0)
                    rank = 0;
                else
                    rank = ((10*playerList.get(i).getPoints())/2)/playerList.get(i).getGameCnt();
            }

            playerList.get(i).setRank(rank);
        }

        ArrayList<Player> rankPlayerList = new ArrayList<>();
        rankPlayerList.add(playerList.get(0));

        for(int i = 1; i < playerList.size(); i++){
            for(int j = 0; j < rankPlayerList.size(); j++){
                if (playerList.get(i).getRank() > rankPlayerList.get(j).getRank()){
                    rankPlayerList.add(j,playerList.get(i));
                    break;
                }
                else
                {
                    if ( j == rankPlayerList.size()-1){
                        rankPlayerList.add(playerList.get(i));
                        break;
                    }
                }
            }
        }

        return rankPlayerList;
    }

    public Player getPlayer(int pid){
        for (int i = 0; i < playerList.size();i++)
        {
            if(playerList.get(i).getPid() == pid)
                return playerList.get(i);
        }

        return null;
    }

}
