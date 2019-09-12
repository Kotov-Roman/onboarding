package com.epam.onboarding.tasks.second;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

import com.epam.onboarding.ApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmlunit.builder.Input;

public class XmlModifierTest extends ApplicationTests {

  @Autowired
  private XmlModifier xmlModifier;

  private static final String TEMPLATE_FILE_PATH = "src/test/resources/templates/document.xml";
  private static final String RESULT_FILE_PATH = "src/test/resources/results/modifiedDocument.xml";
  private static final String EXPECTED_FILE_PATH = "src/test/resources/check/expectedDocument.xml";

  @Test
  public void xmlModifierTest() {

    xmlModifier.modifyXmlProcessInstructions(TEMPLATE_FILE_PATH, RESULT_FILE_PATH);

    assertThat(Input.fromFile(RESULT_FILE_PATH), isSimilarTo(Input.fromFile(EXPECTED_FILE_PATH)));
  }
}