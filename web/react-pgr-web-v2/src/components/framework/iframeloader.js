import React, { Component } from 'react';

const styles = {
  container: {
    position: 'relative',
    height: 0,
    overflow: 'hidden',
    paddingBottom: '75%',
  },
  iframe: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
  },
};

export default class IframeLoader extends Component {
  componentDidMount() {
    const token = window.localStorage.token;
    const tenantId = window.localStorage.tenantId;
    this.ifr.onload = () => {
      this.ifr.contentWindow.postMessage({ token, tenantId }, '*');
    };
  }

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
      <div style={styles.container} className="col-lg-12">
        <iframe
          style={styles.iframe}
          ref={f => {
            this.ifr = f;
          }}
          frameBorder="0"
          src={source}
          allowfullscreen
        />
      </div>
    );
  }
}
