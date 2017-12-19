import React from "react";
import Checkbox from "material-ui/Checkbox";

// not yet completed
const CheckboxUi = ({ options, defaultValue, onCheck, style = {} }) => {
  const renderCheckboxOptions = () => {
    return options.map((option, index) => {
      return (
        <Checkbox
          onCheck={onCheck}
          label={option.label}
          key={index}
          style={style}
        />
      );
    });
  };

  return <div>{renderCheckboxOptions()}</div>;
};

export default CheckboxUi;
