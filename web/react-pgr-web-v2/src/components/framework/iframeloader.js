import React, { Component } from 'react';

class Iframe extends React.Component {
  shouldComponentUpdate() {
    return false;
  }
  render() {
    const { source, height, width } = this.props;
    return (
      <div>
        <iframe frameBorder="0" src={source} height={height} width={width} />
      </div>
    );
  }
}

export default class IframeLoader extends Component {
  // very hacky method
  getIframeUrl = () => {
    const paramsString = this.props.location.search;
    const hash = this.props.location.hash;
    const params = new URLSearchParams(paramsString);
    const url = params.get('url');
    return url + hash;
  };

  render() {
    const source = this.getIframeUrl();
    return (
      <div className="col-lg-12">
        <Iframe source={source} width="1300" height="1300" />
      </div>
    );
  }
}
