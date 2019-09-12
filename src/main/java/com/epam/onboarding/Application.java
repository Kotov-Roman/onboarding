package com.epam.onboarding;

import com.epam.onboarding.tasks.first.XmlDataExtractor;
import com.epam.onboarding.tasks.second.XmlModifier;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Exception {

    ApplicationContext context = SpringApplication.run(Application.class, args);
    XmlDataExtractor xmlDataExtractor = context.getBean(XmlDataExtractor.class);

    //task1
    String inputFilePath = "src/main/resources/templates/notice.xml";
    String outputFilePath = "tmp/result.json";
    xmlDataExtractor.retrieveDataToJsonFile(inputFilePath, outputFilePath);

    //task2
    String input = "src/main/resources/templates/document.xml";
    String output = "tmp/modifiedDocument.xml";
    XmlModifier xmlModifier = context.getBean(XmlModifier.class);
    xmlModifier.modifyXmlProcessInstructions(input, output);
  }

  @Bean
  public XPathFactory getXPathFactory() {
    return XPathFactory.newInstance();
  }

  @Bean
  public DocumentBuilderFactory getDocumentBuilderFactory() {
    return DocumentBuilderFactory.newInstance();
  }
}
