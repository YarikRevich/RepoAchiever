package com.repoachiever.dto;

import com.repoachiever.model.TopicLogsResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

/** Represents state command result. */
@Getter
@AllArgsConstructor(staticName = "of")
public class StateExternalCommandResultDto {
  private TopicLogsResult topicLogsResult;

  private Boolean status;

  private String error;
}
