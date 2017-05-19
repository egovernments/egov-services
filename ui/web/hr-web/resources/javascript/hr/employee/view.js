//getting current guerry strings
function getUrlVars() {
    var vars = [],
        hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

//closing current window
$('#close').on("click", function() {
    window.close();
})

//common object
var commonObject = {
    employeeType: [{
            id: 1,
            name: "Deputation",
            chartOfAccounts: ""
        },
        {
            id: 2,
            name: "Permanent",
            chartOfAccounts: ""
        },
        {
            id: 3,
            name: "Daily Wages`",
            chartOfAccounts: ""
        },
        {
            id: 4,
            name: "Temporary",
            chartOfAccounts: ""
        },
        {
            id: 5,
            name: "Contract",
            chartOfAccounts: ""
        }
    ],
    employeeStatus: ["EMPLOYEED", "RETAIRED", "RESIGNED", "TERMINTED", "DESEASED", "SUSPEPEND", "TRANSFERRED"],
    group: [{
            id: 1,
            name: "State",
            description: ""
        },
        {
            id: 2,
            name: "Central",
            description: ""
        },
        {
            id: 3,
            name: "Local",
            description:""
        }
    ],
    maritalStatus: ["MARRIED", "UNMARRIED", "DISCOVERED", "WIDROW", "WIDOW"],
    user_bloodGroup: ["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"],
    motherTounge: [{
            id: 1,
            name: "Kannada",
            description: "",
            active: true
        },
        {
            id: 2,
            name: "Hindi",
            description: "",
            active: true
        },
        {
            id: 3,
            name: "Tamil",
            description: "",
            active: true
        },
        {
            id: 4,
            name: "Telagu",
            description: "",
            active: true
        }
    ],
    religion: [{
            id: 1,
            name: "Hindu",
            description: "",
            active: true,
        },
        {
            id: 2,
            name: "Muslim",
            description: "",
            active: true
        }
    ],
    community: [{
            id: 1,
            name: "Hindu",
            description: "",
            active: true
        },
        {
            id: 2,
            name: "Muslim",
            description: "",
            active: true
        }
    ],
    category: [{
            id: 1,
            name: "SC",
            description: "",
            active: true
        },
        {
            id: 2,
            name: "ST",
            description: "",
            active: true
        },
        {
            id: 3,
            name: "OBC",
            description: "",
            active: true
        }
    ],
    bank: [{
            id: 1,
            name: "HDFC",
            description: ""
        },
        {
            id: 2,
            name: "SBI",
            description: ""
        },
        {
            id: 3,
            name: "ICIC",
            description: ""
        }
    ],
    bankBranch: [{
            id: 1,
            name: "domlur",
            description: ""
        },
        {
            id: 2,
            name: "mathalli",
            description: ""
        },
        {
            id: 3,
            name: "varhur",
            description: ""
        }
    ],
    recruitmentMode: [{
            id: 1,
            name: "UPSC",
            description: ""
        },
        {
            id: 2,
            name: "Department",
            description: ""
        },
        {
            id: 3,
            name: "Exams",
            description: ""
        }
    ],
    recruitmentType: [{
            id: 1,
            name: "Direct",
            description: ""
        },
        {
            id: 2,
            name: "Transfer",
            description: ""
        },
        {
            id: 3,
            name: "Compensatory ",
            description: ""
        }
    ],
    recruitmentQuota: [{
            id: 1,
            name: "Sports",
            description: ""
        },
        {
            id: 2,
            name: "Ex-Serviceman",
            description: ""
        },
        {
            id: 3,
            name: "Handicapped ",
            description: ""
        }
    ],
    assignments_fund: [{
            id: 1,
            name: "Own",
            description: ""
        },
        {
            id: 2,
            name: "Company",
            description: ""
        }
    ],
    assignments_function: [{
            id: 1,
            name: "IR",
            description: ""
        },
        {
            id: 2,
            name: "No-IT",
            description: ""
        }
    ],
    assignments_functionary: [{
            id: 1,
            name: "developrment",
            description: ""
        },
        {
            id: 2,
            name: "no developrment",
            description: ""
        }
    ],
    assignments_grade: [{
            id: 1,
            name: "1st",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "2nd",
            description: "",
            orderno: "1",
            active: true
        }
    ],
    assignments_designation: [{
            id: 1,
            name: "Juniour Engineer",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "Assistance Engineer",
            description: "",
            orderno: "1",
            active: true
        }
    ],
    assignments_position: [{
            id: 1,
            name: "Juniour Engineer",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "Assistance Engineer",
            description: "",
            orderno: "1",
            active: true
        }
    ],
    assignments_department: [{
            id: 1,
            name: "Juniour Engineer",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "Assistance Engineer",
            description: "",
            orderno: "1",
            active: true
        }
    ],
    jurisdictions_jurisdictionsType: [{
            id: 1,
            name: "Juniour Engineer",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "Assistance Engineer",
            description: "",
            orderno: "1",
            active: true
        }
    ],
    jurisdictions_boundary: [{
            id: 1,
            name: "Juniour Engineer",
            description: "",
            orderno: "1",
            active: true
        },
        {
            id: 2,
            name: "Assistance Engineer",
            description: "",
            orderno: "1",
            active: true
        }
    ]
}

//common shared object
commonObject["assignments_mainDepartments"]=commonObject["assignments_department"];
commonObject["languagesKnow"]=commonObject["motherTounge"];
commonObject["user_locale"]=commonObject["motherTounge"];
commonObject["probation_designation"]=commonObject["assignments_designation"];
commonObject["regularisation_designation"]=commonObject["assignments_designation"];


for(var key in commonObject)
{
  var splitObject=key.split("_");
  if(splitObject.length<2)
  {
    for(var i=0;i<commonObject[key].length;i++)
    {
      if(typeof(commonObject[key][i])==="object")
      $(`#${key}`).append(`<option value='${commonObject[key][i]['id']}'>${commonObject[key][i]['name']}</option>`)
      else
      $(`#${key}`).append(`<option value='${commonObject[key][i]}'>${commonObject[key][i]}</option>`)
    }
  }
  else {
    for(var i=0;i<commonObject[key].length;i++)
    {
      if(typeof(commonObject[key][i])==="object")
      $(`#${splitObject[0]}\\.${splitObject[1]}`).append(`<option value='${commonObject[key][i]['id']}'>${commonObject[key][i]['name']}</option>`)
      else
      $(`#${splitObject[0]}\\.${splitObject[1]}`).append(`<option value='${commonObject[key][i]}'>${commonObject[key][i]}</option>`)
    }
  }

}


//final post object
var employee = {
    code: "",
    dateOfAppointment: "",
    dateOfJoining: "",
    dateOfRetirement: "",
    employeeStatus: "",
    recruitmentMode: "",
    recruitmentType: "",
    recruitmentQuota: "",
    retirementAge: "",
    dateOfResignation: "",
    dateOfTermination: "",
    employeeType: "",
    assignments: [],
    jurisdictions: [],
    motherTounge: "",
    religion: "",
    community: "",
    category: "",
    physicallyDisabled: false,
    medicalReportProduced: true,
    languagesKnow: [],
    maritalStatus: "",
    passportNo: "",
    gpfNo: "",
    bank: "",
    bankBranch: "",
    bankAccount: "",
    group: "",
    placeOfBirth: "",
    documents: "",
    serviceHistory: [],
    probation: [],
    regularisation: [],
    technical: [],
    education: [],
    test: [],
    user: {
      userName:	"",
      name:	"",
      gender: "",
      mobileNumber:	"",
      emailId:	"",
      altContactNumber:	"",
      pan:	"",
      aadhaarNumber:	"",
      permanentAddress:	"",
      permanentCity:	"",
      permanentPincode:	"",
      correspondenceCity:	"",
      correspondencePincode:	"",
      correspondenceAddress:	"",
      active:	"",
      dob:	"",
      locale:	"",
      signature: "",
      fatherOrHusbandName:	"",
      bloodGroup:	"",
      identificationMark: "",
      photo:	""
    }
}

//temprory object for holding modal value
var employeeSubObject = {
    assignments: {
        fromDate: "",
        toDate: "",
        fund: "",
        function: "",
        grade: "",
        designation: "",
        position: "",
        functionary: "",
        department: "",
        hod: "No",
        mainDepartments: "",
        govtOrderNumber: "",
        is_primary: "No"
    },
    jurisdictions:{
        id:1,
        boundary:""
    },
    serviceHistory:{
      id:	0,
      serviceInfo:""	,
      serviceFrom:	"",
      remarks:""	,
      orderNo:	"",
      documents:""
    },
    probation:{
      designation:	"",
      declaredOn:	"",
      orderNo:	"",
      orderDate:	"",
      remarks:	"",
      documents:""
    },
    regularisation:{
      designation:	"",
      declaredOn:	"",
      orderNo:	"",
      orderDate:	"",
      remarks:	"",
      documents:""
    },
    education:{
      qualification:""	,
      majorSubject:"",
      yearOfPassing:	"",
      university:	"",
      documents:	""
    },
    technical:{
      skill:""	,
      grade:"",
      yearOfPassing:	"",
      remarks:	"",
      documents:	""
    },
    test:{
      test:""	,
      yearOfPassing:	"",
      remarks:	"",
      documents:	""
    }
}

//unicersal marker for putting edit index
var editIndex = -1;

//base url for api_id
var baseUrl = "https://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";

//request info from cookies
var requestInfo = {
    "api_id": "string",
    "ver": "string",
    "ts": "2017-01-18T07:18:23.130Z",
    "action": "string",
    "did": "string",
    "key": "string",
    "msg_id": "string",
    "requester_id": "string",
    "auth_token": "aeiou"
};

//form validation
var validation_rules = {};
var final_validatin_rules = {};
var commom_fields_rules = {
    code: {
        required: true
    },
    dob: {
        required: true
    },
    name: {
        required: true
    },
    gender: {
        required: true
    },
    mobileNumber: {
        required: true
    },
    active: {
        required: true
    },
    userName: {
        required: true
    },
    permanentAddress: {
        required: true
    },
    permanentCity: {
        required: true
    },
    permanentPincode: {
        required: true
    },
    dateOfAppointment: {
        required: true
    },
    dateOfJoining: {
        required: false
    },
    dateOfRetirement: {
        required: false
    },
    employeeStatus: {
        required: true
    },
    recruitmentMode: {
        required: false
    },
    recruitmentType: {
        required: true
    },
    recruitmentQuota: {
        required: false
    },
    retirementAge: {
        required: false
    },
    dateOfResignation: {
        required: false
    },
    dateOfTermination: {
        required: false
    },
    employeeType: {
        required: false
    },
    assignments: {
        required: false
    },
    jurisdictions: {
        required: false
    },
    motherTounge: {
        required: false
    },
    religion: {
        required: false
    },
    community: {
        required: false
    },
    category: {
        required: false
    },
    physicallyDisabled: {
        required: false
    },
    medicalReportProduced: {
        required: false
    },
    languagesKnow: {
        required: false
    },
    maritalStatus: {
        required: true
    },
    passportNo: {
        required: false
    },
    gpfNo: {
        required: false
    },
    bank: {
        required: false
    },
    bankBranch: {
        required: false
    },
    bankAccount: {
        required: false
    },
    group: {
        required: false
    },
    placeOfBirth: {
        required: false
    },
    documents: {
        required: false
    },
    serviceHistory: {
        required: false
    },
    probation: {
        required: false
    },
    regularisation: {
        required: false
    },
    technical: {
        required: false
    },
    education: {
        required: false
    },
    test: {
        required: false
    }
}


//Getting data for user input
$("input").on("keyup change", function() {
    fillValueToObject(this);
});

//Getting data for user input
$("select").on("change", function() {
    if(this.id=="jurisdictions.jurisdictionsType") return;
    else {

        fillValueToObject(this);
    }
});

//Getting data for user input
$("textarea").on("keyup change", function() {
    fillValueToObject(this);
})

//file change handle for file upload
$("input[type=file]").on("change", function(evt) {
    // console.log(this.value);
    // agreement[this.id] = this.value;
    var file = evt.currentTarget.files[0];

    //call post api update and update that url in pur agrement object
});

//initial setup
$("#departments").hide();

//it will split object string where it has .
function fillValueToObject(currentState) {
    if (currentState.id.includes(".")) {

        var splitResult = currentState.id.split(".");
        if(splitResult[0]==="user")
        {
            employee[splitResult[0]][splitResult[1]]=currentState.value;
        }
        else {
              employeeSubObject[splitResult[0]][splitResult[1]] = currentState.value;
        }

    } else {
        employee[currentState.id] = currentState.value;
    }
}

//need to cleat editIndex and temprory pbject
$('#assignmentDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["assignments"] = {
        fromDate: "",
        toDate: "",
        fund: "",
        function: "",
        grade: "",
        designation: "",
        position: "",
        functionary: "",
        department: "",
        hod: "",
        mainDepartments: "",
        govtOrderNumber: "",
        is_primary: ""
    };
})

