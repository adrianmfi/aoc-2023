import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day15Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day15.txt";
        var sequences = Files.readString(Paths.get(filePath))
                .strip().split(",");

        int res = 0;
        for (var str : sequences) {
            res += customHash(str);
        }
        System.out.println(String.format("Result is %s", res));
    }

    static public int customHash(String s) {
        var res = 0;
        for (var character : s.toCharArray()) {
            res += character;
            res *= 17;
            res %= 256;
        }
        return res;
    }

}