import React from "react";
import PropTypes from "prop-types";
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

DropDownUi.propTypes = {
  label: PropTypes.string,
  handleChange: PropTypes.func,
  selected: PropTypes.bool,
  options: PropTypes.array.isRequired
};

export default DropDownUi;
