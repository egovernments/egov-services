/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any Long of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.egf.entity;

import static  org.egov.egf.entity.VoucherHeader.SEQ_VOUCHERHEADER ;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "egf_voucherheader")
@SequenceGenerator(name = SEQ_VOUCHERHEADER, sequenceName = SEQ_VOUCHERHEADER, allocationSize = 1)
public class VoucherHeader extends AbstractAuditable  {

    public static final String SEQ_VOUCHERHEADER = "seq_voucherheader";
    private static final long serialVersionUID = -1950866465902911747L;
    @Id
    @GeneratedValue(generator = SEQ_VOUCHERHEADER, strategy = GenerationType.SEQUENCE)
    private Long id;
    @Length(max=16)
    private String name;
    @Length(max=16)
    private String type;
    @Length(max=256)
    private String description;
    @Length(max=32)
    private String voucherNumber;
    private Date voucherDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundId")
    private Fund fundId;
    private Long fiscalPeriodId;
    private Integer status;
    private Long originalvcId;
    private Integer isConfirmed;
    private Long refvhId;
    private String cgvn;
    private Long moduleId;
    
    @Transient
    private Boolean isRestrictedtoOneFunctionCenter;
    @Transient
    private String voucherNumberPrefix;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "voucherHeaderId", targetEntity = GeneralLedger.class)
   // @DrillDownTable
    private Set<GeneralLedger> generalLedger;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "voucherheaderid", targetEntity = Vouchermis.class)
  //  @DrillDown
    private Vouchermis vouchermis;

    @Transient
    private List<GeneralLedger> accountDetails = new ArrayList<>(0);

    @Transient
    private List<GeneralLedgerDetail> subledgerDetails = new ArrayList<>(0);

     
     
    @Transient
    private String voucherNumType;

  

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
       /* for (final GeneralLedger detail : generalLedger)
            amount = amount.add(new BigDecimal(detail.));*/
        return amount;
    }


}
