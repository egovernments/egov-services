package org.egov.property.model;

import java.util.List;
import org.egov.models.ResponseInfo;
import org.egov.models.TitleTransfer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransferSearchResponse {
	

    private List<TitleTransfer> titleTransfers;

    private ResponseInfo responseInfo;

}
