package com.epam.onboarding.tasks.first;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class XmlDataExtractor {

  private static final String CREATION_DATE_EXPRESSION = "NOTICE/WORK/CREATIONDATE/VALUE";
  private static final String MODIFICATION_DATE_EXPRESSION = "NOTICE/WORK/LASTMODIFICATIONDATE/VALUE";
  private static final String IDENTIFIER_EXPRESSION =
      "/NOTICE/WORK/WORK_HAS_EXPRESSION/EMBEDDED_NOTICE/EXPRESSION//EXPRESSION_TITLE[starts-with(../EXPRESSION_USES_LANGUAGE/IDENTIFIER, 'ENG' )]";

  private final XPathFactory xPathFactory;
  private final DocumentBuilderFactory documentBuilderFactory;

  @Autowired
  public XmlDataExtractor(XPathFactory xPathFactory, DocumentBuilderFactory documentBuilderFactory) {
    this.xPathFactory = xPathFactory;
    this.documentBuilderFactory = documentBuilderFactory;
  }

  public void retrieveDataToJsonFile(String inputFilePath, String outputFilePath) {
    File jsonFile = createFile(outputFilePath);
    File inputFile = new File(inputFilePath);
    retrieveDataToJsonFile(inputFile, jsonFile);
  }

  public void retrieveDataToJsonFile(File inputFile, File outputFile) {
//    Document document = getDocument(inputFile);
    DataContainer dataContainer = retrieveDataFromFile(inputFile);
    saveDataAsJson(dataContainer, outputFile);
  }

  @SneakyThrows
  DataContainer retrieveDataFromFile(File inputFile) {

    Document document = getDocument(inputFile);
    String creationDate = retrieveCreationDate(document);
    String modificationDate = retrieveModificationDate(document);
    String expressionTitle = retrieveTitle(document);

    DataContainer dataContainer = DataContainer.builder()
        .creationDate(creationDate)
        .modificationDate(modificationDate)
        .expressionTitle(expressionTitle)
        .build();

    System.out.println(dataContainer.toString());

    return dataContainer;
  }

  @SneakyThrows
  String retrieveCreationDate(Document doc) {
    XPath xPath = xPathFactory.newXPath();
    return (String) xPath.compile(CREATION_DATE_EXPRESSION).evaluate(doc, XPathConstants.STRING);
  }

  @SneakyThrows
  String retrieveModificationDate(Document doc) {
    XPath xPath = xPathFactory.newXPath();
    return (String) xPath.compile(MODIFICATION_DATE_EXPRESSION).evaluate(doc, XPathConstants.STRING);
  }

  /**
   * I have found 2 ways how to do it
   */
  @SneakyThrows
  String retrieveTitle(Document doc) {
    XPath xPath = xPathFactory.newXPath();
    XPathExpression xPathExpression = xPath.compile(IDENTIFIER_EXPRESSION);
    NodeList identifierNodes = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
    String title = null;

    if (identifierNodes.getLength() != 0) {
      Node item = identifierNodes.item(0);
      title = item.getTextContent();
    }

    if (StringUtils.isEmpty(title)) {
      throw new IllegalStateException("some issues during title retrieving: TITLE IS EMPTY");
    }

    return title.trim();
  }

  @SneakyThrows
  Document getDocument(File file) {
    DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(file);
    doc.getDocumentElement().normalize();
    return doc;
  }

  @SneakyThrows
  void saveDataAsJson(DataContainer dataContainer, File outputFile) {
//    File jsonFile = createFile(outputFile);
    Gson gson = new Gson();

    try (PrintWriter printWriter = new PrintWriter(outputFile)) {
      gson.toJson(dataContainer, printWriter);
      printWriter.flush();
    }
  }

  File createFile(String filePath) {
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
