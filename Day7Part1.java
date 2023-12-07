import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Day7Part1 {
    public static void main(String[] args) throws IOException {
        var filePath = "./input/day7.txt";

        var lines = Files.readString(Paths.get(filePath))
                .strip()
                .split(System.lineSeparator());

        var hands = Arrays.stream(lines).map(line -> {
            var bid = Integer.parseInt(line.substring(6));
            var handString = line.substring(0, 5);
            var hand = classifyHand(handString);

            return new HandWithBid(hand, bid);
        }).collect(Collectors.toCollection(ArrayList::new));
        hands.sort(compareHands());

        int result = 0;
        for (int i = 0; i < hands.size(); i++) {
            result += (i + 1) * hands.get(i).bid();
        }
        // System.out.println(hands);
        System.out.println(result);

    }

    enum HandType {
        HIGH(0), PAIR(1), TWO_PAIR(2), THREE_EQ(3), HOUSE(4),
        FOUR_EQ(5), FIVE_EQ(6);

        private final int value;

        // Constructor to set the integer value
        HandType(int value) {
            this.value = value;
        }

        // Getter for the integer value
        public int getValue() {
            return value;
        }
    }

    record HandWithBid(Hand hand, int bid) {
    }

    record Hand(HandType type, String handString) {

    }

    static Hand classifyHand(String handString) {
        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < handString.length(); i++) {
            var c = handString.substring(i, i + 1);
            counts.merge(c, 1, (a, b) -> a + b);
        }
        var values = counts.values();
        if (values.contains(5)) {
            return new Hand(HandType.FIVE_EQ, handString);
        } else if (values.contains(4)) {
            return new Hand(HandType.FOUR_EQ, handString);
        } else if (values.contains(3)) {
            if (values.contains(2)) {
                return new Hand(HandType.HOUSE, handString);
            }
            return new Hand(HandType.THREE_EQ, handString);
        } else if (values.stream().filter(value -> value == 2).count() == 2) {
            return new Hand(HandType.TWO_PAIR, handString);
        } else if (values.contains(2)) {
            return new Hand(HandType.PAIR, handString);
        } else {
            return new Hand(HandType.HIGH, handString);
        }

    }

    static final Map<String, Integer> cardValueMap = new HashMap<>();
    static {
        cardValueMap.putAll(Map.of("A", 14, "K", 13, "Q", 12, "J", 11, "T", 10));
        for (int i = 2; i < 10; i++) {
            cardValueMap.put(Integer.toString(i), i);
        }
    }

    static Comparator<HandWithBid> compareHands() {
        return Comparator.comparingInt((HandWithBid hwb) -> hwb.hand().type().getValue())
                .thenComparing((HandWithBid a, HandWithBid b) -> {
                    var strA = a.hand.handString();
                    var strB = b.hand.handString();
                    for (int i = 0; i < 5; i++) {
                        var valA = cardValueMap.get(strA.substring(i, i + 1));
                        var valB = cardValueMap.get(strB.substring(i, i + 1));
                        if (valA < valB) {
                            return -1;
                        } else if (valB < valA) {
                            return 1;
                        }
                    }
                    return 0;
                });
    }
}
