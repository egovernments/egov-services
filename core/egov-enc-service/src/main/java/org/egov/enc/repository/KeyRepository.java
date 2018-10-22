package org.egov.enc.repository;

import org.egov.enc.models.AsymmetricKey;
import org.egov.enc.models.SymmetricKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeyRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String selectSymmetricKeyQuery = "SELECT * FROM eg_enc_symmetric_keys";
    private static final String selectAsymmetricKeyQuery = "SELECT * FROM eg_enc_asymmetric_keys";

    private static final String insertSymmetricKeyQuery = "INSERT INTO eg_enc_symmetric_keys (secret_key, initial_vector, active, tenant_id) VALUES (? ,?, ?, ?)";
    private static final String insertAsymmetricKeyQuery = "INSERT INTO eg_enc_asymmetric_keys (public_key, private_key, active, tenant_id) VALUES (? ,?, ?, ?)";

    private static final String deactivateSymmetricKeyQuery = "UPDATE eg_enc_symmetric_keys SET active='false'";
    private static final String deactivateAsymmetricKeyQuery = "UPDATE eg_enc_asymmetric_keys SET active='false'";

    private static final String distinctTenantIdsQuery = "SELECT DISTINCT tenant_id FROM eg_enc_symmetric_keys WHERE active='true'";


    @Autowired
    public KeyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int insertSymmetricKey(SymmetricKey symmetricKey) {
        return jdbcTemplate.update(insertSymmetricKeyQuery,
                symmetricKey.getSecretKey(),
                symmetricKey.getInitialVector(),
                symmetricKey.isActive(),
                symmetricKey.getTenantId()
        );
    }

    public int insertAsymmetricKey(AsymmetricKey asymmetricKey) {
        return jdbcTemplate.update(insertAsymmetricKeyQuery,
                asymmetricKey.getPublicKey(),
                asymmetricKey.getPrivateKey(),
                asymmetricKey.isActive(),
                asymmetricKey.getTenantId()
        );
    }

    public int deactivateSymmetricKeys() {
        return jdbcTemplate.update(deactivateSymmetricKeyQuery);
    }

    public int deactivateAsymmetricKeys() {
        return jdbcTemplate.update(deactivateAsymmetricKeyQuery);
    }

    public List<SymmetricKey> fetchSymmetricKeys() {
        return jdbcTemplate.query(selectSymmetricKeyQuery, new BeanPropertyRowMapper<>(SymmetricKey.class));
    }

    public List<AsymmetricKey> fetchAsymmtericKeys() {
        return jdbcTemplate.query(selectAsymmetricKeyQuery, new BeanPropertyRowMapper<>(AsymmetricKey.class));
    }

    public List<String> fetchDistinctTenantIds() {
        return jdbcTemplate.queryForList(distinctTenantIdsQuery, String.class);
    }

}
