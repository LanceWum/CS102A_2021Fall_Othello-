package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Random;

public class Game {
    private static int gameCnt = 0;
    static final int TURN_BLACK = 1;
    static final int TURN_WHITE = -1;

    private int gid;
    private String name;
    private String playTime;
    private Player whitePlayer;
    private Player blackPlayer;
    private int  winnerColor; // -1:  white, 1: black; 0: draw
    private ArrayList<Step> stepList;
    private int[][] board;
    private int[][] latestBoard; // for retract
    private GameType gameType;
    private GameStatus gameStatus;
    private int playTurn;
    private ArrayList<int[]> blackValidGrids = new ArrayList<>();
    private ArrayList<int[]> whiteValidGrids = new ArrayList<>();
    private int retractColor;
    private boolean canRetract;
    private int[][] stepWeights;

    public Game(String name, Player whitePlayer,Player blackPlayer,int[][] stepWeights){
        gameCnt++;
        this.gid = gameCnt;
        this.name = name;
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.playTime = format.format(date);
        this.whitePlayer = whitePlayer;
        this.whitePlayer.setColor(-1);
        this.blackPlayer = blackPlayer;
        this.blackPlayer.setColor(1);
        this.winnerColor = -100;
        this.gameType = GameType.REALTIME;
        this.gameStatus = GameStatus.ONGOING;
        this.stepList = new ArrayList<Step>();
        this.board = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,-1,0,0,0},
                {0,0,0,-1,1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};
        this.latestBoard = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,-1,0,0,0},
                {0,0,0,-1,1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};

        retractColor = 0;
        canRetract = false;

        this.blackValidGrids.add(new int[]{2,4});
        this.blackValidGrids.add(new int[]{3,5});
        this.blackValidGrids.add(new int[]{4,2});
        this.blackValidGrids.add(new int[]{5,3});

        this.whiteValidGrids.add(new int[]{2,3});
        this.whiteValidGrids.add(new int[]{3,2});
        this.whiteValidGrids.add(new int[]{4,5});
        this.whiteValidGrids.add(new int[]{5,4});

        playTurn = TURN_BLACK;
        this.blackPlayer.addGameCnt();
        this.whitePlayer.addGameCnt();

        this.stepWeights = stepWeights;
        this.updateCheatModeInfo(this.blackPlayer);
        this.updateCheatModeInfo(this.whitePlayer);
    }

    // load game data from file, create the game object
    public Game(int gid, String name, String time,Player whitePlayer,Player blackPlayer,int winnerColor,ArrayList<Step> stepList,int[][] stepWeights){
        this.gid = gid;
        this.name = name;
        this.playTime = time;
        this.whitePlayer = whitePlayer;
        this.whitePlayer.setColor(-1);
        this.blackPlayer = blackPlayer;
        this.blackPlayer.setColor(1);
        this.winnerColor = winnerColor;
        this.stepList = stepList;
        this.gameType = GameType.HISTORY;
        this.stepWeights = stepWeights;
        this.blackPlayer.addGameCnt();
        this.whitePlayer.addGameCnt();
    }

    public static int getGameCnt(){return gameCnt;}
    public int getGid() {
        return gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPlayTime() {
        return playTime;
    }
    public Player getWhitePlayer() {
        return whitePlayer;
    }
    public Player getBlackPlayer() {
        return blackPlayer;
    }
    public int getWinnerColor() { return this.winnerColor; }
    public GameStatus getGameStatus() { return this.gameStatus; }
    public void setGameStatus(GameStatus status){ this.gameStatus = status;}
    public GameType getGameType(){return this.gameType;}
    public void setGameType(GameType type){ this.gameType = type;}
    public int[][] getBoard() { return board; }
    public void setBoard(int[][] board) {
        this.board = board;
    }
    public ArrayList<Step> getStepList() {
        return stepList;
    }

    public boolean checkStep(int sid){
        for (int i = 0; i < stepList.size(); i++){
            if (sid == stepList.get(i).getSid())
                return true;
        }
        return false;
    }

    public void addStep(int row,int col){
        boolean isSaved = false;
        int color = playTurn;
        int[] dr = new int[]{ 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] dc = new int[]{ -1, -1, 0, 1, 1, 1, 0, -1 };
        int i,j,flag = 0,count =0;
        int oppColor = color * (-1);

        if (this.board[row][col] != 0)
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
                    if (false == isSaved){
                        for (int ii=0; ii<8; ii++)
                            for(int jj=0; jj<8; jj++)
                                this.latestBoard[ii][jj] = this.board[ii][jj];
                        isSaved = true;
                    }
                    board[m][n] = color;
                    m = m + dr[d];
                    n = n + dc[d];
                }
                count++;
            }
        }
        if((count > 0) || (this.blackPlayer.isCheatMode()&&this.playTurn==TURN_BLACK)
        || (this.whitePlayer.isCheatMode()&&this.playTurn==TURN_WHITE))
        {
            if (this.canRetract == false)
                this.canRetract = true;
            board[row][col] = color;
            updateValidSteps(1);
            updateValidSteps(-1);
            updateGameStatus();

            System.out.printf("----------black valid grids-----------\n");
            for (int ii = 0; ii < blackValidGrids.size(); ii++){
                System.out.printf("(%d,%d)\n",blackValidGrids.get(ii)[0],blackValidGrids.get(ii)[1]);
            }

            System.out.printf("----------white valid grids-----------\n");
            for (int ii = 0; ii < whiteValidGrids.size(); ii++){
                System.out.printf("(%d,%d)\n",whiteValidGrids.get(ii)[0],whiteValidGrids.get(ii)[1]);
            }
        }

        Step s = new Step(row,col,playTurn);
        this.stepList.add(s);
    }

    public static void setGameCnt(int n){ gameCnt = n;}

    public String toString(){
        String s = new String("aaa");
        return s;
    }

    public boolean canClick(int row, int col){
        if(playTurn == TURN_BLACK)
        {
            if (this.blackPlayer.getPlayerType() > 0)
                return false;

            for (int i = 0; i < blackValidGrids.size(); i++){
                if (blackValidGrids.get(i)[0] == row && blackValidGrids.get(i)[1] == col)
                    return true;
            }
        }
        else //white turn
        {
            if (this.whitePlayer.getPlayerType() > 0)
                return false;

            for (int i = 0; i < whiteValidGrids.size(); i++){
                if (whiteValidGrids.get(i)[0] == row && whiteValidGrids.get(i)[1] == col)
                    return true;
            }
        }
        return false;
    }

    public void swapTurn(){
        if (playTurn == TURN_BLACK)
            playTurn = TURN_WHITE;
        else if(playTurn == TURN_WHITE)
            playTurn = TURN_BLACK;
    }

    public int getPlayTurn(){
        return this.playTurn;
    }

    public int getChessCount(int color){
        int sum = 0;

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (board[i][j] == color){
                    sum++;
                }
            }
        }
        return sum;
    }

    /*

     */
    public int updateGameStatus(){
        int blackCount = getChessCount(1);
        int whiteCount = getChessCount(-1);
        if (blackCount == 0 || whiteCount == 0 || (blackCount+whiteCount) == 64){
            this.gameStatus = GameStatus.OVER;
            return 0;
        }

        boolean blackBlock = block(1);
        boolean whiteBlock = block(-1);

        if (blackBlock && whiteBlock){
            this.gameStatus = GameStatus.OVER;
            return 0;
        }

        //do not swap color
        if ((this.playTurn == 1 && whiteBlock) || (this.playTurn == -1 && blackBlock))
            return 1;

        //swap color
        return 2;
    }

    public boolean block(int color){
        int b = blackValidGrids.size();
        int w = whiteValidGrids.size();

        if(color == 1)
        {
            if (blackValidGrids.size() == 0)
                return true;
            else
                return false;
        }
        else
        {
            if (whiteValidGrids.size() == 0)
                return true;
            else
                return false;
        }
    }

    public void endGame(){
        this.gameStatus = GameStatus.OVER;
        int blackCount = getChessCount(1);
        int whiteCount = getChessCount(-1);
        if (whiteCount > blackCount ){
            this.winnerColor = -1;
            whitePlayer.updatePoints(2);
        }
        else if(whiteCount == blackCount){
            this.winnerColor = 0;
            whitePlayer.updatePoints(1);
            blackPlayer.updatePoints(1);
        }
        else{
            this.winnerColor = 1;
            blackPlayer.updatePoints(2);
        }
    }

    public void initBoard(){
        board[3][3] = 1;
        board[3][4] = -1;
        board[4][3] = -1;
        board[4][4] = 1;
    }

    public void updateValidSteps(int color) {
        if (color == 1)
            this.blackValidGrids.clear();
        else
            this.whiteValidGrids.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0 ){
                    int[] step = new int[]{i,j};
                    if (color == 1)
                    {
                        if (this.blackPlayer.isCheatMode()){
                            this.blackValidGrids.add(step);
                        }
                        else{
                            if (isValidStep(i, j, color))
                                this.blackValidGrids.add(step);
                        }
                    }
                    else
                    {
                        if (this.whitePlayer.isCheatMode()){
                            this.whiteValidGrids.add(step);
                        }
                        else{
                            if (isValidStep(i, j, color))
                                this.whiteValidGrids.add(step);
                        }
                    }
                }
            }
        }
    }

    public boolean isValidStep(int row, int col, int color){
        int[] dr = new int[]{ 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] dc = new int[]{ -1, -1, 0, 1, 1, 1, 0, -1 };
        int i,j,flag = 0,count =0;
        int oppColor = color * (-1);

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
                count++;
            }
        }
        if(count > 0)
            return true;
        else
            return false;
    }

    public ArrayList<int[]> getBlackValidGrids(){
        return this.blackValidGrids;
    }
    public ArrayList<int[]> getWhiteValidGrids(){
        return this.whiteValidGrids;
    }

    public boolean retract(int color){
        if (color == this.playTurn || this.canRetract == false)
            return false;

        if (this.retractColor != 0 && this.retractColor != color)
            return false;

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                board[i][j] = latestBoard[i][j];
            }
        }
        int n = this.stepList.size();
        if (n > 0)
        {
            this.stepList.remove(n-1);
            if (this.retractColor == 0)
                this.retractColor = color * (-1);
            else
                this.retractColor = this.retractColor*(-1);
            this.swapTurn();
            this.canRetract = !this.canRetract;
        }

        updateValidSteps(1);
        updateValidSteps(-1);
        return true;
    }

    public int getRetractColor(){return this.retractColor;}

    public ArrayList<Integer> getAlphaGoStep(){
        ArrayList<Integer> step = new ArrayList<>();

        if(this.blackPlayer.getPlayerType() > 0 && this.playTurn == TURN_BLACK){//black playe is robot
            System.out.printf("----blackplayer alphaGo click----\n");

            if (this.blackPlayer.getPlayerType() == 1){
                int n = this.getBlackValidGrids().size();
                if (n != 0){
                    Random r = new Random();
                    int i = r.nextInt(n);
                    int row = this.getBlackValidGrids().get(i)[0];
                    int col = this.getBlackValidGrids().get(i)[1];
                    step.add(new Integer(row));
                    step.add(new Integer(col));
                }
            }
            else if (this.blackPlayer.getPlayerType() == 2){
                int max = -1;
                int row = 0;
                int col = 0;
                for (int i=0;i<this.getBlackValidGrids().size();i++){
                    int r = this.getBlackValidGrids().get(i)[0];
                    int c = this.getBlackValidGrids().get(i)[1];
                    if ( max<this.stepWeights[r][c]){
                        max = this.stepWeights[r][c];
                        row = r;
                        col = c;
                    }
                }
                step.add(new Integer(row));
                step.add(new Integer(col));
            }
        }

        if(this.whitePlayer.getPlayerType() > 0 && this.playTurn == TURN_WHITE){//white player is robot
            System.out.printf("----whiteplayer-alphaGo click----\n");

            if(this.whitePlayer.getPlayerType() == 1){
                int n = this.getWhiteValidGrids().size();
                if (n != 0){
                    Random r = new Random();
                    int i = r.nextInt(n);
                    int row = this.getWhiteValidGrids().get(i)[0];
                    int col = this.getWhiteValidGrids().get(i)[1];
                    step.add(new Integer(row));
                    step.add(new Integer(col));
                }
            }
            else if (this.whitePlayer.getPlayerType() == 2){
                int max = -1;
                int row = 0;
                int col = 0;
                for (int i=0;i<this.getWhiteValidGrids().size();i++){
                    int r = this.getWhiteValidGrids().get(i)[0];
                    int c = this.getWhiteValidGrids().get(i)[1];
                    if ( max<this.stepWeights[r][c]){
                        max = this.stepWeights[r][c];
                        row = r;
                        col = c;
                    }
                }
                step.add(new Integer(row));
                step.add(new Integer(col));
            }
        }

        return step;
    }

    public void updateCheatModeInfo(Player p){
       if (p.isCheatMode()) { //normal mode --> cheat mode
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (this.board[i][j] == 0) {
                        int[] step = new int[]{i, j};
                        if (p.getColor() == 1)
                            this.blackValidGrids.add(step);
                        else
                            this.whiteValidGrids.add(step);
                    }
                }
            }
        }
        else//cheat mode --> normal mode
        {
            updateValidSteps(p.getColor());
        }
    }

    public void saveGame(){
        if (this.stepList.size() == 0)
            return;

        String strItem = "gid:"+Integer.toString(this.gid)+","
                +"name:"+this.name+","
                +"time:"+this.playTime+","
                +"whitePlayerId:"+Integer.toString(this.whitePlayer.getPid())+","
                +"blackPlayerId:"+Integer.toString(this.blackPlayer.getPid())+","
                +"winnerColor:"+Integer.toString(this.winnerColor)+","
                +"stepList:[";
        String strStepList = "";
        for (int i=0; i<this.stepList.size();i++){
            strStepList += "sid:"+Integer.toString(this.stepList.get(i).getSid())+","
                    +"rowIndex:"+Integer.toString(this.stepList.get(i).getRowIndex())+","
                    +"columnIndex:"+Integer.toString(this.stepList.get(i).getColumnIndex())+",";
            if (i != this.stepList.size()-1)
                strStepList += "color:"+Integer.toString(this.stepList.get(i).getColor())+",";
            else
                strStepList += "color:"+Integer.toString(this.stepList.get(i).getColor())+"]";
        }
        strItem += strStepList;
        String key = "gid:"+Integer.toString(this.gid)+",";
        replace(key,strItem);
    }

    public void replace(String key,String newValue) {
        boolean isNewItem = true;
        String temp = "";
        try {
            File file = new File("src\\data\\gamelist.txt");
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


    public void addStep(int row,int col,int color){
        int[] dr = new int[]{ 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] dc = new int[]{ -1, -1, 0, 1, 1, 1, 0, -1 };
        int i,j,flag = 0,count =0;
        int oppColor = color * (-1);

        if (this.board[row][col] != 0)
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
    public void loadHistoryGame(int steps){
        this.board = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,-1,0,0,0},
                {0,0,0,-1,1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};
        this.latestBoard = new int[][]{
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,1,-1,0,0,0},
                {0,0,0,-1,1,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0}};
        this.playTurn = TURN_BLACK;
        for(int i = 0; i < steps; i++){
            int row = stepList.get(i).getRowIndex();
            int col = stepList.get(i).getColumnIndex();
            int color = stepList.get(i).getColor();
            int nextStepColor = 0;
            if (i < (steps -1))
                nextStepColor = stepList.get(i+1).getColor();
            else
                nextStepColor = stepList.get(i).getColor()*(-1);

            this.moveStep(row,col,color,nextStepColor);

            System.out.printf("---%d step---\n",i+1);
            for(int m = 0; m < 8; m++){
                System.out.printf("%3d,%3d,%3d,%3d,%3d,%3d,%3d,%3d\n",board[m][0],
                        board[m][1],board[m][2],board[m][3],board[m][4],board[m][5],
                        board[m][6],board[m][7]);
            }

            if(i == (steps-1))
                this.playTurn = stepList.get(i).getColor()*(-1);
        }
    }

    public void moveStep(int row,int col,int c,int nextStepColor){
        boolean isSaved = false;
        int color = c;
        int[] dr = new int[]{ 0, 1, 1, 1, 0, -1, -1, -1 };
        int[] dc = new int[]{ -1, -1, 0, 1, 1, 1, 0, -1 };
        int i,j,flag = 0,count =0;
        int oppColor = color * (-1);

        if (this.board[row][col] != 0)
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
                    if (false == isSaved){
                        for (int ii=0; ii<8; ii++)
                            for(int jj=0; jj<8; jj++)
                                this.latestBoard[ii][jj] = this.board[ii][jj];
                        isSaved = true;
                    }
                    board[m][n] = color;
                    m = m + dr[d];
                    n = n + dc[d];
                }
                count++;
            }
        }
        if(count > 0)
        {
            //swapTurn();
            this.playTurn = nextStepColor;
            board[row][col] = color;
            updateValidSteps(1);
            updateValidSteps(-1);
            updateGameStatus();

            System.out.printf("----------black valid grids-----------\n");
            for (int ii = 0; ii < blackValidGrids.size(); ii++){
                System.out.printf("(%d,%d)\n",blackValidGrids.get(ii)[0],blackValidGrids.get(ii)[1]);
            }

            System.out.printf("----------white valid grids-----------\n");
            for (int ii = 0; ii < whiteValidGrids.size(); ii++){
                System.out.printf("(%d,%d)\n",whiteValidGrids.get(ii)[0],whiteValidGrids.get(ii)[1]);
            }
        }
    }
}