$('#jurisdictionDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["jurisdictions"] = {
        id:1,
        boundary:""
    }
})

$('#serviceHistoryDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["serviceHistory"] = {
      id:	0,
      serviceInfo:""	,
      serviceFrom:	"",
      remarks:""	,
      orderNo:	"",
      documents:""
    }
})

$('#probationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["probation"] = {
      designation:	"",
      declaredOn:	"",
      orderNo:	"",
      orderDate:	"",
      remarks:	"",
      documents:""
    }
})

$('#regularisationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["regularisation"] = {
      designation:	"",
      declaredOn:	"",
      orderNo:	"",
      orderDate:	"",
      remarks:	"",
      documents:""
    }
})

$('#technicalDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["technical"] = {
      skill:""	,
      grade:"",
      yearOfPassing:	"",
      remarks:	"",
      documents:	""
    }
})

$('#educationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["education"] = {
      qualification:""	,
      majorSubject:"",
      yearOfPassing:	"",
      university:	"",
      documents:	""
    }
})

$('#testDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["test"] = {
      test:""	,
      yearOfPassing:	"",
      remarks:	"",
      documents:	""
    }
})


function enableAndDisable(id) {
    $(`#${id}`).toggle();
}

//common update
function updateTable(tableName, modalName, object) {
    $(tableName).html(``);
    for (var i = 0; i < employee[object].length; i++) {
        $(tableName).append(`<tr>`);
        for (var key in employee[object][i]) {
            $(tableName).append(`<td data-label=${key}>
                                ${employee[object][i][key]}
                          </td>`)
        }
        $(tableName).append(`<td data-label="Action">
                    <a href="#" onclick="markEditIndex(${i},'${modalName}','${object}')" class="btn btn-default btn-action"><span class="glyphicon glyphicon-pencil"></span></a>
                    <a href="#" onclick="commonDeleteFunction('${tableName}','${modalName}','${object}',${i})" class="btn btn-default btn-action"><span class="glyphicon glyphicon-trash"></span></a>
                  </td>`);
        $(tableName).append(`</tr>`);
    }
}

