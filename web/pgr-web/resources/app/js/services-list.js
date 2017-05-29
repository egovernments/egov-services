var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function(){
	getAllServices();

	$('.search').keyup(function(e){
		var searchId = $(this).data('searchid');
		var rule = '*'+$(this).val()+'*';
		if(e.keyCode == 8){
		  $(''+searchId+' .services').show();
		}
		$(''+searchId+" .services:visible").each(function(){
		  var testStr = $(this).find('.content').html().toLowerCase();
		  //console.log(testStr, rule, matchRuleShort(testStr, rule))
		  if(matchRuleShort(testStr, rule))
		    $(this).show();
		  else
		    $(this).hide();
		});
	});

});

$(document).on('click','.services-item .services .content',function(){
	sCode = $(this).data('code');
	name = $(this).data('servicename');
	openPopUp('create-service.html?code='+sCode+'&name='+name,sCode);
});

function getAllServices(){
	$.ajax({
		url: "/pgr/services/_search?type=all&tenantId="+tenantId,
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		beforeSend : function(){
			showLoader();
		},
		success : function(data) {
			serviceResult = (data.complaintTypes).filter(function( obj ) {
				return (obj.keywords).indexOf('deliverable') > -1;
			});
			$('#service_list').html('');
			$.each(serviceResult, function(i,obj){
				$('#service_list').append('<div class="col-lg-4 col-md-4 col-sm-6 col-xs-12 services"><a href="javascript:void(0)"> <div class="content a" data-code="'+obj.serviceCode+'" data-servicename="'+obj.serviceName+'">'+obj.serviceName+'</div> </a></div>');
			});
			$('.services .content').matchHeight();
		},
		complete : function(){
			$('.search').trigger('keyup');
			hideLoader();

		}
	});
}

//Short code
function matchRuleShort(str, rule) {
  return new RegExp("^" + rule.split("*").join(".*") + "$").test(str);
}