import React from 'react';
import ReactDOM from 'react-dom';
import Header from '../global/Header';
import Footer from '../global/Footer';
import {getUrlParameter} from '../global/Custom.js';
var serialize = require('form-serialize');

class App extends React.Component {
  render(){
    return(
      <div>
      <Header/>
      <Content/>
      <Footer/>
      </div>
    )
  }
}

class Content extends React.Component{

  constructor(props){
		super(props);

		this.state = {
      active: true
    }

    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);

	}

  handleChange(e){
    this.setState({active:!this.state.active});
	}

	handleSubmit(e){
    var form = document.querySelector('form');
    var obj = serialize(form, { hash: true, disabled:true, empty:true });
		console.log(obj);
		//ajax submit
		e.preventDefault();
	}

	close(){
		close();
	}

	render(){
		return(
			<div className="main-content">
				<form className="form-horizontal form-groups-bordered" encType="multipart/form-data" onSubmit={this.handleSubmit} autoComplete="off">
          <div>
            <div className="panel panel-primary" data-collapsed="0">
              <div className="panel-heading"></div>
              <div className="panel-body custom-form">
                <div className="form-group">
                  <label className="col-sm-2 control-label text-right">Code:<span className="mandatory"></span></label>
                  <div className="col-sm-3 add-margin">
                    <input id="code" name="code" className="form-control" required="required" type="text" maxLength="25"/>
                  </div>
                  <label className="col-sm-3 control-label text-right">Category Type:<span className="mandatory"></span></label>
                  <div className="col-sm-3 add-margin">
                    <input id="name" name="name" className="form-control" required="required" type="text" maxLength="50"/>
                  </div>
                </div>
                <Update change={this.handleChange} value={this.state.active}/>
              </div>
            </div>
            <div className="form-group text-center">
              <button type="submit" className="btn btn-primary" value="Save" id="buttonid">Save</button>
              <button type="button" className="btn btn-default" value="Reset" id="resetid">Reset</button>
              <a href="javascript:void(0)" className="btn btn-default" onClick={this.close}>Close</a>
            </div>
          </div>
	   			</form>
			</div>
		);
	}
}

class Update extends React.Component{
	render(){
		let type = getUrlParameter('type');
		return type === 'edit' ? (
			<div className="form-group">
				<label className="col-sm-2 control-label text-right">Active</label>
				<div className="col-sm-3 add-margin">
					<input id="activeid" name="active" type="checkbox" defaultValue={this.props.value} onChange={this.props.change} checked={this.props.value} />
				</div>
			</div>
		) : null;
	}
}

ReactDOM.render(<App/>, document.getElementById('root'));
