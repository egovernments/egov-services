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
var sCode = getUrlParameter('code');
var sName = getUrlParameter('name');
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var type = localStorage.getItem("type");
var requestInfo = {};
var currentObj;
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function(){

	$.when(
		
		showLoader(),

		loadBasedonType(),

		doCheckUser(),

		loadServiceDefinition(sCode)

	).then(function() {
		//Hide Loader
		hideLoader();
	});

	//typeahead initialize
	var locationtypeahead = new $.typeahead({
		url : '/egov-location/boundarys/getLocationByLocationName?tenantId='+tenantId+'&locationName=%QUERY',
		element : $('#location'),
		keyValue:'id',
		keyDisplayName:'name',
		minLength : 1,
		hiddenelem : $("#address_id")
	});

	//Additional functionality on typeahead selected
	locationtypeahead.typeaheadobj.on('typeahead:selected', function(event, data){   
		//by default this will happen         
		$("#address_id").val(data.value);   
		//Extra functionality 
		$('#lat, #lng').val(0.0);
    });

	//Clearing hidden field value when clearing autocomplete
    typeaheadWithEventsHandling(locationtypeahead.typeaheadobj, '#address_id');

	$('#createService').click(function(e){

		if($('form').valid()){
			currentObj = $(this);
			currentObj.attr("disabled", "disabled");
			var request={}, data={};
			data['attribValues'] = [];

			data['serviceCode'] = sCode;
			data['description'] = $('#doc').val();			
			data['addressId'] = $('#address_id').val();		
			data['lat'] = $('#lat').val();
			data['lng'] = $('#lng').val();
			data['address'] = $('#landmarkDetails').val();
			data['serviceRequestId'] = '';
			data['firstName'] = $('#first_name').val() ? $('#first_name').val() : userName;
			data['phone'] = $('#phone').val() ? $('#phone').val() : userMobile;
			data['email'] = $('#email').val() ? $('#email').val() : userEmail;
			data['status'] = true;
			data['serviceName'] = '';
			data['requestedDatetime'] = "";
			data['mediaUrl'] = "";
			data['tenantId'] = tenantId;
			data["isAttribValuesPopulated"]=true;
			data["isForNewSchema"]=true;

			obj = {};
			obj = {
			    key: 'receivingMode',
			    name: $('#receivingMode').val() ? $('#receivingMode').val() : 'Website'
			};
			data['attribValues'].push(obj);

			obj = {};
			obj = {
			    key: 'receivingCenter',
			    name: $('#receivingCenter').val() ? $('#receivingCenter').val() : ''
			};
			data['attribValues'].push(obj);

			var obj = {};
			obj = {
			    key: 'status',
			    name:'DSNEW'
			};
			data['attribValues'].push(obj);

			obj = {
			    key: 'aadhaarno',
			    name:$('#aadhaarno').val() ? $('#aadhaarno').val() : ''
			};
			data['attribValues'].push(obj);

			obj = {
			    key: 'requesterAddress',
			    name: $('#complainantAddress').val() ? $('#complainantAddress').val() : ''
			};
			data['attribValues'].push(obj);

			obj = {
			    key: 'keyword',
			    name:'Deliverable_service'
			};
			data['attribValues'].push(obj);

			//Iterate App. details
			$('.appForm *').filter(':input').each(function(){
			    obj = {};
				obj = {
				    key: $(this).attr('name'),
				    name: $(this).val()
				};
				data['attribValues'].push(obj);
			});

			//Checklist
			$('.checkForm *').filter(':input[type="checkbox"]:checked').each(function(){
			    obj = {};
				obj = {
				    key: 'CHECKLIST',
				    name: $(this).attr('name')
				};
				data['attribValues'].push(obj);
				obj = {};
				obj = {
				    key: $(this).attr('name'),
				    name: $(this).is(':checked')
				};
				data['attribValues'].push(obj);
			});

			//upload files
			$('input[type=file]').each(function(){
				var formData=new FormData();
				formData.append('tenantId', tenantId);
				formData.append('module', 'SERVICES');
				var file = $(this)[0].files[0];
				if(!file)
					return;
				formData.append('file', file); 
				var resp = fileUpload(formData);
				var obj = {};
				obj = {
				    key: 'DOCUMENTS',
				    name: $(this).attr('name')
				};
				data['attribValues'].push(obj);
				obj = {};
				obj = {
				    key: $(this).attr('name'),
				    name: resp.files[0].fileStoreId
				};
				data['attribValues'].push(obj);
			});

			request['RequestInfo'] = RequestInfo.requestInfo;
			request['serviceRequest'] = data;
			//console.log(JSON.stringify(request));

			$.ajax({
				url: "/pgr/seva/_create",
				type : 'POST',
				dataType: 'json',
				processData : false,
				contentType: "application/json",
				data : JSON.stringify(request),
				success : function(response){
					//console.log('Final Response',JSON.stringify(response));
					doAck(response);
				},
				error : function(jqXHR, textStatus){
					bootbox.alert( "Create service failed!");
				},
				complete : function(){
					currentObj.removeAttr("disabled");
					hideLoader();
				}
			});
		}
	});

	$('.breadcrumb li a').click(function(){
		$('.acknowledgement, .breadcrumb').addClass('hide');
		$('.createcrn, .tour-section').removeClass('hide');
		$( "form" ).trigger('reset');
		$(this).parent().nextAll('li').remove();
	});

	fromNewService();

});

