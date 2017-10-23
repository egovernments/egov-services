import React, {Component} from 'react';
import { withGoogleMap, GoogleMap, Polygon } from 'react-google-maps';
import withScriptjs from 'react-google-maps/lib/async/withScriptjs';
import _ from "lodash";
import Api from '../.././../../api/api';

const removeZDepth = (latOrLonval) =>{
  return latOrLonval && parseFloat(`${latOrLonval}`.replace(/\d+\s/, "")) || undefined;
}

const convertCoordinatesToPolygonPaths = (coordinatesStr) =>{
   var coordinates = _.chunk(coordinatesStr.split(","), 2);
   return  _.transform(coordinates, function(latLngArry, coordinate) {
      if(coordinate[1] !== undefined && coordinate[0] !== undefined)
          latLngArry.push({lat:removeZDepth(coordinate[1]), lng:removeZDepth(coordinate[0])});
      return true;
    }, []);
}

const AsyncGoogleMap = _.flowRight(
  withScriptjs,
  withGoogleMap,
)(props => {

  const renderPolygons = props.wardsPolygons && props.wardsPolygons.map((ward, idx)=>{
      return ward.polygons.map((polygon)=>{
         return <Polygon options={ward.style}
          paths={polygon}></Polygon>;
      });
  });

  return (
    <GoogleMap
      ref={props.onMapMounted}
      defaultZoom={11}
      center={props.center}>
      {props.wardsPolygons && renderPolygons}
    </GoogleMap>
  )
});


export default class TypeDistributionReport extends Component{

    reteriveKMLFile = () => {
      this.setState({isFetchingData:true});
      var _this = this;
      Api.commonApiGet("https://raw.githubusercontent.com/egovernments/egov-services/master/core/egov-location/src/main/resources/gis/default/wards.kml",
        {}, {}).then(function(response) {
        //console.log('response', response);
        _this.extractManipulateWardsPath(response);
      }, function(err) {
        console.log('error', err);
      });
   }

  extractManipulateWardsPath=(text)=>{

    let parser = new DOMParser();
    let xmlDoc = parser.parseFromString(text,"text/xml");
    let placeMarks = xmlDoc.getElementsByTagName('Placemark');

    let wardsPolygons = [];
    
    for(let i=0; i<placeMarks.length;i++){
      let placeMark = placeMarks[i];
      let ward = { name : placeMark.getElementsByTagName("name")[0].innerHTML,
       style:{strokeColor: '#FF0000', strokeOpacity: 0.8, strokeWeight: 2, fillColor: '#FF0000', fillOpacity: 0.35},
       polygons:[]};
      let coordinatesStr = placeMark.getElementsByTagName("coordinates");
      for(let j=0;j<coordinatesStr.length;j++){
        let coordinateStr = coordinatesStr[j].innerHTML;
        ward.polygons.push(convertCoordinatesToPolygonPaths(coordinateStr));
      }
      wardsPolygons.push(ward);
    }
    this.setState({isFetchingData:false, wardsPolygons});
  }

  constructor(){
     super();
     this.state={
       isFetchingData : false
     }
   }

   componentDidMount(){
     this.reteriveKMLFile();
   }

   render(){

     return(
       <AsyncGoogleMap
         googleMapURL="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=AIzaSyDrxvgg2flbGdU9Fxn6thCbNf3VhLnxuFY"
         wardsPolygons = {this.state.wardsPolygons}
         loadingElement={
           <div style={{ height: `100%` }}>
           </div>
         }
         containerElement={
           <div style={{ height: `100%` }} />
         }
         mapElement={
           <div style={{ height: `100%` }} />
         }
         center={{ lat: 13.047374, lng: 79.9281215 }}
         onMapMounted={()=>{}}
       />
     )
   }

}
