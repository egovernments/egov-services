import React, { Component } from 'react';
import AppBar from 'material-ui/AppBar';
import {brown500} from 'material-ui/styles/colors';

const styles = {
  mainLogo : {
    height: 60,
    borderRadius: '50%',
    marginTop:-6
  },

  rightIcon : {
    height:25,
    marginTop:12,
    marginRight:12
  }
}

const Logo = () => {
    return(<img src={require("../../images/main_logo.jpg")} style={styles.mainLogo} />);
}

const RightIcon = () => {
    return(<img src={require("../../images/logo@2x.png")}  style={styles.rightIcon} />);
}

class Header extends Component {
  render() {
    return (
      <div className="Header">
            <AppBar
              title="Financials"
              iconElementLeft={<Logo />}
              iconElementRight={<RightIcon/>}
            />
            {/*<div> Your userid is invalid, please check its format</div>*/}
          {/*  <div className="text-right" id="google_translate_element"></div>*/}
      </div>
    );
  }
}

export default Header;
