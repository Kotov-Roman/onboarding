package com.epam.onboarding.tasks.first;

import lombok.Builder;

@Builder
public class DataContainer {

  private String creationDate;

  private String modificationDate;

  private String expressionTitle;

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getModificationDate() {
    return modificationDate;
  }

  public void setModificationDate(String modificationDate) {
    this.modificationDate = modificationDate;
  }

  public String getExpressionTitle() {
    return expressionTitle;
  }

  public void setExpressionTitle(String expressionTitle) {
    this.expressionTitle = expressionTitle;
  }

  @Override
  public String toString() {
    return "DataContainer{" + "\n" +
        "creationDate='" + creationDate + '\'' + "\n" +
        "modificationDate='" + modificationDate + '\'' + "\n" +
        "expressionTitle='" + expressionTitle + '\'' + "\n" +
        '}';
  }
}
