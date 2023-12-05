import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Day5Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day5.txt";
        var content = Files.readString(Paths.get(filePath)).strip();

        var seedsMatcher = Pattern.compile("\\d+")
                .matcher(content.substring(0, content.indexOf(System.lineSeparator())));

        var seeds = new HashSet<Long>();
        while (seedsMatcher.find()) {
            var seed = seedsMatcher.group();
            seeds.add(Long.parseLong(seed));
        }

        var rangePattern = Pattern
                .compile("(?<destStart>\\d+) (?<getSourceStart>\\d+) (?<length>\\d+)$", Pattern.MULTILINE);

        var mapNames = List.of("seed-to-soil", "soil-to-fertilizer", "fertilizer-to-water", "water-to-light",
                "light-to-temperature", "temperature-to-humidity", "humidity-to-location");

        int mapStartIndex = 0;
        int mapEndIndex = content.indexOf(mapNames.get(0));
        var mapped = List.copyOf(seeds);

        for (int i = 0; i < mapNames.size(); i++) {
            mapStartIndex = mapEndIndex;
            mapEndIndex = i == mapNames.size() - 1 ? content.length()
                    : content.indexOf(mapNames.get(i + 1), mapStartIndex);
            var rangeMap = new RangeMap();
            var rangeMatcher = rangePattern.matcher(content.substring(mapStartIndex, mapEndIndex));
            while (rangeMatcher.find()) {
                var destStart = Long.parseLong(rangeMatcher.group("destStart"));
                var getSourceStart = Long.parseLong(rangeMatcher.group("getSourceStart"));
                var length = Long.parseLong(rangeMatcher.group("length"));
                rangeMap.addRange(new Range(destStart, getSourceStart, length));
            }

            mapped = mapped.stream().map(v -> rangeMap.getDestination(v)).toList();
        }

        System.out.println("Result " + mapped.stream().mapToLong(v -> v).min().orElseThrow());

    }

}

class RangeMap {
    private final TreeMap<Long, Range> map = new TreeMap<>();

    public void addRange(Range range) {
        map.put(range.getSourceStart(), range);
        map.put(range.getSourceStart() + range.getRangeLength() - 1, range);
    }

    public long getDestination(long source) {
        var rangeStart = map.floorEntry(source);
        var rangeEnd = map.ceilingEntry(source);

        if (rangeEnd == null || rangeStart == null) {
            return source;
        }
        if (rangeStart.getValue() == rangeEnd.getValue()) {
            return rangeStart.getValue().getDestinationStart() + (source - rangeStart.getValue().getSourceStart());
        }

        return source;
    }
}

class Range {

    private final long destinationStart;
    private final long getSourceStart;
    private final long getRangeLength;

    public Range(long destinationStart, long getSourceStart, long getRangeLength) {
        this.destinationStart = destinationStart;
        this.getSourceStart = getSourceStart;
        this.getRangeLength = getRangeLength;
    }

    public long getSourceStart() {
        return getSourceStart;
    }

    public long getRangeLength() {
        return getRangeLength;
    }

    public long getDestinationStart() {
        return destinationStart;
    }
};