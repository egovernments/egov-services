import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import AutoComplete from 'material-ui/AutoComplete';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../api/api';

const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  },
  paddingStyle:{
    padding: '15px'
  },
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: brown500
  },
  underlineFocusStyle: {
    borderColor: brown500
  },
  floatingLabelStyle: {
    color: brown500
  },
  floatingLabelFocusStyle: {
    color: brown500
  },
  customWidth: {
    width:100
  },
  checkbox: {
    marginTop: 37
  }
};

const names = [
  'Oliver Hansen',
  'Van Henry',
  'April Tucker',
  'Ralph Hubbard',
  'Omar Alexander',
  'Carlos Abbott',
  'Miriam Wagner',
  'Bradley Wilkerson',
  'Virginia Andrews',
  'Kelly Snyder',
];

class BulkEscalationGeneration extends Component {
    constructor(props) {
      super(props)
      this.state = {
              dataSource: [],
              values: [],
            };
    }

    componentWillMount() {

    }

    componentDidMount() {

    }

    handleUpdateInput = (value) => {
      this.setState({
          dataSource: [
            value,
            value + value,
            value + value + value,
          ],
      });
    }

    handleChange = (event, index, values) => this.setState({values});

    menuItems = (values) => {
    return names.map((name) => (
      <MenuItem
        key={name}
        insetChildren={true}
        checked={values && values.indexOf(name) > -1}
        value={name}
        primaryText={name}
      />
    ));
  }

    render() {

      const {values} = this.state;

      return(<div className="bulkEscalationGeneration">
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Bulk Escalation Generation < /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={4}>
                                        <AutoComplete
                                          hintText="Type anything"
                                          dataSource={this.state.dataSource}
                                          onUpdateInput={this.handleUpdateInput}
                                        />
                                  </Col>
                                  <Col xs={12} md={4}>
                                        <SelectField
                                           multiple={true}
                                           hintText="Select a name"
                                           value={values}
                                           onChange={this.handleChange}
                                         >
                                            {this.menuItems(values)}
                                         </SelectField>
                                  </Col>
                                  <Col xs={12} md={4}>
                                      <AutoComplete
                                        hintText="Type anything"
                                        dataSource={this.state.dataSource}
                                        onUpdateInput={this.handleUpdateInput}
                                      />
                                  </Col>
                              </Row>
                          </Grid>
                      </CardText>
                  </Card>
              </CardText>
          </Card>
      </div>)
    }
}


const mapStateToProps = state => {
  return ({createReceivingCenter : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: []
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
      validationData: {
        required: {
          current: [],
          required: []
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  handleChange: (e, property, isRequired, pattern) => {
    console.log("handlechange"+e+property+isRequired+pattern);
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value: e.target.value,
      isRequired,
      pattern
    });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(BulkEscalationGeneration);
