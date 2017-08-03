package org.egov.pgr.domain.exception;

import lombok.Getter;

import java.util.HashMap;

public class PGRMasterException extends RuntimeException{

    @Getter
    private HashMap<String ,String > hashMap;

    public PGRMasterException(HashMap<String ,String > hashMap){
        this.hashMap = hashMap;
    }
}
