package org.egov.inv.persistence.repository;

import org.springframework.stereotype.Service;

@Service
public class StoreJdbcRepository {

    public String getStoreId() {
        return "select nextval('seq_stores') as storeid";
    }

}