//common edit mark index
function markEditIndex(index = -1, modalName = "", object = "") {
    editIndex = index;
    $('#' + modalName).modal('show');
    //assignments  details modal when it edit modalName
    $('#' + modalName).on('shown.bs.modal', function(e) {
        if (editIndex != -1) {
            employeeSubObject[object] = Object.assign({}, employee[object][editIndex]);

            for (var key in employeeSubObject[object]) {
                // if($(`#${object}\\.${key}`).length == 0) {
                //       alert("not there");
                //   }
                $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
            }
        }
    })
    // alert("fired");
}

//common add and update
function commonAddAndUpdate(tableName, modalName, object) {
    // switchValidation(object);
    if (editIndex != -1) {
        employee[object][editIndex] = employeeSubObject[object];
        updateTable("#" + tableName, modalName, object);
    } else {
        employee[object].push(Object.assign({}, employeeSubObject[object]));
        updateTable("#" + tableName, modalName, object);
    }
}

//common Delete
function commonDeleteFunction(tableName, modalName, object, index) {
    employee[object].splice(index, 1);
    updateTable(tableName, modalName, object);
}

//show reslut event
// $("#showResultEvent").on("click",function() {
//     // $("#showResult").text(employee);
//     console.log(employee);
// })





//
final_validatin_rules = Object.assign(validation_rules, commom_fields_rules);
for (var key in final_validatin_rules) {
    if (final_validatin_rules[key].required) {
        $(`label[for=${key}]`).append(`<span> *</span>`);
    }
    // $(`#${key}`).attr("disabled",true);
};