function loadServiceDefinition(code){
	$.ajax({
		url: "/pgr/servicedefinition/_search?tenantId="+tenantId+"&serviceCode="+code,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		beforeSend : function(){
			showLoader();
		},
		contentType: "application/json",
		success : function(data){
			var renderFields = new $.renderFields();
			var finTemplate = renderFields.render({'data':data.attributes,'create':true});

			if(finTemplate.formFields)
				$('#servicesBlock').prepend(finTemplate.formFields);
			else
				$('#servicesBlock').parents('.panel-primary').hide();

			if(finTemplate.checklist)
				$('#servicesBlockClist').prepend(finTemplate.checklist);
			else
				$('#servicesBlockClist').parents('.panel-primary').hide();

			if(finTemplate.documents)
				$('#servicesBlockDocs').prepend(finTemplate.documents);
			else
				$('#servicesBlockDocs').parents('.panel-primary').hide();

			initDatePicker();
			fileConstraint();
			formValidation();
			translate();
		},
		error: function(){
			//bootbox.alert('Error!');
		},
		complete : function(){
			hideLoader();
		}
	});
}

function fileUpload(formData){
	var fresponse;
	$.ajax({
		url: "/filestore/v1/files",
		type : 'POST',
		// THIS MUST BE DONE FOR FILE UPLOADING
		contentType: false,
		async : false,
		processData : false,
		beforeSend : function(){
			showLoader();
		},
		data : formData,
		success: function(fileresponse){
			fresponse = fileresponse;
		},
		error: function(){
			bootbox.alert('Media file not uploaded!');
			currentObj.removeAttr("disabled");
			hideLoader();
		},
		complete : function(){
			//console.log('Complete function called!');
		}
	});
	return fresponse;
}

var userName, userMobile, userEmail;

function doCheckUser(){
	var userId = localStorage.getItem('id');
	if(userId){
		var userArray = [];
		userArray.push(userId);
		var userRequestInfo = {};
		userRequestInfo['RequestInfo'] = RequestInfo.requestInfo;
		userRequestInfo['id'] = userArray;
		userRequestInfo['tenantId'] = tenantId;
		$.ajax({
			url : '/user/_search',
			type: 'POST',
			contentType: "application/json",
			data : JSON.stringify(userRequestInfo),
			success : function(userResponse){
				userName = userResponse.user[0].name;
				userMobile = userResponse.user[0].mobileNumber;
				userEmail = userResponse.user[0].emailId;
			},
			error: function(){
				bootbox.alert('userInfo failed!');
			}
		});	
	}
}

function doAck(response){
	var acklabel = translate('core.msg.acknowledgement');
	$('.breadcrumb').append('<li class="active">'+acklabel+'</li>');
	$('.acknowledgement, .breadcrumb').removeClass('hide');
	$('.acknowledgement #firstname').html('Dear '+response.serviceRequests[0].firstName+',');
	$('.acknowledgement #crn').html(response.serviceRequests[0].serviceRequestId);
	$('.createcrn, .tour-section').addClass('hide');
}

function fromNewService(){
	if(sCode)
		$('header .title2').html(sName);
}

function loadBasedonType(){
	if(type == 'CITIZEN'){//citizen
		$('.citizenremove').remove();
	}else if(type == 'EMPLOYEE'){//employee
		
	}
}