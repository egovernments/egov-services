import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import {Tabs, Tab} from 'material-ui/Tabs';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import {Card, CardHeader, CardText, CardTitle} from 'material-ui/Card';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import Dialog from 'material-ui/Dialog';
import {translate} from '../../common/common';
import AutoComplete from 'material-ui/AutoComplete';
import Api from '../../../api/api';
import $ from "jquery";

const checkIfNoDup = function(employee, subObject) {
    if (employee["jurisdictions"].length === 0)
        return true;
    else {
        for (let i = 0; i < employee["jurisdictions"].length; i++) {
            if (employee["jurisdictions"][i] == subObject)
                return false;
        }
    }

    return true;
}

const validateDates = function(employee, subObject, editIndex) {
    if (subObject.isPrimary == "true" || subObject.isPrimary == true) {
        for (let i = 0; i < employee["assignments"].length; i++) {
            if (employee["assignments"][i].isPrimary && (editIndex == '' || (editIndex && i != editIndex))) {
                var subFromDate = new Date(subObject.fromDate.split("/")[1] + "/" + subObject.fromDate.split("/")[0] + "/" + subObject.fromDate.split("/")[2]).getTime();
                var fromDate = new Date(employee["assignments"][i].fromDate.split("/")[1] + "/" + employee["assignments"][i].fromDate.split("/")[0] + "/" + employee["assignments"][i].fromDate.split("/")[2]).getTime();
                var subToDate = new Date(subObject.toDate.split("/")[1] + "/" + subObject.toDate.split("/")[0] + "/" + subObject.toDate.split("/")[2]).getTime();
                var toDate = new Date(employee["assignments"][i].toDate.split("/")[1] + "/" + employee["assignments"][i].toDate.split("/")[0] + "/" + employee["assignments"][i].toDate.split("/")[2]).getTime();

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

const getNameById = function(object, id) {
    if(!id) return '';
    for(var i=0; i<object.length; i++) {
        if(id == object[i].id)
            return object[i].name;
    }
}

const checkRequiredFields = function(type, object) {
  let errorText = {};
  switch(type) {
    case 'assignment':
      if(!object.fromDate) {
        errorText["assignments.fromDate"] = "Required";
      } else if(!object.toDate) {
        errorText["assignments.toDate"] = "Required";
      } else if(!object.department) {
        errorText["assignments.department"] = "Required";
      } else if(!object.designation) {
        errorText["assignments.designation"] = "Required";
      } else if(!object.position) {
        errorText["assignments.position"] = "Required";
      } else if((object.hod == true || object.hod == "true") && (!object.mainDepartments || (object.mainDepartments && object.mainDepartments.length == 0))) {
        
        errorText["assignments.mainDepartments"] = "Required";
      }
      break;
    case 'jurisdiction':
      if(!object.jurisdictionsType) {
        errorText["jurisdictions.jurisdictionsType"] = "Required";
      } else if(!object.boundary) {
        errorText["jurisdictions.boundary"] = "Required";
      }
      break;
    case 'serviceDet':
      if(!object.serviceInfo) {
        errorText["serviceHistory.serviceInfo"] = "Required";
      } else if(!object.serviceFrom) {
        errorText["serviceHistory.serviceFrom"] = "Required";
      }
      break;
    case 'probation':
      if(!object.designation) {
        errorText["probation.designation"] = "Required";
      } else if(!object.declaredOn) {
        errorText["probation.declaredOn"] = "Required";
      } else if(!object.orderDate) {
        errorText["probation.orderDate"] = "Required";
      }
      break;
    case 'regular':
      if(!object.designation) {
        errorText["regularisation.designation"] = "Required";
      } else if(!object.declaredOn) {
        errorText["regularisation.declaredOn"] = "Required";
      } else if(!object.orderDate) {
        errorText["regularisation.orderDate"] = "Required";
      }
      break;
    case 'edu':
      if(!object.designation) {
        errorText["education.qualification"] = "Required";
      } else if(!object.declaredOn) {
        errorText["education.yearOfPassing"] = "Required";
      }
      break;
    case 'tech':
      if(!object.skill) {
        errorText["technical.skill"] = "Required";
      }
      break;
    case 'dept':
      if(!object.skill) {
        errorText["test.test"] = "Required";
      } else if(!object.skill) {
        errorText["test.yearOfPassing"] = "Required";
      }
      break;
  }

  return errorText;
}

const hasFile = function(elements) {
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

const isHavingPrimary = function(employee) {
    for (var i = 0; i < employee.assignments.length; i++) {
        if (employee.assignments[i].isPrimary == "true" || employee.assignments[i].isPrimary == true) {
            return true;
        }

    }
    return false;
}

const makeAjaxUpload = function(file, cb) {
    if (file.constructor == File) {
        let formData = new FormData();
        formData.append("jurisdictionId", localStorage.getItem("tenantId"));
        formData.append("module", "HR");
        formData.append("file", file);

        Api.commonApiPost("/filestore/v1/files",{}, formData).then(function(res) {
          cb(null, res);
        }, function(err){
          cb("");
        });
    } else {
        cb(null, {
            files: [{
                fileStoreId: file
            }]
        })
    }
}

const uploadFiles = function(employee, cb) {
    let len;
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

const getBoundaryValues = function(allBoundariesList, boundary, self, ind) {
    for(var i=0; i<allBoundariesList.length; i++) {
        if(allBoundariesList[i].id == boundary){
            return (
                <tr key={ind}>
                    <td>{allBoundariesList[i].boundaryType.name}</td>
                    <td>{allBoundariesList[i].name}</td>
                    <td>
                        {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(ind, 'jurisdictions')}}></span>}&nbsp;&nbsp;
                        {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(ind, 'jurisdictions')}}></span>}
                    </td>
                </tr>
            )
        }
    }
}

const assignmentsDefState = {
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
    govtOrderNumber: "",
    documents: null
};
const jurisDefState = {
    jurisdictionsType: "",
    boundary: ""
};
const serviceDefState = {
    serviceInfo: "",
    serviceFrom: "",
    remarks: "",
    orderNo: "",
    documents: null
};
const probDefState = {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
};
const regDefState = {
    designation: "",
    declaredOn: "",
    orderNo: "",
    orderDate: "",
    remarks: "",
    documents: null
};
const eduDefState = {
    qualification: "",
    majorSubject: "",
    yearOfPassing: "",
    university: "",
    documents: null
};
const techDefState = {
    skill: "",
    grade: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
};
const deptDefState = {
    test: "",
    yearOfPassing: "",
    remarks: "",
    documents: null
};

class Employee extends Component {
	constructor(props) {
		super(props);
		this.state = {
      open: false,
      editIndex: '',
      modal: '',
			employeetypes: [],
			statuses: [],
			groups: [],
			banks: [],
			categories: [],
			maritalstatuses: [],
			bloodgroups: [],
			languages: [],
			religions: [],
			recruitmentmodes: [],
			recruitmenttypes: [],
			grades: [],
			funds: [],
			functionaries: [],
			functions: [],
			boundarytypes: [],
			designations: [],
			departments: [],
			recruitmentquotas: [],
			genders: [],
			communities: [],
            allBoundariesList: [],
      bankBranches: [],
      boundaries: [],
      positionList: [],
      positionListConfig: {
         text: 'name',
        value: 'id'
      },
      screenType: "create",
      errorText: {},
      allPosition: [],
      subObject: {
        assignments: Object.assign({}, assignmentsDefState),
        jurisdictions: Object.assign({}, jurisDefState),
        serviceHistory: Object.assign({}, serviceDefState),
        probation: Object.assign({}, probDefState),
        regularisation: Object.assign({}, regDefState),
        education: Object.assign({}, eduDefState),
        technical: Object.assign({}, techDefState),
        test: Object.assign({}, deptDefState)
      }
		}
	}

	setInitialState = (_state) => {
		this.setState(_state);
	}

  loadBranches = (id) => {
    let self = this;
    if(!id)
      self.setState({
        bankBranches: []
      });
    Api.commonApiPost("egf-masters/bankbranches/_search", {"bank": id}).then(function(res) {
      self.setState({
        bankBranches: res["bankBranches"]
      })
    }, function(err) {
      self.setState({
        bankBranches: []
      })
    })
  }

  loadBoundaries = (id) => {
    let self = this;
    Api.commonApiPost("egov-location/boundarys/getByBoundaryType", {"boundaryTypeId": id}).then(function(res) {
      self.setState({
        boundaries: res["Boundary"]
      })
    }, function(err) {

    })
  }

  getDate = (dateStr) => {
    if(dateStr && typeof dateStr == "string"){
        if(dateStr.indexOf("/") > -1) {
            var dateArr = dateStr.split("/");
            return new Date(dateArr[2], Number(dateArr[1])-1, dateArr[0]);
        } else if(dateStr.indexOf("-") > -1) {
            var dateArr = dateStr.split("-");
            return new Date(dateArr[0], Number(dateArr[1])-1, dateArr[2]);
        }
    } else return "";
  }

	fetchURLData(url, query={}, defaultObject, cb) {
		Api.commonApiPost(url, query).then(function(res){
			  cb(res);
   		}, function(err) {
   			cb(defaultObject);
   		})
	}

  handleClose = () => {
    this.setState({
      open: false
    })
  }

  handleStateChange = (e, parent, key, isDate) => {
    let self = this;
    let val = e.target.value;
    if(isDate) {
      val = new Date(val);
      val = ('0' + val.getDate()).slice(-2) + '/' + ('0' + (val.getMonth()+1)).slice(-2) + '/' + val.getFullYear();
    }
    self.setState({
      subObject: {
        [parent]: {
          ...self.state.subObject[parent],
          [key]: val
        }
      }
    }, function() {
      if(parent == "assignments" && self.state.subObject[parent].designation && self.state.subObject[parent].department) {
        if((self.state.subObject[parent].isPrimary == "true" || self.state.subObject[parent].isPrimary == true)) {
          if(self.state.subObject[parent].fromDate) {
            Api.commonApiPost("hr-masters/vacantpositions/_search", {
                    departmentId: self.state.subObject[parent].department,
                    designationId: self.state.subObject[parent].designation,
                    asOnDate: self.state.subObject[parent].fromDate,
                    pageSize: 100
            }).then(function(res) {
              self.setState({
                positionList: res["Position"]
              })
            }, function(err) {

            })
          }
        } else {
            Api.commonApiPost("hr-masters/positions/_search", {
                    departmentId: self.state.subObject[parent].department,
                    designationId: self.state.subObject[parent].designation,
                    pageSize: 100
            }).then(function(res) {
              self.setState({
                positionList: res["Position"]
              })
            }, function(err) {

            })
        }
      }
    })
  }

  editModalOpen = (ind, type) => {
    let dat;
    switch (type) {
      case 'assignments':
        dat = Object.assign({}, this.props.Employee.assignments[ind]);
        if(dat.hod && dat.hod.length) {
            dat.mainDepartments = [];
            for(var i=0; i<dat.hod.length; i++) {
                dat.mainDepartments.push(dat.hod[i]);
            }
            dat.hod = true;
        }
        this.setState({
          open: true,
          modal: 'assignment',
          errorText: {},
          editIndex: ind,
          subObject: {
            'assignments': dat
          }
        })
        break;
      case 'jurisdictions':
        dat = {};
        for(var i=0; i<this.state.allBoundariesList.length; i++) {
            if(this.props.Employee.jurisdictions[ind] == this.state.allBoundariesList[i].id) {
                dat["jurisdictionsType"] = this.state.allBoundariesList[i].boundaryType.id + "";
                this.loadBoundaries(dat["jurisdictionsType"]);
                dat["boundary"] = this.state.allBoundariesList[i].id;
                break;
            }
        }
        this.setState({
          open: true,
          modal: 'jurisdiction',
          errorText: {},
          editIndex: ind,
          subObject: {
            'jurisdictions': dat
          }
        })
        break;
      case 'serviceDet':
        dat = Object.assign({}, this.props.Employee.serviceHistory[ind]);
        this.setState({
          open: true,
          modal: 'serviceDet',
          errorText: {},
          editIndex: ind,
          subObject: {
            'serviceHistory': dat
          }
        })
        break;
      case 'probation':
        dat = Object.assign({}, this.props.Employee.probation[ind]);
        this.setState({
          open: true,
          modal: 'probation',
          errorText: {},
          editIndex: ind,
          subObject: {
            'probation': dat
          }
        })
        break;
      case 'regular':
        dat = Object.assign({}, this.props.Employee.regularisation[ind]);
        this.setState({
          open: true,
          modal: 'regular',
          errorText: {},
          editIndex: ind,
          subObject: {
            'regularisation': dat
          }
        })
        break;
      case 'edu':
        dat = Object.assign({}, this.props.Employee.education[ind]);
        this.setState({
          open: true,
          modal: 'edu',
          errorText: {},
          editIndex: ind,
          subObject: {
            'education': dat
          }
        })
        break;
      case 'tech':
        dat = Object.assign({}, this.props.Employee.technical[ind]);
        this.setState({
          open: true,
          errorText: {},
          modal: 'tech',
          editIndex: ind,
          subObject: {
            'technical': dat
          }
        })
        break;
      case 'dept':
        dat = Object.assign({}, this.props.Employee.jurisdictions[ind]);
        this.setState({
          open: true,
          errorText: {},
          modal: 'dept',
          editIndex: ind,
          subObject: {
            'test': dat
          }
        })
        break;
    }
  }

  delModalOpen = (ind, type) => {
    switch (type) {
      case 'assignments':
        let assignments = Object.assign([], this.props.Employee.assignments);
        assignments.splice(ind);
        this.props.handleChange({target: {value: assignments}}, "assignments", false, '');
        break;
      case 'jurisdictions':
        let jurisdictions = Object.assign([], this.props.Employee.jurisdictions);
        assignments.splice(ind);
        this.props.handleChange({target: {value: jurisdictions}}, "jurisdictions", false, '');
        break;
      case 'serviceDet':
        let serviceHistory = Object.assign([], this.props.Employee.serviceHistory);
        serviceHistory.splice(ind);
        this.props.handleChange({target: {value: serviceHistory}}, "serviceHistory", false, '');
        break;
      case 'probation':
        let probation = Object.assign([], this.props.Employee.probation);
        probation.splice(ind);
        this.props.handleChange({target: {value: probation}}, "probation", false, '');
        break;
      case 'regular':
        let regularisation = Object.assign([], this.props.Employee.regularisation);
        regularisation.splice(ind);
        this.props.handleChange({target: {value: regularisation}}, "regularisation", false, '');
        break;
      case 'edu':
        let education = Object.assign([], this.props.Employee.education);
        education.splice(ind);
        this.props.handleChange({target: {value: education}}, "education", false, '');
        break;
      case 'tech':
        let techDefState = Object.assign([], this.props.Employee.techDefState);
        techDefState.splice(ind);
        this.props.handleChange({target: {value: techDefState}}, "techDefState", false, '');
        break;
      case 'dept':
        let test = Object.assign([], this.props.Employee.test);
        test.splice(ind);
        this.props.handleChange({target: {value: test}}, "test", false, '');
        break;
    }
  }

  submitModalData = (e) => {
    let {editIndex} = this.state, self = this;
    let errorText = {};
    switch (this.state.modal) {
      case 'assignment':

        errorText = checkRequiredFields('assignment', this.state.subObject.assignments);

        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        if(self.getDate(this.state.subObject.assignments.fromDate).getTime() > self.getDate(this.state.subObject.assignments.toDate).getTime()) {
            return self.props.toggleSnackbarAndSetText(true, "From Date should be less than To Date.", false, true);
        }

        let assignments = Object.assign([], this.props.Employee.assignments || []);
        var asst = {...this.state.subObject.assignments};
        if(asst.hod == "true" || asst.hod == true) {
            asst.hod = [];
            for(let i=0; i<asst.mainDepartments.length; i++) {
                asst.hod.push(asst.mainDepartments[i]);
            }
        }

        delete asst.mainDepartments;
        if(!validateDates(this.props.Employee, asst, editIndex)) {
            return this.props.toggleSnackbarAndSetText(true, "Assignment dates overlapping.", false, true);
        }

        if(this.state.editIndex === '')
          assignments.push(asst);
        else
          assignments[editIndex] = Object.assign({}, asst);
        this.props.handleChange({target:{value: assignments}}, "assignments", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
             assignments: Object.assign({}, assignmentsDefState)
          }
        })
        break;
      case 'jurisdiction':
        errorText = checkRequiredFields('jurisdiction', this.state.subObject.jurisdictions);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let jurisdictions = Object.assign([], this.props.Employee.jurisdictions || []);
        var jst = this.state.subObject.jurisdictions.boundary;
        if(!checkIfNoDup(this.props.Employee, jst)) {
            return this.props.toggleSnackbarAndSetText(true, "Duplicate entry not allowed.", false, true);
        }

        if(this.state.editIndex === '')
          jurisdictions.push(jst);
        else
          jurisdictions[editIndex] = Object.assign({}, jst);
        this.props.handleChange({target:{value: jurisdictions}}, "jurisdictions", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            jurisdictions: Object.assign({}, jurisDefState)
          }
        })
        break;
      case 'serviceDet':
        errorText = checkRequiredFields('serviceDet', this.state.subObject.serviceHistory);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let serviceHistory = Object.assign([], this.props.Employee.serviceHistory || []);
        if(this.state.editIndex === '') {
          serviceHistory.push(this.state.subObject.serviceHistory);
        } else {
          serviceHistory[editIndex] = Object.assign({}, this.state.subObject.serviceHistory);
        }

        this.props.handleChange({target:{value: serviceHistory}}, "serviceHistory", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            serviceHistory: Object.assign({}, serviceDefState)
          }
        })
        break;
      case 'probation':
        errorText = checkRequiredFields('probation', this.state.subObject.probation);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let probation = Object.assign([], this.props.Employee.probation || []);
        if(this.state.editIndex === '')
          probation.push(this.state.subObject.probation);
        else
          probation[editIndex] = Object.assign({}, this.state.subObject.probation);
        this.props.handleChange({target:{value: probation}}, "probation", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            probation: Object.assign({}, probDefState)
          }
        })
        break;
      case 'regular':
        errorText = checkRequiredFields('regular', this.state.subObject.regularisation);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let regularisation = Object.assign([], this.props.Employee.regularisation || []);
        if(this.state.editIndex === '')
          regularisation.push(this.state.subObject.regularisation);
        else
          regularisation[editIndex] = Object.assign({}, this.state.subObject.regularisation);
        this.props.handleChange({target:{value: regularisation}}, "regularisation", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            regularisation: Object.assign({}, regDefState)
          }
        })
        break;
      case 'edu':
        errorText = checkRequiredFields('edu', this.state.subObject.education);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let education = Object.assign([], this.props.Employee.education || []);
        if(this.state.editIndex === '')
          education.push(this.state.subObject.education);
        else
          education[editIndex] = Object.assign({}, this.state.subObject.education);
        this.props.handleChange({target:{value: education}}, "education", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            education: Object.assign({}, eduDefState)
          }
        })
        break;
      case 'tech':
        errorText = checkRequiredFields('tech', this.state.subObject.technical);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let technical = Object.assign([], this.props.Employee.technical || []);
        if(this.state.editIndex === '')
          technical.push(this.state.subObject.technical);
        else
          technical[editIndex] = Object.assign({}, this.state.subObject.technical);
        this.props.handleChange({target:{value: technical}}, "technical", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            technical: Object.assign({}, techDefState)
          }
        })
        break;
      case 'dept':
        errorText = checkRequiredFields('dept', this.state.subObject.test);
        if(Object.keys(errorText).length > 0) {
          return self.setState({errorText});
        }

        let test = Object.assign([], this.props.Employee.test || []);
        if(this.state.editIndex == '')
          test.push(this.state.subObject.test);
        else
          test[editIndex] = Object.assign({}, this.state.subObject.test);
        this.props.handleChange({target:{value: test}}, "test", false, '');
        this.setState({
          subObject: {
            ...this.state.subObject,
            test: Object.assign({}, deptDefState)
          }
        })
        break;
    }

    this.handleClose();
  }

  renderContent = () => {
    let {
      isFormValid,
        Employee,
        fieldErrors,
        handleChange,
        handleChangeNextLevel
    } = this.props;
    let self = this;
    let {subObject, modal} = self.state;

    switch (modal) {
      case 'assignment':
        return (
          <form>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <label>Is Primary? *</label>
                <RadioButtonGroup name={translate("employee.Assignment.fields.prim")} valueSelected={subObject.assignments.isPrimary} onChange={(e, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "isPrimary")
                }}>
                  <RadioButton
                    value={true}
                    label="Yes"
                  />
                  <RadioButton
                    value={false}
                    label="No"
                  />
                </RadioButtonGroup>
              </div>
              <div className="col-md-6 col-xs-12">
                <DatePicker
                  errorText = {self.state.errorText["assignments.fromDate"]}
                  formatDate={function(date) {
                    date =new Date(date);
                    return ('0' + date.getDate()).slice(-2) + '/'
                              + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                              + date.getFullYear();
                  }}
                  floatingLabelText={translate("employee.Assignment.fields.fromDate")+" *"} hintText={translate("employee.Assignment.fields.fromDate")+" *"} value={self.getDate(subObject.assignments.fromDate)} onChange={(eve, date) => {
                  self.handleStateChange({target:{value:date.getTime()}}, "assignments", "fromDate", true)
                }}/>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <DatePicker
                  errorText = {self.state.errorText["assignments.toDate"]}
                  formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                  floatingLabelText={translate("employee.Assignment.fields.toDate")+" *"} hintText={translate("employee.Assignment.fields.toDate")+" *"} value={self.getDate(subObject.assignments.toDate)} onChange={(eve, date) => {
                  self.handleStateChange({target:{value:date.getTime()}}, "assignments", "toDate", true)
                }}/>
              </div>
              <div className="col-md-6 col-xs-12">
                <SelectField
                  errorText = {self.state.errorText["assignments.department"]}
                  floatingLabelText={translate("employee.Assignment.fields.department")+" *"} value={subObject.assignments.department} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "department")
                }}>
                    {
                      self.state.departments && self.state.departments.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <SelectField
                  errorText = {self.state.errorText["assignments.designation"]}
                  floatingLabelText={translate("employee.Assignment.fields.designation")+"*"} value={subObject.assignments.designation} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "designation")
                }}>
                    {
                      self.state.designations && self.state.designations.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
              <div className="col-md-6 col-xs-12">
                {/*<TextField floatingLabelText={"Position *"} value={subObject.assignments.position} onChange={(e) => {
                                  self.handleStateChange(e, "assignments", "position")
                                }}/>*/}
                <AutoComplete
                  errorText = {self.state.errorText["assignments.position"]}
                  fullWidth={true}
                  floatingLabelText={"Position *"}
                  filter={AutoComplete.caseInsensitiveFilter}
                  dataSource={self.state.positionList}
                  dataSourceConfig={this.state.positionListConfig}
                  value={subObject.assignments.position}
                  onKeyUp={(e) => {handleChange({target: {value: ''}}, "position", true, '')}}
                  onNewRequest={(chosenRequest, index) => {
                    var e = {
                        target: {
                          value: chosenRequest.id
                        }
                    };
                    self.handleStateChange(e, "assignments", "position")
                   }}
                />
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <SelectField floatingLabelText={translate("employee.Assignment.fields.grade")} value={subObject.assignments.grade} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "grade")
                }}>
                    {
                      self.state.grades && self.state.grades.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
              <div className="col-md-6 col-xs-12">
                <SelectField floatingLabelText={translate("wc.create.groups.fields.Funtion")} value={subObject.assignments.function} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "function")
                }}>
                    {
                      self.state.functions && self.state.functions.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <SelectField floatingLabelText={translate("employee.Assignment.fields.functionary")} value={subObject.assignments.functionary} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "functionary")
                }}>
                    {
                      self.state.functionaries && self.state.functionaries.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
              <div className="col-md-6 col-xs-12">
                <SelectField floatingLabelText={translate("employee.Assignment.fields.fund")} value={subObject.assignments.fund} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "fund")
                }}>
                    {
                      self.state.funds && self.state.funds.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <label>If HOD? *</label>
                <RadioButtonGroup name={translate("employee.Assignment.fields.hod")} valueSelected={subObject.assignments.hod} onChange={(e, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "hod")
                }}>
                  <RadioButton
                    value={true}
                    label="Yes"
                  />
                  <RadioButton
                    value={false}
                    label="No"
                  />
                </RadioButtonGroup>
              </div>
              <div className="col-md-6 col-xs-12">
              {subObject.assignments.hod && <SelectField
               errorText = {self.state.errorText["assignments.mainDepartments"]}
               floatingLabelText={translate("employee.Assignment.fields.department")+" *"} multiple={true} value={subObject.assignments.mainDepartments} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value:value}}, "assignments", "mainDepartments")
                }}>
                    {
                      self.state.departments && self.state.departments.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>}
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <TextField floatingLabelText={translate("employee.Assignment.fields.govtOrderNumber")} value={subObject.assignments.govtOrderNumber} onChange={(e) => {
                  self.handleStateChange(e, "assignments", "govtOrderNumber")
                }}/>
              </div>
              <div className="col-md-6 col-xs-12">
                <label style={{marginTop:"20px"}}>Document</label>
                <input type="file"/>
              </div>
            </div>
          </form>
        )
      case 'jurisdiction':
        return (
          <form>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <SelectField
                 errorText = {self.state.errorText["jurisdictions.jurisdictionsType"]}
                 floatingLabelText={translate("employee.Employee.fields.jurisdictionsType")+"*"} value={subObject.jurisdictions.jurisdictionsType} onChange={(event, key, value) => {
                  self.loadBoundaries(value)
                  self.handleStateChange({target:{value: value}}, "jurisdictions", "jurisdictionsType")
                }}>
                    {
                      self.state.boundarytypes && self.state.boundarytypes.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
              <div className="col-md-6 col-xs-12">
                <SelectField
                  errorText = {self.state.errorText["jurisdictions.boundary"]}
                  floatingLabelText={translate("employee.Employee.fields.jurisdictionsList")+"*"} value={subObject.jurisdictions.boundary} onChange={(event, key, value) => {
                  self.handleStateChange({target:{value: value}}, "jurisdictions", "boundary")
                }}>
                    {
                      self.state.boundaries && self.state.boundaries.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                </SelectField>
              </div>
            </div>
          </form>
        )
      case 'serviceDet':
        return (
          <form>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <TextField
                  errorText = {self.state.errorText["serviceHistory.serviceInfo"]}
                  floatingLabelText={translate("employee.ServiceHistory.fields.ServiceEntryDescription")+" *"} value={subObject.serviceHistory.serviceInfo} onChange={(e) => {
                  self.handleStateChange(e, "serviceHistory", "serviceInfo")
                }}/>
              </div>
              <div className="col-md-6 col-xs-12">
                <DatePicker
                 errorText = {self.state.errorText["serviceHistory.serviceFrom"]}
                 formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                floatingLabelText={translate("employee.ServiceHistory.fields.date")+"*"} hintText={translate("employee.ServiceHistory.fields.date")+"*"} value={self.getDate(subObject.serviceHistory.serviceFrom)} onChange={(eve, date) => {
                  self.handleStateChange({target:{value:date.getTime()}}, "serviceHistory", "serviceFrom", true)
                }}/>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <TextField floatingLabelText={translate("employee.ServiceHistory.fields.remarks")} value={subObject.serviceHistory.remarks} onChange={(e) => {
                  self.handleStateChange(e, "serviceHistory", "remarks")
                }}/>
              </div>
              <div className="col-md-6 col-xs-12">
                <TextField floatingLabelText={translate("employee.ServiceHistory.fields.orderNo")} value={subObject.serviceHistory.orderNo} onChange={(e) => {
                  self.handleStateChange(e, "serviceHistory", "orderNo")
                }}/>
              </div>
            </div>
            <div className="row">
              <div className="col-md-6 col-xs-12">
                <label style={{marginTop:"20px"}}>{translate("employee.ServiceHistory.fields.documents")}</label>
                <input type="file"/>
              </div>
            </div>
          </form>
        )
      case 'probation':
          return (
            <form>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <SelectField
                    errorText = {self.state.errorText["probation.designation"]}
                    floatingLabelText={translate("employee.Assignment.fields.designation")+" *"} value={subObject.probation.designation} onChange={(event, key, value) => {
                    self.handleStateChange({target:{value: value}}, "probation", "designation")
                  }}>
                    {
                      self.state.designations && self.state.designations.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                  </SelectField>
                </div>
                <div className="col-md-6 col-xs-12">
                  <DatePicker
                  errorText = {self.state.errorText["probation.declaredOn"]}
                  formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                  floatingLabelText={translate("employee.Regularisation.fields.declaredOn")+" *"} hintText={translate("employee.Regularisation.fields.declaredOn")+" *"} value={self.getDate(subObject.probation.declaredOn)} onChange={(eve, date) => {
                    self.handleStateChange({target:{value: date.getTime()}}, "probation", "declaredOn", true)
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.ServiceHistory.fields.orderNo")}value={subObject.probation.orderNo} onChange={(e) => {
                    self.handleStateChange(e, "probation", "orderNo")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <DatePicker
                  formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                  floatingLabelText={translate("employee.Probation.fields.orderDate")+"*"} hintText={translate("employee.Probation.fields.orderDate")+"*" }value={self.getDate(subObject.probation.orderDate)} onChange={(eve, date) => {
                    self.handleStateChange({target:{value:date.getTime()}}, "probation", "orderDate", true)
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.Probation.fields.remarks")} value={subObject.probation.remarks} onChange={(e) => {
                    self.handleStateChange(e, "probation", "remarks")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <label style={{marginTop:"20px"}}>{translate("employee.Probation.fields.documents")}</label>
                  <input type="file" multiple/>
                </div>
              </div>
            </form>
          )
      case 'regular':
          return (
            <form>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <SelectField
                    errorText = {self.state.errorText["regularisation.designation"]}
                    floatingLabelText={translate("employee.Assignment.fields.designation")+" *"} value={subObject.regularisation.designation} onChange={(event, key, value) => {
                    self.handleStateChange({target:{value: value}}, "regularisation", "designation")
                  }}>
                    {
                      self.state.designations && self.state.designations.map(function(v, i){
                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                      })
                  }
                  </SelectField>
                </div>
                <div className="col-md-6 col-xs-12">
                  <DatePicker
                  errorText = {self.state.errorText["regularisation.declaredOn"]}
                  formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                  floatingLabelText={translate("employee.Regularisation.fields.declaredOn")+" *"} hintText={translate("employee.Regularisation.fields.declaredOn")+" *"} value={self.getDate(subObject.regularisation.declaredOn)} onChange={(eve, date) => {
                    self.handleStateChange({target:{value: date.getTime()}}, "regularisation", "declaredOn", true)
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.Regularisation.fields.orderNo")} value={subObject.regularisation.orderNo} onChange={(e) => {
                    self.handleStateChange(e, "regularisation", "orderNo")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <DatePicker
                  formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                  floatingLabelText={translate("employee.Regularisation.fields.orderDate")+" *"} hintText={translate("employee.Regularisation.fields.orderDate")+" *"} value={self.getDate(subObject.regularisation.orderDate)} onChange={(eve, date) => {
                    self.handleStateChange({target:{value: date.getTime()}}, "regularisation", "orderDate", true)
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.Regularisation.fields.remarks")} value={subObject.regularisation.remarks} onChange={(e) => {
                    self.handleStateChange(e, "regularisation", "remarks")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <label style={{marginTop:"20px"}}>{translate("employee.Regularisation.fields.documents")}</label>
                  <input type="file" multiple/>
                </div>
              </div>
            </form>
          )
      case 'edu':
          return (
            <form>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField
                    errorText = {self.state.errorText["education.qualification"]}
                    floatingLabelText={translate("employee.EducationalQualification.fields.qualification")+" *"} value={subObject.education.qualification} onChange={(e) => {
                    self.handleStateChange(e, "education", "qualification")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.EducationalQualification.fields.majorSubject")} value={subObject.education.majorSubject} onChange={(e) => {
                    self.handleStateChange(e, "education", "majorSubject")
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField
                    errorText = {self.state.errorText["education.yearOfPassing"]}
                    type="number" floatingLabelText={translate("employee.EducationalQualification.fields.yearOfPassing")+"*"} value={subObject.education.yearOfPassing} onChange={(e) => {
                    self.handleStateChange(e, "education", "yearOfPassing")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.EducationalQualification.fields.university")} value={subObject.education.university} onChange={(e) => {
                    self.handleStateChange(e, "education", "university")
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <label style={{marginTop:"20px"}}>{translate("employee.TechnicalQualification.fields.documents")}</label>
                  <input type="file" multiple/>
                </div>
              </div>
            </form>
          )
      case 'tech':
          return (
            <form>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField
                    errorText = {self.state.errorText["technical.skill"]}
                    floatingLabelText={translate("employee.TechnicalQualification.fields.skill")+" *"} value={subObject.technical.skill} onChange={(e) => {
                    self.handleStateChange(e, "technical", "skill")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.TechnicalQualification.fields.grade")} value={subObject.technical.grade} onChange={(e) => {
                    self.handleStateChange(e, "technical", "grade")
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField type="number" floatingLabelText={translate("employee.TechnicalQualification.fields.yearOfPassing")} value={subObject.technical.yearOfPassing} onChange={(e) => {
                    self.handleStateChange(e, "technical", "yearOfPassing")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.TechnicalQualification.fields.remarks")} value={subObject.technical.remarks} onChange={(e) => {
                    self.handleStateChange(e, "technical", "remarks")
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <label style={{marginTop:"20px"}}>{translate("employee.TechnicalQualification.fields.documents")}</label>
                  <input type="file" multiple/>
                </div>
              </div>
            </form>
          )
      case 'dept':
          return (
            <form>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField
                    errorText = {self.state.errorText["technical.skill"]}
                    floatingLabelText={translate("employee.DepartmentalTest.fields.test") +" *"} value={subObject.test.test} onChange={(e) => {
                    self.handleStateChange(e, "test", "test")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <TextField
                    errorText = {self.state.errorText["technical.skill"]}
                    type="number" floatingLabelText={translate("employee.TechnicalQualification.fields.yearOfPassing")+"*"} value={subObject.test.yearOfPassing} onChange={(e) => {
                    self.handleStateChange(e, "test", "yearOfPassing")
                  }}/>
                </div>
              </div>
              <div className="row">
                <div className="col-md-6 col-xs-12">
                  <TextField floatingLabelText={translate("employee.TechnicalQualification.fields.remarks")} value={subObject.test.remarks} onChange={(e) => {
                    self.handleStateChange(e, "test", "remarks")
                  }}/>
                </div>
                <div className="col-md-6 col-xs-12">
                  <label style={{marginTop:"20px"}}>{translate("employee.TechnicalQualification.fields.documents")}</label>
                  <input type="file" multiple/>
                </div>
              </div>
            </form>
          )
    }
  }

  getModalTitle = () => {
    switch(this.state.modal) {
      case 'assignment':
        return "Assignments Grade";
      case 'jurisdiction':
        return "Jurisdiction";
      case 'serviceDet':
        return "Service History";
      case 'probation':
        return "Probation";
      case 'regular':
        return "Regularisation";
      case 'edu':
        return "Educational Qualification";
      case 'tech':
        return "Technical Qualification";
      case 'dept':
        return 'Departmental Test';
    }
  }

  setModalOpen = (type) => {
    this.setState({
      open: true,
      modal: type,
      boundaries: [],
      editIndex: '',
      errorText: {},
      subObject: {
        assignments: Object.assign({}, assignmentsDefState),
        jurisdictions: Object.assign({}, jurisDefState),
        serviceHistory: Object.assign({}, serviceDefState),
        probation: Object.assign({}, probDefState),
        regularisation: Object.assign({}, regDefState),
        education: Object.assign({}, eduDefState),
        technical: Object.assign({}, techDefState),
        test: Object.assign({}, deptDefState)
      }
    })
  }
	componentDidMount() {
    	let self = this;
        self.setState({
          screenType: window.location.hash.indexOf("update") > -1 ? "update" : (window.location.hash.indexOf("view") > -1 ? "view" : "create")
        }, function() {
          if(self.state.screenType == "update" || self.state.screenType == "view") {
            Api.commonApiPost("/hr-employee/employees/" + self.props.match.params.id + "/_search", {}).then(function(res) {
              for(var i=0; i<res.Employee.assignments.length; i++) {
                res.Employee.assignments[i].fromServer = true;
              }
              self.props.setForm(res.Employee, true);
            }, function(err) {

            })
          } else {
            self.props.setForm({
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
              medicalReportProduced: false,
              languagesKnown: [],
              maritalStatus: "",
              passportNo: null,
              gpfNo: null,
              bank: "",
              bankBranch: "",
              bankAccount: "",
              group: "",
              placeOfBirth: "",
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
                      tenantId: localStorage.getItem('tenantId')
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
                  tenantId: localStorage.getItem('tenantId')
              },
              tenantId: localStorage.getItem('tenantId')
            });
          }
        });

   		let count = 23, _state = {};
   		let checkCountAndSetState = function(key, res) {
   			_state[key] = res;
   			count--;
   			if(count == 0) {
   				self.setInitialState(_state);
          self.props.setLoadingStatus('hide');
        }
   		}

      self.props.setLoadingStatus('loading');
   		self.fetchURLData("/hr-masters/employeetypes/_search", {}, [], function(res){
   			checkCountAndSetState("employeetypes", res["EmployeeType"]);
   		});
        self.fetchURLData("hr-masters/positions/_search", {}, [], function(res){
            checkCountAndSetState("allPosition", res["Position"]);
        });
   		self.fetchURLData("/hr-masters/hrstatuses/_search", { objectName:"Employee Master" }, [], function(res){
   			checkCountAndSetState("statuses", res["HRStatus"]);
   		});
   		self.fetchURLData("/hr-masters/groups/_search", {}, [], function(res){
   			checkCountAndSetState("groups", res["Group"]);
   		});
   		self.fetchURLData("/egf-masters/banks/_search", {}, [], function(res){
   			checkCountAndSetState("banks", res["banks"]);
   		});
   		self.fetchURLData("/egov-common-masters/categories/_search", {}, [], function(res){
   			checkCountAndSetState("categories", res["Category"]);
   		});
   		self.fetchURLData("/hr-employee/maritalstatuses/_search", {}, [], function(res){
   			checkCountAndSetState("maritalstatuses", res["MaritalStatus"]);
   		});
   		self.fetchURLData("/hr-employee/bloodgroups/_search", {}, [], function(res){
   			checkCountAndSetState("bloodgroups", res["BloodGroup"]);
   		});
   		self.fetchURLData("/egov-common-masters/languages/_search", {}, [], function(res){
   			checkCountAndSetState("languages", res["Language"]);
   		});
   		self.fetchURLData("/egov-common-masters/religions/_search", {}, [], function(res){
   			checkCountAndSetState("religions", res["Religion"]);
   		});
   		self.fetchURLData("/hr-masters/recruitmentmodes/_search", {}, [], function(res){
   			checkCountAndSetState("recruitmentmodes", res["RecruitmentMode"]);
   		});
   		self.fetchURLData("/hr-masters/recruitmenttypes/_search", {}, [], function(res){
   			checkCountAndSetState("recruitmenttypes", res["RecruitmentType"]);
   		});
   		self.fetchURLData("/hr-masters/grades/_search", {}, [], function(res){
   			checkCountAndSetState("grades", res["Grade"]);
   		});
   		self.fetchURLData("/egf-masters/funds/_search", {}, [], function(res){
   			checkCountAndSetState("funds", res["funds"]);
   		});
   		self.fetchURLData("/egf-masters/functionaries/_search", {}, [], function(res){
   			checkCountAndSetState("functionaries", res["functionaries"]);
   		});
   		self.fetchURLData("/egf-masters/functions/_search", {}, [], function(res){
   			checkCountAndSetState("functions", res["functions"]);
   		});
   		self.fetchURLData("/egov-location/boundarytypes/getByHierarchyType", {hierarchyTypeName: "ADMINISTRATION"}, [], function(res){
   			checkCountAndSetState("boundarytypes", res["BoundaryType"]);
   		});
   		self.fetchURLData("hr-masters/designations/_search", {}, [], function(res){
   			checkCountAndSetState("designations", res["Designation"]);
   		});
   		self.fetchURLData("egov-common-masters/departments/_search", {}, [], function(res){
   			checkCountAndSetState("departments", res["Department"]);
   		});
   		self.fetchURLData("hr-masters/recruitmentquotas/_search", {}, [], function(res){
   			checkCountAndSetState("recruitmentquotas", res["RecruitmentQuota"]);
   		});
   		self.fetchURLData("egov-common-masters/genders/_search", {}, [], function(res){
   			checkCountAndSetState("genders", res["Gender"]);
   		});
   		self.fetchURLData("egov-common-masters/communities/_search", {}, [], function(res){
   			checkCountAndSetState("communities", res["Community"]);
   		});

        Api.commonApiGet("egov-location/boundarys", {
            "Boundary.tenantId": localStorage.getItem("tenantId")
        }).then(function(res) {
            checkCountAndSetState("allBoundariesList", res["Boundary"]);
        }, function(err) {
            checkCountAndSetState("allBoundariesList", []);
        })
	}

	handleDateChange = (type, date) => {
    let self = this;
    let _date = new Date(date);
    let name;
    switch(type) {
      case 'dob':
        name = "user.dob";
        break;
      case 'appointmentDate':
        name = "dateOfAppointment";
        if(self.props.Employee.dateOfRetirement) {
          var dateParts1 = self.props.Employee.dateOfRetirement.split("/");
          var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
          var date1 = new Date(newDateStr).getTime();
          if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Retirement Date.", false, true);
        }

        if(self.props.Employee.dateOfTermination) {
          var dateParts1 = self.props.Employee.dateOfTermination.split("/");
          var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
          var date1 = new Date(newDateStr).getTime();
          if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Termination Date.", false, true);
        }

        if(self.props.Employee.dateOfResignation) {
          var dateParts1 = self.props.Employee.dateOfResignation.split("/");
          var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
          var date1 = new Date(newDateStr).getTime();
          if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Resignation Date.", false, true);
        }

        if(self.props.Employee.dateOfJoining) {
           var dateParts1 = self.props.Employee.dateOfResignation.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be after Appointment Date.", false, true);
        }
        break;
      case 'joiningDate':
        name = "dateOfJoining";
        if(self.props.Employee.dateOfAppointment) {
           var dateParts1 = self.props.Employee.dateOfAppointment.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be after Appointment Date.", false, true);
        }

        if(self.props.Employee.dateOfRetirement) {
           var dateParts1 = self.props.Employee.dateOfRetirement.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Retirement Date.", false, true);
        }

        if(self.props.Employee.dateOfTermination) {
           var dateParts1 = self.props.Employee.dateOfTermination.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Termination Date.", false, true);
        }

        if(self.props.Employee.dateOfResignation) {
           var dateParts1 = self.props.Employee.dateOfTermination.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date > date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Resignation Date.", false, true);
        }
        break;
      case 'retirementDate':
        name = "dateOfRetirement";
        if(self.props.Employee.dateOfAppointment) {
           var dateParts1 = self.props.Employee.dateOfAppointment.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Retirement Date.", false, true);
        }
        if(self.props.Employee.dateOfJoining) {
           var dateParts1 = self.props.Employee.dateOfAppointment.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Retirement Date.", false, true);
        }
        break;
      case 'terminationDate':
        name = "dateOfTermination";
        if(self.props.Employee.dateOfAppointment) {
           var dateParts1 = self.props.Employee.dateOfAppointment.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Termination Date.", false, true);
        }

        if(self.props.Employee.dateOfJoining) {
           var dateParts1 = self.props.Employee.dateOfJoining.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Termination Date.", false, true);
        }
        break;
      case 'resignationDate':
        name = "dateOfResignation";
        if(self.props.Employee.dateOfAppointment) {
           var dateParts1 = self.props.Employee.dateOfAppointment.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Appointment Date must be before Resignation Date.", false, true);
        }

        if(self.props.Employee.dateOfJoining) {
          var dateParts1 = self.props.Employee.dateOfJoining.split("/");
           var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
           var date1 = new Date(newDateStr).getTime();
           if(date < date1)
            return self.props.toggleSnackbarAndSetText(true, "Joining Date must be before Resignation Date.", false, true);
        }
        break;
    }

    if(name.indexOf(".") == -1){
      self.props.handleChange({target: {value: ('0' + _date.getDate()).slice(-2) + '/' + ('0' + (_date.getMonth()+1)).slice(-2) + '/' + _date.getFullYear()}}, name, true, '');
    }
	  else {
      var _split = name.split(".");
      self.props.handleChangeNextLevel({target: {value: ('0' + _date.getDate()).slice(-2) + '/' + ('0' + (_date.getMonth()+1)).slice(-2) + '/' + _date.getFullYear()}}, _split[0], _split[1], true, '');
    }
  }

	renderEmployee() {
		let {
		  isFormValid,
	      Employee,
	      fieldErrors,
	      handleChange,
	      handleChangeNextLevel
		} = this.props;
		let {handleDateChange} = this;
		let self = this;
		return (
			<Card className="uiCard">
              <CardText>
              	<Grid>
                  	<Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                        {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.name")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.name : ""}</label></span>
                            )
                         :
                            <TextField floatingLabelText={translate("employee.Employee.fields.User.name")+ "*"} errorText={fieldErrors["user"] && fieldErrors["user"]["name"]} value={Employee.user ? Employee.user.name : ""} onChange={(e) => {
                                handleChangeNextLevel(e, "user", "name", true, '')
                            }}/>
                        }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      	{self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.code")}</span></label><br/>
                                <label>{Employee.code}</label></span>
                            )
                         :
                            <TextField floatingLabelText={translate("employee.Employee.fields.code")+ "*"} errorText={fieldErrors["code"]} value={Employee.code} onChange={(e) => {
                      		    handleChange(e, "code", true, '')
                      	    }} disabled={self.state.screenType == 'update'}/>
                        }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                        {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.employeeType")}</span></label><br/>
                                <label>{Employee.employeeType}</label></span>
                            )
                         :

                          	<SelectField floatingLabelText={translate("employee.Employee.fields.employeeType") + " *"} errorText={fieldErrors["employeeType"]} value={Employee.employeeType} onChange={(event, key, value) => {
                          		handleChange({target:{value:value}}, "employeeType", true, '')
                          	}}>
                                {
                                	self.state.employeetypes && self.state.employeetypes.map(function(v, i){
                                		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                                	})
                            	}
                            </SelectField>
                        }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                        {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.employeeStatus")}</span></label><br/>
                                <label>{Employee.employeeStatus}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.employeeStatus")+" *"} errorText={fieldErrors["employeeStatus"]} value={Employee.employeeStatus} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, "employeeStatus", true, '')
                      	}}>
                            {
                            	self.state.statuses && self.state.statuses.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.code}/>)
                            	})
                        	}
                        </SelectField>
                        }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                        {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>Employee Group</span></label><br/>
                                <label>{Employee.group}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={"Employee Group"} errorText={fieldErrors["group"]} value={Employee.group} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, "group", true, '')
                      	}}>
                            {
                            	self.state.groups && self.state.groups.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                        }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                        {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>Date Of Birth</span></label><br/>
                                <label>{Employee.user ? Employee.user.dob : ""}</label></span>
                            )
                         :

                      	<DatePicker
                          formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }} floatingLabelText="Date Of Birth *" hintText="Date Of Birth *" value={Employee.user ? self.getDate(Employee.user.dob) : ""} onChange={(eve, date) => {
                      		handleDateChange('dob', date)
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      	{self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.gender")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.gender : ""}</label></span>
                            )
                         :
                            <SelectField floatingLabelText={translate("employee.Employee.fields.User.gender") +"*"} errorText={fieldErrors["user"] && fieldErrors["user"]["gender"]} value={Employee.user ? Employee.user.gender : ""} onChange={(event, key, value) => {
                      		handleChangeNextLevel({target:{value:value}}, "user", "gender", true, '')
                      	}}>
                            {
                            	self.state.genders && self.state.genders.map(function(v, i){
                            		return (<MenuItem value={v} key={i} primaryText={v}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      	{self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.maritalStatus")}</span></label><br/>
                                <label>{Employee.maritalStatus}</label></span>
                            )
                         :
                            <SelectField floatingLabelText={translate("employee.Employee.fields.maritalStatus") +"*"} errorText={fieldErrors["maritalStatus"]} value={Employee.maritalStatus} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, "maritalStatus", true, '')
                      	}}>
                            {
                            	self.state.maritalstatuses && self.state.maritalstatuses.map(function(v, i){
                            		return (<MenuItem value={v} key={i} primaryText={v}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      	{self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.userName")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.userName : ""}</label></span>
                            )
                         :

                        <TextField floatingLabelText={translate("employee.Employee.fields.User.userName")+" *"} errorText={fieldErrors["user"] && fieldErrors["user"]["userName"]} errorText={fieldErrors["user.userName"]} value={Employee.user ? Employee.user.userName : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, "user", "userName", true, '')
                      	}} disabled={self.state.screenType == 'update'}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>Is User Active?</span></label><br/>
                                <label>{Employee.user ? Employee.user.active : ''}</label></span>
                            )
                         :

                        <span><label>Is User Active? *</label>
                      	<RadioButtonGroup name="isActive" valueSelected={Employee.user ? Employee.user.active : ''} onChange={(e, value) => {
                      		handleChangeNextLevel({target:{value:value}}, 'user', 'active', true, '')
                      	}}>
          					      <RadioButton
          					        value={true}
          					        label="Yes"
          					      />
          					      <RadioButton
          					        value={false}
          					        label="No"
          					      />
                				</RadioButtonGroup></span>
                            }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.mobileNumber")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.mobileNumber : ""}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.User.mobileNumber")+" *"}  errorText={fieldErrors["user"] && fieldErrors["user"]["mobileNumber"]} value={Employee.user ? Employee.user.mobileNumber : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'mobileNumber', true, /^\d{10}$/)
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.email")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.emailId : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.email")} errorText={fieldErrors["user"] && fieldErrors["user"]["emailId"]} type="email" value={Employee.user ? Employee.user.emailId : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'emailId', false, '')
                      	}}/>
                      }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.fatherSpouseName")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.fatherOrHusbandName : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.fatherSpouseName")} errorText={fieldErrors["user"] && fieldErrors["user"]["fatherOrHusbandName"]} value={Employee.user ? Employee.user.fatherOrHusbandName : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'fatherOrHusbandName', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.birth")}</span></label><br/>
                                <label>{Employee.placeOfBirth}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.User.birth")} errorText={fieldErrors["placeOfBirth"]} value={Employee.placeOfBirth} onChange={(e) => {
                      		handleChange(e, 'placeOfBirth', true, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.bloodGroup")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.bloodGroup : ""}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.User.bloodGroup")} errorText={fieldErrors["user"] && fieldErrors["user"]["bloodGroup"]} value={Employee.user ? Employee.user.bloodGroup : ""} onChange={(event, key, value) => {
                      		handleChangeNextLevel({target:{value:value}}, "user", "bloodGroup", false, '')
                      	}}>
                            {
                            	self.state.bloodgroups && self.state.bloodgroups.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.motherTongue")}</span></label><br/>
                                <label>{Employee.motherTongue}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.motherTongue")} errorText={fieldErrors["motherTongue"]} value={Employee.motherTongue} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'motherTongue', false, '')
                      	}}>
                            {
                            	self.state.languages && self.state.languages.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.religion")}</span></label><br/>
                                <label>{Employee.religion}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.religion")} errorText={fieldErrors["religion"]} value={Employee.religion} onChange={(event, key, value) => {
							handleChange({target:{value:value}}, 'religion', false, '')
                      	}}>
                            {
                            	self.state.religions && self.state.religions.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.community")}</span></label><br/>
                                <label>{Employee.community}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.community")} errorText={fieldErrors["community"]} value={Employee.community} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'community', false, '')
                      	}}>
                            {
                            	self.state.communities && self.state.communities.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.category")}</span></label><br/>
                                <label>{Employee.category}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.category")} errorText={fieldErrors["category"]} value={Employee.category} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'category', false, '')
                      	}}>
                            {
                            	self.state.categories && self.state.categories.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.physicallyDisabled")}</span></label><br/>
                                <label>{Employee.physicallyDisabled ? "Yes" : "No"}</label></span>
                            )
                         :

                        <span><label>Is Physically Handicapped?</label>
                      	<RadioButtonGroup name={translate("employee.Employee.fields.physicallyDisabled")} valueSelected={Employee.physicallyDisabled} onChange={(e, value) => {
                      		handleChange({target:{value:value}}, 'physicallyDisabled', false, '')
                      	}}>
          					      <RadioButton
          					        value={true}
          					        label="Yes"
          					      />
          					      <RadioButton
          					        value={false}
          					        label="No"
          					      />
                				</RadioButtonGroup></span>
                            }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.medicalReportProduced")}</span></label><br/>
                                <label>{Employee.medicalReportProduced ? "Yes" : "No"}</label></span>
                            )
                         :

                        <span><label>Is Medical Report Available?</label>
                      	<RadioButtonGroup name={translate("employee.Employee.fields.medicalReportProduced")} valueSelected={Employee.medicalReportProduced} onChange={(e, value) => {
                      		handleChange({target:{value:value}}, 'medicalReportProduced', true, '')
                      	}}>
          					      <RadioButton
          					        value={true}
          					        label="Yes"
          					      />
          					      <RadioButton
          					        value={false}
          					        label="No"
          					      />
              					</RadioButtonGroup></span>
                        }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.identification")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.identificationMark : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.identification")} errorText={fieldErrors["user"] && fieldErrors["user"]["identificationMark"]} value={Employee.user ? Employee.user.identificationMark : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'identificationMark', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.pan")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.pan : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.pan")} errorText={fieldErrors["user"] && fieldErrors["user"]["pan"]} value={Employee.user ? Employee.user.pan : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'pan', false, /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/)
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.passportNo")}</span></label><br/>
                                <label>{Employee.passportNo}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.passportNo")} errorText={fieldErrors["passportNo"]} value={Employee.passportNo} onChange={(e) => {
                      		handleChange(e, 'passportNo', false, '')
                      	}}/>
                      }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.gpfNo")}</span></label><br/>
                                <label>{Employee.gpfNo}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.gpfNo")} errorText={fieldErrors["gpfNo"]} value={Employee.gpfNo} onChange={(e) => {
                      		handleChange(e, 'gpfNo', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.aadhaarNumber")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.aadhaarNumber : ""}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.User.aadhaarNumber")} errorText={fieldErrors["user"] && fieldErrors["user"]["aadhaarNumber"]} value={Employee.user ? Employee.user.aadhaarNumber : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'aadhaarNumber', false, /^\d{12}$/)
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.bank")}</span></label><br/>
                                <label>{Employee.bank}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.bank")} errorText={fieldErrors["bank"]} value={Employee.bank} onChange={(event, key, value) => {
                      		self.loadBranches(value)
                          handleChange({target:{value:value}}, 'bank', false, '')
                      	}}>
                            {
                            	self.state.banks && self.state.banks.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.bankBranch")}</span></label><br/>
                                <label>{Employee.bankBranch}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.bankBranch")} errorText={fieldErrors["bankBranch"]} value={Employee.bankBranch} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'bankBranch', false, '')
                      	}}>
                            {
                            	self.state.bankBranches && self.state.bankBranches.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.bankAccount")}</span></label><br/>
                                <label>{Employee.bankAccount}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.bankAccount")} errorText={fieldErrors["bankAccount"]} value={Employee.bankAccount} onChange={(e) => {
                      		handleChange(e, 'bankAccount', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.User.mobileNumber")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.altContactNumber : ""}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.User.mobileNumber")} errorText={fieldErrors["user"] && fieldErrors["user"]["altContactNumber"]} value={Employee.user ? Employee.user.altContactNumber : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'altContactNumber', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}></Col>
                      <Col xs={12} sm={4} md={3} lg={3}></Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.permanentAddress")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.permanentAddress : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.permanentAddress")} errorText={fieldErrors["user"] && fieldErrors["user"]["permanentAddress"]} value={Employee.user ? Employee.user.permanentAddress : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'permanentAddress', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.city")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.permanentCity : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.city")} errorText={fieldErrors["user"] && fieldErrors["user"]["permanentCity"]} value={Employee.user ? Employee.user.permanentCity : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'permanentCity', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.parmanentPinNumber")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.permanentPinCode : ""}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.parmanentPinNumber")} errorText={fieldErrors["user"] && fieldErrors["user"]["permanentPinCode"]} value={Employee.user ? Employee.user.permanentPinCode : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'permanentPinCode', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}></Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.correspondenceAddress")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.correspondenceAddress : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.correspondenceAddress")} errorText={fieldErrors["user"] && fieldErrors["user"]["correspondenceAddress"]} value={Employee.user ? Employee.user.correspondenceAddress : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'correspondenceAddress', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.city")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.correspondenceCity : ""}</label></span>
                            )
                         :

                      	<TextField floatingLabelText={translate("employee.Employee.fields.city")} errorText={fieldErrors["user.correspondenceCity"]} value={Employee.user ? Employee.user.correspondenceCity : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'correspondenceCity', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.correspondencePinNumber")}</span></label><br/>
                                <label>{Employee.user ? Employee.user.correspondencePinCode : ""}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.correspondencePinNumber")} errorText={fieldErrors["user"] && fieldErrors["user"]["correspondencePinCode"]} value={Employee.user ? Employee.user.correspondencePinCode : ""} onChange={(e) => {
                      		handleChangeNextLevel(e, 'user', 'correspondencePinCode', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}></Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.languagesKnown")}</span></label><br/>
                                <label>{Employee.languagesKnown}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.languagesKnown")} errorText={fieldErrors["languagesKnown"]} multiple={true} value={Employee.languagesKnown} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'languagesKnown', false, '')
                      	}}>
                            {
                            	self.state.languages && self.state.languages.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.recruitmentMode")}</span></label><br/>
                                <label>{Employee.recruitmentMode}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.recruitmentMode")} errorText={fieldErrors["recruitmentMode"]} value={Employee.recruitmentMode} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'recruitmentMode', false, '')
                      	}}>
                            {
                            	self.state.recruitmentmodes && self.state.recruitmentmodes.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.recruitmentType")}</span></label><br/>
                                <label>{Employee.recruitmentType}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.recruitmentType")} errorText={fieldErrors["recruitmentType"]} value={Employee.recruitmentType} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'recruitmentType', false, '')
                      	}}>
                            {
                            	self.state.recruitmenttypes && self.state.recruitmenttypes.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.recruitmentQuota")}</span></label><br/>
                                <label>{Employee.recruitmentQuota}</label></span>
                            )
                         :

                      	<SelectField floatingLabelText={translate("employee.Employee.fields.recruitmentQuota")} errorText={fieldErrors["recruitmentQuota"]} value={Employee.recruitmentQuota} onChange={(event, key, value) => {
                      		handleChange({target:{value:value}}, 'recruitmentQuota', false, '')
                      	}}>
                            {
                            	self.state.recruitmentquotas && self.state.recruitmentquotas.map(function(v, i){
                            		return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
                            	})
                        	}
                        </SelectField>
                    }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.dateOfAppointment")}</span></label><br/>
                                <label>{Employee.dateOfAppointment}</label></span>
                            )
                         :

                      	<DatePicker
                         formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }} floatingLabelText={translate("employee.Employee.fields.dateOfAppointment")+ "*"} hintText={translate("employee.Employee.fields.dateOfAppointment")+ "*"} errorText={fieldErrors["dateOfAppointment"]} value={self.getDate(Employee.dateOfAppointment)} onChange={(eve, date) => {
                      		handleDateChange('appointmentDate', date.getTime())
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.dateOfJoining")}</span></label><br/>
                                <label>{Employee.dateOfJoining}</label></span>
                            )
                         :

                      	<DatePicker
                          formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                          floatingLabelText={translate("employee.Employee.fields.dateOfJoining")} hintText={translate("employee.Employee.fields.dateOfJoining")} errorText={fieldErrors["dateOfJoining"]} value={self.getDate(Employee.dateOfJoining)} onChange={(eve, date) => {
                      		handleDateChange('joiningDate', date.getTime())
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.retirementAge")}</span></label><br/>
                                <label>{Employee.retirementAge}</label></span>
                            )
                         :

                      	<TextField type="number" floatingLabelText={translate("employee.Employee.fields.retirementAge")} errorText={fieldErrors["retirementAge"]} value={Employee.retirementAge} onChange={(e) => {
                      		handleChange(e, 'retirementAge', false, '')
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.dateOfRetirement")}</span></label><br/>
                                <label>{Employee.dateOfRetirement}</label></span>
                            )
                         :

                      	<DatePicker
                         formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                         }}
                         floatingLabelText={translate("employee.Employee.fields.dateOfRetirement")} hintText={translate("employee.Employee.fields.dateOfRetirement")} errorText={fieldErrors["dateOfRetirement"]} value={self.getDate(Employee.dateOfRetirement)} onChange={(eve, date) => {
                      		handleDateChange('retirementDate', date.getTime())
                      	}}/>
                      }
                      </Col>
                   </Row>
                   <Row>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.dateOfTermination")}</span></label><br/>
                                <label>{Employee.dateOfTermination}</label></span>
                            )
                         :

                      	<DatePicker
                          formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                          floatingLabelText={translate("employee.Employee.fields.dateOfTermination")} hintText={translate("employee.Employee.fields.dateOfTermination")} errorText={fieldErrors["dateOfTermination"]} value={self.getDate(Employee.dateOfTermination)} onChange={(eve, date) => {
                      		handleDateChange('terminationDate', date.getTime())
                      	}}/>
                      }
                      </Col>
                      <Col xs={12} sm={4} md={3} lg={3}>
                      {self.state.screenType == "view" ?
                            (
                                <span><label><span style={{"fontWeight":"bold"}}>{translate("employee.Employee.fields.dateOfResignation")}</span></label><br/>
                                <label>{Employee.dateOfResignation}</label></span>
                            )
                         :

                      	<DatePicker
                          formatDate={function(date) {
                            date =new Date(date);
                            return ('0' + date.getDate()).slice(-2) + '/'
                                      + ('0' + (date.getMonth()+1)).slice(-2) + '/'
                                      + date.getFullYear();
                          }}
                          floatingLabelText={translate("employee.Employee.fields.dateOfResignation")} hintText={translate("employee.Employee.fields.dateOfResignation")} errorText={fieldErrors["dateOfResignation"]} value={self.getDate(Employee.dateOfResignation)} onChange={(eve, date) => {
                      		handleDateChange('resignationDate', date.getTime())
                      	}}/>
                      }
                      </Col>
                      {self.state.screenType != "view" ? <Col xs={12} sm={4} md={3} lg={3}>
                        <label style={{marginTop:"20px"}}>{translate("employee.Employee.fields.EmployeePhoto")}</label>
                      	<input type="file" />
                      </Col> : ""}
                      {self.state.screenType != "view" ? <Col xs={12} sm={4} md={3} lg={3}>
                        <label style={{marginTop:"20px"}}>{translate("employee.Employee.fields.EmployeeSignature")}</label>
                      	<input type="file"/>
                      </Col> : ""}
                   </Row>
                   <Row>
                   	  {self.state.screenType != "view" ? <Col xs={12} sm={4} md={3} lg={3}>
                        <label style={{marginTop:"20px"}}>{translate("employee.Employee.fields.OtherAttachments")}</label>
                      	<input type="file" multiple/>
                      </Col> : ""}
                   </Row>
                </Grid>
              </CardText>
            </Card>
		);
	}

	renderAssignment() {
		let {
		  isFormValid,
	      Employee,
	      fieldErrors,
	      handleChange,
	      handleChangeNextLevel
		} = this.props;
    let self = this;
		const renderAssignmentBody = function() {
      return self.props.Employee.assignments && self.props.Employee.assignments.length ? self.props.Employee.assignments.map(function(val, i) {
        return (
          <tr key={i}>
              <td>{val.fromDate}</td>
              <td>{val.toDate}</td>
              <td>{getNameById(self.state.departments, val.department)}</td>
              <td>{getNameById(self.state.designations, val.designation)}</td>
              <td>{getNameById(self.state.allPosition, val.position)}</td>
              <td>{val.isPrimary ? "Yes" : "No"}</td>
              <td>{getNameById(self.state.funds, val.fund)}</td>
              <td>{getNameById(self.state.functions, val.function)}</td>
              <td>{getNameById(self.state.functionaries, val.functionary)}</td>
              <td>{getNameById(self.state.grades, val.grade)}</td>
              <td><ol>{val.hod && val.hod.length ? val.hod.map(function(v, i) {
                return (
                    <li>{getNameById(self.state.departments, v)}</li>
                )
              }) : ""}</ol></td>
              <td>{val.govtOrderNumber}</td>
              <td>{val.documents && val.documents.length}</td>
              <td>
                {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'assignments')}}></span>}&nbsp;&nbsp;
                {(self.state.screenType != "view")
                    && !val.fromServer
                    && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'assignments')}}></span>}
              </td>
          </tr>
        )
      }) : '';
    }
		return (
			<Card className="uiCard">
				<CardText>
					<Table bordered responsive className="table-striped">
						<thead>
							<th>{translate("employee.Assignment.fields.fromDate")}</th>
							<th>{translate("employee.Assignment.fields.toDate")}</th>
							<th>{translate("employee.Assignment.fields.department")}</th>
							<th>{translate("employee.Assignment.fields.designation")}</th>
							<th>{translate("employee.Assignment.fields.position")}</th>
							<th>{translate("employee.Assignment.fields.primary")}</th>
							<th>{translate("employee.Assignment.fields.fund")}</th>
							<th>{translate("employee.Assignment.fields.function")}</th>
							<th>{translate("employee.Assignment.fields.functionary")}</th>
							<th>{translate("employee.Assignment.fields.grade")}</th>
							<th>{translate("employee.Assignment.fields.hod")}</th>
							<th>{translate("employee.Assignment.fields.govtOrderNumber")}</th>
							<th>{translate("employee.Assignment.fields.documents")}</th>
							<th>{translate("employee.Assignment.fields.action")}</th>
						</thead>
						<tbody>
							{renderAssignmentBody()}
						</tbody>
					</Table>
					<Row>
            <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
              <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                self.setModalOpen('assignment')
              }}>
                <span className="glyphicon glyphicon-plus"></span>
              </FloatingActionButton>
            </Col>
          </Row>
				</CardText>
			</Card>
		)
	}

	renderJurisdiction() {
		let {
		  isFormValid,
	      Employee,
	      fieldErrors,
	      handleChange,
	      handleChangeNextLevel
		} = this.props;
        let self = this;
		const renderJurisdictionBody = function() {
          return self.props.Employee.jurisdictions &&  self.props.Employee.jurisdictions.length ? self.props.Employee.jurisdictions.map(function(val, i) {
            return getBoundaryValues(self.state.allBoundariesList, val, self, i);
          }) : ''
        }
		return (
			<Card className="uiCard">
				<CardText>
					<Table bordered responsive className="table-striped">
						<thead>
							<th>{translate("employee.jurisdiction.fields.boundaryType")}</th>
							<th>{translate("employee.jurisdiction.fields.boundary")}</th>
							<th>Action</th>
						</thead>
						<tbody>
							{renderJurisdictionBody()}
						</tbody>
					</Table>
					<Row>
            <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
              <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                self.setModalOpen('jurisdiction')
              }}>
                <span className="glyphicon glyphicon-plus"></span>
              </FloatingActionButton>
            </Col>
          </Row>
				</CardText>
			</Card>
		)
	}

	renderService() {
		let {
		  isFormValid,
	      Employee,
	      fieldErrors,
	      handleChange,
	      handleChangeNextLevel
		} = this.props;
    let self = this;
		const renderServiceBody = function (type) {
			switch(type) {
				case 'service':
          return self.props.Employee.serviceHistory &&  self.props.Employee.serviceHistory.length ? self.props.Employee.serviceHistory.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{val.serviceInfo}</td>
                  <td>{val.serviceFrom}</td>
                  <td>{val.remarks}</td>
                  <td>{val.orderNo}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'serviceDet')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'serviceDet')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
				case 'probation':
          return self.props.Employee.probation &&  self.props.Employee.probation.length ? self.props.Employee.probation.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{getNameById(self.state.designations, val.designation)}</td>
                  <td>{val.declaredOn}</td>
                  <td>{val.orderNo}</td>
                  <td>{val.orderDate}</td>
                  <td>{val.remarks}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'probation')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'probation')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
				case 'regularization':
          return self.props.Employee.regularisation &&  self.props.Employee.regularisation.length ? self.props.Employee.regularisation.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{val.designation}</td>
                  <td>{val.declaredOn}</td>
                  <td>{val.orderNo}</td>
                  <td>{val.orderDate}</td>
                  <td>{val.remarks}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'regular')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'regular')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
			}
		}

		return (
			<div>
				<Card className="uiCard">
					<CardTitle title={translate("employee.ServiceHistory.title")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
								<th>{translate("employee.ServiceHistory.fields.ServiceEntryDescription")}</th>
								<th>{translate("employee.ServiceHistory.fields.date")}</th>
								<th>{translate("employee.ServiceHistory.fields.remarks")}</th>
								<th>{translate("employee.ServiceHistory.fields.orderNo")}</th>
								<th>{translate("employee.ServiceHistory.fields.documents")}</th>
								<th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderServiceBody('service')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('serviceDet')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
				<Card className="uiCard">
					<CardTitle title={translate("employee.Employee.fields.probation")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
								<th>{translate("employee.Assignment.fields.designation")}</th>
								<th>{translate("employee.Probation.fields.declaredOn")}</th>
								<th>{translate("employee.Probation.fields.orderNo")}</th>
								<th>{translate("employee.Probation.fields.orderDate")}</th>
								<th>{translate("employee.Probation.fields.remarks")}</th>
								<th>{translate("employee.Probation.fields.documents")}</th>
								<th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderServiceBody('probation')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('probation')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
				<Card className="uiCard">
					<CardTitle title={translate("employee.Employee.fields.regularisation")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
              <th>{translate("employee.Assignment.fields.designation")}</th>
              <th>{translate("employee.Regularisation.fields.declaredOn")}</th>
              <th>{translate("employee.Probation.fields.orderNo")}</th>
              <th>{translate("employee.Probation.fields.orderDate")}</th>
              <th>{translate("employee.Probation.fields.remarks")}</th>
              <th>{translate("employee.Probation.fields.documents")}</th>
              <th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderServiceBody('regularization')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('regular')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
			</div>
		)
	}

	renderOtherDetails() {
		let {
		    isFormValid,
	      Employee,
	      fieldErrors,
	      handleChange,
	      handleChangeNextLevel
		} = this.props;
    let self = this;
		const renderOtherDetailsBody = function(type) {
			switch(type) {
				case 'edu':
          return self.props.Employee.education &&  self.props.Employee.education.length ? self.props.Employee.education.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{val.qualification}</td>
                  <td>{val.majorSubject}</td>
                  <td>{val.yearOfPassing}</td>
                  <td>{val.university}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'edu')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'edu')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
				case 'tech':
          return self.props.Employee.technical &&  self.props.Employee.technical.length ? self.props.Employee.technical.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{val.skill}</td>
                  <td>{val.grade}</td>
                  <td>{val.yearOfPassing}</td>
                  <td>{val.remarks}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'tech')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'tech')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
				case 'dept':
          return self.props.Employee.test &&  self.props.Employee.test.length ? self.props.Employee.test.map(function(val, i) {
            return (
              <tr key={i}>
                  <td>{val.serviceInfo}</td>
                  <td>{val.serviceFrom}</td>
                  <td>{val.remarks}</td>
                  <td>{val.orderNo}</td>
                  <td>{val.documents && val.documents.length}</td>
                  <td>
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-pencil" onClick={() => { self.editModalOpen(i, 'dept')}}></span>}&nbsp;&nbsp;
                    {self.state.screenType != "view" && <span className="glyphicon glyphicon-trash"  onClick={() => { self.delModalOpen(i, 'dept')}}></span>}
                  </td>
              </tr>
            )
          }) : ''
					break;
			}
		}

		return (
			<div>
				<Card className="uiCard">
					<CardTitle title={translate("employee.EducationalQualification.title")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
								<th>{translate("employee.EducationalQualification.fields.qualification")}</th>
								<th>{translate("employee.EducationalQualification.fields.majorSubject")}</th>
								<th>{translate("employee.TechnicalQualification.fields.yearOfPassing")}</th>
								<th>{translate("employee.EducationalQualification.fields.university")}</th>
								<th>{translate("employee.EducationalQualification.fields.documents")}</th>
								<th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderOtherDetailsBody('edu')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('edu')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
				<Card className="uiCard">
					<CardTitle title={translate("employee.TechnicalQualification.title")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
								<th>{translate("employee.TechnicalQualification.fields.skill")}</th>
								<th>{translate("employee.TechnicalQualification.fields.grade")}</th>
								<th>{translate("employee.TechnicalQualification.fields.yearOfPassing")}</th>
								<th>{translate("employee.TechnicalQualification.fields.remarks")}</th>
								<th>{translate("employee.EducationalQualification.fields.documents")}</th>
								<th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderOtherDetailsBody('tech')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('tech')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
				<Card className="uiCard">
					<CardTitle title={translate("employee.DepartmentalTest.title")} />
					<CardText>
						<Table bordered responsive className="table-striped">
							<thead>
								<th>{translate("employee.DepartmentalTest.fields.test")}</th>
								<th>{translate("employee.TechnicalQualification.fields.yearOfPassing")}n</th>
								<th>{translate("employee.TechnicalQualification.fields.remarks")}</th>
                <th>{translate("employee.EducationalQualification.fields.documents")}</th>
								<th>{translate("employee.Assignment.fields.action")}</th>
							</thead>
							<tbody>
								{renderOtherDetailsBody('dept')}
							</tbody>
						</Table>
						<Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton style={{marginRight: 0}} mini={true} secondary={true} onClick={() => {
                  self.setModalOpen('dept')
                }}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>
					</CardText>
				</Card>
			</div>
		)
	}

  createOrUpdate = (e) => {
    e.preventDefault();
    let employee = Object.assign({}, this.props.Employee);
    let self = this;
    if (employee.assignments.length == 0 || employee.jurisdictions.length == 0) {
      self.props.toggleSnackbarAndSetText(true, "Please enter atleast one assignment and jurisdiction.");
    } else if(!isHavingPrimary(employee)) {
      self.props.toggleSnackbarAndSetText(true, "Atleast one primary assignment is required.");
    } else {
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

      if(employee["assignments"] && employee["assignments"].length) {
        for(var i=0; i<employee["assignments"].length; i++) {
            if(employee["assignments"][i].hod == false) {
                employee["assignments"][i].hod = [];
            }
        }
      }

      if (employee.user && employee.user.dob && self.state.screenType == "update" && employee.user.dob.indexOf("-") > -1) {
        var _date = employee.user.dob.split("-");
        employee.user.dob = _date[2] + "/" + _date[1] + "/" + _date[0];
      }

      self.props.setLoadingStatus('loading');
      uploadFiles(employee, function(err, emp) {
        if (err) {
            //Handle error
            self.props.setLoadingStatus('hide');
        } else {
          Api.commonApiPost("/hr-employee/employees/" + ((self.state.screenType == "update") ? "_update" : "_create"), {}, {Employee: employee}).then(function(res) {
            self.props.setLoadingStatus('hide');
            self.props.toggleSnackbarAndSetText(true, "Employee created successfully.");
            setTimeout(function() {
                self.props.setRoute("/search/employee/searchEmployee/view");
            }, 1500);
          }, function(err) {
            self.props.setLoadingStatus('hide');
          })
        }
      })
    }
  }

	render () {
		let self = this;
		return (
      <div>
  			<form onSubmit={(e) => {self.createOrUpdate(e)}}>
  				<Tabs>
  				    <Tab label={translate("employee.Employee.title")}>
  				      <div>
  				        {self.renderEmployee()}
  				      </div>
  				    </Tab>
  				    <Tab label={translate("employee.Assignment.title")}>
  				      <div>
  				        {self.renderAssignment()}
  				      </div>
  				    </Tab>
  				    <Tab
  				      label={translate("employee.Jurisdictions.title")}>
  				      <div>
  				        {self.renderJurisdiction()}
  				      </div>
  				    </Tab>
  				    <Tab
  				      label={translate("employee.service.title")}>
  				      <div>
  				        {self.renderService()}
  				      </div>
  				    </Tab>
  				    <Tab
  				      label={translate("employee.other.title")}>
  				      <div>
  				        {self.renderOtherDetails()}
  				      </div>
  				    </Tab>
  				</Tabs>
  				<br/>
          <div style={{textAlign: "center"}}>
  				  <RaisedButton type="submit" label="Submit" primary={true} disabled={!self.props.isFormValid}/>
          </div>
  			</form>
        <Dialog
            title={self.getModalTitle()}
            actions={
              [<FlatButton
              label={translate("employee.Cancel.Button")}
              primary={true}
              onTouchTap={self.handleClose}
            />,
            <FlatButton
              label={translate("employee.addedit.Button")}
              primary={true}
              keyboardFocused={true}
              onClick={self.submitModalData}
            />]
            }
            modal={false}
            open={self.state.open}
            onRequestClose={self.handleClose}
            autoScrollBodyContent={true}
          >
            {self.renderContent()}
        </Dialog>
      </div>
		);
	}
}

