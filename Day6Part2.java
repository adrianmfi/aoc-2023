import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Day6Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day6-modified.txt";
        var times = new ArrayList<Long>();
        var records = new ArrayList<Long>();

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());
        var numPattern = Pattern.compile("(\\d+)+");
        var timesMatcher = numPattern.matcher(lines[0]);
        var recordsMatcher = numPattern.matcher(lines[1]);
        while (timesMatcher.find()) {
            times.add(Long.parseLong(timesMatcher.group()));
        }
        while (recordsMatcher.find()) {
            records.add(Long.parseLong(recordsMatcher.group()));
        }

        int result = 1;
        for (int gameNum = 0; gameNum < times.size(); gameNum++) {
            var time = times.get(gameNum);
            var record = records.get(gameNum);
            long numWinning = 0;
            for (long startingTime = 1; startingTime < time; startingTime++) {
                if (startingTime * (time - startingTime) > record) {
                    numWinning++;
                }
            }
            result *= numWinning;
        }
        System.out.println(result);
    }
}