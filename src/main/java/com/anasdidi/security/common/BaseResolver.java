package com.anasdidi.security.common;

import graphql.schema.DataFetchingEnvironment;

public abstract class BaseResolver {

  protected String getExecutionId(DataFetchingEnvironment env) {
    return ApplicationUtils.getFormattedUUID(env.getExecutionId().toString());
  }

  protected int getSearchBy(String... values) {
    for (int i = 0; i < values.length; i++) {
      if (!values[i].isBlank()) {
        return i;
      }
    }
    return -1;
  }
}
