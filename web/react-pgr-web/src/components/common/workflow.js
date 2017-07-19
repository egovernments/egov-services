import React, {Component} from 'react';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
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

const workflow = ({workflowdetails}) => {
  const renderWorkflow = () => {
    if(workflowdetails != undefined){
      return workflowdetails.map((workflow, index) =>
      {
        var department;
        for(var k in workflow.values.department.values){
          department = Object.values(workflow.values.department.values[k])[1];
        }
        return (
          <Row style={styles.addBorderBottom} key={index}>
            <Col xs={12} md={2}>
              {workflow.last_updated}
            </Col>
            <Col xs={12} md={2}>
              {workflow.sender_name}
            </Col>
            <Col xs={12} md={2}>
              {workflow.status}
            </Col>
            <Col xs={12} md={2}>
              {workflow.owner}
            </Col>
            <Col xs={12} md={2}>
              {department}
            </Col>
            <Col xs={12} md={2}>
              {workflow.comments}
            </Col>
          </Row>
        )
      });
    }
  }
  return(
    <Grid style={{width:'100%'}}>
      <Card style={{margin:'15px 0'}}>
        <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
         {translate('core.lbl.history')}
        < /div>}/>
        <CardText style={{padding:'8px 16px 0'}}>
          <Row style={styles.addBorderBottom}>
            <Col xs={12} md={2}>
              {translate('core.lbl.date')}
            </Col>
            <Col xs={12} md={2}>
              {translate('pgr.lbl.updatedby')}
            </Col>
            <Col xs={12} md={2}>
              {translate('core.lbl.status')}
            </Col>
            <Col xs={12} md={2}>
              {translate('pgr.lbl.currentowner')}
            </Col>
            <Col xs={12} md={2}>
              {translate('core.lbl.department')}
            </Col>
            <Col xs={12} md={2}>
              {translate('core.lbl.comments')}
            </Col>
          </Row>
          {renderWorkflow()}
        </CardText>
      </Card>
    </Grid>
  );
}

export default workflow;
