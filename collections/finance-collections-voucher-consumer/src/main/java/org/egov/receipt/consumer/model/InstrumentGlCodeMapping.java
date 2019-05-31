package org.egov.receipt.consumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstrumentGlCodeMapping {
private String instrumenttype;
private String glcode;
private Long validFrom;
private Long validTo;
}
