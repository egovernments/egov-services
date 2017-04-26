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
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var srn = getUrlParameter('srn');
var lat, lng, myCenter,status;
var updateResponse = {};
var loadDD = new $.loadDD();
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function()
{

	if(localStorage.getItem('type') == 'CITIZEN'){
		$('.employee-action').remove();
	}else if(localStorage.getItem('type') == 'EMPLOYEE'){
		$('.citizen-action').remove();
	}else{
		$('.action-section').remove();
	}

	getComplaint();
	
	$('.slide-history-menu').click(function(){
		$('.history-slide').slideToggle();
		if($('#toggle-his-icon').hasClass('fa fa-angle-down'))
		{
			$('#toggle-his-icon').removeClass('fa fa-angle-down').addClass('fa fa-angle-up');
			//$('#see-more-link').hide();
			}else{
			$('#toggle-his-icon').removeClass('fa fa-angle-up').addClass('fa fa-angle-down');
			//$('#see-more-link').show();
		}
	});

	$('#status').change(function(){
		if($(this).val() == 'FORWARDED')
			$('#approvalDepartment, #approvalDesignation, #approvalPosition').attr('required', 'required');
		else
			$('#approvalDepartment, #approvalDesignation, #approvalPosition').removeAttr('required');
	})

	$('#ct-sel-jurisd').change(function(){
		getLocality($(this).val(),'');
	});
	
	$('#approvalDepartment').change(function(){
		getDesignation($(this).val());
	});
	
	$('#approvalDesignation').change(function(){
		getUser($('#approvalDepartment').val(),$(this).val())
	});

	$('#complaint-locate').on('show.bs.modal', function() {
		//Must wait until the render of the modal appear, thats why we use the resizeMap and NOT resizingMap!! ;-)
		//$('#show_address_in_map').html($('#address_locate').html());
		myCenter=new google.maps.LatLng(lat, lng);
		initialize();
		resizeMap();
	});

	$('#update-complaint').click(function(){
		if($('form').valid()){
			var obj = $(this);
			obj.attr("disabled", "disabled");
			complaintUpdate(obj);
		}
	});

});

function getComplaint(){
	$.ajax({
		url: "/pgr/seva/_search?tenantId=ap.public&service_request_id="+srn,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo),
		beforeSend : function(){
			showLoader();
		},
		success : function(response){
			//console.log('Get complaint done!'+JSON.stringify(response));
			updateResponse = JSON.parse(JSON.stringify(response));

			if(response.service_requests.length == 0){
				bootbox.alert(translate('pgr.error.notvalid.srn'));
				hideLoader();
				return;
			}

			status = response.service_requests[0].values.complaintStatus;
			
			if(localStorage.getItem('type') == 'EMPLOYEE'){
				if(status == 'COMPLETED' || status == 'REJECTED')
					$('.action-section').remove();
			}else if(localStorage.getItem('type') == 'CITIZEN'){
				if(status == 'WITHDRAWN')
					$('.action-section').remove();
			}

			$.ajax({
				url: "/filestore/v1/files/tag?tag="+srn,
				type : 'GET',
				success : function(fileresponse){
					//console.log(fileresponse.files)

					//History
					$.ajax({
						url : "/workflow/history?tenantId=ap.public&workflowId="+response.service_requests[0].values.stateId,
						type : 'GET',
						success : function(work_response){

							//console.log('Work flow histry',JSON.stringify(work_response));

							var wf_response = {};
							wf_response['workflow'] = work_response;

							var wf_source   = $("#wfcomplaint-script").html();
							var wf_template = Handlebars.compile(wf_source);
							$('.wfcomplaint').append(wf_template(wf_response));

							if(localStorage.getItem('type') == 'CITIZEN' && status != 'COMPLETED')
								$('.feedback').remove();

							lat = response.service_requests[0].lat;
							lng = response.service_requests[0].lng;

							if (lat != '0' && lng != '0')
								getAddressbyLatLng(lat, lng, response);

							var receivingcenter = response.service_requests[0].values.receivingCenter;

							if(receivingcenter)
								getReceivingCenterbyId(receivingcenter, response);

							var departmentId = response.service_requests[0].values.departmentId;

							getDepartmentbyId(departmentId, response);

							var locationId = response.service_requests[0].values.locationId;
							var childLocationId = response.service_requests[0].values.childLocationId;

							if(locationId)
								getBoundarybyId(locationId,'LocationName', response);

							if(childLocationId)
								getBoundarybyId(childLocationId, 'ChildLocationName', response);

							response['files'] = fileresponse.files;
							var source   = $("#viewcomplaint-script").html();
							var template = Handlebars.compile(source);
							$('.viewcomplaint').append(template(response));
							translate();
							///console.log('response with files',JSON.stringify(response));

							//Start actions
							if(localStorage.getItem('type') == 'EMPLOYEE' || localStorage.getItem('type') == 'CITIZEN'){
								var loadDD = new $.loadDD();
								nextStatus(loadDD);
							}

							if(localStorage.getItem('type') == 'EMPLOYEE'){
								$('#status').attr('required','required');
								var wardId = response.service_requests[0].values.locationId;
								var localityid = response.service_requests[0].values.childLocationId;
								var serviceName = response.service_requests[0].service_name;
								complaintType(loadDD, serviceName);
								getWard(loadDD, wardId);
								getLocality(wardId, localityid);
								getDepartment(loadDD);
							}

						},
						error:function(){
							bootbox.alert('Workflow Error!');
						},
						complete : function(){
							hideLoader();
						}
					});
				},
				error : function(){
					bootbox.alert('Media file Error!');
					hideLoader();
				},
				complete : function(){
					//console.log('Media Complete function called!');
				}
			});
		},
		error : function(jqXHR, textStatus){
			bootbox.alert( "Request failed!");
			hideLoader();
		},
		complete : function(){
			//console.log('Main complete called')
		}
	});
}

