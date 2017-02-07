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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "Supplier")
@SequenceGenerator(name = Relation.SEQ, sequenceName = Relation.SEQ, allocationSize = 1)
public class Relation extends AbstractPersistable<Integer> implements java.io.Serializable, EntityType {

	private static final long serialVersionUID = -9041193691552971915L;

	public static final String SEQ = "SEQ_SUPPLIER";

	@Id
	@GeneratedValue(generator = SEQ, strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(unique = true)
	@NotNull
	@Length(max = 50)
	private String code;

	@NotNull
	@Length(max = 50)
	private String name;

	@Length(max = 300)
	private String address;

	@Length(max = 10)
	private String mobile;
	@Length(max = 25)
	private String email;
	@Length(max = 250)
	private String narration;

	private Boolean isactive;

	@Length(max = 10)
	private String panno;

	@Length(max = 20)
	private String tinno;

	@Length(max = 25)
	private String regno;

	@Length(max = 25)
	private String bankaccount;

	@Length(max = 12)
	private String ifsccode;

	@ManyToOne
	@JoinColumn(name = "bank")
	private BankEntity bank;

	@Override
	public String getBankname() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBankaccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPanno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTinno() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIfsccode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getModeofpay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public Integer getEntityId() {
		if (this.id != null)
			return this.id.intValue();
		else
			return null;
	}

	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EgwStatus getEgwStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}