# eGov Payment Gateway

Module acts as a liaison between eGov apps and external payment gateways. It facilitates payments, reconciliation of payments and look up of transactions' status'.

- The module is intended to be invoked by a fronting billing / collection service.
- Granular details about the API's can be found in the [swagger api definition](egov-pg-service.yml)
- Reconciliation service is set to run on a daily basis and is scheduled on a Quartz Scheduler.


*Gateways Supported*
 - AXIS
 - PAYTM
 - PHONEPE

Additional gateways can be added by implementing the [Gateway](src/main/java/org/egov/pg/service/Gateway.java) interface.

# Build & Run

    mvn clean install
    java -jar target/egov-pg-service-1.0-SNAPSHOT.jar

## Dependencies

- Postgres database to store transaction data and enable quartz clustered scheduling.
- ID Gen Module to generate unique transaction ID's.
- Persister module for persistence.
- Merchant specific properties, such as merchant id and secret needs to be configured.
