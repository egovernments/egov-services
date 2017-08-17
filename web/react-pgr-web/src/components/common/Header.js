import React, {Component} from 'react';
import {connect} from 'react-redux';
// import {Link} from 'react-router-dom';
import AppBar from 'material-ui/AppBar';
import Drawer from 'material-ui/Drawer';
import IconMenu from 'material-ui/IconMenu';
import MenuItem from 'material-ui/MenuItem';
import IconButton from 'material-ui/IconButton';
// import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';
// import PropTypes from 'prop-types'
import { withRouter } from 'react-router';
// import Api from '../../api/api';
import {logo, tenantName} from './temp/local';
import {getTitleCase} from '../framework/utility/utility';
import $ from 'jquery';
// import {history} from 'react-router-dom'

// import {Grid, Row, Col} from 'react-bootstrap';


import CustomMenu from './CustomMenu';

// var base='/';
var tenantContent = {
  "details" : {
    "panavel" : {
      "name" : "Panvel",
      "logo" : "./temp/images/panvelLogo.jpg"
    },
    "default" : {
      "name" : "My City",
      "logo" : "./temp/images/yourCity.jpg"
    }
  }
};

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
    marginRight: "10px",
    marginBottom: "10px"
  }
}

const getTenantId = () => {
  if(localStorage.getItem("tenantId")) {
    return localStorage.getItem("tenantId");
  }

  return window.location.hash.split("#/")[1];
}


const getThemefile = function(tenantId, cb) {
    $.ajax({
        url: "https://raw.githubusercontent.com/abhiegov/test/master/tenantDetails.json",
        success: function(res) {
            var tenantContext = JSON.parse(res)["details"][tenantId];
            cb(tenantContext);
        },
        error: function() {
            var tenantContext = tenantContent[tenantId];
            cb(tenantContext);
        }
    })
}

const Logo = (props) => {
  /*if(props.tenantInfo && props.tenantInfo[0] && props.tenantInfo[0].imageId) {

  } else */
  if(props.tenantContext && props.tenantContext.logo) {
    return (<img width="64" src={props.tenantContext.logo} style={styles.mainLogo} alt="logo"/>);
  } else {
    if(logo[getTenantId()]) {
        return (<img width="64" src={logo[getTenantId()]} style={styles.mainLogo} alt="logo"/>);
    } else if(logo["default"]) {
        return (<img width="64" src={logo["default"]} style={styles.mainLogo} alt="logo"/>);
    } else {
        return (<img width="64" src={require("../../images/headerLogo.png")} style={styles.mainLogo} alt="logo"/>);
    }
  }
}

const getTitle = (tenantInfo, tenantContext) => {
    if(tenantContext) {
        return tenantContext.name;
    } else
        return tenantInfo && tenantInfo[0] && tenantInfo[0].city && tenantInfo[0].city.name ? getTitleCase(tenantInfo[0].city.name) : (tenantName[getTenantId()] || "My City");
}

const RightIcon = (props) => {
  //console.log(props);
  if (props.token) {
    return (
      <div>
      <i onClick={()=>{
        if(localStorage.getItem("token"))
          props.setRoute("/prd/dashboard");
        else
          props.setRoute("/");
      }} className="material-icons" style={{"color":"white", "cursor": "pointer"}}>home</i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <img src={require("../../images/logo@2x.png")} style={styles.rightIcon} alt="right icon"/>
      <span style={{color:"white"}}>{window.localStorage.getItem("userRequest")?JSON.parse(window.localStorage.getItem("userRequest")).name:""}</span>
      <IconMenu
            iconButtonElement={<IconButton style={{color:"white"}}><i className="material-icons">more_vert</i></IconButton>}
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'left', vertical: 'top'}}
          >
          {/*<MenuItem primaryText="Worklist"  leftIcon={<i className="material-icons">list</i>}></MenuItem>
            <MenuItem primaryText="Drafts" leftIcon={<i className="material-icons">drafts</i>}></MenuItem>
            <MenuItem primaryText="Notification" leftIcon={<i className="material-icons">notifications</i>}></MenuItem>*/
          /*  <MenuItem primaryText="Edit Profile" containerElement={<Link to={`/profileEdit`}></Link>}  leftIcon={<i className="material-icons">mode_edit</i>}></MenuItem>*/
          /*  <MenuItem primaryText="Change Password" leftIcon={<i className="material-icons">fingerprint</i>}></MenuItem>
            <MenuItem primaryText="Feedback" leftIcon={<i className="material-icons">chat</i>}></MenuItem>
            <MenuItem primaryText="Report an issue" leftIcon={<i className="material-icons">bug_report</i>}></MenuItem>
            <MenuItem primaryText="Help" leftIcon={<i className="material-icons">help</i>}></MenuItem>
            */}
            <MenuItem primaryText="Sign Out" onClick={(e)=>{
                //  Api.commonApiPost("/user/_logout", {access_token: localStorage.getItem('auth')}).then(function(response)
                //  {
                //    var locale = localStorage.getItem('locale');
                //    localStorage.clear();
                //    localStorage.setItem('locale',locale);
                //    props.logout();
                //  },function(err) {
                //    console.log(err);
                // });
                document.title = "Dashboard";
                var locale = localStorage.getItem('locale');
                var tenantId=localStorage.getItem('tenantId');
                localStorage.clear();
                localStorage.setItem('locale',locale);
                props.logout(tenantId);
            }} leftIcon={<i className="material-icons">lock</i>}></MenuItem>
     </IconMenu>


      </div>
    );
  } else if(window.location.hash === "#/") {
    return(
      <img src={require("../../images/logo@2x.png")} style={styles.rightIcon} alt="right icon"/>
    )
  } else {
    return(
      <div>
        <i onClick={()=>{
          props.setRoute("/");
        }} className="material-icons"
           style={{"color":"white", "cursor": "pointer"}}>home</i>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <img src={require("../../images/logo@2x.png")} style={styles.rightIcon} alt="right icon"/>
      </div>
    )

  }

}

