$(document).ready(function(){

	if(localStorage.getItem("lang")){
		$('#lang-dropdown').val(localStorage.getItem("lang"))
	}

	$('#lang-dropdown').change(function(){
		var sel_value = $(this).val();
		$.ajax({
			url : '/localization/messages?tenantId=ap.public&locale='+sel_value,
			type : 'GET',
			success : function(response){
				locale = response.messages;

				//local storage
				localStorage.setItem("lang", sel_value);
				localStorage.setItem("lang_response", JSON.stringify(locale));

				var langresult;

				$('[data-translate]').each(function(i,v){
					var translate = $(this).data('translate');
					langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
					  return obj.code == translate;
					});
					//console.log(translate+'<--->'+JSON.stringify(result)+'<---->'+Object.values(result[0])[1]);
					var type = this.tagName.toLowerCase();
					if(type == 'input' || type == 'textarea')
						$(this).attr('placeholder',Object.values(langresult[0])[1]);
					else
						$(this).contents().first().replaceWith(Object.values(langresult[0])[1]);
				});

			},error : function(){
				bootbox.alert('localization failed!')
			},
			complete: function(){
				$('.lang-class').modal('hide');
			}
		});
	});
})