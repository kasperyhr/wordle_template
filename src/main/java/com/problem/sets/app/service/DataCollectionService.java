package com.problem.sets.app.service;

import com.problem.sets.app.model.TestSuite;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataCollectionService {
  private final GuessService guessService;
  @Getter
  private List<TestSuite> testSuiteList;
  private TestSuite testSuite;

  @Autowired
  public DataCollectionService(GuessService guessService) {
    this.guessService = guessService;
    testSuiteList = new ArrayList<>();
  }

  public void initTestSuit(
      int secretWordLength, int dictionarySize, int randomSeed) {
    testSuite = TestSuite.builder()
        .secretWordLength(secretWordLength)
        .dictionarySize(dictionarySize)
        .randomSeed(randomSeed)
        .testCases(new ArrayList<>())
        .build();
  }

  public void addTestCase() {
    testSuite.getTestCases().add(guessService.getTestCase());
  }

  public void testSuiteEnd() {
    testSuite.sortTestCases();
    testSuiteList.add(testSuite);
  }

  public void sortTestSuite() {
    testSuiteList.sort(Comparator.comparing(TestSuite::getTestSuiteString));
  }
}
