import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';

export default class UiButton extends Component {
	constructor(props) {
       super(props);
   	}

	renderBtn = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<RaisedButton type={item.uiType || "button"} label={item.label} primary={true} onClick={this.props.handler || function(){}}/>
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