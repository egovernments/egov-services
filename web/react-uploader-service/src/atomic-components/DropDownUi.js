import React from "react";
import SelectField from "material-ui/SelectField";
import MenuItem from "material-ui/MenuItem";

const DropDownUi = ({ label, options = [], name, selected, handleChange }) => {
  const renderSelectMenuItems = () => {
    return options.map((value, index) => {
      return <MenuItem key={index} value={value} primaryText={value} />;
    });
  };

  return (
    <SelectField
      floatingLabelText={label}
      value={selected}
      onChange={handleChange}
    >
      {renderSelectMenuItems()}
    </SelectField>
  );
};

export default DropDownUi;
