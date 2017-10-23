import React, {Component} from 'react';
import { withGoogleMap, GoogleMap } from 'react-google-maps';
import withScriptjs from 'react-google-maps/lib/async/withScriptjs';
import _ from "lodash";

const AsyncGoogleMap = _.flowRight(
  withScriptjs,
  withGoogleMap,
)(props => (
  <GoogleMap
    ref={props.onMapMounted}
    defaultZoom={11}
    center={props.center}>
  </GoogleMap>
));


export default class TypeDistributionReport extends Component{

   constructor(){
     super()
   }

   render(){

     return(
       <AsyncGoogleMap
         googleMapURL="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=AIzaSyDrxvgg2flbGdU9Fxn6thCbNf3VhLnxuFY"
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
         center={{ lat: -34.397, lng: 150.644 }}
         onMapMounted={()=>{}}
       />
     )
   }

}
