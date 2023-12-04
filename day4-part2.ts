import { readFileSync } from "fs";

const drawIdx = 8;
const pipeIdx = 40;
const file = readFileSync("./input/day4.txt", "utf-8");
const matches = file
  .trim()
  .split("\n")
  .map((line) => getNumMatches(line));
const copies = new Map<number, number>();
let totalCopies = 0;
matches.forEach((match, i) => {
  const numCopies = 1 + (copies.get(i) ?? 0);
  totalCopies += numCopies;
  for (let j = i + 1; j <= i + match; j++) {
    if (copies.has(j)) {
      copies.set(j, copies.get(j)! + numCopies);
    } else {
      copies.set(j, numCopies);
    }
  }
});
console.log({ totalCopies });
function getNumMatches(line: string): number {
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

  return numMatches;
}
