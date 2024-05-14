package com.repoachiever.repository;

import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.entity.repository.ExporterEntity;
import com.repoachiever.entity.repository.ProviderEntity;
import com.repoachiever.exception.QueryEmptyResultException;
import com.repoachiever.exception.QueryExecutionFailureException;
import com.repoachiever.exception.RepositoryOperationFailureException;
import com.repoachiever.repository.executor.RepositoryExecutor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents repository implementation to handle provider table.
 */
@ApplicationScoped
public class ExporterRepository {
    @Inject
    PropertiesEntity properties;

    @Inject
    RepositoryExecutor repositoryExecutor;

    /**
     * Inserts given values into the exporter table.
     *
     * @param host given exporter host.
     * @throws RepositoryOperationFailureException if operation execution fails.
     */
    public void insert(String host) throws RepositoryOperationFailureException {
        try {
            repositoryExecutor.performQuery(
                    String.format(
                            "INSERT INTO %s (host) VALUES ('%s')",
                            properties.getDatabaseExporterTableName(),
                            host));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }
    }

    /**
     * Checks if exporter entity with the given host is present.
     *
     * @param host given host of the exporter.
     * @return result of the check.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public Boolean isPresentByHost(String host) throws RepositoryOperationFailureException {
        try {
            ResultSet resultSet = repositoryExecutor.performQueryWithResult(
                    String.format(
                            "SELECT t.id FROM %s as t WHERE t.host = '%s'",
                            properties.getDatabaseExporterTableName(),
                            host));

            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RepositoryOperationFailureException(e.getMessage());
            }
        } catch (QueryEmptyResultException e) {
            return false;
        } catch (QueryExecutionFailureException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return true;
    }

    /**
     * Attempts to retrieve exporter entity by the given host.
     *
     * @param host given host of the exporter.
     * @return retrieved config entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public ExporterEntity findByHost(String host) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.id FROM %s as t WHERE t.host = '%s'",
                                    properties.getDatabaseExporterTableName(),
                                    host));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        Integer id;

        try {
            id = resultSet.getInt("id");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return ExporterEntity.of(id, host);
    }

    /**
     * Attempts to retrieve provider entity by the given identificator.
     *
     * @param id given identificator of the exporter.
     * @return retrieved exporter entity.
     * @throws RepositoryOperationFailureException if repository operation fails.
     */
    public ExporterEntity findById(Integer id) throws RepositoryOperationFailureException {
        ResultSet resultSet;

        try {
            resultSet =
                    repositoryExecutor.performQueryWithResult(
                            String.format(
                                    "SELECT t.host FROM %s as t WHERE t.id = '%s'",
                                    properties.getDatabaseExporterTableName(),
                                    id));

        } catch (QueryExecutionFailureException | QueryEmptyResultException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        String host;

        try {
            host = resultSet.getString("host");
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        try {
            resultSet.close();
        } catch (SQLException e) {
            throw new RepositoryOperationFailureException(e.getMessage());
        }

        return ExporterEntity.of(id, host);
    }
}
