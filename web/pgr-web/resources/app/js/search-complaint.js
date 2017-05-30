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
var loadDD = new $.loadDD();
var keyword = getUrlParameter('keyword');
var tableContainer = $("#complaintSearchResults");
var complaintList, department, ward = [];
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
 $(document).ready(function() {

	$.when(
		
		/*Show loader*/
		showLoader(),

		//Location
		loadWard(),

		//load receiving mode
		loadReceivingMode(),

		/*load complaint types*/
		complaintType(),

		loadDeparment(),

		loadBoundarys(),

		loadStatus()

	).then(function() {
		//Hide Loader
		hideLoader();
	});

	if(keyword != 'Complaint'){
		$('.complaintSection').hide();
	}

    $('#toggle-searchcomp').click(function () {
        if ($(this).data('translate') == "core.lbl.more") {
            $(this).data('translate', 'core.lbl.less');
            $(this).html(translate('core.lbl.less'));
            $('.show-searchcomp-more').show();
		} else {
	        $(this).data('translate', 'core.lbl.more');
	        $(this).html(translate('core.lbl.more'));
	        $('.show-searchcomp-more').hide();
		}
		
	});

	$("#when_date").change(function () {
        populatedate($('#when_date').val());
	});

    $('#searchComplaints').click(function () {

    	var formData = $("form :input")
	    .filter(function(index, element) {
	        return $(element).val() != "";
	    }).serialize();// does the job!

	    if(formData.length == 0){
	    	bootbox.alert(translate('core.msg.criteria.required'));
	    	return;
	    }

	    var searchURL = '/pgr/seva/_search?tenantId='+tenantId+'&'+formData;

    	tableContainer = $("#complaintSearchResults").DataTable( {
    		"ajax": {
	            "url": searchURL,
	            type: 'POST',
	            contentType: "application/json",
	            processData : true,
	            data: function ( requestInfo ) {
			      return JSON.stringify( requestInfo );
			    },
	            "dataSrc": "serviceRequests",
	            beforeSend : function(){
					showLoader();
				},
				error: function(){
					bootbox.alert('Error loading data!')
				},
				complete : function(){
					$('#complaintSearchResults').removeClass('hide');
					hideLoader();
				}
	        },
			destroy:true,
			"deferRender": true,
			autoWidth: false,
			"aaSorting": [],
			dom: "<'row'<'col-xs-12 pull-right'f>r>t<'row buttons-margin'<'col-md-6  col-xs-6'l><'col-md-3 col-xs-6'B><'col-md-3 col-xs-6 text-right'p>>",
			buttons: [
			    'excel','print',
			    {
				    extend: 'pdf',
				    filename: 'Grievance List',
				    pageSize : 'LEGAL',
			        orientation : 'landscape',
				    exportOptions: {
				        columns: ':visible',
				    }
				}
			],
			columns: [
				{data: 'serviceRequestId'},
				{data: 'serviceName'},
				{data: 'firstName'},
				{"render": function ( data, type, full, meta ) {
					var wardname,localityname;
					for (var item of full.attribValues) {
						if(item['key']=='locationId')
							wardname = getBoundariesbyId(item['name']);
						else if(item['key']=='childLocationId')
							localityname = getBoundariesbyId(item['name']);
					}
					return wardname+' - '+localityname;
			    } },
				{data: 'values.status', "render": function ( data, type, full, meta ) {
					var st, kw;
					for (var item of full.attribValues) {
						if(item['key']=='status'){
							st =  item['name'];
						}else if(item['key']=='keyword'){
							kw =  item['name'];
						}
					}
					if(kw != 'Complaint'){
						var results = (JSON.parse(localStorage.getItem('status'))).filter(function (el) {
						  return el.code == st;
						});
						return results[0] ? results[0].name : st;
					}else
						return st;
			    }},
				{data: 'values.departmentId', "render": function ( data, type, full, meta ) {
					for (var item of full.attribValues) {
						if(item['key']=='departmentId')
							return getDepartmentbyId(item['name']);
					}
			    } },
				{data : 'requestedDatetime'},
				{"visible":false,"render": function ( data, type, full, meta ) {
					var wardname,localityname;
					for (var item of full.attribValues) {
						if(item['key']=='keyword')
							return item['name'];
					}
			    } },
			]
	    });

	});

});