function complaintUpdate(obj){
	
	var duplicateResponse, req_obj = {};
	duplicateResponse =  JSON.parse(JSON.stringify(updateResponse));

	req_obj['RequestInfo'] = duplicateResponse.response_info;
	req_obj['ServiceRequest'] = duplicateResponse.service_requests[0];
	req_obj['RequestInfo']['auth_token'] = localStorage.getItem('auth');

	var dat = new Date().toLocaleDateString();
	var time = new Date().toLocaleTimeString();
	var date = dat.split("/").join("-");
	req_obj.ServiceRequest['updated_datetime'] = date+' '+time;
	req_obj.ServiceRequest['tenantId'] = 'ap.public';
	req_obj.ServiceRequest.values['complaintStatus'] = $('#status').val() ? $('#status').val() : status;
	req_obj.ServiceRequest.values['approvalComments'] = $('#approvalComment').val();
	if(localStorage.getItem('type') == 'CITIZEN')
		req_obj.ServiceRequest.values['userId'] = localStorage.getItem('id');
	req_obj.ServiceRequest.values['assignment_id'] = req_obj.ServiceRequest.values['assigneeId'];
	delete req_obj.ServiceRequest.values['assigneeId'];
	req_obj.ServiceRequest.values['status'] = req_obj.ServiceRequest.values['complaintStatus'];
	delete req_obj.ServiceRequest.values['complaintStatus'];

	if($("#approvalDepartment").val())
		req_obj.ServiceRequest.values['departmentName'] = $("#approvalDepartment option:selected").text();
	if($("#approvalPosition").val())
		req_obj.ServiceRequest.values['assignment_id'] = $("#approvalPosition").val();

	$.ajax({
		url: "/pgr/seva/_update?jurisdiction_id=ap.public",
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		beforeSend : function(){
			showLoader();
		},
		data : JSON.stringify(req_obj),
		success : function(response){

			var filefilledlength = Object.keys(filefilled).length;
			
			if(filefilledlength > 0){
				
				var formData=new FormData();
				formData.append('jurisdictionId', 'ap.public');
				formData.append('module', 'PGR');
				formData.append('tag', srn);
				// Main magic with files here

				$('input[name=file]').each(function(){
					var file = $(this)[0].files[0];
					formData.append('file', file); 
				});

				$.ajax({
					url: "/filestore/v1/files",
					type : 'POST',
					async : false,
					// THIS MUST BE DONE FOR FILE UPLOADING
					contentType: false,
					processData : false,
					data : formData,
					success: function(fileresponse){
						//console.log('file upload success');
						bootbox.alert(translate('pgr.msg.success.grievanceupdated'), function(){ 
							window.location.reload();
						});
					},
					error: function(){
						bootbox.alert('Media file failed!');
						obj.removeAttr("disabled");
						hideLoader();
					}
				});
			}else{
				bootbox.alert(translate('pgr.msg.success.grievanceupdated'), function(){ 
					window.location.reload();
				});
			}
		},
		error : function(){
			bootbox.alert('Error while update!');
		},
		complete : function(){
			obj.removeAttr("disabled");
			hideLoader();
		}
	});
}

