import { readFileSync } from "fs";
import IntervalTree from "@flatten-js/interval-tree";

const file = readFileSync("./input/day3.txt", "utf-8").trim();

const sum = getPartSum(file);
console.log({ sum });

function getPartSum(file: string): number {
  const width = file.indexOf("\n") + 1;
  const numbers = new IntervalTree<number>();
  for (const numberMatch of file.matchAll(/[0-9]+/g)) {
    const start = numberMatch.index!;
    const end = start + numberMatch[0].length - 1;
    numbers.insert([start, end], parseInt(numberMatch[0]));
  }
  let sum = 0;
  for (const starMatch of file.matchAll(/\*/g)) {
    let firstNumber: number | null = null;
    let secondNumber: number | null = null;
    let tooManyMatches = false;

    for (let matchAbove of numbers.search([
      starMatch.index! - width - 1,
      starMatch.index! - width + 1,
    ])) {
      if (firstNumber === null) {
        firstNumber = matchAbove;
      } else if (secondNumber === null) {
        secondNumber = matchAbove;
      } else {
        tooManyMatches = true;
      }
    }
    if (!tooManyMatches) {
      for (let matchSameLine of numbers.search([
        starMatch.index! - 1,
        starMatch.index! + 1,
      ])) {
        if (firstNumber === null) {
          firstNumber = matchSameLine;
        } else if (secondNumber === null) {
          secondNumber = matchSameLine;
        } else {
          tooManyMatches = true;
        }
      }
    }
    if (!tooManyMatches) {
      for (let matchBelow of numbers.search([
        starMatch.index! + width - 1,
        starMatch.index! + width + 1,
      ])) {
        if (firstNumber === null) {
          firstNumber = matchBelow;
        } else if (secondNumber === null) {
          secondNumber = matchBelow;
        } else {
          tooManyMatches = true;
        }
      }
    }
    if (!tooManyMatches && firstNumber && secondNumber) {
      sum += firstNumber * secondNumber;
    }
  }

  return sum;
}
