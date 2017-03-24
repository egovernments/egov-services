//getting current guerry strings


//closing current window
$('#close').on("click", function() {
    window.close();
})

function returnObject(alies) {
    switch (alies) {
        case "department":
            return "assignments_department";
            break;
        case "designation":
            return "assignments_designation";
            break;
        case "position":
            return "assignments_position";
            break;
        case "mainDepartments":
            return "assignments_department";
            break;
        case "fund":
            return "assignments_fund";
            break;
        case "function":
            return "assignments_function";
            break;
        case "functionary":
            return "assignments_functionary";
            break;
        case "grade":
            return "assignments_grade";
            break;
        case "jurisdictionsType":
            return "jurisdictions_jurisdictionsType";
            break;
        case "boundary":
            return "jurisdictions_boundary";
            break;
        default:

    }

}


// localStorage.setItem("employeeType",JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"]));
// employeeType:JSON.parse(localStorage.getItem("employeeType"))==""?(localStorage.setItem("employeeType",JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"]))|| []) :JSON.parse(localStorage.getItem("employeeType")),

//common object
var yearOfPassing=[];

for (var i = 2000; i <= new Date().getFullYear(); i++) {
    yearOfPassing.push(i);
}
console.log(yearOfPassing);
var commonObject = {
    employeeType: getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [],
    employeeStatus: getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"] || [],
    group: getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"] || [],
    maritalStatus: ["MARRIED", "UNMARRIED", "DIVORCED", "WIDOWER", "WIDOW"],
    user_bloodGroup: ["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"],
    motherTounge: getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"] || [],
    religion: getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"] || [],
    community: getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"] || [],
    category: getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"] || [],
    bank: getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"] || [],
    bankBranch:[],
    recruitmentMode: getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"] || [],
    recruitmentType: getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"] || [],
    recruitmentQuota: getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"] || [],
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
    assignments_grade: getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"] || [],
    assignments_designation: getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [],
    assignments_position: [],
    assignments_department: getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [],
    jurisdictions_jurisdictionsType: [{
            id: "CITY",
            name: "City",
            description: "",
            active: true
        },
        {
            id: "WARD",
            name: "Ward",
            description: "",
            active: true
        },
        {
            id: "ZONE",
            name: "Zone",
            description: "",
            active: true
        }
    ],
    jurisdictions_boundary:[],
    yearOfPassing
}

// getCommonMaster("hr-masters", "positions", "Position").responseJSON["Position"] || [];

//common shared object
commonObject["assignments_mainDepartments"] = commonObject["assignments_department"];
commonObject["languagesKnown"] = commonObject["motherTounge"];
commonObject["user_locale"] = commonObject["motherTounge"];
commonObject["probation_designation"] = commonObject["assignments_designation"];
commonObject["regularisation_designation"] = commonObject["assignments_designation"];
commonObject["education_yearOfPassing"]=commonObject["yearOfPassing"];
commonObject["technical_yearOfPassing"]=commonObject["yearOfPassing"];
commonObject["test_yearOfPassing"]=commonObject["yearOfPassing"];

for (var key in commonObject) {
    var splitObject = key.split("_");
    if (splitObject.length < 2) {
        for (var i = 0; i < commonObject[key].length; i++) {
            if (typeof(commonObject[key][i]) === "object")
                $(`#${key}`).append(`<option value='${commonObject[key][i]['id']}'>${typeof(commonObject[key][i]['name'])=="undefined"?commonObject[key][i]['code']:commonObject[key][i]['name']}</option>`)
            else
                $(`#${key}`).append(`<option value='${commonObject[key][i]}'>${commonObject[key][i]}</option>`)
        }
    } else {
        for (var i = 0; i < commonObject[key].length; i++) {
            if (typeof(commonObject[key][i]) === "object")
                $(`#${splitObject[0]}\\.${splitObject[1]}`).append(`<option value='${commonObject[key][i]['id']}'>${commonObject[key][i]['name']}</option>`)
            else
                $(`#${splitObject[0]}\\.${splitObject[1]}`).append(`<option value='${commonObject[key][i]}'>${commonObject[key][i]}</option>`)
        }
    }

}


