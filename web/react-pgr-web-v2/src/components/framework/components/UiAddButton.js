import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from 'react-router';

import RaisedButton from 'material-ui/RaisedButton';

class UiAddButton extends Component {
  constructor(props) {
    super(props);
  }

  add = e => {
    let { customUrl } = this.props;
    let { setRoute, match } = this.props;
    let { params } = match;
    // console.log(`/create/${params.moduleName}`+ (params.master && "/"+params.master));
    // debugger;
    if (customUrl) {
      // debugger;
      localStorage.setItem('returnUrl', 'search/' + params.moduleName + '/' + params.master + '/view');
      setRoute(customUrl);
    } else {
      setRoute(`/create/${params.moduleName}` + (params.master && '/' + params.master));
    }
  };

  render() {
    let { add } = this;
    return (
      <RaisedButton
        type="button"
        onClick={e => {
          add(e);
        }}
        primary={true}
        label="Add"
        icon={
          <i style={{ color: 'white' }} className="material-icons">
            add
          </i>
        }
      />
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
  setRoute: route => dispatch({ type: 'SET_ROUTE', route }),
});

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(UiAddButton));
