import React from 'react';
import './button.less';

export default class Button extends React.Component {
  render() {
      return (
        <a className="btn btn-default" href="#" role="button">{this.props.text}</a>
      );
  }
}
