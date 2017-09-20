package org.egov.egf.voucher.web.repository;

import org.egov.egf.voucher.web.contract.VoucherContract;
import org.egov.egf.voucher.web.requests.VoucherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherContractRepository {

    private RestTemplate restTemplate;
    private String hostUrl;
    public static final String SEARCH_URL = " /egf-voucher/vouchers/search?";

    public VoucherContractRepository(@Value("${egf.voucher.host.url}") String hostUrl, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.hostUrl = hostUrl;
    }

    public VoucherContract findById(VoucherContract voucherContract) {

        String url = String.format("%s%s", hostUrl, SEARCH_URL);
        StringBuffer content = new StringBuffer();
        if (voucherContract.getId() != null) {
            content.append("id=" + voucherContract.getId());
        }

        if (voucherContract.getTenantId() != null) {
            content.append("&tenantId=" + voucherContract.getTenantId());
        }
        url = url + content.toString();
        VoucherResponse result = restTemplate.postForObject(url, null, VoucherResponse.class);

        if (result.getVouchers() != null && result.getVouchers().size() == 1) {
            return result.getVouchers().get(0);
        } else {
            return null;
        }

    }
}