import React, {Component} from 'react';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import styles from '../../../styles/material-ui';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import {translate} from '../common';

const customStyles = {
  titleStyle: {
    paddingBottom: 0
  }
}

export default class PdfViewer extends Component{

  constructor(props){
    super(props);
    this.state={
      pdfData : undefined
    };
  }

  render(){
    return(
      <Grid style={styles.fullWidth}>
        <Card style={styles.marginStyle}>
          <CardTitle title={translate(this.props.title)} style={customStyles.titleStyle} />
          <CardText>
            <iframe type="application/pdf" style={{width:'100%'}} height="500" src={this.props.pdfData}></iframe>
            {this.props.children}
          </CardText>
        </Card>
      </Grid>
    )
  }

}
