package org.egov.inv.persistance.repository.buider;

import org.springframework.stereotype.Component;

@Component
public class MaterialQueryBuilder {

    public String getSerialNo() {
        return "select code from material order by id desc limit 1";
    }
}
