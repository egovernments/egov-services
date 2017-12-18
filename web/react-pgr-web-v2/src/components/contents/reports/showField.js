import React, { Component } from 'react';
import { Grid, Row, Col, Table, DropdownButton } from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import DatePicker from 'material-ui/DatePicker';
import Checkbox from 'material-ui/Checkbox';
import { translate } from '../../common/common';
import areIntlLocalesSupported from 'intl-locales-supported';

export default class ShowField extends Component {
  constructor(props) {
    super(props);
  }

  renderFields = obj => {
    let des = translate(obj.label);
    // let mandatory = obj.isMandatory ? " *" : "";
    // let description = des + mandatory;
    let description = des;

    let dropDownData = [];

    // obj.defaultValue=typeof(obj.defaultValue)=="object"?obj.defaultValue:{};
    if (typeof obj.defaultValue == 'object') {
      for (var variable in obj.defaultValue) {
        // console.log(variable);

        dropDownData.push({
          key: variable,
          // value:typeof(searchForm[variable])=="object"?new Date(searchForm[variable]).getTime():searchForm[variable]
          value: obj.defaultValue[variable],
        });
      }
    }

    // if (obj.type=="checkbox") {
    //   this.props.handler({target: {value: obj.defaultValue}}, obj.name, obj.isMandatory ? true : false, "");
    // }

    // console.log(obj);
    //console.log('came to renderfields',obj.code);

    // console.log(dropDownData);
    // let DateTimeFormat;
    // if (areIntlLocalesSupported(['fr', 'fa-IR'])) {
    //   DateTimeFormat = global.Intl.DateTimeFormat;
    // }
    switch (obj.type) {
      case 'string':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <TextField
              value={this.props.value}
              className="custom-form-control-for-textfield"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              onChange={e => this.props.handler(e, obj.name, obj.isMandatory ? true : false, '')}
            />
          </Col>
        );
      case 'number':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <TextField
              value={this.props.value}
              className="custom-form-control-for-textfield"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              onChange={e => this.props.handler(e, obj.name, obj.isMandatory ? true : false, /^[+-]?\d+(\.\d+)?$/)}
            />
          </Col>
        );
      case 'date':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <DatePicker
              className="custom-form-control-for-textfield"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              value={typeof obj.value == 'object' ? obj.value : {}}
              onChange={(first, object) => {
                let e = {
                  target: {
                    value: object,
                  },
                };
                this.props.handler(e, obj.name, obj.isMandatory ? true : false, '');
              }}
            />
            {/*<DatePicker fullWidth={true} DateTimeFormat={DateTimeFormat} locale="fr" floatingLabelText={<span>{description} <span style={{"color": "#FF0000"}}>{obj.isMandatory ? " *" : ""}</span></span>}  />*/}
          </Col>
        );
      case 'epoch':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <DatePicker
              className="custom-form-control-for-textfield"
              id={obj.label.split('.').join('-')}
              autoOk={true}
              fullWidth={true}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              value={obj.value ? obj.value : ''}
              errorText={this.props.dateField ? (obj.name === this.props.dateField ? this.props.dateError : '') : ''}
              formatDate={date => {
                let dateObj = new Date(date);
                let year = dateObj.getFullYear();
                let month = dateObj.getMonth() + 1;
                let dt = dateObj.getDate();
                dt = dt < 10 ? '0' + dt : dt;
                month = month < 10 ? '0' + month : month;
                return dt + '-' + month + '-' + year;
              }}
              onChange={(first, object) => {
                let e = {
                  target: {
                    value: object,
                  },
                };
                this.props.handler(e, obj.name, obj.isMandatory ? true : false, '');
              }}
            />
            {/*<DatePicker fullWidth={true} DateTimeFormat={DateTimeFormat} locale="fr" floatingLabelText={<span>{description} <span style={{"color": "#FF0000"}}>{obj.isMandatory ? " *" : ""}</span></span>}  />*/}
          </Col>
        );
      case 'singlevaluelist':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <SelectField
              className="custom-form-control-for-select"
              hintText="Select"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              dropDownMenuProps={{
                animated: false,
                targetOrigin: { horizontal: 'left', vertical: 'bottom' },
              }}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              value={typeof obj.value == 'undefined' ? '' : obj.value}
              onChange={(event, key, value) => {
                // this.setState({
                //   value
                // })
                let e = {
                  target: {
                    value,
                  },
                };
                this.props.handler(e, obj.name, obj.isMandatory ? true : false, '');
              }}
              maxHeight={200}
            >
              <MenuItem value="" primaryText="Select" />
              {dropDownData.map((dd, index) => <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.value)} />)}
            </SelectField>
          </Col>
        );

      case 'url':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <SelectField
              className="custom-form-control-for-select"
              hintText="Select"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              dropDownMenuProps={{
                animated: false,
                targetOrigin: { horizontal: 'left', vertical: 'bottom' },
              }}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              value={typeof obj.value == 'undefined' ? '' : obj.value}
              onChange={(event, key, value) => {
                // this.setState({
                //   value
                // })
                let e = {
                  target: {
                    value,
                  },
                };
                this.props.handler(e, obj.name, obj.isMandatory ? true : false, '');
              }}
              maxHeight={200}
            >
              <MenuItem value="" primaryText="Select" />
              {dropDownData.map((dd, index) => <MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.value)} />)}
            </SelectField>
          </Col>
        );

      case 'multivaluelist':
        return (
          <Col xs={12} sm={4} md={3} lg={3}>
            <SelectField
              className="custom-form-control-for-select"
              hintText="Select"
              id={obj.label.split('.').join('-')}
              fullWidth={true}
              multiple={true}
              dropDownMenuProps={{
                animated: false,
                targetOrigin: { horizontal: 'left', vertical: 'bottom' },
              }}
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              value={typeof obj.value == 'undefined' ? '' : obj.value}
              onChange={(event, key, value) => {
                // this.setState({
                //   value
                // })
                let e = {
                  target: {
                    value,
                  },
                };
                this.props.handler(e, obj.name, obj.isMandatory ? true : false, '');
              }}
              maxHeight={200}
            >
              {dropDownData.map((dd, index) => (
                <MenuItem
                  insetChildren={true}
                  checked={obj.value && obj.value.indexOf(dd.key) > -1 ? true : false}
                  value={translate(dd.key)}
                  key={index}
                  primaryText={translate(dd.value)}
                />
              ))}
            </SelectField>
          </Col>
        );

      case 'checkbox':
        return (
          <Col xs={12} md={3}>
            <Checkbox
              id={obj.label.split('.').join('-')}
              label={
                <span>
                  {description} <span style={{ color: '#FF0000' }}>{obj.isMandatory ? ' *' : ''}</span>
                </span>
              }
              checked={obj.value ? obj.value : false}
              onCheck={e => this.props.handler({ target: { value: e.target.checked } }, obj.name, obj.isMandatory ? true : false, '')}
            />
          </Col>
        );

      default:
        return <div />;
    }
  };
  render() {
    return this.renderFields(this.props.obj);
  }
}

// case "multivaluelist":
//   return(
//     <Col xs={12} md={3}>
//       <SelectField fullWidth={true} multiple={true} floatingLabelText={<span>{description} <span style={{"color": "#FF0000"}}>{obj.isMandatory ? " *" : ""}</span></span>}  />
//     </Col>
//   );
