package org.egov.egf.instrument.web.contract;
import javax.validation.constraints.NotNull;

import org.egov.common.web.contract.AuditableContract;
import org.egov.egf.instrument.domain.model.TransactionType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonPropertyOrder({ "transactionType","reconciledOncreate","statusOnCreate","statusOnUpdate","statusOnReconcile"})
public class InstrumentTypePropertyContract extends AuditableContract{
	
	@NotNull
	private TransactionType transactionType;
	@NotNull
	private Boolean reconciledOncreate;
	@NotNull
	private InstrumentStatusContract statusOnCreate;
	@NotNull
	private InstrumentStatusContract statusOnUpdate;
	@NotNull
	private InstrumentStatusContract statusOnReconcile;
}