package org.egov.asset.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sequence {
    ASSETCATEGORYCODESEQUENCE("seq_egasset_categorycode"), ASSETCATEGORYSEQUENCE(
            "seq_egasset_assetcategory"), ASSETCODESEQUENCE("seq_egasset_assetcode"), ASSETSEQUENCE(
                    "seq_egasset_asset"), REVALUATIONSEQUENCE("seq_egasset_revaluation"), CURRENTVALUESEQUENCE(
                            "seq_egasset_current_value"), DISPOSALSEQUENCE(
                                    "seq_egasset_disposal"), DEPRECIATIONSEQUENCE("seq_egasset_depreciation");

    private String value;

    Sequence(final String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static Sequence fromValue(final String text) {
        for (final Sequence b : Sequence.values())
            if (String.valueOf(b.value).equals(text))
                return b;
        return null;
    }
}
