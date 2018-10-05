package org.egov.collection.consumer.model;

public enum InstrumentStatus {
    NEW, DEPOSITED, CANCELLED, DISHONOURED, RECONCILED;

    public static boolean contains(String test) {
        for (InstrumentStatus val : InstrumentStatus.values()) {
            if (val.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        return false;
    }
}
