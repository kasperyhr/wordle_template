package com.problem.sets.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProgressBarService {
  private int progressBarSize;

  @Autowired
  public ProgressBarService(@Value("${service.progressBar.size}") int progressBarSize) {
    this.progressBarSize = progressBarSize;
  }

  public void printProgressBar(int curr, int size) {
    int currentLength = (curr * progressBarSize) / size;
    String bar = "<" +
        "=".repeat(Math.max(0, currentLength)) +
        " ".repeat(Math.max(0, progressBarSize - currentLength)) +
        ">" +
        " (" + curr + "/" + size + ")\r";
    System.out.print(bar);
  }

  public void printProgressBarEnd() {
    log.info("Done.");
  }
}