$.validator.addMethod('phone', function(value) {
    return /^[0-9]{10}$/.test(value);
}, 'Please enter a valid phone number.');

$.validator.addMethod('aadhar', function(value) {
    return /^[0-9]{12}$/.test(value);
}, 'Please enter a valid aadhar.');

$.validator.addMethod('pan_no', function(value) {
    return /^[0-9a-zA-Z]{10}$/.test(value);
}, 'Please enter a valid pan.');

$("#addEmployee").on("click",function() {
      //  $("#createEmployeeForm").submit();
      switchValidation("final_validatin_rules");
})





var assignmentDetailValidation={
  fromDate:{
      required:true
  },
  toDate: {
      required:true
  },
  fund: {
      required:false
  },
  function: {
      required:false
  },
  grade: {
      required:false
  },
  designation: {
      required:true
  },
  position: {
      required:true
  },
  functionary: {
      required:false
  },
  department: {
      required:true
  },
  hod: {
      required:true
  },
  mainDepartments: {
      required:true
  },
  govtOrderNumber: {
      required:false
  },
  is_primary: {
      required:true
  }
};

var jurisdictions={
    jurisdictionsType:{
        required:true
    },
    boundary:{
        required:true
    }
};

var serviceHistory={
  id:	{
      required:true
  },
  serviceInfo:{
      required:true
  }	,
  serviceFrom:	{
      required:true
  },
  remarks:{
      required:true
  },
  orderNo:	{
      required:true
  },
  documents:{
      required:false
  }
};

