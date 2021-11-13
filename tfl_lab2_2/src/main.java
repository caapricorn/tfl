import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {

    public static ArrayList<String> DeleteSpaces(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            test.set(i, test.get(i).replaceAll(" ", ""));
        }
        return test;
    }

    public static boolean CheckVar(ArrayList<String> test) {
        ArrayList<String> equations = new ArrayList<String>();
        ArrayList<String> variables = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[A-Z]");
        int count = 0;
        for (int i = 0; i < test.size(); i++) {
            Matcher matcher = pattern.matcher(test.get(i));
            while (matcher.find()) {
                if (count == 0) equations.add(test.get(i).substring(matcher.start(), matcher.end()));
                else variables.add(test.get(i).substring(matcher.start(), matcher.end()));
                //System.out.println(test.get(i).substring(matcher.start(), matcher.end()));
                count++;
            }
            count = 0;
        }
        for (String d : variables) {
            if (!equations.contains(d)) return false;
        }
        return true;
    }

    public static ArrayList<String> DeleteVar(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            String d = test.get(i).substring(0, 1);
            while (test.get(i).lastIndexOf(d) != 0) {
                int count = test.get(i).lastIndexOf(d);
                if (test.get(i).length() >= count && Objects.equals(test.get(i).substring(count + 1, count + 2), "+")) {
                    test.set(i, test.get(i).substring(0, count + 1) + test.get(i).substring(count + 2));
                }
                test.set(i, test.get(i).substring(0, count) + "*" + test.get(i).substring(count + 1));
            }
        }
        return test;
    }

    public static void Algorithm(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            String d = test.get(i).substring(0, 1);
            for (int j = 0; j < test.size(); j++ ) {
                if (j == i) continue;
                while (test.get(j).contains(d)) {
                    int count = test.get(j).lastIndexOf(d);
                    test.set(j, test.get(j).substring(0, count) + "(" + test.get(i).substring(2) + ")" + test.get(j).substring(count + 1));
                    test = DeleteVar(test);
                }
            }
        }
        System.out.println("Ответ:");
        for (String d : test) System.out.println(d);
    }

    public static boolean Parse(ArrayList<String> test) {
        test = DeleteSpaces(test);
        Pattern pattern = Pattern.compile("[A-Z]=((\\([a-z][a-z]*\\|[a-z][a-z]*(\\|[a-z][a-z]*)*\\)|[a-z][a-z]*)[A-Z](\\+(\\([a-z][a-z]*\\|[a-z][a-z]*(\\|[a-z][a-z]*)*\\)|[a-z][a-z]*)[A-Z])*\\+)?(\\([a-z][a-z]*\\|[a-z][a-z]*(\\|[a-z][a-z]*)*\\)|[a-z][a-z]*)");
        //System.out.println("регурялрка - ");
        for (int i = 0; i < test.size(); i++) {
            Matcher matcher = pattern.matcher(test.get(i));
            if (matcher.find()) {
                 if (!Objects.equals(test.get(i), test.get(i).substring(matcher.start(), matcher.end()))) return false;
                    //System.out.println(test.get(i).substring(matcher.start(), matcher.end()));
                 //else return false;
            } else return false;
        }
        if (!CheckVar(test)) return false;
        test = DeleteVar(test);
        Algorithm(test);
    return true;
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 6; i++) {
                File file = new File("/home/caapricorn/IdeaProjects/tfl_lab2_2/test" + Integer.toString(i) + ".txt");
                FileReader fr = new FileReader(file);
                BufferedReader reader = new BufferedReader(fr);
                ArrayList<String> list = new ArrayList<String>();
                String line = reader.readLine();
                System.out.println("Тест " + Integer.toString(i));
                while (line != null) {
                    list.add(line);
                    System.out.println(line);
                    line = reader.readLine();
                }
                if (!Parse(list)) System.out.println("Некорректная система уравнений!!!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
