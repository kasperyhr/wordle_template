package com.problem.sets.app.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuiteIntegration {
  String testSuiteString;
  int secretWordLength;
  int dictionarySize;
  int randomSeed;
  int minGuessed;
  int maxGuessed;
  int totalGuessed;
  double avgGuessed;
  List<String> worstWords;
}
