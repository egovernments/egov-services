import React, { Component } from 'react';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {brown500, red500,white} from 'material-ui/styles/colors';
import Paper from 'material-ui/Paper';

const styles = {
    floatRight: {
      float:'right'
    },

    paper: {
      width: '100%',
      marginTop: 20,
      display: 'inline-block',
      backgroundColor:"#f3f4f5",
      color:"black",
      fontSize:12,
      paddingTop:16,
      paddingBottom:16,
    },

    putDown : {
      position:'absolute',
      bottom:0
    }
}

class Footer extends Component {
  render() {
    return (
      <div className="Footer" style={styles.paper}>
          <Col xs={12} lg={12}>
          <Row>
              <Col xs={12} sm={5}>Copyright &#169; 2017 eGovernments Foundation.<sup>&#174;</sup></Col>
              <Col xs={12} sm={7}>
                <div style={styles.floatRight}>eGov ERP - 2.1.0-SNAPSHOT_2486 @ Core - 2.1.0-SNAPSHOT_2486</div>
              </Col>
          </Row>
          </Col>


      </div>
    );
  }
}

export default Footer;
