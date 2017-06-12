import React, { Component } from 'react';
// import {Switch,Route} from 'react-router-dom';

import Header from './common/Header';
import Footer from './common/Footer';
import UsageType from "./contents/masters/UsageType";
import PipeSize from "./contents/masters/PipeSize";
import CategoryType from "./contents/masters/CategoryType";

import router from "../router";


class App extends Component {
  render() {
    return (
      <div className="App">
          <Header/>
              {router}
          <Footer/>
      </div>
    );
  }
}

export default App;
