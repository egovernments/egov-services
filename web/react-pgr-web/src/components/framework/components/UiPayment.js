import React, { Component } from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import DatePicker from 'material-ui/DatePicker';



const style = {
  margin: 12,
};

const CashOrMops = ()=>{
    return(
        <div>
          <Col xs={12} md={12}>
                        <TextField
                            floatingLabelText="Amount:"
                            /><br />
                            <TextField
                            floatingLabelText="Paid By:"
                </Col>
