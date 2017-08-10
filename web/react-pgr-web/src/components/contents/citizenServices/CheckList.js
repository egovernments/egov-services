import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import Checkbox from 'material-ui/Checkbox';
import {translate} from '../../common/common';
import PropTypes from 'prop-types';

const styles = {
  checkbox: {
    marginBottom: 16,
    marginTop: 16
  }
};

export default class CheckList extends Component{

  constructor(){
      super();
  }

  renderCheckBoxItems = () =>{

    if(!this.props.items)
       return null;

    return this.props.items.map((attribute, index)=>{
         if(attribute.isActive)
           return <CheckBoxItem key={index} obj={attribute}/>
    });
  }

  render(){
      const checkBoxList = this.renderCheckBoxItems();
      return(
        <Col xs={12}>
          {checkBoxList}
        </Col>
      )
  }
}

class CheckBoxItem extends Component{
    render() {
        return (
          <Checkbox
            ref = {this.props.obj.key}
            label = {translate(this.props.obj.name)}
            style = {styles.checkbox}
          />)
    }
}

CheckList.propTypes = {
  items:  React.PropTypes.arrayOf(React.PropTypes.shape({
     key: React.PropTypes.string.isRequired,
     name: React.PropTypes.string.isRequired
   })).isRequired
};
