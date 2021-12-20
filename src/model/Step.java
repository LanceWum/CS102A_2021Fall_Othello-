package model;

public class Step {
    private int sid;
    private int rowIndex;
    private int columnIndex;
    private int color;
    private static int stepCnt = 0;

    public Step(int rowIndex,int columnIndex,int color){
        stepCnt++;
        this.sid = stepCnt;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.color = color;
    }
    public Step(int sid,int rowIndex,int columnIndex,int color){
        stepCnt = sid;
        this.sid = sid;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.color = color;
    }
    public int getSid() {
        return sid;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public static void setStepCnt(int stepCnt) {
        Step.stepCnt = stepCnt;
    }

    @Override
    public String toString() {
        return "Step{" +
                "sid=" + sid +
                ", rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                ", color=" + color +
                '}';
    }
}
