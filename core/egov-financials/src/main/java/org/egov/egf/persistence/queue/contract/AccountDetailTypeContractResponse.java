package  org.egov.egf.persistence.queue.contract;
import java.util.List;  
 import lombok.Data; 
public @Data class AccountDetailTypeContractResponse {
private ResponseInfo responseInfo = null;
private List<AccountDetailTypeContract> accountDetailTypes;
private AccountDetailTypeContract accountDetailType;
private Page page=new Page();}