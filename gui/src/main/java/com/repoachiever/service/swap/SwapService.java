package com.repoachiever.service.swap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.SwapFileCreationFailedException;
import com.repoachiever.exception.SwapFileDeletionFailedException;
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
   * @throws SwapFileCreationFailedException if swap file creation failed.
   */
  public String createSwapFile(String swapRootPath, List<TopicLogsUnit> content)
      throws SwapFileCreationFailedException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    String absoluteSwapFilePath =
        Paths.get(swapRootPath, String.format("%s.swp", UUID.randomUUID())).toString();

    File swapFile = new File(absoluteSwapFilePath);

    try {
      mapper.writeValue(swapFile, content);
    } catch (IOException e) {
      throw new SwapFileCreationFailedException(e.getMessage());
    }

    return absoluteSwapFilePath;
  }

  /**
   * Deletes temporate swap file.
   *
   * @param swapFilePath given location of the swap file to be removed.
   * @throws SwapFileDeletionFailedException if swap file deletion failed.
   */
  public void deleteSwapFile(String swapFilePath) throws SwapFileDeletionFailedException {
    try {
      Files.deleteIfExists(Paths.get(swapFilePath));
    } catch (IOException e) {
      throw new SwapFileDeletionFailedException(e.getMessage());
    }
  }
}
