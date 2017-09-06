package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetFinancialParams {

    FUND("Fund"), FUNCTION("Function"), FUNCTIONARY("Functionary"), SCHEME("Scheme"), SUBSCHEME(
            "Subscheme"), FUNDSOURCE("Fundsource"),FISCAL("Fiscal");

    private String value;

    AssetFinancialParams(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static AssetFinancialParams fromValue(final String text) {
        for (final AssetFinancialParams b : AssetFinancialParams.values())
            if (String.valueOf(b.value).equals(text))
                return b;
        return null;
    }
}
