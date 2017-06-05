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
var openedWindows = [], tenantId;
var locale = [];
$(document).ready(function()
{
	tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : getUrlParameter('tenant') ? getUrlParameter('tenant') : 'default';
	
	$.ajaxSetup({
	    beforeSend: function(xhr, settings) {
	    	var patt = new RegExp("maps.googleapis.com");
			var res = patt.test(settings.url);
			var authToken = localStorage.getItem("auth");
	    	if(authToken && !res){
	        	xhr.setRequestHeader('auth-token', authToken);
	    	}
	    }
	});
	
	try { $('.twitter-typeahead').css('display','block'); } catch(e){}
	
	try { $(":input").inputmask(); }catch(e){}
	
	try { 
		
		initDatePicker();

		}catch(e){
		//console.warn("No Date Picker");
	}

	fileConstraint();
	
	try { 
		$('[data-toggle="tooltip"]').tooltip({
			'placement': 'bottom'
		});
		}catch(e){
		//console.warn("No tooltip");
	}
		
	try{
		
		$('.select2').select2({
			placeholder: "Select",
			minimumResultsForSearch: 1,
			width:'100%'
		});
		
		$('select').on('select2:close', function (evt) {
		  	$(this).focus();
		});
		
	}catch(e){
		//console.log('No select2');
	}
	
	$("form.form-horizontal[data-ajaxsubmit!='true']").submit(function( event ) {
		$('.loader-class').modal('show', {backdrop: 'static'});
	});
	
	//fade out success message
	$(".alert-success").fadeTo(2000, 500).slideUp(500, function(){
   		$(".alert-success").alert('close');
	});
	
	try{

		formValidation();

	}catch(e){
		//console.warn("No validation involved");
	}
	
	$('.signout').click(function(){
		var RI = new $.RequestInfo(localStorage.getItem('auth'));
		var obj = {};
		obj['RequestInfo'] = RI.requestInfo;
		$.ajax({
			url : '/user/_logout?access_token='+localStorage.getItem('auth'),
			type: 'POST',
			processData : false,
			contentType: "application/json",
			data : JSON.stringify(obj),
			success : function(response){
				if(response.status == 'Logout successfully'){
					clearLocalStorage();
					window.open('../index.html','_self');
					$.each( openedWindows, function( i, val ) {
						var window = val;
						window.close();
					});
				}
			},
			error : function(){
				bootbox.confirm("Sign out failed. Will redirect to login page. Try logging in once again.", function(result){ 
					if(result){
						window.open("../index.html","_self");
					}
				});
			},
			complete: function(){

			}
		});
	});

	$(document).on('click','.open-popup', function(e){
		e.preventDefault();
		openPopUp($(this).attr('href'),$(this).data('name'));
	});

	//Header
	var imgSrc = (tenantId == "default") ? "../resources/global/images/logo@2x.png"  : "../resources/global/images/panavel.png";
	$('[data-include=header]').append('<nav class="navbar navbar-default navbar-custom navbar-fixed-top"> <div class="container-fluid"> <div class="navbar-header col-md-8 col-xs-8"> <a class="navbar-brand" href="javascript:void(0);"> <img src="'+imgSrc+'" height="60"> <div> <span class="title2" data-translate="'+$('header').data('header-title')+'"></span> </div> </a> </div> <div class="nav-right-menu col-md-4 col-xs-4"> <ul class="hr-menu text-right"> <li class="ico-menu"> <a href="http://www.egovernments.org" data-strwindname = "egovsite" class="open-popup"> <img src="../resources/global/images/egov_logo_tr_h.png" title="Powered by eGovernments" height="37" alt=""> </a> </li> </ul> </div> </div> </nav>');

	//footer
	$('[data-include=footer]').append('<a href="http://eGovernments.org" target="_blank"><span data-translate="core.lbl.page.footer"></span></a>')

	//Loader
	$('body').append('<div class="modal fade loader-class" data-backdrop="static"> <div class="modal-dialog"> <div class="modal-body"> <div class="row spinner-margin text-center"> <div class="col-md-12 "> <div class="spinner"> <div class="rect1"></div> <div class="rect2"></div> <div class="rect3"></div> <div class="rect4"></div> <div class="rect5"></div> </div> </div> <div class="col-md-12 spinner-text" data-translate="core.alert.pleasewait"></div> </div> </div> </div> </div>');

	translate();

});

