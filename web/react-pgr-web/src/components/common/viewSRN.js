import React, {Component} from 'react';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import FileDownload from 'material-ui/svg-icons/action/get-app';
import {translate} from './common';

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

const viewsrn = ({srn}) => {
  const filesUploaded = () =>{
    if(srn.files != undefined){
      return srn.files.map((files, index) => {
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
  return (
    <Grid style={{width:'100%'}}>
      <Card style={{margin:'15px 0'}}>
        <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
           {translate('pgr.lbl.srn')} : {srn.serviceRequestId}
         < /div>}/>
         <CardText style={{padding:'8px 16px 0'}}>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('core.lbl.add.name')}
              </Col>
              <Col xs={6} md={3}>
                {srn.firstName}
              </Col>
              <Col xs={6} md={3}>
                {translate('core.lbl.mobilenumber')}
              </Col>
              <Col xs={6} md={3}>
                {srn.phone ? srn.phone : 'N/A'}
              </Col>
            </Row>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('core.lbl.email.compulsory')}
              </Col>
              <Col xs={6} md={3}>
                {srn.email ? srn.email : 'N/A'}
              </Col>
              <Col xs={6} md={3}>
                {translate('core.lbl.address')}
              </Col>
              <Col xs={6} md={3}>
                {srn.requesterAddress ? srn.requesterAddress : 'N/A'}
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
                {srn.description}
              </Col>
            </Row>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('pgr.lbl.grievance.type')}
              </Col>
              <Col xs={6} md={3}>
                {srn.serviceName}
              </Col>
              <Col xs={6} md={3}>
                {translate('core.lbl.department')}
              </Col>
              <Col xs={6} md={3}>
                {srn.departmentName}
              </Col>
            </Row>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('pgr.lbl.registered.date')}
              </Col>
              <Col xs={6} md={3}>
                {srn.requestedDatetime}
              </Col>
              <Col xs={6} md={3}>
                {translate('pgr.lbl.nextescalation.date')}
              </Col>
              <Col xs={6} md={3}>
                {srn.expectedDatetime}
              </Col>
            </Row>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('pgr.lbl.filedvia')}
              </Col>
              <Col xs={6} md={3}>
                {srn.receivingMode}
              </Col>
              <Col xs={6} md={3}>
                {translate('pgr.lbl.receivingcenter')}
              </Col>
              <Col xs={6} md={3}>
                {srn.receivingCenterName ? srn.receivingCenterName :'N/A'}
              </Col>
            </Row>
            <Row style={styles.addBorderBottom}>
              <Col xs={6} md={3}>
                {translate('core.lbl.location')}
              </Col>
              <Col xs={6} md={3}>
                {srn.childLocationName ? srn.childLocationName + " - " + srn.locationName : srn.locationName}
              </Col>
              <Col xs={6} md={3}>
                {translate('core.lbl.landmark')}
              </Col>
              <Col xs={6} md={3}>
                {srn.address ? srn.address : 'N/A'}
              </Col>
            </Row>
            {srn.externalCRN ?
              <Row style={{padding:'10px'}}>
                <Col xs={6} md={3}>
                  External CRN
                </Col>
                <Col xs={6} md={3}>
                  {srn.externalCRN}
                </Col>
              </Row>
            : ''}
            <Row style={{padding:'10px'}}>
              <Col xs={6} md={3}>
                Files
              </Col>
              {filesUploaded()}
            </Row>
         </CardText>
      </Card>
    </Grid>
  )
}

export default viewsrn;
