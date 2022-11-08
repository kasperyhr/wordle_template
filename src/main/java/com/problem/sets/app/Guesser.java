package com.problem.sets.app;

import java.util.List;
import com.problem.sets.app.service.GuessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Guesser {
  public void findSecretWord(List<String> dictionary, GuessService guessService) {
    throw new NotImplementedException("This method is not implemented");
  }
}