const mapStateToProps = state => {
  return ({Employee : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({

    setForm: (data, isUpdate) => {
        var requiredList = ['user.name', 'code', 'employeeType', 'dateOfAppointment', 'employeeStatus', 'maritalStatus', 'user.userName', 'user.gender', 'user.mobileNumber', 'user.active', 'user.dob'];
        dispatch({
            type: "SET_FORM",
            data,
            isFormValid: isUpdate ? true : false,
            fieldErrors: {},
            validationData: {
                required: {
                    current: [],
                    required: requiredList
                },
                pattern: {
                    current: [],
                    required: []
                }
            }
        });
    },

    handleChange: (e, property, isRequired, pattern) => {
        dispatch({
            type: "HANDLE_CHANGE",
            property,
            value: e.target.value,
            isRequired,
            pattern
        });
    },

    handleChangeNextLevel: (e, property, propertyOne, isRequired, pattern) => {
    	dispatch({
            type: "HANDLE_CHANGE_NEXT_ONE",
            property,
            propertyOne,
            value: e.target.value,
            isRequired,
            pattern
        });
    },

    setLoadingStatus: (loadingStatus) => {
        dispatch({ type: "SET_LOADING_STATUS", loadingStatus });
    },

    toggleDailogAndSetText: (dailogState, msg) => {
        dispatch({ type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState, msg });
    },

    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
        dispatch({ type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg });
    },
    setRoute: (route) => dispatch({type: "SET_ROUTE", route})
})

export default connect(mapStateToProps, mapDispatchToProps)(Employee);
