import create from './create';
import view from './view';
import search from './search';
import showFields from './showFields';
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
import UiDialogBox from './components/UiDialogBox'
import UigoogleMaps from './components/UigoogleMaps'
import UiWorkflow from './components/UiWorkflow';
import UiTimeField from './components/UiTimeField';
import UiCalendar from './components/UiCalendar';

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
	component: UiTextField,
	route: ''
}, {
	component: UiSelectField,
	route: ''
}, {
	component: UiSelectFieldMultiple,
	route: ''
}, {
	component: UiButton,
	route: ''
}, {
	component: UiCheckBox,
	route: ''
}, {
	component: UiEmailField,
	route: ''
}, {
	component: UiMobileNumber,
	route: ''
}, {
	component: UiTextArea,
	route: ''
}, {
	component: UiNumberField,
	route: ''
}, {
	component: UiDatePicker,
	route: ''
}, {
	component: UiMultiFileUpload,
	route: ''
}, {
	component: UiSingleFileUpload,
	route: ''
}, {
	component: UiAadharCard,
	route: ''
}, {
	component: UiPanCard,
	route: ''
}, {
	component: UiLabel,
	route: ''
}, {
	component: UiRadioButton,
	route: ''
}, {
	component: UiTextSearch,
	route: ''
}, {
	component: UiDocumentList,
	route: ''
}, {
	component: UiAutoComplete,
	route: ''
}, {
	component: UiPinCode,
	route: ''
}, {
	component: UiArrayField,
	route: ''
}, {
	component: UiFileTable,
	route: ''
}, {
	component: UiMultiFieldTable,
	route: ''
}, {
	component: UiDialogBox,
	route: ''
}, {
	component: UigoogleMaps,
	route: ''
}, {
	component: UiWorkflow,
	route: ''
}, {
	component: UiTimeField,
	route: ''
}, {
	component: UiCalendar,
	route: ''
}];

module.exports = routes;