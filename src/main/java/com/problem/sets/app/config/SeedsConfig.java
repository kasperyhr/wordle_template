package com.problem.sets.app.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedsConfig {
  @Bean
  public Map<Integer, List<Integer>> seedsByLength(
      @Value("#{'${dictionary.seeds}'.split(';')}") String[] seedsStrings) {
    return Arrays.stream(seedsStrings)
        .collect(Collectors.toMap(
            this::getLength,
            this::getSeeds
        ));
  }

  private int getLength(String str) {
    return Integer.parseInt(str.split("#")[0]);
  }

  private List<Integer> getSeeds(String str) {
    return Arrays
        .stream(str.split("#")[1].split(","))
        .map(Integer::parseInt)
        .collect(Collectors.toList());
  }
}
