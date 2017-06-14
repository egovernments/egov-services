import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom';
import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import IconButton from 'material-ui/IconButton';
import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';

import {Grid, Row, Col} from 'react-bootstrap';



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
    marginLeft: "5px",
    marginBottom: "10px"
  }
}

const Logo = () => {

  return (<img src={require("../../images/main_logo.jpg")} style={styles.mainLogo}/>);
}

const RightIcon = (props) => {
  if (props.token) {
    return (
      <div>

      <IconMenu
            iconButtonElement={<IconButton style={{backgroundColor:"white"}}><MoreVertIcon /></IconButton>}
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'left', vertical: 'top'}}
          >
            <MenuItem primaryText="Worklist"  leftIcon={<i className="material-icons">list</i>}></MenuItem>
            <MenuItem primaryText="Drafts" leftIcon={<i className="material-icons">drafts</i>}></MenuItem>
            <MenuItem primaryText="Notification" leftIcon={<i className="material-icons">notifications</i>}></MenuItem>
            <MenuItem primaryText="Edit Profile" leftIcon={<i className="material-icons">mode_edit</i>}></MenuItem>
            <MenuItem primaryText="Change Password" leftIcon={<i className="material-icons">https</i>}></MenuItem>
            <MenuItem primaryText="Feedback" leftIcon={<i className="material-icons">chat</i>}></MenuItem>
            <MenuItem primaryText="Report an issue" leftIcon={<i className="material-icons">bug_report</i>}></MenuItem>
            <MenuItem primaryText="Help" leftIcon={<i className="material-icons">help</i>}></MenuItem>
            <MenuItem primaryText="Sign Out" leftIcon={<i className="material-icons">lock</i>}></MenuItem>
     </IconMenu>

        <img src={require("../../images/logo@2x.png")} style={styles.rightIcon}/>

      </div>
    );
  } else {
    return(
      <img src={require("../../images/logo@2x.png")} style={styles.rightIcon}/>
    )

  }

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
        <AppBar title={< div ><Logo/> UAT Maharashtra Municipal Corporation < /div>} onLeftIconButtonTouchTap={this.handleToggle} iconElementLeft={this.props.token?<IconButton><i className="material-icons">menu</i></IconButton>:<div></div>} iconElementRight={< RightIcon token={this.props.token}/>}/>

        <Drawer containerClassName="side-bar" open={this.state.open}>
          {/*<div id="menu"></div>*/}
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

const mapStateToProps = state => ({
    labels: state.labels,
    appLoaded: state.common.appLoaded,
    appName: state.common.appName,
    currentUser: state.common.currentUser,
    redirectTo: state.common.redirectTo,
    token:state.common.token,
    pleaseWait: state.common.pleaseWait,
    isDialogOpen: state.form.dialogOpen,
    msg: state.form.msg
});

// this.props.appLoaded

const mapDispatchToProps = dispatch => ({
    onLoad: (payload, token) => dispatch({type: 'APP_LOAD', payload, token, skipTracking: true}),
    onRedirect: () => dispatch({type: 'REDIRECT'}),
    setLabels: payload => dispatch({type: 'LABELS', payload}),
    // onUpdateAuth: (value, key) => dispatch({type: 'UPDATE_FIELD_AUTH', key, value}),
    // onLogin: (username, password) => {
    //     dispatch({
    //         type: 'LOGIN',
    //         payload: []//agent.Auth.login(username, password)
    //     })
    // },
    updateError: (error) =>
        dispatch({
            type: 'UPDATE_ERROR',
            error
        }),
    setPleaseWait: (pleaseWait) =>
        dispatch({
            type: 'PLEASE_WAIT',
            pleaseWait
        }),
   toggleDailogAndSetText: (dailogState,msg) => {
          dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
        }
});


export default connect(mapStateToProps,mapDispatchToProps)(Header);

// import IconMenu from 'material-ui/IconMenu';
// import MenuItem from 'material-ui/MenuItem';
// import IconButton from 'material-ui/IconButton';
// import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
