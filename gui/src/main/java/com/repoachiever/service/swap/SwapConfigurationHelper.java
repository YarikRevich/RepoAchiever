package com.repoachiever.service.swap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.SwapFileCreationFailureException;
import com.repoachiever.exception.SwapFileDeletionFailureException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.UUID;

import jakarta.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

/** Represents service responsible for temporate swap file creation. */
@Service
public class SwapConfigurationHelper {
  /**
   * Creates temporate swap file with the given properties.
   *
   * @param swapRoot given swap file root path.
   * @param content given swap file content.
   * @return absolute path to the swap file.
   * @throws SwapFileCreationFailureException if swap file creation failed.
   */
  public String createSwapFile(String swapRoot, String content)
      throws SwapFileCreationFailureException {
//    FileUtils.writeStringToFile(new File(swapRoot), content, StandardCharsets.UTF_8);
//
//    ObjectMapper mapper = new ObjectMapper();
//
//    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//
//    String absoluteSwapFilePath =
//        Paths.get(swapRootPath, String.format("%s.swp", UUID.randomUUID())).toString();
//
//    File swapFile = new File(absoluteSwapFilePath);
//
//    try {
//      mapper.writeValue(swapFile, content);
//    } catch (IOException e) {
//      throw new SwapFileCreationFailureException(e.getMessage());
//    }
//
//    return absoluteSwapFilePath;

    return null;
  }
}
