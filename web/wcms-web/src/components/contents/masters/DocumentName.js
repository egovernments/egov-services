import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Grid,Row,Col,Table } from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import Checkbox from 'material-ui/Checkbox';
import {brown500,red500, white} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';

const styles = {
    errorStyle: {
      color: red500,
    },
    underlineStyle: {
      borderColor: brown500,
    },
    underlineFocusStyle: {
      borderColor: brown500,
    },
    floatingLabelStyle: {
      color: brown500,
    },
    floatingLabelFocusStyle: {
      color: brown500,
    },
};


class DocumentName extends Component {

  constructor(props) {
       super(props);
       this.state = {
       }
   }



    render(){
      let {
        VacantLand,
        handleChange,
        fieldErrors,
      }=this.props;
      return(
        <div className="DocumentName">
            <Card>
                <CardHeader
                    title={<strong style={{color:brown500}}>Create Document Name </strong>}
                />
                <CardText>
                      <Card>
                          <CardText>
                              <Grid>

                                  <Row>
                                    <Col xs={12} md={6}>

                                      <SelectField errorText={fieldErrors.vacantLandPlotArea
                                        ? fieldErrors.vacantLandPlotArea
                                        : ""} value={DocumentName.vacantLandPlotArea?DocumentName.vacantLandPlotArea:""} onChange={(event, index, value) => {
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "vacantLandPlotArea", false, "")}} floatingLabelText="Application Type" >
                                        <MenuItem value={1} primaryText=""/>
                                        <MenuItem value={2} primaryText="Every Night"/>
                                        <MenuItem value={3} primaryText="Weeknights"/>
                                        <MenuItem value={4} primaryText="Weekends"/>
                                        <MenuItem value={5} primaryText="Weekly"/>
                                      </SelectField>

                                    </Col>
                                    <Col xs={12} md={6}>
                                      <TextField errorText={fieldErrors.ownerName
                                        ? fieldErrors.ownerName
                                        : ""} value={DocumentName.ownerName?DocumentName.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Document Name" floatingLabelText="Document Name" />
                                    </Col>


                                  </Row>
                                  <Row>
                                  <Col xs={12} md={6}>
                                                      <Checkbox
                                                       label="Active"
                                                       errorText={fieldErrors.Active
                                                         ? fieldErrors.Active
                                                         : ""}
                                                       value={DocumentName.Active?DocumentName.Active:""}
                                                       onCheck={(event,isInputChecked) => {
                                                         var e={
                                                           "target":{
                                                             "value":isInputChecked
                                                           }
                                                         }
                                                         handleChange(e, "Active", false, "")}
                                                       }
                                                       style={styles.checkbox}
                                                       style={styles.topGap}
                                                      />
                                        </Col>
                                      </Row>

                                </Grid>
                            </CardText>
                            </Card>


                      <div style={{float:"center"}}>
                      <RaisedButton type="submit" label="Create" backgroundColor={brown500} labelColor={white}/>
                      <RaisedButton type="button" label="Close" />
                      </div>
                </CardText>
            </Card>
        </div>
      )
    }

}
const mapStateToProps = state => ({VacantLand: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },

      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },



});

export default connect(mapStateToProps, mapDispatchToProps)(DocumentName);
