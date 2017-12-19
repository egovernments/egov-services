import React from "react";
import Checkbox from "material-ui/Checkbox";

const CheckboxUi = ({ options, defaultValue, onCheck, style = {} }) => {
  const renderCheckboxOptions = () => {
    return options.map((option, index) => {
      return (
        <Checkbox
          onCheck={e => onCheck(e, option.value)}
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
