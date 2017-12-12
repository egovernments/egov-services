/**
 * Created by narendrasisodiya on 12/12/17.
 */

import React, { Component } from 'react';
import UiTextField from './components/UiTextField';

var ComponentMap = {
  UiTextField: {
    comp: UiTextField,
    props: {
      getVal: function() {
        return 'dummy';
      },
      tabIndex: 0,
      ui: 'google',
      item: {
        name: 'name',
        jsonPath: 'collectionPoints[0].name',
        label: 'Collection Point Name',
        type: 'text',
        isRequired: true,
        isDisabled: false,
        maxLength: 128,
        minLength: 1,
        patternErrorMsg: '',
      },
      fieldErrors: {},
    },
  },
};

export default class ComponentLoader extends Component {
  render() {
    console.log('componentName', this.props.match.params.componentName);
    var Comp = ComponentMap[this.props.match.params.componentName].comp;
    var props = ComponentMap[this.props.match.params.componentName].props;
    return (
      <div>
        <h1>Welcome to Component Loader</h1>
        <textarea name="" id="" cols="30" rows="10" />
        <Comp {...props} />
      </div>
    );
  }
}
