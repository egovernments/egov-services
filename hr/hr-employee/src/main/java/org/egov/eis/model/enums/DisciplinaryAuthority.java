/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.eis.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisciplinaryAuthority {

    GOVERNMENTOFAP_APSECRETARIAT_VELAGAPUDI(
            "Government of AP, AP Secretariat, Velagapudi"), COMMISSIONER_DIRECTOR_OF_MUNICIPAL_ADMINISTRATION(
                    "Commissioner & Director of Municipal Administration"), ENGINEER_IN_CHIEF(
                            "Engineer-in-Chief (PH)"), DIRECTOR_OF_TOWN_COUNTRY_PLANNING(
                                    "Director of Town & Country Planning"), REGIONAL_DIRECTOR_CUM_APPELLATE_COMMISSIONER_OF_MUNICIPAL_ADMINISTRATION(
                                            " Regional Director-cum-Appellate Commissioner of Municipal Administration concerned"), REGIONAL_DEPUTY_DIRECTOR_OF_TOWN_COUNTRY_PLANNING(
                                                    "Regional Deputy Director of Town & Country Planning concerned"), SUPERINTENDING_ENGINEER(
                                                            "Superintending Engineer (PH)  concerned"), CHAIRPERSON_MAYOR_OF_MUNICIPAL_COUNCIL(
                                                                    "Chairperson/Mayor of Municipal Council concerned"), MUNICIPAL_COMMISSIONER(
                                                                            "Municipal Commissioner concerned"), OTHERS(
                                                                                    "Others");

    private String value;

    DisciplinaryAuthority(final String value) {
        this.value = value;
    }

    @JsonCreator
    public static List<String> getAllObjectValues() {
        final List<String> allObjectValues = new ArrayList<>();
        for (final DisciplinaryAuthority obj : DisciplinaryAuthority.values())
            allObjectValues.add(obj.value);
        return allObjectValues;
    }

    public static List<Map<String, String>> getDisciplinaryAuthority() {
        final List<Map<String, String>> disciplinaryAuthes = new ArrayList<>();
        for (final DisciplinaryAuthority obj : DisciplinaryAuthority.values()) {
            final Map<String, String> disciplinaryAuth = new HashMap<>();
            disciplinaryAuth.put("id", obj.toString());
            disciplinaryAuth.put("name", obj.value);
            disciplinaryAuthes.add(disciplinaryAuth);
        }
        return disciplinaryAuthes;
    }

    @JsonCreator
    public static DisciplinaryAuthority fromValue(final String passedValue) {
        for (final DisciplinaryAuthority obj : DisciplinaryAuthority.values())
            if (String.valueOf(obj).equals(passedValue.toUpperCase()))
                return obj;
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return name();
    }

}
