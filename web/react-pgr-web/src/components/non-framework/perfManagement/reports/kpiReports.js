import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';

import RaisedButton from 'material-ui/RaisedButton';
import DropDownMenu from 'material-ui/DropDownMenu';
import MenuItem from 'material-ui/MenuItem';
import SelectField from 'material-ui/SelectField';

const style = {
    margin: 12
};

class KPIReports extends Component {
    constructor(props) {
        super(props);
        this.state = {value: 2};
      }
    
    handleChange = (event, index, value) => this.setState({value});

    render() {
        return (
            <div>
                <Card className="uiCard">
                <Grid fluid>
                    <CardText>
                        <Row>
                        <Col xs={12} sm={6} md={6} >
                            <SelectField
                                className="custom-form-control-for-select"
                                floatingLabelStyle={{"color":"#696969", "fontSize": "20px", "white-space": "nowrap"}}
                                labelStyle={{"color": "#5F5C57"}}
                                floatingLabelFixed={true}
                                dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                style={{"display": 'inline-block'}}
                                errorStyle={{"float":"left"}}
                                fullWidth={true}
                                floatingLabelText={<span>Deparment <span style={{"color": "#FF0000"}}> *</span></span>}
                                value={this.state.value}
                                onChange={this.handleChange}
                            >
                                <MenuItem value={1} primaryText="Never" />
                                <MenuItem value={2} primaryText="Every Night" />
                                <MenuItem value={3} primaryText="Weeknights" />
                                <MenuItem value={4} primaryText="Weekends" />
                                <MenuItem value={5} primaryText="Weekly" />
                            </SelectField>
                    </Col>
                    <Col  xs={12} sm={6} md={6}>
                            <SelectField
                                className="custom-form-control-for-select"
                                floatingLabelStyle={{"color":"#696969", "fontSize": "20px", "white-space": "nowrap"}}
                                labelStyle={{"color": "#5F5C57"}}
                                floatingLabelFixed={true}
                                dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                style={{"display": 'inline-block'}}
                                errorStyle={{"float":"left"}}
                                fullWidth={true}
                                floatingLabelText={<span>KPI <span style={{"color": "#FF0000"}}> *</span></span>}
                                value={this.state.value}
                                onChange={this.handleChange}
                            >
                                <MenuItem value={1} primaryText="Never" />
                                <MenuItem value={2} primaryText="Every Night" />
                                <MenuItem value={3} primaryText="Weeknights" />
                                <MenuItem value={4} primaryText="Weekends" />
                                <MenuItem value={5} primaryText="Weekly" />
                            </SelectField>
                    </Col>
                    </Row>
                    </CardText>
                    </Grid>
                </Card>
                <Card className="uiCard">
                <Grid fluid>
                    <CardText>
                        <Row>
                            <Col xs={12} sm={6} md={6} >
                                <SelectField
                                    className="custom-form-control-for-select"
                                    floatingLabelStyle={{"color":"#696969", "fontSize": "20px", "white-space": "nowrap"}}
                                    labelStyle={{"color": "#5F5C57"}}
                                    floatingLabelFixed={true}
                                    dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                    style={{"display": 'inline-block'}}
                                    errorStyle={{"float":"left"}}
                                    fullWidth={true}
                                    floatingLabelText={<span>ULBs <span style={{"color": "#FF0000"}}> *</span></span>}
                                    value={this.state.value}
                                    onChange={this.handleChange}
                                >
                                    <MenuItem value={1} primaryText="Never" />
                                    <MenuItem value={2} primaryText="Every Night" />
                                    <MenuItem value={3} primaryText="Weeknights" />
                                    <MenuItem value={4} primaryText="Weekends" />
                                    <MenuItem value={5} primaryText="Weekly" />
                                </SelectField>
                            </Col>
                            <Col  xs={12} sm={6} md={6}>
                                <SelectField
                                    className="custom-form-control-for-select"
                                    floatingLabelStyle={{"color":"#696969", "fontSize": "20px", "white-space": "nowrap"}}
                                    labelStyle={{"color": "#5F5C57"}}
                                    floatingLabelFixed={true}
                                    dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                    style={{"display": 'inline-block'}}
                                    errorStyle={{"float":"left"}}
                                    fullWidth={true}
                                    floatingLabelText={<span>Financial Year <span style={{"color": "#FF0000"}}> *</span></span>}
                                    value={this.state.value}
                                    onChange={this.handleChange}
                                >
                                    <MenuItem value={1} primaryText="Never" />
                                    <MenuItem value={2} primaryText="Every Night" />
                                    <MenuItem value={3} primaryText="Weeknights" />
                                    <MenuItem value={4} primaryText="Weekends" />
                                    <MenuItem value={5} primaryText="Weekly" />
                                </SelectField>
                            </Col>
                        </Row>
                    </CardText>
                    </Grid>
                    
                </Card>
                <div style={{"textAlign": "center"}}>
                    <br/>
                    <RaisedButton label="View" style={style} primary={true} type="button" />
                </div>
            </div>
        )
    }
}

export default KPIReports;