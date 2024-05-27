package com.repoachiever.service.swap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.SwapFileCreationFailureException;
import com.repoachiever.exception.SwapFileDeletionFailureException;
import com.repoachiever.model.TopicLogsUnit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** Represents service responsible for temporate swap file creation. */
@Service
public class SwapService {

  /**
   * Creates temporate swap file with the given properties.
   *
   * @param swapRootPath given swap file root path.
   * @param content given swap file content.
   * @return absolute path to the swap file.
   * @throws SwapFileCreationFailureException if swap file creation failed.
   */
  public String createSwapFile(String swapRootPath, List<TopicLogsUnit> content)
      throws SwapFileCreationFailureException {
    ObjectMapper mapper = new ObjectMapper();

    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    String absoluteSwapFilePath =
        Paths.get(swapRootPath, String.format("%s.swp", UUID.randomUUID())).toString();

    File swapFile = new File(absoluteSwapFilePath);

    try {
      mapper.writeValue(swapFile, content);
    } catch (IOException e) {
      throw new SwapFileCreationFailureException(e.getMessage());
    }

    return absoluteSwapFilePath;
  }

  /**
   * Deletes temporate swap file.
   *
   * @param swapFilePath given location of the swap file to be removed.
   * @throws SwapFileDeletionFailureException if swap file deletion failed.
   */
  public void deleteSwapFile(String swapFilePath) throws SwapFileDeletionFailureException {
    try {
      Files.deleteIfExists(Paths.get(swapFilePath));
    } catch (IOException e) {
      throw new SwapFileDeletionFailureException(e.getMessage());
    }
  }
}
