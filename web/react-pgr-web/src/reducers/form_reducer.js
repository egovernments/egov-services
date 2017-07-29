import _ from 'lodash';

const defaultState = {
  form: {},
  files: [],
  msg: '',
  toastMsg:'',
  dialogOpen: false,
  snackbarOpen:false,
  fieldErrors: {},
  isFormValid: false,
  validationData: {},
  validatePropertyOwner: {},
  validatePropertyFloor: {},
  showTable: false,
  buttonText: "Search",
  editIndex: -1,
  isSuccess: false,
  isError: false,
  isOwnerValid: false,
  isFloorValid: false
};

export default(state = defaultState, action) => {
  switch (action.type) {
	  
	  
	case "ADD_REQUIRED" : 
	var b = state.validationData.required.required.indexOf(action.property);
	if(b==-1){
		state.validationData.required.required.push(action.property);
	}
		
		return {
			...state,
		}
	case "REMOVE_REQUIRED" : 
	
		var a = state.validationData.required.required.indexOf(action.property);
		var b = state.validationData.required.current.indexOf(action.property);
		
		console.log('isthere', a)
		if(a!=-1){
			state.validationData.required.required.splice(a,1);
		}
		
		if(b!=-1){
			state.validationData.required.current.splice(b,1);
		}
			
		return {
			...state
		}

    case "PUSH_ONE":

          if (!state.form.hasOwnProperty(action.formArray)) {
            state.form[action.formArray]=[];
          }

          return {
            ...state,
            form: {
              ...state.form,
              [action.formArray]: [
                ...state.form[action.formArray],
                   state.form[action.formData]
              ]
            }
          }

      case "RESET_OBJECT":

              return {
                ...state,
                form: {
                  ...state.form,
                  [action.object]:null
                },
				isOwnerValid: action.isSectionValid,
				isFloorValid: action.isSectionValid
              }

	case "EMPTY_PROPERTY":

		return {
			...state,
			form:{
				...state.form,
				[action.property]:null
			},
			validationData: action.validationData,
			isFormValid: action.isFormValid
		}


    case "UPDATE_OBJECT":

      state.form[action.objectName][state.editIndex]=state.form[action.object]

        return {
          ...state,
          form: {
            ...state.form,
          [action.objectName]: state.form[action.objectName].map((e,i) => e )
          }
        }

    case "EDIT_OBJECT":

        return {
          ...state,
          form: {
            ...state.form,
            [action.objectName]:action.object
          },
		  isOwnerValid: action.isSectionValid,
				isFloorValid: action.isSectionValid
        }

    case "EDIT_INDEX":

      return {
        ...state,
        editIndex: action.index
      }

    case "DELETE_OBJECT":

      return {
        ...state,
        form: {
          ...state.form,
          [action.property]:[
            ...state.form[action.property].slice(0, action.index),
            ...state.form[action.property].slice(action.index+1)
          ]
        }
      }


    case "SET_FORM":
      return {
        ...state,
        form: action.data,
        fieldErrors: action.fieldErrors,
        validationData: action.validationData,
        isFormValid: action.isFormValid
      }

    case "HANDLE_CHANGE":
      validationData = undefined;
      validationData = validate(action.isRequired, action.pattern, action.property, action.value, state.validationData);
      return {
        ...state,
        form: {
          ...state.form,
          [action.property]: action.value
        },
        fieldErrors: {
          ...state.fieldErrors,
          [action.property]: validationData.errorText
        },
        validationData: validationData.validationData,
        isFormValid: validationData.isFormValid
      }

      case 'FILE_UPLOAD':
      var filearray = [];
      filearray = [...state.files];
      filearray.push(action.files);
      return {
          ...state,
          files: filearray
      };

      case 'FILE_EMPTY':
      filearray = [];
      return {
          ...state,
          files: filearray
      };

      case 'FILE_REMOVE':
      filearray = [];
      filearray = [...state.files];
      let idx = -1;
      for(let i = 0; i < filearray.length; i++) {
          if (filearray[i].name === action.removefiles) {
              idx = i;
              break;
          }
      }
      if(idx !== -1){
        //remove the index idx object
        filearray.splice(idx, 1);
      }
      return {
          ...state,
          files: filearray
      };

      case "HANDLE_CHANGE_NEXT_ONE":

        validationData = undefined;
        validationData = validate(action.isRequired, action.pattern, action.propertyOne, action.value, state.validationData);
        return {
          ...state,
          form: {
            ...state.form,
            [action.property]: {
              ...state.form[action.property],
              [action.propertyOne]: action.value
            }
          },
          fieldErrors: {
            ...state.fieldErrors,
            [action.property]: {
              ...state.fieldErrors[action.property],
              [action.propertyOne]: validationData.errorText
            }

          },
          validationData: validationData.validationData,
          isFormValid: validationData.isFormValid
        }
		
	 case "HANDLE_CHANGE_OWNER":

       let validatePropertyOwner;
	   console.log('state', state);
        validatePropertyOwner = validate2(action.isRequired, action.pattern, action.propertyOne, action.value, state.validatePropertyOwner);
		console.log(validatePropertyOwner);
        return {
          ...state,
          form: {
            ...state.form,
            [action.property]: {
              ...state.form[action.property],
              [action.propertyOne]: action.value
            }
          },
          fieldErrors: {
            ...state.fieldErrors,
            [action.property]: {
              ...state.fieldErrors[action.property],
              [action.propertyOne]: validatePropertyOwner.errorText
            }

          },
          validatePropertyOwner: validatePropertyOwner.validatePropertyOwner,
          isOwnerValid: validatePropertyOwner.isOwnerValid
        }	

		
	 case "HANDLE_CHANGE_FLOOR":

       let validatePropertyFloor;
	   console.log('state', state);
        validatePropertyFloor = validate3(action.isRequired, action.pattern, action.propertyOne, action.value, state.validatePropertyFloor);
		console.log(validatePropertyFloor);
        return {
          ...state,
          form: {
            ...state.form,
            [action.property]: {
              ...state.form[action.property],
              [action.propertyOne]: action.value
            }
          },
          fieldErrors: {
            ...state.fieldErrors,
            [action.property]: {
              ...state.fieldErrors[action.property],
              [action.propertyOne]: validatePropertyFloor.errorText
            }

          },
			validatePropertyFloor: validatePropertyFloor.validatePropertyFloor,
			isFloorValid: validatePropertyFloor.isFloorValid
        }
		
    case "HANDLE_CHANGE_NEXT_TWO":
      let validationData = undefined;
      validationData = validate(action.isRequired, action.pattern, action.propertyTwo, action.value, state.validationData);
      return {
        ...state,
        form: {
          ...state.form,
          [action.property]: {
            ...state.form[action.property],
            [action.propertyOne]: {
              ...state.form[action.property][action.propertyOne],
              [action.propertyTwo]: action.value
            }
          }
        },
        fieldErrors: {
          ...state.fieldErrors,
          [action.propertyTwo]: validationData.errorText
        },
        validationData: validationData.validationData,
        isFormValid: validationData.isFormValid
      }

    case "ADD_MANDATORY":
    var obj = state.validationData;
    if(!obj.required.required.includes(action.property)){
      obj.required.required.push(action.property)
      if (action.pattern.toString().length > 0) obj.pattern.required.push(action.property);
      return{
        ...state,
        validationData: obj
      }
    }else{
      return {
        ...state
      }
    }

    case "PUSH_ONE_ARRAY" :

    if (!state.form.hasOwnProperty(action.formObject)) {
          state.form[action.formObject] = {};
          state.form[action.formObject][action.formArray] = [];
    } else if(!state.form[action.formObject]) { alert('Boom2');
      state.form[action.formObject] = {};
      state.form[action.formObject][action.formArray] = [];
    } else if(!state.form[action.formObject][action.formArray]) {
      state.form[action.formObject][action.formArray] = [];
      //console.log(state.form[action.formObject]);
    }

    return {
      ...state,
      form: {
        ...state.form,
        [action.formObject]: {
          ...state.form[action.formObject],
             [action.formArray]:[
               ...state.form[action.formObject][action.formArray],
               state.form[action.formData]
             ]
        }
      }
    }


    case "REMOVE_MANDATORY":
    var obj = state.validationData;
    if(obj.required.required.includes(action.property)){
      let rindex = obj.required.required.indexOf(action.property);
      obj.required.required.splice(rindex, 1);
      if(obj.required.current.includes(action.property)){
        let cindex = obj.required.current.indexOf(action.property);
        obj.required.current.splice(cindex, 1);
      }
      if (action.pattern.toString().length > 0){
        let pindex = obj.pattern.required.indexOf(action.property);
        obj.pattern.required.splice(pindex, 1);
      }
      return{
        ...state,
        validationData: obj
      }
    }else{
      return {
        ...state
      }
    }
	
    case "RESET_STATE":
      return {
        form: {},
        files :[],
        fieldErrors: {},
        validationData: action.validationData,
		validatePropertyOwner: action.validatePropertyOwner,
		validatePropertyFloor: action.validatePropertyFloor,
        msg: '',
        dialogOpen: false,
        snackbarOpen: false,
        isFormValid: false,
        showTable:false,
        buttonText:"Search"
      }
	  
    case 'FIELD_ERRORS':
      return {
        ...state,
        fieldErrors: action.errors
      }

    case "SHOW_TABLE":
      return {
        ...state,
        showTable: action.state
      }
    case "BUTTON_TEXT":
      return {
        ...state,
        buttonText: action.text
      }
    case "TOGGLE_DAILOG_AND_SET_TEXT":
      return {
        ...state,
        msg:action.msg,
        dialogOpen:action.dailogState,
      }
    case "TOGGLE_SNACKBAR_AND_SET_TEXT":
      return {
        ...state,
        toastMsg: action.toastMsg,
        snackbarOpen: action.snackbarState,
        isSuccess: action.isSuccess || false,
        isError: action.isError || false
      }
    case "SET_LOADING_STATUS":
      return {
        ...state,
        loadingStatus: action.loadingStatus
      }
    default:
      return state;
  }
}

