package com.problem.sets.app.io;

import com.problem.sets.app.constant.Constant;
import com.problem.sets.app.model.TestCase;
import com.problem.sets.app.model.TestSuite;
import com.problem.sets.app.model.TestSuiteIntegration;
import com.problem.sets.app.service.DataCollectionService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpreadsheetGenerator {
  private DataCollectionService dataCollectionService;
  private String filePath;

  @Autowired
  public SpreadsheetGenerator(
      DataCollectionService dataCollectionService,
      @Value("${report.path}") String filePath) {
    this.dataCollectionService = dataCollectionService;
    this.filePath = filePath;
  }

  public void writeToSpreadsheet() {
    log.info("Writing to spreadsheet");

    Workbook wb = new XSSFWorkbook();
    List<TestSuite> testSuiteList = dataCollectionService.getTestSuiteList();
    writeReport(wb.createSheet("report"), testSuiteList);
    testSuiteList.forEach(testSuite -> generateSheetForTestSuite(wb, testSuite));

    try {
      OutputStream stream
          = new FileOutputStream(filePath);
      wb.write(stream);
    } catch (IOException exception) {
      log.error("Cannot create report");
    }
  }

  private void generateSheetForTestSuite(Workbook wb, TestSuite testSuite) {
    Sheet sheet = wb.createSheet(testSuite.getTestSuiteString());

    generateRowForTestCaseHeader(sheet.createRow(0));

    IntStream.range(0, testSuite.getDictionarySize())
        .mapToObj(rowIndex -> sheet.createRow(rowIndex + 1))
        .forEach(row ->
            generateRowForTestCase(row,
                testSuite.getTestCases().get(row.getRowNum() - 1)));
  }

  private void generateRowForTestCaseHeader(Row row) {
    row.createCell(0).setCellValue("Secret Word");
    row.createCell(1).setCellValue("Guessed Attempts");
  }

  private void generateRowForTestCase(Row row, TestCase testCase) {
    row.createCell(0).setCellValue(testCase.getSecretWord());
    row.createCell(1).setCellValue(testCase.getGuessedAttempts().toString());
  }

  private void writeReport(Sheet sheet, List<TestSuite> testSuiteList) {
    writeReportHeader(sheet.createRow(0));

    IntStream.range(0, testSuiteList.size())
        .mapToObj(rowIndex -> sheet.createRow(rowIndex + 1))
        .forEach(row ->
            writeReportBody(row, testSuiteList.get(row.getRowNum() - 1)));
  }

  private void writeReportHeader(Row row) {
    row.createCell(0).setCellValue(Constant.TEST_SUITE_NAME);
    row.createCell(1).setCellValue(Constant.SECRET_WORD_LENGTH);
    row.createCell(2).setCellValue(Constant.DICTIONARY_SIZE);
    row.createCell(3).setCellValue(Constant.RANDOM_SEED);
    row.createCell(4).setCellValue(Constant.MINIMUM_NUMBER_GUESSED);
    row.createCell(5).setCellValue(Constant.MAXIMUM_NUMBER_GUESSED);
    row.createCell(6).setCellValue(Constant.TOTAL_NUMBER_GUESSED);
    row.createCell(7).setCellValue(Constant.AVERAGE_NUMBER_GUESSED);
    row.createCell(8).setCellValue(Constant.WORST_GUESSED_WORDS);
  }

  private void writeReportBody(Row row, TestSuite testSuite) {
    TestSuiteIntegration integration =
        generateTestSuiteIntegrationFromTestSuite(testSuite);
    row.createCell(0).setCellValue(integration.getTestSuiteString());
    row.createCell(1).setCellValue(integration.getSecretWordLength());
    row.createCell(2).setCellValue(integration.getDictionarySize());
    row.createCell(3).setCellValue(integration.getRandomSeed());
    row.createCell(4).setCellValue(integration.getMinGuessed());
    row.createCell(5).setCellValue(integration.getMaxGuessed());
    row.createCell(6).setCellValue(integration.getTotalGuessed());
    row.createCell(7).setCellValue(integration.getAvgGuessed());
    row.createCell(8).setCellValue(integration.getWorstWords().toString());
  }

  private TestSuiteIntegration generateTestSuiteIntegrationFromTestSuite(
      TestSuite testSuite) {
    return TestSuiteIntegration.builder()
        .testSuiteString(testSuite.getTestSuiteString())
        .secretWordLength(testSuite.getSecretWordLength())
        .dictionarySize(testSuite.getDictionarySize())
        .randomSeed(testSuite.getRandomSeed())
        .minGuessed(testSuite.minGuessed())
        .maxGuessed(testSuite.maxGuessed())
        .totalGuessed(testSuite.totalGuessed())
        .avgGuessed(testSuite.avgGuessed())
        .worstWords(testSuite.worstWords())
        .build();
  }
}
