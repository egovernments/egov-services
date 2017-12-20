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
          style={style}
        />
      );
    });
  };

  return <div>{renderCheckboxOptions()}</div>;
};

CheckboxUi.propTypes = {
  option: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string.isRequired,
      value: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
        .isRequired
    }).isRequired
  ),
  defaultValue: PropTypes.string,
  onCheck: PropTypes.func,
  style: PropTypes.object
};

export default CheckboxUi;
