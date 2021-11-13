import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static void main(String[] args) {
        Instant start = Instant.now();
        try {
            File file = new File("/home/caapricorn/IdeaProjects/tfl_lab2_1/test.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String[] test = new String[10];
            String line = reader.readLine();
            int count = 0;
            while (line != null) {
                test[count] = line;
                count++;
                //
                line = reader.readLine();
            }

            for (int i = 0; i < 7; i++) {
                System.out.println("тест" + i);
                Pattern pattern = Pattern.compile("([1-9][0-9]*(0))*[048]|([1-9][0-9]*)*[13579][26]|([1-9][0-9]*)*[2468][048]");
                //Pattern pattern = Pattern.compile("([^0]\\d*0)*[^(1235679)]|([^0]\\d*)*[^(02468)][^(01345789)]|([^0]\\d*)*[^(013579)][^(1235679)]");
                //Pattern pattern = Pattern.compile("([1-9][0-9]*?(0))*?[048]|([1-9][0-9]*?)*?[13579][26]|([1-9][0-9]*?)*?[2468][048]");
                Matcher matcher = pattern.matcher(test[i]);
                while (matcher.find()) {
                    System.out.println(test[i].substring(matcher.start(), matcher.end()));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
    }
}
