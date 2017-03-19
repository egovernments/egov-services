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
var type = localStorage.getItem("type");
var loadDD = new $.loadDD();
$(document).ready(function()
{
	/*Productization - Aslam*/
	$.when(
		
		/*Show loader*/
		showLoader(),

		loadBasedonType(),

		loadReceivingMode(),

		/*load grievance  category*/
		complaintCategory(),

		/*load top 5 complaint types*/
		topComplaintTypes()

	).then(function() {
		//Hide Loader
		hideLoader();
	});

	var captcha = new $.Captcha("#captchaCanvas","#captchaimage");

	/*Productization - Aslam*/
	 
	$('#complaintTypeCategory').change(function() {
		if ($(this).val()) {
			$.ajax({
				url: "/pgr/services?type=category&categoryId="+$(this).val()+"&tenantId=ap.public",
				async :false,
			}).done(function(data) {
				loadDD.load({
					element:$('#complaintType'),
					data:data,
					keyValue:'serviceCode',
					keyDisplayName:'serviceName'
				});
			});
		}else{
			$('#complaintType').empty().append($("<option />").val('').text('Select'));
		}
	});

	//typeahead initialize
	var locationtypeahead = new $.typeahead({
		url : '/v1/location/boundarys/getLocationByLocationName?locationName=%QUERY',
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
	
	$('.freq-ct').click(function(){ 
		$('#complaintTypeName').typeahead('val',$(this).html().trim());
		$("#complaintTypeName").trigger('blur');
	});

	$('#receivingMode').change(function(){
		if($(this).val() == 'MANUAL'){
			loadReceivingCenter();
			$('#recenter').removeClass('hide');
			$('#receivingCenter').attr({
				name : 'receivingCenter',
				required : 'required'
			});
		}else{
			hideReceivingCenter();
		}
	});
	
	$('.tour-section').click(function(){
		$('.demo-class').modal('show', {backdrop: 'static'});
		var tour = new Tour({
			  steps: [
	          {
			    element: "#f-name",
			    title: "Name",
			    content: "Enter your full name!"
			  },
			  {
			    element: "#mob-no",
			    title: "Mobile Number",
			    content: "Enter your valid 10 digit mobile number!"
			  },
			  {
			    element: "#email",
			    title: "Email ID",
			    content: "Enter your valid email id!"
			  },
			  {
			    element: "#address",
			    title: "Address",
			    content: "Enter your present residential address!"
			  },
			  {
			    element: "#topcomplaint",
			    title: "Top Grievance Types",
			    content: "Select your grievance from here or else choose it from below grievance category and grievance type!"
			  },
			  {
			    element: "#complaintTypeCategory",
			    title: "Grievance Category",
			    content: "Select your grievance category!"
			  },
			  {
			    element: "#complaintType",
			    title: "Grievance Type",
			    content: "Select your grievance type!"
			  },
			  {
			    element: "#doc",
			    title: "Grievance Details",
			    content: "Describe your grievance details briefly!"
			  },
			  {
			    element: "#upload-section",
			    title: "Upload Photograph / Video",
			    content: "Upload grievance relevated photo / video (Max : 3 files)!"
			  },
			  {
			    element: "#location-tour",
			    title: "Grievance Location",
			    content: "Enter your grievance location or click on the location icon to select your desired location from the map!"
			  },
			  {
			    element: "#landmarkDetails",
			    title: "Landmark",
			    content: "Enter your landmark (if any)!"
			  },
			  {
			    element: "#captcha-section iframe",
			    title: "Captcha",
			    content: "Click on the checkbox to validate captcha!"
			  },
			  {
			    element: "#create-griev",
			    title: "Create Grievance",
			    content: "Finally, Click here to submit your grievance!"
			  }],
			  storage: false,
			  duration: 6000,
			  onShown: function (tour) {
				  //console.log(tour.getCurrentStep());
				  var step = tour.getCurrentStep();
				  if(step == 0){
					  typingfeel('James Jackson', '#f-name');
				  }else if(step == 1){
					  typingfeel('9988776655', '#mob-no');
				  }else if(step == 2){
					  typingfeel('james.jackson@gmail.com', '#email');
				  }else if(step == 3){
					  typingfeel('Colorado U.S', '#address');
				  }else if(step == 4){
					  //typingfeel('Colorado U.S', '#address');
				  }else if(step == 5){
					  $('#complaintTypeCategory').val('1').attr("selected", "selected");
				  }else if(step == 6){
					  $('<option>').val('1').text('Absenteesim of sweepers').appendTo('#complaintType');
					  $('#complaintType').val('1').attr("selected", "selected");
				  }else if(step == 7){
					  typingfeel('Dog menace in madiwala', '#doc');
				  }else if(step == 9){ 
				    typingfeelintypeahead('Rev','#location','Revenue, Zone-4, Srikakulam  Municipality');
				  }else if(step == 10){
					  typingfeel('Spencer Plaza', '#landmarkDetails');
				  }
			  },
			  onEnd: function (tour) {
				  location.reload();
			  },
			  template : "<div class='popover tour'> <div class='arrow'></div> <h3 class='popover-title'></h3> <div class='popover-content'></div> </nav> </div>"
		});
		// Initialize the tour
		tour.init();
		// Start the tour
		tour.start();
	    
	});

	$('.captcha-section .fa-refresh').click(function(){
		captcha.refreshCaptcha();
	});

	$('#create-griev').click(function(){

		var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
		
		if($('form').valid()){
			if(captcha.validateCaptcha($('#captcha').val())){
				//ajax to submit
				var obj = $(this);
				obj.attr("disabled", "disabled");
				var $form = $("form");
				var data = getFormData($form);

				data['service_request_id'] = '';
				data['first_name'] = $('#first_name').val() ? $('#first_name').val() : '';
				data['phone'] = $('#phone').val() ? $('#phone').val() : '';
				data['email'] = $('#email').val() ? $('#email').val() : '';
				data['status'] = true;
				data['service_name'] = $('#complaintType option:selected').text();
				data['requested_datetime'] = "";
				data['media_url'] = "";
				data['tenantId'] = 'ap.public';

				var finobj = {};
				finobj['receivingMode'] = $('#receivingMode').val() ? $('#receivingMode').val() : 'Website';
				finobj['receivingCenter'] = $('#receivingCenter').val() ? $('#receivingCenter').val() : '';
				finobj['status'] = 'REGISTERED';
				if(localStorage.getItem("type") == 'CITIZEN')
					finobj['userId'] = localStorage.getItem('id');
				finobj['complainantAddress'] = $('#complainantAddress').val() ? $('#complainantAddress').val() : '';

				data['values'] = finobj;

				//console.log(data);

				//console.log(RequestInfo.requestInfo);

				var request = {};
				request['RequestInfo'] = RequestInfo.requestInfo;
				request['ServiceRequest'] = data;

				//console.log(JSON.stringify(request));

				$.ajax({
					url: "/pgr/seva?jurisdiction_id=ap.public",
					type : 'POST',
					dataType: 'json',
					processData : false,
					contentType: "application/json",
					beforeSend : function(){
						showLoader();
					},
					data : JSON.stringify(request),
					success : function(response){
						//console.log(JSON.stringify(response));
						//console.log('complaint Created');

						var filefilledlength = Object.keys(filefilled).length;

						if(filefilledlength > 0){
							//AJAX Fileupload
							var formData=new FormData();
							formData.append('jurisdictionId', 'ap.public');
							formData.append('module', 'PGR');
							formData.append('tag', response.service_requests[0].service_request_id);
							// Main magic with files here

							$('input[name=file]').each(function(){
								var file = $(this)[0].files[0];
								formData.append('file', file); 
							});

							$.ajax({
								url: "/filestore/files",
								type : 'POST',
								// THIS MUST BE DONE FOR FILE UPLOADING
	    						contentType: false,
								processData : false,
								data : formData,
								success: function(fileresponse){
									//console.log('file upload success');
									//Ack page shown
									doAck(response);
								},
								error: function(){
									bootbox.alert('Media file not uploaded!');
								},
								complete : function(){
									hideLoader();
									obj.removeAttr("disabled");
									//console.log('Complete function called!');
								}
							});
						}else{
							//console.log('File is empty');
							//Ack page shown
							doAck(response);
							hideLoader();
						}
					},
					error : function(jqXHR, textStatus){
						bootbox.alert( "Create grievance failed!" );
						obj.removeAttr("disabled");
						hideLoader();
						$('.acknowledgement, .breadcrumb').addClass('hide');
						$('.createcrn, .tour-section').removeClass('hide');
					}
				});
			}else{
				bootbox.alert("Captcha failed!", function(){ 
					captcha.refreshCaptcha();
					$('#captcha').focus(); 
				});
			}
		}else{
			captcha.refreshCaptcha();
		}

	});

	$('.breadcrumb li a').click(function(){
		$('.acknowledgement, .breadcrumb').addClass('hide');
		$('.createcrn, .tour-section').removeClass('hide');
		$( "form" ).trigger('reset');
		hideReceivingCenter();
		clearimagepreview();
		captcha.refreshCaptcha();
		$(this).parent().nextAll('li').remove();
	});

	$(document).on('click','.freq-ct',function(){
		$("#complaintTypeCategory").val($(this).data('category'));
		$("#complaintTypeCategory").trigger('change');
		$('#complaintType').val($(this).data('type'));
	});

});

function hideReceivingCenter(){
	$('#recenter').addClass('hide');
	$('#receivingCenter').removeAttr('name required');
}

function doAck(response){
	$('.breadcrumb').append('<li class="active" data-translate="ack">Acknowledgement</li>');
	localization();
	$('.acknowledgement, .breadcrumb').removeClass('hide');
	$('.acknowledgement #firstname').html('Dear '+response.service_requests[0].first_name+',');
	$('.acknowledgement #crn').html(response.service_requests[0].service_request_id);
	$('.createcrn, .tour-section').addClass('hide');
	//console.log('Complaint Acknowledgement Done');
}

function typingfeel(text, input){
	$.each(text.split(''), function(i, letter){
        setTimeout(function(){
            //we add the letter to the container
            $(input).val($(input).val() + letter);
        }, 200*(i+1));
    });
}

function typingfeelintypeahead(text, input, typeaheadtext){
	//text is split up to letters
    $.each(text.split(''), function(i, letter){
        setTimeout(function(){
            //we add the letter to the container
            $(input).val($(input).val() + letter);
            $(input).trigger("input");
            $("span.twitter-typeahead .tt-suggestion > p").mouseenter();
            if(i == 2)
            { 
            	$(input).typeahead('val',typeaheadtext); 
            	$(input).blur(); 
        	}
        }, 1000*(i+1));
    });
}

function loadReceivingMode(){
	$.ajax({
		url : "/pgr/receivingmode?tenantId=ap.public",
		success : function(response){
			loadDD.load({
				element:$('#receivingMode'),
				data:response,
				keyValue:'code',
				keyDisplayName:'name'
			});
			$('.receivingModeSection').removeClass('hide');
		},
		error: function(){
			bootbox.alert('Receiving mode failed!')
		}
	});
}

function loadReceivingCenter(){
	$.ajax({
		url : "/pgr/receivingcenter?tenantId=ap.public",
		success : function(response){
			loadDD.load({
				element:$('#receivingCenter'),
				data:response,
				keyValue:'id',
				keyDisplayName:'name'
			});
			$('#recenter').removeClass('hide');
		},
		error: function(){
			bootbox.alert('Receiving mode failed!')
		}
	})
}

function complaintCategory(){
	$.ajax({
		url: "/pgr/complaintTypeCategories?tenantId=ap.public",
		success : function(data){
			loadDD.load({
				element:$('#complaintTypeCategory'),
				data:data,
				keyValue:'id',
				keyDisplayName:'name'
			});
		},
		error: function(){
			//bootbox.alert('Error!');
		}
	})
}

function topComplaintTypes(){
	$.ajax({
		url: "/pgr/services?type=frequency&count=5&tenantId=ap.public",
		success : function(data){
			if(data.length > 0){
				$('#topcomplaint').html('');
				$.each(data,function(i,obj){
					$('#topcomplaint').append('<a href="javascript:void(0)" data-type="'+obj.serviceCode+'" data-category="'+obj.groupId+'" class="btn btn-secondary btn-xs tag-element freq-ct" data-toggle="popover" title="Click to select the Grievance category and type">'+obj.serviceName+'</a>')
				});
			}else{
				$('#topcomplaintsection').hide();
			}
		},
		error: function(){
			//bootbox.alert('Error!');
		}
	})
}

function loadBasedonType(){
	if(type == 'CITIZEN'){//citizen
		$('.citizenremove').remove();
	}else if(type == 'EMPLOYEE'){//employee
		
	}else{//anonymous
		$('.officialremove').remove();
	}
}