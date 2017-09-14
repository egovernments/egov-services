import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../../api/api';
import {translate, epochToDate, epochToTime} from '../../../common/common';
import styles from '../../../../styles/material-ui';

var self;

class Acknowledgement extends Component{
  constructor(props){
    super(props);
    this.state={};
  }
  componentDidMount(){
    this.initData(this.props.match.params.id);
  }
  componentWillReceiveProps(nextProps){
    if(this.props.match.params.id !== nextProps.match.params.id){
      this.initData(nextProps.match.params.id);
    }
  }
  initData = (id) => {
    let {setForm, setLoadingStatus} = this.props;
    this.setState({licenseId : id});
    setLoadingStatus('loading');
    Api.commonApiPost("/tl-services/license/v1/_search",{ids : id}, {}, false, true).then(function(response)
    {
      if(response.licenses.length > 0){
        self.setState({license : response.licenses[0]});
        setForm(response.licenses[0]);
        setLoadingStatus('hide');
      }else{
        self.handleError('License does not exist');
      }
    },function(err) {
      setLoadingStatus('hide');
      self.handleError(err.message);
    });
  }
  handleError = (msg) => {
    let {toggleDailogAndSetText, setLoadingStatus}=this.props;
    setLoadingStatus('hide');
    toggleDailogAndSetText(true, msg);
  }
  render(){
    self = this;
    let {viewLicense, setRoute} = this.props;
    return(
      <Grid style={styles.fullWidth}>
        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Acknowledgement - New Trade License< /div>}/>
          <CardText>
            <Table responsive style={{fontSize:"bold"}} bordered condensed>
               <tbody>
                   <tr>
                       <td style={{textAlign:"left"}}>
                         <img src="./temp/images/headerLogo.png" height="60" width="60"/>
                       </td>
                       <td style={{textAlign:"center"}}>
                           <span style={{fontWeight:500, fontSize:'15px'}}>Roha Municipal Council</span><br/>
                       </td>
                       <td style={{textAlign:"right"}}>
                         <img src="./temp/images/AS.png" height="60" />
                       </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Application Number : {viewLicense.applicationNumber}
                           </Col>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Applicant Name : {viewLicense.ownerName}
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Service Name : New License
                           </Col>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Department Name : PUBLIC HEALTH AND SANITATION
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={12} sm={12} md={12} lg={12}>
                             Application Fee :
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Application Date : {epochToDate(viewLicense.applicationDate)}
                           </Col>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Application Time : {epochToTime(viewLicense.applicationDate)}
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Due Date :
                           </Col>
                           <Col xs={6} sm={6} md={6} lg={6}>
                             Due Time :
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3}>
                       <Grid>
                         <Row>
                           <Col xs={12} sm={12} md={12} lg={12}>
                             Note : The SLA period starts after the payment of the application Fee
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                   <tr>
                     <td colSpan={3} className="text-right">
                       <Grid>
                         <Row>
                           <Col xs={12} sm={12} md={12} lg={12}>
                             <br/><br/><br/><br/>
                             <div>Signing Authority</div>
                             <div><b>Roha Municipal Council</b></div>
                           </Col>
                         </Row>
                       </Grid>
                     </td>
                   </tr>
                 </tbody>
               </Table>
               <div className="text-center">
                 <RaisedButton style={styles.marginStyle} label="View" primary={true} onClick={(e)=>{setRoute('/non-framework/tl/transaction/viewLicense/'+this.state.licenseId)}}/>
                 {/*<RaisedButton style={styles.marginStyle} label="Print" primary={true}/>*/}
               </div>
          </CardText>
        </Card>
      </Grid>
    )
  }
}

const mapStateToProps = state => {
  return ({viewLicense : state.form.form});
};

const mapDispatchToProps = dispatch => ({
  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:false,
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
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  setRoute:(route)=>{
    dispatch({type:'SET_ROUTE',route})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(Acknowledgement);
