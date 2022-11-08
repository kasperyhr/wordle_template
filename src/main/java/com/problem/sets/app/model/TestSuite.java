package com.problem.sets.app.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuite {
  private int secretWordLength;
  private int dictionarySize;
  private int randomSeed;
  private List<TestCase> testCases;

  public String getTestSuiteString() {
    return dictionarySize + "_" + secretWordLength + "_" + randomSeed;
  }

  public void sortTestCases() {
    testCases.sort(Comparator.comparing(TestCase::getSecretWord));
  }

  public int minGuessed() {
    return testCases.stream().mapToInt(testCase ->
        testCase.getGuessedAttempts().size()).min().orElse(0);
  }

  public int maxGuessed() {
    return testCases.stream().mapToInt(testCase ->
        testCase.getGuessedAttempts().size()).max().orElse(0);
  }

  public int totalGuessed() {
    return testCases.stream().mapToInt(testCase ->
        testCase.getGuessedAttempts().size()).sum();
  }

  public double avgGuessed() {
    return totalGuessed() / (double) dictionarySize;
  }

  public List<String> worstWords() {
    int maxGuessed = maxGuessed();
    return testCases.stream()
        .filter(testCase -> testCase.getGuessedAttempts().size() == maxGuessed)
        .map(testCase -> testCase.getSecretWord())
        .collect(Collectors.toList());
  }
}
