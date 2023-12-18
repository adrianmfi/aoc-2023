import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Day16Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day16.txt";
        var board = Files.readString(Paths.get(filePath))
                .strip().split(System.lineSeparator());

        int maxEnergized = 0;
        for (int i = 0; i < board.length; i++) {
            var beam1 = new LightBeam(Direction.Right, new Position(i, 0));
            var beam2 = new LightBeam(Direction.Left, new Position(i, board[0].length() - 1));
            maxEnergized = Math.max(maxEnergized, getNumEnergized(board, beam1));
            maxEnergized = Math.max(maxEnergized, getNumEnergized(board, beam2));
        }

        for (int i = 0; i < board[0].length(); i++) {
            var beam1 = new LightBeam(Direction.Down, new Position(0, i));
            var beam2 = new LightBeam(Direction.Up, new Position(board.length - 1, i));
            maxEnergized = Math.max(maxEnergized, getNumEnergized(board, beam1));
            maxEnergized = Math.max(maxEnergized, getNumEnergized(board, beam2));
        }

        System.out.println(maxEnergized);
    }

    static int getNumEnergized(String[] board, LightBeam initialBeam) {
        var visited = new HashSet<LightBeam>();
        var splits = new ArrayDeque<LightBeam>();

        splits.add(initialBeam);

        while (!splits.isEmpty()) {
            var beam = splits.pop();
            while (beam != null && !visited.contains(beam)) {
                visited.add(beam);
                // System.out.println(beam);
                var next = getNextPositions(board, beam);
                // System.out.println(next);
                // System.out.println();

                if (next.size() == 0) {
                    beam = null;
                } else if (next.size() == 1) {
                    beam = next.get(0);
                } else {
                    beam = next.get(0);
                    splits.add(next.get(1));
                }

            }
        }

        return (int) visited
                .stream()
                .map(beam -> beam.position())
                .distinct()
                .count();
    }

    static List<LightBeam> getNextPositions(String[] board, LightBeam beam) {
        var result = new ArrayList<LightBeam>();

        var pos = beam.position();
        var tile = board[pos.row()].charAt(pos.column());
        switch (tile) {
            case '.':
                result.add(getNextEmptySpace(beam));
                break;
            case '/':
                result.add(getNextSplitterForwardSlash(beam));
                break;
            case '\\':
                result.add(getNextSplitterBackslash(beam));
                break;
            case '|':
                result.addAll(getNextSplitterVerticalSplitter(beam));
                break;
            case '-':
                result.addAll(getNextSplitterHorizontalSplitter(beam));
                break;
            default:
                throw new RuntimeException(String.format("Unexpected %s", tile));

        }
        var height = board.length;
        var width = board[0].length();
        return result.stream().filter(r -> isWithinBounds(width, height, r)).toList();
    }

    static boolean isWithinBounds(int width, int height, LightBeam beam) {
        return 0 <= beam.position().column() && beam.position().column() < width
                && 0 <= beam.position().row() && beam.position().row() < height;
    }

    static LightBeam getNextEmptySpace(LightBeam beam) {
        var row = beam.position().row();
        var col = beam.position().column();
        switch (beam.direction()) {
            case Up:
                row--;
                break;
            case Down:
                row++;
                break;
            case Left:
                col--;
                break;
            case Right:
                col++;
                break;
        }
        return new LightBeam(beam.direction(), new Position(row, col));
    }

    static LightBeam getNextSplitterForwardSlash(LightBeam beam) {
        var row = beam.position().row();
        var col = beam.position().column();
        var direction = beam.direction();
        switch (beam.direction()) {
            case Up:
                col++;
                direction = Direction.Right;
                break;
            case Down:
                col--;
                direction = Direction.Left;
                break;
            case Left:
                row++;
                direction = Direction.Down;
                break;
            case Right:
                row--;
                direction = Direction.Up;
                break;
        }
        return new LightBeam(direction, new Position(row, col));
    }

    static LightBeam getNextSplitterBackslash(LightBeam beam) {
        var row = beam.position().row();
        var col = beam.position().column();
        var direction = beam.direction();
        // \
        switch (beam.direction()) {
            case Up:
                col--;
                direction = Direction.Left;
                break;
            case Down:
                col++;
                direction = Direction.Right;
                break;
            case Left:
                row--;
                direction = Direction.Up;
                break;
            case Right:
                row++;
                direction = Direction.Down;
                break;
        }
        return new LightBeam(direction, new Position(row, col));
    }

    static List<LightBeam> getNextSplitterHorizontalSplitter(LightBeam beam) {
        var row = beam.position().row();
        var col = beam.position().column();
        // -
        switch (beam.direction()) {
            case Up:
                return List.of(
                        new LightBeam(Direction.Left, new Position(row, col - 1)),
                        new LightBeam(Direction.Right, new Position(row, col + 1)));
            case Down:
                return List.of(
                        new LightBeam(Direction.Left, new Position(row, col - 1)),
                        new LightBeam(Direction.Right, new Position(row, col + 1)));
            case Left:
                return List.of(
                        new LightBeam(Direction.Left, new Position(row, col - 1)));
            case Right:
                return List.of(
                        new LightBeam(Direction.Right, new Position(row, col + 1)));
        }
        throw new RuntimeException();
    }

    static List<LightBeam> getNextSplitterVerticalSplitter(LightBeam beam) {
        var row = beam.position().row();
        var col = beam.position().column();
        // |
        switch (beam.direction()) {
            case Up:
                return List.of(
                        new LightBeam(Direction.Up, new Position(row - 1, col)));
            case Down:
                return List.of(
                        new LightBeam(Direction.Down, new Position(row + 1, col)));
            case Left:
                return List.of(
                        new LightBeam(Direction.Up, new Position(row - 1, col)),
                        new LightBeam(Direction.Down, new Position(row + 1, col)));
            case Right:
                return List.of(
                        new LightBeam(Direction.Up, new Position(row - 1, col)),
                        new LightBeam(Direction.Down, new Position(row + 1, col)));
        }
        throw new RuntimeException();
    }

    enum Direction {
        Left, Right, Down, Up
    }

    record Position(int row, int column) {
    }

    record LightBeam(Direction direction, Position position) {
    }

}