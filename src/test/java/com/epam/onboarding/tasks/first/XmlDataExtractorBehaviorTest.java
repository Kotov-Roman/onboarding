package com.epam.onboarding.tasks.first;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlDataExtractorBehaviorTest {

  @Spy
  @InjectMocks
  private XmlDataExtractor xmlDataExtractor;

  @Mock
  private XPathFactory xPathFactory;

  @Mock
  private DocumentBuilderFactory documentBuilderFactory;

  private static final String CREATION_DATE_EXPRESSION = "NOTICE/WORK/CREATIONDATE/VALUE";
  private static final String MODIFICATION_DATE_EXPRESSION = "NOTICE/WORK/LASTMODIFICATIONDATE/VALUE";
  private static final String IDENTIFIER_EXPRESSION =
      "/NOTICE/WORK/WORK_HAS_EXPRESSION/EMBEDDED_NOTICE/EXPRESSION//EXPRESSION_TITLE[starts-with(../EXPRESSION_USES_LANGUAGE/IDENTIFIER, 'ENG' )]";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void retrieveDataToJsonFile() {
    DataContainer dataContainer = mock(DataContainer.class);
    File outputFile = mock(File.class);
    File inputFile = mock(File.class);

    doReturn(dataContainer).when(xmlDataExtractor).retrieveDataFromFile(inputFile);
    doNothing().when(xmlDataExtractor).saveDataAsJson(dataContainer, outputFile);

    xmlDataExtractor.retrieveDataToJsonFile(inputFile, outputFile);

    verify(xmlDataExtractor).retrieveDataFromFile(inputFile);
    verify(xmlDataExtractor).saveDataAsJson(dataContainer, outputFile);
  }

  @Test
  public void retrieveDataFromFile() {
    File inputFile = mock(File.class);
    Document document = mock(Document.class);
    String creationDate = "1001-01-01T00:00:00.000+00:00";
    String modificationDate = "2018-06-01T20:10:58.641+02:00";
    String expressionTitle = "title";

    doReturn(document).when(xmlDataExtractor).getDocument(inputFile);
    doReturn(creationDate).when(xmlDataExtractor).retrieveCreationDate(document);
    doReturn(modificationDate).when(xmlDataExtractor).retrieveModificationDate(document);
    doReturn(expressionTitle).when(xmlDataExtractor).retrieveTitle(document);

    DataContainer actualDataContainer = xmlDataExtractor.retrieveDataFromFile(inputFile);

    DataContainer expectedContainer = DataContainer.builder()
        .creationDate(creationDate)
        .modificationDate(modificationDate)
        .expressionTitle(expressionTitle)
        .build();

    assertThat(actualDataContainer).isEqualTo(expectedContainer);

    verify(xmlDataExtractor).retrieveCreationDate(document);
    verify(xmlDataExtractor).retrieveModificationDate(document);
    verify(xmlDataExtractor).retrieveTitle(document);
  }

  @Test
  @SneakyThrows
  public void retrieveTitle_Exception() {
    Document doc = mock(Document.class);
    XPath xPath = mock(XPath.class);
    XPathExpression xPathExpression = mock(XPathExpression.class);
    NodeList nodeList = mock(NodeList.class);

    doReturn(xPath).when(xPathFactory).newXPath();
    doReturn(xPathExpression).when(xPath).compile(IDENTIFIER_EXPRESSION);
    doReturn(nodeList).when(xPathExpression).evaluate(doc, XPathConstants.NODESET);

    doReturn(0).when(nodeList).getLength();

    Throwable throwable = catchThrowable(() -> xmlDataExtractor.retrieveTitle(doc));

    assertThat(throwable).isInstanceOf(IllegalStateException.class);

    verify(nodeList, never()).item(0);
  }

  @Test
  @SneakyThrows
  public void retrieveTitle_Success() {
    Document doc = mock(Document.class);
    XPath xPath = mock(XPath.class);
    XPathExpression xPathExpression = mock(XPathExpression.class);
    NodeList nodeList = mock(NodeList.class);
    Node item = mock(Node.class);
    String title = "title";

    doReturn(xPath).when(xPathFactory).newXPath();
    doReturn(xPathExpression).when(xPath).compile(IDENTIFIER_EXPRESSION);
    doReturn(nodeList).when(xPathExpression).evaluate(doc, XPathConstants.NODESET);
    doReturn(1).when(nodeList).getLength();
    doReturn(item).when(nodeList).item(0);
    doReturn(title).when(item).getTextContent();

    String actualTitle = xmlDataExtractor.retrieveTitle(doc);

    assertThat(actualTitle).isEqualTo(title);
  }

  @Test
  @SneakyThrows
  public void getDocument() {
    File file = mock(File.class);
    Document doc = mock(Document.class);
    DocumentBuilder documentBuilder = mock(DocumentBuilder.class);
    Element element = mock(Element.class);

    doReturn(documentBuilder).when(documentBuilderFactory).newDocumentBuilder();
    doReturn(doc).when(documentBuilder).parse(file);
    doReturn(element).when(doc).getDocumentElement();
    doNothing().when(element).normalize();

    xmlDataExtractor.getDocument(file);
  }
}