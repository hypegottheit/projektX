package sample;

import java.util.ArrayList;
import java.util.Iterator;

public class SA {
    private Node root;
    private Node current;
    ArrayList<Lexem> table;
    Iterator<Lexem> it;
    ArrayList<String> ob;
    ArrayList<Lexem> vars;

    public Node analyze(ArrayList<Lexem> res, ArrayList<Lexem> vars) {
        this.table = res;
        this.vars = vars;
        ob = new ArrayList<>();
        it = table.listIterator();
        root = new Node("S");
        current = root;
        Lexem lexem;
        while (it.hasNext()) {
            lexem = it.next();
            if (lexem.getValue().equals("end")) return null;
            if (lexem.getValue().equals("let")) let(null);
            else if (lexem.getValue().equals("int") || lexem.getValue().equals("float") || lexem.getValue().equals("bool")) {
                assignment(lexem);
            } else operator(lexem);
            current = root;
        }
        if (!table.get(table.size() - 1).getValue().equals("end"))
            throw new RuntimeException("Ошибка: ожидался 'end'; Получено ");
        return root;
    }

    private void operator(Lexem lexem) {
        Node node = createNode("operator");
        if (lexem.getValue().equals("if")) condition();
        else if (lexem.getValue().equals("input")) input();
        else if (lexem.getValue().equals("output")) output();
        else if (lexem.getTable() == 4) let(lexem);
        else if (lexem.getValue().equals("let")) let(null);
        else if (lexem.getValue().equals("{")) compositeOperator();
        else cycle(lexem);
        current = node;
    }

    private void operator() {
        Node node = createNode("operator");
        Lexem lexem = it.next();
        if (lexem.getValue().equals("if")) condition();
        else if (lexem.getValue().equals("input")) input();
        else if (lexem.getValue().equals("output")) output();
        else if (lexem.getValue().equals("let")) let(null);
        else if (lexem.getTable() == 4) {
            let(lexem);
        } else if (lexem.getValue().equals("{")) compositeOperator();
        else cycle(lexem);
        current = node;
    }

    private void cycle(Lexem lexem) {
        if (lexem.getValue().equals("for")) cycleFor();
        else if (lexem.getValue().equals("do")) cycleDoWhile();
        else throw new RuntimeException("Ошибка: ожидался 'for' или 'do while'; Получено " + lexem.getValue());
    }

    private void cycleFor() {
        Node node = createNode("for");
        Lexem lexem = it.next();
        if (!lexem.getValue().equals("("))
            throw new RuntimeException("Ошибка: ожидался '(' Получено " + lexem.getValue());
        boolean b = assignment(it.next());
        current = node;
        if (!b)
            if (!it.next().getValue().equals(";"))
                throw new RuntimeException("Ошибка: ожидался ';' Получено " + lexem.getValue());
        boolExpression();
        current = node;
        lexem = it.next();
        if (!lexem.getValue().equals(";"))
            throw new RuntimeException("Ошибка: ожидался ';' Получено " + lexem.getValue());
        let(null);
        current = node;
        lexem = it.next();
        if (!lexem.getValue().equals(")"))
            throw new RuntimeException("Ошибка: ожидался ')' Получено " + lexem.getValue());
        operator();
        current = node;

    }

    private void cycleDoWhile() {
        Node node = createNode("do while");
        Lexem lexem = it.next();
        if (!lexem.getValue().equals("while"))
            throw new RuntimeException("Ошибка: ожидался 'while' Получено " + lexem.getValue());
        boolExpression();
        current = node;
        operator(it.next());
        current = node;
        lexem = it.next();
        if (!lexem.getValue().equals("loop"))
            throw new RuntimeException("Ошибка: ожидался 'loop' Получено " + lexem.getValue());
        if (!it.next().getValue().equals(":"))
            throw new RuntimeException("Ошибка: ожидался ':' Получено " + lexem.getValue());
    }

    private void compositeOperator() {
        Node node = createNode("composite");
        while (it.hasNext()) {
            operator();
            current = node;
            Lexem lexem = it.next();
            if (lexem.getValue().equals("}")) {
                return;
            }
            if (lexem.getValue().equals(";")) {
                operator();
                current = node;
            }
        }
    }

    private void input() {
        Node node = createNode("input");
        Lexem l = it.next();
        if (l.getValue().equals("(")) {
            l = it.next();
            while (!l.getValue().equals(")")) {
                if (l.getTable() != 4)
                    throw new RuntimeException("Ошибка: ожидался идентификатор Получено " + l.getValue());
                if (!ob.contains(l.getValue()))
                    throw new RuntimeException("Переменная " + l.getValue() + " не объявлена");
                createNode(l.getValue());
                current = node;
                l = it.next();
            }
            if (it.next().getValue().equals(":"))
                return;
            else throw new RuntimeException("Ошибка: ожидался ':' Получено " + l.getValue());
        } else throw new RuntimeException("Ошибка: ожидался '(' Получено " + l.getValue());
    }

    private void output() {
        Node node = createNode("output");
        Lexem l = it.next();
        if (l.getValue().equals("(")) {
            outexpression();
            l = it.next();
            if (!l.getValue().equals(":"))
                throw new RuntimeException("Ошибка: ожидался ':' Получено " + l.getValue());
        } else throw new RuntimeException("Ошибка: ожидался '(' Получено " + l.getValue());
    }

