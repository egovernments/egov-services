import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import {translate} from '../../common/common';
import areIntlLocalesSupported from 'intl-locales-supported';

export default class ShowField extends Component
{
  constructor(props) {
       super(props);
      //  this.state={
      //    value:""
      //  }
   }

  renderFields = (obj) =>{
    // console.log(obj);
    let des = translate(obj.label);

    let mandatory = obj.hasOwnProperty("isMandatory")?(obj.isMandatory? " *" : ""):"*";
    let description = des + mandatory;

    let dropDownData=[];

      // obj.defaultValue=typeof(obj.defaultValue)=="object"?obj.defaultValue:{};
      if (typeof(obj.defaultValue)=="object") {
        for (var variable in obj.defaultValue) {
          // console.log(variable);

          dropDownData.push({
            key:variable,
            // value:typeof(searchForm[variable])=="object"?new Date(searchForm[variable]).getTime():searchForm[variable]
            value:obj.defaultValue[variable]

          })
        }
      }



    // console.log(obj);
    //console.log('came to renderfields',obj.code);




    // console.log(dropDownData);
    // let DateTimeFormat;
    // if (areIntlLocalesSupported(['fr', 'fa-IR'])) {
    //   DateTimeFormat = global.Intl.DateTimeFormat;
    // }
    switch(obj.type){
      case "string":
        return (
          <Col xs={12} md={3}>
            <TextField fullWidth={true} floatingLabelText={description
            } onChange={(e) => this.props.handler(e, obj.name, mandatory=="*"?true:false, '')} />
          </Col>
        );
      case "number":
        return(
          <Col xs={12} md={3}>
            <TextField fullWidth={true} floatingLabelText={description} onChange={(e) => this.props.handler(e, obj.name, mandatory=="*"?true:false, /^[+-]?\d+(\.\d+)?$/)}   />
          </Col>
        );
      case "date" :
        return(
          <Col xs={12} md={3}>
            <DatePicker fullWidth={true}  floatingLabelText={description} value={typeof(obj.value)=="object"?obj.value:{}} onChange={(first, object)=>{

              let e={
                target:{
                  value:object
                }
              }
              this.props.handler(e, obj.name, mandatory=="*"?true:false, '')

            }}/>
          {/*<DatePicker fullWidth={true} DateTimeFormat={DateTimeFormat} locale="fr" floatingLabelText={description}  />*/}
          </Col>
        );
        case "epoch" :
          return(
            <Col xs={12} md={3}>
              <DatePicker fullWidth={true}  floatingLabelText={description}  onChange={(first, object)=>{

                let e={
                  target:{
                    value:object
                  }
                }
                this.props.handler(e, obj.name, mandatory=="*"?true:false, '')

              }}/>
            {/*<DatePicker fullWidth={true} DateTimeFormat={DateTimeFormat} locale="fr" floatingLabelText={description}  />*/}
            </Col>
          );
      case "singlevaluelist":
        return(
          <Col xs={12} md={3}>
            <SelectField fullWidth={true} floatingLabelText={description} value={typeof(obj.value)=="undefined"?"":obj.value} onChange={(event, key, value) => {
              // this.setState({
              //   value
              // })
              let e={
                target:{
                  value
                }
              }
              this.props.handler(e, obj.name, mandatory=="*"?true:false, "")
            }} maxHeight={200} >
            {dropDownData.map((dd, index) => (
                <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.value)} />
            ))}
            </SelectField>
          </Col>
        );

        case "url":
          return(
            <Col xs={12} md={3}>
              <SelectField fullWidth={true} floatingLabelText={description} value={typeof(obj.value)=="undefined"?"":obj.value} onChange={(event, key, value) => {
                // this.setState({
                //   value
                // })
                let e={
                  target:{
                    value
                  }
                }
                this.props.handler(e, obj.name, mandatory=="*"?true:false, "")
              }} maxHeight={200} >
              {dropDownData.map((dd, index) => (
                  <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.value)} />
              ))}
              </SelectField>
            </Col>
          );
      // case "multivaluelist":
      //   return(
      //     <Col xs={12} md={3}>
      //       <SelectField fullWidth={true} multiple={true} floatingLabelText={description}  />
      //     </Col>
      //   );
    }
  }
  render(){
    return (
      <div>
        {this.renderFields(this.props.obj)}
      </div>
    );
  }
}