$("#complaintSearchResults").on('click','tbody tr',function(event) {
	var srn = tableContainer.row( this ).data().serviceRequestId;
	openPopUp('view-complaint.html?srn='+srn, srn);
});

function loadDeparment(){
	$.ajax({
		url: "/egov-common-masters/departments/_search?tenantId="+tenantId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		department = data.Department;
	});
}

function getDepartmentbyId(departmentId){
	var depObj = department.filter(function( obj ) {
	  return obj.id == departmentId;
	});
	var value = (depObj[0]) ? (Object.values(depObj[0])[1]) : '-';
	return value;
}

function loadBoundarys(){
	$.ajax({
		url : '/egov-location/boundarys?boundary.tenantId='+tenantId,
		success : function(data){
			ward = data.Boundary;
		}
	})
}

function getBoundariesbyId(wardId){
	var wardObj = ward.filter(function( obj ) {
	  return obj.id == wardId;
	});
	var value = (wardObj[0]) ? (Object.values(wardObj[0])[1]) : '-';
	return value;
}
 
function populatedate(id) {
    var d = new Date();
    var quarter = getquarter(d);
    var start, end;
    switch (id) {
		
		case "lastsevendays":
		$("#end_date").datepicker("setDate", d);
		start = new Date(d.setDate((d.getDate() - 7)));
		var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
		$("#start_date").datepicker("setDate", start);
		break;
		
		case "lastthirtydays":
		$("#end_date").datepicker("setDate", d);
		start = new Date(d.setDate((d.getDate() - 30)));
		var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
		$("#start_date").datepicker("setDate", start);
		break;
		
		case "lastninetydays":
		$("#end_date").datepicker("setDate", d);
		start = new Date(d.setDate((d.getDate() - 90)));
		var start = new Date(start.getFullYear(), start.getMonth(), start.getDate());
		$("#start_date").datepicker("setDate", start);
		break;
		
		case "today":
		$("#end_date").datepicker("setDate", d);
		$("#start_date").datepicker("setDate", d);
		break;
		
		case "all":
		$("#end_date").val("");
		$("#start_date").val("");
		break;
		
	}
}

function getquarter(d) {
	if (d.getMonth() >= 0 && d.getMonth() <= 2) {
        quarter = 4;
		} else if (d.getMonth() >= 3 && d.getMonth() <= 5) {
        quarter = 1;
		} else if (d.getMonth() >= 6 && d.getMonth() <= 8) {
		quarter = 2;
		} else if (d.getMonth() >= 9 && d.getMonth() <= 11) {
		quarter = 3;
	}
	
    return quarter;
}

function loadReceivingMode(){
	$.ajax({
		url : "/pgr/receivingmode/_search?tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		success : function(response){
			loadDD.load({
				element:$('#receivingMode'),
				placeholder : 'Select Receiving Mode',
				data:response.receivingModes,
				keyValue:'id',
				keyDisplayName:'name'
			});
		},
		error: function(){
			bootbox.alert('Receiving mode failed!')
		}
	});
}

function complaintType(){
	$.ajax({
		url: "/pgr/services/_search?type=all&tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json"
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data.complaintTypes,
			placeholder : 'Select Complaint Type',
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
		});
	});
}					

function loadStatus(){
	var appendURL = '';
	
	if(keyword != 'Complaint')
		appendURL = '&keyword=Deliverable_service';
	else
		appendURL = '';
	$.ajax({
		url: "/workflow/v1/statuses/_search?tenantId="+tenantId+appendURL,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#status'),
			placeholder : 'Select Status',
			data:data.statuses,
			keyValue:'code',
			keyDisplayName:'name'
		});
	});
}

function loadWard(){
	$.ajax({
		url: "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?boundaryTypeName=Ward&hierarchyTypeName=Administration&tenantId="+tenantId,
		type : 'POST',
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		loadDD.load({
			element:$('#ct-location'),
			placeholder : 'Select Location', // default - Select(optional)
			data:data.Boundary,
			keyValue:'id',
			keyDisplayName:'name'
		});
	});
}

$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
	if(keyword == "Complaint"){
		if(aData[7] == 'Complaint')
			return true;
		else
			return false;
	}else{
		if(aData[7] == 'Deliverable_service')
			return true;
		else
			return false;
	}
	
});