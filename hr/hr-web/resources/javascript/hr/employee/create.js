
var employeeType, employeeStatus, group, motherTongue, religion, community, category, bank, recruitmentMode, recruitmentType, recruitmentQuota, assignments_grade, assignments_designation, assignments_department, assignments_fund, assignments_functionary, assignments_function,assignments_position, maritalStatus, user_bloodGroup;

try { employeeType = !localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined" ? (localStorage.setItem("employeeType", JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [])), JSON.parse(localStorage.getItem("employeeType"))) : JSON.parse(localStorage.getItem("employeeType")); } catch (e) {
    console.log(e);
    employeeType = [];
}
try { employeeStatus = !localStorage.getItem("employeeStatus") || localStorage.getItem("employeeStatus") == "undefined" ? (localStorage.setItem("employeeStatus", JSON.stringify(getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"] || [])), JSON.parse(localStorage.getItem("employeeStatus"))) : JSON.parse(localStorage.getItem("employeeStatus")); } catch (e) {
    console.log(e);
    employeeStatus = [];
}
try { group = !localStorage.getItem("group") || localStorage.getItem("group") == "undefined" ? (localStorage.setItem("group", JSON.stringify(getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"] || [])), JSON.parse(localStorage.getItem("group"))) : JSON.parse(localStorage.getItem("group")); } catch (e) {
    console.log(e);
    group = [];
}
try { maritalStatus = !localStorage.getItem("maritalStatus") || localStorage.getItem("maritalStatus") == "undefined" ? (localStorage.setItem("maritalStatus", JSON.stringify(commonApiPost("hr-employee", "maritalstatuses", "_search", {tenantId, pageSize:500}).responseJSON["MaritalStatus"] || [])), JSON.parse(localStorage.getItem("maritalStatus"))) : JSON.parse(localStorage.getItem("maritalStatus")); } catch (e) {
    console.log(e);
    maritalStatus = [];
}
try { user_bloodGroup = !localStorage.getItem("user_bloodGroup") || localStorage.getItem("user_bloodGroup") == "undefined" ? (localStorage.setItem("user_bloodGroup", JSON.stringify(commonApiPost("hr-employee", "bloodgroups", "_search", {tenantId, pageSize:500}).responseJSON["BloodGroup"] || [])), JSON.parse(localStorage.getItem("user_bloodGroup"))) : JSON.parse(localStorage.getItem("user_bloodGroup")); } catch (e) {
    console.log(e);
    user_bloodGroup = [];
}
try { motherTongue = !localStorage.getItem("motherTongue") || localStorage.getItem("motherTongue") == "undefined" ? (localStorage.setItem("motherTongue", JSON.stringify(getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"] || [])), JSON.parse(localStorage.getItem("motherTongue"))) : JSON.parse(localStorage.getItem("motherTongue")); } catch (e) {
    console.log(e);
    motherTongue = [];
}
try { religion = !localStorage.getItem("religion") || localStorage.getItem("religion") == "undefined" ? (localStorage.setItem("religion", JSON.stringify(getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"] || [])), JSON.parse(localStorage.getItem("religion"))) : JSON.parse(localStorage.getItem("religion")); } catch (e) {
    console.log(e);
    religion = [];
}
try { community = !localStorage.getItem("community") || localStorage.getItem("community") == "undefined" ? (localStorage.setItem("community", JSON.stringify(getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"] || [])), JSON.parse(localStorage.getItem("community"))) : JSON.parse(localStorage.getItem("community")); } catch (e) {
    console.log(e);
    community = [];
}
try { category = !localStorage.getItem("category") || localStorage.getItem("category") == "undefined" ? (localStorage.setItem("category", JSON.stringify(getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"] || [])), JSON.parse(localStorage.getItem("category"))) : JSON.parse(localStorage.getItem("category")); } catch (e) {
    console.log(e);
    category = [];
}
try { bank = !localStorage.getItem("bank") || localStorage.getItem("bank") == "undefined" ? (localStorage.setItem("bank", JSON.stringify(getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"] || [])), JSON.parse(localStorage.getItem("bank"))) : JSON.parse(localStorage.getItem("bank")); } catch (e) {
    console.log(e);
    bank = [];
}
try { recruitmentMode = !localStorage.getItem("recruitmentMode") || localStorage.getItem("recruitmentMode") == "undefined" ? (localStorage.setItem("recruitmentMode", JSON.stringify(getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"] || [])), JSON.parse(localStorage.getItem("recruitmentMode"))) : JSON.parse(localStorage.getItem("recruitmentMode")); } catch (e) {
    console.log(e);
    recruitmentMode = [];
}
try { recruitmentType = !localStorage.getItem("recruitmentType") || localStorage.getItem("recruitmentType") == "undefined" ? (localStorage.setItem("recruitmentType", JSON.stringify(getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"] || [])), JSON.parse(localStorage.getItem("recruitmentType"))) : JSON.parse(localStorage.getItem("recruitmentType")); } catch (e) {
    console.log(e);
    recruitmentType = [];
}
try { recruitmentQuota = !localStorage.getItem("recruitmentQuota") || localStorage.getItem("recruitmentQuota") == "undefined" ? (localStorage.setItem("recruitmentQuota", JSON.stringify(getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"] || [])), JSON.parse(localStorage.getItem("recruitmentQuota"))) : JSON.parse(localStorage.getItem("recruitmentQuota")); } catch (e) {
    console.log(e);
    recruitmentQuota = [];
}
try { assignments_grade = !localStorage.getItem("assignments_grade") || localStorage.getItem("assignments_grade") == "undefined" ? (localStorage.setItem("assignments_grade", JSON.stringify(getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"] || [])), JSON.parse(localStorage.getItem("assignments_grade"))) : JSON.parse(localStorage.getItem("assignments_grade")); } catch (e) {
    console.log(e);
    assignments_grade = [];
}
try { assignments_fund = !localStorage.getItem("assignments_fund") || localStorage.getItem("assignments_fund") == "undefined" ? (localStorage.setItem("assignments_fund", JSON.stringify(getCommonMaster("egf-masters", "funds", "funds").responseJSON["funds"])) || []) : JSON.parse(localStorage.getItem("assignments_fund")); } catch (e) {
    console.log(e);
    assignments_fund = [];
}
try { assignments_functionary = !localStorage.getItem("assignments_functionary") || localStorage.getItem("assignments_functionary") == "undefined" ? (localStorage.setItem("assignments_functionary", JSON.stringify(getCommonMaster("egf-masters", "functionaries", "funds").responseJSON["functionaries"] || [])), JSON.parse(localStorage.getItem("assignments_functionary"))) : JSON.parse(localStorage.getItem("assignments_functionary")); } catch (e) {
    console.log(e);
    assignments_functionary = [];
}
try { assignments_function = !localStorage.getItem("assignments_function") || localStorage.getItem("assignments_function") == "undefined" ? (localStorage.setItem("assignments_function", JSON.stringify(getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"] || [])), JSON.parse(localStorage.getItem("assignments_function"))) : JSON.parse(localStorage.getItem("assignments_function")); } catch (e) {
    console.log(e);
    assignments_function = [];
}

try { jurisdictions_jurisdictionsType = !localStorage.getItem("jurisdictions_jurisdictionsType") || localStorage.getItem("jurisdictions_jurisdictionsType") == "undefined" ? (localStorage.setItem("jurisdictions_jurisdictionsType", JSON.stringify(commonApiPost("egov-location/boundarytypes", "getByHierarchyType", "", { tenantId, hierarchyTypeName: "ADMINISTRATION" }).responseJSON["BoundaryType"] || [])), JSON.parse(localStorage.getItem("jurisdictions_jurisdictionsType"))) : JSON.parse(localStorage.getItem("jurisdictions_jurisdictionsType")); } catch (e) {
    console.log(e);
    jurisdictions_jurisdictionsType = [];
}
try {
  assignments_position = !localStorage.getItem("assignments_position") || localStorage.getItem("assignments_position") == "undefined" ? (localStorage.setItem("assignments_position", JSON.stringify(getCommonMaster("hr-masters", "positions", "Position").responseJSON["Position"] || [])), JSON.parse(localStorage.getItem("assignments_position"))) : JSON.parse(localStorage.getItem("assignments_position"));
} catch (e) {
  console.log(e);
  assignments_position = [];

}
try {
    assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [])), JSON.parse(localStorage.getItem("assignments_designation"))) : JSON.parse(localStorage.getItem("assignments_designation"));
} catch (e) {
    console.log(e);
      assignments_designation = [];
}
try {
   assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
} catch (e) {
    console.log(e);
    assignments_department = [];
}


$('#close').on("click", function() {
  window.close();
})


function returnObject(alies, optional) {
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
      if (optional == "CITY") {
        return "juridictionTypeForCity";
      } else if (optional == "WARD") {
        return "juridictionTypeForWard";
      } else {
        return "juridictionTypeForZone";
      }
      break;
    default:

  }

}


// localStorage.setItem("employeeType",JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"]));
// var employeeType=JSON.parse(localStorage.getItem("employeeType"))==""?(localStorage.setItem("employeeType",JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"]))|| []) :JSON.parse(localStorage.getItem("employeeType"));
// console.log(employeeType);
//common object
var yearOfPassing = [];

for (var i = 2000; i <= new Date().getFullYear(); i++) {
  yearOfPassing.push(i);
}

var hrConfigurations = commonApiPost("hr-masters", "hrconfigurations", "_search", {
  tenantId
}).responseJSON || [];
if (hrConfigurations["HRConfiguration"]["Autogenerate_employeecode"] == "N" || typeof(hrConfigurations["HRConfiguration"]["Autogenerate_employeecode"]) == "undefined") {
  $("#code").prop("disabled", false);
} else {
  $("#code").prop("disabled", true);
}

// console.log(yearOfPassing);
// var commonObject = {
//     employeeType: getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [],
//     employeeStatus: getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"] || [],
//     group: getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"] || [],
//     maritalStatus: ["MARRIED", "UNMARRIED", "DIVORCED", "WIDOWER", "WIDOW"],
//     user_bloodGroup: ["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"],
//     motherTongue: getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"] || [],
//     religion: getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"] || [],
//     community: getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"] || [],
//     category: getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"] || [],
//     bank: getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"] || [],
//     bankBranch: [],
//     recruitmentMode: getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"] || [],
//     recruitmentType: getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"] || [],
//     recruitmentQuota: getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"] || [],
//     assignments_fund: [{
//             id: 1,
//             name: "Own",
//             description: ""
//         },
//         {
//             id: 2,
//             name: "Company",
//             description: ""
//         }
//     ],
//     assignments_function: [{
//             id: 1,
//             name: "IR",
//             description: ""
//         },
//         {
//             id: 2,
//             name: "No-IT",
//             description: ""
//         }
//     ],
//     assignments_functionary: [{
//             id: 1,
//             name: "developrment",
//             description: ""
//         },
//         {
//             id: 2,
//             name: "no developrment",
//             description: ""
//         }
//     ],
//     assignments_grade: getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"] || [],
//     assignments_designation: getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [],
//     assignments_position: [],
//     assignments_department: getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [],
//     jurisdictions_jurisdictionsType: [{
//             id: "CITY",
//             name: "City",
//             description: "",
//             active: true
//         },
//         {
//             id: "WARD",
//             name: "Ward",
//             description: "",
//             active: true
//         },
//         {
//             id: "ZONE",
//             name: "Zone",
//             description: "",
//             active: true
//         }
//     ],
//     jurisdictions_boundary: [],
//     yearOfPassing
// }
// var juridictionTypeForCity=[];
// var juridictionTypeForWard=[];
// var juridictionTypeForZone=[];


var commonObject = {
  employeeType,
  employeeStatus,
  group,
  maritalStatus,
  user_bloodGroup,
  motherTongue,
  religion,
  community,
  category,
  bank,
  bankBranch: [],
  recruitmentMode,
  recruitmentType,
  recruitmentQuota,
  assignments_fund,
  assignments_function,
  assignments_functionary,
  assignments_grade,
  assignments_designation,
  assignments_position: [],
  assignments_department,
  jurisdictions_jurisdictionsType,
  jurisdictions_boundary: [],
  yearOfPassing,
  juridictionTypeForCity: [],
  juridictionTypeForWard: [],
  juridictionTypeForZone: []
}

// getCommonMaster("hr-masters", "positions", "Position").responseJSON["Position"] || [];

//common shared object
commonObject["assignments_mainDepartments"] = commonObject["assignments_department"];
commonObject["languagesKnown"] = commonObject["motherTongue"];
commonObject["user_locale"] = commonObject["motherTongue"];
commonObject["probation_designation"] = commonObject["assignments_designation"];
commonObject["regularisation_designation"] = commonObject["assignments_designation"];
commonObject["education_yearOfPassing"] = commonObject["yearOfPassing"];
commonObject["technical_yearOfPassing"] = commonObject["yearOfPassing"];
commonObject["test_yearOfPassing"] = commonObject["yearOfPassing"];

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


function getNameById(object, id, optional) {
  // console.log(commonObject[object].length);
  // return commonObject[object].length
  object = returnObject(object, optional);
  for (var i = 0; i < commonObject[object].length; i++) {
    if (commonObject[object][i].id == id) {
      return commonObject[object][i].name;
    }
  }
  return "";
}

var tempListBox = [];
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
  motherTongue: "",
  religion: "",
  community: "",
  category: "",
  physicallyDisabled: false,
  medicalReportProduced: true,
  languagesKnown: [],
  maritalStatus: "",
  passportNo: null,
  gpfNo: null,
  bank: "",
  bankBranch: "",
  bankAccount: "",
  group: "",
  placeOfBirth: "",
  documents: null,
  serviceHistory: [],
  probation: [],
  regularisation: [],
  technical: [],
  education: [],
  test: [],
  user: {
    roles: [{
      code: "EMPLOYEE",
      name: "EMPLOYEE",
      tenantId
    }],
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
    permanentPinCode: "",
    correspondenceCity: "",
    correspondencePinCode: "",
    correspondenceAddress: "",
    active: true,
    dob: "",
    locale: "",
    signature: "",
    fatherOrHusbandName: "",
    bloodGroup: null,
    identificationMark: "",
    photo: "",
    type: "EMPLOYEE",
    password: "12345678",
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
    isPrimary: false,
    fund: "",
    function: "",
    functionary: "",
    grade: "",
    hod: false,
    // mainDepartments: "",
    govtOrderNumber: "",
    documents: null
  },
  jurisdictions: {
    jurisdictionsType: "",
    boundary: ""
  },
  serviceHistory: {
    serviceInfo: "",
    serviceFrom: "",
    remarks: "",
    orderNo: "",
    documents: null
  },
  probation: {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
  },
  regularisation: {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
  },
  education: {
    qualification: "",
    majorSubject: "",
    yearOfPassing: "",
    university: "",
    documents: null
  },
  technical: {
    skill: "",
    grade: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
  },
  test: {
    test: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
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
    required: true
  },
  motherTongue: {
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
    required: true,
    phone: true
  },
  "user.emailId": {
    required: false
  },
  "user.altContactNumber": {
    required: false,
    phone:true
  },
  "user.pan": {
    required: false,
    panNo:true
  },
  "user.aadhaarNumber": {
    required: false,
    aadhar:true
  },
  "user.permanentAddress": {
    required: false
  },
  "user.permanentCity": {
    required: false
  },
  "user.permanentPinCode": {
    required: false
  },
  "user.correspondenceCity": {
    required: false
  },
  "user.correspondencePinCode": {
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
    required: false,

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
    required: false
  },
  permanentCity: {
    required: false
  },
  permanentPinCode: {
    required: false
  },
  correspondenceCity: {
    required: false
  },
  correspondencePinCode: {
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

$("input[name='user.dob']").datepicker({
  format: 'dd/mm/yyyy',
  endDate: '-15y',
  autoclose: true
});

// $("input[name='user.dob']").val("");
// $("input[name='user.dob']").on("change", function(e) {
//       fillValueToObject(this);
//   });


$('#dateOfAppointment').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfAppointment').on("change", function(e) {
  // fillValueToObject(this);
  // $('#dateOfJoining').val(this.value);
  // this.id="dateOfJoining";
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfAppointment").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 < date2) {
    showError("Appointment date must be before Joining date");
    $("#dateOfAppointment").val("");
  }

});




$('#dateOfJoining').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfJoining').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfAppointment").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 < date2) {
    showError("Appointment date must be before Joining date");
    $("#dateOfAppointment").val("");
  }

});

$('#dateOfJoining').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfJoining').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfRetirement").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 >= date2) {
    showError("Date of retairement must be after Joining date");
    $("#dateOfRetirement").val("");
  }

});



$('#dateOfRetirement').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfRetirement').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfRetirement").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 > date2) {
    showError("Retirement date must be after Joining date");
    $("#dateOfRetirement").val("");
  } else {}
});



$('#dateOfJoining').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfJoining').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfTermination").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 >= date2) {
    showError("Date of Termination must be after Joining date");
    $("#dateOfTermination").val("");
  }

});


$('#dateOfTermination').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfTermination').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfTermination").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 > date2) {
    showError("Termination date must be after Joining date");
    $("#dateOfTermination").val("");
  } else {}

});

$('#dateOfJoining').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfJoining').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfResignation").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 >= date2) {
    showError("Date of Designation  must be after Joining date");
    $("#dateOfResignation").val("");
  }

});

$('#dateOfResignation').datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$('#dateOfResignation').on("change", function(e) {
  // fillValueToObject(this);
  var date_received = $("#dateOfJoining").val();
  var date_completed = $("#dateOfResignation").val();
  var dateParts = date_received.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = date_completed.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 > date2) {
    showError("Resignation date must be after Joining date");
    $("#dateOfResignation").val("");
  } else {}
});



$("input[name='assignments.fromDate']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$("input[name='assignments.fromDate']").on("change", function(e) {
  // fillValueToObject(this);
  var from = $("input[name='assignments.fromDate']").val();
  var to = $("input[name='assignments.toDate']").val();
  var dateParts = from.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);

  var dateParts = to.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 > date2) {
    showError("End date must be after From date");
    $("input[name='assignments.toDate']").val("");
  } else {}
});



$("input[name='assignments.toDate']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$("input[name='assignments.toDate']").on("change", function(e) {
  // fillValueToObject(this);
  var from = $("input[name='assignments.fromDate']").val();
  var to = $("input[name='assignments.toDate']").val();
  var dateParts = from.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
  var date1 = new Date(newDateStr);
  var dateParts = to.split("/");
  var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
  var date2 = new Date(newDateStr);
  if (date1 > date2) {
    showError("End date must be after From date");
    $("input[name='assignments.toDate']").val("");
  } else {}
});




$("input[name='serviceHistory.serviceFrom']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});


$("input[name='probation.orderDate']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});


$("input[name='probation.declaredOn']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});
$("input[name='regularisation.orderDate']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});

$("input[name='regularisation.declaredOn']").datepicker({
  format: 'dd/mm/yyyy',
  autoclose: true
});









var startDate;
$("#dateOfJoining").datepicker({
  timepicker: true,
  closeOnDateSelect: false,
  closeOnTimeSelect: true,
  initTime: true,
  format: 'dd/mm/yyyy',
  minDate: 0,
  onChangeDateTime: function(dp, $input) {
    startDate = $("#startdate").val();
  },
  autoclose: true
});
$("#dateOfRetirement").datepicker({
  timepicker: true,
  closeOnDateSelect: false,
  closeOnTimeSelect: true,
  initTime: true,
  format: 'dd/mm/yyyy',
  onClose: function(current_time, $input) {
    var endDate = $("#enddate").val();
    if (startDate > endDate) {
      alert('Please select correct date');
    }
  },
  autoclose: true
});



// $('.datepicker').datepicker({
//       format: 'dd/mm/yyyy'
//
// });
//
// $(".datepicker").on("change", function() {
//     // alert('hey');
//     fillValueToObject(this);
// });
// .on('changeDate', function (ev) {
//     $('#date-daily').change();
// });
//
// $('#date-daily').val('0000-00-00');
// $('#date-daily').change(function () {
//     console.log($('#date-daily').val());
// });

// $('#user\\.dob').on("change",function()
// {
//     fillValueToObject(this);
// })
//Getting data for user input
$("input").on("keyup change", function() {
  fillValueToObject(this);
  getPositions(this);
});

//Getting data for user input
$("select").on("change", function() {
  // if (this.id == "jurisdictions.jurisdictionsType") return;
  // else {
  if (this.id == "bank") {
    commonObject["bankbranches"] = commonApiPost("egf-masters", "bankbranches", "_search", {
      tenantId,
      "bank.id": this.value
    }).responseJSON["bankBranches"] || [];
    $(`#bankBranch`).html(`<option value=''>Select</option>`)
    for (var i = 0; i < commonObject["bankbranches"].length; i++) {
      $(`#bankBranch`).append(`<option value='${commonObject["bankbranches"][i]['id']}'>${commonObject["bankbranches"][i]['name']}</option>`)
    }
  }
  if (this.id == "jurisdictions.jurisdictionsType") {
    commonObject["jurisdictions_boundary"] = commonApiPost("egov-location", "boundarys", "getByBoundaryType", {
      boundaryTypeId: this.value,
      tenantId
    }).responseJSON["Boundary"] || [];
    if (this.value == "CITY") {
      commonObject["juridictionTypeForCity"] = commonObject["jurisdictions_boundary"];
    } else if (this.value == "WARD") {
      commonObject["juridictionTypeForWard"] = commonObject["jurisdictions_boundary"];

    } else {
      commonObject["juridictionTypeForZone"] = commonObject["jurisdictions_boundary"];

    }
    $(`#jurisdictions\\.boundary`).html(`<option value=''>Select</option>`)

    for (var i = 0; i < commonObject["jurisdictions_boundary"].length; i++) {
      $(`#jurisdictions\\.boundary`).append(`<option value='${commonObject["jurisdictions_boundary"][i]['id']}'>${commonObject["jurisdictions_boundary"][i]['name']}</option>`)
    }
    // return;
  }
  getPositions(this);
  fillValueToObject(this);
  // }
});

//Getting data for user input
$("textarea").on("keyup change", function() {
  fillValueToObject(this);
})

//file change handle for file upload
/*$("input[type=file]").on("change", function(evt) {
    // console.log(this.value);
    // agreement[this.id] = this.value;
    var file = evt.currentTarget.files[0];

    //call post api update and update that url in pur agrement object
});*/

//initial setup
$("#departments").hide();

//it will split object string where it has .
function fillValueToObject(currentState) {
  if (currentState.id.includes(".")) {

    var splitResult = currentState.id.split(".");
    if (splitResult[0] === "user") {
      if (currentState.id == "user.dob" && currentState.value.indexOf("/") > -1) {
          var dateSplit = currentState.value.split("/");
          //var date = new Date(dateSplit[0], dateSplit[1], dateSplit[2]);
          //var day = date.getDate().toString().length === 1 ? "0" + date.getDate() : date.getDate();
          //var monthIn = date.getMonth().toString().length === 1 ? "0" + date.getMonth() : date.getMonth();
          //var yearIn = date.getFullYear();
          employee[splitResult[0]][splitResult[1]] =  dateSplit[1]  + "/" + dateSplit[0] + "/" + dateSplit[2];
      } else if (currentState.id == "user.active") {
        employee[splitResult[0]][splitResult[1]] = currentState.value;

      } else if (currentState.type === "file") {
        employee[splitResult[0]][splitResult[1]] = $.extend(true, [], currentState.files);
      } else {
        employee[splitResult[0]][splitResult[1]] = currentState.value;
      }
    } else {
      // if (currentState.id == "assignments.fromDate" || currentState.id == "assignments.toDate" || currentState.id == "serviceHistory.serviceFrom" || currentState.id == "probation.orderDate" || currentState.id == "probation.declaredOn" || currentState.id == "regularisation.declaredOn" || currentState.id == "regularisation.orderDate" || currentState.id == "education.yearOfPassing" || currentState.id == "technical.yearOfPassing" || currentState.id == "test.yearOfPassing") {
      //     var dateSplit = currentState.value.split("-");
      //     var date = new Date(dateSplit[0], dateSplit[1], dateSplit[2]);
      //     var day = date.getDate().toString().length === 1 ? "0" + date.getDate() : date.getDate();
      //     var monthIn = date.getMonth().toString().length === 1 ? "0" + date.getMonth() : date.getMonth();
      //     var yearIn = date.getFullYear();
      //     employeeSubObject[splitResult[0]][splitResult[1]] = day + "/" + monthIn + "/" + yearIn;
      //
      // } else
      if (currentState.id == "assignments.mainDepartments") {
        tempListBox = [];
        for (var i = 0; i < $(currentState).val().length; i++) {
          tempListBox.push({
            department: $(currentState).val()[i]
          })
        }


      }
      // else if(currentState.id=="assignments.isPrimary")
      // {
      //   employeeSubObject[splitResult[0]][splitResult[1]]=currentState.value=="No"?false:true;
      // }
      // if (currentState.id=="jurisdictions.boundary") {
      //       employeeSubObject[splitResult[0]][splitResult[1]].push(currentState.value);
      // }
      else if (currentState.type === "file") {
        employeeSubObject[splitResult[0]][splitResult[1]] = $.extend(true, [], currentState.files);
      } else
        employeeSubObject[splitResult[0]][splitResult[1]] = currentState.value;
    }

  } else {
    // if (currentState.id == "dateOfAppointment" || currentState.id == "dateOfRetirement" || currentState.id == "dateOfTermination" || currentState.id == "retirementAge" || currentState.id == "dateOfJoining" || currentState.id == "dateOfRetirement" || currentState.id == "dateOfTermination" || currentState.id == "dateOfResignation") {
    //     var dateSplit = currentState.value.split("-");
    //     var date = new Date(dateSplit[0], dateSplit[1], dateSplit[2]);
    //     var day = date.getDate().toString().length === 1 ? "0" + date.getDate() : date.getDate();
    //     var monthIn = date.getMonth().toString().length === 1 ? "0" + date.getMonth() : date.getMonth();
    //     var yearIn = date.getFullYear();
    //     employee[currentState.id] = day + "/" + monthIn + "/" + yearIn;
    // } else
    if (currentState.id == "languagesKnown") {
      employee[currentState.id] = $(currentState).val();
    } else if (currentState.type === "file") {
      employee[currentState.id] = $.extend(true, [], currentState.files);
    } else {
      employee[currentState.id] = currentState.value;
    }
  }
}

function clearModalInput(object, properties) {
  for (var variable in properties) {
    if (properties.hasOwnProperty(variable)) {
      if (variable == "isPrimary") {
        $('[data-primary="yes"]').prop("checked", false);
        $('[data-primary="no"]').prop("checked", true);
      } else if (variable == "hod") {
        $('#departments').hide();
        $('[data-hod="yes"]').prop("checked", false);
        $('[data-hod="no"]').prop("checked", true);
      } else
        $("#" + object + "\\." + variable).val(properties[variable]);
    }
  }

  /*$('#assignmentDetailModal input[type="radio":checked]').each(function(){
      this.checked = false;
  });*/
}

//need to cleat editIndex and temprory pbject
$('#assignmentDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["assignments"] = {
    fromDate: "",
    toDate: "",
    department: "",
    designation: "",
    position: "",
    isPrimary: false,
    fund: "",
    function: "",
    functionary: "",
    grade: "",
    hod: false,
    // mainDepartments: "",
    govtOrderNumber: "",
    documents: null
  };
  clearModalInput("assignments", employeeSubObject["assignments"]);
})

$('#jurisdictionDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["jurisdictions"] = {
    jurisdictionsType: "",
    boundary: ""
  }
  clearModalInput("jurisdictions", employeeSubObject["jurisdictions"]);
})

$('#serviceHistoryDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["serviceHistory"] = {

    serviceInfo: "",
    serviceFrom: "",
    remarks: "",
    orderNo: "",
    documents: null
  }
  clearModalInput("serviceHistory", employeeSubObject["serviceHistory"]);
})

$('#probationDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["probation"] = {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
  }
  clearModalInput("probation", employeeSubObject["probation"]);

})

$('#regularisationDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["regularisation"] = {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
  }
  clearModalInput("regularisation", employeeSubObject["regularisation"]);

})

$('#technicalDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["technical"] = {
    skill: "",
    grade: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
  }
  clearModalInput("technical", employeeSubObject["technical"]);

})

