import React, {Component} from 'react';
import {connect} from 'react-redux';
import ShowForm from '../../common/showForm';
import Api from '../../../api/api';

export default class grievanceCreateSD extends Component {
  constructor(props) {
    super(props);
    this.state={
      customFields : []
    };
  }
  componentWillMount(){
    var currentThis = this;
    Api.commonApiPost("/pgr/servicedefinition/_search",{serviceCode : 'PTPH'}).then(function(response)
    {
      currentThis.setState({customFields: response.attributes});
    },function(err) {

    });
  }
  render(){
    return(
      <div>
        <ShowForm data={this.state.customFields}/>
      </div>
    );
  }
}
