import React from 'react';
import LinearProgress from 'material-ui/LinearProgress';
import _ from "lodash";
import {Sector} from 'recharts';

const styles = {
  tooltipStyle:{
    backgroundColor : '#3A3A3A',
    padding:5
  },
  p:{
    display:"block",
    margin:0,
    textAlign:'left',
    padding:5,
    fontSize : 12
  }
}


const HorizontalPageLoader = () => (
  <LinearProgress mode="indeterminate" />
);

const CustomizedAxisTick = (props) =>{
    const MONTH_NAMES = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN",
      "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
    ];
    const {x, y, payload, isMonthNumber} = props;
    let value = isMonthNumber ? MONTH_NAMES[parseInt(payload.value)-1] : payload.value;

   	return (
        <text x={x} y={y+15} textAnchor="middle" fill="#666">{value}</text>
    );
};

const CustomizedYAxisLabel = (props) =>{
    const {viewBox, title} = props;
   	return (
      <g transform={`translate(${viewBox.x},${viewBox.y})`}>
        <text x={-(viewBox.height/2)} y={viewBox.y+13} textAnchor="middle" fill="#666" transform="rotate(-90)">{title}</text>
      </g>
    );
};

const CustomizedLegend = (props) =>{

  const {payload} = props;

  const legends = payload.map((data, idx)=>{
    return (<li key={idx} className={`recharts-legend-item legend-item-${idx}`} style={{display: 'block', marginRight:10}}>
      <svg className="recharts-surface" width="14" height="14" viewBox="0 0 32 32" version="1.1" style={{display: 'inline-block', verticalAlign: 'middle', marginRight:4}}>
        <path stroke="none" fill={data.color} d="M0,4h32v24h-32z" className="recharts-legend-icon"></path>
      </svg>
      <span className="recharts-legend-item-text" style={{fontSize:12}}>{`${data.value} - ${data.payload.value} (${(data.payload.percent * 100).toFixed(2)}%)`}</span>
    </li>)
  });

  return (
    <ul style={{padding:5}}>
      {legends}
    </ul>
  );

}

const RenderActiveShape = (props) => {
  const RADIAN = Math.PI / 180;
  const { cx, cy, midAngle, innerRadius, outerRadius, startAngle, endAngle,
    fill, payload, percent, value } = props;

  return (
    <g>
      {/* <text x={cx} y={cy} dy={8} textAnchor="middle" fill={fill}>{payload.name}</text> */}
      <Sector
        cx={cx}
        cy={cy}
        innerRadius={innerRadius}
        outerRadius={outerRadius}
        startAngle={startAngle}
        endAngle={endAngle}
        fill={fill}
      />
      <Sector
        cx={cx}
        cy={cy}
        startAngle={startAngle}
        endAngle={endAngle}
        innerRadius={outerRadius + 6}
        outerRadius={outerRadius + 10}
        fill={fill}
      />
    </g>
  )
}

const getTenantId = ()=>{
  return localStorage.getItem("tenantId") || "default";
}

const CustomTooltip = (props)=>{
     const {active} = props;
     if (active) {

       const sortByDataValue = (payload) => {
         //let sortedPayload = [];
         let sortedDatas = _.orderBy(payload[0].payload.data, ['count'], ['desc']);
         return sortedDatas.map((data)=>{
            for(let i=0;i<payload.length;i++){
              let isMatchesName = _.get(payload[i], `payload.${payload[i].name}`) === data.name;
              if(isMatchesName)
                return payload[i];
            }
         });
       }

       const { payload, label } = props;

       sortByDataValue(payload);

       const payloadSorted = sortByDataValue(payload);

       const labels = payloadSorted.map((area, idx)=> {
         let labelName = _.get(area, `payload.${area.name}`);
         return (<p key={idx} className="label"  style={{...styles.p, color:area.color}}>{`${labelName} : ${area.value}`}</p>)
       })

       return (
         <div className="custom-tooltip" style={styles.tooltipStyle}>
           {labels}
         </div>
       );
   }
   return null;
}

const extractManipulateCityAndWardsPath = (wardResponse, kmlText, cityLatLng, color, totalComplaints) => {

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

    let parser = new DOMParser();
    let xmlDoc = parser.parseFromString(kmlText,"text/xml");
    let placeMarks = xmlDoc.getElementsByTagName('Placemark');

    let wardsPolygons = [];

    for(let i=0; i<placeMarks.length;i++){
      let placeMark = placeMarks[i];
      let boundaryCode = placeMark.getElementsByTagName("name")[0].innerHTML;
      let fillColorStyle = {fillColor:"#FFFFFF", fillOpacity:0};

      let wardObj = _.find(wardResponse, function(ward) {return ward.boundary  ==  boundaryCode;});
      let wardName;
      let noOfComplaints;
      if(wardObj){
        wardName = wardObj.boundaryName;
        noOfComplaints = wardObj.count;

        let opacityVal = (wardObj.count / totalComplaints * .70);

        if(opacityVal < 0.25){
          opacityVal = 0.25;
        }

        fillColorStyle = {fillColor:color, fillOpacity: opacityVal.toFixed(2)}
      }

      let ward = { name : wardName, boundaryCode : boundaryCode, noOfComplaints:noOfComplaints || 0,
       style:{
         strokeWeight: 2,
         strokeColor: color || '#FF0000', strokeOpacity: 0.8,
         ...fillColorStyle
        },
       polygons:[]};
      let coordinatesStr = placeMark.getElementsByTagName("coordinates");
      for(let j=0;j<coordinatesStr.length;j++){
        let coordinateStr = coordinatesStr[j].innerHTML;
        ward.polygons.push(convertCoordinatesToPolygonPaths(coordinateStr));
      }
      wardsPolygons.push(ward);
    }
    return {kml:kmlText, wardsPolygons, cityLatLng, openBoundaryInfoWindow:undefined};
}

export {HorizontalPageLoader as HorizontalPageLoader,
  CustomizedAxisTick as CustomizedAxisTick,
  CustomizedYAxisLabel as CustomizedYAxisLabel, getTenantId as getTenantId,
  CustomTooltip as CustomTooltip, CustomizedLegend as CustomizedLegend,
  RenderActiveShape as RenderActiveShape,
  extractManipulateCityAndWardsPath as extractManipulateCityAndWardsPath}
