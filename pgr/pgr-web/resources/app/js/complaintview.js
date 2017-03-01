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
var srn = getParameterByName('srn');
var lat, lng, myCenter;
//var tenantId = getParameterByName('tenantId');

$(document).ready(function()
{

	if(localStorage.getItem('type') == 'CITIZEN'){
		$('.employee-action').remove();
	}else if(localStorage.getItem('type') == 'EMPLOYEE'){
		$('.citizen-action').remove();
	}else{
		$('.action-section').remove();
	}

	/*$("#btn_submit").click(function(){
		if($("#btn_submit").name=='Close')
			{
			return true;
			}
		if($('#inc_messge').val() == '')
		{
			$('#inc_messge').addClass('error');
		}else
		{
			document.forms[0].submit();
			$('#inc_messge').removeClass('error');
		}
		
	});*/
	
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

	$('#update-complaint').click(function(){
		/*if($('form').valid()){
			console.log('ready to update!')
		}*/
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
				beforeSend : function(){
					showLoader();
				},
				// THIS MUST BE DONE FOR FILE UPLOADING
				contentType: false,
				processData : false,
				data : formData,
				success: function(fileresponse){
					//console.log('file upload success');
				},
				error: function(){
					bootbox.alert('Media file failed!');
				},
				complete : function(){
					hideLoader();
					//console.log('Complete function called!');
				}
			});
		}
	});
	
	/*$('#ct-sel-jurisd').change(function(){
		//console.log("came jursidiction"+$('#ct-sel-jurisd').val());
		$.ajax({
			url: "/pgr/ajax-getChildLocation",
			type: "GET",
			data: {
				id : $('#ct-sel-jurisd').val()
			},
			dataType: "json",
			success: function (response) {
				//console.log("success"+response);
				$('#location').empty();
				
				$('#location').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					
				     $('#location').append($('<option>').text(value.name).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				//console.log("failed");
			}
		});
	});*/
	
	/*$('#approvalDepartment').change(function(){
		$.ajax({
			url: "/pgr/ajax-approvalDesignations",     
			type: "GET",
			data: {
				approvalDepartment : $('#approvalDepartment').val()   
			},
			dataType: "json",
			success: function (response) {
				//console.log("success"+response);
				$('#approvalDesignation').empty();
				$('#approvalDesignation').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalDesignation').append($('<option>').text(value.name).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				//console.log("failed");
			}
		});
	});*/
	
	/*$('#approvalDesignation').change(function(){
		$.ajax({
			url: "/pgr/ajax-approvalPositions",     
			type: "GET",
			data: {
				approvalDesignation : $('#approvalDesignation').val(),
				approvalDepartment : $('#approvalDepartment').val()    
			},
			dataType: "json",
			success: function (response) {
				//console.log("success"+response);
				$('#approvalPosition').empty();
				$('#approvalPosition').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalPosition').append($('<option>').text(value.name).attr('value', value.id));  
				});
				
			}, 
			error: function (response) {
				//console.log("failed");
			}
		});
	});*/

	var headers = new $.headers();

	//console.log(headers.header)

	$.ajax({
		url: "/pgr/seva?jurisdiction_id=2&service_request_id="+srn,
		headers : headers.header,
		beforeSend : function(){
			showLoader();
		},
		success : function(response){
			//console.log('Get complaint done!');

			if(response.service_requests.length == 0){
				bootbox.alert('Not a valid SRN!');
				hideLoader();
				return;
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

							if(localStorage.getItem('type') == 'CITIZEN' && response.service_requests[0].values.ComplaintStatus != 'COMPLETED')
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
							if(localStorage.getItem('type') == 'EMPLOYEE'){
								var loadDD = new $.loadDD();
								complaintType(loadDD);
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
	
});

function complaintType(loadDD){
	$.ajax({
		url: "/pgr/services?type=all&tenantId=ap.public",
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data,
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
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
