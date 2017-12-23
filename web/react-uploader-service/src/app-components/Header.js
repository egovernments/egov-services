import React from "react";
import AppBar from "material-ui/AppBar";
import FlatButton from "material-ui/FlatButton";

const Header = () => {
  return (
    <header>
      <AppBar
        title="Data Upload Service"
        iconElementRight={<FlatButton href="/" label="Create Job" />}
      />
    </header>
  );
};

export default Header;
