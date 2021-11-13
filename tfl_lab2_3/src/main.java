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

    public static ArrayList<String> DeleteVar(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            String d = test.get(i).substring(0, 1);
            while (test.get(i).lastIndexOf(d) != 0) {
                int count = test.get(i).lastIndexOf(d);
//                if (test.get(i).length() >= count && Objects.equals(test.get(i).substring(count + 1, count + 2), "+")) {
//                    test.set(i, test.get(i).substring(0, count + 1) + test.get(i).substring(count + 2));
//                }
                test.set(i, test.get(i).substring(0, count) + "*" + test.get(i).substring(count + 1));
            }
        }
        return test;
    }

    public static void Algorithm(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            //if (test.get(i).substring(0, 1).equals("S")) continue;
            String d = test.get(i).substring(0, 1);
            for (int j = 0; j < test.size(); j++ ) {
                if (j == i) continue;
                //else if (test.get(j).substring(0, 1).equals("S")) continue;
                while (test.get(j).contains(d)) {
                    int count = test.get(j).lastIndexOf(d);
                    test.set(j, test.get(j).substring(0, count) + "(" + test.get(i).substring(3) + ")" + test.get(j).substring(count + 1));
                    test = DeleteVar(test);
                }
            }
        }

        System.out.println("Ответ:");
        for (String d : test) {
            d = d.replaceAll("->", "=");
            System.out.println(d);
        }
    }

    public static boolean CheckIdentical(String test1, String test2) {
        test1 = test1.substring(3);
        test2 = test2.substring(3);
        String[] arrStrings1 = test1.split("[|]");
        //String[] arrStrings2 = test2.split("|");
        for (int i = 0; i < arrStrings1.length; i++) {
            if (Objects.equals(arrStrings1[i], test2)) return false;
        }
        return true;
    }

    public static ArrayList<String> simplify(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            for (int j = i + 1; j < test.size(); j++) {
                //if (j <= i) continue;
                if (test.get(i).substring(0, 1).equals(test.get(j).substring(0, 1))) {
                    //if (!test.get(i).contains(test.get(j).substring(3)))
                    if (CheckIdentical(test.get(i), test.get(j)))
                    test.set(i, test.get(i) + "|" + test.get(j).substring(3));
                    test.remove(j);
                    j--;
                }
            }
        }
        test = DeleteVar(test);
        for (String d : test) System.out.println("simplify " + d);
        return test;
    }

    public static ArrayList<String> DelSupport(ArrayList<String> test, String str) {
        for (int k = 0; k < test.size(); k++) {
            if (test.get(k).contains(str)) {
                test.remove(k);
                k--;
            }
        }
        return test;
    }

    public static ArrayList<String> Del(ArrayList<String> test, String str) {
        for (int k = 0; k < test.size(); k++) {
            if (test.get(k).contains(str) && !str.equals("S")) {
                String str1 = test.get(k).substring(0, 1);
                test.remove(k);
                k--;
                if (!str1.equals("S")) test = DelSupport(test, str1);
            }
        }
        return test;
    }

    public static ArrayList<String> CheckTrap(ArrayList<String> test) {
        for (int i = 0; i < test.size(); i++) {
            String str1 = test.get(i).substring(0, 1);
            for (int j = i + 1; j < test.size(); j++) {
                String str2 = test.get(j).substring(0, 1);
                if (str1.equals(str2)) continue;
                if (test.get(j).contains(str1) && test.get(i).contains(str2) && !str1.equals("S") && !str2.equals("S")) {
                    test.remove(i);
                    test.remove(j-1);
                    --i;
                    j--;
                    test = Del(test, str1);
                    test = Del(test, str2);
                }
            }
        }
        System.out.println("Without trap:");
        for (String d : test) System.out.println(d);
        return test;
    }

    public static boolean Parse(ArrayList<String> test) {
        test = DeleteSpaces(test);
        Pattern pattern = Pattern.compile("[A-Z]->([a-z][A-Z]|[a-z])");
        //System.out.println("регурялрка - ");
        for (int i = 0; i < test.size(); i++) {
            Matcher matcher = pattern.matcher(test.get(i));
            if (matcher.find()) {
                if (!Objects.equals(test.get(i), test.get(i).substring(matcher.start(), matcher.end()))) return false;
                //System.out.println(test.get(i).substring(matcher.start(), matcher.end()));
                //else return false;
            } else return false;
        }
        test = CheckTrap(test);
        test = simplify(test);
        Algorithm(test);
        return true;
    }

    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 8; i++) {
                File file = new File("/home/caapricorn/IdeaProjects/tfl_lab2_3/test" + Integer.toString(i) + ".txt");
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
