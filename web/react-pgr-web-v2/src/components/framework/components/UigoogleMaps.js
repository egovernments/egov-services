import React, { Component } from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';

/* global google */
import _ from 'lodash';
import { withGoogleMap, GoogleMap, Marker } from 'react-google-maps';
import withScriptjs from 'react-google-maps/lib/async/withScriptjs';
import SearchBox from 'react-google-maps/lib/places/SearchBox';

var axios = require('axios');
var _this;
var addressHolder;

const INPUT_STYLE = {
  boxSizing: `border-box`,
  MozBoxSizing: `border-box`,
  border: `1px solid transparent`,
  width: `240px`,
  height: `32px`,
  marginTop: `9px`,
  padding: `0 12px`,
  borderRadius: `1px`,
  boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
  fontSize: `14px`,
  outline: `none`,
  textOverflow: `ellipses`,
};
/*
 * This is the modify version of:
 * https://developers.google.com/maps/documentation/javascript/examples/event-arguments
 *
 * Loaded using async loader.
 */
const AsyncGettingStartedExampleGoogleMap = _.flowRight(withScriptjs, withGoogleMap)(props => (
  <GoogleMap options={{ scrollwheel: false }} ref={props.onMapMounted} defaultZoom={11} center={props.center} onBoundsChanged={props.onBoundsChanged}>
    <SearchBox
      ref={props.onSearchBoxMounted}
      bounds={props.bounds}
      controlPosition={google.maps.ControlPosition.TOP_LEFT}
      onPlacesChanged={props.onPlacesChanged}
      inputPlaceholder="Search"
      inputStyle={INPUT_STYLE}
    />
    {props.markers.map((marker, index) => <Marker position={marker.position} key={index} />)}
  </GoogleMap>
));

class SimpleMap extends Component {
  constructor(props) {
    super(props);
    this.state = {
      zoom: 10,
      center: { lat: 19.076, lng: 72.8777 },
      markers: [
        {
          position: { lat: 19.076, lng: 72.8777 },
        },
      ],
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.lat && nextProps.lng) {
      this.setState({ center: { lat: nextProps.lat, lng: nextProps.lng } });
      this.setState({
        markers: [{ position: { lat: nextProps.lat, lng: nextProps.lng } }],
      });
    }
  }

  handleMapMounted = this.handleMapMounted.bind(this);
  handleBoundsChanged = this.handleBoundsChanged.bind(this);
  handleSearchBoxMounted = this.handleSearchBoxMounted.bind(this);
  handlePlacesChanged = this.handlePlacesChanged.bind(this);

  handleMapMounted(map) {
    this._map = map;
  }

  handleBoundsChanged() {
    let tempArray = [];
    tempArray.push(this._map.getCenter());

    // Add a marker for each place returned from search bar
    const markers = tempArray.map(place => ({
      position: this._map.getCenter(),
    }));

    // Set markers; set map center to first search result
    const mapCenter = markers.length > 0 ? markers[0].position : this.state.center;

    this.setState({
      center: mapCenter,
      markers,
    });
  }

  handleSearchBoxMounted(searchBox) {
    this._searchBox = searchBox;
  }

  handlePlacesChanged() {
    const places = this._searchBox.getPlaces();

    // Add a marker for each place returned from search bar
    const markers = places.map(place => ({
      position: place.geometry.location,
    }));

    // Set markers; set map center to first search result
    const mapCenter = markers.length > 0 ? markers[0].position : this.state.center;

    this.setState({
      center: mapCenter,
      markers,
    });
  }

  render() {
    return (
      <AsyncGettingStartedExampleGoogleMap
        googleMapURL="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=AIzaSyDrxvgg2flbGdU9Fxn6thCbNf3VhLnxuFY"
        loadingElement={<div style={{ height: `100%` }} />}
        containerElement={<div style={{ height: `100%` }} />}
        mapElement={<div style={{ height: `100%` }} />}
        center={this.state.center}
        onMapMounted={this.handleMapMounted}
        onBoundsChanged={() => {
          this.handleBoundsChanged();
          this.props.handler(this.state.center.lat(), this.state.center.lng());
        }}
        onSearchBoxMounted={this.handleSearchBoxMounted}
        bounds={this.state.bounds}
        onPlacesChanged={() => {
          this.handlePlacesChanged();
          this.props.handler(this.state.center.lat(), this.state.center.lng());
        }}
        markers={this.state.markers}
      />
    );
  }
}

export default class UigoogleMaps extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
    };

    this.handleOpen = () => {
      this.setState({ open: true });
    };

    this.handleClose = () => {
      this.setState({ open: false });
    };
  }

  getAddress = (lat, lng) => {
    console.log(addressHolder);
    let self = this;
    axios.post('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng + '&sensor=true').then(function(response) {
      addressHolder = response.data.results[0] ? response.data.results[0].formatted_address : '';
      console.log(addressHolder);
    });
    console.log(addressHolder);
  };

  renderMaps = item => {
    switch (this.props.ui) {
      case 'google':
        const actions = [<FlatButton label="Select" primary={true} onClick={this.handleClose} />];
        return (
          <div>
            <TextField
              floatingLabelFixed={true}
              floatingLabelText={
                <span>
                  {item.label} <span style={{ color: '#FF0000', fontSize: '18px' }}>{item.isRequired ? ' *' : ''}</span>
                </span>
              }
              style={{ width: '70%', padding: '0px' }}
              textareaStyle={{ color: 'black' }}
              className="custom-form-control-for-textarea"
              disabled={true}
              multiLine={true}
              value={this.props.getVal(item.jsonPathAddress)}
            />
            <FlatButton
              id={item.label.split('.').join('-')}
              style={{ width: '20%' }}
              icon={<img src="./temp/images/map_logo.png" height="37px" width="30%" />}
              type={item.uiType || 'button'}
              primary={typeof item.primary != 'undefined' ? item.primary : true}
              secondary={item.secondary || false}
              onClick={this.handleOpen}
              disabled={item.isDisabled ? true : false}
            />
            <Dialog title="Google Maps" style={{ width: '90%', height: '90%' }} actions={actions} modal={true} open={this.state.open}>
              <div style={{ width: '100%', height: 400 }}>
                <SimpleMap
                  markers={[]}
                  handler={(lat, lng) => {
                    this.getAddress(lat, lng);
                    let self = this;
                    this.props.handler(
                      { target: { value: lng } },
                      item.jsonPathLng,
                      item.isRequired ? true : false,
                      '',
                      item.requiredErrMsg,
                      item.patternErrMsg
                    );
                    this.props.handler(
                      { target: { value: lat } },
                      item.jsonPathLat,
                      item.isRequired ? true : false,
                      '',
                      item.requiredErrMsg,
                      item.patternErrMsg
                    );
                    console.log(item);
                    axios
                      .post('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + lng + '&sensor=true')
                      .then(function(response) {
                        addressHolder = response.data.results[0] ? response.data.results[0].formatted_address : '';
                        console.log(addressHolder);
                        self.props.handler(
                          { target: { value: addressHolder } },
                          item.jsonPathAddress,
                          item.isRequired ? true : false,
                          '',
                          item.requiredErrMsg,
                          item.patternErrMsg
                        );
                      });
                  }}
                />
              </div>
            </Dialog>
          </div>
        );
    }
  };

  render() {
    return this.renderMaps(this.props.item);
  }
}
