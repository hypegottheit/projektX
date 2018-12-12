package sample;

public class Digit {

    private int num;
    private String value;
    private String stack;
    private String t;

    public Digit(int num, String value, String stack, String t) {
        this.num = num;
        this.value = value;
        this.stack = stack;
        this.t = t;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
