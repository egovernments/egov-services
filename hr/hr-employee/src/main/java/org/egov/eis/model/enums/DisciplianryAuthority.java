package org.egov.eis.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisciplianryAuthority {
    
    GOVERNMENTOFAP_APSECRETARIAT_VELAGAPUDI("Government of AP, AP Secretariat, Velagapudi"), COMMISSIONER_DIRECTOR_OF_MUNICIPAL_ADMINISTRATION("Commissioner & Director of Municipal Administration"), 
    ENGINEER_IN_CHIEF("Engineer-in-Chief (PH)"),DIRECTOR_OF_TOWN_COUNTRY_PLANNING("Director of Town & Country Planning"),
    REGIONAL_DIRECTOR_CUM_APPELLATE_COMMISSIONER_OF_MUNICIPAL_ADMINISTRATION(" Regional Director-cum-Appellate Commissioner of Municipal Administration concerned"),
    REGIONAL_DEPUTY_DIRECTOR_OF_TOWN_COUNTRY_PLANNING("Regional Deputy Director of Town & Country Planning concerned"),
    SUPERINTENDING_ENGINEER("Superintending Engineer (PH)  concerned"),
    CHAIRPERSON_MAYOR_OF_MUNICIPAL_COUNCIL("Chairperson/Mayor of Municipal Council concerned"), 
    MUNICIPAL_COMMISSIONER("Municipal Commissioner concerned"), OTHERS("Others, if any");
    
    private String value;

    DisciplianryAuthority(String value) {
        this.value = value;
    }

    @JsonCreator
    public static List<String> getAllObjectValues() {
        List<String> allObjectValues = new ArrayList<>();
        for (DisciplianryAuthority obj : DisciplianryAuthority.values()) {
            allObjectValues.add(obj.value);
        }
        return allObjectValues;
    }

    public static List<Map<String, String>> getDisciplianryAuthority() {
        List<Map<String, String>> disciplianryAuthes = new ArrayList<>();
        for (DisciplianryAuthority obj : DisciplianryAuthority.values()) {
            Map<String, String> disciplianryAuth = new HashMap<>();
            disciplianryAuth.put("id", obj.toString());
            disciplianryAuth.put("name", obj.value);
            disciplianryAuthes.add(disciplianryAuth);
        }
        return disciplianryAuthes;
    }

    @JsonCreator
    public static DisciplianryAuthority fromValue(String passedValue) {
        for (DisciplianryAuthority obj : DisciplianryAuthority.values()) {
            if (String.valueOf(obj).equals(passedValue.toUpperCase())) {
                return obj;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return StringUtils.capitalize(name());
    }

}
