package org.egov.lams.contract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AssetEs {

    private String asset;
    private String assetName;
    private String assetCode;
    private String assetCategory;
    private String assetDrno;

}
