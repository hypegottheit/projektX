package taf.kr.core;

import java.util.ArrayList;
import java.util.Arrays;

public class Table {
    public ArrayList<String> functionWords =
            new ArrayList<String>(Arrays.asList("do", "else", "end_else", "false", "for", "if", "input", "let",
                    "loop", "output", "then", "true", "while"));
    public ArrayList<String> limiters = new ArrayList<String>(Arrays.asList("{", "}", ";", ",", "(", ")", ":", "=",
            "+", "~", "-", "and", "div", "eq", "ge", "gt", "le", "lt", "min", "mult", "ne", "or", "plus", "int", "float", "bool", "end"));
    public ArrayList<Lexem> varnames = new ArrayList();
    public ArrayList<String> names = new ArrayList();
    public ArrayList<Digit> digits = new ArrayList();
    public ArrayList<Lexem> res = new ArrayList();
}
