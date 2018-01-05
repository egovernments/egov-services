package org.egov.inv.persistence.entity;

import java.math.BigDecimal;

import org.egov.inv.model.DisposalDetail;
import org.egov.inv.model.Material;
import org.egov.inv.model.ScrapDetail;
import org.egov.inv.model.Uom;

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
public class DisposalDetailEntity {

	public static final String TABLE_NAME = "disposaldetail";
	public static final String SEQUENCE_NAME = "seq_disposaldetail";
	public static final String ALIAS = "disposaldetail";

	private String id;

	private String tenantId;

	private Double disposalQuantity;

	private String material;

	private String uom;

	private Double userDisposalQuantity;

	private Double disposalValue;

	private String disposalNumber;

	private String scrapDetailId;

	public DisposalDetail toDomain() {
		DisposalDetail disposalDetail = new DisposalDetail();
		disposalDetail.setId(id);
		disposalDetail.setTenantId(tenantId);
		disposalDetail.setDisposalQuantity(BigDecimal.valueOf(disposalQuantity));
		Material mat = new Material();
		mat.setCode(material);
		disposalDetail.setMaterial(mat);
		Uom uo = new Uom();
		uo.setCode(uom);
		disposalDetail.setUom(uo);
		disposalDetail.setUserDisposalQuantity(BigDecimal.valueOf(userDisposalQuantity));
		disposalDetail.setDisposalValue(BigDecimal.valueOf(disposalValue));
		ScrapDetail scrapDetail = new ScrapDetail();
		scrapDetail.setId(scrapDetailId);
		disposalDetail.setScrapDetails(scrapDetail);
		return disposalDetail;
	}

	public DisposalDetailEntity toEntity(DisposalDetail disposalDetail) {
		if (disposalDetail.getId() != null)
			this.id = disposalDetail.getId();
		if (disposalDetail.getTenantId() != null)
			this.tenantId = disposalDetail.getTenantId();
		if (disposalDetail.getDisposalQuantity() != null)
			this.disposalQuantity = Double.valueOf(disposalDetail.getDisposalQuantity().toString());
		this.userDisposalQuantity = Double.valueOf(disposalDetail.getUserDisposalQuantity().toString());
		this.disposalValue = Double.valueOf(disposalDetail.getDisposalValue().toString());
		if (disposalDetail.getScrapDetails() != null)
			if (disposalDetail.getScrapDetails().getId() != null)
				this.scrapDetailId = disposalDetail.getScrapDetails().getId();
		return this;
	}

}
