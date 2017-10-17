package org.egov.tradelicense.web.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.common.domain.exception.AdhaarNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentDateNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotValidException;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.CustomDataMigrationBindException;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeApplicationException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeLicenseException;
import org.egov.tradelicense.common.domain.exception.EndPointException;
import org.egov.tradelicense.common.domain.exception.IdNotFoundException;
import org.egov.tradelicense.common.domain.exception.InvalidAdminWardException;
import org.egov.tradelicense.common.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidDocumentTypeException;
import org.egov.tradelicense.common.domain.exception.InvalidFeeDetailException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.domain.exception.InvalidLocalityException;
import org.egov.tradelicense.common.domain.exception.InvalidPropertyAssesmentException;
import org.egov.tradelicense.common.domain.exception.InvalidRevenueWardException;
import org.egov.tradelicense.common.domain.exception.InvalidSubCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidUomException;
import org.egov.tradelicense.common.domain.exception.InvalidValidityYearsException;
import org.egov.tradelicense.common.domain.exception.LegacyFeeDetailNotFoundException;
import org.egov.tradelicense.common.domain.exception.MandatoryDocumentNotFoundException;
import org.egov.tradelicense.common.domain.exception.NonLegacyLicenseUpdateException;
import org.egov.tradelicense.common.domain.exception.OldLicenseNotFoundException;
import org.egov.tradelicense.common.domain.exception.PropertyAssesmentNotFoundException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotEmptyException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotFoundException;
import org.egov.tradelicense.common.domain.exception.UserException;
import org.egov.tradelicense.web.adapters.error.AdhaarNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.AgreeMentDateNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.AgreeMentNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.AgreeMentNotValidAdapter;
import org.egov.tradelicense.web.adapters.error.DuplicateTradeApplicationAdapter;
import org.egov.tradelicense.web.adapters.error.DuplicateTradeLicenseAdapter;
import org.egov.tradelicense.web.adapters.error.EndPointExceptionAdapter;
import org.egov.tradelicense.web.adapters.error.IdNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidAdminWardAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidCategoryAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidDocumentTypeAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidFeeDetailAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidLocalityAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidPropertyAssesmentAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidRevenueWardAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidSubCategoryAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidUomAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidValidityYearsAdapter;
import org.egov.tradelicense.web.adapters.error.LegacyFeeDetailNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.MandatoryDocumentNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.NonLegacyLicenseUpdateAdapter;
import org.egov.tradelicense.web.adapters.error.OldLicenseNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.PropertyAssesmentNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.TradeLicensesNotEmptyAdapter;
import org.egov.tradelicense.web.adapters.error.TradeLicensesNotFoundAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ErrorResponse handleMissingParamsError(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setMessage("tl.error.missingparams");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomBindException.class)
	public ErrorResponse handleBindingErrors(CustomBindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((CustomBindException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((CustomBindException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((CustomBindException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		if (errors.getGlobalError() != null) {
			error.setCode(Integer.valueOf(errors.getGlobalError().getCode()));
			error.setMessage(errors.getGlobalError().getObjectName());
			error.setDescription(errors.getGlobalError().getDefaultMessage());
		} else {
			if (errors.getFieldErrorCount() > 0) {
				error.setMessage("tl.error.missingfields");
				error.setCode(400);
				error.setDescription("Missing fields");
			}
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			error.setFields(new ArrayList<>());
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				String errorCode = "tl.error." + errs.getField().substring(errs.getField().indexOf(".") + 1) + "."
						+ errs.getCode();
				ErrorField f = new ErrorField(errorCode.toLowerCase(), errs.getDefaultMessage(), errs.getField());
				error.getFields().add(f);
			}
		}
		errRes.setError(error);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomDataMigrationBindException.class)
	public ErrorResponse handleBindingErrors(CustomDataMigrationBindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		TradeLicenseRequest tradeLicenseRequest = ((CustomDataMigrationBindException) ex).getTradeLicenseRequest();
		responseInfo.setApiId(((CustomDataMigrationBindException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((CustomDataMigrationBindException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((CustomDataMigrationBindException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		if (errors.getGlobalError() != null) {
			error.setCode(Integer.valueOf(errors.getGlobalError().getCode()));
			error.setMessage(errors.getGlobalError().getObjectName());
			error.setDescription(errors.getGlobalError().getDefaultMessage());
		} else {
			if (errors.getFieldErrorCount() > 0) {
				error.setMessage("tl.error.missingfields");
				error.setCode(400);
				error.setDescription("Missing fields");
			}
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			error.setFields(new ArrayList<>());
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				
				TradeLicenseContract tlContract = null;
                String errField = errs.getField().substring(errs.getField().indexOf(".") + 1);
                String licenseIndex = errs.getField().substring((errs.getField().indexOf("[") + 1), (errs.getField().indexOf("[") + 2));
                
                if(licenseIndex != null && !licenseIndex.isEmpty()){
                    
                    Integer intVal = Integer.valueOf(licenseIndex);
                    
                    if(intVal != null && tradeLicenseRequest.getLicenses() != null 
                            && tradeLicenseRequest.getLicenses().get(intVal) != null){
                        
                        tlContract = tradeLicenseRequest.getLicenses().get(intVal);
                    }
                    
                    if(tlContract != null && tlContract.getIsLegacy() != null 
                            && tlContract.getIsLegacy() == Boolean.TRUE
                            && tlContract.getIsDataPorting() != null 
							&& tlContract.getIsDataPorting() == Boolean.TRUE){
                        
                    	if (errField != null && errs.getCode() != null 
								&& (errs.getCode().equalsIgnoreCase("NotEmpty") || errs.getCode().equalsIgnoreCase("NotNull"))
								&& (errField.equalsIgnoreCase("ownerMobileNumber")
								|| errField.equalsIgnoreCase("fatherspousename")
								|| errField.equalsIgnoreCase("ownerEmailid")
								|| errField.equalsIgnoreCase("ownerAddress")
								|| errField.equalsIgnoreCase("ownerShipType")
								|| errField.equalsIgnoreCase("uom")
								|| errField.equalsIgnoreCase("tradeCommencementDate")
								|| errField.equalsIgnoreCase("adminWard")
								|| errField.equalsIgnoreCase("category")
								|| errField.equalsIgnoreCase("subCategory")
								|| errField.equalsIgnoreCase("validityYears"))){
                        
                        	//nothing to do here
                        } else {
                        	
                        	String errorCode = "tl.error." + errs.getField().substring(errs.getField().indexOf(".") + 1) + "."
            						+ errs.getCode();
            				ErrorField f = new ErrorField(errorCode.toLowerCase(), errs.getDefaultMessage(), errs.getField());
            				error.getFields().add(f);
                        }
                        
                    } else {
                    	
                    	String errorCode = "tl.error." + errs.getField().substring(errs.getField().indexOf(".") + 1) + "."
        						+ errs.getCode();
        				ErrorField f = new ErrorField(errorCode.toLowerCase(), errs.getDefaultMessage(), errs.getField());
        				error.getFields().add(f);
                    }
                }
                
				
			}
		}
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ErrorResponse handleCustomBindErrors(BindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidInputException.class)
	public ErrorResponse handleInvalidInputErrors(InvalidInputException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserException.class)
	public ErrorResponse handleInvalidInputErrors(UserException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((UserException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((UserException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((UserException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("tl.error.user.creation");
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomInvalidInputException.class)
	public ErrorResponse handleCustomInvalidInputException(CustomInvalidInputException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((CustomInvalidInputException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((CustomInvalidInputException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((CustomInvalidInputException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage(((CustomInvalidInputException) ex).getErrorCode());
		error.setDescription(((CustomInvalidInputException) ex).getCustomMsg());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public ErrorResponse handleThrowable(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleServerError(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AdhaarNotFoundException.class)
	public ErrorResponse handleAdhaarNotFoundException(AdhaarNotFoundException ex) {
		return new AdhaarNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AgreeMentDateNotFoundException.class)
	public ErrorResponse handleAgreeMentDateNotFoundException(AgreeMentDateNotFoundException ex) {
		return new AgreeMentDateNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AgreeMentNotFoundException.class)
	public ErrorResponse handleAgreeMentNotFoundException(AgreeMentNotFoundException ex) {
		return new AgreeMentNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AgreeMentNotValidException.class)
	public ErrorResponse handleAgreeMentNotValidException(AgreeMentNotValidException ex) {
		return new AgreeMentNotValidAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateTradeLicenseException.class)
	public ErrorResponse handleDuplicateTradeLicenseException(DuplicateTradeLicenseException ex) {
		return new DuplicateTradeLicenseAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateTradeApplicationException.class)
	public ErrorResponse handleDuplicateTradeApplicationException(DuplicateTradeApplicationException ex) {
		return new DuplicateTradeApplicationAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EndPointException.class)
	public ErrorResponse handleEndPointException(EndPointException ex) {
		return new EndPointExceptionAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IdNotFoundException.class)
	public ErrorResponse handleIdNotFoundException(IdNotFoundException ex) {
		return new IdNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getFieldName(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidAdminWardException.class)
	public ErrorResponse handleInvalidAdminWardException(InvalidAdminWardException ex) {
		return new InvalidAdminWardAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidCategoryException.class)
	public ErrorResponse handleInvalidCategoryException(InvalidCategoryException ex) {
		return new InvalidCategoryAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDocumentTypeException.class)
	public ErrorResponse handleInvalidDocumentTypeException(InvalidDocumentTypeException ex) {
		return new InvalidDocumentTypeAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidFeeDetailException.class)
	public ErrorResponse handleInvalidFeeDetailException(InvalidFeeDetailException ex) {
		return new InvalidFeeDetailAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidLocalityException.class)
	public ErrorResponse handleInvalidLocalityException(InvalidLocalityException ex) {
		return new InvalidLocalityAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidPropertyAssesmentException.class)
	public ErrorResponse handleInvalidPropertyAssesmentException(InvalidPropertyAssesmentException ex) {
		return new InvalidPropertyAssesmentAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRevenueWardException.class)
	public ErrorResponse handleInvalidRevenueWardException(InvalidRevenueWardException ex) {
		return new InvalidRevenueWardAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidSubCategoryException.class)
	public ErrorResponse handleInvalidSubCategoryException(InvalidSubCategoryException ex) {
		return new InvalidSubCategoryAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidUomException.class)
	public ErrorResponse handleInvalidUomException(InvalidUomException ex) {
		return new InvalidUomAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidValidityYearsException.class)
	public ErrorResponse handleInvalidValidityYearsException(InvalidValidityYearsException ex) {
		return new InvalidValidityYearsAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MandatoryDocumentNotFoundException.class)
	public ErrorResponse handleMandatoryDocumentNotFoundException(MandatoryDocumentNotFoundException ex) {
		return new MandatoryDocumentNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NonLegacyLicenseUpdateException.class)
	public ErrorResponse handleNonLegacyLicenseUpdateException(NonLegacyLicenseUpdateException ex) {
		return new NonLegacyLicenseUpdateAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(OldLicenseNotFoundException.class)
	public ErrorResponse handleLicenseNotFoundException(OldLicenseNotFoundException ex) {
		return new OldLicenseNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PropertyAssesmentNotFoundException.class)
	public ErrorResponse handleInvalidPropertyAssementException(PropertyAssesmentNotFoundException ex) {
		return new PropertyAssesmentNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(LegacyFeeDetailNotFoundException.class)
	public ErrorResponse handleLegacyFeeDetailNotFoundException(LegacyFeeDetailNotFoundException ex) {
		return new LegacyFeeDetailNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(TradeLicensesNotFoundException.class)
	public ErrorResponse handleTradeLicensesNotFoundException(TradeLicensesNotFoundException ex) {
		return new TradeLicensesNotFoundAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(TradeLicensesNotEmptyException.class)
	public ErrorResponse handleTradeLicensesNotEmptyException(TradeLicensesNotEmptyException ex) {
		return new TradeLicensesNotEmptyAdapter().getErrorResponse(ex.getCustomMsg(), ex.getRequestInfo());
	}
	

}