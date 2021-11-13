import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

class Constructors {
    String function;
    int number;
}

class Variables {
    String function;
}

class FirstAndSecond {
    String function;
}

public class TRS {

    public static StringBuilder uni = new StringBuilder();
    public static StringBuilder answer = new StringBuilder();

    public static FirstAndSecond[] firstandsecond_parse(String firstandsecond) {
        char[] str = firstandsecond.toCharArray();
        int i = 0;
        int count = 0;
        char first = 0;
        while (str[i] != "(".charAt(0) && i < str.length) {
            first = str[i];
            i++;
        }
        firstandsecond = firstandsecond.substring(i + 1,firstandsecond.length() - 1);
        StringBuilder sb = new StringBuilder();
        int kolvo = 0;
        for(char c : firstandsecond.toCharArray()) {
            if (c == "(".charAt(0)) count++;
            if (c == ")".charAt(0)) count--;
            if (c == ",".charAt(0) && count == 0) kolvo++;
        }
        FirstAndSecond[] ArrayOfFirstAndSecond = new FirstAndSecond[kolvo + 2];
        FirstAndSecond formm = new FirstAndSecond();
        formm.function = Character.toString(first);
        i = 0;
        ArrayOfFirstAndSecond[i] = formm;
        i++;
        for(char c : firstandsecond.toCharArray()) {
            if (c == "(".charAt(0)) count++;
            if (c == ")".charAt(0)) count--;
            if (c == " ".charAt(0)) continue;
            if (c == ",".charAt(0) && count == 0) {
                FirstAndSecond form = new FirstAndSecond();
                form.function = sb.toString();
                ArrayOfFirstAndSecond[i] = form;
                sb = sb.delete(0,sb.length());
                i++;
                continue;
            }
            sb.append(c);
        }
        FirstAndSecond form = new FirstAndSecond();
        form.function = sb.toString();
        ArrayOfFirstAndSecond[i] = form;
        return ArrayOfFirstAndSecond;
    }

    public static Variables[] variables_parse(String variables) {
        int occurrencesCount = variables.length() - variables.replace(",", "").length();
        Variables[] ArrayOfVariables = new Variables[occurrencesCount + 1];
        String[] array = variables.split("[ ,()]+");
        for (int i = 0; i <= occurrencesCount; i++) {
            Variables form = new Variables();
            form.function = array[i];
            ArrayOfVariables[i] = form;
        }
        return ArrayOfVariables;
    }

    public static Constructors[] constructors_parse(String constructors) {
        int occurrencesCount = constructors.length() - constructors.replace(",", "").length();
        Constructors[] ArrayOfConstructors = new Constructors[occurrencesCount+1];
        int count = 0;
        String[] array = constructors.split("[ ,()]+");
        for (int i = 0; i <= occurrencesCount; i++ , count +=2) {
            Constructors form = new Constructors();
            form.function = array[count];
            form.number = Integer.parseInt(array[count+1]);
            ArrayOfConstructors[i] = form;
        }
        return ArrayOfConstructors;
    }

    public static void parse_trs(String[] test){
        Constructors[] ArrayOfConstructors;
        Variables[] ArrayOfVariables;
        FirstAndSecond[] ArrayOfFirst;
        FirstAndSecond[] ArrayOfSecond;
        for (int i = 0; i < test.length; i++) {
            int value = test[i].indexOf('=');
            test[i] = test[i].substring(value + 2,test[i].length());
        }
        ArrayOfConstructors = constructors_parse(test[0]);
        ArrayOfVariables = variables_parse(test[1]);
        ArrayOfFirst = firstandsecond_parse(test[2]);
        ArrayOfSecond = firstandsecond_parse(test[3]);
        unification(ArrayOfConstructors,ArrayOfVariables,ArrayOfFirst,ArrayOfSecond);
    }

    public static boolean consist_variables(String ArrayOfFirst, Variables[] ArrayOfVariables) {
        for (Variables d : ArrayOfVariables) {
            if (Objects.equals(d.function, ArrayOfFirst)) return true;
        }
        return false;
    }

    public static int divide(String ArrayOfSecond) {
        int open = 0;
        int close = 0;
        int i = 0;
        for(char c : ArrayOfSecond.toCharArray()) {
            if (c == "(".charAt(0)) open++;
            if (c == ")".charAt(0)) close++;
            if (c == ",".charAt(0) && open == close) return i;
            i++;
        }
        return -1;
    }

