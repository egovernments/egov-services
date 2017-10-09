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
import UiAutoComplete from './components/UiAutoComplete'
import FloatingActionButton from 'material-ui/FloatingActionButton'
import UiDate from './components/UiDate';
import UiPinCode from './components/UiPinCode';

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
          <Grid style={{paddingTop:0}}>
            <Row>
              {group.fields.map((field, fieldIndex)=>{
                  if(!field.isHidden) {
                    return (
                      <Col key={fieldIndex} xs={12} sm={noCols} md={noCols}>
                          {renderField(field, self.props.screen,fieldIndex)}
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


  renderField=(item, screen,index)=> {
    if(screen == "view") {
      if (item.type == "datePicker") {
        item.isDate=true;
      }
      item.type = "label";
    }
  	switch(item.type) {
  		case 'text':
  			 return <UiTextField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;

case 'singleValueListMultiple':
  			return <UiSelectFieldMultiple tabIndex={index} ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;

  		case 'singleValueList':
  			return <UiSelectField tabIndex={index} ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'multiValueList':
      return <UiSingleFileUpload tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
      case 'autoCompelete':
        return <UiAutoComplete tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler || ""}/>
    			break;
  		case 'number':
  			return <UiNumberField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'textarea':
  			return <UiTextArea tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'mobileNumber':
  			return <UiMobileNumber tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'checkbox':
  			return <UiCheckBox tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'email':
  			return <UiEmailField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'button':
  			return <UiButton tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'datePicker':
  			return <UiDatePicker tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;
    case 'date':
    			return <UiDate tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
          break;
      case 'singleFileUpload':
  			return <UiSingleFileUpload tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;

      case 'multiFileUpload':
    		return <UiMultiSelectField tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
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
        return <UiDocumentList tabIndex={index} ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
    }
  }

  render() {
    let  {groups, noCols, uiFramework}=this.props;
  	return ( <div>
  	 	{this.renderGroups(groups, noCols, uiFramework)}
  	 </div>)
  }
}
