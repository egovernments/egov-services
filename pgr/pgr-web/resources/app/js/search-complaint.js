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
 var tableContainer = $("#complaintSearchResults");
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

		loadStatus()

	).then(function() {
		//Hide Loader
		hideLoader();
	});

    $('#toggle-searchcomp').click(function () {
        if ($(this).html() == "More..") {
            $(this).html('Less..');
            $('.show-searchcomp-more').show();
			} else {
            $(this).html('More..');
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
	    	bootbox.alert('Atleast one search criteria is required!');
	    	return;
	    }

	    var searchURL = '/pgr/seva?jurisdiction_id=2&'+formData;
    	var headers = new $.headers();

    	tableContainer = $("#complaintSearchResults").DataTable( {
    		"ajax": {
	            "url": searchURL,
	            headers : headers.header,
	            "dataSrc": "service_requests",
	            beforeSend : function(){
					showLoader();
				},
				error: function(){
					bootbox.alert('Error loading data!')
				},
				complete : function(){
					hideLoader();
				}
	        },
			destroy:true,
			autoWidth: false,
			"aaSorting": [],
			dom: "<'row'<'col-xs-12 pull-right'f>r>t<'row buttons-margin'<'col-md-3 col-xs-6'i><'col-md-3  col-xs-6'l><'col-md-3 col-xs-6'B><'col-md-3 col-xs-6 text-right'p>>",
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
				{title: 'Complaint Number', data: 'service_request_id'},
				{title: 'Grievance Type', data: 'service_name'},
				{title: 'Name', data: 'first_name'},
				{title: 'Location', data: 'values.LocationName'},
				{title: 'Status', data: 'values.ComplaintStatus'},
				{title: 'Department', data: 'values.departmentName'},
				{title: 'Registration Date', data : 'requested_datetime'}
			]
	    });

	});

});

$("#complaintSearchResults").on('click','tbody tr',function(event) {
	var srn = tableContainer.row( this ).data().service_request_id;
	openPopUp('view-complaint.html?srn='+srn, srn);
});
 
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
		url : "/pgr/receivingmode?tenantId=ap.public",
		success : function(response){
			loadDD.load({
				element:$('#receivingMode'),
				data:response,
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
		url: "/pgr/services?type=all&tenantId=ap.public"
	}).done(function(data) {
		loadDD.load({
			element:$('#complaintType'),
			data:data,
			keyValue:'serviceCode',
			keyDisplayName:'serviceName'
		});
	});
}					

function loadStatus(){
	$.ajax({
		url: "/pgr/_statuses?tenantId=ap-public",
		type : 'POST'
	}).done(function(data) {
		loadDD.load({
			element:$('#status'),
			data:data,
			keyValue:'name',
			keyDisplayName:'name'
		});
	});
}

function loadWard(){
	$.ajax({
		url: "/v1/location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?boundaryTypeName=Ward&hierarchyTypeName=Administration",
		type : 'POST'
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