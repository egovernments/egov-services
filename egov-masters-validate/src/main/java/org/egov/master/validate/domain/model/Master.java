package org.egov.master.validate.domain.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Service
@NoArgsConstructor
public   class Master {
    public  String name;
    public  String dataKey;
    public  String schema;
    public String[] uniqueFields;
    public Master(String name, String dataKey, String schema){
        this.name = name;
        this.dataKey = dataKey;
        this.schema = schema;
    }
}
