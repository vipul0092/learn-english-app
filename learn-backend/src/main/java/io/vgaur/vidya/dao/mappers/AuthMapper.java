package io.vgaur.vidya.dao.mappers;

import io.vgaur.vidya.models.auth.ApiKeyData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by vgaur created on 03/08/20
 */
public interface AuthMapper {

    void saveToken(@Param("id") UUID tokenId, @Param("metadata") String metadata)
            throws SQLException, PersistenceException;

    String getToken(@Param("id") UUID tokenId) throws SQLException, PersistenceException;

    ApiKeyData getApiKey(@Param("id") UUID apiKey) throws SQLException, PersistenceException;

    void deleteToken(@Param("id") UUID tokenId) throws SQLException, PersistenceException;
}
