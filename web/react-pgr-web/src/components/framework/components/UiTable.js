import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {translate} from '../../common/common';
import {connect} from 'react-redux';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');

class UiTable extends Component {
	constructor(props) {
       super(props);
   	}

   	componentWillMount() {
	    $('#searchTable').DataTable({
	       dom: 'lBfrtip',
	       buttons: [],
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
	         dom: 'lBfrtip',
	         buttons: [],
	          ordering: false,
	          bDestroy: true,
	          language: {
	             "emptyTable": "No Records"
	          }
	    });
  	}

  	render() {
  		let {resultList, rowClickHandler} = this.props;

  		const renderTable = function () {
  			return (
  				<Card className="uiCard">
		          <CardHeader title={<strong> {translate("ui.table.title")} </strong>}/>
		          <CardText>
		          <Table id="searchTable" bordered responsive className="table-striped">
		          <thead>
		            <tr>
		              {resultList.resultHeader && resultList.resultHeader.length && resultList.resultHeader.map((item, i) => {
		                return (
		                  <th key={i}>{translate(item.label)}</th>
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
			                          <td key={i2}>{item2 + ""}</td>
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