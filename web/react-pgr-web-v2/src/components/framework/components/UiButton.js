import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';

const style = {
  margin: '0 4px',
};

export default class UiButton extends Component {
	constructor(props) {
       super(props);
   	}

	renderBtn = (item,icon) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<RaisedButton icon={icon} style={style} id={item.label.split(".").join("-")} type={item.uiType || "button"} label={item.label} primary={typeof item.primary != 'undefined' ? item.primary : true} secondary={item.secondary || false} onClick={this.props.handler || function(){}} disabled={item.isDisabled ? true : false}/>
  				);
  		}
  	}

	render () {
		return this.renderBtn(this.props.item,this.props.icon)
	}
}
