package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/** Represents RepoAchiever Cluster suspender. */
@Getter
@AllArgsConstructor(staticName = "of")
public class SuspenderDto {
    private String name;

    private AtomicReference<CountDownLatch> awaiter;
}
