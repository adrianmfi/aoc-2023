import { readFileSync } from "fs";

function isDigit(char: string) {
  return "0" <= char && char <= "9";
}

function getNumber(line: string): number {
  let numberAsString = "";
  for (let i = 0; i < line.length; i++) {
    const char = line.at(i)!;
    if (isDigit(char)) {
      numberAsString += char;
      break;
    }
  }

  for (let i = line.length - 1; i >= 0; i--) {
    const char = line.at(i)!;
    if (isDigit(char)) {
      numberAsString += char;
      break;
    }
  }

  console.log(numberAsString);
  return Number.parseInt(numberAsString, 10);
}

const file = readFileSync("./input/day1.txt", "utf-8");
const sum = file
  .trim()
  .split("\n")
  .map((line) => getNumber(line))
  .reduce((prev, curr) => prev + curr, 0);
console.log(sum);
