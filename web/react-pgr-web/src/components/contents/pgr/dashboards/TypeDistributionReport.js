import React, {Component} from 'react';
import { withGoogleMap, GoogleMap, Polygon } from 'react-google-maps';
import withScriptjs from 'react-google-maps/lib/async/withScriptjs';
import _ from "lodash";

const triangleCoords = [
  {lat: 25.774, lng: -80.190},
  {lat: 18.466, lng: -66.118},
  {lat: 32.321, lng: -64.757},
  {lat: 25.774, lng: -80.190}
];

const removeZDepth = (latOrLonval) =>{
  return latOrLonval && parseInt(`${latOrLonval}`.replace(/\d+\s/, "")) || undefined;
}

const convertCoordinatesToPolygonPaths = (coordinatesStr) =>{
   var coordinates = _.chunk(coordinatesStr.split(","), 2);
   return  _.transform(coordinates, function(latLngArry, coordinate) {
      if(coordinate[1] !== undefined && coordinate[0] !== undefined)
          latLngArry.push({lat:removeZDepth(coordinate[0]), lng:removeZDepth(coordinate[1])});
      return true;
    }, []);
}

const AsyncGoogleMap = _.flowRight(
  withScriptjs,
  withGoogleMap,
)(props => (
  <GoogleMap
    ref={props.onMapMounted}
    defaultZoom={11}
    center={props.center}>

    <Polygon options={{strokeColor: '#FF0000', strokeOpacity: 0.8, strokeWeight: 2, fillColor: '#FF0000', fillOpacity: 0.35}}
      paths={props.polygonPath}></Polygon>

  </GoogleMap>
));


export default class TypeDistributionReport extends Component{

   constructor(){
     super();



   }

