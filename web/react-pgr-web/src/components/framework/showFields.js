import React, {Component} from 'react';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';

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


export default class ShowFields extends Component {
  constructor(props) {
       super(props);
  }

  renderGroups=(groups,noCols,uiFramework="google")=>
  {
    let {renderField}=this;
    switch (uiFramework) {
      case "google":
        return groups.map((group,groupIndex)=>{
          return (<Card key={groupIndex}>
                    <CardHeader style={{paddingBottom:0}} title={group.label}/>
                    <CardText style={{padding:0}}>
                    <Grid>
                      <Row>
                        {group.fields.map((field,fieldIndex)=>{
                            return (
                                <Col key={fieldIndex} xs={12} md={noCols}>
                                    {renderField(field)}
                                </Col>
                            )
                        })}
                      </Row>
                    </Grid>
                    </CardText>
                </Card>)
        })
        break;
    }
  }


  renderField=(item)=> {
  	switch(item.type) {
  		case 'text':
  			 return <UiTextField ui="google" item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'singleValueList':
  			return <UiSelectField ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'multiValueList':
  			return <UiMultiSelectField ui="google" item={item} />
  			break;
  		case 'number':
  			return <UiNumberField ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'textarea':
  			return <UiTextArea ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'mobileNumber':
  			return <UiMobileNumber ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'checkbox':
  			return <UiCheckBox ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'email':
  			return <UiEmailField ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'button':
  			return <UiButton ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  			break;
  		case 'datePicker':
  			return <UiDatePicker ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;
      case 'singleFileUpload':
  			return <UiSingleFileUpload ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
        break;
      case 'multiFileUpload':
    		return <UiMultiSelectField ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
  	  case 'pan':
        return <UiPanCard ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
      case 'aadhar':
        return <UiAadharCard ui="google" item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
    }
  }

  render() {
    let  {groups,noCols,uiFramework}=this.props;
  	return ( <div>
  	 	{this.renderGroups(groups,noCols,uiFramework)}
  	 </div>)
  }
}
