import { readFileSync } from "fs";

function reverseString(str: string) {
  return str.split("").reverse().join("");
}

const firstNumberString = "one|two|three|four|five|six|seven|eight|nine";
const lastNumberString = reverseString(firstNumberString);

const firstRegex = new RegExp(firstNumberString + "|[0-9]");
const lastRegex = new RegExp(lastNumberString + "|[0-9]");

const matchToNumericChar: Record<string, string> = {
  "0": "0",
  "1": "1",
  "2": "2",
  "3": "3",
  "4": "4",
  "5": "5",
  "6": "6",
  "7": "7",
  "8": "8",
  "9": "9",
  one: "1",
  two: "2",
  three: "3",
  four: "4",
  five: "5",
  six: "6",
  seven: "7",
  eight: "8",
  nine: "9",
};

function getNumber(line: string): number {
  const firstMatch = line.match(firstRegex)![0];
  const lastMatch = reverseString(line).match(lastRegex)![0];

  return Number.parseInt(
    matchToNumericChar[firstMatch] +
      matchToNumericChar[reverseString(lastMatch)],
    10
  );
}

const file = readFileSync("./input/day1.txt", "utf-8");

const sum = file
  .trim()
  .split("\n")
  .map((line) => getNumber(line))
  .reduce((prev, curr) => prev + curr, 0);
console.log({ sum });
