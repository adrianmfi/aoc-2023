import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Day14Part2 {
    enum BoardPiece {
        Empty, Cube, Rounded
    }

    public static void main(String[] args) throws IOException {
        var filePath = "./input/day14.txt";
        var boardString = Files.readString(Paths.get(filePath))
                .strip().split(System.lineSeparator());

        var board = buildBoard(boardString);
        var boards = new ArrayList<Board>();
        var repetitionStart = 0;
        var repetitionEnd = 0;
        var remainingRotations = 1000000000;
        while (remainingRotations > 0) {
            tiltNorth(board);
            tiltWest(board);
            tiltSouth(board);
            tiltEast(board);

            if (repetitionEnd == 0) {
                var boardCopy = new Board(copyBoard(board));
                if (boards.contains(boardCopy)) {
                    repetitionStart = boards.indexOf(boardCopy);
                    repetitionEnd = boards.size();
                    remainingRotations = remainingRotations % (repetitionEnd - repetitionStart);
                }
                boards.add(boardCopy);
            }
            remainingRotations--;
        }

        System.out.println(String.format("Result is %s", getBoardValue(board)));
    }

    static void tiltNorth(BoardPiece[][] board) {
        for (int i = 0; i < board[0].length; i++) {
            var freeSlot = 0;
            for (int j = 0; j < board.length; j++) {
                switch (board[j][i]) {
                    case Rounded:
                        swap(board, j, i, freeSlot, i);
                        freeSlot++;
                        break;
                    case Cube:
                        freeSlot = j + 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    static void tiltWest(BoardPiece[][] board) {
        for (int i = 0; i < board.length; i++) {
            var freeSlot = 0;
            for (int j = 0; j < board[0].length; j++) {
                switch (board[i][j]) {
                    case Rounded:
                        swap(board, i, j, i, freeSlot);
                        freeSlot++;
                        break;
                    case Cube:
                        freeSlot = j + 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    static void tiltSouth(BoardPiece[][] board) {
        for (int i = 0; i < board[0].length; i++) {
            var freeSlot = board.length - 1;
            for (int j = board.length - 1; j >= 0; j--) {
                switch (board[j][i]) {
                    case Rounded:
                        swap(board, j, i, freeSlot, i);
                        freeSlot--;
                        break;
                    case Cube:
                        freeSlot = j - 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    static BoardPiece[][] buildBoard(String[] boardStrings) {
        var board = new BoardPiece[boardStrings.length][];
        for (int i = 0; i < boardStrings.length; i++) {
            board[i] = new BoardPiece[boardStrings[i].length()];
            for (int j = 0; j < boardStrings[i].length(); j++) {
                switch (boardStrings[i].charAt(j)) {
                    case '#':
                        board[i][j] = BoardPiece.Cube;
                        break;
                    case 'O':
                        board[i][j] = BoardPiece.Rounded;
                        break;
                    default:
                        board[i][j] = BoardPiece.Empty;
                        break;
                }
            }
        }
        return board;
    }

    static void tiltEast(BoardPiece[][] board) {
        for (int i = 0; i < board.length; i++) {
            var freeSlot = board[0].length - 1;
            for (int j = board[0].length - 1; j >= 0; j--) {
                switch (board[i][j]) {
                    case Rounded:
                        swap(board, i, j, i, freeSlot);
                        freeSlot--;
                        break;
                    case Cube:
                        freeSlot = j - 1;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    static int getBoardValue(BoardPiece[][] board) {
        int res = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(BoardPiece.Rounded)) {
                    res += board.length - i;
                }
            }

        }
        return res;
    }

    static void swap(BoardPiece[][] board, int row1, int col1, int row2, int col2) {
        var tmp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = tmp;
    }

    static public boolean arePiecesEqual(BoardPiece[][] bp1, BoardPiece[][] bp2) {
        for (int i = 0; i < bp1.length; i++) {
            for (int j = 0; j < bp1[i].length; j++) {
                if (bp1[i][j] != bp2[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static BoardPiece[][] copyBoard(BoardPiece[][] originalBoard) {
        if (originalBoard == null) {
            return null;
        }

        BoardPiece[][] copiedBoard = new BoardPiece[originalBoard.length][];
        for (int i = 0; i < originalBoard.length; i++) {
            copiedBoard[i] = new BoardPiece[originalBoard[i].length];
            System.arraycopy(originalBoard[i], 0, copiedBoard[i], 0, originalBoard[i].length);
        }

        return copiedBoard;
    }

    public record Board(BoardPiece[][] pieces) {

        @Override
        public int hashCode() {
            if (pieces == null) {
                return 0;
            }

            final int prime = 31;
            int result = 1;

            for (BoardPiece[] row : pieces) {
                result = prime * result + Arrays.hashCode(row);
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Board)) {
                return false;
            }
            return arePiecesEqual(this.pieces, ((Board) o).pieces);
        }

    }

}