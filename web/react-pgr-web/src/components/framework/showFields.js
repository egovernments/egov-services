import React, {Component} from 'react'

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap'
import {Card, CardHeader, CardText} from 'material-ui/Card'

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
import UiDocuments from './components/UiDocuments'
import UiAutoComplete from './components/UiAutoComplete'
import FloatingActionButton from 'material-ui/FloatingActionButton'
import UiDate from './components/UiDate';
import UiPinCode from './components/UiPinCode';
import UiArrayField from './components/UiArrayField';
import UiFileTable from './components/UiFileTable';
import UiMultiFieldTable from './components/UiMultiFieldTable';
import UiHyperLink from './components/UiHyperLink'
import UigoogleMaps from './components/UigoogleMaps'
import UiWorkflow from './components/UiWorkflow';
import UiTimeField from './components/UiTimeField';
import UiWindowForm from './components/UiWindowForm';
import UiCalendar from './components/UiCalendar';
import UiImage from './components/UiImage';
import UiMultiFieldAddToTable from './components/UiMultiFieldAddToTable';
import UiNestedTablesInputs from './components/UiNestedTablesInputs';


let styles={
  reducePadding: {
    paddingTop:4,
    paddingBottom:0
  }
}

export default class ShowFields extends Component {
  constructor(props) {
       super(props);
       this.state = {};
  }

  changeExpanded = (name) => {
    this.setState({
      [name]: !this.state[name]
    })
  }

