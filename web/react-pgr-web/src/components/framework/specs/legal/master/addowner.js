var dat = {
  "legal.create": {
    numCols: 4,
    title:"advocates.search.document.title",
    useTimestamp: true,
    objectName: "agencies",
    groups: [
       {
        name: "agencyDetails",
        label: "advocates.create.group.title.agencyDetails",
        fields: [
         {
            name: "advocateTitle",
            jsonPath: "title",
            label: "advocates.create.advocateTitle",
            type: "singleValueList",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "",
            defaultValue:[{ key:"Mr",value:"Mr"},{ key:"Mrs",value:"Mrs"},{ key:"Ms",value:"Ms"},{ key:"Miss",value:"Miss"}]
          },
          {
            name: "aadharNumber",
            jsonPath: "aadhar",
            label: "advocates.create.aadharNumber",
            pattern: "",
            type: "aadhar",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "gender",
            jsonPath: "gender",
            label: "advocates.create.gender",
            pattern: "",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: "",
             defaultValue:[{ key:"Male",value:"Male"},{ key:"Female",value:"Female"}]
          },
          {
            name: "firstName",
            jsonPath: "firstName",
            label: "advocates.create.firstName",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "secondName",
            jsonPath: "secondName",
            label: "advocates.create.secondName",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "lastName",
            jsonPath: "lastName",
            label: "advocates.create.lastName",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "age",
            jsonPath: "age",
            label: "advocates.create.age",
            pattern: "",
            type: "number",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "dob",
            jsonPath: "dob",
            label: "advocates.create.dob",
            pattern: "",
            type: "datePicker",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "address",
            jsonPath: "address",
            label: "advocates.create.address",
            pattern: "",
            type: "textarea",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "mobileNumber",
            jsonPath: "mobileNumber",
            label: "advocates.create.mobileNumber",
            pattern: "",
            type: "mobileNumber",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "contactNumber",
            jsonPath: "contactNo",
            label: "advocates.create.contactNumber",
            pattern: "",
            type: "mobileNumber",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "email",
            jsonPath: "emailId",
            label: "advocates.create.email",
            pattern: "",
            type: "email",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "panNumber",
            jsonPath: "pan",
            label: "advocates.create.panNumber",
            pattern: "",
            type: "pan",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "VATTinNumber",
            jsonPath: "vatTinNo",
            label: "advocates.create.VATTinNumber",
            pattern: "",
            type: "number",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          }
        ]
      }
    ]
  }

};
export default dat;