function getNameById(object, id) {
    // console.log(commonObject[object].length);
    // return commonObject[object].length
    object = returnObject(object);
    for (var i = 0; i < commonObject[object].length; i++) {
        if (commonObject[object][i].id == id) {
            return commonObject[object][i].name;
        }
    }
    return "";
}

var tempListBox=[];
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
    languagesKnown: [],
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
        userName: "",
        name: "",
        gender: "",
        mobileNumber: "",
        emailId: "",
        altContactNumber: "",
        pan: "",
        aadhaarNumber: "",
        permanentAddress: "",
        permanentCity: "",
        permanentPincode: "",
        correspondenceCity: "",
        correspondencePincode: "",
        correspondenceAddress: "",
        active: "true",
        dob: "",
        locale: "",
        signature: "",
        fatherOrHusbandName: "",
        bloodGroup: "",
        identificationMark: "",
        photo: "",
        type:"EMPLOYEE",
        password:"12345678",
        tenantId
    },
    tenantId
}

//temprory object for holding modal value
var employeeSubObject = {
    assignments: {
        fromDate: "",
        toDate: "",
        department: "",
        designation: "",
        position: "",
        isPrimary: "false",
        fund: "",
        function: "",
        functionary: "",
        grade: "",
        hod: "false",
        // mainDepartments: "",
        govtOrderNumber: ""
    },
    jurisdictions:{
      jurisdictionsType:"",
      boundary:""
    },
    serviceHistory: {
        id: 1,
        serviceInfo: "",
        serviceFrom: "",
        remarks: "",
        orderNo: "",
        documents: ""
    },
    probation: {
        designation: "",
        declaredOn: "",
        orderNo: "",
        orderDate: "",
        remarks: "",
        documents: ""
    },
    regularisation: {
        designation: "",
        declaredOn: "",
        orderNo: "",
        orderDate: "",
        remarks: "",
        documents: ""
    },
    education: {
        qualification: "",
        majorSubject: "",
        yearOfPassing: "",
        university: "",
        documents: ""
    },
    technical: {
        skill: "",
        grade: "",
        yearOfPassing: "",
        remarks: "",
        documents: ""
    },
    test: {
        test: "",
        yearOfPassing: "",
        remarks: "",
        documents: ""
    }
}

//unicersal marker for putting edit index
var editIndex = -1;




