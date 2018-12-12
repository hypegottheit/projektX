package taf.kr.core;

import java.util.ArrayList;
import java.util.Arrays;

public class LA {

    ArrayList<Character> limiters = new ArrayList<Character>(Arrays.asList(' ', '{', '}', ';', ',', '(', ')', ':', '=',
            '~'));
    char[] let = new char[27];
    char[] b = {'0', '1'};
    char[] o = {'2', '3', '4', '5', '6', '7'};
    char[] d = {'8', '9'};
    char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    char[] hLet = {'a', 'b', 'c', 'd', 'e', 'f'};
    String code;
    String stack;
    Table table;
    boolean negative = false;

    public void analyze(String code, Table table) {
        this.table = table;
        this.code = code.toLowerCase();
        deleteComm();
        //analize();
    }

    private void deleteComm() {
        int commIndex = 0;
        StringBuilder codeBuild = new StringBuilder(code);
        boolean comm = false;
        for (int i = 1; i < codeBuild.length() - 1; i++) {
            char ch = codeBuild.charAt(i);
            char chNext = codeBuild.charAt(i + 1);
            if ((ch == '(') && (chNext == '*')) {
                comm = true;
                commIndex = i;
            }
            if ((ch == '*') && (chNext == ')') && comm) {
                codeBuild.delete(commIndex, i + 2);
                i = commIndex;
                comm = false;
            }
            if (((ch == ' ') && (chNext != '.') && (limiters.contains(chNext))) || (ch == '\t')) {
                codeBuild.delete(i, i + 1);
                i--;
            }
        }
        code = codeBuild.toString();
        stack = "";
        analize();
    }

    private void analize() {
        for (int i = 0; i < 26; i++) {
            let[i] = (char) ((byte) 'a' + i);
        }
        let[26] = '_';
        for (int i = 0; i < code.length(); i++) {
            char curr = code.charAt(i);
            while (!limiters.contains(curr)) {
                if (has(let, curr)) {
                    while (!limiters.contains(curr)) {
                        stack += curr;
                        i++;
                        if (!has(let, curr))
                            throw new RuntimeException("Неправильный символ " + curr);
                        if (code.length() > i)
                            curr = code.charAt(i);
                        else {
                            if (stack.equals("end"))
                                add(2, table.limiters.indexOf(stack));
                            return;
                        }
                    }
                    if (table.functionWords.contains(stack)) {
                        add(1, table.functionWords.indexOf(stack));
                        stack = "";
                    } else if ((!stack.equals(" ")) && (table.limiters.contains(stack))) {
                        add(2, table.limiters.indexOf(stack));
                        stack = "";
                    } else {
                        if (!table.names.contains(stack)) {
                            table.names.add(stack);
                            table.varnames.add(new Lexem(table.varnames.size(), stack));
                        }
                        add(4, table.names.indexOf(stack));
                        stack = "";
                    }
                } else if (curr == '~') negative = true;
                else if (has(b, curr)) {
                    i = readB(curr, i);
                    i++;
                } else if (has(o, curr)) {
                    i = readO(curr, i);
                    i++;
                } else if (has(d, curr)) {
                    i = readD(curr, i);
                    i++;
                } else if (curr == '.')
                    i = readReal(curr, i);
                else throw new RuntimeException("Неизвестный символ " + curr);
                if (i < code.length())
                    curr = code.charAt(i);
                else return;
            }
            if (curr == '~') negative = true;
            if (limiters.contains(curr) && (curr != ' ') && (curr != '~'))
                add(2, table.limiters.indexOf(String.valueOf(String.valueOf(curr))));
        }
    }

    private boolean has(char[] mass, char symbol) {
        for (char mas : mass) {
            if (mas == symbol)
                return true;
        }
        return false;
    }

    private int readB(char curr, int i) {
        while (!limiters.contains(curr)) {
            if (has(o, curr)) {  // если содержит числа от 2 до 7
                i = readO(curr, i);
                return i;
            } else if (has(d, curr)) {  // если содержит числа 8, 9
                i = readD(curr, i);
                return i;
            } else if (curr == 'e') {
                i = readReal(curr, i);
                return i;
            } else if ((curr == 'b') && limiters.contains(code.charAt(i + 1))) {   // если содержит b и следующий разделитель
                convertAndAdd(2);
                return i++;
            } else if ((curr == 'o') && limiters.contains(code.charAt(i + 1))) {     // если содержит o и следующий разделитель
                convertAndAdd(8);
                return i++;
            } else if ((curr == 'd') && limiters.contains(code.charAt(i + 1))) {
                convertAndAdd(10);
                return i++;
            } else if (has(hLet, curr)) {
                i = readH(curr, i);
                return i;
            } else if (curr == '.') {
                i = readReal(curr, i);
                return i;
            } else if (has(b, curr)) {
                stack += curr;
                i++;
                curr = code.charAt(i);
            } else throw new RuntimeException("Неизвестный символ " + curr);
        }
        if (stack.length() > 0)
            convertAndAdd(2);
        if (curr != ' ')
            add(2, table.limiters.indexOf(String.valueOf(String.valueOf(curr))));
        return i++;
    }

