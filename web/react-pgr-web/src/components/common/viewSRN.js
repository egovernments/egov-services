import React, {Component} from 'react';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import FileDownload from 'material-ui/svg-icons/action/get-app';
import {translate} from './common';
var axios = require('axios');

const styles = {
  headerStyle : {
    fontSize : 19
  },
  addBorderBottom:{
    borderBottom: '1px solid #eee',
    padding: '10px'
  },
  marginStyle:{
    margin: '15px'
  }
};

class viewsrn extends Component{
  constructor(props){
    super(props);
    this.state = {
      address : undefined
    }
  }
  componentWillReceiveProps(nextprops){
    if((nextprops.srn.lat !== 0 && nextprops.srn.lng !== 0) && (nextprops.srn.lat && nextprops.srn.lng))
      this.getAddress(nextprops.srn.lat, nextprops.srn.lng);
  }
  filesUploaded = () =>{
    if(this.props.srn.files != undefined){
      return this.props.srn.files.map((files, index) => {
        return (
          <Col xs={6} md={3} key={index}>
            <RaisedButton
              href={files.url}
              download
              label={"File " + (index+1)}
              primary={true}
              fullWidth = {true}
              style={styles.marginStyle}
              labelPosition="before"
              icon={<FileDownload />}
            />
          </Col>
        );
      });
    }
  }
  getAddress = (lat, lng) => {
    var _this = this;
    axios.post('https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true')
    .then(function (response) {
      if(response.data.results.length > 0)
        _this.setState({address : response.data.results[0].formatted_address});
    });
  }
  render(){
    return (
      <Grid style={{width:'100%'}}>
        <Card style={{margin:'15px 0'}}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
             {translate('pgr.lbl.srn')} : {this.props.srn.serviceRequestId}
           < /div>}/>
           <CardText style={{padding:'8px 16px 0'}}>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.add.name')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.firstName}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.mobilenumber')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.phone ? this.props.srn.phone : 'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.email.compulsory')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.email ? this.props.srn.email : 'N/A'}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.address')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.systemRequesterAddress ? this.props.srn.systemRequesterAddress : 'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.enter.aadharcard.number')}
                </Col>
                <Col xs={6} md={3}>
                  N/A
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.description')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.description}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.grievance.type')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.serviceName}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.department')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.departmentName}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.registered.date')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.requestedDatetime}
                </Col>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.nextescalation.date')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.expectedDatetime}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.filedvia')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.systemReceivingMode}
                </Col>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.receivingcenter')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.receivingCenterName ? this.props.srn.receivingCenterName :'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.location')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.address ? this.state.address :
                  this.props.srn.childLocationName ? this.props.srn.childLocationName + " - " + this.props.srn.locationName : this.props.srn.locationName
                  }
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.landmark')}
                </Col>
                <Col xs={6} md={3}>
                  {this.props.srn.address ? this.props.srn.address : 'N/A'}
                </Col>
              </Row>
              {this.props.srn.systemExternalCRN ?
                <Row style={styles.addBorderBottom}>
                  <Col xs={6} md={3}>
                    External CRN
                  </Col>
                  <Col xs={6} md={3}>
                    {this.props.srn.systemExternalCRN}
                  </Col>
                </Row>
              : ''}
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  Files
                </Col>
                {this.filesUploaded()}
              </Row>
           </CardText>
        </Card>
      </Grid>
    )
  }
}

export default viewsrn;