//form validation
var validation_rules = {};
var final_validatin_rules = {};
var commom_fields_rules = {
    code: {
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
        required: false
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
    languagesKnown: {
        required: false
    },
    maritalStatus: {
        required: false
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
    "user.userName": {
        required: true
    },
    "user.name": {
        required: true
    },
    "user.gender": {
        required: true
    },
    "user.mobileNumber": {
        required: true
    },
    "user.emailId": {
        required: false
    },
    "user.altContactNumber": {
        required: false
    },
    "user.pan": {
        required: false
    },
    "user.aadhaarNumber": {
        required: false
    },
    "user.permanentAddress": {
        required: true
    },
    "user.permanentCity": {
        required: true
    },
    "user.permanentPincode": {
        required: true
    },
    "user.correspondenceCity": {
        required: false
    },
    "user.correspondencePincode": {
        required: false
    },
    "user.correspondenceAddress": {
        required: false
    },
    "user.active": {
        required: true
    },
    "user.dob": {
        required: true
    },
    "user.locale": {
        required: false
    },
    "user.signature": {
        required: false
    },
    "user.fatherOrHusbandName": {
        required: false
    },
    "user.bloodGroup": {
        required: false
    },
    "user.identificationMark": {
        required: false
    },
    "user.photo": {
        required: false
    },
    "assignments.fromDate": {
        required: true
    },
    "assignments.toDate": {
        required: true
    },
    "assignments.fund": {
        required: false
    },
    "assignments.function": {
        required: false
    },
    "assignments.grade": {
        required: false
    },
    "assignments.designation": {
        required: true
    },
    "assignments.position": {
        required: true
    },
    "assignments.functionary": {
        required: false
    },
    "assignments.department": {
        required: true
    },
    "assignments.hod": {
        required: true
    },
    "assignments.mainDepartments": {
        required: true
    },
    "assignments.govtOrderNumber": {
        required: false
    },
    "assignments.is_primary": {
        required: true
    },
    "jurisdictions.jurisdictionsType": {
        required: true
    },
    "jurisdictions.boundary": {
        required: true
    },
    "education.qualification": {
        required: true
    },
    "education.majorSubject": {
        required: false
    },
    "education.yearOfPassing": {
        required: true
    },
    "education.university": {
        required: false
    },
    "education.documents": {
        required: false
    },
    "serviceHistory.id": {
        required: false
    },
    "serviceHistory.serviceInfo": {
        required: true
    },
    "serviceHistory.serviceFrom": {
        required: true
    },
    "serviceHistory.remarks": {
        required: false
    },
    "serviceHistory.orderNo": {
        required: false
    },
    "serviceHistory.documents": {
        required: false
    },
    "probation.designation": {
        required: true
    },
    "probation.declaredOn": {
        required: true
    },
    "probation.orderNo": {
        required: false
    },
    "probation.orderDate": {
        required: false
    },
    "probation.remarks": {
        required: false
    },
    "probation.documents": {
        required: false
    },
    "regularisation.designation": {
        required: true
    },
    "regularisation.declaredOn": {
        required: true
    },
    "regularisation.orderNo": {
        required: false
    },
    "regularisation.orderDate": {
        required: false
    },
    "regularisation.remarks": {
        required: false
    },
    "regularisation.documents": {
        required: false
    },
    "education.qualification": {
        required: true
    },
    "education.majorSubject": {
        required: false
    },
    "education.yearOfPassing": {
        required: true
    },
    "education.university": {
        required: false
    },
    "education.documents": {
        required: false
    },
    "technical.skill": {
        required: true
    },
    "technical.grade": {
        required: false
    },
    "technical.yearOfPassing": {
        required: false
    },
    "technical.remarks": {
        required: false
    },
    "technical.documents": {
        required: false
    },
    "test.test": {
        required: true
    },
    "test.yearOfPassing": {
        required: true
    },
    "test.remarks": {
        required: false
    },
    "test.documents": {
        required: false
    }

}

var assignmentDetailValidation = {
    fromDate: {
        required: true
    },
    toDate: {
        required: true
    },
    fund: {
        required: false
    },
    function: {
        required: false
    },
    grade: {
        required: false
    },
    designation: {
        required: true
    },
    position: {
        required: true
    },
    functionary: {
        required: false
    },
    department: {
        required: true
    },
    hod: {
        required: true
    },
    mainDepartments: {
        required: true
    },
    govtOrderNumber: {
        required: false
    },
    is_primary: {
        required: true
    }
};

var jurisdictions = {
    jurisdictionsType: {
        required: true
    },
    boundary: {
        required: true
    }
};

var serviceHistory = {
    id: {
        required: false
    },
    serviceInfo: {
        required: true
    },
    serviceFrom: {
        required: true
    },
    remarks: {
        required: false
    },
    orderNo: {
        required: false
    },
    documents: {
        required: false
    }
};

var probation = {
    designation: {
        required: true
    },
    declaredOn: {
        required: true
    },
    orderNo: {
        required: false
    },
    orderDate: {
        required: false
    },
    remarks: {
        required: false
    },
    documents: {
        required: false
    }
};

var regularisation = {
    designation: {
        required: true
    },
    declaredOn: {
        required: true
    },
    orderNo: {
        required: false
    },
    orderDate: {
        required: false
    },
    remarks: {
        required: false
    },
    documents: {
        required: false
    }
};

var education = {
    qualification: {
        required: true
    },
    majorSubject: {
        required: false
    },
    yearOfPassing: {
        required: true
    },
    university: {
        required: false
    },
    documents: {
        required: false
    }
};

var technical = {
    skill: {
        required: true
    },
    grade: {
        required: false
    },
    yearOfPassing: {
        required: false
    },
    remarks: {
        required: false
    },
    documents: {
        required: false
    }
}

var test = {
    test: {
        required: true
    },
    yearOfPassing: {
        required: true
    },
    remarks: {
        required: false
    },
    documents: {
        required: false
    }
};

var user = {
    userName: {
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
    emailId: {
        required: false
    },
    altContactNumber: {
        required: false
    },
    pan: {
        required: false
    },
    aadhaarNumber: {
        required: false
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
    correspondenceCity: {
        required: false
    },
    correspondencePincode: {
        required: false
    },
    correspondenceAddress: {
        required: false
    },
    active: {
        required: true
    },
    dob: {
        required: true
    },
    locale: {
        required: false
    },
    signature: {
        required: false
    },
    fatherOrHusbandName: {
        required: false
    },
    bloodGroup: {
        required: false
    },
    identificationMark: {
        required: false
    },
    photo: {
        required: false
    }
}


//Getting data for user input
$("input").on("keyup change changeDate", function() {
    fillValueToObject(this);
});

//Getting data for user input
$("select").on("change", function() {
    // if (this.id == "jurisdictions.jurisdictionsType") return;
    // else {
        if(this.id=="bank")
        {
          commonObject["bankbranches"]=commonApiPost("egf-masters", "bankbranches", "_search",{tenantId}).responseJSON["bankBranches"] || [];
          $(`#bankBranch`).html(`<option value=''>Select</option>`)
          for (var i = 0; i < commonObject["bankbranches"].length; i++) {
              $(`#bankBranch`).append(`<option value='${commonObject["bankbranches"][i]['id']}'>${commonObject["bankbranches"][i]['name']}</option>`)
          }
        }
        if(this.id=="jurisdictions.jurisdictionsType")
        {
          commonObject["jurisdictions_boundary"]=commonApiPost("v1/location", "boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:this.value,hierarchyTypeName:"ADMINISTRATION"}).responseJSON["Boundary"] || [];
          $(`#jurisdictions\\.boundary`).html(`<option value=''>Select</option>`)

          for (var i = 0; i < commonObject["jurisdictions_boundary"].length; i++) {
              $(`#jurisdictions\\.boundary`).append(`<option value='${commonObject["jurisdictions_boundary"][i]['id']}'>${commonObject["jurisdictions_boundary"][i]['name']}</option>`)
          }
          // return;
        }
        if ((employeeSubObject["assignments"]["department"]!="" && employeeSubObject["assignments"]["designation"]!="")&&(this.id=="assignments.department" ||this.id=="assignments.designation")) {
          commonObject["assignments_position"]=commonApiPost("hr-masters", "positions", "_search",{tenantId,departmentId:employeeSubObject["assignments"]["department"],designationId:employeeSubObject["assignments"]["designation"]}).responseJSON["Position"] || [];
          $(`#assignments\\.position`).html(`<option value=''>Select</option>`)

          for (var i = 0; i < commonObject["assignments_position"].length; i++) {
              $(`#assignments\\.position`).append(`<option value='${commonObject["assignments_position"][i]['id']}'>${commonObject["assignments_position"][i]['name']}</option>`)
          }
        }
        fillValueToObject(this);
    // }
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
        if (splitResult[0] === "user") {
            // if(currentState.id=="user.dob")
            // {
            //   var dateSplit=currentState.value.split("-");
            //   var date=new Date(dateSplit[0],dateSplit[1],dateSplit[2]);
            //   var day=date.getDate().toString().length===1?"0"+date.getDate():date.getDate();
            //   var monthIn=date.getMonth().toString().length===1?"0"+date.getMonth():date.getMonth();
            //   var yearIn=date.getFullYear();
            //   employee[splitResult[0]][splitResult[1]] = day+"/"+monthIn+"/"+yearIn;
            //
            // }
            if(currentState.id=="user.active")
            {
              employee[splitResult[0]][splitResult[1]] = currentState.value;

            }
            else {
              employee[splitResult[0]][splitResult[1]] = currentState.value;

            }
        } else
        {
            if( currentState.id=="assignments.fromDate" || currentState.id=="assignments.toDate" ||currentState.id=="serviceHistory.serviceFrom" ||currentState.id=="probation.orderDate" ||currentState.id=="probation.declaredOn" ||currentState.id=="regularisation.declaredOn" ||currentState.id=="regularisation.orderDate" ||currentState.id=="education.yearOfPassing" ||currentState.id=="technical.yearOfPassing" || currentState.id=="test.yearOfPassing")
            {
              var dateSplit=currentState.value.split("-");
              var date=new Date(dateSplit[0],dateSplit[1],dateSplit[2]);
              var day=date.getDate().toString().length===1?"0"+date.getDate():date.getDate();
              var monthIn=date.getMonth().toString().length===1?"0"+date.getMonth():date.getMonth();
              var yearIn=date.getFullYear();
              employeeSubObject[splitResult[0]][splitResult[1]] = day+"/"+monthIn+"/"+yearIn;

            }
            else if (currentState.id=="assignments.mainDepartments") {
                  tempListBox.push({department:currentState.value});
            }
            // else if(currentState.id=="assignments.isPrimary")
            // {
            //   employeeSubObject[splitResult[0]][splitResult[1]]=currentState.value=="No"?false:true;
            // }
            // if (currentState.id=="jurisdictions.boundary") {
            //       employeeSubObject[splitResult[0]][splitResult[1]].push(currentState.value);
            // }

            else {
              employeeSubObject[splitResult[0]][splitResult[1]] = currentState.value;

            }
        }

    } else {
        if(currentState.id=="dateOfAppointment" ||currentState.id=="dateOfRetirement" ||currentState.id=="dateOfTermination" ||currentState.id =="retirementAge" ||currentState.id=="dateOfJoining" || currentState.id=="dateOfRetirement" || currentState.id=="dateOfTermination" ||currentState.id=="dateOfResignation")
        {
          var dateSplit=currentState.value.split("-");
          var date=new Date(dateSplit[0],dateSplit[1],dateSplit[2]);
          var day=date.getDate().toString().length===1?"0"+date.getDate():date.getDate();
          var monthIn=date.getMonth().toString().length===1?"0"+date.getMonth():date.getMonth();
          var yearIn=date.getFullYear();
          employee[currentState.id] = day+"/"+monthIn+"/"+yearIn;
        }
        else if(currentState.id=="languagesKnown")
        {
            employee[currentState.id].push(currentState.value);
        }
        else {
          employee[currentState.id] = currentState.value;
        }
    }
}

function clearModalInput(object, properties) {
    for (var variable in properties) {
        if (properties.hasOwnProperty(variable)) {
            $("#" + object + "\\." + variable).val(properties[variable]);
        }
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
        position: "No",
        functionary: "",
        department: "",
        hod: "No",
        // mainDepartments: "",
        govtOrderNumber: "",
        is_primary: ""
    };
    clearModalInput("assignments", employeeSubObject["assignments"]);
})

$('#jurisdictionDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["jurisdictions"] = {
        jurisdictionsType: "",
        boundary: ""
    }
    clearModalInput("jurisdictions", employeeSubObject["jurisdictions"]);

})

$('#serviceHistoryDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["serviceHistory"] = {
        id: employee["serviceHistory"].length+1,
        serviceInfo: "",
        serviceFrom: "",
        remarks: "",
        orderNo: "",
        documents: ""
    }
    clearModalInput("serviceHistory", employeeSubObject["serviceHistory"]);

})

