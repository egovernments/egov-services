import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {translate, Api} from 'egov-common-utility';
import {connect} from 'react-redux';
import _ from 'lodash';
import jp from "jsonpath";
import Button from './UiButton';
import UiCheckBox from './UiCheckBox';
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

var enumWithUnderscore = [{
	propertyName: "rateType"
}]

class UiTable extends Component {
	constructor(props) {
       super(props);
       this.state = {};
   	}

   	componentWillMount() {
	    $('#searchTable').DataTable({
	       dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
	       buttons: [ 'excel', {
                extend: 'pdf',
                orientation: 'landscape',
                pageSize: 'LEGAL',
								exportOptions: {
							      modifier: {
							         page: 'current'
							      		}
							   },
								 customize: function(doc) {
								      doc.defaultStyle.fontSize = 10; //<-- set fontsize to 16 instead of 10
											// doc.style.tableBorder=5;

								   },
									text: 'Pdf/Print',
            },'copy', 'csv'
						// , {
            //     extend: 'print',
            //     customize: function ( win ) {
            //         $(win.document.body)
            //             .css( 'font-size', '6pt' )
            //             // .prepend(
            //             //     '<img src="http://datatables.net/media/images/logo-fade.png" style="position:absolute; top:0; left:0;" />'
            //             // );
						//
            //         // $(win.document.body).find( 'table' )
            //         //     .addClass( 'compact' )
            //         //     .css( 'font-size', 'inherit' );
            //     }
            // }
					],
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
	         buttons: [ 'excel', {
	                extend: 'pdf',
	                orientation: 'landscape',
	                pageSize: 'LEGAL',
									exportOptions: {
								      modifier: {
								         page: 'current'
								      		}
								   },
									 customize: function(doc) {
									      doc.defaultStyle.fontSize = 10; //<-- set fontsize to 16 instead of 10
												// var myTable = document.getElementById('searchTable');
												// myTable.style.border="1px solid black";
									   }
										 ,
										 text: 'Pdf/Print',
	            },'copy', 'csv'
							// ,  {
              //   extend: 'print',
              //   customize: function ( win ) {
              //       $(win.document.body)
              //           .css( 'font-size', '8pt' )
              //       //     .prepend(
              //       //         '<img src="http://datatables.net/media/images/logo-fade.png" style="position:absolute; top:0; left:0;" />'
              //       //     );
							// 			//
              //       // $(win.document.body).find( 'table' )
              //       //     .addClass( 'compact' )
              //       //     .css( 'font-size', 'inherit' );
              //   }
            // }
					],
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
  		let {resultList, rowClickHandler,showDataTable,showHeader,rowButtonClickHandler,rowCheckboxClickHandler,selectedValue} = this.props;
  		let self = this;

  		const getNameById = function(item2, i2) {
				if(resultList.resultHeader[i2].isChecked){
									var selected=false;
									if(selectedValue == item2){
										selected=true;
									}
							return 	(<span style={{"margin-right":"20px"}}><UiCheckBox item={resultList.resultHeader[i2].checkedItem}ui="google" handler={()=>{rowCheckboxClickHandler(item2)}} isSelected={selected}/></span>)

				}else if(resultList.resultHeader[i2].isAction){
							if(_.isArray(item2)){
									return resultList.resultHeader[i2].actionItems.map((actionitem,index) =>{

											return 	(<span style={{"margin-right":"20px"}}><a onClick={()=>{rowButtonClickHandler(actionitem.url,item2[1])}}>{item2[0]}</a></span>)

					//		return 	(<span style={{"margin-right":"20px"}}><Button item={{"label": item2[0], "uiType":"primary"}} ui="google" handler={()=>{rowButtonClickHandler(actionitem.url,item2[1])}}/></span>)
					})

							}else{
             	return resultList.resultHeader[i2].actionItems.map((actionitem,index) =>{
							return 	(<span style={{"margin-right":"20px"}}><Button item={{"label": actionitem.label, "uiType":"primary"}} ui="google" handler={()=>{rowButtonClickHandler(actionitem.url,item2)}}/></span>)
				     	})
							}

				}else if(resultList.resultHeader[i2].url) {
  				return self.state[resultList.resultHeader[i2].label] ? self.state[resultList.resultHeader[i2].label][item2] : (item2 + "");
  			} else if(resultList.resultHeader[i2].isDate) {
					var _date = new Date(Number(item2));
					return ('0' + _date.getDate()).slice(-2) + '/'
					+ ('0' + (_date.getMonth()+1)).slice(-2) + '/'
					+ _date.getFullYear();
				} else if (resultList.resultHeader[i2].isComma) {
						let _commaVal = item2.toString();
			 		 var y = _commaVal.split(".")[1];
			 		 _commaVal =_commaVal.split(".")[0];
			 		 var lastThree = _commaVal.substring(_commaVal.length-3);
			 		 var otherNumbers = _commaVal.substring(0,_commaVal.length-3);
			 		 if(otherNumbers != '')
			 				 lastThree = ',' + lastThree;
			 		 var resCal = otherNumbers.replace(/\B(?=(\d{2})+(?!\d))/g, ",") + lastThree ;
			 		 var res = (y==null) ? resCal : (resCal + "." + y);
			 		 return res;
				}else {
  				return item2 === true ? translate("employee.createPosition.groups.fields.outsourcepost.value1") : (item2 === false ? translate("employee.createPosition.groups.fields.outsourcepost.value2") : (item2 === null ? "" : (item2 + "")));
  			}
  		}


			const _removeEnumUnderScore = function(item2, i2){
				if(resultList.resultHeader[i2].label == "Rate Type"){

				}
			}


  		const renderTable = function() {
  			return (
  				<Card className="uiCard">
		          <CardHeader title={<strong> {showHeader==undefined?translate("ui.table.title"):(showHeader?translate("ui.table.title"):"")} </strong>}/>
		          <CardText>
		          <Table className="table table-striped table-bordered" cellspacing="0" width="100%" id={(showDataTable==undefined)?"searchTable":(showDataTable?"searchTable":"")} responsive >
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
		                    <tr key={i} onClick={() => { if(!resultList.disableRowClick){rowClickHandler(i)}}}>
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
