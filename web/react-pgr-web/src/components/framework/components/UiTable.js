import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {translate} from '../../common/common';
import {connect} from 'react-redux';
import Api from '../../../api/api';
import _ from 'lodash';
import jp from "jsonpath";
// const $ = require('jquery');
// $.DataTable = require('datatables.net');
// const dt = require('datatables.net-bs');
//
// const buttons = require('datatables.net-buttons-bs');
//
// require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
// require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
// require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
// require('datatables.net-buttons/js/buttons.print.js'); // Print view button

import $ from 'jquery';
import 'datatables.net-buttons/js/buttons.html5.js';// HTML 5 file export
import 'datatables.net-buttons/js/buttons.flash.js';// Flash file export
import jszip from 'jszip/dist/jszip';
import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
import 'datatables.net-buttons/js/buttons.flash.js';
import 'datatables.net-buttons-bs';

pdfMake.vfs = pdfFonts.pdfMake.vfs;

class UiTable extends Component {
	constructor(props) {
       super(props);
       this.state = {};
   	}

   	componentWillMount() {
	    $('#searchTable').DataTable({
	       dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
	       buttons: [ 'excel', 'pdf','copy', 'csv',  'print'],
	       bDestroy: true,
	       language: {
	           "emptyTable": "No Records"
	       }
	    });
   	}

   	componentWillUnmount() {
    	$('#searchTable').DataTable().destroy(true);
  	}

  	componentWillUpdate() {
  		let {flag} = this.props;
	    if(flag == 1) {
	      flag = 0;
	      $('#searchTable').dataTable().fnDestroy();
	    }
	}

	componentDidUpdate() {
	    $('#searchTable').DataTable({
	         dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
	         buttons: [ 'excel', 'pdf','copy', 'csv',  'print'],
	          ordering: false,
	          bDestroy: true,
	          language: {
	             "emptyTable": "No Records"
	          }
	    });
  	}

  	componentDidMount() {
  		let self = this;
  		if(this.props.resultList.resultHeader && this.props.resultList.resultHeader.length) {
	    	for(let m=0; m<this.props.resultList.resultHeader.length; m++) {
	    		if(this.props.resultList.resultHeader[m].url) {
	    			let splitArray = this.props.resultList.resultHeader[m].url.split("?");
					let context="";
					let id={};
					for (var j = 0; j < splitArray[0].split("/").length; j++) {
						if (j==(splitArray[0].split("/").length-1)) {
								context+=splitArray[0].split("/")[j];
						} else {
							context+=splitArray[0].split("/")[j]+"/";
						}
					}

					let queryStringObject = splitArray[1].split("|")[0].split("&");
					for (var i = 0; i < queryStringObject.length; i++) {
						if (i) {
							id[queryStringObject[i].split("=")[0]]=queryStringObject[i].split("=")[1];
						}
					}
	    			Api.commonApiPost(context, id, {}, "", self.props.useTimestamp || false).then(function(response) {
	    				let keys = jp.query(response,splitArray[1].split("|")[1]);
						let values = jp.query(response,splitArray[1].split("|")[2]);
						let dropDownData = {};
						for (var k = 0; k < keys.length; k++) {
							dropDownData[keys[k]] = values[k];
						}
						self.setState({
							[self.props.resultList.resultHeader[m].label]: dropDownData 	
						}, function() {
						})
	    			}, function(err){

	    			})
	    		}
	    	}
	    }
  	}

  	render() {
  		let {resultList, rowClickHandler,showDataTable,showHeader} = this.props;
  		let self = this;

  		const getNameById = function(item2, i2) {
  			if(resultList.resultHeader[i2].url) {
  				return self.state[resultList.resultHeader[i2].label] ? self.state[resultList.resultHeader[i2].label][item2] : (item2 + "");
  			} else {
  				return item2 === true ? translate("employee.createPosition.groups.fields.outsourcepost.value1") : (item2 === false ? translate("employee.createPosition.groups.fields.outsourcepost.value2") : (item2 === null ? "" : (item2 + "")));
  			}
  		}

  		const renderTable = function() {
  			return (
  				<Card className="uiCard">
		          <CardHeader title={<strong> {showHeader==undefined?translate("ui.table.title"):(showHeader?translate("ui.table.title"):"")} </strong>}/>
		          <CardText>
		          <Table id={(showDataTable==undefined)?"searchTable":(showDataTable?"searchTable":"")} bordered responsive className="table-striped">
		          <thead>
		            <tr>
		              {resultList.resultHeader && resultList.resultHeader.length && resultList.resultHeader.map((item, i) => {
		                return (
		                  <th  key={i}>{translate(item.label)}</th>
		                )
		              })}

		            </tr>
		          </thead>
		          <tbody>

		                {resultList.hasOwnProperty("resultValues") && resultList.resultValues.map((item, i) => {
		                  return (
		                    <tr key={i} onClick={() => {rowClickHandler(i)}}>
		                      {
		                      	item.map((item2, i2)=>{
			                        return (
			                          <td  key={i2}>{typeof item2 != "undefined" ? getNameById(item2, i2) : ""}</td>
			                        )
		                      })}
		                    </tr>
		                    )

		                })}


		          </tbody>
		        </Table>
		      </CardText>
		      </Card>
  			)
  		}

  		return (
  			<div>
  				{this.props.resultList && renderTable()}
  			</div>
  		)
  	}
}

const mapStateToProps = state => ({ flag:state.report.flag });

const mapDispatchToProps = dispatch => ({
  setFlag:(flag)=>{
      dispatch({type:"SET_FLAG",flag})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(UiTable);
