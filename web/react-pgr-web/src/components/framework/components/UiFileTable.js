import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import {translate} from '../../common/common';

export default class UiFileTable extends Component {
	constructor(props) {
       super(props);
   	}

   	renderFileObject = (item, i) => {
   		if(this.props.readonly) {
   			return (
				<a href={window.location.origin + "/filestore/v1/files/id?tenantId=" + localStorage.tenantId + "&fileStoreId=" + this.props.getVal(item.jsonPath + "[" + i + "]." + item.fileList.id)} target="_blank">{translate("wc.craete.file.Download")}</a>
			);
   		} else {
   			return (<input type="file" onChange= {(e) => {
				this.props.handler({target:{value: e.target.files[0]}}, (item.jsonPath + "[" + i + "]." + item.fileList.id), item.isRequired, '', item.requiredErrMsg, item.patternErrMsg)
			}}/>);
   		}
   	}

   	renderRowList = (item) => {
		   if(!item.fileCount){
			   item.fileCount=10;
		   }
   		let arr = [...Array(item.fileCount).keys()];
   		let value = "";
   		
   		return (
   			<tbody>
   				{	
   					arr && arr.length && arr.map((v, i) => {
   						let fileListName = item.jsonPath + "[" + i + "]." + item.fileList.name;
   						let fileStoreId = item.jsonPath + "[" + i + "]." + item.fileList.id;

   		 value=this.props.getVal(fileListName);
   		let idValue = this.props.getVal(fileStoreId);
   		if(value || idValue || !this.props.readonly){
								return (<tr key={i}>
											<td>{i+1}</td>
											<td>
												<TextField
						              				className="cutustom-form-controll-for-textfield"
						              				id={item.jsonPath.split(".").join("-")}
													inputStyle={{"color": "#5F5C57"}}
													errorStyle={{"float":"left"}}
													fullWidth={true}
													disabled={this.props.readonly}
													value={value}
													onChange={(e) => {
														this.props.handler(e, fileListName, item.isRequired, '', item.requiredErrMsg, item.patternErrMsg)
													}} />
											</td>
											<td>
												{this.renderFileObject(item, i)}
											</td>
										</tr>)
}
							})
   				}
   			</tbody>
   		)
   	}

   	renderFileTable = (item) => {
   		switch (this.props.ui) {
			case 'google':
				return (
					<Table className="table table-striped table-bordered" responsive>
						<thead>
							<tr>
								<th>#</th>
								<th>{translate("tl.create.license.table.documentName")}</th>
								<th>{translate("wc.create.groups.fileDetails.title")}</th>
							</tr>
						</thead>
						{this.renderRowList(item)}
					</Table>
				)
		}
   	}

   	render () {
   		return (
   			<div>
   				{this.renderFileTable(this.props.item)}
   			</div>
   		)
   	}
}