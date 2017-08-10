import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import Dialog from 'material-ui/Dialog';
import Fields from '../../common/Fields';
import {translate} from '../../common/common';
import CheckList from './CheckList';
import Documents from './Documents';
import PropTypes from 'prop-types';
import FileInput from './FileInput';

//group constraint value
const CONDITION_ALL_REQUIRED = 'ALL_REQUIRED';
const CONDITION_AT_LEAST_ONE_REQUIRED = 'AT_LEAST_ONE_REQUIRED';

//codes of the documents and checklists
const CHECKLIST_CODE = "CHECKLIST";
const DOCUMENTS_CODE = "DOCUMENTS";

const styles={
  cardStyle : {
    marginBottom:16
  }
}

let mapStateToProps, mapDispatchToProps;

class FormSection extends Component{

      constructor(){
        super();
      }

      componentWillMount(){

      }

      getSectionValidationInformation = (condition) => {
          if(condition === CONDITION_ALL_REQUIRED)
             return "*All fields are required";
          else if(condition === CONDITION_AT_LEAST_ONE_REQUIRED)
             return "*At least, Please fill any one field"
          else
             return "";
      }

      getTitle = (groupName, constraint) => {
        if(!groupName)
          return null;

        return (
          <CardTitle
            title={groupName}
            subtitle={this.getSectionValidationInformation(constraint)}
         />
        )
      };

      renderFiles = (fields) => {
        return fields.map((field, index)=>{
          if(field.code != CHECKLIST_CODE && field.code != DOCUMENTS_CODE){
            if(field.dataType == "file" || field.dataType == "multifile"){
              var fileField = this.props.files.find((fileField) => fileField.code == field.code);
              var files = fileField ? fileField.files : [];
              return (<Col key={index} xs={12} md={4} lg={4}>
                  <FileInput key={index} dialogOpener={this.props.dialogOpener} error={this.props.errors[field.code] || ""} isRequired={field.required} isMultipleFile={field.dataType == "multifile"} code={field.code} name={field.description} addFileHandler={this.props.addFile}
                       removeFileHandler={this.props.removeFile} files={files}></FileInput>
              </Col>)
            }
          }
        });
      }

      renderFields = (fields) => {

          const renderSections = [fields.map((field, index)=>{
            if(field.code != CHECKLIST_CODE && field.code != DOCUMENTS_CODE){
              if(field.dataType != "file" && field.dataType != "multifile"){
                field.dataType = field.dataType.toLowerCase() == "number"? "integer" : field.dataType.toLowerCase();

                field.attributes = field.attributes || [];
                return <Fields key={index} obj={field} value={this.props.values[field.code] || ""} error={this.props.errors[field.code] || ""} handler={this.props.handler}></Fields>;
              }
            }

            /*else if (field.code == CHECKLIST_CODE){
              return <CheckList key={index} items={field.attribValues}></CheckList>
            }
            else if(field.code == DOCUMENTS_CODE){
               return <Documents key={index} items={field.attribValues} addFileHandler={this.props.addFile}
                   removeFileHandler={this.props.removeFile}
                   files={this.props.files}></Documents>
            }*/
          })];

          return renderSections;
      };

      render(){

        const fields=this.renderFields(this.props.fields);
        const files=this.renderFiles(this.props.fields);

        const obj={code:"test", required:true, description:"Test DateTime"};

        return(
          <Card style={styles.cardStyle}>
            {this.getTitle(this.props.name, this.props.constraint)}
            <CardText>
            <Grid fluid={true}>
                <Row>
                  {fields}
                </Row>
                <br/>
                <Row>
                  {files}
                </Row>
            </Grid>
           </CardText>
          </Card>
        )
     }
}

FormSection.propTypes = {
  fields:  React.PropTypes.arrayOf(React.PropTypes.shape({
     variable: React.PropTypes.bool.isRequired,
     code: React.PropTypes.string.isRequired,
     required: React.PropTypes.bool.isRequired
   })).isRequired
};

export default FormSection;
