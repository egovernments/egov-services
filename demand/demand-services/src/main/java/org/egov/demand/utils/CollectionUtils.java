package org.egov.demand.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.egov.demand.persistence.entity.EgBill;
import org.egov.demand.persistence.entity.EgBillDetails;
import org.egov.demand.web.contract.collection.BillAccountDetails;
import org.egov.demand.web.contract.collection.BillAccountDetails.PURPOSE;
import org.egov.demand.web.contract.collection.BillDetails;
import org.egov.demand.web.contract.collection.BillInfo.COLLECTIONTYPE;
import org.egov.demand.web.contract.collection.BillInfoImpl;
import org.egov.demand.web.contract.collection.BillPayeeDetails;

public class CollectionUtils {
	private static final Logger LOGGER = Logger.getLogger(CollectionUtils.class);
    public String generateBillXML(EgBill bill, String displayMsg) {
        String xmlData = "";
        BillCollectXmlHandler handler = new BillCollectXmlHandler();
        try {
            if (bill != null && displayMsg != null) {
                LOGGER.info(" before preparing Bill XML xmlData===" + xmlData);
                xmlData = handler.toXML(prepareBillInfoXml(bill, displayMsg));
                LOGGER.info("xmlData===" + xmlData);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in postBillCollectionDetails", ex);
        }
        return xmlData;
    }

    public BillInfoImpl prepareBillInfoXml(EgBill bill, String displayMsg) {
        List<BillPayeeDetails> billPayeeDetList = new ArrayList<BillPayeeDetails>();
        BillDetails billDetails = null;
        BillAccountDetails billAccDetails = null;
        List<String> collModesList = new ArrayList<String>();
        BillPayeeDetails billPayeeDet = null;
        BillInfoImpl billInfoImpl = null;
        try {
            if (bill != null) {
                if (bill.getCollModesNotAllowed() != null) {
                    String[] collModes = bill.getCollModesNotAllowed().split(",");
                    for (String coll : collModes) {
                        collModesList.add(coll);
                    }
                }
                billInfoImpl = new BillInfoImpl(bill.getServiceCode(), bill.getFundCode(), bill.getFunctionaryCode(),
                        bill.getFundSourceCode(), bill.getDepartmentCode(), displayMsg, bill.getCitizenName(),
                        (bill.getPartPaymentAllowed() == 'Y' ? true : false), (bill.getOverrideAccountHeadsAllowed() == 'Y' ? true : false), collModesList,
                        COLLECTIONTYPE.F);
                billPayeeDet = new BillPayeeDetails(bill.getCitizenName(), bill.getCitizenAddress(), bill.getEmailId());
                billDetails = new BillDetails(bill.getId().toString(), bill.getCreateDate(), bill.getConsumerId(),bill.getConsumerType(),
                        bill.getBoundaryNum().toString(), bill.getBoundaryType(), bill.getDescription(),
                        bill.getTotalAmount(), bill.getMinAmtPayable());
                billPayeeDetList.add(billPayeeDet);
                billInfoImpl.setPayees(billPayeeDetList);
                billInfoImpl.setCallbackForApportioning(bill.getCallBackForApportion() == 'Y' ? true : false);
                boolean isActualDemand = false;

                for (EgBillDetails egBillDet : bill.getEgBillDetails()) {
                    isActualDemand = egBillDet.getAdditionalFlag()!=null && egBillDet.getAdditionalFlag() == 1 ? true : false;
                    billAccDetails = new BillAccountDetails(egBillDet.getGlcode(), egBillDet.getOrderNo(),
                            egBillDet.getCrAmount(), egBillDet.getDrAmount(), egBillDet.getFunctionCode(),
                            egBillDet.getDescription(), isActualDemand,egBillDet.getPurpose()!=null?PURPOSE.valueOf(egBillDet.getPurpose()):PURPOSE.OTHERS,egBillDet.getGroupId());
                    billDetails.addBillAccountDetails(billAccDetails);
                }
                billPayeeDet.addBillDetails(billDetails);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception in prepareBillInfoXml method", ex);
        }
        return billInfoImpl;
    }
}
