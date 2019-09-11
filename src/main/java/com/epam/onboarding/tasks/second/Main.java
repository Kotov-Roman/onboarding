package com.epam.onboarding.tasks.second;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;

public class Main {

  public static void main(String[] args) throws IOException {

    String inputFilePath = "document.xml";

    try {

      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      XMLEventReader xmlEventReader = inputFactory.createXMLEventReader(new FileReader(inputFilePath));

      XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
      FileWriter fileWriter = new FileWriter("result.xml");
      XMLEventWriter xmlEventWriter = outputFactory.createXMLEventWriter(fileWriter);

      while (xmlEventReader.hasNext()) {
        XMLEvent event = xmlEventReader.nextEvent();

        switch (event.getEventType()) {

          case XMLStreamConstants.PROCESSING_INSTRUCTION:
            ProcessingInstruction instruction = (ProcessingInstruction) event;
            String target = instruction.getTarget();
            if (target.equalsIgnoreCase("CLG.MDFO")) {
              continue;
            }
        }

        xmlEventWriter.add(event);
      }

      xmlEventWriter.flush();
      xmlEventWriter.close();

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
  }
}
