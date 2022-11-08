package com.problem.sets.app.driver;

import com.problem.sets.app.Guesser;
import com.problem.sets.app.exception.DictionaryException;
import com.problem.sets.app.io.SpreadsheetGenerator;
import com.problem.sets.app.service.DataCollectionService;
import com.problem.sets.app.service.DictionaryService;
import com.problem.sets.app.service.GuessService;
import com.problem.sets.app.service.ProgressBarService;
import com.problem.sets.app.service.ValidationService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WordleDriver {
  private final DictionaryService dictionaryService;
  private final GuessService guessService;
  private final ProgressBarService progressBarService;
  private final ValidationService validationService;
  private final DataCollectionService dataCollectionService;
  private final SpreadsheetGenerator spreadsheetGenerator;
  private final Guesser guesser;
  private final int dictionaryMaxSize;
  private final int defaultWordLength;
  private final Map<Integer, List<Integer>> seedsByLength;

  private List<String> dictionary;
  private Random random;
  private int progressBarIndex;

  @Autowired
  public WordleDriver(
      DictionaryService dictionaryService,
      GuessService guessService,
      ProgressBarService progressBarService,
      ValidationService validationService,
      DataCollectionService dataCollectionService,
      SpreadsheetGenerator spreadsheetGenerator,
      Guesser guesser,
      @Value("${driver.dictionary.max.size}") int dictionaryMaxSize,
      @Value("${driver.default.word.length}") int defaultWordLength,
      @Autowired Map<Integer, List<Integer>> seedsByLength) {
    this.dictionaryService = dictionaryService;
    this.guessService = guessService;
    this.progressBarService = progressBarService;
    this.validationService = validationService;
    this.dataCollectionService = dataCollectionService;
    this.spreadsheetGenerator = spreadsheetGenerator;
    this.guesser = guesser;
    this.dictionaryMaxSize = dictionaryMaxSize;
    this.defaultWordLength = defaultWordLength;
    this.seedsByLength = seedsByLength;
  }

  @PostConstruct
  public void init() {
    // Run for a single word.
    // setUp();
    // runWithRandomWord();

    // Run for all words with given seeds.
    runForAllSeeds();
  }

  public void setUp() {
    setUpWithRandomSeedAndWordLength(
        Instant.now().toEpochMilli(), defaultWordLength);
  }

  private void setUpWithRandomSeedAndWordLength(
      long seed, int wordLength) {
    random = new Random(seed);

    List<String> masterDictionary
        = dictionaryService.getWordDictWithLength(wordLength);

    if (masterDictionary.size() < dictionaryMaxSize) {
      throw new DictionaryException("Not enough dictionary words");
    } else {
      dictionary = new ArrayList<>();
      IntStream.range(0, dictionaryMaxSize)
          .forEach(_index -> {
            int randomIndex = random.nextInt(masterDictionary.size());
            dictionary.add(masterDictionary.get(randomIndex));
            masterDictionary.remove(randomIndex);
          });
    }
  }

  public void runWithRandomWord() {
    int index = random.nextInt(dictionaryMaxSize);

    guessService.setDictionary(dictionary);
    setFindAndValidate(index);
  }

  private void runWithAllWords() {
    int progressBarMax = totalWordCount();

    guessService.setDictionary(dictionary);
    List<Integer> indexArray =
        IntStream.range(0, dictionaryMaxSize).boxed()
            .collect(Collectors.toList());

    Collections.shuffle(indexArray);

    IntStream.range(0, dictionaryMaxSize)
        .forEach(index -> {
          progressBarService
              .printProgressBar(progressBarIndex++, progressBarMax);
          setFindAndValidate(indexArray.get(index));
          dataCollectionService.addTestCase();
        });

    dataCollectionService.testSuiteEnd();
    progressBarService
        .printProgressBar(progressBarIndex, progressBarMax);
  }

  public void runForAllSeeds() {
    progressBarIndex = 0;
    seedsByLength.forEach((key, value) -> value
        .forEach(seed -> {
          dataCollectionService.initTestSuit(
              key, dictionaryMaxSize, seed);
          setUpWithRandomSeedAndWordLength(seed, key);
          runWithAllWords();
        }));

    progressBarService.printProgressBarEnd();
    dataCollectionService.sortTestSuite();
    spreadsheetGenerator.writeToSpreadsheet();
  }

  private void setFindAndValidate(int index) {
    guessService.setSecretWord(index);
    guesser.findSecretWord(dictionary, guessService);
    validationService.validate(guessService);
  }

  private int totalWordCount() {
    return seedsByLength.values().stream()
        .mapToInt(seeds -> seeds.size() * dictionaryMaxSize)
        .sum();
  }
}