function validate(isRequired, pattern, name, value, validationData) {
  let errorText = "";
  if (isRequired) {
    if (value.length || value) {
      if (_.indexOf(validationData.required.current, name) == -1) {
        validationData.required.current.push(name);
      }
    } else {
      validationData.required.current = _.remove(validationData.required.current, (item) => {
        return item != name
      });
      errorText = "This is field is required";
    }
  }
  if (pattern.toString().length > 0) {
    if (value != "") {
      if (pattern.test(value)) {
        // if (_.indexOf(validationData.pattern.current, name) == -1) {
        //   validationData.pattern.current.push(name);
        // }

      } else if(pattern == "/^[0-9]+$/"){

		validationData.required.current = _.remove(validationData.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "It expects numeric only";

	  } else {
        validationData.required.current = _.remove(validationData.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "Invalid field data";
      }
    }
  }
  if (!isRequired && value == "") {
    errorText = "";
  }
  // console.log(validationData.required.required)
  // console.log(validationData.required.current)
  // var isFormValid=false;
  // (validationData.required.required.length == validationData.required.current.length) && (validationData.pattern.required.length == validationData.pattern.current.length)
  console.log(validationData.required.required.length, validationData.required.current.length);
  return {
    errorText: errorText,
    validationData: validationData,
    isFormValid: (validationData.required.required.length == validationData.required.current.length)
  };
}


function validate2(isRequired, pattern, name, value, validatePropertyOwner) {
	
console.log('Here', validatePropertyOwner);

  let errorText = "";
  if (isRequired) {
    if (value.length || value) {
      if (_.indexOf(validatePropertyOwner.required.current, name) == -1) {
        validatePropertyOwner.required.current.push(name);
      }
    } else {
      validatePropertyOwner.required.current = _.remove(validatePropertyOwner.required.current, (item) => {
        return item != name
      });
      errorText = "This is field is required";
    }
  }
  if (pattern.toString().length > 0) {
    if (value != "") {
      if (pattern.test(value)) {
        // if (_.indexOf(validationData.pattern.current, name) == -1) {
        //   validationData.pattern.current.push(name);
        // }

      } else if(pattern == "/^[0-9]+$/"){

		validatePropertyOwner.required.current = _.remove(validatePropertyOwner.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "It expects numeric only";

	  } else {
        validatePropertyOwner.required.current = _.remove(validatePropertyOwner.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "Invalid field data";
      }
    }
  }
  if (!isRequired && value == "") {
    errorText = "";
  }
  // console.log(validationData.required.required)
  // console.log(validationData.required.current)
  // var isFormValid=false;
  // (validationData.required.required.length == validationData.required.current.length) && (validationData.pattern.required.length == validationData.pattern.current.length)
  console.log(validatePropertyOwner.required.required.length, validatePropertyOwner.required.current.length);
  return {
    errorText: errorText,
    validatePropertyOwner: validatePropertyOwner,
    isOwnerValid: (validatePropertyOwner.required.required.length == validatePropertyOwner.required.current.length)
  };
}


function validate3(isRequired, pattern, name, value, validatePropertyFloor) {
	
console.log('Here', validatePropertyFloor);

  let errorText = "";
  if (isRequired) {
    if (value.length || value) {
      if (_.indexOf(validatePropertyFloor.required.current, name) == -1) {
        validatePropertyFloor.required.current.push(name);
      }
    } else {
      validatePropertyFloor.required.current = _.remove(validatePropertyFloor.required.current, (item) => {
        return item != name
      });
      errorText = "This is field is required";
    }
  }
  if (pattern.toString().length > 0) {
    if (value != "") {
      if (pattern.test(value)) {
        // if (_.indexOf(validationData.pattern.current, name) == -1) {
        //   validationData.pattern.current.push(name);
        // }

      } else if(pattern == "/^[0-9]+$/"){

		validatePropertyFloor.required.current = _.remove(validatePropertyFloor.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "It expects numeric only";

	  } else {
        validatePropertyFloor.required.current = _.remove(validatePropertyFloor.required.current, (item) => {
          return item != name
        });
        // validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
        //   return item != name
        // });
        errorText = "Invalid field data";
      }
    }
  }
  if (!isRequired && value == "") {
    errorText = "";
  }
  // console.log(validationData.required.required)
  // console.log(validationData.required.current)
  // var isFormValid=false;
  // (validationData.required.required.length == validationData.required.current.length) && (validationData.pattern.required.length == validationData.pattern.current.length)
  return {
    errorText: errorText,
    validatePropertyFloor: validatePropertyFloor,
    isFloorValid: (validatePropertyFloor.required.required.length == validatePropertyFloor.required.current.length)
  };
}
