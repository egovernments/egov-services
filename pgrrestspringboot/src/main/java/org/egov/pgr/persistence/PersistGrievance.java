package org.egov.pgr.persistence;

public interface PersistGrievance {

    void save();

    /*
        CITIZEN/EMPLOYEE/ANONYMOUS:
            name
            email
            mobile

         BOUNDARY: - Location service
            locationId
            childLocationId

         Status: Auto
            status: registered

          Routing: - EA Service
            assigneeId

            //workflow somone should do

          EscalationDate: We NEED TO DO IT
            Expiry date

     */

}
