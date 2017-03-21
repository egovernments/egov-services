$(document).ready(function()
{
    $.ajaxSetup({
	    beforeSend: function(xhr) {
	    	var authToken = localStorage.getItem("auth");
	    	if(authToken)
	        	xhr.setRequestHeader('auth-token', authToken);
	    }
	});

});