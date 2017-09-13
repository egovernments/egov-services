package org.egov.egf.bill.persistence.entity ;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.egf.bill.domain.model.Checklist;
import org.egov.egf.bill.domain.model.ChecklistSearch;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChecklistSearchEntity extends ChecklistEntity { private String ids; 
private String  sortBy; 
private Integer pageSize; 
private Integer offset; 
public Checklist toDomain(){ 
Checklist checklist = new Checklist (); 
super.toDomain( checklist);return checklist ;}
 
public ChecklistSearchEntity toEntity( ChecklistSearch checklistSearch){
super.toEntity(( Checklist)checklistSearch);
this.pageSize=checklistSearch.getPageSize(); this.offset=checklistSearch.getOffset(); this.sortBy=checklistSearch.getSortBy(); this.ids=checklistSearch.getIds(); return this;} 
 
} 