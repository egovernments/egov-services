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
var departmentId, designationId;
var loadDD = new $.loadDD();
$(document).ready(function()
{
	if(localStorage.getItem('type') == 'CITIZEN'){
		$('.employee-action').remove();
	}else if(localStorage.getItem('type') == 'EMPLOYEE'){
		$('.citizen-action').remove();
	}else{
		$('.action-section').remove();
	}
	
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
		getLocality($(this).val());
	});
	
	$('#approvalDepartment').change(function(){
		getDesignation($(this).val());
	});
	
	$('#approvalDesignation').change(function(){
		getUser($('#approvalDepartment').val(),$(this).val())
	});

	var headers = new $.headers();

	//console.log(headers.header)

	$.ajax({
		url: "/pgr/seva?jurisdiction_id=2&service_request_id="+srn,
		headers : headers.header,
		beforeSend : function(){
			showLoader();
		},
		success : function(response){
			//console.log('Get complaint done!'+JSON.stringify(response));
			updateResponse = response;

			if(response.service_requests.length == 0){
				bootbox.alert('Not a valid SRN!');
				hideLoader();
				return;
			}

			status = response.service_requests[0].values.ComplaintStatus;
			
			if(localStorage.getItem('type') == 'EMPLOYEE'){
				if(status == 'COMPLETED' || status == 'REJECTED')
					$('.action-section').remove();
			}else if(localStorage.getItem('type') == 'CITIZEN'){
				if(status == 'WITHDRAWN')
					$('.action-section').remove();
			}

			$.ajax({
				url: "/filestore/files?tag="+srn,
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

							lat = response.service_requests[0].lat;
							lng = response.service_requests[0].lng;

							if(localStorage.getItem('type') == 'CITIZEN' && status != 'COMPLETED')
								$('.feedback').remove();

							if (lat != '0' && lng != '0'){
								$.ajax({ 
							        type: "POST",
							        async : false,
							        url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng='+response.service_requests[0].lat+','+response.service_requests[0].lng,
							        dataType: 'json',
							        success : function(data){
							        	response.service_requests[0].values['latlngAddress'] = data.results[0].formatted_address;
							        }
								});
							}

							response['files'] = fileresponse.files;
							var source   = $("#viewcomplaint-script").html();
							var template = Handlebars.compile(source);
							//response['service_requests'][0]['customLocation'] = response['service_requests'][0]['values'].ChildLocationName+' - '+response['service_requests'][0]['values'].LocationName;
							$('.viewcomplaint').append(template(response));
							//console.log('response with files',JSON.stringify(response));

							//Start actions
							if(localStorage.getItem('type') == 'EMPLOYEE' || localStorage.getItem('type') == 'CITIZEN'){
								var loadDD = new $.loadDD();
								nextStatus(loadDD);
								$('#status').val(status);
							}

							if(localStorage.getItem('type') == 'EMPLOYEE'){
								var wardId = response.service_requests[0].values.LocationId;
								var localityid = response.service_requests[0].values.ChildLocationId;
								complaintType(loadDD);
								$('#complaintType option').each(function(){
									if($(this).text() == (response.service_requests[0].service_name)){
										$(this).attr('selected', 'selected');
									}
								});
								getWard(loadDD);
								getDepartment(loadDD);
								$('#ct-sel-jurisd').val(wardId);
								getLocality(wardId);
								$('#location').val(localityid);
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

function complaintUpdate(obj){
	var req_obj = {};
	req_obj['RequestInfo'] = updateResponse.response_info;
	req_obj['ServiceRequest'] = updateResponse.service_requests[0];

	req_obj['RequestInfo']['auth_token'] = localStorage.getItem('auth');

	var dat = new Date().toLocaleDateString();
	var time = new Date().toLocaleTimeString();
	var date = dat.split("/").join("-");
	req_obj.ServiceRequest['updated_datetime'] = date+' '+time;
	req_obj.ServiceRequest['tenantId'] = 'ap.public';
	req_obj.ServiceRequest.values['ComplaintStatus'] = $('#status').val();
	req_obj.ServiceRequest.values['approvalComments'] = $('#approvalComment').val();
	req_obj.ServiceRequest.values['userId'] = localStorage.getItem('id');
	req_obj.ServiceRequest.values['assignment_id'] = req_obj.ServiceRequest.values['assigneeId'];
	delete req_obj.ServiceRequest.values['assigneeId'];
	req_obj.ServiceRequest.values['locationId'] = req_obj.ServiceRequest.values['LocationId'];
	delete req_obj.ServiceRequest.values['LocationId'];
	req_obj.ServiceRequest.values['childLocationId'] = req_obj.ServiceRequest.values['ChildLocationId'];
	delete req_obj.ServiceRequest.values['ChildLocationId'];
	req_obj.ServiceRequest.values['childLocationName'] = req_obj.ServiceRequest.values['ChildLocationName'];
	delete req_obj.ServiceRequest.values['ChildLocationName'];
	req_obj.ServiceRequest.values['receivingMode'] = req_obj.ServiceRequest.values['ReceivingMode'];
	delete req_obj.ServiceRequest.values['ReceivingMode'];
	req_obj.ServiceRequest.values['status'] = req_obj.ServiceRequest.values['ComplaintStatus'];
	delete req_obj.ServiceRequest.values['ComplaintStatus'];
	req_obj.ServiceRequest.values['locationName'] = req_obj.ServiceRequest.values['LocationName'];
	delete req_obj.ServiceRequest.values['LocationName'];
	if($("#approvalDepartment").val())
		req_obj.ServiceRequest.values['departmentName'] = $("#approvalDepartment option:selected").text();
	if($("#approvalPosition").val())
		req_obj.ServiceRequest.values['assignment_id'] = $("#approvalPosition").val();
	//console.log(JSON.stringify(req_obj));
	$.ajax({
		url: "/pgr/seva?jurisdiction_id=ap.public",
		type : 'PUT',
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
					url: "/filestore/files",
					type : 'POST',
					async : false,
					// THIS MUST BE DONE FOR FILE UPLOADING
					contentType: false,
					processData : false,
					data : formData,
					success: function(fileresponse){
						//console.log('file upload success');
						bootbox.alert('Grievance updated succesfully!', function(){ 
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
				bootbox.alert('Grievance updated succesfully!', function(){ 
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

function complaintType(loadDD){
	$.ajax({
		url: "/pgr/services?type=all&tenantId=ap.public",
		async : false
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data,
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
		});
	});
}

function nextStatus(loadDD){
	$.ajax({
		url: "/pgr/_getnextstatuses?userId="+localStorage.getItem("id")+"&currentStatus="+status+"&tenantId=ap.public",
		type : 'POST',
		async : false
	}).done(function(data) {
		loadDD.load({
			element:$('#status'),
			data:data,
			keyValue:'name',
			keyDisplayName:'name'
		});
	});
}

function getWard(loadDD){
	$.ajax({
		url: "/v1/location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?boundaryTypeName=Ward&hierarchyTypeName=Administration",
		type : 'POST',
		async : false
	}).done(function(data) {
		loadDD.load({
			element:$('#ct-sel-jurisd'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

function getLocality(boundaryId){
	$.ajax({
		url: "/v1/location/boundarys/childLocationsByBoundaryId?boundaryId="+boundaryId,
		type : 'POST',
		async : false
	}).done(function(data) {
		loadDD.load({
			element:$('#location'),
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
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
		type : 'POST'
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
		type : 'POST'
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