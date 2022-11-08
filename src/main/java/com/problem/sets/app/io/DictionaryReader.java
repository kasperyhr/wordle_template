package com.problem.sets.app.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class DictionaryReader {
  public InputStream getDictionaryInputStream(String dictionaryPath)
      throws FileNotFoundException {
    Path path = Paths.get(dictionaryPath);
    File file = path.toFile();
    return new FileInputStream(file);
  }
}