  renderCard = (group, groupIndex, noCols, jsonPath, uiFramework, groups, isMultiple) => {
    let self = this;
    let {addNewCard, removeCard} = this.props;
    let {renderField}=this;
    return (
      <Card style={{"display": eval(group.hide) ? "none" : "block", "marginBottom": isMultiple ? '0px' : '', "marginTop": isMultiple ? '0px' : ''}} className={"uiCard "+group.name} key={groupIndex} expanded={self.state[group.name] ? false : true} onExpandChange={() => {self.changeExpanded(group.name)}}>
          {!isMultiple && <CardHeader style={{paddingTop:4,paddingBottom:0}} title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{group.label}</div>} actAsExpander={true}/>}
          <CardText style={{paddingTop:0,paddingBottom:0}} expandable={true}>
          <Grid fluid={group.isFullWidth || false}  style={{paddingTop:0}}>
            <Row>
              {group.fields.map((field, fieldIndex)=>{
                  if(!field.isHidden) {
                    return (
                      <Col key={fieldIndex} xs={12} sm={field.type === "documentList" || field.type === "viewDocuments" || field.type === "fileTable" || field.type === "tableList" || field.type === "nestedTableList" || (field.type === "textarea" && field.fullWidth === true) || field.type === "workflow" || field.type === "multiFieldAddToTable" ? 12 : noCols} md={field.type === "documentList" || field.type === "viewDocuments" || field.type === "fileTable" || field.type === "tableList" || field.type === "nestedTableList" || (field.type === "textarea" && field.fullWidth === true) || field.type==="window" || field.type === "workflow" || field.type === "multiFieldAddToTable" ? 12 : noCols}>
                          {renderField(field, self.props.screen,fieldIndex,field.screenView)}
                      </Col>
                    )
                  }
              })}
            </Row>
            {self.props.screen!= "view" && group.multiple && (!groups[groupIndex+1] || (groups[groupIndex+1] && groups[groupIndex+1].name != group.name)) && <Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton mini={true} onClick={() => {addNewCard(group, jsonPath, group.name)}}>
                  <span className="glyphicon glyphicon-plus"></span>
                </FloatingActionButton>
              </Col>
            </Row>}
            {self.props.screen!= "view" && group.multiple && (groups[groupIndex+1] && groups[groupIndex+1].name == group.name) && <Row>
              <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                <FloatingActionButton mini={true} secondary={true} onClick={() => {removeCard(jsonPath, groupIndex, group.name)}}>
                  <span className="glyphicon glyphicon-minus"></span>
                </FloatingActionButton>
              </Col>
            </Row>}
          </Grid>
          <div style={{"marginLeft": "15px", "marginRight": "15px"}}>
            {
              group.children &&
              group.children.length ?
              group.children.map(function(child) {
                return self.renderGroups(child.groups, noCols, uiFramework, child.jsonPath);
              }) : ""
            }
          </div>
          </CardText>
      </Card>
    )
  }

  renderGroups=(groups, noCols, uiFramework="google", jsonPath) => {
    let self = this;
    switch (uiFramework) {
      case "google":
        let listArr = {};
        for(var i=0; i<groups.length; i++) {
          if(groups[i].multiple) {
            if(!listArr[groups[i].name]) listArr[groups[i].name] = {
              objects: []
            };
            listArr[groups[i].name].objects.push({
              object: groups[i],
              index: i
            });
          } else {
            listArr[groups[i].name] =  {
              object: groups[i],
              index: i
            }
          }
        }

        return Object.keys(listArr).map((key, groupIndex) => {
          if(listArr[key].objects) {
            return (
              <Card className={"uiCard "} expanded={true}>
                <CardHeader style={{paddingTop:4,paddingBottom:0}} title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{listArr[key].objects[0].object.label}</div>} subtitle={typeof(listArr[key].objects[0].object.description)?listArr[key].objects[0].object.description:""} showExpandableButton={true} actAsExpander={true}/>
                  <CardText style={{paddingTop:0,paddingBottom:0}} style={{padding:0}} expandable={true}>
                    {
                      listArr[key].objects.map((grp, grpIndex) => {
                        return self.renderCard(grp.object, grp.index, noCols, jsonPath, uiFramework, groups, true);
                      })
                    }
                  </CardText>
              </Card>
            );
          } else {
            return self.renderCard(listArr[key].object, listArr[key].index, noCols, jsonPath, uiFramework, groups);
          }
        })
        break;
    }
  }


  renderField=(item, screen, index,screenView)=> {
    // console.log(item.type, item.jsonPath);
    if(screen == "view" && ["viewDocuments","documentList", "fileTable", "arrayText", "arrayNumber", "tableList", "workflow"].indexOf(item.type) == -1 ) {
      if (item.type == "datePicker") {
        item.isDate = true;
      }
      if(item.type =="timePicker"){
        item.isTime=true
      }
      item.type = "label";
    }
  	switch(item.type) {
  		case 'text':
  			 return <UiTextField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'singleValueListMultiple':
  			return <UiSelectFieldMultiple tabIndex={index} ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'singleValueList':
  			return <UiSelectField tabIndex={index} ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'multiValueList':
      return <UiMultiSelectField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'autoCompelete':
        return <UiAutoComplete tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler || ""}/>
      case 'multiFieldAddToTable':
        return <UiMultiFieldAddToTable tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} screen={screen}/>
    	case 'number':
  			return <UiNumberField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'textarea':
  			return <UiTextArea tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'mobileNumber':
  			return <UiMobileNumber tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'checkbox':
  			return <UiCheckBox tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'email':
  			return <UiEmailField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'button':
  			return <UiButton tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  		case 'datePicker':
  			return <UiDatePicker tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'date':
    			return <UiDate tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'singleFileUpload':
  			return <UiSingleFileUpload tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'multiFileUpload':
    		return <UiMultiFileUpload tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  	  case 'pan':
        return <UiPanCard tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'aadhar':
        return <UiAadharCard tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'pinCode':
        return <UiPinCode tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'label':
        return <UiLabel tabIndex={index} getVal={this.props.getVal} item={item}/>
      case 'radio':
        return <UiRadioButton tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'textSearch':
        return <UiTextSearch tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler}/>
      case 'documentList':
        return <UiDocumentList tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} readonly={screen === "view" ? "true" : ""}/>
      case 'viewDocuments':
        return <UiDocuments id={this.props.id} tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} readonly={screen === "view" ? "true" : ""}/>
      case 'arrayText':
      case 'arrayNumber':
        return <UiArrayField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} readonly={screen === "view" ? "true" : ""}/>
      case 'fileTable':
        return <UiFileTable tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} readonly={screen === "view" ? "true" : (screenView?true:"")}/>
      case 'tableList':
        return <UiMultiFieldTable tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} screen={screen}/>
      case 'nestedTableList':
        return <UiNestedTablesInputs tabIndex={index} ui={this.props.ui} getRequiredFields={this.props.getRequiredFields} addRequiredFields={this.props.addRequiredFields} delRequiredFields={this.props.delRequiredFields} setVal={this.props.setVal} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} screen={screen}/>
      case 'workflow':
        return <UiWorkflow tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} initiateWF={this.props.initiateWF} workflowId={this.props.workflowId || ""}/>
      case 'timePicker':
        return <UiTimeField tabIndex={index} ui={this.props.ui}
        getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors}
        handler={this.props.handler} screen={screen}/>;
      case 'window':
        return <UiWindowForm tabIndex={index} ui={this.props.ui}
        getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors}
        handler={this.props.handler} screen={screen}/>;
      case 'calendar':
        return <UiCalendar ui={this.props.ui} getVal={this.props.getVal}
        item={item} fieldErrors={this.props.fieldErrors}
        handler={this.props.handler} screen={screen}/>;
      case 'customComponent':
        // console.log(item.path);
        // {
        //   "name": "customReport",
        //   "jsonPath": "Connection.property.nameOfApplicant",
        //   "label": "wc.create.groups.applicantDetails.nameOfApplicant",
        //   "pattern": "",
        //   "type": "customComponent",
        //   "isRequired": false,
        //   "isDisabled": true,
        //   "requiredErrMsg": "",
        //   "patternErrMsg": "",
        //   "path":"./CustomComponent"
        // }

          var CustomComonent=require(item.path+".js").default;
          return <CustomComonent/>
          case 'hyperLink':
      			return <UiHyperLink tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>

          case 'googleMaps':
        		return <UigoogleMaps tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>

            case 'image':
              return <UiImage tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
          // return <div></div>
    }
  }

  render() {
    let  {groups, noCols, uiFramework}=this.props;
  	return ( <div>
  	 	{this.renderGroups(groups, noCols, uiFramework)}
  	 </div>)
  }
}
