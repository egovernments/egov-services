import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import _ from "lodash";
import Api from '../.././../../api/api';
import {getTenantId, extractManipulateCityAndWardsPath} from './ReportUtils';
import GisMapView from './GisMapView';

export default class GisAnalysisReport extends Component {

  constructor(){
    super()
    this.setState({
      isFetchingData:true
    });
  }

  componentDidMount(){
    
  }

  render(){
      return(
        <Grid fluid={true}>

          <Row>
            <Col sm={6} md={6} lg={6} xs={12}>

            </Col>
            <Col sm={6} md={6} lg={6} xs={12}>

            </Col>
          </Row>

          <Row>
            <Col sm={4} md={4} lg={4} xs={12}>

            </Col>
            <Col sm={4} md={4} lg={4} xs={12}>

            </Col>
            <Col sm={4} md={4} lg={4} xs={12}>

            </Col>
            <Col sm={4} md={6} lg={4} xs={12}>

            </Col>
          </Row>

        </Grid>
      )
  }

}
