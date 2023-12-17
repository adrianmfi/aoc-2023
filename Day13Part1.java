import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Day13Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day13.txt";
        var patterns = Files.readString(Paths.get(filePath))
                .strip()
                .split("\n\n");

        var res = 0;
        for (int i = 0; i < patterns.length; i++) {
            final int index = i;
            res += getReflectionRow(patterns[i]).map(value -> value * 100)
                    .or(() -> getReflectionRow(rotateString(patterns[index])))
                    .orElseThrow();
        }
        System.out.println(String.format("Result is %s", res));

    }

    static Optional<Integer> getReflectionRow(String pattern) {
        List<Integer> candidates = new ArrayList<Integer>();
        var lines = pattern.split(System.lineSeparator());
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].equals(lines[i + 1])) {
                candidates.add(i);
            }
        }

        candidates = candidates.stream().filter(
                candidate -> {
                    for (int i = 1; i < Math.min(candidate + 1, lines.length - candidate - 1); i++) {
                        if (!lines[candidate - i].equals(lines[candidate + i + 1])) {
                            return false;
                        }
                    }

                    return true;
                }).toList();
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(candidates.get(0) + 1);
    }

    static String rotateString(String original) {
        var lines = original.split(System.lineSeparator());
        var sb = new StringBuilder();
        for (int i = 0; i < lines[0].length(); i++) {

            for (int j = lines.length - 1; j >= 0; j--) {
                sb.append(lines[j].charAt(i));
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

}