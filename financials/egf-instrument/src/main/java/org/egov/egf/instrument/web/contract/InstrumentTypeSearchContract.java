package org.egov.egf.instrument.web.contract ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class InstrumentTypeSearchContract extends InstrumentTypeContract { private String ids; 
private Integer pageSize; 
private Integer offset; 
} 