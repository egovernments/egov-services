import React, { Component } from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import { Link } from 'react-router-dom'

export default class Payment extends Component {
	render () {
		return (
			<Card>
            <CardHeader/>
              <CardText style={{textAlign: "center"}}>
              	<h4>Gateway response <Link to="/prd/dashboard">Please Click Here</Link> to go back to dashboard</h4>
              </CardText>
            </Card>

		)
	}
}
