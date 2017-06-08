import _ from 'lodash';

const defaultState = {
  form: {},
  msg: '',
  dialogOpen: false,
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
          form: {
            ...state.form,
            [action.object]:null
          }
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

    case "RESET_STATE":
      return {
        form: {},
        fieldErrors: {},
        validationData: action.validationData,
        msg: '',
        dialogOpen: false,
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
        if (_.indexOf(validationData.pattern.current, name) == -1) {
          validationData.pattern.current.push(name);
        }
      } else {
        validationData.pattern.current = _.remove(validationData.pattern.current, (item) => {
          return item != name
        });
        errorText = "Invalid field data";
      }
    }
  }
  if (!isRequired && value == "") {
    errorText = "";
  }
  // var isFormValid=false;
  // (validationData.required.required.length == validationData.required.current.length) && (validationData.pattern.required.length == validationData.pattern.current.length)
  return {
    errorText: errorText,
    validationData: validationData,
    isFormValid: (validationData.required.required.length == validationData.required.current.length) && (errorText == "")
  };
}
