import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class CustomButton extends Component {
	constructor(props) {
       super(props);
   	}

	renderBtn = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<RaisedButton label={item.label} primary={true} onClick={this.props.handler}/>
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderBtn(this.props.item)}
	      </div>
	    );
	}
}