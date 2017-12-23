import React from "react";
import AppBar from "material-ui/AppBar";
import FlatButton from "material-ui/FlatButton";

const headerStyles = {
  position: "fixed",
  width: "100%",
  top: "0px",
  zIndex: "10"
};

const Header = () => {
  return (
    <header style={headerStyles}>
      <AppBar
        title="Data Upload Service"
        iconElementRight={<FlatButton href="/" label="Create Job" />}
      />
    </header>
  );
};

export default Header;
