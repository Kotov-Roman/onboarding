package com.epam.onboarding.tasks.first;

import com.epam.onboarding.ApplicationTests;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class XmlDataExtractorTest extends ApplicationTests {

  @Autowired
  private XmlDataExtractor xmlDataExtractor;

  private static final String TEMPLATE_FILE_PATH = "src/test/resources/templates/notice.xml";
  private static final String RESULT_FILE_PATH = "src/test/resources/results/result.json";
  private static final String EXPECTED_FILE_PATH = "src/test/resources/check/expectedResult.json";

  @Test
  public void xmlModifierTest() {

    xmlDataExtractor.retrieveDataToJsonFile(TEMPLATE_FILE_PATH, RESULT_FILE_PATH);

    String expectedString = readFile(EXPECTED_FILE_PATH);
    String actualString = readFile(EXPECTED_FILE_PATH);

    Gson gson = new Gson();
    DataContainer expectedData = gson.fromJson(expectedString, DataContainer.class);
    DataContainer actualData = gson.fromJson(actualString, DataContainer.class);

    Assertions.assertThat(actualData).isEqualTo(expectedData);
  }

  private static String readFile(String filePath) {
    StringBuilder result = new StringBuilder();

    try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
      stream.forEach(result::append);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result.toString();
  }
}
