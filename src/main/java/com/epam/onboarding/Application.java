package com.epam.onboarding;

import com.epam.onboarding.tasks.first.XmlDataExtractor;
import com.epam.onboarding.tasks.second.XmlModifier;
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
    String inputFileName = "notice.xml";
    xmlDataExtractor.retrieveDataToJson(inputFileName);


    //task2
    String input = "document.xml";
    String output = "result.xml";
    XmlModifier xmlModifier = context.getBean(XmlModifier.class);
    xmlModifier.modifyXmlProcessInstructions(input, output);
  }

  @Bean
  public XPathFactory getXPathFactory() {
    return XPathFactory.newInstance();
  }
}
