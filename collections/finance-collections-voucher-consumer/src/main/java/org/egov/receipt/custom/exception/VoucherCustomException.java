package org.egov.receipt.custom.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherCustomException extends Exception{
	String status;
	String message;
public VoucherCustomException(String message) {
	super(message);
}
@Override
public String getMessage() {
	return "[status : "+status+", message : "+message+"]";
}

}
