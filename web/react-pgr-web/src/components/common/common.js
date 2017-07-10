export function translate(locale_text){
  if(locale_text &&  localStorage.getItem("lang_response")){
		var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
		  return obj.code == locale_text;
		});
		if(langresult[0])
      return Object.values(langresult[0])[1];
    else
      return locale_text;
	}
}

export function validate_fileupload(files, formats){
  var filelimit = 5242880;
  for(let i=0; i<files.length; i++) {
    let file = files[i];
    let filename = file.name;
    let fileext = filename.split('.').pop().toLowerCase();
    let filesize = file.size;
    //console.log('came to file:', filename, filename.length, filesize, fileext, formats);
    //file length validation
    if(filename.length <= 50){
      //console.log('file name length validation success');
      if(formats.indexOf(fileext) >= 0){
        //console.log('file formats validation success');
        if(filesize <= filelimit){
          //console.log('file size validation success');
        }else {
          return 'File size exceeds 5MB';
        }
      }else {
        return 'Allowed file formats is '+formats+'';
      }
    }else {
      return 'File name length should not exceed 50 characters';
    }
  }
  return true;
}
