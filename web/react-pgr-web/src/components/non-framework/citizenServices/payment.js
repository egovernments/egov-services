import React, { Component } from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import { Link } from 'react-router-dom'
import $ from 'jquery';


export default class Payment extends Component {

  constructor(props)
  {
    super(props);
    this.state={
      msg:""
    }
  }

  componentDidMount()
  {
      this.setState({msg:this.props.match.params.msg});
  }

	render () {
    let  {msg}=this.state;
		return (
			<Card>
            <CardHeader/>
              <CardText style={{textAlign: "center"}}>
              	<h4>Gateway response {msg}. <Link to="/prd/dashboard">Please Click Here</Link> to go back to dashboard</h4>
              </CardText>
            </Card>

		)
	}
}
