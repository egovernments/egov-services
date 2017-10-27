import React, {Component} from 'react';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import {translate} from '../../common/common';

export default class UiFileTable extends Component {
	constructor(props) {
       super(props);
   	}

   	renderFileObject = (item, val, i) => {
   		if(this.props.readonly) {
   			return (
				<a href={window.location.origin + "/filestore/v1/files/id?tenantId=" + localStorage.tenantId + "&fileStoreId=" + this.props.getVal(item.jsonPath + "[" + i + "]." + val.id)} target="_blank">{translate("wc.craete.file.Download")}</a>
			);
   		} else {
   			<input type="file" onChange= {(e) => {
				this.props.handler({target:{value: e.target.files[0]}}, (item.jsonPath + "[" + i + "]." + val.id), item.isRequired, '', item.requiredErrMsg, item.patternErrMsg)
			}}/>
   		}
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
						<tbody>
							{item.fileList && item.fileList.length && item.fileList.map((v, i) => {
								<tr key={i}>
									<td>{i+1}</td>
									<td>
										<TextField
				              				className="cutustom-form-controll-for-textfield"
				              				id={item.jsonPath.split(".").join("-")}
											inputStyle={{"color": "#5F5C57"}}
											errorStyle={{"float":"left"}}
											fullWidth={true}
											disabled={this.props.readonly}
											value={this.props.getVal(item.jsonPath + "[" + i + "]." + v.name)}
											onChange={(e) => {
												this.props.handler(e, (item.jsonPath + "[" + i + "]." + v.name), item.isRequired, '', item.requiredErrMsg, item.patternErrMsg)
											}} />
									</td>
									<td>
										{this.renderFileObject(item, v, i)}
									</td>
								</tr>
							})}
						</tbody>
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
