/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
var tableContainer, positionId, dt_date, dt_sender, dt_now, dt_status, dt_comments;
var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function()
{	

	var imgSrc = (tenantId == "default") ? "../resources/global/images/logo@2x.png"  : "../resources/global/images/panavel.png";
	var cityName = (tenantId == "default") ? "Kurnool" : "Panvel";
	
	$('.homepage_logo').attr('src',imgSrc)
	$('#hp-citizen-title').html(cityName);

	$('#new-pass').popover({ trigger: "focus",placement: "bottom"});
	
	$(document).on("keydown", disableRefresh);

	preventBack();
    
	$.fn.dataTable.moment( 'DD/MM/YYYY h:mm a' );
	
	$('.page-container.horizontal-menu header.navbar .navbar-right > li, .page-container.horizontal-menu header.navbar .navbar-right > li ul li').hover(
			function() {
				$(this).children('ul').show();
			},
			function() {
				$(this).children('ul').removeAttr('style');
				$(this).children('ul').hide();
	});

	dt_date = translate('core.lbl.date');
	dt_sender = translate('core.lbl.sender');
	dt_now = translate('core.lbl.natureofwork');
	dt_status = translate('core.lbl.status');
	dt_comments = translate('core.lbl.comments');
	
	//TODO not yet implemented at backend
	/*$('#feedback-form').on('submit', function(e){
        e.preventDefault();
        $.ajax({
                url: 'home/feedback/sent',
                type: 'GET',
                data: {'subject':$("#subject").val(),'message':$("#comment").val()},
                success: function(data) {
                	bootbox.alert("Your feedback successfully submitted.");
                },
                error: function() {
                        
                }, complete : function() {
                	$('.add-feedback').modal('hide');
                }
        });
        
	});*/

	var password = false;
	
	$('.check-password').blur(function(){
		if(($('#new-pass').val()!="") && ($('#retype-pass').val()!=""))
		{
			if ($('#new-pass').val() === $('#retype-pass').val()) {
					password = true;
					$('.password-error').hide();
				}else{
					password = false;
					$('.password-error').show();
					$('#retype-pass').addClass('error');
			}
		}
	});	

	$('#resetpassword').click(function(){
		
		$.validator.addMethod("passwordvalidate",function(value){
		    return /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[?!@$^*-`(){}])(?!.*[&<>#%"'/\\ ]).{8,32}$/.test(value);
		},translate('core.error.password'));

		jQuery.validator.addClassRules({
			passwordvalidate : { passwordvalidate : true }    
		});

		var reqObj = {};
		reqObj['RequestInfo'] = RequestInfo.requestInfo;
		reqObj['existingPassword'] = $('#old-pass').val();
		reqObj['newPassword'] = $('#new-pass').val();
		reqObj['tenantId'] = tenantId;

		if($('#password-form').valid() && password){
			$.ajax({
				url: '/user/password/_update',
				type : 'POST',
				processData : false,
				contentType: "application/json",
				data : JSON.stringify( reqObj ),
				success : function(response){
					$('.change-password').modal('hide');
					bootbox.alert(translate('core.msg.success.password.updated'));
				},
				error : function(xhr, status, error){
					$('.change-password').modal('hide');
					var response = JSON.parse(xhr.responseText);
					var errorMsg = response.error.fields[0].code;
					bootbox.alert(translate(errorMsg));
				}
			})
		}else{

		}
	});
	
	getPosition();
	getStatus();
	worklist();
	
	$("#official_inbox").on('click','tbody tr td i.inbox-history',function(e) {
		var tr = $(this).closest('tr');
		var row = tableContainer.row(tr);
		var stateId = tableContainer.row(row[0][0]).data()['values']['stateId'];
		$('.history-inbox').modal('show');
		var historyTableContainer = $("#historyTable"); 
		historyTableContainer.DataTable({
			"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-6 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-3 col-xs-6 text-right'p>>",
			"autoWidth": false,
			"paging": false,
			"destroy":true,
	        "aaSorting": [],
			"ajax": {
				"url": "/workflow/history?tenantId="+tenantId+"&workflowId="+stateId,
				"dataSrc":"",
			},
			"columns": [
						{ "title":dt_date, "data": "created_Date","width": "20%" },
						{ "title":dt_sender, "data": "owner","width": "15%" },
						{ "title":dt_now, "data": "natureOfTask","width": "20%" },
						{ "title":dt_status, "data": "status","width": "20%" },
						{ "title":dt_comments, "data": "comments","width": "20%" }
					]
		});
		
		e.stopPropagation();
	});
	
	$('.workspace').click(function(){
		$('.main-space').hide();
		$('.workspace').removeClass('active');
		clearnow();
		$('#'+$(this).attr('data-work')).find('input').val('');
		$(this).addClass('active');
		if($(this).attr('data-work') == 'worklist' ){
			focussedmenu = "worklist";
			worklist();
		}else if($(this).attr('data-work') == 'drafts' ){
			focussedmenu = "drafts";
			drafts();
		}else if($(this).attr('data-work') == 'notifications' ){
			focussedmenu = "notifications";
			notifications();
		}
		$('#'+$(this).attr('data-work')).show();
	});
	
	$('.search-table').keyup(function(){
		tableContainer.search( this.value ).draw();
	});

	$("#official_inbox").on('click','tbody tr',function(event) {
		var srn = tableContainer.row( this ).data()['serviceRequestId'];
		openPopUp('view-complaint.html?srn='+srn,srn)
	});
	
	$("#official_drafts").on('click','tbody tr',function(event) {
		if (tableContainer1.fnGetData(this,6) != undefined) {
			var windowObjectReference = window.open(tableContainer1.fnGetData(this,6), ''+tableContainer1.fnGetData(this,5)+'', 'width=900, height=700, top=300, left=150,scrollbars=yes'); 
			openedWindows.push(windowObjectReference);
			windowObjectReference.focus();
		}
	});

	$('#natureofwork').on('click', 'ul li a', function() {
		$('#natureofwork ul li').removeClass('active');
		$(this).parent().addClass('active');
		if($('#natureofwork ul li a[data-now=Reset]').length == 0){
			$('#natureofwork ul').append('<li role="presentation"><a href="javascript:void(0)" data-now=Reset><span><i class="fa fa-refresh"></i></span>Reset / Clear</a></li>');
		}
	    now = unescape($(this).data('now'));
	    now_json = [];
	    //console.log('Clicked item-->'+now);
	    refreshnow(now);
	    $('#inboxsearch').trigger('keyup');
	});
	
	//search menu item in tree
	$('.search_list').hide();//Initially hide search item section

	var menujson = [];
	var count = 0;
	var ind = 0;
	var offsetht = 0;
	var offsetbottomht = 0;
	var set = 0;
    
	$('#searchtree').on('keyup', function(e){
		
		switch(e.keyCode) {
    	
	        case 38: /// up arrow
	        	e.preventDefault();
	        	count = (count - 1) > 0 ? (count - 1) : 0;
                ind = (count - 1) > 0 ? (count - 1) : 0;
                //console.log('Count:'+count+'<--->Index:'+ind);
                if(count > 0){
                    $('.ullist li').removeClass('focus');
                    $('.ullist li').eq(ind).addClass('focus');
                    //console.log('Top offset:'+$('.ullist li.focus').offset().top+'<--->List top:'+$('.list').position().top );
                    if($('.ullist li.focus').offset().top <= 68){
						offsetbottomht = (($('.list').position().top + ($(window).height() -63 - 48 -29)) > 0) ? 0 : ($('.list').position().top + ($(window).height() -63 - 48 -29));
						$('.list').animate({ top : offsetbottomht }, 500);
						set = 0;
						offsetht = 0;
                    }
                }
                break;
                
	        case 40: // down arrow
	        	e.preventDefault();
	        	// Store the reference to our top level link
	        	var link = $(this);

	        	// Find the ul li element that acts as the search item
	        	var dropdown = link.parent('.search').parent('.page-container.horizontal-menu').find('.ullist li');
	        	
                // If there is a UL available, place focus on the first focusable element within
                if(dropdown.length > 0){
                    // Make sure to stop event bubbling
                    if(count >= dropdown.length){
                    	//console.log('Bottom failed');
                    }else{
                    	count = count + 1;
	                    ind = count - 1;
	                    $('.ullist li').removeClass('focus');
	                    //console.log('Count:'+count+'<--->Index:'+ind);
	                    $('.ullist li').eq(ind).addClass('focus');
	                    //console.log('Top offset:'+$('.ullist li.focus').offset().top+'<--->Window Height:'+($(window).height() - 29 - 30));
	                    if($('.ullist li.focus').offset().top > ($(window).height() - 60)){
		                    set = $('.ullist li.focus').offset().top -63 -48;
		                    offsetht += set;
	                    	$('.list').animate({ top: -(offsetht)+'px' }, 500);
	                    }
                    }
                }
                break;

			case 13: /// enter key
	        	$('.ullist').find('li.focus a').click();
                break;

			case 27: /// Escape key
				$('#searchtree').val('');
				$('#searchtree').trigger('blur');
				clearsearchlist();
                break;

            default : //Logic for search menu tree
            	menujson = [];
           	    count = 0;
				ind = 0;
				$('.list').css('top','0px');
				
				if($(this).val().length > 3){
					
					var result = getObject(menuItems, $(this).val());
					//console.log('Menu JSON:'+JSON.stringify(result));
					
					$('.search_list .list ul').html('');
					
					//Load dropdown values withrespect to json
					if ( result.length == 0 ) {
						$('.search_list').hide();
					}else{
						searchlist_height = $( window ).height() -63 -49 -29;
						$('.search_list').show();
						$('.search_list').height(searchlist_height);
						$.each(result, function(k, v) {
							$('.search_list .list ul').append('<li><a href='+v.link+' class="open-popup" data-strwindname='+v.id+'>'+v.name+'</a></li>');
						});
						//console.log($('.list').innerHeight());
						if($('.list').innerHeight() <= $('.search_list').innerHeight()){
							$('.search_list').css('overflow-y', 'hidden');
						}else{
							$('.search_list').css('overflow-y', 'scroll');
						}
					}
				}else{
					//Menu JSON empty when seach key length less than 3
					clearsearchlist();
				}
            	break;
                
        }
	});

	$(document).on('focus', '#searchtree', function(){
		 $('.searchicon').hide();
	}).on('blur', '#searchtree', function(){
		if($(this).val().length > 0){
			$('.searchicon').hide();
		}else{
			$('.searchicon').show();
		}
	});
	
	$('.searchicon').click(function(){
		$(this).hide();
		$('#searchtree').focus();
	});

	//prevent cursor from moving while using arrow keys
	$('input').bind('keydown', function(e){
	    if(e.keyCode == '38' || e.keyCode == '40'){
	        e.preventDefault();
	    }
	});
	
	function getObject(theObject, searchkey) {
		searchkey = searchkey.toLowerCase();
	    var result = null;
	    if(theObject instanceof Array) {
	        for(var i = 0; i < theObject.length; i++) {
	            result = getObject(theObject[i], searchkey);
	        }
	    }
	    else
	    {
	        for(var prop in theObject) {
	            //console.log(prop + ': ' + theObject[prop]);
	            if(prop == 'name') {
	                if (theObject[prop].toLowerCase().indexOf(searchkey) >= 0){
	                	if(theObject.link != 'javascript:void(0);' && theObject.icon != 'fa fa-times-circle remove-favourite'){
	                		var obj = {};
	                		obj['id'] = theObject.id;
	                		obj['name'] = theObject.name;
	                		obj['link'] = theObject.link;
	                		menujson.push(obj);
	                		return theObject;
	                	}
	                }
	            }
	            if(theObject[prop] instanceof Object || theObject[prop] instanceof Array){
		            //console.log('came for inner object iteration');
	            	result = getObject(theObject[prop], searchkey);
	            }
	        }
	    }
	    return menujson;
	}

	function clearsearchlist(){
		menujson = [];
		//Show No results in dropdown or hide it
		$('.search_list').hide();
		$('.list ul').html('');
		$('.list').css('top','0px');
		offsetht = 0;
		offsetbottomht = 0;
	}
	
	
	$("#official_inbox, #official_drafts").on('click','tbody tr td span.details',function(e) {
		$(this).parent().html($(this).data('text'));
		e.stopPropagation();
		e.preventDefault();
	});
	
	$('#inboxsearch, #draftsearch').keyup(function(e) {
	     if (e.keyCode == 27) { // escape key maps to keycode `27`
	        $(this).val('');
	        $('#'+$(this).attr('id')).trigger('keyup');
	    }
	});

});

var response_json= [];
var counts = {};
var now_json = [];
var now_name=[];

function clearnow(){
	$('#natureofwork').html('');
	response_json= [];
	counts = {};
	now_json = [];
	now_name = [];
}
//common ajax functions for worklist, drafts and notifications 
function worklist(){
	if(!positionId)
		return;

	tableContainer1 = $("#official_inbox"); 
	tableContainer = tableContainer1.DataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		"autoWidth": false,
        "aaSorting": [],
		"ajax": {
			url : "/pgr/seva/_search?tenantId="+tenantId+"&assignmentId="+positionId,
			type: 'POST',
			contentType: "application/json",
            processData : true,
            data: function ( requestInfo ) {
		      return JSON.stringify( requestInfo );
		    },
			dataSrc : "serviceRequests"
		},
		"columns": [
		{ "title": "Application No.", "data": "serviceRequestId","width": "15%" },
		{ "title": dt_date, "data": "requestedDatetime","width": "15%" },
		{ "title":dt_sender, "data": "firstName","width": "15%" },
		{ "title":dt_now, "width": "15%", "render": function ( data, type, full, meta ) {
			for (var item of full.attribValues) {
				if(item['key']=='keyword')
					return (item['name'] == 'Complaint' ? 'Grievance' : 'Service');
			}
	    } },
		{ "title":dt_status, "width": "15%", "render": function ( data, type, full, meta ) {
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
				return results[0] ? results[0].name.toUpperCase() : st;
			}else
				return st;
	    } },
		{ "title":dt_comments, "width": "25%", "render": function ( data, type, full, meta ) {
			var text = 'Service Request Number '+(full.serviceRequestId)+' for '+(full.serviceName)+' filed on '+(full.requestedDatetime)+'. Date of Resolution is '+(full.expectedDatetime);
			return text;
	    } },
		{ "data" : null, "target":-1,"defaultContent": '<i class="fa fa-history inbox-history history-size" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'},
		]/*,
		"columnDefs": [
               {
                   "render": function ( data, type, row ) {
                       return type === 'display' && data.length > 75 ? data.substr( 0, 75 )+' <span class="details" data-text="'+data+'"><button class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button</span>' : data;
                   },
                   "targets": 4
               }
           ] ,
		"fnInitComplete": function (oSettings, json) {
	          response_json = JSON.stringify(json.data);
	          //console.log('response--->'+response_json);
	          if(JSON.parse(response_json).length != 0){
		          $.each(JSON.parse(response_json), function(key, value) {
		        	  if (!counts.hasOwnProperty(value.task)) {
		        		  counts[value.task] = 1;
	        		  } else {
	        			  counts[value.task]++;
	        		  }
		          });
		          //console.log('Count object'+JSON.stringify(counts));
		          //console.log('Length of the count object-->'+Object.keys(counts).length);
		          
		          if(Object.keys(counts).length > 1){
		        	  $('#natureofwork').append('<ul class="nav nav-pills" role="tablist"></ul>');
		        	  for (var k in counts){
			        	    if (counts.hasOwnProperty(k)) {
			        	    	now_name.push(k);
			        	    	var key = escape(k);
			        	    	$('#natureofwork ul').append('<li role="presentation"><a href="javascript:void(0)" data-now="'+key+'"><span><i class="fa fa-tags"></i></span>'+k+' <span class="badge">'+counts[k]+'</span></a></li>');
			        	         //console.log("Key is " + k + ", value is" + counts[k]);
			        	    }
				          }
		          }else{
		        	  //console.log('Count length is '+Object.keys(counts).length+'.. Due to that not appended!!');
		          }
		          
		          
	          }else{
	        	  //console.log('Response data is empty');
	          }
	     }*/
	});
}

function drafts(){
	tableContainer1 = $("#official_drafts"); 
	/*tableContainer1.dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
        "aaSorting": [],
		"autoWidth": false,
		"ajax": "inbox/draft",
		"columns": [
		{ "data": "date","width": "16%" },
		{ "data": "sender","width": "15%" },
		{ "data": "task","width": "20%" },
		{ "data": "status","width": "24%" },
		{ "data": "details","width": "20%" },
		{ "data": "id","visible": false, "searchable": false },
		{ "data": "link","visible": false, "searchable": false }
		],
		"columnDefs": [
	       {
	           "render": function ( data, type, row ) {
	               return type === 'display' && data.length > 75 ? data.substr( 0, 75 )+' <span class="details" data-text="'+data+'"><button class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button</span>' : data;
	           },
	           "targets": 4
	       }
	   ]
	});*/
}

function notifications(){
	tableContainer1 = $("#official_notify");
	tableContainer1.dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		/* Disable initial sort */
        "aaSorting": [],
		"autoWidth": false
	});
}

function worklistwrtnow(json){
	//console.log('came to construct datatable!!');
	//$("#official_inbox").empty();
	tableContainer1 = $("#official_inbox"); 
	tableContainer1.dataTable({
		"sDom": "<'row'<'col-xs-12 hidden col-right'f>r>t<'row buttons-margin'<'col-md-5 col-xs-12'i><'col-md-3 col-xs-6'l><'col-md-4 col-xs-6 text-right'p>>",
		"aLengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
		"bDestroy": true,
		/* Disable initial sort */
        "aaSorting": [],
		"autoWidth": false,
		"data": json,
			"columns": [
			{ "data": "date","width": "16%" },
			{ "data": "sender","width": "15%" },
			{ "data": "task","width": "20%" },
			{ "data": "status","width": "24%" },
			{ "data": "details","width": "20%" },
			{ "data" : null, "target":-1,"defaultContent": '<i class="fa fa-history inbox-history history-size" class="tooltip-secondary" data-toggle="tooltip" title="History"></i>'},
			{ "data": "id","visible": false, "searchable": false },
			{ "data": "link","visible": false, "searchable": false }
		],
		"columnDefs": [
            {
                "render": function ( data, type, row ) {
                    return type === 'display' && data.length > 75 ? data.substr( 0, 75 )+' <span class="details" data-text="'+data+'"><button class="btn-xs" style="font-size:10px;">More <i class="fa fa-angle-double-right" aria-hidden="true"></i></button</span>' : data;
                },
                "targets": 4
            }
        ] 
	});
}

function refreshnow(now){
	//console.log('came to refresh nature of work');
    //console.log('parent JSON-->'+response_json);
    if(now != 'Reset' && now!= undefined){
    	//console.log('nature of work other than reset or undefined--->'+now);
	    $.each(JSON.parse(response_json), function(key, value) {
	    	if (value.task === now) {
	    		//console.log(JSON.stringify(value));
	    		now_json.push(value);
	    	}
	    });
	    //console.log('NOW JSON-->'+JSON.stringify(now_json));
	    worklistwrtnow(now_json);
    }else{
    	//console.log('came as reset or undefined');
    	$('#natureofwork ul li a[data-now="Reset"]').parent().remove();
    	worklistwrtnow(JSON.parse(response_json));
    }
}

function inboxloadmethod(){
	//bootbox.alert('came to my parent'+focussedmenu);
	//bootbox.alert('Nature of work'+now);
	clearnow();
	worklist();
	/*if(focussedmenu == 'worklist'){
		worklist();
		//nature of work make it stable
		setTimeout(function(){ 
			if(now_name.indexOf(now) > -1){
				refreshnow(now);
				$('#natureofwork ul').append('<li role="presentation"><a href="javascript:void(0)" data-now=Reset><span><i class="fa fa-refresh"></i></span>Reset / Clear</a></li>');
				var key =  escape(now);
				$('#natureofwork ul li a[data-now="'+key+'"]').parent().addClass('active');
			}else{
				refreshnow('Reset');
			}
			$('#inboxsearch').trigger('keyup');
		}, 500);
	}else if(focussedmenu == 'drafts'){
		drafts();
	}else if(focussedmenu == 'notifications'){
		notifications();
	}*/
}

function getPosition(){
	$.ajax({
		url : '/hr-employee/employees/_search?tenantId='+tenantId+'&id='+localStorage.getItem('id'),
		type : 'POST',
		processData : false,
		contentType: "application/json",
		data : JSON.stringify( requestInfo ),
		async : false,
		success : function(response){
			try{
				positionId = response.Employee[0].assignments[0].position;
				$('.profile-text').text(response.Employee[0].userName);
			}catch(e){

			}
			
		},
		error: function(){
			bootbox.alert('Error getting positionId!');
		},
		complete : function(){

		}
	})
}

function getStatus(){
	$.ajax({
		url: "/workflow/v1/statuses/_search?tenantId="+tenantId+'&keyword=Deliverable_service',
		type : 'POST',
		dataType: 'json',
		processData : false,
		async : false,
		contentType: "application/json",
		data : JSON.stringify(requestInfo)
	}).done(function(data) {
		localStorage.setItem('status', JSON.stringify(data.statuses));
	});
}

$.fn.dataTableExt.afnFiltering.push(function(oSettings, aData, iDataIndex) {
	if(aData[4] == 'COMPLETED' || aData[4] == 'WITHDRAWN' || aData[4] == 'REJECTED' || aData[4] == 'APPROVED')
		return false;
	else
		return true;
});