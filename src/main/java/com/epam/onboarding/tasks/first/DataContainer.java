package com.epam.onboarding.tasks.first;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DataContainer {

  private String creationDate;

  private String modificationDate;

  private String expressionTitle;

  @Override
  public String toString() {
    return "DataContainer{" + "\n" +
        "creationDate='" + creationDate + '\'' + "\n" +
        "modificationDate='" + modificationDate + '\'' + "\n" +
        "expressionTitle='" + expressionTitle + '\'' + "\n" +
        '}';
  }
}
