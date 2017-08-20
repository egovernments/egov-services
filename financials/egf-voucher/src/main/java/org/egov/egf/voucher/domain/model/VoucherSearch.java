package org.egov.egf.voucher.domain.model ;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VoucherSearch extends Voucher{ private String ids; 
private String  sortBy; 
private Integer pageSize; 
private Integer offset; 
} 