    private void outexpression() {
        Node node = createNode("expression");
        Lexem l = it.next();
        while (!l.getValue().equals(")")) {
            String type = getType(l);
            if (!((l.getTable() == 4 && ob.contains(l.getValue())) || l.getTable() == 3))
                throw new RuntimeException("Ошибка: ожидалось выражение или переменная");
            l = it.next();
            if (l.getValue().equals(")")) break;
            else {
                if (isMathGroup(l)) {
                    l = it.next();
                    if ((((l.getTable() == 4 && ob.contains(l.getValue())) || l.getTable() == 3)) &&
                            getType(l).equals(type)) {
                        l = it.next();
                    } else
                        throw new RuntimeException("Ошибка: несовместимые типы в выражении " + type + " и " + getType(l));
                } else if (l.getTable() != 4)
                    throw new RuntimeException("Ошибка: ожидалось выражение или переменная");
            }
        }
    }

    private void condition() {
        Node node = createNode("if");
        String espr = boolExpression();
        if (!espr.equals("boolean") && (!espr.equals("one")))
            throw new RuntimeException("Ошибка: ожидался тип boolean");
        current = node;
        if (!espr.equals("one")) {
            Lexem lexem = it.next();
            if (!lexem.getValue().equals("then"))
                throw new RuntimeException("Ошибка: ожидался 'then' Получено " + lexem.getValue());
        }
        Node nodethen = createNode("then");
        operator();
        current = node;
        Lexem lexem = it.next();
        if (lexem.getValue().equals("else")) {
            nodethen = createNode("else");
            operator();
            current = node;
            lexem = it.next();
        }
        if (!lexem.getValue().equals("end_else"))
            throw new RuntimeException("Ошибка: ожидался 'end_else' Получено " + lexem.getValue());
        if (!it.next().getValue().equals(":")) throw new RuntimeException("Ошибка: ожидался ':'");
    }

    private void let(Lexem lexem) {
        if (lexem == null)
            lexem = it.next();
        if (ob.contains(lexem.getValue())) {
            Node node = createNode("let");
            Lexem l = it.next();
            if (!l.getValue().equals("="))
                throw new RuntimeException("Ошибка: ожидался '=' Получено " + l.getValue());
            mathExpression(getType(lexem));
            current = node;
        } else throw new RuntimeException("Переменная " + lexem.getValue() + " не объявлена");
    }

    private boolean assignment(Lexem ident) {
        Lexem next = it.next();
        ArrayList<Lexem> newLexems = new ArrayList<>();
        while (!next.getValue().equals(":")) {
            if (!ob.contains(next.getValue())) {
                ob.add(next.getValue());
                int y = next.getNum();
                next.setT(ident.getValue());
                vars.set(y, next);
                next = it.next();
                if (next.getValue().equals(":"))
                    return true;
                if (!next.getValue().equals(","))
                    throw new RuntimeException("Ошибка: ожидался ',' Получено:" + next.getValue());
                next = it.next();
            } else throw new RuntimeException("Переменная " + next.getValue() + " уже объявлена");
        }
        return true;
    }

    private String boolExpression() {
        Node node = createNode("expression");
        Lexem l = it.next();
        if (l.getTable() == 4 && ob.contains(l.getValue())) {
            String type = getType(l);
            l = it.next();
            if (isBoolGroup(l)) {
                l = it.next();
                if ((l.getTable() == 4 && ob.contains(l.getValue())) &&
                        getType(l).equals(type))
                    return "boolean";
                else throw new RuntimeException("Ошибка: несовместимые типы в выражении " + type + " и " + getType(l));
            } else if ((l.getValue().equals("then")) && (type.equals("boolean")))
                return "one";
            else throw new RuntimeException("Ошибка в выражении. Ожидалась перемнная типа boolean или выражение");
        }
        throw new RuntimeException("Ошибка в выражении. Ожидалась перемнная типа boolean или выражение");
    }

    private String mathExpression(String identType) {
        Node node = createNode("expression");
        Lexem l = it.next();
        if (l.getValue().equals("true") || l.getValue().equals("false")) {
            if (!identType.equals("boolean"))
                throw new RuntimeException("Ошибка : несовместимые типы в выражении Получено " + identType + " и boolean");
            else {
                l = it.next();
                if (l.getValue().equals(";"))
                    return "pris";
                else throw new RuntimeException("Ошибка ожидался ';' Получено " + l.getValue());
            }
        } else if (identType.equals(getType(l)) || identType.equals(l.getT())) {
            String type = getType(l);
            l = it.next();
            if (isMathGroup(l)) {
                l = it.next();
                if (((l.getTable() == 4 && ob.contains(l.getValue())) || l.getTable() == 3) &&
                        getType(l).equals(type))
                    return type;
                else throw new RuntimeException("Ошибка: несовместимые типы в выражении " + type + " и " + getType(l));
            }
            if (l.getValue().equals(":"))
                return "pris";
            else throw new RuntimeException("Ошибка ожидался ';' Получено " + l.getValue());
        }
        throw new RuntimeException("Ошибка: несовместимые типы в выражении ");
    }

    private String getType(Lexem l) {
        if (l.getValue().equals("true") || l.getValue().equals("false"))
            return "boolean";
        if (l.getT() != null)
            return l.getT();
        if (l.getTable() == 4)
            return vars.get(l.getNum()).getT();
        return "int";
    }

    private boolean isBoolGroup(Lexem lexem) {
        return lexem.getValue().equals("ne") || lexem.getValue().equals("eq") || lexem.getValue().equals("lt")
                || lexem.getValue().equals("le") || lexem.getValue().equals("gt") || lexem.getValue().equals("ge");
    }

    private boolean isMathGroup(Lexem lexem) {
        return lexem.getValue().equals("plus") || lexem.getValue().equals("min") || lexem.getValue().equals("or")
                || lexem.getValue().equals("mult") || lexem.getValue().equals("div") || lexem.getValue().equals("and");
    }

    private Node createNode(String name) {
        Node node = new Node(name);
        current.addNode(node);
        current = node;
        return node;
    }
}

