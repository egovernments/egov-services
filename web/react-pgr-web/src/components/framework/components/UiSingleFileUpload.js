import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';

export default class UiSingleFileUpload extends Component {
	constructor(props) {
       super(props);
   	}

	renderSingleFileUpload = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<RaisedButton
					  containerElement='label'
					  fullWidth={true} 
					  value={this.props.getVal(item.jsonPath)}
					  disabled={item.isDisabled}
					  label={item.label}>
					    <input type="file" style={{ display: 'none' }} onChange={(e) => this.props.handler({target:{value: e.target.files[0]}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}/>
					</RaisedButton>
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderSingleFileUpload(this.props.item)}
	      </div>
	    );
	}
}