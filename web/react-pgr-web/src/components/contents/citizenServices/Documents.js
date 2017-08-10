import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import Checkbox from 'material-ui/Checkbox';
import {translate} from '../../common/common';
import PropTypes from 'prop-types';
import FileInput from './FileInput';

export default class Documents extends Component{

   constructor(){
     super();
   }

   componentWillMount(){

   }

   renderFileInputs = () =>{

     if(!this.props.items)
        return null;

     return this.props.items.map((attribute, index)=>{
          var field = this.props.files.find((field) => field.code == attribute.key);
          var files = field ? field.files : [];
          console.log('field', attribute.code, field);

          if(attribute.isActive){
            return <FileInput key={index} code={attribute.key} name={attribute.name} addFileHandler={this.props.addFileHandler}
                 removeFileHandler={this.props.removeFileHandler} files={files} />;
          }
     });

   }

   render(){
      const fileInputs=this.renderFileInputs();
      return(
       <Row>
        {fileInputs}
       </Row>
      )
   }
}

Documents.propTypes = {
  items:  React.PropTypes.arrayOf(React.PropTypes.shape({
     key: React.PropTypes.string.isRequired,
     name: React.PropTypes.string.isRequired
   })).isRequired
};