function translate(keyname){
	try{
		if(keyname){
			var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
			  return obj.code == keyname;
			});
			return Object.values(langresult[0])[1];
		}else{
			$('[data-translate], [data-content], [data-original-title]').each(function(i,v){
				var translate = $(this).data('translate');
				var content = $(this).data('content');
				var title = $(this).data('original-title');
				//console.log(translate, content , title)
				if(translate){
					var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
					  return obj.code == translate;
					});
					var type = this.tagName.toLowerCase();
					if(type == 'input' || type == 'textarea'){
						if(langresult[0]) $(this).attr('placeholder', Object.values(langresult[0])[1]);
					}
					else{
						if(langresult[0]) $(this).html(Object.values(langresult[0])[1]);
						else $(this).html(translate);
					}
				}
				if(content){
					var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
					  return obj.code == content;
					});
					if(langresult[0]) $(this).attr('data-content',Object.values(langresult[0])[1]);
					else $(this).html(content);
				}
				if(title){
					var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
					  return obj.code == title;
					});
					if(langresult[0]) $(this).attr('data-original-title',Object.values(langresult[0])[1]);
					else $(this).html(title);
				}
			});
		}
	}catch(e){
		
	}
}

function pageScrollTop()
{
    var body = $("html, body");
    body.stop().animate({scrollTop:0}, '500', 'swing', function() { 
       //bootbox.alert("Finished animating");
    });
}

function formValidation(){
	$('form').validate({
		showErrors: function (errorMap, errorList) {
		  if (typeof errorList[0] != "undefined") {
		      var position = $(errorList[0].element).offset().top-($('.navbar-header').height()+40);
		      $('html, body').animate({
		          scrollTop: position
		      }, 300);
		  }
		  this.defaultShowErrors();
		}
	});

	jQuery.extend(jQuery.validator.messages, {
		required: translate('core.error.required')
	});

	$.validator.addMethod("mobilevalidate",function(value){
	    return /^\d{10}$/.test(value);
	},translate('core.lbl.enter.mobilenumber'));

	$.validator.addMethod("emailvalidate",function(value){
	    return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value);
	},translate('core.error.valid.email'));

	$.validator.addMethod("numbervalidate",function(value){
	    return  /^[+-]?\d+(\.\d+)?$/.test(value);
	},'Enter valid number');

	$.validator.addMethod("datevalidate",function(value){
	    return  /^(\d{1,2})-(\d{1,2})-(\d{4})$/.test(value);
	},'Enter valid date format (dd-mm-yyyy)');

    jQuery.validator.addClassRules({
		mobilevalidate: { mobilevalidate : true },
		emailvalidate: { emailvalidate : true } ,
		numbervalidate: { numbervalidate : true } ,
		datevalidate: { datevalidate : true }  
	});
}

//Typeahead event handling
$.fn.getCursorPosition = function() {
    var el = $(this).get(0);
    var pos = 0;
    var posEnd = 0;
    if('selectionStart' in el) {
        pos = el.selectionStart;
        posEnd = el.selectionEnd;
    } else if('selection' in document) {
        el.focus();
        var Sel = document.selection.createRange();
        var SelLength = document.selection.createRange().text.length;
        Sel.moveStart('character', -el.value.length);
        pos = Sel.text.length - SelLength;
        posEnd = Sel.text.length;
    }
    return [pos, posEnd];
};