$('#educationDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["education"] = {
    qualification: "",
    majorSubject: "",
    yearOfPassing: "",
    university: "",
    documents: null
  }
  clearModalInput("education", employeeSubObject["education"]);

})

$('#testDetailModal').on('hidden.bs.modal', function(e) {
  $('.error-p').hide();
  editIndex = -1;
  employeeSubObject["test"] = {
    test: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
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
    if (object == "assignments") {
      $(tableName).append(`<td data-label=${"fromDate"}>

                                ${employee[object][i]["fromDate"]}
                          </td>`)
      $(tableName).append(`<td data-label=${"toDate"}>

                                ${employee[object][i]["toDate"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"department"}>
                                ${getNameById("department",employee[object][i]["department"],"")}
                          </td>`)
      $(tableName).append(`<td data-label=${"designation"}>
                                ${getNameById("designation",employee[object][i]["designation"],"")}
                            </td>`)
      try {
        assignments_position = commonApiPost("hr-masters", "positions", "_search", {
          tenantId,
          id: employee[object][i]["position"]
        }).responseJSON["Position"] || [];
      } catch (e) {
        console.log(e);
        assignments_position = [];
      }
      $(tableName).append(`<td data-label=${"position"}>

                                                      ${assignments_position.length>0?assignments_position[0]["name"]:""}
                            </td>`)
      $(tableName).append(`<td data-label=${"isPrimary"}>

                                                      ${employee[object][i]["isPrimary"]}
                                                </td>`)
      $(tableName).append(`<td data-label=${"fund"}>
                                                                          ${getNameById("fund",employee[object][i]["fund"],"")}
                                                                    </td>`)
      $(tableName).append(`<td data-label=${"function"}>
                                                                                              ${getNameById("function",employee[object][i]["function"],"")}
                                                                                        </td>`)
      $(tableName).append(`<td data-label=${"functionary"}>
                                                                                                                  ${getNameById("functionary",employee[object][i]["functionary"],"")}
                                                                                                            </td>`)
      $(tableName).append(`<td data-label=${"grade"}>
                                                                                                                                      ${getNameById("grade",employee[object][i]["grade"],"")}
                                                                                                                                </td>`)
      $(tableName).append(`<td data-label=${"hod"}>
      ${(employee[object][i]["hod"].length &&typeof(employee[object][i]["hod"])=="object") ?getHodDetails(employee[object][i]["hod"]):""}

      </td>`)

      $(tableName).append(`<td data-label=${"govtOrderNumber"}>

                                                                                                                                                                            ${employee[object][i]["govtOrderNumber"]}
                                                                                                                                                                      </td>`)
      $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)

    } else if(object=="jurisdictions"){
      try {
        bnd = commonApiGet("egov-location", "boundarys", "", {
          "Boundary.id":( typeof employee[object][i] == "object" ? employee[object][i]["boundary"] : employee[object][i]),
          "Boundary.tenantId":tenantId
        }).responseJSON["Boundary"] || [];
      } catch (e) {
        console.log(e);
        bnd = [];
      }

      $(tableName).append(`<td data-label=${"jurisdictionsType"}>
                                ${bnd.length > 0 && bnd[0]["boundaryType"] ? bnd[0]["boundaryType"]["name"] : ""}
                          </td>`)
      $(tableName).append(`<td data-label=${"boundary"}>
                                ${bnd.length > 0 ? bnd[0]["name"] : ""}
                          </td>`)
    } else if (object=="serviceHistory") {
      $(tableName).append(`<td data-label=${"serviceInfo"}>

                                ${employee[object][i]["serviceInfo"]}
                          </td>`)
      $(tableName).append(`<td data-label=${"serviceFrom"}>

                                ${employee[object][i]["serviceFrom"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"remarks"}>

                                                                        ${employee[object][i]["remarks"]}
                                                                                      </td>`)
                                                                                      $(tableName).append(`<td data-label=${"orderNo"}>

                                                                                                                                                        ${employee[object][i]["orderNo"]}
                                                                                                                                                                      </td>`)

        $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)
    } else if (object=="probation" ||object=="regularisation") {
      $(tableName).append(`<td data-label=${"designation"}>
          ${getNameById("designation",employee[object][i]["designation"],"")}
      </td>`)
      $(tableName).append(`<td data-label=${"declaredOn"}>

                                ${employee[object][i]["declaredOn"]}
                          </td>`)
  $(tableName).append(`<td data-label=${"orderNo"}>
                        ${employee[object][i]["orderNo"]}                                                               </td>`)
      $(tableName).append(`<td data-label=${"orderDate"}>

                                ${employee[object][i]["orderDate"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"remarks"}>

                                                                        ${employee[object][i]["remarks"]}
                                                                                      </td>`)
        $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)
    } else if (object=="education" ) {

      $(tableName).append(`<td data-label=${"qualification"}>

                                ${employee[object][i]["qualification"]}
                          </td>`)
  $(tableName).append(`<td data-label=${"majorSubject"}>
                        ${employee[object][i]["majorSubject"]}                                                               </td>`)
      $(tableName).append(`<td data-label=${"yearOfPassing"}>

                                ${employee[object][i]["yearOfPassing"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"university"}>

                                                                        ${employee[object][i]["university"]}
                                                                                      </td>`)
        $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)
    } else if (object=="technical" ) {

      $(tableName).append(`<td data-label=${"skill"}>

                                ${employee[object][i]["skill"]}
                          </td>`)
  $(tableName).append(`<td data-label=${"grade"}>
                        ${employee[object][i]["grade"]}                                                               </td>`)
      $(tableName).append(`<td data-label=${"yearOfPassing"}>

                                ${employee[object][i]["yearOfPassing"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"remarks"}>

                                                                        ${employee[object][i]["remarks"]}
                                                                                      </td>`)
        $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)
    } else if (object=="test" ) {

      $(tableName).append(`<td data-label=${"test"}>

                                ${employee[object][i]["test"]}
                          </td>`)
  $(tableName).append(`<td data-label=${"yearOfPassing"}>

                                ${employee[object][i]["yearOfPassing"]}
                                              </td>`)
      $(tableName).append(`<td data-label=${"remarks"}>

                                                                        ${employee[object][i]["remarks"]}
                                                                                      </td>`)
        $(tableName).append(`<td data-label=${"documents"}>

                                                                                                                                                                                                  ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                                                                                                                                                                            </td>`)
    }



    // for (var key in employee[object][i]) {
    //   if (key === "department" || key === "designation" ||  key === "fund" || key === "function" || key === "functionary" || (object == "assignments" && key === "grade") || key === "mainDepartments" || key === "jurisdictionsType") {
    //     $(tableName).append(`<td data-label=${key}>
    //                               ${getNameById(key,employee[object][i][key],"")}
    //                         </td>`)
    //   } else if (key === "boundary") {
    //
    //   } else if (key == "hod") {
    //     $(tableName).append(`<td data-label=${key}>
    //                             ${employee[object][i][key].length>0?"Yes":"No"}
    //                       </td>`)
    //   } else if (key === "position") {
    //     try {
    //       assignments_position = commonApiPost("hr-masters", "positions", "_search",{tenantId,id:employee[object][i][key]}).responseJSON["Position"] || [];
    //     } catch (e) {
    //       console.log(e);
    //       assignments_position = [];
    //     }
    //     $(tableName).append(`<td data-label=${key}>
    //
    //                               ${assignments_position.length>0?assignments_position[0]["name"]:""}
    //                         </td>`)
    //   }
    //    else if ((key != "id"|| object=="serviceHistory") && key != "createdBy" && key != "createdDate" && key != "lastModifiedBy" && key != "lastModifiedDate" && key != "tenantId") {
    //     if (key == "documents") {
    //       // var name="";
    //       // for (var i = 0; i < employee[object][i][key].length; i++) {
    //       //   name=name +" "+ employee[object][i][key][i]["name"];
    //       // }
    //       $(tableName).append(`<td data-label=${key}>
    //
    //                                   ${employee[object][i][key]?employee[object][i][key].length:""}
    //                             </td>`)
    //     } else {
    //       $(tableName).append(`<td data-label=${key}>
    //
    //                                 ${employee[object][i][key]}
    //                           </td>`)
    //     }
    //
    //   }
    //
    // }
    if (getUrlVars()["type"] != "view") {
      $(tableName).append(`<td data-label="Action">
                      <button type="button" onclick="markEditIndex(${i},'${modalName}','${object}')" class="btn btn-default btn-action"><span class="glyphicon glyphicon-pencil"></span></button>
                      <button type="button" onclick="commonDeleteFunction('${tableName}','${modalName}','${object}',${i})" class="btn btn-default btn-action"><span class="glyphicon glyphicon-trash"></span></button>
                    </td>`);
    } else {
      $(tableName).append(`<td data-label="Action">
                      NA
                    </td>`);
    }

    $(tableName).append(`</tr>`);
  }
}

