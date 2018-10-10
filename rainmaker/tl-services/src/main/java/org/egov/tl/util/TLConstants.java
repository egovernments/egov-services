package org.egov.tl.util;

import org.springframework.stereotype.Component;

@Component
public class TLConstants {


    public TLConstants() {}

    public static final String PROPERTY_JSONPATH = "$.Properties[0].propertyId";

    public static final String MODULE = "tl-services";

    public static final String NOTIFICATION_LOCALE = "en_IN";

    public static final String NOTIFICATION_INITIATED = "tl.en.counter.initiate";

    public static final String NOTIFICATION_APPLIED = "tl.en.counter.submit";

    public static final String NOTIFICATION_PAYMENT_OWNER = "tl.en.counter.payment.successful.owner";

    public static final String NOTIFICATION_PAYMENT_PAYER = "tl.en.counter.payment.successful.payer";

    public static final String NOTIFICATION_PAID = "tl.en.counter.pending.approval";

    public static final String NOTIFICATION_APPROVED = "tl.en.counter.approved";

    public static final String NOTIFICATION_REJECTED = "tl.en.counter.rejected";

    public static final String NOTIFICATION_CANCELLED = "tl.en.counter.cancelled";





}
