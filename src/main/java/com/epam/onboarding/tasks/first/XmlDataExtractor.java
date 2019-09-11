package com.epam.onboarding.tasks.first;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Component
public class XmlDataExtractor {

  private static final String CREATION_DATE_EXPRESSION = "NOTICE/WORK/CREATIONDATE/VALUE";
  private static final String MODIFICATION_DATE_EXPRESSION = "NOTICE/WORK/LASTMODIFICATIONDATE/VALUE";
  private static final String IDENTIFIER_EXPRESSION = "NOTICE/WORK/WORK_HAS_EXPRESSION/EMBEDDED_NOTICE/EXPRESSION/EXPRESSION_USES_LANGUAGE/IDENTIFIER";

  public void retrieveDataToJson(String inputFilePath) throws Exception {

    File inputFile = new File(inputFilePath);
    DataContainer dataContainer = retrieveDataFromFile(inputFile);
    saveDataAsJson(dataContainer);
  }

  private DataContainer retrieveDataFromFile(File inputFile) throws Exception {

    String creationDate = retrieveCreationDate(inputFile);
    String modificationDate = retrieveModificationDate(inputFile);
    String title = retrieveTitle(inputFile);

    DataContainer dataContainer = new DataContainer();
    dataContainer.setCreationDate(creationDate);
    dataContainer.setExpressionTitle(title);
    dataContainer.setModificationDate(modificationDate);

    System.out.println(dataContainer.toString());

    return dataContainer;
  }

  @SuppressWarnings("Duplicates")
  private String retrieveCreationDate(File file) throws Exception {

    Document doc = getDocument(file);
    XPath xPath = XPathFactory.newInstance().newXPath();

    return (String) xPath.compile(CREATION_DATE_EXPRESSION).evaluate(doc, XPathConstants.STRING);
  }

  @SuppressWarnings("Duplicates")
  private String retrieveModificationDate(File file) throws Exception {

    Document doc = getDocument(file);
    XPath xPath = XPathFactory.newInstance().newXPath();

    return (String) xPath.compile(MODIFICATION_DATE_EXPRESSION).evaluate(doc, XPathConstants.STRING);
  }

  /**
   * I have found 2 ways how to do it
   */
  private String retrieveTitle(File file) throws Exception {

    Document doc = getDocument(file);
    XPath xPath = XPathFactory.newInstance().newXPath();
    String title = null;

//    String expression = "NOTICE/WORK/WORK_HAS_EXPRESSION";
//    NodeList nodes = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
//    for (int i = 0; i < nodes.getLength(); i++) {
//      Node item = nodes.item(i);
//      String langIdentifierExpression = "EMBEDDED_NOTICE/EXPRESSION/EXPRESSION_USES_LANGUAGE/IDENTIFIER";
//      String identifier = (String) xPath.compile(langIdentifierExpression).evaluate(item, XPathConstants.STRING);
//
//      if (identifier.equalsIgnoreCase("eng")) {
//        String titleExpression = "EMBEDDED_NOTICE/EXPRESSION/EXPRESSION_TITLE";
//        title = (String) xPath.compile(titleExpression).evaluate(item, XPathConstants.STRING);
//        System.out.println(title.trim());
//      }
//    }

    NodeList identifierNodes = (NodeList) xPath.compile(IDENTIFIER_EXPRESSION).evaluate(doc, XPathConstants.NODESET);

    for (int i = 0; i < identifierNodes.getLength(); i++) {

      Node item = identifierNodes.item(i);
      String identifier = item.getTextContent();

      if (identifier.equalsIgnoreCase("eng")) {

        //MAYBE EXISTS BETTER WAY RO GET SECOND PARENT?
        item = item.getParentNode().getParentNode();
        String titleExpression = "EXPRESSION_TITLE/VALUE";
        title = (String) xPath.compile(titleExpression).evaluate(item, XPathConstants.STRING);
      }
    }

    if (StringUtils.isEmpty(title)) {
      throw new IllegalStateException("some issues during title retrieving: TITLE IS EMPTY");
    }

    return title.trim();
  }

  private Document getDocument(File file) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(file);
    doc.getDocumentElement().normalize();
    return doc;
  }

  private void saveDataAsJson(DataContainer dataContainer) throws FileNotFoundException {

    File jsonFile = createJsonFile();
    Gson gson = new Gson();
    PrintWriter printWriter = new PrintWriter(jsonFile);
    gson.toJson(dataContainer, printWriter);
    printWriter.flush();
    printWriter.close();
  }

  private File createJsonFile() {

    String fileSeparator = System.getProperty("file.separator");
    String relativePath = "tmp" + fileSeparator + "result.json";
    File file = new File(relativePath);

    try {
      if (file.createNewFile()) {
        System.out.println(relativePath + " File Created in Project root directory");
      }
      else {
        System.out.println("File " + relativePath + " already exists in the project root directory");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return file;
  }
}
