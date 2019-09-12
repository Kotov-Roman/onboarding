package com.epam.onboarding.tasks.first;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import javax.xml.xpath.XPathFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.w3c.dom.Document;

public class XmlDataExtractorTest {

  @Mock
  private XPathFactory xPathFactory;

  @Spy
  @InjectMocks
  private XmlDataExtractor xmlDataExtractor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void retrieveDataToJson() {
    Document document = mock(Document.class);
    DataContainer dataContainer = mock(DataContainer.class);

    doReturn(document).when(xmlDataExtractor).getDocument(any(File.class));
    doReturn(dataContainer).when(xmlDataExtractor).retrieveDataFromFile(document);
    doNothing().when(xmlDataExtractor).saveDataAsJson(dataContainer);

    xmlDataExtractor.retrieveDataToJson(anyString());

    verify(xmlDataExtractor).saveDataAsJson(dataContainer);
  }
}