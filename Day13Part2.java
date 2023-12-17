import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Day13Part2 {
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

    // 0: equal
    // 1: one equal
    // 2: multiple equal
    static int characterDiff(String s1, String s2) {
        boolean hasOne = false;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                if (hasOne) {
                    return 2;
                }
                hasOne = true;
            }
        }
        return hasOne ? 1 : 0;
    }

    static Optional<Integer> getReflectionRow(String pattern) {

        Set<Integer> candidates = new HashSet<Integer>();
        var lines = pattern.split(System.lineSeparator());
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].equals(lines[i + 1]) || characterDiff(lines[i], lines[i + 1]) == 1) {
                candidates.add(i);
            }
        }

        candidates = candidates.stream().filter(
                candidate -> {
                    var foundLine = false;
                    for (int i = 0; i < Math.min(candidate + 1, lines.length - candidate - 1); i++) {
                        var line1 = lines[candidate - i];
                        var line2 = lines[candidate + i + 1];
                        var diff = characterDiff(line1, line2);
                        if (diff == 1) {
                            if (foundLine) {
                                return false;
                            }
                            foundLine = true;
                        } else if (diff > 1) {
                            return false;
                        }
                    }
                    return foundLine;
                }).collect(Collectors.toSet());
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(candidates.iterator().next() + 1);
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