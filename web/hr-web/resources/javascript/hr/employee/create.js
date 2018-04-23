var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
var filesToBeDeleted = {};
var employeeType, employeeStatus, group, motherTongue, religion, community, category, bank, recruitmentMode, recruitmentType, recruitmentQuota, assignments_grade, assignments_designation, assignments_department, assignments_fund, assignments_functionary, assignments_function, assignments_position, maritalStatus, user_bloodGroup;
var count = 22;
var tempListBox = [];
var commonObject, hrConfigurations, startDate;
var yearOfPassing = [];
for (var i = 2000; i <= new Date().getFullYear(); i++) {
    yearOfPassing.push(i);
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
    ifscCode:"",
    group: "",
    placeOfBirth: "",
    transferredEmployee:false,
    documents: [],
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
        city:"",
        serviceInfo: "",
        serviceFrom: "",
        serviceTo:"",
        department:"",
        designation:"",
        remarks: "",
        orderNo: "",
        documents: null,
        isAssignmentBased:false
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
        required: true,
        alphanumeric: true
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
        required: false,
        alphanumeric: true
    },
    bank: {
        required: true
    },
    bankBranch: {
        required: true
    },
    bankAccount: {
        required: true,
        alphanumericWSplChar: true
    },
    ifscCode:{
        required: true,
        alphanumeric: true
    },
    group: {
        required: false
    },
    placeOfBirth: {
        required: false,
        alphaWSpcNDot: true
    },
    documents: {
        required: false
    },
    "user.userName": {
        required: true
    },
    "user.name": {
        required: true,
        alphaWSpcNDot: true
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
        phone: true
    },
    "user.pan": {
        required: false,
        panNo: true
    },
    "user.aadhaarNumber": {
        required: true,
        aadhar: true
    },
    "user.permanentAddress": {
        required: false,
        alphanumericWAllSplCharNSpc: true
    },
    "user.permanentCity": {
        required: false,
        alphanumericWSpc: true
    },
    "user.permanentPinCode": {
        required: false
    },
    "user.correspondenceCity": {
        required: false,
        alphanumericWSpc: true
    },
    "user.correspondencePinCode": {
        required: false
    },
    "user.correspondenceAddress": {
        required: false,
        alphanumericWAllSplCharNSpc: true
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
        required: false,
        alphaWSpcNDot: true
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
        required: false,
        alphanumericWSplCharNSpc: true
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
        required: true,
        alphaWSplChar: true
    },
    "education.majorSubject": {
        required: false,
        alphaWSplChar: true
    },
    "education.yearOfPassing": {
        required: true,
        year: true
    },
    "education.university": {
        required: false,
        alphaWSplChar: true
    },
    "education.documents": {
        required: false
    },
    "serviceHistory.id": {
        required: false
    },
    "serviceHistory.city": {
        required: true
    },
    "serviceHistory.serviceInfo": {
        required: true
    },
    "serviceHistory.serviceFrom": {
        required: true
    },
    "serviceHistory.serviceTo": {
        required: true
    },
    "serviceHistory.department": {
        required: true
    },
    "serviceHistory.designation": {
        required: true
    },
    "serviceHistory.remarks": {
        required: false,
        alphanumericWSpc: true
    },
    "serviceHistory.orderNo": {
        required: false,
        alphanumericWSplCharNSpc: true
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
    "technical.skill": {
        required: true
    },
    "technical.grade": {
        required: false
    },
    "technical.yearOfPassing": {
        required: false,
        year: true
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
        required: true,
        year: true
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
    
    city: {
        required: true
    },
    serviceInfo: {
        required: true
    },
    serviceFrom: {
        required: true
    },
    serviceTo: {
        required: true
    },
    department: {
        required: true
    },
    designation: {
        required: true
    },
    position: {
        required: false
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
        required: true

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

function checkCount() {
    count--;
    if (count == 0)
        loadUI();
}

function getNameById(object, id, optional) {
    object = returnObject(object, optional);
    for (var i = 0; i < commonObject[object].length; i++) {
        if (commonObject[object][i].id == id) {
            return commonObject[object][i].name;
        }
    }
    return "";
}

function isHavingPrimary() {
    for (var i = 0; i < employee.assignments.length; i++) {
        if (employee.assignments[i].isPrimary == "true" || employee.assignments[i].isPrimary == true) {
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
                employee.user.photo = `${res.files[0].fileStoreId}`;
                uploadFiles(employee, cb);
            }
        })
    } else if (employee.user.signature && typeof employee.user.signature == "object") {
        makeAjaxUpload(employee.user.signature[0], function(err, res) {
            if (err) {
                cb(err);
            } else {
                employee.user.signature = `${res.files[0].fileStoreId}`;
                uploadFiles(employee, cb);
            }
        })
    } else if (employee.documents && employee.documents.length && employee.documents[0].constructor == File) {
        let counter = employee.documents.length,
            breakout = 0;
        for (let i = 0, len = employee.documents.length; i < len; i++) {
            makeAjaxUpload(employee.documents[i], function(err, res) {
                if (breakout == 1)
                    return;
                else if (err) {
                    cb(err);
                    breakout = 1;
                } else {
                    counter--;
                    employee.documents[i] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.assignments[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.assignments[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.assignments[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.serviceHistory[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.serviceHistory[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.serviceHistory[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.probation[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.probation[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.probation[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.regularisation[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.regularisation[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.regularisation[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.technical[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.technical[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.technical[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.education[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.education[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.education[i].documents[j] = `${res.files[0].fileStoreId}`;
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
            for (let j = 0, len1 = employee.test[i].documents.length; j < len1; j++) {
                makeAjaxUpload(employee.test[i].documents[j], function(err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        employee.test[i].documents[j] = `${res.files[0].fileStoreId}`;
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

function checkNRemoveFile() {
    for (var key in filesToBeDeleted) {
        if (key == "photo" || key == "signature") {
            employee.user[key] = "";
        } else if (key == "documents") {
            for (var i = 0; i < filesToBeDeleted[key].length; i++) {
                var ind = employee[key].indexOf(filesToBeDeleted[key][i]);
                employee[key].splice(ind, 1);
            }
        } else if(key == "servicehistory"){
            key = "serviceHistory";
            for (var i = 0; i < filesToBeDeleted[key].length; i++) {
                for (var j = 0; j < employee[key].length; j++) {
                    var ind = employee[key][j]["documents"].indexOf(filesToBeDeleted[key][i]);
                    if (ind > -1) {
                        employee[key][j]["documents"].splice(ind, 1);
                        break;
                    }
                }
            }
        } else {
            for (var i = 0; i < filesToBeDeleted[key].length; i++) {
                for (var j = 0; j < employee[key].length; j++) {
                    var ind = employee[key][j]["documents"].indexOf(filesToBeDeleted[key][i]);
                    if (ind > -1) {
                        employee[key][j]["documents"].splice(ind, 1);
                        break;
                    }
                }
            }
        }
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
    if (file.constructor == File) {
        let formData = new FormData();
        formData.append("jurisdictionId", tenantId);
        formData.append("module", "HR");
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
    } else {
        cb(null, {
            files: [{
                fileStoreId: file
            }]
        })
    }
}

function hasAllRequiredFields(emp) {
    return (emp.user.name && emp.code && emp.employeeType && emp.employeeStatus && emp.user.dob && emp.user.gender && emp.maritalStatus && emp.user.userName && emp.user.mobileNumber && emp.user.mobileNumber.length == 10 && emp.dateOfAppointment);
}

function checkIfNoDup(employee, objectType, subObject) {
    if (employee[objectType].length === 0)
        return true;
    else if (objectType == "jurisdictions") {
        for (let i = 0; i < employee[objectType].length; i++) {
            if (typeof employee[objectType][i] == "object" && employee[objectType][i].jurisdictionsType == subObject.jurisdictionsType && employee[objectType][i].boundary == subObject.boundary)
                return false;
            else if (employee[objectType][i] == subObject.boundary)
                return false;
        }
    }

    return true;
}

function validateDates(employee, objectType, subObject) {
    if (objectType == "assignments" && (subObject.isPrimary == "true" || subObject.isPrimary == true)) {
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

function enableAndDisable(id) {
    $(`#${id}`).toggle();
}

function getNameByCode(object, code, property = "") {
    if (code == "" || code == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].code == code) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].code == code) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

//common add and update
function commonAddAndUpdate(tableName, modalName, object) {
    // if(switchValidation(object))
    if(modalName == "assignmentDetailModal" && employeeSubObject[object].position) {
        if(commonObject["assignments_position"]) {
            var flag = 0;
            for(var i=0; i< commonObject["assignments_position"].length; i++) {
                if($("#assignments\\.position").val() == commonObject["assignments_position"][i].name)
                    flag = 1;
            }

            if(flag == 0)
                $("#assignments\\.position").val("");
        } else {
            $("#assignments\\.position").val("");
        }
    }

    if ($("#createEmployeeForm").valid()) {
        if (checkIfNoDup(employee, object, employeeSubObject[object])) {

            if (validateDates(employee, object, employeeSubObject[object])) {
                if (editIndex != -1) {
                    if (object == "assignments") {
                        var hod_value = employeeSubObject[object]["hod"];
                        employeeSubObject[object]["hod"] = [];
                        if (((hod_value.constructor == Array && hod_value.length) || [true, "true"].indexOf(hod_value) > -1) && tempListBox.length > 0) {
                            tempListBox.map(function(val) {
                                employeeSubObject[object]["hod"].push(val);
                            })
                            tempListBox = [];
                        }
                    }
                    employee[object][editIndex] = employeeSubObject[object];
                    updateTable("#" + tableName, modalName, object);
                } else {
                    if (object == "assignments") {
                        var hod_value = employeeSubObject[object]["hod"];
                        employeeSubObject[object]["hod"] = [];
                        if ([true, "true"].indexOf(hod_value) > -1 && tempListBox.length > 0) {
                            tempListBox.map(function(val) {
                                employeeSubObject[object]["hod"].push(val);
                            })
                            tempListBox = [];
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

function fillValueToObject(currentState) {
    if (currentState.id.includes(".")) {

        var splitResult = currentState.id.split(".");
        if (splitResult[0] === "user") {
            if (currentState.id == "user.active") {
                employee[splitResult[0]][splitResult[1]] = currentState.value;
            } else if (currentState.type === "file") {
                employee[splitResult[0]][splitResult[1]] = currentState.files;
            } else {
                employee[splitResult[0]][splitResult[1]] = currentState.value;
            }
        } else {
            if (currentState.id == "assignments.mainDepartments") {
                tempListBox = [];
                for (var i = 0; i < $(currentState).val().length; i++) {
                    tempListBox.push({
                        department: $(currentState).val()[i]
                    })
                }


            } else if (currentState.type === "file") {
                if (!employeeSubObject[splitResult[0]][splitResult[1]])
                    employeeSubObject[splitResult[0]][splitResult[1]] = [];
                for (var i = 0; i < currentState.files.length; i++) {
                    employeeSubObject[splitResult[0]][splitResult[1]].push(currentState.files[i]);
                }
            } else
                employeeSubObject[splitResult[0]][splitResult[1]] = currentState.value;
        }

    } else {
        if (currentState.id == "languagesKnown") {
            employee[currentState.id] = $(currentState).val();
        } else if (currentState.type === "file") {
            if (!employee[currentState.id])
                employee[currentState.id] = [];
            for (var i = 0; i < currentState.files.length; i++) {
                employee[currentState.id].push(currentState.files[i]);
            }
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
}

function updateTable(tableName, modalName, object) {
    $(tableName).html(``);
    for (let i = 0; i < employee[object].length; i++) {
        if (object == "assignments") {
            commonApiPost("hr-masters", "positions", "_search", {
                tenantId,
                id: employee[object][i]["position"]
            }, function(err, res) {
                if (res) {
                    assignments_position = res["Position"];
                } else {
                    assignments_position = [];
                }
                $(tableName).append(`<tr>`);
                $(tableName).append(`<td data-label=${"fromDate"}>

                                  ${employee[object][i]["fromDate"] || ""}
                            </td>`)
                $(tableName).append(`<td data-label=${"toDate"}>

                                  ${employee[object][i]["toDate"] || ""}
                                                </td>`)
                $(tableName).append(`<td data-label=${"department"}>
                                  ${getNameById("department",employee[object][i]["department"],"") || ""}
                            </td>`)
                $(tableName).append(`<td data-label=${"designation"}>
                                  ${getNameById("designation",employee[object][i]["designation"],"") || ""}
                              </td>`)
                $(tableName).append(`<td data-label=${"position"}>
                                        ${assignments_position.length>0?assignments_position[0]["name"]:""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"isPrimary"}>
                                        ${(employee[object][i]["isPrimary"] ? ([true, "true"].indexOf(employee[object][i]["isPrimary"]) > -1 ? "Yes" : "No") : ("No"))}
                                    </td>`)
                $(tableName).append(`<td data-label=${"fund"}>
                                        ${getNameById("fund",employee[object][i]["fund"],"") || ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"function"}>
                                        ${getNameById("function",employee[object][i]["function"],"") || ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"functionary"}>
                                        ${getNameById("functionary",employee[object][i]["functionary"],"") || ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"grade"}>
                                        ${getNameById("grade",employee[object][i]["grade"],"") || ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"hod"}>
                                         ${(employee[object][i]["hod"].length &&typeof(employee[object][i]["hod"])=="object") ?getHodDetails(employee[object][i]["hod"]):""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"govtOrderNumber"}>
                                         ${employee[object][i]["govtOrderNumber"] || ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"documents"}>
                                        ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                    </td>`)
                closeTR(tableName, modalName, object, i, employee[object][i]["fromServer"]);
            });
        } else if (object == "jurisdictions") {
            commonApiGet("egov-location", "boundarys", "", {
                "Boundary.id": (typeof employee[object][i] == "object" ? employee[object][i]["boundary"] : employee[object][i]),
                "Boundary.tenantId": tenantId
            }, function(err, res) {
                if (res) {
                    bnd = res.Boundary;
                } else {
                    bnd = [];
                }
                $(tableName).append(`<tr>`);
                $(tableName).append(`<td data-label=${"jurisdictionsType"}>
                                    ${bnd.length > 0 && bnd[0]["boundaryType"] ? bnd[0]["boundaryType"]["name"] : ""}
                                    </td>`)
                $(tableName).append(`<td data-label=${"boundary"}>
                                          ${bnd.length > 0 ? bnd[0]["name"] : ""}
                                    </td>`)
                closeTR(tableName, modalName, object, i);
            });
        } else if (object == "serviceHistory") {
            $(tableName).append(`<tr>`);
            $(tableName).append(`<td data-label=${"serviceCity"}>
                                  ${employee[object][i]["city"] || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"serviceInfo"}>
                                  ${employee[object][i]["serviceInfo"] || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"serviceFrom"}>
                                  ${employee[object][i]["serviceFrom"] || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"serviceTo"}>
                                ${employee[object][i]["serviceTo"] || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"department"}>
                            ${getNameByCode(commonObject["serviceHistory_department"],employee[object][i]["department"]) || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"designation"}>
                          ${getNameByCode(commonObject["serviceHistory_designation"],employee[object][i]["designation"]) || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"remarks"}>
                                    ${employee[object][i]["remarks"]}
                                </td>`)
            $(tableName).append(`<td data-label=${"orderNo"}>
                                    ${employee[object][i]["orderNo"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"documents"}>
                                    ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                </td>`);
            closeTR(tableName, modalName, object, i);
        } else if (object == "probation" || object == "regularisation") {
            $(tableName).append(`<tr>`);
            $(tableName).append(`<td data-label=${"designation"}>
                                    ${getNameById("designation",employee[object][i]["designation"],"")}
                                </td>`)
            $(tableName).append(`<td data-label=${"declaredOn"}>
                                  ${employee[object][i]["declaredOn"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"orderNo"}>
                                  ${employee[object][i]["orderNo"]}
                                </td>`)
            $(tableName).append(`<td data-label=${"orderDate"}>
                                  ${employee[object][i]["orderDate"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"remarks"}>
                                    ${employee[object][i]["remarks"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"documents"}>
                                    ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                </td>`);
            closeTR(tableName, modalName, object, i);
        } else if (object == "education") {
            $(tableName).append(`<tr>`);
            $(tableName).append(`<td data-label=${"qualification"}>
                                  ${employee[object][i]["qualification"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"majorSubject"}>
                                    ${employee[object][i]["majorSubject"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"yearOfPassing"}>
                                  ${employee[object][i]["yearOfPassing"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"university"}>
                                    ${employee[object][i]["university"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"documents"}>
                                    ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                </td>`);
            closeTR(tableName, modalName, object, i);
        } else if (object == "technical") {
            $(tableName).append(`<tr>`);
            $(tableName).append(`<td data-label=${"skill"}>
                                  ${employee[object][i]["skill"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"grade"}>
                                    ${employee[object][i]["grade"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"yearOfPassing"}>
                                  ${employee[object][i]["yearOfPassing"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"remarks"}>
                                    ${employee[object][i]["remarks"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"documents"}>
                                     ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                </td>`)
            closeTR(tableName, modalName, object, i);
        } else if (object == "test") {
            $(tableName).append(`<tr>`);
            $(tableName).append(`<td data-label=${"test"}>
                                  ${employee[object][i]["test"] || ""}
                            </td>`)
            $(tableName).append(`<td data-label=${"yearOfPassing"}>
                                  ${employee[object][i]["yearOfPassing"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"remarks"}>
                                    ${employee[object][i]["remarks"] || ""}
                                </td>`)
            $(tableName).append(`<td data-label=${"documents"}>
                                    ${employee[object][i]["documents"]?employee[object][i]["documents"].length:""}
                                </td>`)
            closeTR(tableName, modalName, object, i);
        }
    }
}

function closeTR(tableName, modalName, object, i, fromUpdateBool) {
    if (getUrlVars()["type"] != "view") {
        if(fromUpdateBool) {
            $(tableName).append(`<td data-label="Action">
                    <button type="button" onclick="markEditIndex(${i},'${modalName}','${object}', '${fromUpdateBool}')" class="btn btn-default btn-action"><span class="glyphicon glyphicon-pencil"></span></button>
                  </td>`);
        } else {
            $(tableName).append(`<td data-label="Action">
                    <button type="button" onclick="markEditIndex(${i},'${modalName}','${object}')" class="btn btn-default btn-action"><span class="glyphicon glyphicon-pencil"></span></button>
                    <button type="button" onclick="commonDeleteFunction('${tableName}','${modalName}','${object}',${i})" class="btn btn-default btn-action"><span class="glyphicon glyphicon-trash"></span></button>
                  </td>`);
        }
    } else {
        $(tableName).append(`<td data-label="Action">
                    NA
                  </td>`);
    }

    $(tableName).append(`</tr>`);
}

function getHodDetails(object) {
    var returnObj = "<ol>";
    for (var i = 0; i < object.length; i++) {
        returnObj += `<li>${getNameById("department",object[i]["department"],"")}</li>`
    }
    returnObj += "</ol>";
    return returnObj;
}

//common edit mark index - tested123
function markEditIndex(index = -1, modalName = "", object = "", fromServer) {
    editIndex = index;
    $('#' + modalName).modal('show');
    //assignments  details modal when it edit modalName
    $('#' + modalName).on('shown.bs.modal', function(e) {
            if (editIndex != -1) {
                if (object == "jurisdictions") {
                    //Get the boundary to get boundaryType
                    commonApiGet("egov-location", "boundarys", "", {
                        "Boundary.id": (typeof employee[object][editIndex] == "object" ? employee[object][editIndex]["boundary"] : employee[object][editIndex]),
                        "Boundary.tenantId": tenantId
                    }, function(err, res) {
                        if (res) {
                            var _bnd = res.Boundary;
                            if (_bnd && _bnd.length) {
                                var jType = _bnd[0]["boundaryType"].id;
                                commonApiPost("egov-location", "boundarys", "getByBoundaryType", {
                                    boundaryTypeId: jType,
                                    tenantId
                                }, function(err, res) {
                                    if (res) {
                                        commonObject["jurisdictions_boundary"] = res.Boundary;
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
                                            boundary: (typeof employee[object][editIndex] == "object" ? employee[object][editIndex]["boundary"] : employee[object][editIndex])
                                        }

                                        for (key in employeeSubObject[object]) {
                                            $(`#${object}\\.${key}`).val(employeeSubObject[object][key]);
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    if(object == "assignments") {
                        $("#assignments\\.toDate").datepicker('setStartDate', null);
                    }
                    if(object == "assignments" && fromServer) {
                        //$("#assignmentDetailModal input, #assignmentDetailModal select").attr('disabled', false);
                        $("#assignmentDetailModal input, #assignmentDetailModal select").attr('disabled', true);
                        $("#assignments\\.fromDate").attr('disabled', true);
                        $("#assignments\\.toDate").attr('disabled', false);
                        $("#assignments\\.toDate").datepicker('setStartDate', new Date());
                        if(employee[object][editIndex].toDate) {
                            var date = employee[object][editIndex].toDate.split("/");
                            if(new Date().getTime() > employee[object][editIndex].fromToDate) {
                                $("#assignmentDetailModal input, #assignmentDetailModal select").attr('disabled', true);
                                $("#assignments\\.toDate").attr('disabled', false);
                                $("#assignments\\.toDate").datepicker('setStartDate', new Date());
                            }
                        }
                    }

                    employeeSubObject[object] = Object.assign({}, employee[object][editIndex]);
                    for (var key in employeeSubObject[object]) {
                        if (key == "documents")
                            continue;
                        else if (key == "position") {
                            setTimeout(function(key, object) {
                                getPositions({
                                    id: "assignments.department"
                                }, function() {
                                    for(var k=0; k<commonObject["assignments_position"].length; k++) {
                                        if(employeeSubObject[object][key] == commonObject["assignments_position"][k].id) {
                                            $(`#${object}\\.${key}`).val(commonObject["assignments_position"][k].name);
                                            break;
                                        }

                                    }
                                });
                            }, 200, key, object);
                        }

                        if (key == "isPrimary") {
                            if (employeeSubObject[object][key] == "true" || employeeSubObject[object][key] == true) {
                                $('[data-primary="yes"]').prop("checked", true);
                                $('[data-primary="no"]').prop("checked", false);
                            } else {
                                $('[data-primary="yes"]').prop("checked", false);
                                $('[data-primary="no"]').prop("checked", true);
                            }
                        } else if (key == "hod") {
                            if ((employeeSubObject[object][key] && employeeSubObject[object][key].constructor == Array && employeeSubObject[object][key].length) || (employeeSubObject[object][key] == true || employeeSubObject[object][key] == "true")) {
                                $('[data-hod="yes"]').prop("checked", true);
                                $('[data-hod="no"]').prop("checked", false);
                                $("#departments").show();
                                tempListBox = Object.assign([], employeeSubObject[object][key]);
                                $("#assignments\\.mainDepartments option:selected").removeAttr("selected");
                                var _val = [];
                                for (var i = 0; i < employeeSubObject[object][key].length; i++) {
                                    $("#assignments\\.mainDepartments option[value=" + employeeSubObject[object][key][i].department + "]").attr("selected", true);
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

//common Delete
function commonDeleteFunction(tableName, modalName, object, index) {
    employee[object].splice(index, 1);
    updateTable(tableName, modalName, object);
}

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

function getPositions(_this, cb) {
    if (($("#assignments\\.department").val() != "" && $("#assignments\\.designation").val() != "") && (_this.id == "assignments.department" || _this.id == "assignments.designation" || _this.id == "assignment.fromDate" || _this.id == "assignments.isPrimary")) {
        if (employeeSubObject["assignments"].isPrimary == "true") {
            if ($("#assignments\\.fromDate").val()) {
                var _date = $("#assignments\\.fromDate").val();
                commonApiPost("hr-masters", "vacantpositions", "_search", {
                    tenantId,
                    departmentId: $("#assignments\\.department").val(),
                    designationId: $("#assignments\\.designation").val(),
                    asOnDate: _date,
                    pageSize: 500
                }, function(err, res) {
                    if (res) {
                        commonObject["assignments_position"] = res.Position;
                        setPositionId(commonObject, employeeSubObject);
                        /*$(`#assignments\\.position`).html(`<option value=''>Select</option>`);
                        for (var i = 0; i < commonObject["assignments_position"].length; i++) {
                            $(`#assignments\\.position`).append(`<option value='${commonObject["assignments_position"][i]['id']}'>${commonObject["assignments_position"][i]['name']}</option>`)
                        }*/
                    }
                    if (cb) cb();
                });
            }
        } else {
            commonApiPost("hr-masters", "positions", "_search", {
                tenantId,
                departmentId: $("#assignments\\.department").val(),
                designationId: $("#assignments\\.designation").val(),
                pageSize: 500
            }, function(err, res) {
                if (res) {
                    commonObject["assignments_position"] = res.Position;
                    setPositionId(commonObject, employeeSubObject);
                    /*$(`#assignments\\.position`).html(`<option value=''>Select</option>`);
                    for (var i = 0; i < commonObject["assignments_position"].length; i++) {
                        $(`#assignments\\.position`).append(`<option value='${commonObject["assignments_position"][i]['id']}'>${commonObject["assignments_position"][i]['name']}</option>`)
                    }*/
                }
                if (cb) cb();
            });
        }
    }
}

function setPositionId(commonObject, employeeSubObject) {
    var _positions = [];
    for(var k=0; k<commonObject["assignments_position"].length; k++) {
        _positions.push(commonObject["assignments_position"][k].name);
    }

    $(`#assignments\\.position`).autocomplete({
        source: _positions,
        minLength: 0,
        change: function(e, ui) {
            for(var k=0; k<commonObject["assignments_position"].length; k++) {
                if(commonObject["assignments_position"][k].name == ui.item.value) {
                    employeeSubObject["assignments"]["position"] = commonObject["assignments_position"][k].id;
                    break;
                }
            }
        }
    })

    $(`#assignments\\.position`).focus(function() {
        $(this).autocomplete("search", $(this).val());
    });
}

function showAndPrint(currentEmployee) {
    employee = currentEmployee;
    $("#code").prop("disabled", true);
    $("#user\\.userName").prop("disabled", true);
    printValue("", currentEmployee);
    displayFiles(employee);
    for(var i=0; i<employee.assignments.length; i++) {
        employee.assignments[i].fromServer = true;
        var date = employee.assignments[i].toDate.split("/");
        employee.assignments[i].fromToDate = new Date(date[2], date[1]-1, date[0]).getTime();
    }

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
}

function showAndPrint2(currentEmployee) {
    employee = currentEmployee;
    $("#code").prop("disabled", true);
    printValue("", currentEmployee);
    displayFiles(employee);

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
}

function printValue(object = "", values) {
    if (object != "") {

    } else {
        for (var key in values) {
            (function(key) {
                if (key == "documents") return;
                if (typeof values[key] === "object" && key == "user") {
                    for (ckey in values[key]) {
                        if (typeof values[key][ckey] != "undefined" || values[key][ckey]) {
                            if (["signature", "photo"].indexOf(ckey) > -1) continue;
                            //Get description
                            if (ckey == "dob") {
                                $("[name='" + key + "." + ckey + "']").val(values[key][ckey] ? values[key][ckey].split("-")[2] + "/" + values[key][ckey].split("-")[1] + "/" + values[key][ckey].split("-")[0] : "");

                            } else if (ckey == "active") {
                                if ([true, "true"].indexOf(values[key][ckey]) > -1) {
                                    $('[data-active="yes"]').prop("checked", true);
                                    $('[data-active="no"]').prop("checked", false);
                                } else {
                                    $('[data-active="no"]').prop("checked", true);
                                    $('[data-active="yes"]').prop("checked", false);
                                }
                            } else {
                                $("[name='" + key + "." + ckey + "']").val(values[key][ckey] ? values[key][ckey] : "");

                            }
                        }
                    }
                } else if (key == "physicallyDisabled") {
                    if ([true, "true"].indexOf(values[key]) > -1) {
                        $('[data-ph="yes"]').prop("checked", true);
                    } else {
                        $('[data-ph="no"]').prop("checked", true);
                    }
                } else if (key == "medicalReportProduced") {
                    if ([true, "true"].indexOf(values[key]) > -1) {
                        $('[data-mrp="yes"]').prop("checked", true);
                    } else {
                        $('[data-mrp="no"]').prop("checked", true);
                    }
                } else if (key == "bank" && values[key]) {
                    commonApiPost("egf-masters", "bankbranches", "_search", {
                        "pageSize" : 500,
                        tenantId,
                        "bank": values[key]
                    }, function(err, res) {
                        if (res) {
                            commonObject["bankbranches"] = res["bankBranches"];
                            $(`#bankBranch`).html(`<option value=''>Select</option>`)
                            for (var i = 0; i < commonObject["bankbranches"].length; i++) {
                                $(`#bankBranch`).append(`<option value='${commonObject["bankbranches"][i]['id']}'>${commonObject["bankbranches"][i]['name']}</option>`)
                            }
                            $("[name='" + key + "']").val(values[key] ? values[key] : "");
                            $("[name='bankBranch']").val(values["bankBranch"] || "");
                        }
                    })
                } else if (values[key]) {
                    $("[name='" + key + "']").val(values[key] ? values[key] : "");
                } else {

                }
            })(key);
        }
    }
}


function displayFiles(employee) {
    var tBody = "#fileBody",
        count = 1;
    $(tBody).html("");

    if (employee.user && employee.user.signature) {
        appendTr(tBody, count, "Signature", employee.user.signature);
        count++;
    }

    if (employee.user && employee.user.photo) {
        appendTr(tBody, count, "Photo", employee.user.photo);
        count++;
    }

    for (var key in employee) {
        if (key == "documents" && employee[key] && employee[key].constructor == Array && employee[key].length > 0) {
            for (var i = 0; i < employee[key].length; i++) {
                appendTr(tBody, count, "Documents", employee[key][i]);
                count++;
            }
        } else if (employee[key] && employee[key].constructor == Array && employee[key].length > 0) {
            for (var i = 0; i < employee[key].length; i++) {
                if (typeof employee[key][i] == "object") {
                    for (key2 in employee[key][i]) {
                        if (key2 == "documents" && employee[key][i][key2].constructor == Array && employee[key][i][key2].length > 0) {
                            for (var j = 0; j < employee[key][i][key2].length; j++) {
                                appendTr(tBody, count, key, employee[key][i][key2][j]);
                                count++;
                            }
                        }
                    }
                }
            }
        }
    }
}

function appendTr(tBodyName, count, name, fileId) {
    if ($("#fileTable").css('display') == 'none')
        $("#fileTable").show();

    $(tBodyName).append(`<tr data-key=${count}>
                            <td>
                              ${count}
                            </td>
                            <td>
                              ${titleCase(name)}
                            </td>
                            <td>
                              <a href=${window.location.origin + CONST_API_GET_FILE + fileId} target="_blank">
                                Download
                              </a>
                            </td>
                            <td>
                              ${getUrlVars()["type"] == "view" ? "" : "<button type='button' class='btn btn-close btn-danger' onclick='deleteFile(event, `" + name + "`, `" + fileId + "`, `" + count + "`)'>Delete</button>"}
                            </td>
                          </tr>`);
              }

              function deleteFile(e, name, fileId, count) {
                e.stopPropagation();

                if($("[data-key='"+ count +"']").css("backgroundColor") == "rgb(211, 211, 211)") {
                  var ind = filesToBeDeleted[name.toLowerCase()].indexOf(fileId);
                  filesToBeDeleted[name.toLowerCase()].splice(ind, 1);
                  $("[data-key='"+ count +"']").css("backgroundColor","#ffffff");
                  $("[data-key='"+ count +"']").css("textDecoration","");
                  $("[data-key='"+ count +"'] button").text("Delete");
                } else {
                  if(!filesToBeDeleted[name.toLowerCase()])
                    filesToBeDeleted[name.toLowerCase()] = [];
                  filesToBeDeleted[name.toLowerCase()].push(fileId);
                  $("[data-key='"+ count +"']").css("backgroundColor","#d3d3d3");
                  $("[data-key='"+ count +"']").css("textDecoration","line-through");
                  $("[data-key='"+ count +"'] button").text("Undo");
                }
              }

function loadUI() {

                commonApiPost("hr-masters", "hrconfigurations", "_search", {
                    tenantId
                }, function(err, res) {
                    if (res) {
                        hrConfigurations = res;
                        if (hrConfigurations["HRConfiguration"]["Autogenerate_employeecode"] == "N" || typeof(hrConfigurations["HRConfiguration"]["Autogenerate_employeecode"]) == "undefined") {
                            $("#code").prop("disabled", false);
                        } else {
                            $("#code").prop("disabled", true);
                        }
                    }
                });

                commonObject = {
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
                    districtList,
                    jurisdictions_boundary: [],
                    yearOfPassing,
                    juridictionTypeForCity: [],
                    juridictionTypeForWard: [],
                    juridictionTypeForZone: []
                }

                commonObject["assignments_mainDepartments"] = commonObject["assignments_department"];
                commonObject["languagesKnown"] = commonObject["motherTongue"];
                commonObject["user_locale"] = commonObject["motherTongue"];
                commonObject["probation_designation"] = commonObject["assignments_designation"];
                commonObject["regularisation_designation"] = commonObject["assignments_designation"];
                commonObject["education_yearOfPassing"] = commonObject["yearOfPassing"];
                commonObject["technical_yearOfPassing"] = commonObject["yearOfPassing"];
                commonObject["test_yearOfPassing"] = commonObject["yearOfPassing"];
                commonObject["serviceHistory_department"] = commonObject["assignments_department"];
                commonObject["serviceHistory_designation"] = commonObject["assignments_designation"];
                commonObject["serviceHistory_city"] = commonObject["districtList"];


                for (var key in commonObject) {
                    var splitObject = key.split("_");
                    if(key == "serviceHistory_department"){
                        for (let i = 0; i < commonObject[key].length; i++) 
                        $(`#serviceHistory\\.department`).append(`<option value='${commonObject[key][i]['code']}'>${commonObject[key][i]['name']}</option>`)

                    } else if(key == "serviceHistory_designation"){
                        for (let i = 0; i < commonObject[key].length; i++) 
                        $(`#serviceHistory\\.designation`).append(`<option value='${commonObject[key][i]['code']}'>${commonObject[key][i]['name']}</option>`)

                    } else if(key == "serviceHistory_city"){
                        for (let i = 0; i < commonObject[key].length; i++) 
                        $(`#serviceHistory\\.city`).append(`<option value='${commonObject[key][i]['city']['localName']}'>${commonObject[key][i]['city']['localName']}</option>`)

                    } else if (splitObject.length < 2) {
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


            if (getUrlVars()["type"] == "update") {
                if (getUrlVars()["id"]) {
                        commonApiPost("hr-employee", "employees/" + getUrlVars()["id"], "_search", {
                            tenantId
                        }, function(err, res) {
                            var currentEmployee = res.Employee;
                            showAndPrint(currentEmployee);
                        });
                    } else {
                        commonApiPost("hr-employee", "employees", "_loggedinemployee", {
                            tenantId
                        }, function(err, res) {
                            var obj = res.Employee[0];
                            commonApiPost("hr-employee", "employees/" + obj.id, "_search", {
                                tenantId
                            }, function(err, res2) {
                                var currentEmployee = res2.Employee;
                                showAndPrint(currentEmployee);
                            });
                        });
                    }
            }

            if (getUrlVars()["type"] == "view") {
                if (getUrlVars()["id"]) {
                        commonApiPost("hr-employee", "employees/" + getUrlVars()["id"], "_search", {
                            tenantId
                        }, function(err, res) {
                            var currentEmployee = res.Employee;
                            showAndPrint2(currentEmployee);
                        });
                    } else {
                        commonApiPost("hr-employee", "employees", "_loggedinemployee", {
                            tenantId
                        }, function(err, res) {
                            var obj = res.Employee[0];
                            commonApiPost("hr-employee", "employees/" + obj.id, "_search", {
                                tenantId
                            }, function(err, res2) {
                                var currentEmployee = res2.Employee;
                                showAndPrint2(currentEmployee);
                            })
                        });
                    }

                    $("#createEmployeeForm input").prop("disabled", true);

                    $("#createEmployeeForm select").prop("disabled", true);

                    $("#createEmployeeForm textarea").prop("disabled", true);

                    $("#addEmployee").hide();

                    $(".btn-default").hide();
            }

                $("input[name='user.dob']").datepicker({
                    format: 'dd/mm/yyyy',
                    endDate: '-15y',
                    autoclose: true
                });

                $('#dateOfAppointment, #dateOfRetirement').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfAppointment, #dateOfRetirement').on("change", function(e) {
                    var _from = $('#dateOfAppointment').val();
                    var _to = $('#dateOfRetirement').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Appointment Date must be before Retirement Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });

                $('#dateOfAppointment, #dateOfTermination').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfAppointment, #dateOfTermination').on("change", function(e) {
                    var _from = $('#dateOfAppointment').val();
                    var _to = $('#dateOfTermination').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Appointment Date must be before Termination Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });


                $('#dateOfAppointment, #dateOfResignation').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfAppointment, #dateOfResignation').on("change", function(e) {
                    var _from = $('#dateOfAppointment').val();
                    var _to = $('#dateOfResignation').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Appointment Date must be before Resignation Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });

                $('#dateOfAppointment, #assignments\\.fromDate').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfAppointment, #assignments\\.fromDate').on("change", function(e) {
                    var _from = $('#dateOfAppointment').val();
                    var _to = $('#assignments\\.fromDate').val();
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Appointment Date must be before Assignment Date.");
                            $('#assignments\\.fromDate').val("");
                        }
                    }
                });

                $('#dateOfJoining, #dateOfAppointment').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfJoining, #dateOfAppointment').on("change", function(e) {
                    var _from = $('#dateOfJoining').val();
                    var _to = $('#dateOfAppointment').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 < date2) {
                            showError("Joining Date must be After Appointment Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });




                $('#user\\.dob, #dateOfAppointment').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#user\\.dob, #dateOfAppointment').on("change", function(e) {
                    var _from = $('#user\\.dob').val();
                    var _to = $('#dateOfAppointment').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Appointment Date must be After Date of Birth.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });





                $('#dateOfJoining, #dateOfRetirement').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true
                });

                $('#dateOfJoining, #dateOfRetirement').on("change", function(e) {
                    var _from = $('#dateOfJoining').val();
                    var _to = $('#dateOfRetirement').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Joining Date must be before Retirement Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });



                $('#dateOfJoining, #dateOfTermination').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true

                });
                $('#dateOfJoining, #dateOfTermination').on("change", function(e) {
                    var _from = $('#dateOfJoining').val();
                    var _to = $('#dateOfTermination').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Joining Date must be before Termination Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
                });


                $('#dateOfJoining, #dateOfResignation').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose: true

                });
                $('#dateOfJoining, #dateOfResignation').on("change", function(e) {
                    var _from = $('#dateOfJoining').val();
                    var _to = $('#dateOfResignation').val();
                    var _triggerId = e.target.id;
                    if (_from && _to) {
                        var dateParts1 = _from.split("/");
                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                        var date1 = new Date(newDateStr);
                        var dateParts2 = _to.split("/");
                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                        var date2 = new Date(newDateStr);
                        if (date1 > date2) {
                            showError("Joining Date must be before Resignation Date.");
                            $('#' + _triggerId).val("");
                        }
                    }
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

                $("input[name='serviceHistory.serviceTo']").datepicker({
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

                //Getting data for user input
                $("input").on("keyup change", function() {
                    fillValueToObject(this);
                    getPositions(this);
                });

                //Getting data for user input
                $("select").on("change", function() {
                    var _this = this;
                    if (_this.id == "bank") {
                        commonApiPost("egf-masters", "bankbranches", "_search", {
                            "pageSize" : 500,
                            tenantId,
                            "bank": _this.value
                        }, function(err, res) {
                            if (res) {
                                commonObject["bankbranches"] = res["bankBranches"];
                                $(`#bankBranch`).html(`<option value=''>Select</option>`)
                                for (var i = 0; i < commonObject["bankbranches"].length; i++) {
                                    $(`#bankBranch`).append(`<option value='${commonObject["bankbranches"][i]['id']}'>${commonObject["bankbranches"][i]['name']}</option>`)
                                }
                            }
                        });
                    }
                    if (_this.id == "jurisdictions.jurisdictionsType") {
                        commonApiPost("egov-location", "boundarys", "getByBoundaryType", {
                            boundaryTypeId: _this.value,
                            tenantId
                        }, function(err, res) {
                            if (res) {
                                commonObject["jurisdictions_boundary"] = res.Boundary;
                                if (_this.value == "CITY") {
                                    commonObject["juridictionTypeForCity"] = commonObject["jurisdictions_boundary"];
                                } else if (_this.value == "WARD") {
                                    commonObject["juridictionTypeForWard"] = commonObject["jurisdictions_boundary"];

                                } else {
                                    commonObject["juridictionTypeForZone"] = commonObject["jurisdictions_boundary"];

                                }
                                $(`#jurisdictions\\.boundary`).html(`<option value=''>Select</option>`)

                                for (var i = 0; i < commonObject["jurisdictions_boundary"].length; i++) {
                                    $(`#jurisdictions\\.boundary`).append(`<option value='${commonObject["jurisdictions_boundary"][i]['id']}'>${commonObject["jurisdictions_boundary"][i]['name']}</option>`)
                                }
                            }
                        });
                    }
                    if(_this.id == "serviceHistory.city"){
                        commonApiPost("hr-masters", "designations", "_search", {
                            tenantId: tenantId,
                            destinationTenant: "ap."+_this.value.toLowerCase(),
                            pageSize: 500
                        }, function(err, res) {
                            if (res) {
                                commonObject["serviceHistory_designation"] = res["Designation"];
                                $(`#serviceHistory\\.designation`).html(`<option value=''>Select Designation</option>`)
                                for (var i = 0; i < commonObject["serviceHistory_designation"].length; i++) {
                                    $(`#serviceHistory\\.designation`).append(`<option value='${commonObject["serviceHistory_designation"][i]['code']}'>${commonObject["serviceHistory_designation"][i]['name']}</option>`)
                                }
                            }
                        });

                        commonApiPost("hr-masters", "departments", "_search", {
                            tenantId: tenantId,
                            destinationTenant: "ap."+_this.value.toLowerCase(),
                            pageSize: 500
                        }, function(err, res) {
                            if (res) {
                                commonObject["serviceHistory_department"] = res["Department"];
                                $(`#serviceHistory\\.department`).html(`<option value=''>Select Department</option>`)
                                for (var i = 0; i < commonObject["serviceHistory_department"].length; i++) {
                                    $(`#serviceHistory\\.department`).append(`<option value='${commonObject["serviceHistory_department"][i]['code']}'>${commonObject["serviceHistory_department"][i]['name']}</option>`)
                                }
                            }
                        });

                    }
                    getPositions(_this);
                    fillValueToObject(_this);
                });

                //Getting data for user input
                $("textarea").on("keyup change", function() {
                    fillValueToObject(this);
                })

                //initial setup
                $("#departments").hide();

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
                        city:"",
                        serviceInfo: "",
                        serviceFrom: "",
                        serviceTo:"",
                        department:"",
                        designation:"",
                        remarks: "",
                        orderNo: "",
                        documents: null,
                        isAssignmentBased:false
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

                final_validatin_rules = Object.assign(validation_rules, commom_fields_rules);
                for (var key in final_validatin_rules) {
                    if (final_validatin_rules[key].required) {
                        // console.log(key.split("."));
                        if (key.split(".").length == 1) {
                            $(`label[for=${key}]`).append(`<span> *</span>`);

                        }
                    }
                };

                $.validator.addMethod('phone', function(value) {
                    return value ? /^[0-9]{10}$/.test(value) : true;
                }, 'Please enter a valid phone number.');

                $.validator.addMethod('aadhar', function(value) {
                    return value ? /^[0-9]{12}$/.test(value) : true;
                }, 'Please enter a valid aadhar.');

                $.validator.addMethod('panNo', function(value) {
                    return value ? /^(?:[0-9]+[a-z]|[a-z]+[0-9])[a-z0-9]*$/i.test(value) && value.length === 10 : true;
                }, 'Please enter a valid pan.');

                $.validator.addMethod('alpha', function(value) {
                    return value ? /^[a-zA-Z]*$/.test(value) : true;
                }, 'Only alphabets allowed.');

                $.validator.addMethod('alphaWSpcNDot', function(value) {
                    return value ? /^[a-zA-Z \.]*$/.test(value) : true;
                }, 'Only alphabets and spaces allowed.');

                $.validator.addMethod('alphanumeric', function(value) {
                    return value ? /^[a-zA-Z0-9]*$/.test(value) : true;
                }, 'Only alphanumeric characters allowed.');

                $.validator.addMethod('alphanumericWSpc', function(value) {
                    return value ? /^[a-zA-Z0-9 ]*$/.test(value) : true;
                }, 'Only alphanumeric characters and space allowed.');

                $.validator.addMethod('alphanumericWSplChar', function(value) {
                    return value ? /^[a-zA-Z0-9\-\/\_]*$/.test(value) : true;
                }, 'Only alphanumeric with -/_ allowed.');

                $.validator.addMethod('alphanumericWSplCharNSpc', function(value) {
                    return value ? /^[a-zA-Z0-9\-\/\_ ]*$/.test(value) : true;
                }, 'Only alphanumeric with -/_ allowed.');

                $.validator.addMethod('alphanumericWAllSplCharNSpc', function(value) {
                    return value ? /^[a-zA-Z0-9\-\/\_ \#\(\)\,\.\&]*$/.test(value) : true;
                }, 'Only alphanumeric with -/_#(),.& allowed.');

                $.validator.addMethod('alphaWSplChar', function(value) {
                    return value ? /^[a-zA-Z\-\/\_\.\*\@\#\$\%\^\&\ ]*$/.test(value) : true;
                }, 'Only alphabets with special characters allowed.');

                $.validator.addMethod('year', function(value) {
                    return value ? /^\d{4}$/.test(value) : true;
                }, 'Year should have 4 digits.');

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


                $("#addEmployee").on("click", function(e) {
                    e.preventDefault();
                    $("#createEmployeeForm").submit();
                });


                // Adding Jquery validation dynamically
                $("#createEmployeeForm").validate({
                    rules: final_validatin_rules,
                    submitHandler: function(form) {
                        // console.log(form);
                        if (!hasAllRequiredFields(employee)) {
                            showError("Please enter all mandatory fields.");
                        } else if (employee.assignments.length > 0 && employee.jurisdictions.length > 0) {
                            if (!isHavingPrimary()){
                                return showError("Atleast one primary assignment is required.");
                              }else{
                                for (var i = 0; i < employee.assignments.length; i++) {

                                    var _from = employee.dateOfAppointment;
                                    var _to = employee.assignments[i].fromDate;
                                    if (_from && _to) {
                                        var dateParts1 = _from.split("/");
                                        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                                        var date1 = new Date(newDateStr);
                                        var dateParts2 = _to.split("/");
                                        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                                        var date2 = new Date(newDateStr);
                                        if (date1 > date2) {
                                            return showError("Appointment Date must be before Assignment Date.");
                                        }
                                    }
                                }
                              }
                            //Call api
                            var __emp = Object.assign({}, employee);

                            if (employee["jurisdictions"] && employee["jurisdictions"].length) {
                                var empJuridictions = employee["jurisdictions"];
                                employee["jurisdictions"] = [];
                                for (var i = 0; i < empJuridictions.length; i++) {
                                    if (typeof empJuridictions[i] == "object")
                                        employee["jurisdictions"].push(empJuridictions[i].boundary);
                                    else
                                        employee["jurisdictions"].push(empJuridictions[i]);
                                }
                            }

                            if (employee.user && employee.user.dob && getUrlVars()["type"] == "update" && employee.user.dob.indexOf("-") > -1) {
                                var _date = employee.user.dob.split("-");
                                employee.user.dob = _date[2] + "/" + _date[1] + "/" + _date[0];
                            }

                            if (getUrlVars()["type"] == "update") {
                                checkNRemoveFile();
                            }

                            for(var k=0; k<employee["assignments"].length; k++) {
                                delete employee["assignments"][k].fromServer;
                            }

                            //Upload files if any
                            uploadFiles(employee, function(err, emp) {
                                if (err) {
                                    //Handle error
                                } else {
                                    $.ajax({
                                        url: baseUrl + "/hr-employee/employees/" + ((getUrlVars()["type"] == "update") ? "_update" : "_create") + "?tenantId=" + tenantId,
                                        type: 'POST',
                                        dataType: 'json',
                                        data: JSON.stringify({
                                            RequestInfo: requestInfo,
                                            Employee: emp
                                        }),
                                        headers: {
                                            'auth-token': authToken
                                        },
                                        contentType: 'application/json',
                                        success: function(res) {
                                            window.location.href = "app/hr/common/employee-search.html?type=view";
                                        },
                                        error: function(err) {
                                            if (err.responseJSON && err.responseJSON.Error) {
                                                var _err = err["responseJSON"].Error.message || "";
                                                if (err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
                                                    for (var key in err["responseJSON"].Error.fields) {
                                                        _err += "\n " + err["responseJSON"].Error.fields[key] + " ";
                                                    }
                                                    showError(_err);
                                                } else {
                                                    showError(err["statusText"]);
                                                }
                                                employee = Object.assign({}, __emp);
                                            } else {
                                                showError(err["statusText"]);
                                                employee = Object.assign({}, __emp);
                                            }
                                        }
                                    });
                                }
                            })
                        } else {
                            showError("Please enter atleast one assignment and jurisdiction.");
                        }
                    }
                })

                addMandatoryStart(assignmentDetailValidation, "assignments");

                addMandatoryStart(jurisdictions, "jurisdictions");

                addMandatoryStart(serviceHistory, "serviceHistory");

                addMandatoryStart(probation, "probation");

                addMandatoryStart(regularisation, "regularisation");

                addMandatoryStart(education, "education");

                addMandatoryStart(test, "test");

                addMandatoryStart(technical, "technical");

                addMandatoryStart(user, "user");

}

    $(document).ready(function() {
            if (window.opener && window.opener.document) {
                var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
                if (logo_ele && logo_ele[0]) {
                    document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
                }
            }
            if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Employee");

            $("#assignmentDetailsModalOpenBtn").on('click', function() {
                $("#assignments\\.toDate").datepicker('setStartDate', null);
                $("#assignmentDetailModal input, #assignmentDetailModal select").attr('disabled', false);
            })
            getDropdown("employeeType", function(res) {
                employeeType = res;
                checkCount();
            })
            getDropdown("employeeStatus", function(res) {
                employeeStatus = res;
                checkCount();
            },{objectName:"Employee Master"})
            getDropdown("group", function(res) {
                group = res;
                checkCount();
            })
            getDropdown("bank", function(res) {
                bank = res;
                checkCount();
            })
            getDropdown("category", function(res) {
                category = res;
                checkCount();
            })
            getDropdown("community", function(res) {
                community = res;
                checkCount();
            })
            getDropdown("religion", function(res) {
                religion = res;
                checkCount();
            })
            getDropdown("motherTongue", function(res) {
                motherTongue = res;
                checkCount();
            })
            getDropdown("user_bloodGroup", function(res) {
                user_bloodGroup = res;
                checkCount();
            })
            getDropdown("maritalStatus", function(res) {
                maritalStatus = res;
                checkCount();
            })
            getDropdown("assignments_function", function(res) {
                assignments_function = res;
                checkCount();
            })
            getDropdown("assignments_functionary", function(res) {
                assignments_functionary = res;
                checkCount();
            })
            getDropdown("assignments_fund", function(res) {
                assignments_fund = res;
                checkCount();
            })
            getDropdown("assignments_grade", function(res) {
                assignments_grade = res;
                checkCount();
            })
            getDropdown("recruitmentQuota", function(res) {
                recruitmentQuota = res;
                checkCount();
            })
            getDropdown("recruitmentType", function(res) {
                recruitmentType = res;
                checkCount();
            })
            getDropdown("recruitmentMode", function(res) {
                recruitmentMode = res;
                checkCount();
            })
            getDropdown("assignments_department", function(res) {
                assignments_department = res;
                checkCount();
            })
            getDropdown("assignments_designation", function(res) {
                assignments_designation = res;
                checkCount();
            })
            getDropdown("assignments_position", function(res) {
                assignments_position = res;
                checkCount();
            })
            getDropdown("jurisdictions_jurisdictionsType", function(res) {
                jurisdictions_jurisdictionsType = res;
                checkCount();
            })
            getDropdown("districtList", function(res) {
                districtList = res;
                checkCount();
            })

            $('#close').on("click", function() {
                window.close();
            })
});
