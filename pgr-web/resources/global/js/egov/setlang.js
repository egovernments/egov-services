$(document).ready(function(){
	$('.set-lang').click(function(){
		var sel_value = $('#lang-dropdown option:selected').val();
		$.ajax({
			url : 'http://localhost:32/localization/messages?tenantId=ap.public&locale='+sel_value,
			type : 'GET',
			success : function(response){
				locale = response.messages;

				//local storage
				localStorage.setItem("lang", sel_value);
				localStorage.setItem("lang_response", JSON.stringify(locale));

				//console.log(JSON.stringify(locale));
				
				$.each(JSON.parse(localStorage.getItem("lang_response")), function(i, item){
					var ele=$('[data-translate="'+ item.code +'"]');
					ele.each(function(i,el){
						//el.firstChild.data = item.message;
						$(this).contents().first().replaceWith(item.message);
					});
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