class Header extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      menuItems: [],
      reRender: false,
      tenantContext: {}
    };
  }

  handleToggle = () => {
    var self = this;
    self.props.handleToggle(!self.props.showMenu);
  };

  initDat() {

  }

  componentDidMount() {
    var self = this;
    getThemefile(getTenantId(), function(context) {
        self.setState({
            reRender: true,
            tenantContext: context
        });
    });

    //When api ready asssign api response to menuItems
    let menuItems=[];
    this.setState({menuItems})
  }

  render() {
    let {tenantContext} = this.state;
    return (
      <div className="Header">
        <AppBar title={<div><Logo tenantInfo={this.props.tenantInfo} tenantContext={tenantContext}/> {getTitle(this.props.tenantInfo, tenantContext)} </div>}
                onLeftIconButtonTouchTap={this.handleToggle}
                iconElementLeft={this.props.token && this.props.currentUser.type !== "CITIZEN" ? <IconButton><i className="material-icons">menu</i></IconButton> : <div></div>}
                iconElementRight={< RightIcon showHome={this.props.showHome} token={this.props.token} logout={this.props.logout} setRoute={this.props.setRoute}/>}/>

        <Drawer containerClassName="side-bar" open={this.props.showMenu || false} >
         {this.props.actionList.length>0 && <CustomMenu menuItems={this.state.menuItems} actionList={this.props.actionList} />}
        </Drawer>
      </div>
    );
  }
}

const mapStateToProps = state => ({
    labels: state.labels,
    appLoaded: state.common.appLoaded,
    appName: state.common.appName,
    currentUser: state.common.currentUser,
    // redirectTo: state.common.redirectTo,
    token:state.common.token,
    pleaseWait: state.common.pleaseWait,
    // isDialogOpen: state.form.dialogOpen,
    // msg: state.form.msg,
    showMenu: state.common.showMenu,
    actionList:state.common.actionList,
    showHome: state.common.showHome,
    tenantInfo: state.common.tenantInfo

});

// this.props.appLoaded

const mapDispatchToProps = dispatch => ({
    setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
    handleToggle: (showMenu) => dispatch({type: 'MENU_TOGGLE', showMenu}),
    // onLoad: (payload, token) => dispatch({type: 'APP_LOAD', payload, token, skipTracking: true}),
    // onRedirect: () => dispatch({type: 'REDIRECT'}),
    setLabels: payload => dispatch({type: 'LABELS', payload}),
    logout:(tenantId)=>dispatch({type:'LOGOUT',tenantId}),
    // onUpdateAuth: (value, key) => dispatch({type: 'UPDATE_FIELD_AUTH', key, value}),
    // onLogin: (username, password) => {
    //     dispatch({
    //         type: 'LOGIN',
    //         payload: []//agent.Auth.login(username, password)
    //     })
    // },
  //   updateError: (error) =>
  //       dispatch({
  //           type: 'UPDATE_ERROR',
  //           error
  //       }),
  //   setPleaseWait: (pleaseWait) =>
  //       dispatch({
  //           type: 'PLEASE_WAIT',
  //           pleaseWait
  //       }),
  //  toggleDailogAndSetText: (dailogState,msg) => {
  //         dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  //       }
});


export default withRouter(connect(mapStateToProps,mapDispatchToProps)(Header));

// import IconMenu from 'material-ui/IconMenu';
// import MenuItem from 'material-ui/MenuItem';
// import IconButton from 'material-ui/IconButton';
// import MoreVertIcon from 'material-ui/svg-icons/navigation/more-vert';



// WEBPACK FOOTER //
// ./src/components/common/Header.js
