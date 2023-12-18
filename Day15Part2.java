import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day15Part2 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day15.txt";
        var sequences = Files.readString(Paths.get(filePath))
                .strip().split(",");

        int res = 0;

        @SuppressWarnings("unchecked")
        List<Lens>[] buckets = new List[256];

        for (var str : sequences) {
            var lens = Lens.of(str);
            handleLens(buckets, lens);
        }

        for (int i = 0; i < buckets.length; i++) {
            var bucket = buckets[i];
            if (bucket == null) {
                continue;
            }
            for (int j = 0; j < bucket.size(); j++) {
                res += (1 + i) * (1 + j) * bucket.get(j).focalLength();
            }
        }

        System.out.println(String.format("Result is %s", res));
    }

    static void handleLens(List<Lens>[] buckets, Lens newLens) {
        var bucketIndex = customHash(newLens.label());
        var bucket = buckets[bucketIndex];
        if (newLens.operation() == '=') {
            if (bucket == null) {
                buckets[bucketIndex] = new ArrayList<>();
                buckets[bucketIndex].add(newLens);
            } else {
                for (int i = 0; i < bucket.size(); i++) {
                    if (bucket.get(i).label.equals(newLens.label)) {
                        bucket.set(i, newLens);
                        return;
                    }
                }
                bucket.add(newLens);
            }
        } else {
            if (bucket == null) {
                return;
            }
            for (int i = 0; i < bucket.size(); i++) {
                if (bucket.get(i).label().equals(newLens.label())) {
                    bucket.remove(i);
                    break;
                }
            }
        }
    }

    static int customHash(String s) {
        var res = 0;
        for (var character : s.toCharArray()) {
            res += character;
            res *= 17;
            res %= 256;
        }
        return res;
    }

    record Lens(
            String label,
            char operation,
            int focalLength) {

        static Lens of(String str) {
            var operationIndex = str.indexOf('=');
            if (operationIndex == -1) {
                operationIndex = str.indexOf('-');
            }
            var operation = str.charAt(operationIndex);
            var label = str.substring(0, operationIndex);
            int focalLength = 0;
            if (operation == '=') {
                focalLength = Integer.parseInt(str.substring(operationIndex + 1));
            }
            return new Lens(label, operation, focalLength);
        }
    };

}