import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';

export default class UiLabel extends Component {
	constructor(props) {
       super(props);
   	}

   	renderLabel = (item) => {
   		return (
        <div>
   			<Row>
            <Col xs={12}>
              <label>{item.label}</label>
            </Col>
            <Col xs={12}>{this.props.getVal(item.jsonPath) || "-"}</Col>
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