    public static String find_subterm(String constr, String str) {
        String x = str;
        x = x.substring(constr.length() + 1);
        if (x.charAt(x.length() - 1) == ")".charAt(0)) x = x.substring(0, x.length() - 2);
        return x;
    }

    public static boolean consist_constr(String symbol, Constructors[] ArrayOfConstructors) {
        for (Constructors d : ArrayOfConstructors) {
            if (Objects.equals(d.function, symbol)) return true;
        }
        return false;
    }

    public static int find_constr(String symbol, Constructors[] ArrayOfConstructors) {
        for (int i = 0; i < ArrayOfConstructors.length; ++i) {
            if (Objects.equals(symbol, ArrayOfConstructors[i].function)) return ArrayOfConstructors[i].number;
        }
        return -5;
    }

    public static boolean check_rules_of_constr(String s, String x, Constructors[] ArrayOfConstructors) {
        int open = 0;
        int close = 0;
        int summary = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == "(".charAt(0)) open++;
            if (s.charAt(i) == ")".charAt(0)) close++;
            if (s.charAt(i) == ",".charAt(0) && open - 1 == close) summary++;
        }
        int amount_of = find_constr(x, ArrayOfConstructors) - 1;
        if (amount_of == -1 && summary == 0) return true;
        return summary == amount_of;
    }

    public static boolean branch(FirstAndSecond[] ArrayOfFirst, String x, String ArrayOfSecond, Constructors[] ArrayOfConstructors,Variables[] ArrayOfVariables) {
        if (consist_variables(x, ArrayOfVariables)) return false;
        else if (consist(ArrayOfFirst, find_subterm(x, ArrayOfSecond), ArrayOfConstructors, ArrayOfVariables)) {
            return consist_constr(x, ArrayOfConstructors) && check_rules_of_constr(ArrayOfSecond, x, ArrayOfConstructors);
        }
        return true;
    }

    public static boolean consist (FirstAndSecond[] ArrayOfFirst, String ArrayOfSecond, Constructors[] ArrayOfConstructors,Variables[] ArrayOfVariables) {
        String x = "";
        String y;
        String z;
        int i = 0;
        int sub_divide = divide(ArrayOfSecond);
        if (sub_divide != -1) {
            z = ArrayOfSecond.substring(0, sub_divide);
            y = ArrayOfSecond.substring(sub_divide + 1);
            if (!consist(ArrayOfFirst, z, ArrayOfConstructors, ArrayOfVariables)) return false;
            if (!consist(ArrayOfFirst, y, ArrayOfConstructors, ArrayOfVariables)) return false;
            return true;
        } else {
            while (i < ArrayOfSecond.length()) {
                if (ArrayOfSecond.charAt(i) == "(".charAt(0)) {
                    return branch(ArrayOfFirst, x, ArrayOfSecond, ArrayOfConstructors, ArrayOfVariables);
                } else x += ArrayOfSecond.charAt(i);
                i++;
            }
        }
        if (consist_variables(x, ArrayOfVariables)) return true;
        else return consist_constr(x, ArrayOfConstructors) && check_rules_of_constr(x, x, ArrayOfConstructors);
    }

    public static int[] parse_bracket(String first, String Second) {
        int i = 0, j = 0;
        while (i < first.length()) {
            if (first.charAt(i) == "(".charAt(0)) break;
            i++;
        }
        while (j < Second.length()) {
            if (Second.charAt(j) == "(".charAt(0)) break;
            j++;
        }
        return new int[]{i, j};
    }

    public static int check_consist_two_constr(String first, String second, Constructors[] ArrayOfConstructors, Variables[] ArrayOfVariables) {
        String x_first, y_first = null, x_second, y_second = null;
        int[] bracket;
        bracket = parse_bracket(first, second);
        x_first = first.substring(0, bracket[0]);
        if (bracket[0] < first.length()) {
            y_first = first.substring(bracket[0] + 1);
            y_first = y_first.substring(0, y_first.length() - 1);
        }
        x_second = second.substring(0, bracket[1]);
        if (bracket[1] < second.length()) {
            y_second = second.substring(bracket[1] + 1);
            //if (bracket[1] != second.length() - 1)
            y_second = y_second.substring(0, y_second.length() - 1);
        }
        if (consist_variables(x_first, ArrayOfVariables) && consist_constr(x_second, ArrayOfConstructors)) return 2;
        else if (consist_variables(x_second, ArrayOfVariables) && consist_constr(x_first, ArrayOfConstructors)) return 1;
        else if (consist_variables(x_second, ArrayOfVariables) && consist_variables(x_first, ArrayOfVariables)) return 0;
        else if (x_first.equals(x_second)) return check_consist_two_constr(y_first, y_second, ArrayOfConstructors, ArrayOfVariables);
        return -1;
    }

    public static boolean uni_rule(int i, Constructors[] ArrayOfConstructors, Variables[] ArrayOfVariables, FirstAndSecond[] ArrayOfFirst, FirstAndSecond[] ArrayOfSecond) {
        if (ArrayOfFirst.length != ArrayOfSecond.length || ArrayOfFirst[0].function.charAt(0) != ArrayOfSecond[0].function.charAt(0)) return false;
        else if (consist_variables(ArrayOfFirst[i].function, ArrayOfVariables)) {
            if (ArrayOfSecond[i].function.contains(ArrayOfFirst[i].function)) return true;
            if (consist(ArrayOfFirst, ArrayOfSecond[i].function, ArrayOfConstructors, ArrayOfVariables)) {
                TRS.answer.append(" " + ArrayOfFirst[i].function + ":=" + ArrayOfSecond[i].function + " ");
                TRS.uni.append(ArrayOfSecond[i].function + ",");
                return true;
            }
        } else if (consist_variables(ArrayOfSecond[i].function, ArrayOfVariables)) {
            if (ArrayOfFirst[i].function.contains(ArrayOfSecond[i].function)) return true;
            if (consist(ArrayOfFirst, ArrayOfFirst[i].function, ArrayOfConstructors, ArrayOfVariables)) {
                TRS.answer.append(" " + ArrayOfSecond[i].function + ":=" + ArrayOfFirst[i].function + " ");
                TRS.uni.append(ArrayOfFirst[i].function + ",");
                return true;
            }
        } else if (consist(ArrayOfFirst, ArrayOfFirst[i].function, ArrayOfConstructors, ArrayOfVariables) && consist(ArrayOfFirst, ArrayOfSecond[i].function, ArrayOfConstructors, ArrayOfVariables)) {
            int ans = check_consist_two_constr(ArrayOfFirst[i].function, ArrayOfSecond[i].function, ArrayOfConstructors, ArrayOfVariables);
            if (ans == 2) {
                TRS.answer.append(" " + ArrayOfFirst[i].function + ":=" + ArrayOfSecond[i].function + " ");
                TRS.uni.append(ArrayOfSecond[i].function + ",");
                return true;
            } else if (ans == 1) {
                TRS.answer.append(" " + ArrayOfSecond[i].function + ":=" + ArrayOfFirst[i].function + " ");
                TRS.uni.append(ArrayOfFirst[i].function + ",");
                return true;
            } else if (ans == 0) return false;
        }
        return false;
    }

    public static void unification(Constructors[] ArrayOfConstructors, Variables[] ArrayOfVariables, FirstAndSecond[] ArrayOfFirst, FirstAndSecond[] ArrayOfSecond) {
        for (int i = 1; i < ArrayOfFirst.length; i++) {
            if (!uni_rule(i, ArrayOfConstructors,ArrayOfVariables,ArrayOfFirst,ArrayOfSecond)) {
                System.out.println("термы НЕ унифицируются посредством подстановок");
                return;
            }
        }
        TRS.uni.delete(TRS.uni.length() - 1, TRS.uni.length());
        System.out.println("термы унифицируются посредством подстановок");
        System.out.println(TRS.answer);
        System.out.println("Унификатор -");
        System.out.print(ArrayOfFirst[0].function + "(");
        System.out.println(TRS.uni + ")");
        TRS.uni.delete(0, TRS.uni.length());
        TRS.answer.delete(0, TRS.answer.length());
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 6; i++) {
                File file = new File("/home/caapricorn/IdeaProjects/tfl_lab1/test" + Integer.toString(i) + ".txt");
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                String[] test = new String[4];
                String line = reader.readLine();
                int count = 0;
                System.out.println("Тест " + Integer.toString(i));
                while (line != null) {
                    test[count] = line;
                    count++;
                    System.out.println(line);
                    line = reader.readLine();
                }
                parse_trs(test);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
