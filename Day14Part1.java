import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day14Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day14.txt";
        var board = Files.readString(Paths.get(filePath))
                .strip().split(System.lineSeparator());

        int res = 0;
        for (int i = 0; i < board[0].length(); i++) {
            var freeSlot = 0;
            for (int j = 0; j < board.length; j++) {
                var currentChar = board[j].charAt(i);
                if (currentChar == 'O') {
                    // "move" the O to the free slot and increment the free slot
                    res += board.length - freeSlot;
                    freeSlot++;
                } else if (currentChar == '#') {
                    freeSlot = j + 1;
                }
            }
        }
        System.out.println(String.format("Result is %s", res));
    }

}