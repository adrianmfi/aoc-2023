import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Day10Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day10.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        Position startPos = null;
        findS: for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[0].length(); j++) {
                if (lines[i].charAt(j) == 'S') {
                    startPos = new Position(i, j);
                    break findS;
                }
            }
        }

        if (startPos == null) {
            throw new RuntimeException("No start pos found");
        }

        var prevPos = new Position(startPos.getRow(), startPos.getCol());
        // Just by inspecting the input, we see that the position to the right leads
        // into S
        var currentPos = new Position(startPos.getRow(), startPos.getCol() + 1);
        var loopVerticalPositions = new HashMap<Integer, TreeSet<Integer>>();
        var loopPositions = new HashSet<Position>();
        while (!currentPos.equals(startPos)) {
            var currentChar = lines[currentPos.getRow()].charAt(currentPos.getCol());
            if (currentChar == '|' || currentChar == 'L' || currentChar == 'J') {
                loopVerticalPositions.merge(
                        currentPos.getRow(),
                        new TreeSet<Integer>(Set.of(currentPos.getCol())),
                        (existingSet, newSet) -> {
                            // Create a new set to avoid modifying the original sets
                            var mergedSet = new TreeSet<>(existingSet);
                            mergedSet.addAll(newSet);
                            return mergedSet;
                        });
            }
            loopPositions.add(new Position(currentPos.getRow(), currentPos.getCol()));
            updatePosition(prevPos, currentPos, currentChar);
        }

        int count = 0;
        // Check if point in polygon by "raytracing"
        for (int i = 0; i < lines.length; i++) {
            if (!loopVerticalPositions.containsKey(i)) {
                continue;
            }
            var verticalPositionsForRow = loopVerticalPositions.get(i);
            List<Integer> indices = new ArrayList<>();
            for (int j = 0; j < lines[0].length(); j++) {
                if (!loopPositions.contains(new Position(i, j))) {
                    var numLarger = verticalPositionsForRow.tailSet(j).size();
                    if (numLarger % 2 != 0) {
                        // inside
                        count++;
                        indices.add(j);
                    }
                }

            }
        }
        System.out.println(count);

    }

    static void updatePosition(Position prevPos, Position currentPos, char pipe) {
        switch (pipe) {
            case '-': {
                if (prevPos.getCol() < currentPos.getCol()) {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setCol(currentPos.getCol() + 1);
                } else {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setCol(currentPos.getCol() - 1);
                }
                break;
            }
            case '7': {
                if (prevPos.getCol() < currentPos.getCol()) {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setRow(currentPos.getRow() + 1);
                } else {
                    prevPos.setRow(currentPos.getRow());
                    currentPos.setCol(currentPos.getCol() - 1);
                }
                break;
            }
            case '|': {
                if (prevPos.getRow() < currentPos.getRow()) {
                    prevPos.setRow(prevPos.getRow());
                    currentPos.setRow(currentPos.getRow() + 1);
                } else {
                    prevPos.setRow(currentPos.getRow());
                    currentPos.setRow(currentPos.getRow() - 1);

                }
                break;
            }
            case 'J': {
                if (prevPos.getCol() < currentPos.getCol()) {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setRow(currentPos.getRow() - 1);
                } else {
                    prevPos.setRow(currentPos.getRow());
                    currentPos.setCol(currentPos.getCol() - 1);

                }
                break;
            }
            case 'F': {
                if (prevPos.getRow() > currentPos.getRow()) {
                    prevPos.setRow(currentPos.getRow());
                    currentPos.setCol(currentPos.getCol() + 1);
                } else {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setRow(currentPos.getRow() + 1);

                }
                break;
            }
            case 'L': {
                if (prevPos.getRow() < currentPos.getRow()) {
                    prevPos.setRow(currentPos.getRow());
                    currentPos.setCol(currentPos.getCol() + 1);
                } else {
                    prevPos.setCol(currentPos.getCol());
                    currentPos.setRow(currentPos.getRow() - 1);
                }
                break;
            }

            default: {
                // System.out.println(pipe == '7');
                throw new RuntimeException("Unexpected symbol " + pipe + " at " + currentPos);
            }
        }
    }
}

class Position {
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + row;
        result = prime * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        if (row != other.row)
            return false;
        if (col != other.col)
            return false;
        return true;
    }

}