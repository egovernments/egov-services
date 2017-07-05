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
  showTable: false,
  buttonText: "Search",
  editIndex: -1
};

export default(state = defaultState, action) => {
  switch (action.type) {

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
          form: {}
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
          }
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
      return {
          ...state,
          files: action.files
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
      console.log(state.form[action.formObject]);
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
        toastMsg:action.toastMsg,
        snackbarOpen:action.snackbarState,
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
  console.log(validationData.required.required.length)
  console.log(validationData.required.current.length)
  // var isFormValid=false;
  // (validationData.required.required.length == validationData.required.current.length) && (validationData.pattern.required.length == validationData.pattern.current.length)
  return {
    errorText: errorText,
    validationData: validationData,
    isFormValid: (validationData.required.required.length == validationData.required.current.length)
  };
}
