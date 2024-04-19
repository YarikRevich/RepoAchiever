package com.repoachiever.converter;

import com.opencsv.bean.CsvToBeanBuilder;
import com.repoachiever.exception.SecretsConversionException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

/** Represents cloud provider secrets converter */
public class SecretsConverter {
  /**
   * Converts given credentials CSV file to a certain object. Exposed as a static method to be used
   * with Terraform command definitions.
   *
   * @param content given file content to be processed.
   * @return converted credentials.
   * @throws SecretsConversionException if any operation in conversion flow failed.
   */
  @SuppressWarnings("unchecked")
  public static <T> T convert(Class<T> obj, String content) throws SecretsConversionException {
    try {
      return (T)
          new CsvToBeanBuilder(
                  new BufferedReader(
                      new InputStreamReader(new ByteArrayInputStream(content.getBytes()))))
              .withType(obj.getDeclaredConstructor().newInstance().getClass())
              .withIgnoreLeadingWhiteSpace(true)
              .build()
              .parse()
              .get(1);
    } catch (InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException e) {
      throw new SecretsConversionException(e.getMessage());
    }
  }
}
