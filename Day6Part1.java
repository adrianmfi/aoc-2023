import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day6Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day6.txt";
        var times = new ArrayList<Integer>();
        var records = new ArrayList<Integer>();

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());
        var numPattern = Pattern.compile("\\d+");
        var timesMatcher = numPattern.matcher(lines[0]);
        var recordsMatcher = numPattern.matcher(lines[1]);
        while (timesMatcher.find()) {
            times.add(Integer.parseInt(timesMatcher.group()));
        }
        while (recordsMatcher.find()) {
            records.add(Integer.parseInt(recordsMatcher.group()));
        }

        int result = 1;
        for (int gameNum = 0; gameNum < times.size(); gameNum++) {
            var time = times.get(gameNum);
            var record = records.get(gameNum);
            int numWinning = 0;
            for (int startingTime = 1; startingTime < time; startingTime++) {
                if (startingTime * (time - startingTime) > record) {
                    numWinning++;
                }
            }
            result *= numWinning;
        }
        System.out.println(result);
    }
}