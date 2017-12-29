import React from "react";
import PropTypes from "prop-types";
import Checkbox from "material-ui/Checkbox";

const CheckboxUi = ({ options, defaultValue, onCheck, style = {} }) => {
  const renderCheckboxOptions = () => {
    return options.map((option, index) => {
      return (
        <Checkbox
          onCheck={e => onCheck(e, option.value)}
          label={option.label}
          key={index}
          checked={option.checked}
          style={style}
        />
      );
    });
  };

  return <div>{renderCheckboxOptions()}</div>;
};

CheckboxUi.propTypes = {
  options: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      value: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
        .isRequired,
      checked: PropTypes.bool.isRequired
    }).isRequired
  ),
  defaultValue: PropTypes.string,
  onCheck: PropTypes.func,
  style: PropTypes.object
};

export default CheckboxUi;
