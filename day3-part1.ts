import { readFileSync } from "fs";

const file = readFileSync("./input/day3.txt", "utf-8").trim();

const sum = getPartSum(file);
console.log({ sum });

function getPartSum(file: string): number {
  const lines = file.split("\n");
  const symbolPositions = new Map<number, Set<number>>();
  for (let i = 0; i < lines.length; i++) {
    let line = lines[i];
    for (let j = 0; j < line.length; j++) {
      let char = line.at(j)!;
      if (char !== "." && !isDigit(char)) {
        if (symbolPositions.has(i)) {
          symbolPositions.get(i)!.add(j);
        } else {
          symbolPositions.set(i, new Set([j]));
        }
      }
    }
  }
  let sum = 0;
  for (let i = 0; i < lines.length; i++) {
    let line = lines[i];
    for (let numberMatch of line.matchAll(/[0-9]+/g)) {
      numberLoop: for (
        let rowToCheck = i - 1;
        rowToCheck <= i + 1;
        rowToCheck++
      ) {
        for (
          let colToCheck = numberMatch.index! - 1;
          colToCheck <= numberMatch.index! + numberMatch[0].length;
          colToCheck++
        ) {
          if (symbolPositions.get(rowToCheck)?.has(colToCheck)) {
            sum += parseInt(numberMatch[0]);
            break numberLoop;
          }
        }
      }
    }
  }
  return sum;
}

function isDigit(char: string) {
  return "0" <= char && char <= "9";
}
