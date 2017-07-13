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


export function format_lat_long(latorlong)
{
	var loc_arry = latorlong.split(",");
	var degree= parseFloat(loc_arry[0]);
	var minutes= parseFloat(loc_arry[1]);
	var seconds= parseFloat(loc_arry[2]);

	//formula is degree+((minutes*60)+seconds/3600)
	var formatted = degree+((minutes*60)+seconds)/3600;

	return formatted;
}

export function toLocalTime(regDate) {
  var dat = regDate.split(" ")[0];
  dat = dat.split("-")[1] + "-" + dat.split("-")[0] + "-" + dat.split("-")[2] + " " + regDate.split(" ")[1];
  dat = new Date(dat + " UTC").toString();
  return dat.substr(0, dat.indexOf("GMT"));
}
