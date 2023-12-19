import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day18Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day18.txt";
        var instructions = Arrays.stream(Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator())).map(Instruction::of).toList();
        var points = parseRing(instructions);
        var area = ringArea(points);
        System.out.println(area);
    }

    // Assumes clockwise polygon - if the area is negative, then inverse
    // instructions
    static Position[] parseRing(List<Instruction> instructions) {
        var points = new ArrayList<Position>();
        var currentPoint = new Position(0, 0);

        for (int i = 0; i < instructions.size(); i++) {
            points.add(currentPoint);

            var row = currentPoint.row();
            var col = currentPoint.col();
            var instruction = instructions.get(i);
            var prevInstruction = instructions.get((i - 1 + instructions.size()) % instructions.size());
            var nextInstruction = instructions.get((i + 1) % instructions.size());
            var distanceModifier = getDistanceModifier(prevInstruction.dir(), instruction.dir(), nextInstruction.dir());

            switch (instruction.dir()) {
                case U:
                    row -= instruction.dist() + distanceModifier;
                    break;
                case D:
                    row += instruction.dist() + distanceModifier;
                    break;
                case L:
                    col -= instruction.dist() + distanceModifier;
                    break;
                case R:
                    col += instruction.dist() + distanceModifier;
                    break;
                default:
                    throw new RuntimeException();
            }
            currentPoint = new Position(row, col);
        }
        points.add(currentPoint);

        return points.toArray(new Position[points.size()]);
    }

    static int getDistanceModifier(Direction prevDirection, Direction currDirection, Direction nextDirection) {
        var prevCorner = isOutwardCorner(prevDirection, currDirection);
        var currCorner = isOutwardCorner(currDirection, nextDirection);
        // am i on outward corner, and going to another outward corner?
        if (prevCorner && currCorner) {
            return 1;
            // am i on outward corner, going into inward corner or vice versa?
        } else if (prevCorner != currCorner) {
            return 0;
            // am i on inward corner, going into another inward corner?
        } else {
            return -1;
        }

    }

    static boolean isOutwardCorner(Direction prevDirection, Direction currDirection) {
        switch (prevDirection) {
            case U:
                return currDirection.equals(Direction.R);
            case R:
                return currDirection.equals(Direction.D);
            case L:
                return currDirection.equals(Direction.U);
            case D:
                return currDirection.equals(Direction.L);
            default:
                throw new RuntimeException();
        }
    }

    static int ringArea(Position[] ring) {
        var area = 0;
        for (var i = 0; i < ring.length; i++) {
            var currentPoint = ring[i];
            var nextPoint = ring[(i + 1) % ring.length];
            area += cross(currentPoint, nextPoint);
        }
        return area / 2;
    }

    static int cross(Position a, Position b) {
        return a.col() * b.row() - b.col() * a.row();
    }

    enum Direction {
        U, D, L, R

    }

    record Instruction(Direction dir, int dist) {
        static Instruction of(String string) {
            var parts = string.split(" ");
            return new Instruction(Direction.valueOf(parts[0]), Integer.parseInt(parts[1]));

        }
    }

    record Position(int row, int col) {
    };
}