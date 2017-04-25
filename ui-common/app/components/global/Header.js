import React from 'react';
import '../../../node_modules/bootstrap/dist/css/bootstrap.css';
import '../../styles/custom.less';

export default class Header extends React.Component{
	render(){
		return(
			<header className="navbar navbar-fixed-top">
	   			<nav className="navbar navbar-default navbar-custom navbar-fixed-top">
	   				<div className="container-fluid">
	   					<div className="navbar-header col-md-10 col-xs-10">
	   						<a className="navbar-brand" href="javascript:void(0);">
	   							<img src={require('../../images/logo@2x.png')} height="60"/>
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
	   									<img src={require('../../images/egov.png')} title="Powered by eGovernments" height="20px" />
	   								</a>
	   							</li>

	   						</ul>
	   					</div>
	   				</div>
	   			</nav>
	   		</header>
		);
  }
}
