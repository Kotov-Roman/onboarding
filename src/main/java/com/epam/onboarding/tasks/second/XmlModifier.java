package com.epam.onboarding.tasks.second;

import java.io.FileReader;
import java.io.FileWriter;
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

  @SneakyThrows
  public void modifyXmlProcessInstructions(String inputFilePath, String outputFilePath) {

    try (FileReader fileReader = new FileReader(inputFilePath);
        FileWriter fileWriter = new FileWriter(outputFilePath)) {

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
}
