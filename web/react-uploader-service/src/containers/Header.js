import React from "react";
import AppBar from "material-ui/AppBar";
import FlatButton from "material-ui/FlatButton";

const Header = () => {
  return (
    <header>
      <AppBar
        title="Data Uploader Service"
        iconElementRight={<FlatButton label="Home" />}
      />
    </header>
  );
};

export default Header;
