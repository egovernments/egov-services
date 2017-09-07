import React, { Component } from 'react';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import { Link } from 'react-router-dom'
import $ from 'jquery';
import {connect} from 'react-redux';


class Payment extends Component {

  constructor(props)
  {
    super(props);
    this.state={
      msg:{}
    }
  }

  componentDidMount()
  {
      let {msg}=this.props.match.params;
      // console.log(msg);
      msg=decodeURIComponent((msg+'').replace(/\+/g, '%20'));
      // console.log(msg);
      let splitArray=msg.split("|");
      let {setRoute}=this.props;
      let paymentGateWayResponse={}

      for (var i = 0; i < splitArray.length; i++) {
        // splitArray[i].split(":")[0]
        // splitArray[i].split(":")[1]
        paymentGateWayResponse[splitArray[i].split(":")[0]]=splitArray[i].split(":")[1];
        // this.setState({msg:{
        //   [splitArray[i].split("=")[0]]:[splitArray[i].split("=")[1]]
        // }});
      }

      console.log(paymentGateWayResponse);
      window.localStorage.setItem("paymentGateWayResponse",JSON.stringify(paymentGateWayResponse))
      if (paymentGateWayResponse["status"]!="failed") {
          if (window.localStorage.getItem("workflow")=="create" || window.localStorage.getItem("workflow")=="fireNoc") {
            setRoute("/non-framework/citizenServices/"+window.localStorage.getItem("workflow")+"/pay/"+window.localStorage.getItem("moduleName")+"/success");
          }
          else {
            setRoute("/non-framework-cs/citizenServices/"+window.localStorage.getItem("workflow")+"/pay/"+window.localStorage.getItem("moduleName")+"/success");
          }
      }
      else {
          alert("Payment failed");
          setRoute("/prd/dashboard");
      }
      this.setState({msg:this.props.match.params.msg});
  }

	render () {
    let  {msg}=this.state;
		return (
			<Card>
            <CardHeader/>
              <CardText style={{textAlign: "center"}}>
              	<h4>Gateway response redirecting please wait .....</h4>
              </CardText>
      </Card>

		)
	}
}

const mapStateToProps = state => ({
});

const mapDispatchToProps = dispatch => ({
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(Payment);
