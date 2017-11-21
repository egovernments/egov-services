import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import * as Templates from "../templates/index";

export default class TemplateParser extends Component {
	constructor(props) {
		super(props);
	}

	printTemplate = () => {
		var mywindow = window.open('', 'PRINT', 'height=400,width=600');
	    var cdn = `
	      <!-- Latest compiled and minified CSS -->
	      <link rel="stylesheet" media="all" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

	      <!-- Optional theme -->
	      <link rel="stylesheet" media="all" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">  `;
	    mywindow.document.write('<html><head><title> </title>');
	    mywindow.document.write(cdn);
	    mywindow.document.write('</head><body>');
	    mywindow.document.write(document.getElementById('printTemplate').innerHTML);
	    mywindow.document.write('</body></html>');

	    mywindow.document.close(); // necessary for IE >= 10
	    mywindow.focus(); // necessary for IE >= 10*/

	    setTimeout(function(){
	      mywindow.print();
	      mywindow.close();
	    }, 1000);
	}

	render() {
		const Template = Templates[this.props.match.params.templatePath];
		return(
			<div>
				<div id="printTemplate">
					{localStorage.reportData ? JSON.parse(localStorage.reportData).map((v, i) => {
						return (
							<div>
								<Template data={v}/>
								<br/>
								<div style={{"page-break-after": "always"}}></div>
							</div>
						)
					}) : ""}
				</div>
				<div style={{"textAlign": "center"}}>
					<RaisedButton label={"Print"} primary={true} onClick={this.printTemplate}/>
				</div>
			</div>
		)
	}
}
