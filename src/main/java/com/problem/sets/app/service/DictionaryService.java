package com.problem.sets.app.service;

import com.problem.sets.app.exception.DictionaryException;
import com.problem.sets.app.io.DictionaryReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DictionaryService {
  private DictionaryReader reader;
  private List<String> wordDict;
  private String resourceText;

  @Autowired
  public DictionaryService(
      DictionaryReader reader,
      @Value("${service.dictionary.resourceText}") String resourceText) {
    this.reader = reader;
    this.resourceText = resourceText;
  }

  public List<String> getWordDictWithLength(int length) {
    if (wordDict == null) {
      loadDictionary();
    }

    return wordDict.stream()
        .filter(word -> word.length() == length)
        .collect(Collectors.toList());
  }

  private void loadDictionary() {
    InputStream inputStream = null;

    try {
      inputStream = reader.getDictionaryInputStream(resourceText);
      loadWordDictFromInputStream(inputStream);
    } catch (IOException exception) {
      throw new DictionaryException("Error while loading dictionary.");
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void loadWordDictFromInputStream(InputStream inputStream)
      throws IOException {
    List<String> dict = new ArrayList<>();
    try (BufferedReader br =
             new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        dict.add(line);
      }
    }

    wordDict = dict.stream()
        .filter(this::isAllLettersLowerCase)
        .collect(Collectors.toList());
  }

  private boolean isAllLettersLowerCase(String word) {
    return word.matches("[a-z]+");
  }
}
