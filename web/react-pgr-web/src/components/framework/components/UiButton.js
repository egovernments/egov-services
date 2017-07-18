import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';

export default class UiButton extends Component {
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