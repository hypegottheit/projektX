package sample;

public class Lexem {

    private int table;
    private int num;
    private String value;
    private String t;

    public Lexem(int table, int num, String value) {
        this.table = table;
        this.num = num;
        this.value = value;
    }

    public Lexem(int num, String value, String t) {
        this.num = num;
        this.value = value;
        this.t = t;
    }

    public Lexem(int num, String value) {
        this.num = num;
        this.value = value;
    }

    public Lexem(int table, int num, String value, String t) {
        this.table = table;
        this.num = num;
        this.value = value;
        this.t = t;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String print() {
        return "(" + this.table + "," + this.num + ")";
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isVar() {
        return this.table == 4;
    }
}
