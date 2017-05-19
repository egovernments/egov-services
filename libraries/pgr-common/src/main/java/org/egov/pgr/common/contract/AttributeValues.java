package org.egov.pgr.common.contract;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AttributeValues {
    public static String getAttributeSingleValue(List<AttributeEntry> attributeValues, String expectedKey) {
        if (attributeValues == null) {
            return null;
        }
        return attributeValues.stream()
            .filter(a -> expectedKey.equals(a.getKey()))
            .findFirst()
            .map(AttributeEntry::getName)
            .orElse(null);
    }

    public static List<String> getAttributeMultipleValue(List<AttributeEntry> attributeValues, String expectedKey) {
        if (attributeValues == null) {
            return null;
        }
        return attributeValues.stream()
            .filter(a -> expectedKey.equals(a.getKey()))
            .map(AttributeEntry::getName)
            .collect(Collectors.toList());
    }

    public static void createOrUpdateAttributeEntry(List<AttributeEntry> attributeValues, String key, String name) {
        if (isAttributeKeyPresent(attributeValues, key)) {
            updateAttributeEntry(attributeValues, key, name);
        } else {
            createAttributeEntry(attributeValues, key, name);
        }
    }

    private static boolean isAttributeKeyPresent(List<AttributeEntry> attributeValues, String key) {
        return attributeValues.stream()
            .anyMatch(attribute -> key.equals(attribute.getKey()));
    }

    private static void createAttributeEntry(List<AttributeEntry> attributeValues, String key, String name) {
        attributeValues.add(new AttributeEntry(key, name));
    }

    private static void updateAttributeEntry(List<AttributeEntry> attributeValues, String key, String name) {
        final AttributeEntry matchingEntry = attributeValues.stream()
            .filter(attribute -> key.equals(attribute.getKey()))
            .findFirst()
            .orElse(null);
        matchingEntry.setName(name);
    }
}
