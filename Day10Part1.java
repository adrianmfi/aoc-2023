import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day10Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day10.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        Position startPos = null;
        findS: for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                if (lines[i].charAt(j) == 'S') {
                    startPos = new Position(i, j);
                    break findS;
                }
            }
        }

        if (startPos == null) {
            throw new RuntimeException("No pos found");
        }

        // Just by inspecting the input, we see that the position to the right leads
        // into S
        var prevPos = new Position(startPos.getRow(), startPos.getCol());
        var currentPos = new Position(startPos.getRow(), startPos.getCol() + 1);
        int numSteps = 0;
        while (!currentPos.equals(startPos)) {
            updatePosition(prevPos, currentPos, lines[currentPos.getRow()].charAt(currentPos.getCol()));
            numSteps++;
        }
        System.out.println(Math.round(numSteps / 2.0));
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
        return "Position [row=" + row + ", col=" + col + "]";
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