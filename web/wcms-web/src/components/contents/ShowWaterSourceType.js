function getUrlVars() {
   var vars = [],
       hash;
   var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
   for (var i = 0; i < hashes.length; i++) {
       hash = hashes[i].split('=');
       vars.push(hash[0]);
       vars[hash[0]] = hash[1];
   }
   return vars;
}




import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import Checkbox from 'material-ui/Checkbox';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import DataTable from '../common/Table';
import Api from '../../api/wCAPIS';


const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');


const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var flag = 0;
const styles = {
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
  }
};

class ShowWaterSourceType extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search',list:[],
         tenantId : 'default',

       }
       this.search=this.search.bind(this);
   }

   close(){
         // widow.close();
         open(location, '_self').close();
     }

  componentWillMount()
  {
    $('#waterSourceTypeTable').DataTable({
         dom: 'lBfrtip',
         buttons: [
                   'excel', 'pdf', 'print'
          ],
          ordering: false,
          bDestroy: true,

       });

        let response=Api.commonApiPost("wcms-masters", "sourcetype", "_search", {},{}).then((res)=>
    {
      this.setState({
        list: res.waterSourceType
      });

    },(err)=> {
        alert(err);
    });//call boundary service fetch wards,location,zone data
  }

  componentDidMount()
  {
    let {initForm} = this.props;
    // let tenantId ='default';
    initForm();



}

  componentWillUnmount(){
     $('#waterSourceTypeTable')
     .DataTable()
     .destroy(true);
  }



  search(e)
  {
      let {showTable,changeButtonText}=this.props;
      e.preventDefault();
      // console.log("Show Table");
      flag=1;
      changeButtonText("Search");
      // this.setState({searchBtnText:'Search'})
      showTable(true);
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#waterSourceTypeTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
    // if (true) {
    //     $('#waterSourceTypeTable').DataTable({
    //       dom: 'lBfrtip',
    //       buttons: [
    //                 'excel', 'pdf', 'print'
    //        ],
    //        ordering: false,
    //        bDestroy: true,
    //
    //     });
    // }

  }

  render() {
    let {
      showWaterSourceType,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;

    let {search} = this;
    let{list}=this.state;
    // console.log(showWaterSourceType);
    // console.log(isTableShow);
    // console.log(list);
    let renderAction=function(type,id){
      if (type==="Update") {
        console.log(type);

              return (
                      <a href={`masters/WaterSourceType?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }
    else {

            return (
                    <a href={`masters/WaterSourceType?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>

            );

        }
}

    let renderBody=function()
    {
      //  console.log(list);
      return list.map((item,index)=>
      {

            return (<tr key={index}>
                      <td>{index+1}</td>
                      <td data-label="code">{item.code}</td>
                      <td data-label="name">{item.name}</td>
                      <td data-label="active">{item.active?"ACTIVE":"INACTIVE"}</td>
                      <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>



                </tr>
            );


      })
    }

    const viewTabel=()=>
    {
      return (
        <Card>
          <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Result < /strong>}/>
          <CardText>
        <Table id="waterSourceTypeTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
            <tr>
              <th>Sl No.</th>
              <th>Code</th>
              <th>Water Source Type</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody id="waterSourceTypeTableResultBoday">
                {renderBody()}
            </tbody>

        </Table>
      </CardText>
      </Card>
      )
    }
    return (
      <div className="showWaterSourceType">
        <form onSubmit={(e) => {
          search(e)
        }}>
          <Card>
             <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Water Source Type < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                      <TextField value={showWaterSourceType.name?showWaterSourceType.name:""} onChange={(e) => handleChange(e, "name", false, "")} hintText="waterSourceType" floatingLabelText="waterSourceType" />
                    </Col>
                    <Col xs={12} md={6}>
                      <TextField value={showWaterSourceType.code?showWaterSourceType.code:""} onChange={(e) => handleChange(e, "code", false, "")} hintText="Code" floatingLabelText="Code" />
                    </Col>


                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                                        <Checkbox
                                         label="Active"
                                         defaultChecked={true}
                                         value={showWaterSourceType.active?showWaterSourceType.active:""}
                                         onCheck={(event,isInputChecked) => {
                                           var e={
                                             "target":{
                                               "value":isInputChecked
                                             }
                                           }
                                           handleChange(e, "active", true, "")}
                                         }
                                         style={styles.checkbox}
                                         style={styles.topGap}
                                        />
                          </Col>
                        </Row>


                    </Grid>

                </CardText>
              </Card>

                            <div style={{
                textAlign: "center"
              }}>
                <RaisedButton type="submit" label={buttonText} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton label="Close"/>
              </div>
            </CardText>
          </Card>


                  {isTableShow?viewTabel():""}



        </form>

      </div>
    );
  }
}

const mapStateToProps = state => ({showWaterSourceType: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

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

  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  }


});

export default connect(mapStateToProps, mapDispatchToProps)(ShowWaterSourceType);
