import { readFileSync } from "fs";

const file = readFileSync("./input/day2.txt", "utf-8");

const count = file
  .trim()
  .split("\n")
  .map(getGameValue)
  .reduce((prev, curr) => prev + curr, 0);

console.log({ sum: count });

function getGameValue(line: string): number {
  const [_, game] = line.split(":");
  const max = getMax(game);
  return max.blue * max.green * max.red;
}

function getMax(draw: string) {
  const max: Record<string, number> = {
    red: 0,
    green: 0,
    blue: 0,
  };
  [...draw.matchAll(/(?<number>[0-9]+) (?<color>red|green|blue)/g)].forEach(
    (match) => {
      const { number, color } = match.groups!;
      const numberInt = parseInt(number);
      if (numberInt > max[color]) {
        max[color] = numberInt;
      }
    }
  );
  return max;
}