$('#probationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["probation"] = {
        designation: "",
        declaredOn: "",
        orderNo: "",
        orderDate: "",
        remarks: "",
        documents: ""
    }
    clearModalInput("probation", employeeSubObject["probation"]);

})

$('#regularisationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["regularisation"] = {
        designation: "",
        declaredOn: "",
        orderNo: "",
        orderDate: "",
        remarks: "",
        documents: ""
    }
    clearModalInput("regularisation", employeeSubObject["regularisation"]);

})

$('#technicalDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["technical"] = {
        skill: "",
        grade: "",
        yearOfPassing: "",
        remarks: "",
        documents: ""
    }
    clearModalInput("technical", employeeSubObject["technical"]);

})

$('#educationDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["education"] = {
        qualification: "",
        majorSubject: "",
        yearOfPassing: "",
        university: "",
        documents: ""
    }
    clearModalInput("education", employeeSubObject["education"]);

})

$('#testDetailModal').on('hidden.bs.modal', function(e) {
    editIndex = -1;
    employeeSubObject["test"] = {
        test: "",
        yearOfPassing: "",
        remarks: "",
        documents: ""
    }
    clearModalInput("test", employeeSubObject["test"]);

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
            if (key === "department" || key === "designation" || key === "position" || key === "fund" || key === "function" || key === "functionary" || key === "grade" || key === "mainDepartments" || key === "jurisdictionsType" || key === "boundary") {
                $(tableName).append(`<td data-label=${key}>
                                  ${getNameById(key,employee[object][i][key])}
                            </td>`)
            } else {
                $(tableName).append(`<td data-label=${key}>
                                  ${employee[object][i][key]}
                            </td>`)
            }

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
                if( object+"."+key=="assignments.fromDate" || object+"."+key=="assignments.toDate" ||object+"."+key=="serviceHistory.serviceFrom" ||object+"."+key=="probation.orderDate" ||object+"."+key=="probation.declaredOn" ||object+"."+key=="regularisation.declaredOn" || object+"."+key=="regularisation.orderDate" || object+"."+key=="education.yearOfPassing" || object+"."+key=="technical.yearOfPassing" || object+"."+key=="test.yearOfPassing")
                {
                  var dateSplit=employeeSubObject[object][key].split("/");
                  var date=new Date(dateSplit[0],dateSplit[1],dateSplit[2]);
                  // var day=date.getDate().toString().length===1?"0"+date.getDate():date.getDate();
                  // var monthIn=date.getMonth().toString().length===1?"0"+date.getMonth():date.getMonth();
                  // var yearIn=date.getFullYear();
                  // employeeSubObject[splitResult[0]][splitResult[1]] = day+"/"+monthIn+"/"+yearIn;
                  $(`#${object}\\.${key}`).val(date);

                }
                else {
                  $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
                }
            }
        }
    })
    // alert("fired");
}

