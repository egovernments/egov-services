/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *     accountability and the service delivery of the government  organizations.
 *
 *      Copyright (C) 2016  eGovernments Foundation
 *
 *      The updated version of eGov suite of products as by eGovernments Foundation
 *      is available at http://www.egovernments.org
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program. If not, see http://www.gnu.org/licenses/ or
 *      http://www.gnu.org/licenses/gpl.html .
 *
 *      In addition to the terms of the GPL license to be adhered to in using this
 *      program, the following additional terms are to be complied with:
 *
 *          1) All versions of this program, verbatim or modified must carry this
 *             Legal Notice.
 *
 *          2) Any misrepresentation of the origin of the material is prohibited. It
 *             is required that all modified versions of this material be marked in
 *             reasonable ways as different from the original version.
 *
 *          3) This license does not grant any rights to any user of the program
 *             with regards to rights under trademark law for use of the trade names
 *             or trademarks of eGovernments Foundation.
 *
 *    In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
$(document).ready(function(){

	var password = false;
	
	$('.check-password').blur(function(){
		if(($('#password').val()!="") && ($('#con-password').val()!=""))
		{
			if ($('#password').val() === $('#con-password').val()) {
				password = true;
				$('.password-error').hide();
				$('.check-password').removeClass('error');
			}else{
				password = false;
				$('.password-error').show();
				$('.check-password').addClass('error');
				if($('.error-check').is(':visible')){
					$('.error-check').hide();
				}
			}
		}
	});

	$('#name').keyup(function(){
		var arr = $(this).val().split(' ');
	    var result = "";
	    for (var x=0; x<arr.length; x++)
        result+=arr[x].substring(0,1).toUpperCase()+arr[x].substring(1)+' ';
	    $(this).val(result.substring(0, result.length-1));
	});

	var RI = new $.RequestInfo(localStorage.getItem("auth"));

	$('#otpbtn').click(function () {
		if($('form').valid()){
			var obj = {};
			obj['RequestInfo'] = RI.requestInfo;
			var data={};
			data['tenantId']  = tenantId;
			data['mobileNumber'] = $('#mobileNumber').val();
			obj['otp'] = data;
			//user_otp
			$.ajax({
				url : "/user-otp/v1/_send",
				type : 'POST',
				contentType: "application/json",
				beforeSend : function(){
					$('#otpbtn-section').hide();
				},
				processData : false,
				data : JSON.stringify(obj),
				success : function(response){
					//console.log('OTP success:', JSON.stringify(response));
					if(response.isSuccessful){
						$('#signup-section, #otp-section').show();
						//Add required fields
						$('#password, #con-password, #name, #activationcode').attr("required", true);
					}
				},
				error : function(){
					bootbox.alert('OTP Creation failed!', function(){ 
						$('#otpbtn-section').show();
					});
				}
			});
		}
	});
	
	$('#signupbtn').click(function(e){
		var currentObj = $(this);
		$.validator.addMethod("passwordvalidate",function(value){
		    return /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[?!@$^*-`(){}])(?!.*[&<>#%"'/\\ ]).{8,32}$/.test(value);
		},translate('core.error.password'));

		jQuery.validator.addClassRules({
			passwordvalidate : { passwordvalidate : true }    
		});

		if($('form').valid() && password){
			currentObj.prop('disabled', true);
			//validate otp
			var obj = {};
			obj['RequestInfo'] = RI.requestInfo;
			var data={};
			data['tenantId']  = tenantId;
			data['identity'] = $('#mobileNumber').val();
			data['otp'] = $('#activationcode').val();
			obj['otp'] = data;
			$.ajax({
				url : "/otp/v1/_validate",
				type : 'POST',
				contentType: "application/json",
				processData : false,
				data : JSON.stringify(obj),
				success : function(response){
					//console.log('OTP validated:', JSON.stringify(response));
					//create user
					var RI = new $.RequestInfo(localStorage.getItem("auth"));
					var reqObj = {};
					reqObj['RequestInfo'] = RI.requestInfo;
					//form serialize
					var $form = $("form");
					var data = getFormData($form);
					data['userName'] = $('#mobileNumber').val();
					data['active'] = true;
					data['type'] = 'CITIZEN';
					data['otpReference'] =  response.otp.UUID;
					data['tenantId'] = tenantId;
					reqObj['User'] = data;

					$.ajax({
						url : "/user/citizen/_create",
						type : 'POST',
						processData : false,
						data : JSON.stringify(reqObj),
						contentType: "application/json",
						success : function(userResponse){
							bootbox.alert(translate('core.account.created.successfully'), function(){ 
								window.open("../index.html","_self");
							});
						},
						error : function(xhr, status, error){
							var response = JSON.parse(xhr.responseText);
							var errorMsg = response.Error.message;
							bootbox.alert(errorMsg, function(){ 
								window.location.reload();
							});
						},
						complete : function(){
	
						}
					});
				},
				error : function(){
					bootbox.alert('OTP validation failed!', function(){ 
						currentObj.prop('disabled', false);
						$('#activationcode').val();
						$('#otpbtn-section').show();
						$('#signup-section, #otp-section').hide();
					});
				}
			});
			
		}else{
			e.preventDefault();
		}
	});

	$('#password, #username').popover({ trigger: "focus",placement: "bottom"});
	
	$(document).on('click','.password-view,.otp-view',function(){
		//console.log($(this).data('view'));
		if($(this).hasClass('password-view')){
			if($(this).data('view') == 'show'){
				$('.check-password').attr({type : 'text', autocomplete : 'off'});
				$(this).parent().empty().html('<i class="fa fa-eye-slash password-view" data-view="hide" aria-hidden="true"></i>');
			}else{
				$('.check-password').attr({type : 'password', autocomplete : 'new-password'});
				$(this).parent().empty().html('<i class="fa fa-eye password-view" data-view="show" aria-hidden="true"></i>');
			}
		}else if($(this).hasClass('otp-view')){
			if($(this).data('view') == 'show'){
				$(this).closest('.form-group').find('input').attr({type : 'text', autocomplete : 'off'});
				$(this).parent().empty().html('<i class="fa fa-eye-slash otp-view" data-view="hide" aria-hidden="true"></i>');
			}else{
				$(this).closest('.form-group').find('input').attr({type : 'password', autocomplete : 'new-password'});
				$(this).parent().empty().html('<i class="fa fa-eye otp-view" data-view="show" aria-hidden="true"></i>');
			}
		} 
	});
	
});