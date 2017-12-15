import React, { Component } from 'react';
import MenuItem from 'material-ui/MenuItem';
import SelectField from 'material-ui/SelectField';

export default class KPISelectField extends Component {
  handleChange = (event, index, values) => {
    this.props.onItemsSelected(index, values, this.props.label);
  };

  render() {
    return (
      <SelectField
        className="custom-form-control-for-select"
        floatingLabelStyle={{
          color: '#696969',
          fontSize: '20px',
          'white-space': 'nowrap',
        }}
        labelStyle={{ color: '#5F5C57' }}
        floatingLabelFixed={true}
        dropDownMenuProps={{
          animated: false,
          targetOrigin: { horizontal: 'left', vertical: 'bottom' },
        }}
        style={{ display: 'inline-block' }}
        errorStyle={{ float: 'left' }}
        fullWidth={true}
        hintText="Please Select"
        multiple={this.props.multiple}
        disabled={this.props.disabled ? true : false}
        floatingLabelText={
          <span>
            {this.props.label} {this.props.mandatory ? <span style={{ color: '#FF0000' }}> *</span> : ''}{' '}
          </span>
        }
        value={this.props.value}
        onChange={this.handleChange}
        maxHeight={200}
      >
        {this.props.items.map((item, i) => {
          return <MenuItem key={i} value={i} primaryText={item[this.props.displayKey]} />;
        })}
      </SelectField>
    );
  }
}
