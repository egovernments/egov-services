import React, {Component} from 'react';
import MenuItem from 'material-ui/MenuItem';
import SelectField from 'material-ui/SelectField';

export const EGSelectField = (label, isRequired = false, isMultiSelect = false, isDisabled = false, value = 0, items, displayKey="name", cbOnSelected) => {    
    return (
        <SelectField
            className="custom-form-control-for-select"
            floatingLabelStyle={{"color":"#696969", "fontSize": "20px", "white-space": "nowrap"}}
            labelStyle={{"color": "#5F5C57"}}
            floatingLabelFixed={true}
            dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
            style={{"display": 'inline-block'}}
            errorStyle={{"float":"left"}}
            fullWidth={true}
            multiple={isMultiSelect}
            disabled={ isDisabled ?  true : false }
            floatingLabelText={<span>{label} {isRequired ? <span style={{"color": "#FF0000"}}> *</span> : ""} </span>}
            value={value}
            onChange={ cbOnSelected }
        >
            {
                items.map((item, i) => {
                    return <MenuItem key={i} value={i} primaryText={item[displayKey]} />
                })
            }
        </SelectField>
    )
}