package org.egov.lams.notification.web.contract;

import org.egov.lams.notification.model.Sms;
import org.egov.lams.notification.model.enums.Priority;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Service
public class SmsRequest {

	private String mobileNumber;
	private String message;

	public Sms toDomain() {
		return new Sms(mobileNumber, message, Priority.HIGH);
	}

}
