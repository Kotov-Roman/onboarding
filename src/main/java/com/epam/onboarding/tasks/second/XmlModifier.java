package com.epam.onboarding.tasks.second;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class XmlModifier {

  public void modifyXmlProcessInstructions(String inputFilePath, String outputFilePath) {
    File inputFile = new File(inputFilePath);
    File outputFile = createFile(outputFilePath);
    modifyXmlProcessInstructions(inputFile, outputFile);
  }

  @SneakyThrows
  private void modifyXmlProcessInstructions(File inputFile, File outputFile) {

    try (FileReader fileReader = new FileReader(inputFile);
        FileWriter fileWriter = new FileWriter(outputFile)) {

      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(fileReader);

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      XMLEventWriter xmlEventWriter = outputFactory.createXMLEventWriter(fileWriter);

      writeModifiedData(xmlEventReader, xmlEventWriter);
    }
  }

  @SneakyThrows
  private void writeModifiedData(XMLEventReader xmlEventReader, XMLEventWriter xmlEventWriter) {
    while (xmlEventReader.hasNext()) {
      XMLEvent event = xmlEventReader.nextEvent();

      switch (event.getEventType()) {

        case XMLStreamConstants.PROCESSING_INSTRUCTION:
          ProcessingInstruction instruction = (ProcessingInstruction) event;
          String instructionTarget = instruction.getTarget();
          if (instructionTarget.equalsIgnoreCase("CLG.MDFO")) {
            continue;
          }
      }
      xmlEventWriter.add(event);
    }
    xmlEventWriter.flush();
  }

  private File createFile(String filePath) {
    File file = new File(filePath);

    try {
      if (file.createNewFile()) {
        System.out.println(filePath + " File Created in Project root directory");
      }
      else {
        System.out.println("File " + filePath + " already exists in the project root directory");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return file;
  }
}
