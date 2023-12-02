import { readFileSync } from "fs";

const limits = {
  red: 12,
  green: 13,
  blue: 14,
};

const file = readFileSync("./input/day2.txt", "utf-8");

const count = file
  .trim()
  .split("\n")
  .map(getGameValue)
  .reduce((prev, curr) => prev + curr, 0);

console.log({ sum: count });

function getGameValue(line: string): number {
  const [gameInfo, game] = line.split(":");
  const isValidGame = isValidDraw(game);
  console.log({ line, isValidGame, id: parseInt(gameInfo.slice(5)) });
  if (isValidGame) {
    // gameInfo = "Game [game No]"
    return parseInt(gameInfo.slice(5));
  }
  return 0;
}

function isValidDraw(draw: string): boolean {
  return [
    ...draw.matchAll(/(?<number>[0-9]+) (?<color>red|green|blue)/g),
  ].every(
    (match) =>
      parseInt(match.groups!["number"]) <=
      limits[match.groups!["color"] as "red" | "green" | "blue"]
  );
}