function typeaheadWithEventsHandling(typeaheadobj, hiddeneleid, dependentfield)
{
	  typeaheadobj.on('typeahead:selected', function(event, data){
		//setting hidden value
		$(hiddeneleid).val(data.value);    
	    }).on('keydown', this, function (event) {
	    	var e = event;
	    	
	    	var position = $(this).getCursorPosition();
	        var deleted = '';
	        var val = $(this).val();
	        if (e.which == 8) {
	            if (position[0] == position[1]) {
	                if (position[0] == 0)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0] - 1, 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        else if (e.which == 46) {
	            var val = $(this).val();
	            if (position[0] == position[1]) {
	                
	                if (position[0] === val.length)
	                    deleted = '';
	                else
	                    deleted = val.substr(position[0], 1);
	            }
	            else {
	                deleted = val.substring(position[0], position[1]);
	            }
	        }
	        
	        if(deleted){ 
	        	$(hiddeneleid).val(''); 
	        	cleardependentfield(dependentfield);
        	}

        }).on('keypress', this, function (event) {
        	//getting charcode by independent browser
        	var evt = (evt) ? evt : event;
        	var charCode = (evt.which) ? evt.which : 
                ((evt.charCode) ? evt.charCode : 
                  ((evt.keyCode) ? evt.keyCode : 0));
        	//only characters keys condition
	    	if((charCode >= 32 && charCode <= 127)){
	    		//clearing input hidden value on keyup
	    	    $(hiddeneleid).val('');
	    	    cleardependentfield(dependentfield);
	    	}
        }).on('focusout', this, function (event) { 
    	    //focus out clear textbox, when no values selected from suggestion list
    	    if(!$(hiddeneleid).val())
    	    {	
    	    	$(this).typeahead('val', '');
        		cleardependentfield(dependentfield);
    	    }
       });
}

function cleardependentfield(dependentfield){
	if(!dependentfield){
		return;
	}
	console.log($(dependentfield).prop("type"));
	if($(dependentfield).prop("type") == 'select-one' || $(dependentfield).prop("type") == 'select-multiple'){
		$(dependentfield).empty();
	}else if($(dependentfield).prop("type") == 'text' || $(dependentfield).prop("type") == 'textarea'){
		$(dependentfield).val('');
	}
}

function disableRefresh(e) {
	var key = (e.which || e.keyCode);
	if (e.ctrlKey)
		if (key == 82 || key == 116)
			e.preventDefault();
}

function preventBack(){
	history.pushState(null, null, document.URL);
    window.addEventListener('popstate', function () {
        history.pushState(null, null, document.URL);
    });
}

function select2initialize(obj,data,multiple){
	
	obj.empty();
	
	if(!multiple)
		obj.append("<option value=''>Select</option>");
	
	$('.select2').select2({
		allowClear: true,
        placeholder: "Select",
        minimumResultsForSearch: 1,
		data : data,
		multiple : multiple,
		width:'100%'
	});
	
}

/*productization - Aslam*/
function getFormData($form){
    var unindexed_array = $form.find(":not([data-extra])").serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function(n, i){
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function showLoader(){
	$('.loader-class').modal('show', {backdrop: 'static'})
}

function hideLoader(){
	$('.loader-class').modal('hide')
}

var getUrlParameter = function getUrlParameter(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

function openPopUp(url,name){
	//console.log(url, name)
	var windowObjectReference = window.open(url,name,'width=1000, height=700, top=300, left=260,scrollbars=yes');
	openedWindows.push(windowObjectReference);
	windowObjectReference.focus();
	return false;
}

function clearLocalStorage(){
	localStorage.removeItem('auth');
	localStorage.removeItem('type');
	localStorage.removeItem('id');
	localStorage.removeItem('tenantId');
	localStorage.removeItem('status');
}

function initDatePicker(){
	$(".datepicker").datepicker({
		format: "dd-mm-yyyy",
		autoclose: true
	}); 
}

function fileConstraint(){

	$('input[type=file]').on('change.bs.fileinput',function(e){
		var fileformats = ['doc','docx','xls','xlsx','rtf','pdf','jpeg','jpg','png','txt','zip','dxf'];
		var fileSize = 5e+6;
		var file = $(this)[0].files[0];
		var ext = file['name'].split('.').pop().toLowerCase();
		if($.inArray(ext, fileformats) > -1){
			//do something  
			if(this.files[0].size > fileSize){
				bootbox.alert(translate('core.error.file.exceed'));
				$( this ).val('');	
				return;
			}
		}else{
			bootbox.alert(ext+' '+translate('core.error.fileformat.notallowed'));
			$( this ).val('');
		}
	});

	$(document).on('click','.delete-file',function(){
		$(this).parent('.input-group').find('input[type=file]').val('');
	});

}

var RI = function(auth){
	this.apiId = 'org.egov.pgr';
    this.ver = '1.0';
    var dat = new Date().toLocaleDateString();
	var time = new Date().toLocaleTimeString();
	var date = dat.split("/").join("-");
    this.ts = date+' '+time;
    this.action = 'POST';
    this.did = '4354648646';
    this.key = 'xyz';
    this.msgId = '654654';
    this.requesterId = '61';
    this.authToken = auth;

    var requestInfo={};

    requestInfo['apiId']=this.apiId;
    requestInfo['ver']=this.ver;
    requestInfo['ts']=this.ts;
    requestInfo['action']=this.action;
    requestInfo['did']=this.did;
    requestInfo['key']=this.key;
    requestInfo['msgId']=this.msgId;
    requestInfo['requesterId']=this.requesterId;
    requestInfo['authToken']=this.authToken;

    this.requestInfo = requestInfo;
}

$.RequestInfo = RI;

var loadDropDown = function(){

}

loadDropDown.prototype.load =function(params)
{
	params.element.empty();
	if(params.placeholder)
		params.element.append($("<option />").val('').text(params.placeholder));
	else
		params.element.append($("<option />").val('').text('Select'));
	$.each(params.data,function(i,obj)
    {
		params.element.append($("<option />")
			.val(obj[params.keyValue]).text(obj[params.keyDisplayName]));
    });  
}

$.loadDD = loadDropDown;

var typeahead = function(params){
	// Instantiate the Bloodhound suggestion engine
	var bloodhound = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: params.url,
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (cl) {
					return {
						name: eval('cl.'+params.keyDisplayName),
						value: eval('cl.'+params.keyValue)
					};
				});
			}
		}
	});

	// Initialize the Bloodhound suggestion engine
	bloodhound.initialize();

	// Instantiate the Typeahead UI
	var user_typeahead = params.element.typeahead({
		  hint: true,
		  highlight: true,
		  minLength: params.minLength
		}, {
		displayKey: params.keyDisplayName,
		source: bloodhound.ttAdapter()
	}).on('typeahead:selected', function(event, data){            
		params.hiddenelem.val(data.value);   
    });

    this.typeaheadobj = user_typeahead;
}

