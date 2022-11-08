package com.problem.sets.app.service.impl;

import com.problem.sets.app.service.ComparisonService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class ComparisonServiceImpl implements ComparisonService {
  @Override
  public String compare(String secretWord, String guessWord) {
    if (!validateStringLength(secretWord, guessWord)) {
      return "";
    }

    int length = secretWord.length();

    int[] resultArray = new int[length];
    Arrays.fill(resultArray, -1);

    setExactMatched(secretWord, guessWord, resultArray);
    setPartiallyMatched(secretWord, guessWord, resultArray);
    setMismatched(resultArray);

    return IntStream.of(resultArray)
        .mapToObj(Integer::toString).collect(Collectors.joining());
  }

  @Override
  public boolean isFullyMatched(String result) {
    return result.matches("1+");
  }

  private void setExactMatched(
      String secretWord, String guessWord, int[] resultArray) {
    int length = secretWord.length();

    IntStream.range(0, length)
        .filter(index -> secretWord.charAt(index)
            == guessWord.charAt(index))
        .forEach(index -> resultArray[index] = 1);
  }

  private void setPartiallyMatched(
      String secretWord, String guessWord, int[] resultArray) {
    Map<Character, Integer> partiallyMatchedCount = new HashMap<>();

    setPartiallyMatchedCount(
        secretWord, resultArray, partiallyMatchedCount);

    setResultArrayByPartiallyMatchedCount(
        guessWord, resultArray, partiallyMatchedCount);
  }

  private void setPartiallyMatchedCount(
      String secretWord,
      int[] resultArray,
      Map<Character, Integer> partiallyMatchedCount) {
    int length = secretWord.length();

    IntStream.range(0, length)
        .filter(index -> currentIndexMatched(resultArray, index))
        .map(secretWord::charAt)
        .forEach(ch ->
            partiallyMatchedCount.put(
                (char) ch,
                partiallyMatchedCount
                    .getOrDefault((char) ch, 0) + 1));
  }

  private void setResultArrayByPartiallyMatchedCount(
      String guessWord,
      int[] resultArray,
      Map<Character, Integer> partiallyMatchedCount) {
    int length = guessWord.length();

    IntStream.range(0, length)
        .filter(index -> currentIndexMatched(resultArray, index))
        .mapToObj(index -> Map.entry(index, guessWord.charAt(index)))
        .filter(entry ->
            partiallyMatchedCount.containsKey(entry.getValue()))
        .filter(entry ->
            partiallyMatchedCount.get(entry.getValue()) > 0)
        .forEach(entry -> {
          resultArray[entry.getKey()] = 2;
          partiallyMatchedCount.put(entry.getValue(),
              partiallyMatchedCount.get(entry.getValue()) - 1);
        });
  }

  private void setMismatched(int[] resultArray) {
    IntStream.range(0, resultArray.length)
        .filter(index -> -1 == resultArray[index])
        .forEach(index -> resultArray[index] = 0);
  }

  private boolean currentIndexMatched(int[] resultArray, int i) {
    return resultArray[i] != 1;
  }

  private boolean validateStringLength(String secretWord, String guessWord) {
    return secretWord.length() == guessWord.length();
  }
}
