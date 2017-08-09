import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import Api from '../../../api/api';
import jp from "jsonpath";

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
    var val = this.props.getVal(item.jsonPath);
    if(val && item.hasOwnProperty("url") && item.url.search("\\|")>-1) {
      let splitArray = item.url.split("?");
      let context = "";
      let id = {};
      for (var j = 0; j < splitArray[0].split("/").length; j++) {
        context += splitArray[0].split("/")[j]+"/";
      }

      Api.commonApiPost(context, id, {}, "", useTimestamp || false).then(function(response) {
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
      },function(err) {
          console.log(err);
      });
    }
  }

  componentDidMount() {
    this.setVal();
  }

  componentDidUpdate() {
    this.setVal();
  }

 	renderLabel = (item) => {
 		return (
      <div>
   			<Row>
            {!item.hasOwnProperty("isLabel")?<Col xs={12}>
              <label><span style={{"fontWeight":"bold"}}>{item.label}</span></label>
            </Col>:""}
            <Col style={{textAlign:"center"}} xs={12}>{this.state.value || this.props.getVal(item.jsonPath) || "-"}</Col>
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
