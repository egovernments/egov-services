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
//var tenantId = getParameterByName('tenantId');

$(document).ready(function()
{

	$("#btn_submit").click(function(){
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
		
	});
	
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
	
	$('#ct-sel-jurisd').change(function(){
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
	});
	
	$('#approvalDepartment').change(function(){
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
	});
	
	$('#approvalDesignation').change(function(){
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
			//console.log('Get complaint done!');

			if(response.service_requests.length == 0){
				bootbox.alert('Not a valid SRN!');
				hideLoader();
				return;
			}

			//console.log(JSON.stringify(response));
			$.ajax({
				url: "/filestore/files?tag="+srn,
				type : 'GET',
				success : function(fileresponse){
					//console.log(fileresponse.files)

					/*
					//History
					$.ajax({
						url : "/workflow/history?workflowId=122",
						type : 'GET',
						success : function(work_response){

							console.log(JSON.stringify(work_response));

							var wf_response = {};
							wf_response['workflow'] = work_response;

							var wf_source   = $("#wfcomplaint-script").html();
							var wf_template = Handlebars.compile(wf_source);
							$('.wfcomplaint').append(wf_template(wf_response));

							var com_source   = $("#viewcomplaint-script").html();
							var com_template = Handlebars.compile(com_source);
							$('.viewcomplaint').append(com_template(response));
						},
						error:function(){
							bootbox.alert('Workflow Error!');
						},
						complete : function(){
							hideLoader();
						}
					});*/
					response['files'] = fileresponse.files;
					var source   = $("#viewcomplaint-script").html();
					var template = Handlebars.compile(source);
					//response['service_requests'][0]['customLocation'] = response['service_requests'][0]['values'].ChildLocationName+' - '+response['service_requests'][0]['values'].LocationName;
					$('.viewcomplaint').append(template(response));
					//console.log(JSON.stringify(response));

					var dobj = new Date().toISOString();
				    var dat = dobj.split('T')[0].split("-").reverse().join("-");
				    var tm = dobj.split('T')[1];
				    var tim = tm.substring(0,tm.indexOf("."));
					$('.currDate').html( dat+' '+tim);
				},
				error : function(){
					bootbox.alert('Media file Error!');
				},
				complete : function(){
					hideLoader();
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
	
});

function getParameterByName(name, url) {
    if (!url) {
      url = window.location.href;
    }
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
