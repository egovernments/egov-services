import React, {Component} from 'react'

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap'
import {Card, CardHeader, CardText} from 'material-ui/Card'

import UiTextField from './components/UiTextField'
import UiSelectField from './components/UiSelectField'
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
import FloatingActionButton from 'material-ui/FloatingActionButton'

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

  renderGroups=(groups, noCols, uiFramework="google", jsonPath) => {
    let {renderField}=this;
    let {addNewCard, removeCard} = this.props;
    let self = this;
    switch (uiFramework) {
      case "google":
        return groups.map((group, groupIndex)=>{
          return (<Card style={{"display": group.hide ? "none" : "block"}} className="uiCard" key={groupIndex} expanded={self.state[group.name] ? false : true} onExpandChange={() => {self.changeExpanded(group.name)}}>
                    <CardHeader title={group.label} showExpandableButton={true} actAsExpander={true}/>
                    <CardText style={{padding:0}} expandable={true}>
                    <Grid>
                      <Row>
                        {group.fields.map((field, fieldIndex)=>{
                            if(!field.isHidden) {
                              return (
                                <Col key={fieldIndex} xs={12} md={noCols}>
                                    {renderField(field, self.props.screen)}
                                </Col>
                              )
                            }
                        })}
                      </Row>
                      {group.multiple && (!groups[groupIndex+1] || (groups[groupIndex+1] && groups[groupIndex+1].name != group.name)) && <Row>
                        <Col xsOffset={8} mdOffset={10} xs={4} md={2} style={{"textAlign": "right"}}>
                          <FloatingActionButton mini={true} onClick={() => {addNewCard(group, jsonPath, group.name)}}>
                            <span className="glyphicon glyphicon-plus"></span>
                          </FloatingActionButton>
                        </Col>
                      </Row>}
                      {group.multiple && (groups[groupIndex+1] && groups[groupIndex+1].name == group.name) && <Row>
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
                </Card>)
        })
        break;
    }
  }


  renderField=(item, screen)=> {
    if(screen == "view") {
      item.type = "label";
    }
  	switch(item.type) {
  		case 'text':
  			 return <UiTextField ui={this.props.ui} getVal={this.props.getVal} item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'singleValueList':
  			return <UiSelectField ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'multiValueList':
      return <UiSingleFileUpload ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'number':
  			return <UiNumberField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'textarea':
  			return <UiTextArea ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'mobileNumber':
  			return <UiMobileNumber ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'checkbox':
  			return <UiCheckBox ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'email':
  			return <UiEmailField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'button':
  			return <UiButton ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'datePicker':
  			return <UiDatePicker ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;
      case 'singleFileUpload':
  			return <UiSingleFileUpload ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;

      case 'multiFileUpload':
    		return <UiMultiSelectField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  	  case 'pan':
        return <UiPanCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'aadhar':
        return <UiAadharCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'label':
        return <UiLabel getVal={this.props.getVal} item={item}/>
      case 'radio':
        return <UiRadioButton ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'textSearch':
        return <UiTextSearch ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler}/>
      case 'documentList':
        return <UiDocumentList ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
    }
  }

  render() {
    let  {groups, noCols, uiFramework}=this.props;
  	return ( <div>
  	 	{this.renderGroups(groups, noCols, uiFramework)}
  	 </div>)
  }
}
