/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
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
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var loadDD = new $.loadDD();
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
$(document).ready(function(){
 	
 	doCheckUser();

 	$('#userUpdate').click(function(e){
 		if($('form').valid()){
 			var reqObj = {};
			reqObj['RequestInfo'] = RequestInfo.requestInfo;
			//form serialize
			var $form = $("form");
			var data = getFormData($form);
			data['active'] = true;
			data['type'] = localStorage.getItem("type");
			data['tenantId'] = tenantId;
			data['id'] = localStorage.getItem("id");
			reqObj['User'] = data;
 			//ajax call
 			$.ajax({
 				url: '/user/profile/_update',
 				type : 'POST',
				processData : false,
				data : JSON.stringify(reqObj),
				beforeSend: function(){
					showLoader();
				},
				contentType: "application/json",
				success : function(userResponse){
					bootbox.alert(translate('core.msg.profile.updated'), function(){ 
						doCheckUser();
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
					hideLoader();
				}
 			});
 		}
 	});

});

function doCheckUser(){
	var userId = localStorage.getItem('id');
	if(userId){
		var userArray = [];
		userArray.push(userId);
		var requestInfo = {};
		requestInfo['RequestInfo'] = RequestInfo.requestInfo;
		requestInfo['id'] = userArray;
		requestInfo['tenantId'] = tenantId;
		$.ajax({
			url : '/user/_search',
			type: 'POST',
			beforeSend: function(){
				showLoader();
			},
			contentType: "application/json",
			data : JSON.stringify(requestInfo),
			success : function(userResponse){
				console.log(JSON.stringify(userResponse));
				$('#salutation').val(userResponse.user[0].salutation);
				$('#name').val(userResponse.user[0].name);
				$('input[name=gender][value='+userResponse.user[0].gender+']').prop('checked', true);
				$('#mobileNumber').val(userResponse.user[0].mobileNumber);
				$('#emailId').val(userResponse.user[0].emailId);
				$('#altContactNumber').val(userResponse.user[0].altContactNumber);
				$('#dob').val(userResponse.user[0].dob);
				$('#aadhaarNumber').val(userResponse.user[0].aadhaarNumber);
				$('#pan').val(userResponse.user[0].pan);
				$('#locale').val(userResponse.user[0].locale);
			},
			error: function(){
				bootbox.alert('userInfo failed!');
			},
			complete : function(){
				hideLoader();
			}
		});	
	}
}