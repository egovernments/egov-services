package org.egov.collection.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SurrenderReason {
    /**
     * id is the unique Identifier of the reason
     */
    private String id;
    /**
     * name is the reason of instrument surrender. Example "Damaged cheque","Cheque to be scrapped" etc
     */
    private String name;
    /**
     * description is detailed description of the surrender of a instrument
     */
    private String description;

}