function complaintType(loadDD, serviceName){
	$.ajax({
		url: "/pgr/services?type=all&tenantId=ap.public",
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json"
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data.ComplaintType,
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
		});
		$('#complaintType option').each(function(){
			if($(this).text() == serviceName){
				$(this).attr('selected', 'selected');
			}
		});
	});
}

function nextStatus(loadDD){
	$.ajax({
		url : '/workflow/nextstatuses/_search?currentStatus='+status,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#status'),
			data:data,
			keyValue:'name',
			keyDisplayName:'name'
		});
		$('#status').val(status);
		$('#status').val() ? $('#status').val() : $('#status').val('');
	});
}

function getWard(loadDD, wardId){
	$.ajax({
		url: "/v1/location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?boundaryTypeName=Ward&hierarchyTypeName=Administration",
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#ct-sel-jurisd'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
		$('#ct-sel-jurisd').val(wardId);
		$('#ct-sel-jurisd').val() ? $('#ct-sel-jurisd').val() : $('#ct-sel-jurisd').val('');
	});
}

function getLocality(boundaryId, localityid){
	$.ajax({
		url: "/v1/location/boundarys/childLocationsByBoundaryId?boundaryId="+boundaryId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#location'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
		if(localityid){
			$('#location').val(localityid);
			$('#location').val() ? $('#location').val() : $('#location').val('');	
		}
	});
}

function getDepartment(loadDD){
	$.ajax({
		url: "/eis/departments",
		type : 'GET'
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalDepartment'),
			placeholder : 'Select Deparment', // default - Select(optional)
			data:data.Department,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

function getDesignation(depId){
	$.ajax({
		url: "/eis/designationByDepartmentId?id="+depId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalDesignation'),
			placeholder : 'Select Designation', // default - Select(optional)
			data:data.Designation,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

function getUser(depId, desId){
	//console.log(depId, desId);
	$.ajax({
		url: "/eis/assignmentsByDeptOrDesignId?deptId="+depId+"&desgnId="+desId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo),
	}).done(function(data) {
		loadDD.load({
			element:$('#approvalPosition'),
			placeholder : 'Select Position', // default - Select(optional)
			data:data.Assignment,
			keyValue:'position',
			keyDisplayName:'employee'
		});
	});
}

function getDepartmentbyId(departmentId, response){
	$.ajax({
		url : '/eis/departments?id='+departmentId,
		async : false,
		success : function(depresponse){
			response.service_requests[0].values['departmentName'] = depresponse.Department[0].name;
		},
		error : function(){
			bootbox.alert('Loading departmentName failed');
		}
	});
}

function getBoundarybyId(id, name, response){
	$.ajax({
		url : '/v1/location/boundarys?boundary='+id,
		async : false,
		success : function(lresponse){
			response.service_requests[0].values[''+name+''] = lresponse.Boundary[0].name;
		},
		error : function(){
			bootbox.alert('Loading location failed');
		}
	});
}

function getReceivingCenterbyId(receivingcenter, response){
	$.ajax({
		url : "/pgr/receivingcenter/_getreceivingcenterbyid?tenantId=1&id="+receivingcenter,
		type: 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
,		async : false,
		success : function(centerReponse){
			response.service_requests[0].values['recCenterText'] = centerReponse.name;
		}
	});
}

function getAddressbyLatLng(lat, lng, response){
	$.ajax({ 
        type: "POST",
        async : false,
        url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng,
        dataType: 'json',
        success : function(data){
        	response.service_requests[0].values['latlngAddress'] = data.results[0].formatted_address;
        }
	});
}

function resizeMap() {
	if(typeof map =="undefined") return;
	setTimeout( function(){resizingMap();} , 400);
}

function resizingMap() {
	if(typeof map =="undefined") return;
	var center = map.getCenter();
	google.maps.event.trigger(map, "resize");
	map.setCenter(center); 
}

function initialize() {
	
	marker=new google.maps.Marker({
		position:myCenter
	});
	
	var mapProp = {
		center:myCenter,
		mapTypeControl: true,
		zoom:12,
		mapTypeId:google.maps.MapTypeId.ROADMAP
	};
	
	map=new google.maps.Map(document.getElementById("normal"),mapProp);
	
	marker.setMap(map);
	
};

google.maps.event.addDomListener(window, 'load', initialize);

google.maps.event.addDomListener(window, "resize", resizingMap());

Handlebars.registerHelper('contains', function(string, checkString) {
	var n = string.includes(checkString);
	return n;
});