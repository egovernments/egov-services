import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import Checkbox from 'material-ui/Checkbox';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import Dialog from 'material-ui/Dialog';
import MenuItem from 'material-ui/MenuItem';
import _ from "lodash";
import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';

class NoticeSearch extends Component {
  constructor(props) {
    super(props);
    this.state={};
  }
  componentDidMount(){

  }
  render(){
    return(
      <div>
        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('core.lbl.contact.information')}< /div>}/>
          <CardText style={{paddingTop:0}}>
            <Grid>
              <Row>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.result.groups.applicationNumber')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.groups.tradeOwnerName')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.groups.mobileNumber')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.groups.tradeTitle')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.groups.licenseNumber')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.groups.applicationStatus')} >
                    <MenuItem value="" primaryText="Select" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.create.groups.licensedocumenttype.licenseapptype')}>
                    <MenuItem value="" primaryText="Select" />
                    <MenuItem value="" primaryText="New" />
                    <MenuItem value="" primaryText="Renew" />
                    <MenuItem value="" primaryText="Title Transfer" />
                    <MenuItem value="" primaryText="Cancellation" />
                    <MenuItem value="" primaryText="Business Name Change" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.create.licenses.groups.TradeLocationDetails.Ward')} >
                    <MenuItem value="" primaryText="Select" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.documenttype')} >
                    <MenuItem value="" primaryText="Select" />
                    <MenuItem value="" primaryText="Acknowledgement" />
                    <MenuItem value="" primaryText="Inspection Report" />
                    <MenuItem value="" primaryText="Fee Receipt" />
                    <MenuItem value="" primaryText="Certificate" />
                    <MenuItem value="" primaryText="Rejection Letter" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.fromDate')} />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('tl.search.toDate')} />
                </Col>
              </Row>
            </Grid>
          </CardText>
        </Card>
        <div className="text-center">
          <RaisedButton style={{margin:'15px 5px'}} label={translate('core.lbl.search')} primary={true}/>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return ({});
}

const mapDispatchToProps = dispatch => ({

});

export default connect(mapStateToProps, mapDispatchToProps)(NoticeSearch);
