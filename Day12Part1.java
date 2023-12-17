import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Day12Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day12.txt";
        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var res = Arrays.stream(lines)
                .map(Day12Part1::parseLine)
                .map(Day12Part1::numCombinations)
                .mapToLong(Long::valueOf)
                .sum();
        System.out.println(String.format("Result is %s", res));

    }

    static Sequence parseLine(String line) {
        var p = Pattern.compile("(\\S+) ((\\d.*)+)");
        var matcher = p.matcher(line);
        matcher.find();

        var firstGroup = matcher.group(1);
        var numbers = matcher.group(2).split(",");

        var groups = new ArrayList<Integer>();
        for (var number : numbers) {
            groups.add(Integer.parseInt(number));
        }

        return new Sequence(firstGroup, groups);
    }

    static long numCombinations(Sequence line) {
        return countForSequence(line, 0);
    }

    static int getGroup(List<Integer> groups, Integer index) {
        if (index >= groups.size()) {
            return 0;
        } else {
            return groups.get(index);
        }
    }

    static int countForSequence(Sequence sequence, int numContiguous) {
        var groups = sequence.groups();
        if (sequence.groups().isEmpty()) {
            return sequence.characters().contains("#") || numContiguous > 0 ? 0 : 1;
        }
        var numNeededCharacters = groups.stream().mapToInt(i -> i).sum() + groups.size() - 1;
        if (sequence.characters().length() + numContiguous < numNeededCharacters) {
            return 0;
        }
        var groupIndex = 0;
        var currentGroup = getGroup(groups, groupIndex);

        for (int sequenceIndex = 0; sequenceIndex < sequence.characters().length(); sequenceIndex++) {
            var currentChar = sequence.characters().charAt(sequenceIndex);

            switch (currentChar) {
                case '?': {
                    if (numContiguous == 0) {
                        // A choice must be made. Generate two options, one with # and another with .
                        var remainingGroups = new ArrayList<>(groups.subList(groupIndex, groups.size()));
                        var remainingSequence = new Sequence(sequence.characters().substring(sequenceIndex + 1),
                                remainingGroups);
                        var dotSequenceCount = countForSequence(remainingSequence, 0);
                        var hashSequenceCount = countForSequence(remainingSequence, numContiguous + 1);

                        return dotSequenceCount + hashSequenceCount;
                    } else if (numContiguous < currentGroup) {
                        // Choice must be set to #
                        var remainingGroups = new ArrayList<>(groups.subList(groupIndex, groups.size()));
                        var remainingSequence = new Sequence(sequence.characters().substring(sequenceIndex + 1),
                                remainingGroups);
                        var hashSequenceCount = countForSequence(remainingSequence, numContiguous + 1);
                        return hashSequenceCount;
                    } else {
                        // current must be set to ., and we advance to the next group
                        numContiguous = 0;
                        groupIndex++;
                        currentGroup = getGroup(groups, groupIndex);
                        break;
                    }
                }
                case '#': {
                    if (numContiguous == currentGroup) {
                        // Invalid
                        return 0;
                    } else {
                        numContiguous++;
                    }
                    break;
                }
                case '.': {
                    if (numContiguous == 0) {
                        continue;
                    }
                    if (numContiguous < currentGroup) {
                        return 0;
                    } else {
                        numContiguous = 0;
                        groupIndex++;
                        currentGroup = getGroup(groups, groupIndex);
                    }
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unexpected char");
            }

        }

        return groupIndex >= groups.size() - 1 && getGroup(groups, groupIndex) == numContiguous ? 1 : 0;

    }

    record Sequence(String characters, List<Integer> groups) {
    };

}