    private int readO(char curr, int i) {
        while (!limiters.contains(curr)) {
            if (has(d, curr)) {
                i = readD(curr, i);
                return i;
            } else if (curr == 'e') {
                i = readReal(curr, i);
                return i;
            } else if ((curr == 'o') && limiters.contains(code.charAt(i + 1))) {
                convertAndAdd(8);
                return i++;
            } else if ((curr == 'd') && limiters.contains(code.charAt(i + 1))) {
                convertAndAdd(10);
                return i++;
            } else if (has(hLet, curr)) {
                i = readH(curr, i);
                return i;
            } else if (curr == '.') {
                i = readReal(curr, i);
                return i;
            } else if (has(b, curr) || has(o, curr)) {
                stack += curr;
                i++;
                curr = code.charAt(i);
            } else throw new RuntimeException("Неизвестный символ " + curr);
        }
        if (stack.length() > 0) {
            convertAndAdd(8);
        }
        if (curr != ' ')
            add(2, table.limiters.indexOf(String.valueOf(String.valueOf(curr))));
        return i++;
    }

    private int readD(char curr, int i) {
        while (!limiters.contains(curr)) {
            if (curr == 'e') {
                i = readReal(curr, i);
                return i;
            } else if ((curr == 'd') && limiters.contains(code.charAt(i + 1))) {
                convertAndAdd(10);
                return i++;
            } else if (has(hLet, curr)) {
                i = readH(curr, i);
                return i;
            } else if (curr == '.') {
                i = readReal(curr, i);
                return i;
            } else if (has(b, curr) || has(o, curr) || has(d, curr)) {
                stack += curr;
                i++;
                curr = code.charAt(i);
            } else throw new RuntimeException("Неизвестный символ " + curr);
        }
        if (stack.length() > 0) {
            convertAndAdd(10);
        }
        if (curr != ' ')
            add(2, table.limiters.indexOf(String.valueOf(String.valueOf(curr))));
        return i++;
    }

    private void add(int type, int num) {
        String value = "";
        switch (type) {
            case 1:
                value = table.functionWords.get(num);
                break;
            case 2:
                value = table.limiters.get(num);
                break;
            case 4:
                value = table.names.get(num);
                table.res.add(new Lexem(type, num, value));
                return;
        }
        table.res.add(new Lexem(type, num, value));
    }

    private void add(int type, int num, String value, String t) {
        table.res.add(new Lexem(type, num, value, t));
    }

    private void convertAndAdd(int system) {
        if (negative) {
            stack = '-' + stack;
            negative = false;
        }
        String bin = convertInt(stack, system);
        if (!hasDigit(bin))
            table.digits.add(new Digit(table.digits.size(), bin, stack, "int"));
        add(3, table.digits.size(), bin, "int");
        stack = "";
    }

    private boolean hasDigit(String bin) {
        for (Digit l : table.digits)
            if (l.getValue().equals(bin))
                return true;
        return false;
    }

    private int readReal(char curr, int i) {
        if (curr == '.')
            i++;
        curr = code.charAt(i);
        while (!limiters.contains(curr) && (i < code.length() - 1)) {
            if (curr == 'e') {
                i++;
                curr = code.charAt(i);
                break;
            } else if (has(let, curr))
                i = readH(curr, i);
            else if (has(digits, curr)) {
                stack += curr;
                i++;
                curr = code.charAt(i);
            } else throw new RuntimeException("Неизвестный символ " + curr);
        }
        if ((curr == '+') || (curr == '-') || has(digits, curr)) {
            i++;
            curr = code.charAt(i);
            while (!limiters.contains(curr)) {
                if (has(digits, curr)) {
                    stack += curr;
                    i++;
                    curr = code.charAt(i);
                } else throw new RuntimeException("Неизвестный символ " + curr);
            }
        } else if (!limiters.contains(curr)) throw new RuntimeException("Неизвестный символ " + curr);
        String bin = convertReal(stack);
        if (!table.digits.contains(bin))
            table.digits.add(new Digit(table.digits.size(), bin, stack, "float"));
        add(3, table.digits.size(), bin, "float");
        stack = "";
        return i++;
    }

    private int readH(char curr, int i) {
        while (!limiters.contains(curr)) {
            if (has(b, curr) || has(o, curr) || has(d, curr) || has(hLet, curr)) {
                stack += curr;
                i++;
                curr = code.charAt(i);
            } else throw new RuntimeException("Неизвестный символ " + curr);
        }
        convertAndAdd(16);
        return i++;
    }

    private String convertInt(String sNum, int sNu) {
        int i = Integer.parseInt(sNum, sNu);
        return (Integer.toBinaryString(i));
    }

    private String convertReal(String sNum) {
        //System.out.println(Float.intBitsToFloat(Integer.parseInt(Integer.toBinaryString(Float.floatToIntBits(Float.valueOf(sNum))), 2)));
        return Integer.toBinaryString(Float.floatToIntBits(Float.valueOf(sNum)));
    }
}
