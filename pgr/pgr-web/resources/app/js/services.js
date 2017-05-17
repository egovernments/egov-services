var RequestInfo = new $.RequestInfo(localStorage.getItem("auth"));
var requestInfo = {};
requestInfo['RequestInfo'] = RequestInfo.requestInfo;
$(document).ready(function(){
	$.when(
		/*Show loader*/
		showLoader(),

		getAllServices()

	).then(function() {
		//Hide Loader
		hideLoader();
	});
});

function getAllServices(){
	$.ajax({
		url: "/pgr/services/_search?type=all&tenantId=default",
		type : 'POST',
		data : JSON.stringify(requestInfo),
		dataType: 'json',
		processData : false,
		contentType: "application/json",
		async :false,
	}).done(function(data) {
		
		$.each(data.complaintTypes, function(i,obj){
			console.log(JSON.stringify(obj));
			$('#service_list').append('<div class="col-md-4"> <a href="javascript:void(0)" class="btn btn-block btn-secondary text-left services" data-code="'+obj.serviceCode+'">'+obj.serviceName+'</a> </div>');
		});
	});
}