var probation={
  designation:	{
      required:true
  },
  declaredOn:	{
      required:true
  },
  orderNo:	{
      required:false
  },
  orderDate:	{
      required:false
  },
  remarks:	{
      required:false
  },
  documents:{
      required:false
  }
};

var regularisation={
  designation:	{
      required:true
  },
  declaredOn:	{
      required:true
  },
  orderNo:	{
      required:false
  },
  orderDate:	{
      required:false
  },
  remarks:	{
      required:false
  },
  documents:{
      required:false
  }
};

var education={
  qualification:{
      required:true
  },
  majorSubject:{
      required:false
  },
  yearOfPassing:	{
      required:true
  },
  university:	{
      required:true
  },
  documents:	{
      required:false
  }
};

var technical={
  skill:{
      required:true
  }	,
  grade:{
      required:true
  },
  yearOfPassing:	{
      required:true
  },
  remarks:	{
      required:false
  },
  documents:	{
      required:false
  }
}

var test={
  test:{
      required:true
  },
  yearOfPassing:	{
      required:true
  },
  remarks:	{
      required:false
  },
  documents:	{
      required:false
  }
};



function addMandatoryStart(validationObject,prefix="") {
  for (var key in validationObject) {
      if (prefix==="") {
        if (validationObject[key].required) {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
      }
      else {
        if (validationObject[key].required) {
            $(`label[for=${prefix}\\.${key}]`).append(`<span> *</span>`);
        }
      }

      // $(`#${key}`).attr("disabled",true);
  };
}

addMandatoryStart(assignmentDetailValidation,"assignments");

addMandatoryStart(jurisdictions,"jurisdictions");

addMandatoryStart(serviceHistory,"serviceHistory");

addMandatoryStart(probation,"probation");

addMandatoryStart(regularisation,"regularisation");

addMandatoryStart(education,"education");

addMandatoryStart(test,"test");

addMandatoryStart(technical,"technical");

function switchValidation(whichObject) {
    switch (whichObject) {
      case "final_validatin_rules":
            removeRule({assignments,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
            addRule(final_validatin_rules);
            $("#createEmployeeForm").submit();
        break;
      case "assignments":
            removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
            addRule(assignmentDetailValidation,"assignments");
            $("#createEmployeeForm").valid();
            console.log(  $("#createEmployeeForm").valid());
        break;
      default:

    }
}

function removeRule(arrayOfObject) {
    // console.log(arrayOfObject);
    for (var item in arrayOfObject) {
        if(item==="final_validatin_rules")
        {
          for(var itemInner in arrayOfObject[item])
          {

              $(`#${itemInner}`).rules( "remove" );

          }
        }
        else {
                for(var itemInner in arrayOfObject[item])
                {

                    $(`#${item}.${itemInner}`).rules( "remove" );

                }
        }
    }

}

function addRule(object,name)
{
    // console.log(object);
      for(var item in object)
      {
        if(object[item].required)
        {
          $(`#${name}.${item}`).rules( "add",object[item]);
        }
      }
}




// Adding Jquery validation dynamically
$("#createEmployeeForm").validate({
    rules: final_validatin_rules,
    submitHandler: function(form) {
        console.log(form);
        if(employee.assignments.length>0 && employee.assignments.jurisdictions.length>0)
        {
            //call api
        }
        else {
            alert("Please enter atleast assignment and jurisdiction");
        }
        //alert("submitterd");
        // form.submit();

        // console.log(agreement);
        // $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
        //     RequestInfo: requestInfo,
        //     Agreement: agreement
        // }, function(response) {
        //     // alert("submit");
        //     window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
        //     console.log(response);
        // })
    }
})
