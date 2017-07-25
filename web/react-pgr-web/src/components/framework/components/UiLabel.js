import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';

export default class UiLabel extends Component {
	constructor(props) {
       super(props);
   	}

   	renderLabel = (item) => {
   		return (
   			<Grid>
                <Row>
                	<Col xs={6} md={6}>
                       <label>
			   				{item.label}
			   		   </label>
                    </Col>
                    <Col xs={6} md={6}>
                   		<label>
			   				{this.props.getVal(item.jsonPath)}
			   			</label>    
                    </Col>
                </Row>
            </Grid>
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

