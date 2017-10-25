package org.egov.master.validate.domain.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("configs")
@Service
@NoArgsConstructor
public  class MasterConfiguration {
	public Config[] configs;
    public  List<Config> config;

    
    public MasterConfiguration(List<Config> configs){
        //this.configs = configs;
    }

    
}