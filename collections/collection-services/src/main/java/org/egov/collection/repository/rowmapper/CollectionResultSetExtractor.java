package org.egov.collection.repository.rowmapper;

import org.egov.collection.model.*;
import org.egov.collection.model.enums.CollectionType;
import org.egov.collection.model.enums.InstrumentStatusEnum;
import org.egov.collection.web.contract.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CollectionResultSetExtractor implements ResultSetExtractor<List<Receipt>> {

    @Override
    public List<Receipt> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        Map<String, Receipt> receipts = new LinkedHashMap<>();

        while(resultSet.next()){
            String receiptHeader = resultSet.getString("rh_id");
            Receipt receipt;

            if(!receipts.containsKey(receiptHeader)){
                BillDetail billDetail = BillDetail.builder()
                        .billNumber(resultSet.getString("rh_referenceNumber"))
                        .consumerCode(resultSet.getString("rh_consumerCode"))
                        .consumerType(resultSet.getString("rh_consumerType"))
                        .collectionModesNotAllowed(resultSet.getString("rh_collModesNotAllwd") != null
                                ? Arrays.asList(resultSet.getString("rh_collModesNotAllwd").split("\\s*,\\s*"))
                                : Collections.emptyList())
                        .tenantId(resultSet.getString("rh_tenantId"))
                        .displayMessage(resultSet.getString("rh_displayMsg"))
                        .businessService(resultSet.getString("rh_businessDetails"))
                        .receiptNumber(resultSet.getString("rh_receiptNumber"))
                        .receiptType(resultSet.getString("rh_receiptType"))
                        .channel(resultSet.getString("rh_channel"))
                        .voucherHeader(resultSet.getString("rh_voucherheader"))
                        .collectionType(CollectionType.valueOf(resultSet.getString("rh_collectionType")))
                        .boundary(resultSet.getString("rh_boundary"))
                        .reasonForCancellation(resultSet.getString("rh_reasonForCancellation"))
                        .cancellationRemarks(resultSet.getString("rh_cancellationRemarks"))
                        .status(resultSet.getString("rh_status"))
                        .receiptDate(resultSet.getLong("rh_receiptDate"))
                        .billDescription(resultSet.getString("rh_referenceDesc"))
                        .manualReceiptNumber(resultSet.getString("rh_manualReceiptNumber"))
                        .amountPaid(BigDecimal.ZERO)
                        .totalAmount(getBigDecimalValue(resultSet.getBigDecimal("rh_totalAmount")))
                        .minimumAmount(getBigDecimalValue(resultSet.getBigDecimal("rh_minimumAmount")))
                        .billAccountDetails(new ArrayList<>())
                        .build();

                Bill billInfo = Bill.builder()
                        .payeeName(resultSet.getString("rh_payeename"))
                        .payeeAddress(resultSet.getString("rh_payeeAddress"))
                        .payeeEmail(resultSet.getString("rh_payeeEmail"))
                        .paidBy(resultSet.getString("rh_paidBy"))
                        .tenantId(resultSet.getString("rh_tenantId"))
                        .billDetails(Collections.singletonList(billDetail))
                        .build();

                AuditDetails auditDetailsIns = AuditDetails.builder()
                        .createdBy(resultSet.getLong("rh_createdBy"))
                        .createdDate(resultSet.getLong("rh_createdDate"))
                        .lastModifiedBy(resultSet.getLong("rh_lastModifiedBy"))
                        .lastModifiedDate(resultSet.getLong("rh_lastModifiedDate"))
                        .build();



                Instrument instrument = Instrument.builder()
                        .id(resultSet.getString("ins_instrumentheader"))
                        .amount(resultSet.getBigDecimal("ins_amount"))
                        .transactionDateInput(resultSet.getLong("ins_transactiondate"))
                        .transactionNumber(resultSet.getString("ins_transactionNumber"))
                        .instrumentType(InstrumentType.builder().name(resultSet.getString("ins_instrumenttype"))
                                .build())
                        .instrumentStatus(InstrumentStatusEnum.valueOf(resultSet.getString("ins_instrumentstatus")))
                        .ifscCode(resultSet.getString("ins_ifsccode"))
                        .bank(BankContract.builder().name(resultSet.getString("ins_bankid")).build())
                        .transactionType(TransactionType.valueOf(resultSet.getString("ins_transactiontype")))
                        .payee(resultSet.getString("ins_payee"))
                        .drawer(resultSet.getString("ins_drawer"))
                        .surrenderReason(SurrenderReason.builder().name(resultSet.getString("ins_surrenderreason")).build())
                        .serialNo(resultSet.getString("ins_serialno"))
                        .auditDetails(auditDetailsIns)
                        .build();

                AuditDetails auditDetails = AuditDetails.builder()
                        .createdBy(resultSet.getLong("rh_createdBy"))
                        .createdDate(resultSet.getLong("rh_createdDate"))
                        .lastModifiedBy(resultSet.getLong("rh_lastModifiedBy"))
                        .lastModifiedDate(resultSet.getLong("rh_lastModifiedDate"))
                        .build();

                receipt = Receipt.builder()
                        .tenantId(resultSet.getString("rh_tenantId"))
                        .bill(Collections.singletonList(billInfo))
                        .transactionId(resultSet.getString("rh_transactionid"))
                        .instrument(instrument)
                        .auditDetails(auditDetails)
                        .build();

                receipts.put(receiptHeader, receipt);

            } else {
                receipt = receipts.get(receiptHeader);
            }

            BillDetail billDetail = receipt.getBill().get(0).getBillDetails().get(0);
            billDetail.getBillAccountDetails().add(populateAccountDetail
                    (resultSet, billDetail));

        }

        return new ArrayList<>(receipts.values());
    }

    private BillAccountDetail populateAccountDetail(ResultSet resultSet, BillDetail billDetail) throws SQLException,
            DataAccessException{

        BigDecimal crAmount = getBigDecimalValue(resultSet.getBigDecimal("rd_cramount"));
        billDetail.setAmountPaid(billDetail.getAmountPaid().add(crAmount));

        return BillAccountDetail.builder()
                    .isActualDemand((Boolean) resultSet.getObject("rd_isActualDemand"))
                    .tenantId(resultSet.getString("rd_tenantId"))
                    .billDetail(resultSet.getString("rh_id"))
                    .creditAmount(crAmount)
                    .crAmountToBePaid(getBigDecimalValue(resultSet.getBigDecimal("rd_actualcramountToBePaid")))
                    .accountDescription(resultSet.getString("rd_description"))
                    .order(resultSet.getInt("rd_ordernumber"))
                    .debitAmount(getBigDecimalValue(resultSet.getBigDecimal("rd_dramount")))
                    .glcode(resultSet.getString("rd_chartOfAccount"))
                    .purpose(Purpose.valueOf(resultSet.getString("rd_purpose")))
                    .build();
    }

    private BigDecimal getBigDecimalValue(BigDecimal amount){
        return Objects.isNull(amount) ? BigDecimal.ZERO : amount;
    }

}
