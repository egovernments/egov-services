import React from "react";
import RaisedButton from "material-ui/RaisedButton";

const ButtonUi = ({
  label,
  icon = {},
  onClick,
  primary = false,
  style = {}
}) => {
  return (
    <RaisedButton
      style={style}
      primary={primary}
      type="button"
      label={label}
      onClick={onClick}
      icon={
        <i style={icon.style} className="material-icons">
          {icon.name}
        </i>
      }
    />
  );
};

export default ButtonUi;
