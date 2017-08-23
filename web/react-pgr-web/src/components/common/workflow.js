import React, {Component} from 'react';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import styles from '../../styles/material-ui';
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';
import {translate} from './common';

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
          <TableRow selectable={false} key={index}>
            <TableRowColumn>{workflow.last_updated}</TableRowColumn>
            <TableRowColumn className="hidden-xs">{workflow.sender_name}</TableRowColumn>
            <TableRowColumn>{workflow.status}</TableRowColumn>
            <TableRowColumn>{workflow.owner}</TableRowColumn>
            <TableRowColumn className="hidden-xs">{department}</TableRowColumn>
            <TableRowColumn style={styles.customColumnStyle}>{workflow.comments}</TableRowColumn>
          </TableRow>
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
          <Table>
            <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
              <TableRow>
                <TableHeaderColumn>{translate('core.lbl.date')}</TableHeaderColumn>
                <TableHeaderColumn className="hidden-xs">{translate('pgr.lbl.updatedby')}</TableHeaderColumn>
                <TableHeaderColumn>{translate('core.lbl.status')}</TableHeaderColumn>
                <TableHeaderColumn>{translate('pgr.lbl.currentowner')}</TableHeaderColumn>
                <TableHeaderColumn className="hidden-xs">{translate('core.lbl.department')}</TableHeaderColumn>
                <TableHeaderColumn>{translate('core.lbl.comments')}</TableHeaderColumn>
              </TableRow>
            </TableHeader>
            <TableBody displayRowCheckbox={false}>
              {renderWorkflow()}
            </TableBody>
          </Table>
        </CardText>
      </Card>
    </Grid>
  );
}

export default workflow;
