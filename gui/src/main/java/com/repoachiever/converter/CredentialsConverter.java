package com.repoachiever.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CredentialsConverter {
  @SuppressWarnings("unchecked")
  public static <T> T convert(Object input, Class<T> stub) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(input, stub);
  }
}
