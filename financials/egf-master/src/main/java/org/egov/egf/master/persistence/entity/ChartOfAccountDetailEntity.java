package org.egov.egf.master.persistence.entity;
import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class ChartOfAccountDetailEntity extends AuditableEntity
{
public static final String TABLE_NAME ="egf_chartofaccountdetail";
private String id;
private String chartOfAccountId;
private String accountDetailTypeId;
public ChartOfAccountDetail toDomain(){ 
ChartOfAccountDetail chartOfAccountDetail = new ChartOfAccountDetail (); 
super.toDomain( chartOfAccountDetail);chartOfAccountDetail.setId(this.id);
chartOfAccountDetail.setChartOfAccount(ChartOfAccount.builder().id(chartOfAccountId).build());
chartOfAccountDetail.setAccountDetailType(AccountDetailType.builder().id(accountDetailTypeId).build());
return chartOfAccountDetail ;}

public ChartOfAccountDetailEntity toEntity( ChartOfAccountDetail chartOfAccountDetail ){
super.toEntity(( Auditable)chartOfAccountDetail);
this.id=chartOfAccountDetail.getId();
this.chartOfAccountId=chartOfAccountDetail.getChartOfAccount()!=null?chartOfAccountDetail.getChartOfAccount().getId():null;
this.accountDetailTypeId=chartOfAccountDetail.getAccountDetailType()!=null?chartOfAccountDetail.getAccountDetailType().getId():null;
return this;} 

}