function getHodDetails(object) {
  var returnObj="<ol>";
  for (var i = 0; i < object.length; i++) {
    returnObj+=`<li>${getNameById("department",object[i]["department"],"")}</li>`
  }
  returnObj+="</ol>";
  return returnObj;
}

//common edit mark index
function markEditIndex(index = -1, modalName = "", object = "") {
  editIndex = index;
  $('#' + modalName).modal('show');
  //assignments  details modal when it edit modalName
  $('#' + modalName).on('shown.bs.modal', function(e) {
    if (editIndex != -1) {
      if(object == "jurisdictions") {
        //Get the boundary to get boundaryType
        var _bnd = commonApiGet("egov-location", "boundarys", "", {
          "Boundary.id": employee[object][editIndex],
          "Boundary.tenantId": tenantId
        }).responseJSON["Boundary"] || [];

        if(_bnd && _bnd.length) {
          var jType = _bnd[0]["boundaryType"].id;
          commonObject["jurisdictions_boundary"] = commonApiPost("egov-location", "boundarys", "getByBoundaryType", {
            boundaryTypeId: jType,
            tenantId
          }).responseJSON["Boundary"] || [];
          if (jType == "CITY") {
            commonObject["juridictionTypeForCity"] = commonObject["jurisdictions_boundary"];
          } else if (jType == "WARD") {
            commonObject["juridictionTypeForWard"] = commonObject["jurisdictions_boundary"];
          } else {
            commonObject["juridictionTypeForZone"] = commonObject["jurisdictions_boundary"];
          }
          $(`#jurisdictions\\.boundary`).html(`<option value=''>Select</option>`)

          for (var i = 0; i < commonObject["jurisdictions_boundary"].length; i++) {
            $(`#jurisdictions\\.boundary`).append(`<option value='${commonObject["jurisdictions_boundary"][i]['id']}'>${commonObject["jurisdictions_boundary"][i]['name']}</option>`)
          }

          employeeSubObject[object] = {
            jurisdictionsType: jType,
            boundary: employee[object][editIndex]
          }

          for(key in employeeSubObject[object]) {
            $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
          }
        }
      } else {
        employeeSubObject[object] = Object.assign({}, employee[object][editIndex]);
        for (var key in employeeSubObject[object]) {
          // if($(`#${object}\\.${key}`).length == 0) {
          //       alert("not there");
          //   }
          // if (object + "." + key == "assignments.fromDate" || object + "." + key == "assignments.toDate" || object + "." + key == "serviceHistory.serviceFrom" || object + "." + key == "probation.orderDate" || object + "." + key == "probation.declaredOn" || object + "." + key == "regularisation.declaredOn" || object + "." + key == "regularisation.orderDate" || object + "." + key == "education.yearOfPassing" || object + "." + key == "technical.yearOfPassing" || object + "." + key == "test.yearOfPassing") {
          //     var dateSplit = employeeSubObject[object][key].split("/");
          //     var date = new Date(dateSplit[0], dateSplit[1], dateSplit[2]);
          //     // var day=date.getDate().toString().length===1?"0"+date.getDate():date.getDate();
          //     // var monthIn=date.getMonth().toString().length===1?"0"+date.getMonth():date.getMonth();
          //     // var yearIn=date.getFullYear();
          //     // employeeSubObject[splitResult[0]][splitResult[1]] = day+"/"+monthIn+"/"+yearIn;
          //     $(`#${object}\\.${key}`).val(date);
          //
          // } else {
          //     $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
          // }
          if (key == "position") {
            setTimeout(function(key, object){
               getPositions({
                id: "assignments.department"
               });
               $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
            }, 200, key, object);
          }

          if (key == "isPrimary") {
            if (employeeSubObject[object][key] == "true" ||employeeSubObject[object][key]==true) {
              $('[data-primary="yes"]').prop("checked", true);
              $('[data-primary="no"]').prop("checked", false);
            } else {
              $('[data-primary="yes"]').prop("checked", false);
              $('[data-primary="no"]').prop("checked", true);
            }
          } else if (key == "hod") {
            if ((employeeSubObject[object][key] && employeeSubObject[object][key].constructor == Array && employeeSubObject[object][key].length)||(employeeSubObject[object][key]==true ||employeeSubObject[object][key]=="true")) {
              $('[data-hod="yes"]').prop("checked", true);
              $('[data-hod="no"]').prop("checked", false);
              $("#departments").show();
              tempListBox = Object.assign([], employeeSubObject[object][key]);
              $("#assignments\\.mainDepartments option:selected").removeAttr("selected");
              var _val = [];
              for(var i=0; i<employeeSubObject[object][key].length; i++) {
                $("#assignments\\.mainDepartments option[value=" +  employeeSubObject[object][key][i].department + "]").attr("selected", true);
                _val.push(employeeSubObject[object][key][i].department);
              }
              $("#assignments\\.mainDepartments").val(_val);
            } else {
              $('[data-hod="yes"]').prop("checked", false);
              $('[data-hod="no"]').prop("checked", true);
            }
          } else
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
    if (checkIfNoDup(employee, object, employeeSubObject[object])) {

      if (validateDates(employee, object, employeeSubObject[object])) {
        if (editIndex != -1) {
          if(object == "assignments") {
            employeeSubObject[object]["hod"] = [];
            if (tempListBox.length > 0) {
              tempListBox.map(function(val) {
                employeeSubObject[object]["hod"].push(val);
              })
            }
          }
          employee[object][editIndex] = employeeSubObject[object];
          updateTable("#" + tableName, modalName, object);
        } else {
          if (object == "assignments") {
            employeeSubObject[object]["hod"] = [];
            if (tempListBox.length > 0) {
              tempListBox.map(function(val) {
                employeeSubObject[object]["hod"].push(val);
              })
            }
            employee[object].push(Object.assign({}, employeeSubObject[object]));
            updateTable("#" + tableName, modalName, object);
          } else {
            employee[object].push(Object.assign({}, employeeSubObject[object]));
            updateTable("#" + tableName, modalName, object);
          }

        }
        $(`#${modalName}`).modal("hide");
      } else {
        $(".error-p").text("Assignment dates overlapping.");
        $(".error-p").show();
      }
    } else {
      $(".error-p").text("Duplicate entry not allowed.");
      $(".error-p").show();
    }
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

$(document).ready(function() {

  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }
    $.validator.addMethod('phone', function(value) {
        return value ? /^[0-9]{10}$/.test(value) : true;
    }, 'Please enter a valid phone number.');

    $.validator.addMethod('aadhar', function(value) {
        return value ? /^[0-9]{12}$/.test(value) : true;
    }, 'Please enter a valid aadhar.');

    $.validator.addMethod('panNo', function(value) {
        return value ? /^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i.test(value) && value.length === 10 : true;
    }, 'Please enter a valid pan.');

    $(".onlyNumber").on("keydown", function(e) {
        var key = e.keyCode ? e.keyCode : e.which;
        if (!([8, 9, 13, 27, 46, 110, 190].indexOf(key) !== -1 ||
                (key == 65 && (e.ctrlKey || e.metaKey)) ||
                (key >= 35 && key <= 40) ||
                (key >= 48 && key <= 57 && !(e.shiftKey || e.altKey)) ||
                (key >= 96 && key <= 105)
            )) {
            e.preventDefault();
        }
    });
})


$("#addEmployee").on("click", function(e) {
  e.preventDefault();
  $("#createEmployeeForm").submit();
  // switchValidation("final_validatin_rules");
});


// Adding Jquery validation dynamically
$("#createEmployeeForm").validate({
  rules: final_validatin_rules,
  submitHandler: function(form) {
    // console.log(form);
    if (!hasAllRequiredFields(employee)) {
      showError("Please enter all mandatory fields.");
    } else if ((employee.assignments.length > 0 && isHavingPrimary()) && employee.jurisdictions.length > 0) {
      //Call api
      var __emp = Object.assign({}, employee);

      if(employee["jurisdictions"] && employee["jurisdictions"].length && typeof employee["jurisdictions"][0] == "object") {
          var empJuridictiona = employee["jurisdictions"];
          employee["jurisdictions"] = [];
          for (var i = 0; i < empJuridictiona.length; i++) {
            employee["jurisdictions"].push(empJuridictiona[i].boundary);
          }
      }

      if(employee.user && employee.user.dob && getUrlVars()["type"] == "update" && employee.user.dob.indexOf("-") > -1) {
        var _date = employee.user.dob.split("-");
        employee.user.dob = _date[1] + "/" + _date[2] + "/" + _date[0];
      }
      //Upload files if any
      uploadFiles(employee, function(err, emp) {
        if (err) {
          //Handle error
        } else {
          var response = $.ajax({
            url: baseUrl + "/hr-employee/employees/" + ((getUrlVars()["type"] == "update") ? "_update" : "_create") + "?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify({
              RequestInfo: requestInfo,
              Employee: emp
            }),
            async: false,
            headers: {
              'auth-token': authToken
            },
            contentType: 'application/json'
          });

          if (response["status"] === 200) {
            //showSuccess("Employee" + getUrlVars()["type"] == "update" ? "update" : "add" + "ed successfully.");
            window.location.href = "app/hr/common/employee-search.html?type=view";
          } else {
            showError(response["statusText"]);
            employee = Object.assign({}, __emp);
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
        }
      })
    } else {
      showError("Please enter atleast one assignment and jurisdiction.");
    }
    //alert("submitterd");
    // form.submit();

    // console.log(agreement);
  }
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

function isHavingPrimary() {
  for (var i = 0; i < employee.assignments.length; i++) {
    if (employee.assignments[i].isPrimary) {
      return true;
    }

  }
  return false;
}

function uploadFiles(employee, cb) {
  if (employee.user.photo && typeof employee.user.photo == "object") {
    makeAjaxUpload(employee.user.photo[0], function(err, res) {
      if (err) {
        cb(err);
      } else {
        employee.user.photo = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
        uploadFiles(employee, cb);
      }
    })
  } else if (employee.user.signature && typeof employee.user.signature == "object") {
    makeAjaxUpload(employee.user.signature[0], function(err, res) {
      if (err) {
        cb(err);
      } else {
        employee.user.signature = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
        uploadFiles(employee, cb);
      }
    })
  } else if (employee.documents && employee.documents.length && employee.documents[0].constructor == File) {
    let counter = employee.documents.length,
      breakout = 0;
    for (let i = 0; len = employee.documents.length, i < len; i++) {
      makeAjaxUpload(employee.documents[i], function(err, res) {
        if (breakout == 1)
          return;
        else if (err) {
          cb(err);
          breakout = 1;
        } else {
          counter--;
          employee.documents[i] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
          if (counter == 0 && breakout == 0)
            uploadFiles(employee, cb);
        }
      })
    }
  } else if (employee.assignments.length && hasFile(employee.assignments)) {
    let counter1 = employee.assignments.length,
      breakout = 0;
    for (let i = 0; len = employee.assignments.length, i < len; i++) {
      let counter = employee.assignments[i].documents.length;
      for (let j = 0; len1 = employee.assignments[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.assignments[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.assignments[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.serviceHistory.length && hasFile(employee.serviceHistory)) {
    let counter1 = employee.serviceHistory.length,
      breakout = 0;
    for (let i = 0; len = employee.serviceHistory.length, i < len; i++) {
      let counter = employee.serviceHistory[i].documents.length;
      for (let j = 0; len1 = employee.serviceHistory[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.serviceHistory[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.serviceHistory[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.probation.length && hasFile(employee.probation)) {
    let counter1 = employee.probation.length,
      breakout = 0;
    for (let i = 0; len = employee.probation.length, i < len; i++) {
      let counter = employee.probation[i].documents.length;
      for (let j = 0; len1 = employee.probation[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.probation[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.probation[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.regularisation.length && hasFile(employee.regularisation)) {
    let counter1 = employee.regularisation.length,
      breakout = 0;
    for (let i = 0; len = employee.regularisation.length, i < len; i++) {
      let counter = employee.regularisation[i].documents.length;
      for (let j = 0; len1 = employee.regularisation[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.regularisation[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.regularisation[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.technical.length && hasFile(employee.technical)) {
    let counter1 = employee.technical.length,
      breakout = 0;
    for (let i = 0; len = employee.technical.length, i < len; i++) {
      let counter = employee.technical[i].documents.length;
      for (let j = 0; len1 = employee.technical[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.technical[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.technical[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.education.length && hasFile(employee.education)) {
    let counter1 = employee.education.length,
      breakout = 0;
    for (let i = 0; len = employee.education.length, i < len; i++) {
      let counter = employee.education[i].documents.length;
      for (let j = 0; len1 = employee.education[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.education[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.education[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else if (employee.test.length && hasFile(employee.test)) {
    let counter1 = employee.test.length,
      breakout = 0;
    for (let i = 0; len = employee.test.length, i < len; i++) {
      let counter = employee.test[i].documents.length;
      for (let j = 0; len1 = employee.test[i].documents.length, j < len1; j++) {
        makeAjaxUpload(employee.test[i].documents[j], function(err, res) {
          if (breakout == 1)
            return;
          else if (err) {
            cb(err);
            breakout = 1;
          } else {
            counter--;
            employee.test[i].documents[j] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
            if (counter == 0 && breakout == 0) {
              counter1--;
              if (counter1 == 0 && breakout == 0)
                uploadFiles(employee, cb);
            }
          }
        })
      }
    }
  } else {
    cb(null, employee);
  }
}

function hasFile(elements) {
  if (elements && elements.constructor == Array) {
    for (var i = 0; i < elements.length; i++) {
      if (elements[i].documents && elements[i].documents.constructor == Array)
        for (var j = 0; j < elements[i].documents.length; j++)
          if (elements[i].documents[j].constructor == File)
            return true;
    }
  }
  return false;
}

function makeAjaxUpload(file, cb) {
  let formData = new FormData();
  formData.append("jurisdictionId", "ap.public");
  formData.append("module", "PGR");
  formData.append("file", file);
  $.ajax({
    url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
    data: formData,
    cache: false,
    contentType: false,
    processData: false,
    type: 'POST',
    success: function(res) {
      cb(null, res);
    },
    error: function(jqXHR, exception) {
      cb(jqXHR.responseText || jqXHR.statusText);
    }
  });
}

function hasAllRequiredFields(emp) {
  return (emp.user.name && emp.code && emp.employeeType && emp.employeeStatus && emp.user.dob && emp.user.gender && emp.maritalStatus && emp.user.userName && emp.user.mobileNumber && emp.user.mobileNumber.length == 10 && emp.dateOfAppointment);
}

function checkIfNoDup(employee, objectType, subObject) {
  if (employee[objectType].length === 0)
    return true;
  else if (objectType == "jurisdictions") {
    for (let i = 0; i < employee[objectType].length; i++) {
      if (employee[objectType][i].jurisdictionsType == subObject.jurisdictionsType && employee[objectType][i].boundary == subObject.boundary)
        return false;
    }
  }

  return true;
}

function validateDates(employee, objectType, subObject) {
  if (objectType == "assignments" && subObject.isPrimary) {
    for (let i = 0; i < employee[objectType].length; i++) {
      if (employee[objectType][i].isPrimary && (editIndex == -1 || (editIndex > -1 && i != editIndex))) {
        var subFromDate = new Date(subObject.fromDate.split("/")[1] + "/" + subObject.fromDate.split("/")[0] + "/" + subObject.fromDate.split("/")[2]).getTime();
        var fromDate = new Date(employee[objectType][i].fromDate.split("/")[1] + "/" + employee[objectType][i].fromDate.split("/")[0] + "/" + employee[objectType][i].fromDate.split("/")[2]).getTime();
        var subToDate = new Date(subObject.toDate.split("/")[1] + "/" + subObject.toDate.split("/")[0] + "/" + subObject.toDate.split("/")[2]).getTime();
        var toDate = new Date(employee[objectType][i].toDate.split("/")[1] + "/" + employee[objectType][i].toDate.split("/")[0] + "/" + employee[objectType][i].toDate.split("/")[2]).getTime();

        if (((fromDate >= subFromDate) &&
            (fromDate <= subToDate)) ||
          ((toDate >= subFromDate) &&
            (toDate <= subToDate)) ||
          ((subFromDate >= fromDate) &&
            (subFromDate <= toDate)) ||
          ((subToDate >= fromDate) &&
            (subToDate <= toDate))) {
          return false;
        }
      }
    }
  }

  return true;
}

function getPositions(_this) {
  if (($("#assignments\\.department").val() != "" && $("#assignments\\.designation").val() != "") && (_this.id == "assignments.department" || _this.id == "assignments.designation" || _this.id == "assignment.fromDate" || _this.id == "assignments.isPrimary")) {
    if (employeeSubObject["assignments"].isPrimary == "true") {
      if ($("#assignments\\.fromDate").val()) {
        var _date = $("#assignments\\.fromDate").val();
        commonObject["assignments_position"] = commonApiPost("hr-masters", "vacantpositions", "_search", {
          tenantId,
          departmentId: $("#assignments\\.department").val(),
          designationId: $("#assignments\\.designation").val(),
          asOnDate: _date
        }).responseJSON["Position"] || [];
      }
    } else {
      commonObject["assignments_position"] = commonApiPost("hr-masters", "positions", "_search", {
        tenantId,
        departmentId: $("#assignments\\.department").val(),
        designationId: $("#assignments\\.designation").val()
      }).responseJSON["Position"] || [];
    }

    $(`#assignments\\.position`).html(`<option value=''>Select</option>`);
    for (var i = 0; i < commonObject["assignments_position"].length; i++) {
      $(`#assignments\\.position`).append(`<option value='${commonObject["assignments_position"][i]['id']}'>${commonObject["assignments_position"][i]['name']}</option>`)
    }
  }
}


if (getUrlVars()["type"] == "update") {
  try {

    if (getUrlVars()["id"]) {
      var currentEmployee = commonApiPost("hr-employee", "employees/" + getUrlVars()["id"], "_search", {
        tenantId
      }).responseJSON["Employee"] || {};
    } else {
      var obj = commonApiPost("hr-employee", "employees", "_loggedinemployee" ,{
        tenantId}
      ).responseJSON["Employee"][0];
      var currentEmployee = commonApiPost("hr-employee", "employees/" + obj.id, "_search", {
        tenantId
      }).responseJSON["Employee"] || {};
    }



    employee = currentEmployee;

    printValue("", currentEmployee);

    if (currentEmployee["assignments"].length > 0) {
      updateTable("#agreementTableBody", 'assignmentDetailModal', "assignments")
    }

    if (currentEmployee["jurisdictions"].length > 0) {
      updateTable("#jurisdictionTableBody", 'jurisdictionDetailModal', "jurisdictions")
    }

    if (currentEmployee["serviceHistory"].length > 0) {
      updateTable("#serviceHistoryTableBody", 'serviceHistoryDetailModal', "serviceHistory")
    }

    if (currentEmployee["probation"].length > 0) {
      updateTable("#probationTableBody", 'probationDetailModal', "probation")
    }

    if (currentEmployee["regularisation"].length > 0) {
      updateTable("#regularisationTableBody", 'regularisationDetailModal', "regularisation")
    }

    if (currentEmployee["education"].length > 0) {
      updateTable("#educationTableBody", 'educationDetailModal', "education")
    }

    if (currentEmployee["technical"].length > 0) {
      updateTable("#technicalTableBody", 'technicalDetailModal', "technical")
    }

    if (currentEmployee["test"].length > 0) {
      updateTable("#testTableBody", '#testDetailModal', "test")
    }



  } catch (e) {
    console.log(e);
  }
}

if (getUrlVars()["type"] == "view") {
  try {

    if (getUrlVars()["id"]) {
      var currentEmployee = commonApiPost("hr-employee", "employees/" + getUrlVars()["id"], "_search", {
        tenantId
      }).responseJSON["Employee"] || {};
    } else {
      var obj = commonApiPost("hr-employee", "employees", "_loggedinemployee" ,{
        tenantId}
      ).responseJSON["Employee"][0];
      var currentEmployee = commonApiPost("hr-employee", "employees/" + obj.id, "_search", {
        tenantId
      }).responseJSON["Employee"] || {};
    }

    $("#createEmployeeForm input").prop("disabled", true);

    // $('#createEmployeeForm').addClass('disabled');

    $("#createEmployeeForm select").prop("disabled", true);

    $("#createEmployeeForm textarea").prop("disabled", true);

    $("#addEmployee").hide();



    employee = currentEmployee;
    printValue("", currentEmployee);

    if (currentEmployee["assignments"].length > 0) {
      updateTable("#agreementTableBody", 'xyz', "assignments")
    }

    if (currentEmployee["jurisdictions"].length > 0) {
      updateTable("#jurisdictionTableBody", 'xyz', "jurisdictions")
    }

    if (currentEmployee["serviceHistory"].length > 0) {
      updateTable("#serviceHistoryTableBody", 'xyz', "serviceHistory")
    }

    if (currentEmployee["probation"].length > 0) {
      updateTable("#probationTableBody", 'xyz', "probation")
    }

    if (currentEmployee["regularisation"].length > 0) {
      updateTable("#regularisationTableBody", 'xyz', "regularisation")
    }

    if (currentEmployee["education"].length > 0) {
      updateTable("#educationTableBody", 'xyz', "education")
    }

    if (currentEmployee["technical"].length > 0) {
      updateTable("#technicalTableBody", 'xyz', "technical")
    }

    if (currentEmployee["test"].length > 0) {
      updateTable("#testTableBody", 'xyz', "test")
    }

    $(".btn-default").hide();

  } catch (e) {
    console.log(e);
  }
}




function printValue(object = "", values) {
  if (object != "") {

  } else {
    for (var key in values) {
      if (typeof values[key] === "object" && key == "user") {
        for (ckey in values[key]) {
          if (values[key][ckey]) {
            //Get description
            if (ckey == "dob") {
              $("[name='" + key + "." + ckey + "']").val(values[key][ckey] ? values[key][ckey].split("-")[2] + "/" + values[key][ckey].split("-")[1] + "/" + values[key][ckey].split("-")[0] : "");

            } else if(ckey == "active") {
              if([true, "true"].indexOf(values[key][ckey]) > -1) {
                $('[data-active="yes"]').prop("checked", true);
              } else {
                $('[data-active="no"]').prop("checked", false);
              }
            } else {
              $("[name='" + key + "." + ckey + "']").val(values[key][ckey] ? values[key][ckey] : "");

            }
          }
        }
      } else if(key == "physicallyDisabled") {
        if([true, "true"].indexOf(values[key]) > -1) {
          $('[data-ph="yes"]').prop("checked", true);
        } else {
          $('[data-ph="no"]').prop("checked", true);
        }
      } else if(key == "bank" && values[key]) {
        commonObject["bankbranches"] = commonApiPost("egf-masters", "bankbranches", "_search", {
            tenantId,
            "bank.id": values[key]
        }).responseJSON["bankBranches"] || [];
        $(`#bankBranch`).html(`<option value=''>Select</option>`)
        for (var i = 0; i < commonObject["bankbranches"].length; i++) {
          $(`#bankBranch`).append(`<option value='${commonObject["bankbranches"][i]['id']}'>${commonObject["bankbranches"][i]['name']}</option>`)
        }
        $("[name='" + key + "']").val(values[key] ? values[key] : "");
      } else if (values[key]) {
        $("[name='" + key + "']").val(values[key] ? values[key] : "");
      } else {
        // $("[name='" + (isAsset ? "asset." : "") + key + "']").text("NA");
      }
    }
  }
}
