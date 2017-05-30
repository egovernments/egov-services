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
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;

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

		doCheckUser(),

		/*load top 5 complaint types*/
		topComplaintTypes()

	).then(function() {
		//Hide Loader
		hideLoader();
	});

	/*Productization - Aslam*/
	 
	$('#complaintTypeCategory').change(function() {
		if ($(this).val()) {
			$.ajax({
				url: "/pgr/services/_search?type=category&categoryId="+$(this).val()+"&tenantId="+tenantId,
				type : 'POST',
				data : JSON.stringify(requestInfo),
				dataType: 'json',
				processData : false,
				contentType: "application/json",
				async :false,
			}).done(function(data) {
				loadDD.load({
					element:$('#complaintType'),
					data:data.complaintTypes,
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

		var nameTitle = translate('core.lbl.fullname');
		var mobTitle = translate('core.lbl.mobilenumber');
		var emailTitle = translate('core.lbl.email.compulsory');
		var addressTitle = translate('core.lbl.address');
		var tCompTitle = translate('pgr.lbl.grievance.types');
		var categoryTitle = translate('pgr.lbl.grievance.category');
		var typeTitle = translate('pgr.lbl.grievance.type');
		var detailsTitle = translate('pgr.lbl.grievancedetails');
		var uploadTitle = translate('core.lbl.upload.photoimage');
		var locationTitle = translate('pgr.lbl.grievance.location');
		var lmTitle = translate('core.lbl.landmark');
		var captchaTitle = translate('');
		var cgTitle = translate('pgr.title.create.grievence');

		var nameContent = translate('core.error.fullname.blank');
		var mobContent = translate('core.lbl.enter.mobilenumber');
		var emailContent = translate('core.error.valid.email');
		var addressContent = translate('core.error.residential.address');
		var tCompContent = translate('pgr.error.select.grievance.categoryandtype');
		var categoryContent = translate('pgr.error.select.grievance.category');
		var typeContent = translate('pgr.error.select.grievance.type');
		var detailsContent = translate('pgr.error.describe.grievance.details');
		var uploadContent = translate('pgr.error.upload.photovideo');
		var locationContent = translate('pgr.error.select.grievancelocation');
		var lmContent = translate('pgr.error.landmark.optional');
		var captchaContent = translate('');
		var cgContent = translate('pgr.msg.finalgrievance.submit');

		var tour = new Tour({
			  steps: [
	          {
			    element: "#first_name",
			    title: nameTitle,
			    content: nameContent
			  },
			  {
			    element: "#phone",
			    title: mobTitle,
			    content:mobContent
			  },
			  {
			    element: "#email",
			    title: "Email ID",
			    content: emailContent
			  },
			  {
			    element: "#complainantAddress",
			    title: addressTitle,
			    content: addressContent
			  },
			  {
			    element: "#topcomplaint",
			    title: "Top Grievance Types",
			    content: tCompContent
			  },
			  {
			    element: "#complaintTypeCategory",
			    title: "Grievance Category",
			    content: categoryContent
			  },
			  {
			    element: "#complaintType",
			    title: "Grievance Type",
			    content: typeContent
			  },
			  {
			    element: "#doc",
			    title: detailsTitle,
			    content: detailsContent
			  },
			  {
			    element: "#upload-section",
			    title: uploadTitle,
			    content: uploadContent
			  },
			  {
			    element: "#location-tour",
			    title: locationTitle,
			    content: locationContent
			  },
			  {
			    element: "#landmarkDetails",
			    title: lmTitle,
			    content: lmContent
			  },
			  {
			    element: "#captcha",
			    title: "Captcha",
			    content: "Click on the checkbox to validate captcha!"
			  },
			  {
			    element: "#create-griev",
			    title: cgTitle,
			    content: cgContent
			  }],
			  storage: false,
			  duration: 6000,
			  onShown: function (tour) {
				  //console.log(tour.getCurrentStep());
				  var step = tour.getCurrentStep();
				  if(step == 0){
					  typingfeel('James Jackson', '#first_name');
				  }else if(step == 1){
					  typingfeel('9988776655', '#phone');
				  }else if(step == 2){
					  typingfeel('james.jackson@gmail.com', '#email');
				  }else if(step == 3){
					  typingfeel('Colorado U.S', '#complainantAddress');
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

	/*$('.captcha-section .fa-refresh').click(function(){
		captcha.refreshCaptcha();
	});*/

	$('#create-griev').click(function(){
		
		if($('form').valid()){
			//if(captcha.validateCaptcha($('#captcha').val())){
				//ajax to submit
				var obj = $(this);
				obj.attr("disabled", "disabled");
				var $form = $("form");
				$('#complaintType').removeAttr("disabled");
				var data = getFormData($form);

				data['serviceRequestId'] = '';
				data['firstName'] = $('#first_name').val() ? $('#first_name').val() : userName;
				data['phone'] = $('#phone').val() ? $('#phone').val() : userMobile;
				data['email'] = $('#email').val() ? $('#email').val() : userEmail;
				data['status'] = true;
				data['serviceName'] = $('#complaintType option:selected').text() ? $('#complaintType option:selected').text() : '';
				data['requestedDatetime'] = "";
				data['mediaUrl'] = "";
				data['tenantId'] = tenantId;
				data["isAttribValuesPopulated"]=true;

				data['attribValues'] = [];
				var finobj = {};
				finobj = {
				    key: 'receivingMode',
				    name: $('#receivingMode').val() ? $('#receivingMode').val() : 'Website'
				};
				data['attribValues'].push(finobj);
				finobj = {
				    key: 'receivingCenter',
				    name: $('#receivingCenter').val() ? $('#receivingCenter').val() : ''
				};
				data['attribValues'].push(finobj);
				finobj = {
				    key: 'status',
				    name: 'REGISTERED'
				};
				data['attribValues'].push(finobj);
				finobj = {
				    key: 'requesterAddress',
				    name: $('#complainantAddress').val() ? $('#complainantAddress').val() : ''
				};
				data['attribValues'].push(finobj);
				finobj = {};
				finobj = {
				    key: 'keyword',
				    name:'Complaint'
				};
				data['attribValues'].push(finobj);
				var request = {};
				request['RequestInfo'] = RequestInfo.requestInfo;
				request['serviceRequest'] = data;

				//console.log(JSON.stringify(request));

				$.ajax({
					url: "/pgr/seva/_create",
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
							formData.append('tenantId', tenantId);
							formData.append('module', 'PGR');
							formData.append('tag', response.serviceRequests[0].serviceRequestId);
							// Main magic with files here

							$('input[name=file]').each(function(){
								var file = $(this)[0].files[0];
								formData.append('file', file); 
							});

							$.ajax({
								url: "/filestore/v1/files",
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
							obj.removeAttr("disabled");
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
			/*}else{
				bootbox.alert("Captcha failed!", function(){ 
					captcha.refreshCaptcha();
					$('#captcha').focus(); 
				});
			}*/
		}else{
			//captcha.refreshCaptcha();
		}

	});

	$('.breadcrumb li a').click(function(){
		$('.acknowledgement, .breadcrumb').addClass('hide');
		$('.createcrn, .tour-section').removeClass('hide');
		$( "form" ).trigger('reset');
		hideReceivingCenter();
		clearimagepreview();
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
		url : "/pgr/receivingmode/_search?tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		success : function(response){
			loadDD.load({
				element:$('#receivingMode'),
				data:response.receivingModes,
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
		url : "/pgr/receivingcenter/_search?tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		success : function(response){
			loadDD.load({
				element:$('#receivingCenter'),
				data:response.receivingCenters,
				keyValue:'id',
				keyDisplayName:'name'
			});
			$('#recenter').removeClass('hide');
		},
		error: function(){
			bootbox.alert('Receiving center failed!')
		}
	})
}

function complaintCategory(){
	$.ajax({
		url: "/pgr/servicecategories/_search?tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		async : false,
		contentType: "application/json",
		success : function(data){
			loadDD.load({
				element:$('#complaintTypeCategory'),
				data:data.serviceTypeCategories,
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
		url: "/pgr/services/_search?type=frequency&count=5&tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		success : function(data){
			if(data.complaintTypes.length > 0){
				$('#topcomplaint').html('');
				$.each(data.complaintTypes,function(i,obj){
					if(obj.keywords.indexOf('complaint') > -1)
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