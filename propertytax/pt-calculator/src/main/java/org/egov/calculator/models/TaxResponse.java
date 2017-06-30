package org.egov.calculator.models;

import java.util.List;

import org.egov.models.HeadWiseTax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class contains all the properties which are required for calculating PropertyTax.
 * @author udaya
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxResponse {
    public Double monthlyRentalvalue;
    public Double depreciation;
    public Double grossARV;
    public Double annualRentalValue;
    public Double treeTax;
    public Double generalTax; // General Tax
    public Double educationalCess; // Educational Cess
    public Double employeeGuranteeCess; // Employee Guarantee cess
    public List<HeadWiseTax> taxHeadWiseList;
    public Double totalTax;
    private String floorNumber;
    private Integer unitNo;
}
