import React, {Component} from 'react';
import _ from "lodash";
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {Row, Col} from 'react-bootstrap';
import {translate} from 'egov-common-utility';
import styles from '../styles/material-ui';

export default class UploadDocs extends Component {
  constructor() {
    super();
    this.state={
      fileCount:1
    }
  }
  componentDidMount(){
    // console.log('did mount:', this.props.maxFile);
  }
  componentWillReceiveProps(nextProps){
    // console.log('nextProps:',nextProps.maxFile);
  }
  addFile = () => {
    this.setState((prevState, props) => {
      return {fileCount: prevState.fileCount + 1};
    });
  }
  docs = () => {
    return _.times(this.props.maxFile, idx=>{
      return (<Col xs={12} sm={4} md={3} lg={3} key={idx} className={idx<this.state.fileCount?'':'hide'}>
          <div className="input-group">
            <input type="file" id={`file${idx}`} ref={`file${idx}`} className="form-control" onChange= {(e) => {this.props.handler({target:{value: e.target.files[0]}},`${this.props.path}[${idx}]`,false)}}/>
            <span className="input-group-addon" style={{cursor:'pointer'}} onClick={() => {document.getElementById(`file${idx}`).value = null; this.props.handler({target:{value: ''}},`${this.props.path}[${idx}]`,false)}}><i className="glyphicon glyphicon-trash specific"></i></span>
          </div>
          <br/>
        </Col>)
    })
  }
  render(){
    let {docs, addFile} = this;
    return (
      <Card style={styles.marginStyle}>
        <CardHeader style={{paddingTop:4,paddingBottom:0}} title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate('works.create.groups.label.uploadDocs')}</div>}/>
        <CardText>
          <Row>
            {docs()}
            {this.state.fileCount != this.props.maxFile ?
              <Col xs={12} sm={4} md={3} lg={3}>
                <div className="material-icons" style={{cursor:'pointer'}} onClick={()=>{addFile()}}>add</div>
              </Col> :''}
          </Row>
        </CardText>
      </Card>
    )
  }
}