$.typeahead = typeahead;

var renderFields = function(){
	
}

renderFields.prototype.render =function(objFields)
{
	this.attr_array = objFields.data;
	this.create = objFields.create;
	this.count = 0;

	for (var j = 0; j < this.attr_array.length; j++){
		if(this.attr_array[j].code == 'CHECKLIST'){
			var template = renderFields.prototype.renderTemplate(this.attr_array[j], this.create);
			this.templateCheckList = this.templateCheckList ? this.templateCheckList + template : template;
		}else if(this.attr_array[j].code == 'DOCUMENTS'){
			var template = renderFields.prototype.renderTemplate(this.attr_array[j], this.create);
			this.templateDocuments = this.templateDocuments ? this.templateDocuments + template : template;
		}else{
			var template = '';
			if (this.count%2 == 0)
				template += '<div class="form-group">';
			template += renderFields.prototype.renderTemplate(this.attr_array[j], this.create);
			if (this.count%2 == 1)
				template += '</div>';
			this.templateFormFields = this.templateFormFields ? this.templateFormFields + template : template;
			this.count++;
		}
	}

	this.templateObj = {};
	this.templateObj['formFields'] = this.templateFormFields;
	this.templateObj['checklist'] = this.templateCheckList;
	this.templateObj['documents'] = this.templateDocuments;

	//console.log('final',this.templateObj)

	return this.templateObj;
}

