import React, { Component } from "react";
import App from "../app-components/App";
import { Api } from "../api";

class AppContainer extends Component {
  // auto login temporary
  componentDidMount() {
    const username = "narasappa";
    const password = "demo";
    Api()
      .loginUser(username, password)
      .then(response => {
        // console.log(response.data);
      });
  }

  render() {
    return <App />;
  }
}

export default AppContainer;
