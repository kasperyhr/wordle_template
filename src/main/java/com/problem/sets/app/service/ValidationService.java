package com.problem.sets.app.service;

import com.problem.sets.app.exception.ValidationException;
import com.problem.sets.app.model.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationService {
  public void validate(GuessService guessService) {
    TestCase testCase = guessService.getTestCase();
    log.debug("Test case: " + testCase);
    if (testCase.isFound()) {
      log.debug("Secret word " + testCase.getSecretWord() + " found.");
    } else {
      throw new ValidationException(
          "Secret word " + testCase.getSecretWord() + " not found.");
    }
  }
}