//common add and update
function commonAddAndUpdate(tableName, modalName, object) {
    // if(switchValidation(object))
    if ($("#createEmployeeForm").valid()) {
        if (editIndex != -1) {
            employee[object][editIndex] = employeeSubObject[object];
            updateTable("#" + tableName, modalName, object);
        } else {
            if (object=="assignments") {
              employeeSubObject[object]["hod"]=[];
              if(tempListBox.length>0)
              {
                employeeSubObject[object]["hod"].push(tempListBox);

              }
              employee[object].push(Object.assign({}, employeeSubObject[object]));
              updateTable("#" + tableName, modalName, object);
            }
            else {
              employee[object].push(Object.assign({}, employeeSubObject[object]));
              updateTable("#" + tableName, modalName, object);
            }

        }
        $(`#${modalName}`).modal("hide");
    } else {
        return;
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
        // console.log(key.split("."));
        if (key.split(".").length == 1) {
            $(`label[for=${key}]`).append(`<span> *</span>`);

        }
    }
    // $(`#${key}`).attr("disabled",true);
};

// $.validator.addMethod('phone', function(value) {
//     return /^[0-9]{10}$/.test(value);
// }, 'Please enter a valid phone number.');
//
// $.validator.addMethod('aadhar', function(value) {
//     return /^[0-9]{12}$/.test(value);
// }, 'Please enter a valid aadhar.');
//
// $.validator.addMethod('pan_no', function(value) {
//     return /^[0-9a-zA-Z]{10}$/.test(value);
// }, 'Please enter a valid pan.');

