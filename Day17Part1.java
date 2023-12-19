import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Day17Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day17.txt";
        var boardString = Files.readString(Paths.get(filePath))
                .strip().split(System.lineSeparator());
        var board = new int[boardString.length][];
        var distances = new int[boardString.length][];
        for (int i = 0; i < board.length; i++) {
            var row = boardString[i];
            board[i] = new int[row.length()];
            distances[i] = new int[row.length()];
            for (int j = 0; j < row.length(); j++) {
                board[i][j] = Integer.parseInt(row.substring(j, j + 1));
            }
        }

        var visited = new boolean[boardString.length][][][]; // visited by row, col, num steps, dir
        for (int i = 0; i < visited.length; i++) {
            visited[i] = new boolean[boardString[0].length()][][];
            for (int j = 0; j < boardString[0].length(); j++) {
                visited[i][j] = new boolean[3][];
                for (int k = 0; k < 3; k++) {
                    visited[i][j][k] = new boolean[4];
                }
            }
        }

        var queue = new PriorityQueue<PositionWithPriorityAndDirection>(
                Comparator.comparingInt(PositionWithPriorityAndDirection::distanceFromStart));
        var startPosition = new Position(0, 0);
        queue.add(new PositionWithPriorityAndDirection(startPosition, 0, Direction.Right, 0));
        while (!queue.isEmpty()) {

            var currentPositionWithPriority = queue.poll();
            var nextPositions = getNextPositions(board, currentPositionWithPriority);

            for (var nextPosition : nextPositions) {
                var distanceToNext = distances[nextPosition.p().row()][nextPosition.p().col()];
                var isVisited = visited[nextPosition.p().row()][nextPosition.p().col()][nextPosition
                        .numInDirection() - 1][nextPosition.direction().ordinal()];
                if (!isVisited) {
                    queue.add(nextPosition);
                    visited[nextPosition.p().row()][nextPosition.p().col()][nextPosition
                            .numInDirection() - 1][nextPosition.direction().ordinal()] = true;
                }
                if (distanceToNext == 0 || nextPosition.distanceFromStart() < distanceToNext) {
                    distances[nextPosition.p().row()][nextPosition.p().col()] = nextPosition
                            .distanceFromStart();
                }
            }

        }

        System.out.println(distances[distances.length - 1][distances[0].length - 1]);

    }

    static List<PositionWithPriorityAndDirection> getNextPositions(
            int[][] board,
            PositionWithPriorityAndDirection current) {
        return Arrays.stream(Direction.values()).filter(d -> {
            if (current.direction().equals(d) && current.numInDirection() == 3) {
                return false;
            }
            if (current.direction().equals(oppositeDirection(d))) {
                return false;
            }
            return true;
        }).map(d -> {
            var nextPos = stepInDirection(d, current.p());
            if (!isWithinBounds(nextPos, board.length, board[0].length)) {
                return null;
            }
            var nextPosPriority = current.distanceFromStart + board[nextPos.row()][nextPos.col()];
            var numInDirection = current.direction().equals(d) ? current.numInDirection() + 1 : 1;
            return new PositionWithPriorityAndDirection(nextPos, nextPosPriority, d, numInDirection);
        }).filter(p -> p != null).toList();
    }

    static boolean isWithinBounds(Position p, int width, int height) {
        return 0 <= p.row() && p.row() < height && 0 <= p.col() && p.col() < width;
    }

    static Position stepInDirection(Direction d, Position p) {
        switch (d) {
            case Right:
                return new Position(p.row(), p.col() + 1);
            case Left:
                return new Position(p.row(), p.col() - 1);
            case Up:
                return new Position(p.row() - 1, p.col());
            case Down:
                return new Position(p.row() + 1, p.col());
            default:
                throw new RuntimeException();
        }
    }

    static Direction oppositeDirection(Direction d) {
        switch (d) {
            case Right:
                return Direction.Left;
            case Left:
                return Direction.Right;
            case Up:
                return Direction.Down;
            case Down:
                return Direction.Up;
            default:
                throw new RuntimeException();
        }
    }

    enum Direction {
        Left, Right, Down, Up
    }

    record PositionWithPriorityAndDirection(Position p, int distanceFromStart, Direction direction,
            int numInDirection) {
    };

    record Position(int row, int col) {
    }

}