renderFields.prototype.renderTemplate =function(obj, mode)
{

	this.objToRender = obj;
	this.mode = mode ? '' : 'disabled';

	for (var key in this.objToRender) {
		var value = this.objToRender[key];
		if(key == 'variable'){
			this.variable = value;
		}else if(key == 'dataType'){
			this.dataType = value;
		}else if(key == 'code'){
			this.name = value;
		}else if(key == 'required'){
			this.required = value ? 'required' : '';
		}else if(key == 'description'){
			this.description = value;
		}else if(key == 'attribValues'){
			this.attribValues = value;
		}
	}

	if(this.name == 'CHECKLIST'){
		var this_checklist = '';
		for(var i=0; i < this.attribValues.length; i++){
			if(this.attribValues[i]["isActive"] && localStorage.getItem('type') == 'EMPLOYEE')
				this_checklist += '<div class="form-group"><div class="col-sm-1">'+(i+1)+'</div><div class="col-sm-1"><input type="checkbox" name="'+this.attribValues[i].key+'"></div><div class="col-sm-10" data-translate="'+this.attribValues[i].name+'"></div></div>'
			else if(this.attribValues[i]["isActive"] && localStorage.getItem('type') == 'CITIZEN')
				this_checklist += '<div class="form-group"><div class="col-sm-1">'+(i+1)+'</div><div class="col-sm-1"><input type="checkbox" name="'+this.attribValues[i].key+'" '+this.mode+'></div><div class="col-sm-10" data-translate="'+this.attribValues[i].name+'"></div></div>'
		}
		this.template = this_checklist;
	}else if(this.name == 'DOCUMENTS'){
		var this_documents = '<div class="form-group">';
		for(var i=0; i < this.attribValues.length; i++){
			if(this.attribValues[i]["isActive"] && localStorage.getItem('type') == 'EMPLOYEE')
				this_documents += '<div class="col-sm-4"><div data-translate="'+this.attribValues[i].name+'"></div><div><div class="input-group"><input type="file" name="'+this.attribValues[i].key+'" class="form-control attribFile"><span class="input-group-addon delete-file"><i class="glyphicon glyphicon-trash specific"></i></span></div></div></div>'
			else if(this.attribValues[i]["isActive"] && localStorage.getItem('type') == 'CITIZEN')
				this_documents += '<div class="col-sm-4"><div data-translate="'+this.attribValues[i].name+'"></div><div><div class="input-group"><input type="file" name="'+this.attribValues[i].key+'" '+this.mode+' class="form-control attribFile"><span class="input-group-addon delete-file"><i class="glyphicon glyphicon-trash specific"></i></span></div></div></div>'
		}
		this_documents += '</div>';
		this.template = this_documents;
	}else{
		if(!this.variable){
			this.template = '<label class="col-sm-2 control-label"></label><div class="col-sm-3 add-margin success-msg" data-translate="'+this.description+'"></div><div class="col-sm-1"></div>';
		}else{
			this.pattern = this.dataType == 'number' ? 'numbervalidate' : this.dataType == 'date' ? 'datevalidate' : ''; 
			if(this.dataType == 'number' || this.dataType == 'string'){
				if(this.name == 'PROCESSINGFEE' && this.mode == '')
					this.template = '';
				else if(localStorage.getItem('type') == 'EMPLOYEE' && this.name == 'PROCESSINGFEE' && this.mode == 'disabled')
					this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate="'+this.description+'"></label> <div class="col-sm-3 add-margin"> <input type="text" name="'+this.name+'" class="form-control" '+this.required+' /> </div><div class="col-sm-1"></div>';	
				else
					this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate="'+this.description+'"></label> <div class="col-sm-3 add-margin"> <input type="text" name="'+this.name+'" class="form-control '+this.pattern+'" '+this.required+' '+this.mode+' /> </div><div class="col-sm-1"></div>';	
			}else if(this.dataType == 'date')
				this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate="'+this.description+'"></label> <div class="col-sm-3 add-margin"> <input type="text" name="'+this.name+'" class="form-control datepicker '+this.pattern+'" '+this.required+' '+this.mode+' /> </div><div class="col-sm-1"></div>';	
			else if(this.dataType == 'datetime')
				this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate="'+this.description+'"></label> <div class="col-sm-3 add-margin"> <input type="text" name="'+this.name+'" class="form-control" '+this.required+' '+this.mode+' /> </div><div class="col-sm-1"></div>';	
			else if(this.dataType == 'text')
				this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate"'+this.description+'></label> <div class="col-sm-3 add-margin"> <textarea class="form-control" name="'+this.name+'" data-regexp="'+this.pattern+'" '+this.required+' '+this.mode+' ></textarea> </div><div class="col-sm-1"></div>';
			else if(this.dataType == 'singlevaluelist' || this.dataType == 'multivaluelist'){
				var this_select_content;

				if(this.dataType == 'multivaluelist'){
					this_select_content = '<select name="'+this.name+'" '+this.required+' '+this.mode+' multiple class="form-control">';
				}else
					this_select_content = '<select name="'+this.name+'" '+this.required+' '+this.mode+' class="form-control">';

					this_select_content += '<option value="">Select</option>';

				for(var i=0; i < this.attribValues.length; i++){
					if(this.attribValues[i]["isActive"])
				    	this_select_content += '<option value="' + this.attribValues[i]['key'] + '">' + translate(this.attribValues[i]['name']) + '</option>';
				}

				this_select_content +='</select>';

				this.template = '<label class="col-sm-2 control-label '+this.required+'" data-translate="'+this.description+'"></label><div class="col-sm-3 add-margin">'+this_select_content+'</div><div class="col-sm-1"></div>';
			}else
				this.template = '';
		}
		
	}
	
	return this.template;

}

$.renderFields = renderFields;