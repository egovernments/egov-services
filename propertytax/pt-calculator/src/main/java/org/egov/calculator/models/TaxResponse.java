package org.egov.calculator.models;


/**
 * This class contains all the properties which are required for calculating PropertyTax.
 * @author udaya
 *
 */
public class TaxResponse   {
	public Double MRV;
	public Double depreciation;
	public Double grossARV;
	public Double ARV;
	public Double treeTax;
	public Double generalTax; //General Tax
	public Double EC; //Educational Cess
	public Double EGC; //Employee Guarantee cess
	public Double PT; //Property Tax
	
	public Double getMRV() {
		return MRV;
	}
	public void setMRV(Double mRV) {
		MRV = mRV;
	}
	public Double getDepreciation() {
		return depreciation;
	}
	public void setDepreciation(Double depreciation) {
		this.depreciation = depreciation;
	}
	public Double getGrossARV() {
		return grossARV;
	}
	public void setGrossARV(Double grossARV) {
		this.grossARV = grossARV;
	}
	public Double getARV() {
		return ARV;
	}
	public void setARV(Double aRV) {
		ARV = aRV;
	}
	public Double getTreeTax() {
		return treeTax;
	}
	public void setTreeTax(Double treeTax) {
		this.treeTax = treeTax;
	}
	public Double getGeneralTax() {
		return generalTax;
	}
	public void setGeneralTax(Double gT) {
		generalTax = gT;
	}
	public Double getEC() {
		return EC;
	}
	public void setEC(Double eC) {
		EC = eC;
	}
	
	public Double getEGC() {
		return EGC;
	}
	public void setEGC(Double eGC) {
		EGC = eGC;
	}
	public Double getPT() {
		return PT;
	}
	public void setPT(Double pT) {
		PT = pT;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TaxEntities [MRV=");
		builder.append(MRV);
		builder.append(", depreciation=");
		builder.append(depreciation);
		builder.append(", grossARV=");
		builder.append(grossARV);
		builder.append(", ARV=");
		builder.append(ARV);
		builder.append(", treeTax=");
		builder.append(treeTax);
		builder.append(", generalTax=");
		builder.append(generalTax);
		builder.append(", EC=");
		builder.append(EC);
		builder.append(", EGC=");
		builder.append(EGC);
		builder.append(", PT=");
		builder.append(PT);
		builder.append("]");
		return builder.toString();
	}
	
	
}

