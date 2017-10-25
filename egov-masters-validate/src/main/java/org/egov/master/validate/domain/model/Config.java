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
public   class Config {
    public  List<JsonFile> jsonFile;
    
    public Config(List<JsonFile> jsonFiles){
        this.jsonFile = jsonFiles;
    }

   
}