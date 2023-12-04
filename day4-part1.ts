import { readFileSync } from "fs";

const drawIdx = 8;
const pipeIdx = 40;
const file = readFileSync("./input/day4.txt", "utf-8");
const sum = file
  .trim()
  .split("\n")
  .map((line) => getCardScore(line))
  .reduce((prev, curr) => prev + curr, 0);
console.log({ sum });

function getCardScore(line: string): number {
  const winningNumbers = [
    ...line.slice(drawIdx + 1, pipeIdx).matchAll(/\d+/g),
  ].map((m) => parseInt(m[0]));

  let numMatches = 0;
  [...line.slice(pipeIdx + 1).matchAll(/\d+/g)]
    .map((m) => parseInt(m[0]))
    .forEach((draw) => {
      if (winningNumbers.includes(draw)) {
        numMatches++;
      }
    });

  if (numMatches === 0) {
    return 0;
  }
  return 2 ** (numMatches - 1);
}
