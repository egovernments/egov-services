export function translate(locale_text){
  if(locale_text){

		var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
		  return obj.code == locale_text;
		});
		if(langresult[0])
      return Object.values(langresult[0])[1];
    else
      return locale_text;
	}
}
