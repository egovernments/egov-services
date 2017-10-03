package org.egov.master.validate.domain.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("config")
@Service
public class MasterConfig {
 private  List<Config> masters;  
    
    
}
