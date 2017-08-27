import React, { Component } from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';

export default class ComingSoon extends Component {
	render () {
		return (
			<Card>
            <CardHeader/>
              <CardText style={{textAlign: "center"}}>
              	<h4>Coming Soon.... <a href="/#/prd/dashboard">Please Click Here</a> to go back to dashboard</h4>
              </CardText>
            </Card>
			
		)
	}
}