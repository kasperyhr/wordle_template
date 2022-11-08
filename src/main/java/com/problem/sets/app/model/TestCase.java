package com.problem.sets.app.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCase {
  private String secretWord;
  private boolean found;
  private List<String> guessedAttempts;
}
