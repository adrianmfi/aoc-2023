import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day11.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var galaxies = new ArrayList<Position>();
        var hasRowsGalaxy = new boolean[lines.length];
        var hasColsGalaxy = new boolean[lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[0].length(); j++) {
                if (lines[i].charAt(j) == '#') {
                    galaxies.add(new Position(i, j));
                    hasRowsGalaxy[i] = true;
                    hasColsGalaxy[j] = true;
                }
            }
        }
        var rowsWithoutGalaxies = IntStream.range(0, hasRowsGalaxy.length).filter(i -> !hasRowsGalaxy[i])
                .mapToObj(i -> i)
                .collect((Collectors.toCollection(TreeSet::new)));
        var colsWithoutGalaxies = IntStream.range(0, hasColsGalaxy.length).filter(i -> !hasColsGalaxy[i])
                .mapToObj(i -> i)
                .collect((Collectors.toCollection(TreeSet::new)));

        long sum = 0;
        for (int i = 0; i < galaxies.size() - 1; i++) {
            var gal1 = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                var gal2 = galaxies.get(j);
                sum += getDistanceBetween(gal1, gal2, rowsWithoutGalaxies, colsWithoutGalaxies);
            }

        }
        System.out.println(String.format("Result %d", sum));

    }

    private static long getDistanceBetween(
            Position gal1, Position gal2, TreeSet<Integer> rowsWithoutGalaxies, TreeSet<Integer> colsWithoutGalaxies) {
        var minRow = Math.min(gal1.row(), gal2.row());
        var maxRow = Math.max(gal1.row(), gal2.row());

        var minCol = Math.min(gal1.col(), gal2.col());
        var maxCol = Math.max(gal1.col(), gal2.col());

        var rowsBetween = maxRow - minRow;
        var colsBetween = maxCol - minCol;
        var rowExpansionBetween = (long) (rowsWithoutGalaxies.subSet(minRow, maxRow).size()) * 999_999L;
        var colExpansionBetween = (long) (colsWithoutGalaxies.subSet(minCol, maxCol).size()) * 999_999L;
        return rowsBetween + colsBetween + rowExpansionBetween
                + colExpansionBetween;
    }

    record Position(int row, int col) {
    }

}