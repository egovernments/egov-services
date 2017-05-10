package org.egov.user.persistence.entity;

import lombok.*;
import org.egov.user.domain.model.enums.AddressType;

import javax.persistence.*;

import static org.egov.user.persistence.entity.Address.SEQ_ADDRESS;

@Entity
@Table(name = "eg_user_address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = SEQ_ADDRESS, sequenceName = SEQ_ADDRESS, allocationSize = 1)
public class Address extends AbstractAuditable<Long> {

    private static final long serialVersionUID = -2568199277079287051L;
	public static final String SEQ_ADDRESS = "SEQ_EG_USER_ADDRESS";

	@Id
	@GeneratedValue(generator = SEQ_ADDRESS, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "type")
    private String type;

	@Column(name = "city")
	private String city;

	@Column(name = "pincode")
	private String pinCode;

	@Column(name = "address")
	private String address;

    @Column(name = "tenantid")
    private String tenantId;

	@Column(name = "userid")
	private Long userId;

	public Address(org.egov.user.domain.model.Address address) {
		this.type = address.getType().name();
		this.city = address.getCity();
		this.pinCode = address.getPinCode();
		this.address = address.getAddress();
	}

	public org.egov.user.domain.model.Address toDomain() {
        return org.egov.user.domain.model.Address.builder()
                .pinCode(pinCode)
				.city(city)
				.address(address)
                .type(AddressType.valueOf(type.toUpperCase()))
                .build();
    }
}