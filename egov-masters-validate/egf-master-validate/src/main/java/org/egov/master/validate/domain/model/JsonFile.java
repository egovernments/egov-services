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
public   class JsonFile {
    public  String name;
    public  List<Master> master;

    public JsonFile(String name, List<Master> masters){
        this.name = name;
        this.master = masters;
    }
   
}