$("#addEmployee").on("click", function(e) {
    e.preventDefault();
     $("#createEmployeeForm").submit();
    // switchValidation("final_validatin_rules");
})








function addMandatoryStart(validationObject, prefix = "") {
    for (var key in validationObject) {
        if (prefix === "") {
            if (validationObject[key].required) {
                $(`label[for=${key}]`).append(`<span> *</span>`);
            }
        } else {
            if (validationObject[key].required) {
                $(`label[for=${prefix}\\.${key}]`).append(`<span> *</span>`);
            }
        }

        // $(`#${key}`).attr("disabled",true);
    };
}

addMandatoryStart(assignmentDetailValidation, "assignments");

addMandatoryStart(jurisdictions, "jurisdictions");

addMandatoryStart(serviceHistory, "serviceHistory");

addMandatoryStart(probation, "probation");

addMandatoryStart(regularisation, "regularisation");

addMandatoryStart(education, "education");

addMandatoryStart(test, "test");

addMandatoryStart(technical, "technical");

addMandatoryStart(user, "user");

// function switchValidation(whichObject) {
//     switch (whichObject) {
//         case "final_validatin_rules":
//             // removeRule({assignments,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(final_validatin_rules);
//             $("#createEmployeeForm").submit();
//         case "assignments":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(assignmentDetailValidation, "assignments");
//             return $("#createEmployeeForm").valid();
//         case "jurisdictions":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(jurisdictions, "jurisdictions");
//             return $("#createEmployeeForm").valid();
//         case "serviceHistory":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(serviceHistory, "serviceHistory");
//             return $("#createEmployeeForm").valid();
//         case "probation":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(probation, "probation");
//             return $("#createEmployeeForm").valid();
//         case "regularisation":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(regularisation, "regularisation");
//             return $("#createEmployeeForm").valid();
//         case "education":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(education, "education")
//             return $("#createEmployeeForm").valid();
//         case "technical":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(technical, "technical");
//             return $("#createEmployeeForm").valid();
//         case "test":
//             // removeRule({final_validatin_rules,jurisdictions,serviceHistory,probation,regularisation,education,test,technical});
//             addRule(test, "test");
//             return $("#createEmployeeForm").valid();
//         default:
//
//     }
// }

