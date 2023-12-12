import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day8Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day8.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var directions = lines[0];

        var graph = new HashMap<String, LeftRight>();

        var nodePattern = Pattern.compile("(?<name>.{3}) = [(](?<left>.{3}), (?<right>.{3})[)]");
        for (int i = 2; i < lines.length; i++) {
            var matcher = nodePattern.matcher(lines[i]);
            matcher.find();
            var nodeName = matcher.group("name");
            var left = matcher.group("left");
            var right = matcher.group("right");
            graph.put(nodeName, new LeftRight(left, right));
        }

        var nodes = graph.keySet().stream().filter(node -> node.endsWith("Z"))
                .collect(Collectors.toCollection(ArrayList::new));
        int[] offsets = new int[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            int numSteps = 0;
            var zFound = false;
            while (!zFound) {
                var directionIndex = numSteps % directions.length();
                var step = directions.charAt(directionIndex);

                String node = null;
                if (step == 'L') {
                    node = graph.get(nodes.get(i)).left();
                } else {
                    node = graph.get(nodes.get(i)).right();
                }
                nodes.set(i, node);
                numSteps++;

                if (node.endsWith("Z")) {
                    offsets[i] = numSteps;
                    zFound = true;
                }
            }
        }
        // With some hint, learned that the correct node is hit at cycles starting from
        // 0 but with different intervals.
        // With that, we can find the first index where all collides using LCM.
        var lcm = findLCM(offsets);
        System.out.println(lcm);
    }

    record LeftRight(String left, String right) {
    }

    static long findLCM(int[] array) {
        long result = array[0];
        for (int i = 1; i < array.length; i++) {
            result = lcm(result, array[i]);
        }
        return result;
    }

    static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

}
