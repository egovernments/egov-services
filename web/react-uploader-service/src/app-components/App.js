import React, { Component } from "react";
import Header from "../containers/Header";
import Footer from "../containers/Footer";
import Main from "./Router";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Header />
        <Main />
        <Footer />
      </div>
    );
  }
}

export default App;