// function removeRule(arrayOfObject) {
//     // console.log(arrayOfObject);
//     for (var item in arrayOfObject) {
//         if (item === "final_validatin_rules") {
//             for (var itemInner in arrayOfObject[item]) {
//
//                 $(`#${itemInner}`).rules("remove");
//
//             }
//         } else {
//             for (var itemInner in arrayOfObject[item]) {
//
//                 $(`#${item}\\.${itemInner}`).rules("remove");
//
//             }
//         }
//     }
//
// }
// function addRule(object, name) {
//     // console.log(object);
//     for (var item in object) {
//         if (object[item].required) {
//             $(`#${name}\\.${item}`).rules("add", object[item]);
//         }
//     }
// }




// Adding Jquery validation dynamically
$("#createEmployeeForm").validate({
    rules: final_validatin_rules,
    submitHandler: function(form) {
        // console.log(form);

        if (employee.assignments.length > 0 && employee.jurisdictions.length > 0) {
            //call api
            console.log("calling api");

            var empJuridictiona=employee["jurisdictions"];
            employee["jurisdictions"]=[];
            for (var i = 0; i < empJuridictiona.length; i++) {
              employee["jurisdictions"].push(empJuridictiona[i].boundary);
            }



            var response=$.ajax({
                      url: baseUrl+"/hr-employee/employees/_create?tenantId=1",
                      type: 'POST',
                      dataType: 'json',
                      data:JSON.stringify({
                          RequestInfo: requestInfo,
                          Employee: employee
                      }),
                      async: false,
                      headers: {
                              'auth-token': authToken
                          },
                      contentType: 'application/json'
                  });

                  if(response["status"]===200)
                  {
                    alert("Successfully added");
                    window.location.href="../../../../app/hr/common/employee-attendance.html";
                  }
                  else {
                    alert(response["statusText"]);
                  }


            // $.post(`${baseUrl}hr-employee/employees/_create?tenantId=1`, {
            //     RequestInfo: requestInfo,
            //     Employee: employee
            // }, function(response) {
            //     alert("submit");
            //     // window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
            //     console.log(response);
            // },function(error){
            //   alert("error")
            //   console.log(error);
            // })
        } else {
            alert("Please enter atleast assignment and jurisdiction");
        }
        //alert("submitterd");
        // form.submit();

        // console.log(agreement);



    }
})
