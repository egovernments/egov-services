package org.egov.tl.service;

import org.egov.tl.web.models.Difference;
import org.egov.tl.web.models.TradeLicense;
import org.egov.tl.web.models.TradeLicenseRequest;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.NewObject;
import org.javers.core.diff.changetype.ObjectRemoved;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static org.egov.tl.util.TLConstants.FIELDS_TO_IGNORE;

@Service
public class DiffService {


    /**
     * Creates a list of Difference object between the update and search
     *
     * @param request      The tradeLicenseRequest for update
     * @param searchResult The searched licenses corresponding to the request
     * @return List of Difference object
     */
    public Map<String, Difference> getDifference(TradeLicenseRequest request, List<TradeLicense> searchResult) {

        List<TradeLicense> licenses = request.getLicenses();
        Map<String, Difference> diffMap = new LinkedHashMap<>();
        Map<String, TradeLicense> idToTradeLicenseMap = new HashMap<>();

        searchResult.forEach(license -> {
            idToTradeLicenseMap.put(license.getId(), license);
        });

        TradeLicense licenseFromSearch;

        for (TradeLicense license : licenses) {
            licenseFromSearch = idToTradeLicenseMap.get(license.getId());
            Difference diff = new Difference();
            diff.setId(license.getId());
            diff.setFieldsChanged(getUpdatedFields(license, licenseFromSearch));
            diff.setClassesAdded(getObjectsAddedOrRemoved(license, licenseFromSearch, true));
            diff.setClassesRemoved(getObjectsAddedOrRemoved(license, licenseFromSearch, false));
            diffMap.put(license.getId(), diff);
        }

        return diffMap;
    }


    /**
     * Gives the field names whose values are different in the two classes
     *
     * @param licenseFromUpdate License from update request
     * @param licenseFromSearch License from db on which update is called
     * @return List of variable names which are changed
     */
    private List<String> getUpdatedFields(TradeLicense licenseFromUpdate, TradeLicense licenseFromSearch) {

        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(licenseFromUpdate, licenseFromSearch);
        List<ValueChange> changes = diff.getChangesByType(ValueChange.class);

        List<String> updatedFields = new LinkedList<>();

        if (CollectionUtils.isEmpty(changes))
            return updatedFields;

        changes.forEach(change -> {
            if (!FIELDS_TO_IGNORE.contains(change.getPropertyName()))
                updatedFields.add(change.getPropertyName());
        });

        return updatedFields;
    }


    /**
     * Gives the names of the classes whose object are added or removed between the given licenses
     *
     * @param licenseFromUpdate License from update request
     * @param licenseFromSearch License from db on which update is called
     * @return Names of Classes added or removed during update
     */
    private List<String> getObjectsAddedOrRemoved(TradeLicense licenseFromUpdate, TradeLicense licenseFromSearch, Boolean added) {

        Javers javers = JaversBuilder.javers().build();
        Diff diff = javers.compare(licenseFromUpdate, licenseFromSearch);
        List objectsAddedOrRemoved;
        if (added)
            objectsAddedOrRemoved = diff.getObjectsByChangeType(NewObject.class);
        else objectsAddedOrRemoved = diff.getObjectsByChangeType(ObjectRemoved.class);

        List<String> classModified = new LinkedList<>();

        if (CollectionUtils.isEmpty(objectsAddedOrRemoved))
            return classModified;

        objectsAddedOrRemoved.forEach(object -> {
            if (!classModified.contains(object.getClass().toString()))
                classModified.add(object.getClass().toString());
        });

        return classModified;
    }


}
