import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import IconButton from 'material-ui/IconButton';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';

import CustomMenu from './CustomMenu';

// import {brown500} from 'material-ui/styles/colors';
// import { stack as Menu } from 'react-burger-menu'

// import '../../styles/jquery.multilevelpushmenu.min.css';
// import './jquery.multilevelpushmenu.min.js';
//
// import './custom-menu.js';

const styles = {
  mainLogo: {
    height: 60,
    borderRadius: '50%',
    marginTop: -6
  },

  rightIcon: {
    height: 25,
    marginTop: 12,
    marginRight: 12
  }
}

const Logo = () => {
  return (<img src={require("../../images/main_logo.jpg")} style={styles.mainLogo}/>);
}

const RightIcon = () => {
  return (
    <div>
      <img src={require("../../images/logo@2x.png")} style={styles.rightIcon}/>

    </div>
  );
}

class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      menuItems: []
    };
  }

  handleToggle = () => this.setState({
    open: !this.state.open
  });

  componentDidMount()
  {
    //When api ready asssign api response to menuItems
    let menuItems = [
      {
        title: 'Application',
        id: 'menuID',
        icon: 'fa fa-reorder',
        items: [
          {
            name: 'Lease And Agreement',
            id: 'itemID',
            icon: 'fa fa-laptop',
            link: '#',
            items: [
              {
                title: 'Land And Lease',
                icon: 'fa fa-laptop',
                items: [
                  {
                    name: 'Transactions',
                    icon: 'fa fa-desktop',
                    link: '#',
                    items: [
                      {
                        title: 'Transactions',
                        icon: 'fa fa-desktop',
                        link: '#',
                        items: [
                          {
                            name: 'Search Assets',
                            icon: 'fa fa-phone',
                            link: './app/search-assets/search-asset.html'
                          }, {
                            name: 'Search Agreements',
                            icon: 'fa fa-phone',
                            link: './app/search-agreement/search-agreement.html'
                          }
                        ]
                      }
                    ]
                  }, {
                    name: 'Reports',
                    icon: 'fa fa-desktop',
                    link: '#',
                    items: [
                      {
                        title: 'Reports',
                        icon: 'fa fa-desktop',
                        link: '#',
                        items: [
                          {
                            name: 'DCB Drill down reports',
                            icon: 'fa fa-phone',
                            link: './app/reports/DCB-Drill-down-report.html'
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
    this.setState({menuItems})
  }

  render() {
    return (
      <div className="Header">
        <AppBar title={< div > UAT Maharashtra Municipal Corporation < /div>} onLeftIconButtonTouchTap={this.handleToggle} iconElementRight={< RightIcon />}/>

        <Drawer containerClassName="side-bar" open={this.state.open}>
          <CustomMenu menuItems={this.state.menuItems}/>
        </Drawer>

        {/*
            <MenuItem>
              <Link to="/PropertyTaxSearch">
                  Property Tax
              </Link>
            </MenuItem>
            <div id="menu" className="homepage" style={{minHeight: "242px", width: "250px", height: "242px", minWidth: "250px"}}></div>
            <Menu isOpen={ true }>
               <a id="home" className="menu-item" href="/">Home</a>
               <a id="about" className="menu-item" href="/about">About</a>
               <a id="contact" className="menu-item" href="/contact">Contact</a>
               <a onClick={ this.showSettings } className="menu-item--small" href="">Settings</a>
           </Menu>

          */}
        {/*<div> Your userid is invalid, please check its format</div>*/}
        {/*  <div className="text-right" id="google_translate_element"></div>*/}
      </div>
    );
  }
}

export default Header;

// import IconMenu from 'material-ui/IconMenu';
// import MenuItem from 'material-ui/MenuItem';
// import IconButton from 'material-ui/IconButton';
// import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
