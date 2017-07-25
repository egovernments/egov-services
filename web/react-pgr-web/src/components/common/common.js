import Api from '../../api/api';

var localationData = {
  "reports.pgr.complaintcategorytype":"Complaint Category Type",
  "reports.pgr.positiontype":"Position Type",
  "reports.pgr.wardtype":"Ward Type",
  "wc.create.groups.applicantDetails.title":"Applicant Particulars",
  "wc.create.groups.applicantDetails.propertyIdentifier":"PT Assessment Number",
  "wc.create.groups.applicantDetails.nameOfApplicant":"Name of Applicant",
  "wc.create.groups.applicantDetails.mobileNumber":"Mobile Number",
  "wc.create.groups.applicantDetails.email":"Email",
  "wc.create.groups.applicantDetails.adharNumber":"Aadhaar Number",
  "wc.create.groups.applicantDetails.locality":"Locality",
  "wc.create.groups.applicantDetails.address":"Address",
  "wc.create.groups.applicantDetails.zone":"Zone / Ward / Block",
  "wc.create.groups.applicantDetails.propertyTaxDue":"Property Tax",
  "wc.create.groups.connectionDetails.title":"Connection Details",
  "wc.create.groups.connectionDetails.connectionType":"Connection Type",
  "wc.create.groups.connectionDetails.sourceType":"Water Source Type",
  "wc.create.groups.connectionDetails.propertyType":"Property Type",
  "wc.create.groups.connectionDetails.categoryType":"Category",
  "wc.create.groups.connectionDetails.usageType":"Usage Type",
  "wc.create.groups.connectionDetails.hscPipeSizeType":"H.S.C Pipe Size (Inches)",
  "wc.create.groups.connectionDetails.fields.sumpCapacity":"Sump Capacity (Litres)",
  "wc.create.groups.connectionDetails.fields.numberOfPersons":"No.of persons",
  "wc.create.groups.approvalDetails.title":"Approval Details",
  "wc.create.groups.approvalDetails.fields.department":"Approver Department",
  "wc.create.groups.approvalDetails.fields.designation":"Approver Designation",
  "wc.create.groups.approvalDetails.fields.approver":"Approver  ",
  "wc.create.groups.approvalDetails.fields.comments":"Comments",
  "wc.create.categorytype*":"Category Type*",
  "wc.create.categorytype.title":"Create Category Type",
  "wc.create.groups.floorDetails.title": "Floor Details",
  "wc.create.groups.roomDetails.title": "Room Details",
  "wc.create.groups.floorDetails.floorNo": "Floor No",
  "wc.create.groups.floorDetails.floorName": "Floor Name",
  "wc.create.groups.roomDetails.roomNo": "Room No",
  "wc.create.groups.roomDetails.roomName": "Room Name",
  "wc.create.message.success": "Created Successfully . . . !",
  "wc.create.groups.fileDetails.title": "Document Upload",
  "wc.create.groups.fileDetails.fields.pan": "Pan Card"
}

export function translate(locale_text){
  if(locale_text &&  localStorage.getItem("lang_response")){
		var langresult = JSON.parse(localStorage.getItem("lang_response")).filter(function( obj ) {
		  return obj.code == locale_text;
		});
		if(langresult[0])
      return Object.values(langresult[0])[1];
    else
      return localationData[locale_text] || locale_text;
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
    if(filename.length <= 30){
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
      return 'File name length should not exceed 30 characters';
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

export function fileUpload(file, module, cb) {
  if(file.constructor == File) {
    let formData = new FormData();
    formData.append("tenantId", localStorage.getItem('tenantId'));
    formData.append("module", module);
    formData.append("file", file);
    Api.commonApiPost("/filestore/v1/files",{}, formData).then(function(response) {
      cb(null, response);
    }, function(err) {
      cb(err.message);
    })
  } else {
    cb(null, {files: [{
      fileStoreId: file
    }]});
  }
}