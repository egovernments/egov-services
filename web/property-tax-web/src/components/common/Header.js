import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import {brown500} from 'material-ui/styles/colors';
import '../../styles/index.css'

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
  constructor(props) {
   super(props);
   this.state = {open: false};
  }

  handleToggle = () => this.setState({open: !this.state.open});


  render() {
    return (
      <div className="Header" >
            <AppBar
              title="Property Tax"
              onTitleTouchTap={this.handleToggle}
              iconElementLeft={<Logo />}
              iconElementRight={<RightIcon/>}
            />

            <Drawer containerClassName="side-bar" open={this.state.open}>
              <MenuItem>
                <Link to="/PropertyTaxSearch">
                    Property Tax Search
                </Link></MenuItem>

            </Drawer>
            {/*<div> Your userid is invalid, please check its format</div>*/}
          {/*  <div className="text-right" id="google_translate_element"></div>*/}
      </div>
    );
  }
}

export default Header;
