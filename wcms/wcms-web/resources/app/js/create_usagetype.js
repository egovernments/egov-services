/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2017>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
class App extends React.Component {
	
	render() {
		return (
			<div>
				<Content/>
			</div>
		);
	}
}

/*class Header extends React.Component{
	render(){
		return(
			<header className="navbar navbar-fixed-top">
	   			<nav className="navbar navbar-default navbar-custom navbar-fixed-top">
	   				<div className="container-fluid">
	   					<div className="navbar-header col-md-10 col-xs-10">
	   						<a className="navbar-brand" href="javascript:void(0);">
	   							<img src="" height="60"/>
	   							<div>
	   								<span className="title2">
	   									Create Usage Type
	   								</span>
	   							</div>
	   						</a>
	   					</div>
	   					<div className="nav-right-menu col-md-2 col-xs-2">
	   						<ul className="hr-menu text-right">
	   							<li className="ico-menu">
	   								<a href="http://www.egovernments.org" target="_blank">
	   									<img src="../../resources/global/images/logo@2x.png" title="Powered by eGovernments" height="20px" />
	   								</a>
	   							</li>
	   							
	   						</ul>
	   					</div>
	   				</div>
	   			</nav>
	   		</header>
		);
	}
}*/

class Content extends React.Component{

	render(){
		return(
			<div className="main-content">
				<form className="form-horizontal form-groups-bordered" encType="multipart/form-data" autoComplete="off">
	   				<Create/>
	   			</form>
			</div>
		);
	}
}

class Create extends React.Component{

	constructor(props){
		super(props);

		this.state = {
            formValues: {}
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

	}

	handleChange(e){
		e.preventDefault();
        let formValues = this.state.formValues;
        let name = e.target.name;
        let value = e.target.value;

        formValues[name] = value;

        this.setState({formValues});
	}

	handleSubmit(e){
		if($('form').valid()){
			console.log(this.state.formValues);
			//ajax submit
		}
		e.preventDefault();
	}

	close(){
		close();
	}

	render(){
		return(
			<div>
				<div className="panel panel-primary" data-collapsed="0">
					<div className="panel-heading"></div>
					<div className="panel-body custom-form">
						<div className="form-group">
							<label className="col-sm-3 control-label text-right">Code:<span className="mandatory"></span></label>
							<div className="col-sm-2 add-margin">
								<input id="code" name="code" onChange={this.handleChange} data-pattern="alphanumericwithspecialcharacters" className="form-control patternvalidation" required="required" type="text" maxLength="25"/>
							</div>
							<label className="col-sm-2 control-label text-right">Usage Type:<span className="mandatory"></span></label>
							<div className="col-sm-2 add-margin">
								<input id="name" name="name" onChange={this.handleChange} data-pattern="alphabetwithspace" className="form-control patternvalidation" required="required" type="text" maxLength="50"/>
							</div>
						</div>
						<Update/>
					</div>
				</div>
				<div className="form-group text-center">
					<button type="button" onClick={this.handleSubmit} className="btn btn-primary" value="Save" id="buttonid">Save</button>
					<button type="button" className="btn btn-default" value="Reset" id="resetid">Reset</button>
					<a href="javascript:void(0)" className="btn btn-default" onClick={this.close}>Close</a>
				</div>
			</div>
		);
	}
}

class Update extends React.Component{
	render(){
		let type = getUrlParameter('type');
		return type === 'edit' ? (
			<div className="form-group">
				<label className="col-sm-3 control-label text-right">Active</label>
				<div className="col-sm-2 add-margin">
					<input id="activeid" name="active" type="checkbox"/>
				</div>
			</div>
		) : null;
	}
}

/*class Footer extends React.Component{
	render(){
		return(
			<footer className="main">
				<a href="http://eGovernments.org" target="_blank"><span data-translate="core.lbl.page.footer">Powered by eGovernments Foundation</span></a>
			</footer>
		);
	}
}*/

ReactDOM.render(<App />, document.getElementById('app'));