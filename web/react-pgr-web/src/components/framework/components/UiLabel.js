import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import Api from '../../../api/api';
import jp from "jsonpath";
import {translate} from '../../common/common';
import FlatButton from 'material-ui/FlatButton';

export default class UiLabel extends Component {
	constructor(props) {
       super(props);
       this.state = {
          value: ""
       }
  }

  setVal = () => {
    let {item, useTimestamp} = this.props;
    let self = this;
    var val = this.props.getVal(item.jsonPath,item.isDate);
		if (item.isConfig) {
	    //=======================BASED ON APP CONFIG==========================//
	    Api.commonApiPost('/wcms/masters/waterchargesconfig/_search', {
	      name: "HIERACHYTYPEFORWC"
	    }).then((res1) => {
	      if(res1.WaterConfigurationValue && res1.WaterConfigurationValue[0] && res1.WaterConfigurationValue[0].value && res1.WaterConfigurationValue[0].value) {

	        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:res1.WaterConfigurationValue[0].value}).then((response)=>{
	          if(response) {
							let keys = jp.query(response,"$.Boundary.*.code");
		          let values = jp.query(response,"$.Boundary.*.name");
		          let dropDownData = [];
		          for (var k = 0; k < keys.length; k++) {
		              if(val == keys[k]) {
		                return self.setState({
		                  value: values[k]
		                })
		              }
		          }
						}
	        }).catch((err)=> {
	          console.log(err)
	        })
	      }

	    }).catch((err) => {
	        console.log(err);
	    })

		} else if(val && item.hasOwnProperty("url") && item.url.search("\\|")>-1) {
      let splitArray = item.url.split("?");
      let context = "";
      let id = {};
      for (var j = 0; j < splitArray[0].split("/").length; j++) {
        context += splitArray[0].split("/")[j]+"/";
      }

      let queryStringObject=splitArray[1].split("|")[0].split("&");
      for (var i = 0; i < queryStringObject.length; i++) {
        if (i) {
          id[queryStringObject[i].split("=")[0]]=queryStringObject[i].split("=")[1];
        }
      }

      Api.commonApiPost(context, id, {}, "", useTimestamp || false).then(function(response) {
        if(response) {
          let keys = jp.query(response,splitArray[1].split("|")[1]);
          let values = jp.query(response,splitArray[1].split("|")[2]);
          let dropDownData = [];
          for (var k = 0; k < keys.length; k++) {
              if(val == keys[k]) {
                return self.setState({
                  value: values[k]
                })
              }
          }
        }
      },function(err) {
          console.log(err);
      });
    } else if(val) {
			this.setState({
				value: val
			})
		}

  }

  componentDidMount() {
    this.setVal();
  }

  componentDidUpdate() {
    if(!this.state.value)
      this.setVal();
  }

  openLink(item) {
    window.open(item.hyperLink + "/" + encodeURIComponent(this.state.value || this.props.getVal(item.jsonPath,item.isDate)), 'mywin', 'left=20,top=20,width=500,height=500,toolbar=1,resizable=0');
  }

 	renderLabel = (item) => {
		console.log(item.jsonPath + "-" + this.props.getVal(item.jsonPath) + "-" + this.state.value);
 		return (
      <div>
   			<Row>
            {!item.hasOwnProperty("isLabel")?<Col id={item.jsonPath.split(".").join("-")}		 style={item.hasOwnProperty("textAlign")?{textAlign:item.textAlign}:{textAlign:"left"}} xs={12}>
              <label><span style={{"fontWeight":500, "fontSize": "13px"}}>{translate(item.label)}</span></label>
            </Col>:""}
            {item.hyperLink && (this.state.value || this.props.getVal(item.jsonPath,item.isDate)) ?
              (<Col style={{textAlign:"left"}} xs={12}>
                <FlatButton label={this.state.value || this.props.getVal(item.jsonPath,item.isDate)} primary={true}/>
              </Col>)
              :
              <Col id={item.jsonPath.split(".").join("-")}	style={item.hasOwnProperty("textAlign")?{textAlign:item.textAlign}:{textAlign:"left"}} xs={12}>{this.state.value || this.props.getVal(item.jsonPath,item.isDate) || "NA"}</Col>}
        </Row>
        <br/>
      </div>
 		);
 	}

 	render () {
	  return (
      <div>
        {this.renderLabel(this.props.item)}
      </div>
    );
  }
}
