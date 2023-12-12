import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Day8Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day8.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var directions = lines[0];

        var graph = new HashMap<String, LeftRight>();

        var nodePattern = Pattern.compile("(?<name>[A-Z]{3}) = [(](?<left>[A-Z]{3}), (?<right>[A-Z]{3})[)]");
        for (int i = 2; i < lines.length; i++) {
            var matcher = nodePattern.matcher(lines[i]);
            matcher.find();
            var nodeName = matcher.group("name");
            var left = matcher.group("left");
            var right = matcher.group("right");
            graph.put(nodeName, new LeftRight(left, right));
        }

        int numSteps = 0;
        var currentNode = "AAA";
        while (!currentNode.equals("ZZZ")) {
            var directionNum = numSteps % directions.length();
            var nextStep = directions.substring(directionNum, directionNum + 1);
            if (nextStep.equals("L")) {
                currentNode = graph.get(currentNode).left();
            } else {
                currentNode = graph.get(currentNode).right();
            }
            numSteps++;
        }
        System.out.println(numSteps);

    }

    record LeftRight(String left, String right) {
    }

}
