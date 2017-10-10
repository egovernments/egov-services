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
					<RaisedButton id={item.label.split(".").join("-")} type={item.uiType || "button"} label={item.label} primary={typeof item.primary != 'undefined' ? item.primary : true} secondary={item.secondary || false} onClick={this.props.handler || function(){}} disabled={item.isDisabled ? true : false}/>
  				);
  		}
  	}

	render () {
		return this.renderBtn(this.props.item)
	}
}
