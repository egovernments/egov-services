import React, { Component } from "react";
import { connect } from "react-redux";
import App from "../app-components/App";
import { loginUser } from "../actions/auth";

class AppContainer extends Component {
  // auto login temporary
  componentDidMount() {
    const username = "narasappa";
    const password = "demo";
    this.props.loginUser(username, password);
  }

  render() {
    return <App />;
  }
}

const mapStateToProps = state => ({
  authenticated: state.auth.authenticated,
  userInfo: state.auth.userInfo
});

const mapDispatchToProps = dispatch => ({
  loginUser: (username, password) => dispatch(loginUser(username, password))
});

export default connect(mapStateToProps, mapDispatchToProps)(AppContainer);
