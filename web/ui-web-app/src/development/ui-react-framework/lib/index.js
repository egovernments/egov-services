import create from './create';
import view from './view';
import search from './search';
import ShowFields from './showFields';
import UiTextField from './components/UiTextField'
import UiSelectField from './components/UiSelectField'
import UiSelectFieldMultiple from './components/UiSelectFieldMultiple'
import UiButton from './components/UiButton'
import UiCheckBox from './components/UiCheckBox'
import UiEmailField from './components/UiEmailField'
import UiMobileNumber from './components/UiMobileNumber'
import UiTextArea from './components/UiTextArea'
import UiMultiSelectField from './components/UiMultiSelectField'
import UiNumberField from './components/UiNumberField'
import UiDatePicker from './components/UiDatePicker'
import UiMultiFileUpload from './components/UiMultiFileUpload'
import UiSingleFileUpload from './components/UiSingleFileUpload'
import UiAadharCard from './components/UiAadharCard'
import UiPanCard from './components/UiPanCard'
import UiLabel from './components/UiLabel'
import UiRadioButton from './components/UiRadioButton'
import UiTextSearch from './components/UiTextSearch'
import UiDocumentList from './components/UiDocumentList'
import UiAutoComplete from './components/UiAutoComplete'
import UiPinCode from './components/UiPinCode';
import UiArrayField from './components/UiArrayField';
import UiFileTable from './components/UiFileTable';
import UiMultiFieldTable from './components/UiMultiFieldTable';
import UigoogleMaps from './components/UigoogleMaps'
import UiWorkflow from './components/UiWorkflow';
import UiTimeField from './components/UiTimeField';
import UiCalendar from './components/UiCalendar';
import UiDynamicTable from './components/UiDynamicTable';
import UiDynamicTable2 from './components/uiDynamicTable2';
import UiWindowForm from './components/UiWindowForm';
import UiBackButton from './components/UiBackButton';
import templateParser from './templates/templateParser/templateParser';
import {int_to_words, getFullDate, fileUpload, getInitiatorPosition, getTitleCase} from './utility/utility';

var routes = [{
	component: create,
	route: '/create/:moduleName/:master?/:id?'
}, {
	component: view,
	route: '/view/:moduleName/:master?/:id'
}, {
	component: search,
	route: '/search/:moduleName/:master?/:action'
}, {
	component: create,
	route: '/update/:moduleName/:master?/:id?'
}, {
	component: templateParser,
	route: '/print/report/:templatePath'
}];

export {
	routes,
	UiTextField,
	ShowFields,
	UiSelectField,
	UiSelectFieldMultiple,
	UiButton,
	UiCheckBox,
	UiEmailField,
	UiMobileNumber,
	UiTextArea,
	UiMultiSelectField,
	UiNumberField,
	UiDatePicker,
	UiMultiFileUpload,
	UiSingleFileUpload,
	UiAadharCard,
	UiPanCard,
	UiLabel,
	UiRadioButton,
	UiTextSearch,
	UiDocumentList,
	UiAutoComplete,
	UiPinCode,
	UiArrayField,
	UiFileTable,
	UiMultiFieldTable,
	UigoogleMaps,
	UiWorkflow,
	UiTimeField,
	UiCalendar,
	int_to_words,
	getFullDate,
	fileUpload,
	getInitiatorPosition,
	getTitleCase,
	UiDynamicTable,
	UiDynamicTable2,
	UiWindowForm,
	UiBackButton
};
