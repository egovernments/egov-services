package org.egov.inv.domain.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InventoryUtilities {
	
	public static BigDecimal getQuantityInSelectedUom(BigDecimal quantity, BigDecimal conversionFactor) {
		return quantity.divide(conversionFactor,10, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal getQuantityInBaseUom(BigDecimal quantity, BigDecimal conversionFactor) {
		return quantity.multiply(conversionFactor).setScale(10, RoundingMode.HALF_UP);
	}

}
