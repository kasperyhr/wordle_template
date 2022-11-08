package com.problem.sets.app.service;

import com.problem.sets.app.exception.DictionaryException;
import com.problem.sets.app.model.TestCase;
import com.problem.sets.app.service.impl.ComparisonServiceImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GuessService {
  private final ComparisonService comparisonService;

  private List<String> dictionary;
  private Set<String> dictionarySet;
  @Getter(AccessLevel.PACKAGE)
  private TestCase testCase;

  @Autowired
  public GuessService(ComparisonServiceImpl comparisonService) {
    this.comparisonService = comparisonService;
  }

  public void setDictionary(List<String> dictionary) {
    this.dictionary = dictionary;
    dictionarySet = new HashSet<>(dictionary);
  }

  public void setSecretWord(int index) {
    if (dictionary == null) {
      throw new DictionaryException("Dictionary not defined.");
    }

    if (index < 0 || index >= dictionary.size()) {
      throw new IllegalArgumentException(
          "Invalid index for dictionary: " + index);
    }

    testCase = TestCase.builder()
        .secretWord(dictionary.get(index))
        .found(false)
        .guessedAttempts(new ArrayList<>())
        .build();
  }

  public String guess(String guessWord) {
    if (!testCase.isFound()) {
      testCase.getGuessedAttempts().add(guessWord);
    }

    if (!dictionarySet.contains(guessWord)) {
      return "";
    }

    String result = comparisonService.compare(
        testCase.getSecretWord(), guessWord);
    if (testCase.getSecretWord().equals(guessWord)) {
      testCase.setFound(true);
    }

    log.debug("Secret word: " + testCase.getSecretWord() + ". " +
        " Guess word: " + guessWord + ". Result: " + result);

    return result;
  }
}
