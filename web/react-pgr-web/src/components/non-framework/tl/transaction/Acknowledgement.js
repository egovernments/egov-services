import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../../api/api';
import {translate} from '../../../common/common';
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
    var self = this;
    let {setLoadingStatus} = this.props;
    let {handleError} = this;
    this.setState({licenseId : id});
    setLoadingStatus('loading');
    Api.commonApiPost("/tl-services/license/v1/_search",{ids : id}, {}, false, true).then(function(response)
    {
      if(response.licenses.length > 0){
        Api.commonApiPost("tl-services/noticedocument/v1/_search",{licenseId:id},{}, false, true).then(function(response){
          console.log(response);
          self.setState({'pdf' : '/filestore/v1/files/id?fileStoreId='+response.NoticeDocument[0].fileStoreId+'&tenantId='+localStorage.getItem('tenantId')});
          setLoadingStatus('hide');
        }, function(err) {
            setLoadingStatus('hide');
            handleError(err.message);
        });
      }else{
        handleError('License does not exist');
      }
    },function(err) {
      setLoadingStatus('hide');
      handleError(err.message);
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
            <iframe type="application/pdf" style={{width:'100%'}} height="800" src={this.state.pdf}></iframe>
            <div className="text-center">
             <RaisedButton style={styles.marginStyle} label="View" primary={true} onClick={(e)=>{setRoute('/non-framework/tl/transaction/viewLicense/'+this.state.licenseId)}}/>
             <RaisedButton style={styles.marginStyle} label="Download Acknowledgement" href={this.state.pdf} download primary={true}/>
            </div>
          </CardText>
        </Card>
      </Grid>
    )
  }
}

const mapStateToProps = state => {
  return ({});
};

const mapDispatchToProps = dispatch => ({
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