   render(){

     var text = `<?xml version="1.0" encoding="utf-8" ?>
     <kml xmlns="http://www.opengis.net/kml/2.2">
     <Document><Folder><name>coc_wards</name>
     <Schema name="coc_wards" id="coc_wards">
     	<SimpleField name="Name" type="string"></SimpleField>
     	<SimpleField name="Description" type="string"></SimpleField>
     	<SimpleField name="PROP_MERGE" type="string"></SimpleField>
     	<SimpleField name="MAWS12_W" type="int"></SimpleField>
     	<SimpleField name="NEW_WARD" type="int"></SimpleField>
     	<SimpleField name="AREA_SQKM" type="float"></SimpleField>
     	<SimpleField name="PARLI_CONS" type="string"></SimpleField>
     	<SimpleField name="WARD" type="string"></SimpleField>
     	<SimpleField name="wardid" type="int"></SimpleField>
     	<SimpleField name="wardname" type="string"></SimpleField>
     	<SimpleField name="g" type="string"></SimpleField>
     </Schema>
       <Placemark>
     	<name>18887</name>
     	<description>N001</description>
       <Style><LineStyle><color>ff0000ff</color></LineStyle>  <PolyStyle><fill>0</fill></PolyStyle></Style>
     	<ExtendedData><SchemaData schemaUrl="#coc_wards">
     		<SimpleData name="MAWS12_W">0</SimpleData>
     		<SimpleData name="NEW_WARD">1</SimpleData>
     		<SimpleData name="AREA_SQKM">1.83206134104</SimpleData>
     		<SimpleData name="PARLI_CONS">North</SimpleData>
     		<SimpleData name="WARD">1</SimpleData>
     		<SimpleData name="wardid">18887</SimpleData>
     		<SimpleData name="wardname">N001</SimpleData>
     		<SimpleData name="g">g=wardname</SimpleData>
     	</SchemaData></ExtendedData>
           <Polygon><outerBoundaryIs><LinearRing><coordinates>80.327119778308116,13.217904506054095,0 80.326596682279913,13.217932676483713,0 80.323117593884803,13.218120008442025,0 80.323621674927793,13.220964708662894,0 80.323113745224887,13.221043719848879,0 80.323208990104362,13.221459331297142,0 80.320568124913791,13.221684453345716,0 80.319599703889139,13.222660154130658,0 80.319433852852782,13.222281895122208,0 80.316308371046262,13.226623695137777,0 80.316528478198919,13.227103014048893,0 80.317712716998358,13.229647903732417,0 80.317793131742306,13.231106947493407,0 80.322736091226318,13.232177308604248,0 80.323759171102594,13.23239884273827,0 80.326041095017004,13.234092762309707,0 80.327329355579238,13.235049055200307,0 80.331379552288951,13.233849210200807,0 80.331074538764469,13.232936355336461,0 80.331493530381778,13.232327802478851,0 80.330893900310343,13.23068274824756,0 80.33048168671975,13.229551855328673,0 80.330271038472176,13.229108772523968,0 80.329845050225529,13.22773033322186,0 80.329719929383089,13.227325458804637,0 80.329298125484272,13.225772317413474,0 80.32897757097146,13.224591982448469,0 80.327410480406655,13.219667262138447,0 80.327119778308116,13.217904506054095,0</coordinates></LinearRing></outerBoundaryIs></Polygon>
       </Placemark>

       <Placemark>
     	<name>18888</name>
     	<description>N002</description>
       <Style><LineStyle><color>ff0000ff</color></LineStyle>  <PolyStyle><fill>0</fill></PolyStyle></Style>
     	<ExtendedData><SchemaData schemaUrl="#coc_wards">
     		<SimpleData name="MAWS12_W">0</SimpleData>
     		<SimpleData name="NEW_WARD">2</SimpleData>
     		<SimpleData name="AREA_SQKM">2.84636392149</SimpleData>
     		<SimpleData name="PARLI_CONS">North</SimpleData>
     		<SimpleData name="WARD">2</SimpleData>
     		<SimpleData name="wardid">18888</SimpleData>
     		<SimpleData name="wardname">N002</SimpleData>
     		<SimpleData name="g">g=wardname</SimpleData>
     	</SchemaData></ExtendedData>
           <Polygon><outerBoundaryIs><LinearRing><coordinates>80.327119778308116,13.217904506054095,0 80.326109112001546,13.214708361967116,0 80.325616535903904,13.212954698485715,0 80.325411295896259,13.212400549922704,0 80.324569811106841,13.209814522443935,0 80.323954089740937,13.207680024513747,0 80.323472926098248,13.206428500781643,0 80.322952036117556,13.204487247464034,0 80.322223100431088,13.204789185898429,0 80.321382451479067,13.205016248511427,0 80.318233471149284,13.205686892478626,0 80.317717808314455,13.205922006455937,0 80.317524004112201,13.205930885329447,0 80.317323477134877,13.205974199739096,0 80.31551027256053,13.206393154567657,0 80.315459376068461,13.206410849219786,0 80.314275786466155,13.206822331295893,0 80.311473251641615,13.20752394769996,0 80.308812853946932,13.207088964641267,0 80.308283210741024,13.210940691522538,0 80.31005431627203,13.214198297268451,0 80.311481892074113,13.216823974701422,0 80.31277485124879,13.219202010585517,0 80.31324654158874,13.220069539011607,0 80.313582019656934,13.220686542939131,0 80.316308371046262,13.226623695137777,0 80.319433852852782,13.222281895122208,0 80.319599703889139,13.222660154130658,0 80.320568124913791,13.221684453345716,0 80.323208990104362,13.221459331297142,0 80.323113745224887,13.221043719848879,0 80.323621674927793,13.220964708662894,0 80.323117593884803,13.218120008442025,0 80.326596682279913,13.217932676483713,0 80.327119778308116,13.217904506054095,0</coordinates></LinearRing></outerBoundaryIs></Polygon>
       </Placemark>

     </Folder></Document>`;

     var parser = new DOMParser();
     var xmlDoc = parser.parseFromString(text,"text/xml");

     var placeMark = xmlDoc.getElementsByTagName('Placemark')[0];
     var coordinatesStr = placeMark.getElementsByTagName("coordinates")[0].innerHTML;
     console.log('xmlDoc -->', coordinatesStr);
     //console.log('polygon path -->', convertCoordinatesToPolygonPaths(coordinatesStr));

     const polygonPath = convertCoordinatesToPolygonPaths(coordinatesStr);

     return(
       <AsyncGoogleMap
         googleMapURL="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=geometry,drawing,places&key=AIzaSyDrxvgg2flbGdU9Fxn6thCbNf3VhLnxuFY"
         polygonPath={polygonPath}
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
