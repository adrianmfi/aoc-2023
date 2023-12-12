import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day9Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day9.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var res = Arrays.stream(lines)
                .map(Day9Part1::parseLine)
                .map(Day9Part1::extrapolate)
                .mapToInt(Integer::valueOf)
                .sum();
        System.out.println(String.format("Result is %s", res));
    }

    static int[] parseLine(String line) {
        return Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    static int extrapolate(int[] sequence) {
        if (Arrays.stream(sequence).allMatch(num -> num == 0)) {
            return 0;
        }
        var child = new int[sequence.length - 1];
        for (int i = 0; i < sequence.length - 1; i++) {
            child[i] = sequence[i + 1] - sequence[i];
        }
        var extrapolatedChild = extrapolate(child);
        return sequence[sequence.length - 1] + extrapolatedChild;
    }

}
