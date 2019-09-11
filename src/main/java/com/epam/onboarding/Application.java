package com.epam.onboarding;

import com.epam.onboarding.tasks.first.XmlDataExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

  public static void main(String[] args) throws Exception {

    ApplicationContext context = SpringApplication.run(Application.class, args);

    XmlDataExtractor xmlDataExtractor = context.getBean(XmlDataExtractor.class);

    String inputFileName = "notice.xml";
    xmlDataExtractor.retrieveDataToJson(inputFileName);
  }
}
