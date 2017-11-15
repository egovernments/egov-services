import React, { Component } from "react";
import { Grid, Row, Col, Table, DropdownButton } from "react-bootstrap";
import TextField from "material-ui/TextField";
import FloatingActionButton from "material-ui/FloatingActionButton";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";
import { translate } from "../../common/common";
import _ from "lodash";
import ShowFields from "../showFields";
import { connect } from "react-redux";
import jp from "jsonpath";

var specifications = {};
let reqRequired = [];
class UiWindowForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      mockData: null,
	  valuesObj: {},
	  index:-1
    };
    this.handleChange = this.handleChange.bind(this);
    this.getValueFn = this.getValueFn.bind(this);
  }
  setLabelAndReturnRequired(configObject) {
    if (configObject && configObject.groups) {
      for (var i = 0; configObject && i < configObject.groups.length; i++) {
        configObject.groups[i].label = translate(configObject.groups[i].label);
        for (var j = 0; j < configObject.groups[i].fields.length; j++) {
          configObject.groups[i].fields[j].label = translate(
            configObject.groups[i].fields[j].label
          );
          if (
            configObject.groups[i].fields[j].isRequired &&
            !configObject.groups[i].fields[j].hide &&
            !configObject.groups[i].hide
          )
            reqRequired.push(configObject.groups[i].fields[j].jsonPath);
        }

        if (
          configObject.groups[i].children &&
          configObject.groups[i].children.length
        ) {
          for (var k = 0; k < configObject.groups[i].children.length; k++) {
            this.setLabelAndReturnRequired(configObject.groups[i].children[k]);
          }
        }
      }
    }
  }
  initData() {
    var self = this;
    specifications = require(`../../framework/specs/${this.props.item.subPath}`)
      .default;
    var result =
      typeof results == "string" ? JSON.parse(specifications) : specifications;
    let obj = specifications["legal.create"];
    self.setLabelAndReturnRequired(obj);

    this.setState({
      mockData: JSON.parse(JSON.stringify(specifications))
    });
  }

  componentDidMount() {
    this.initData();
  }
   editRow =(index)=>{
	let {item,getVal}=this.props;
	 var jsonPath= item.jsonPath+"."+item.arrayPath+"["+index+"]";
	 var data = getVal(jsonPath);
	  this.setState({
		 valuesObj:data,
		  index:index,
		  open:true

	  })


   }
    deleteRow =(index)=>{
	   
   }
  renderTable = (item, _internal_val = []) => {	 
    if (item.tableConfig) {
      return (
        <Col xs={12} md={6}>
          <table className="table table-striped table-bordered" responsive>
            <thead>
              <tr>
                <th>#</th>
                {item.tableConfig.header.map((v, i) => {
                  var style = {};
                  if (v.style) {
                    style = v.style;
                  }

                  return (
                    <th style={style} key={i}>
                      {translate(v.label)}
                    </th>
                  );
                })}
				<th>{translate("reports.common.action")}</th>
              </tr>
            </thead>
            <tbody>
               { _.isArray(_internal_val) && _internal_val.map((v, i) => {
                return (
                  <tr>
                    <td>{i}</td>
                    <td>{v}</td>
					<td>
						<div className="material-icons" onClick={()=>{
							this.editRow(i)
						}}>edit</div>
						<div className="material-icons" onClick={()=>{
							this.deleteRow(i)
						}}>delete</div>
					</td>
                  </tr>
                );
              })} 
            </tbody>
          </table>
        </Col>
      );
    } else {
      return (
        <Col xs={12} md={6}>
          <TextField
            className="cutustom-form-controll-for-textfield"
            id={item.jsonPath.split(".").join("-")}
            floatingLabelStyle={{
              color: "#A9A9A9",
              fontSize: "20px",
              "white-space": "nowrap"
            }}
            inputStyle={{ color: "#5F5C57" }}
            floatingLabelFixed={true}
            maxLength={item.maxLength || ""}
            style={{ display: item.hide ? "none" : "inline-block" }}
            errorStyle={{ float: "left" }}
            fullWidth={true}
            floatingLabelText={
              <span>
                {item.label}{" "}
                <span style={{ color: "#FF0000" }}>
                  {item.isRequired ? " *" : ""}
                </span>
              </span>
            }
            //value = {this.state.valueList.join(", ")}
            value={_internal_val && _internal_val.constructor == Array ? _internal_val.join(", ") : ""}
            disabled={true}
          />
        </Col>
      );
    }
  };

  renderField = item => {
    let val = this.props.getVal(item.jsonPath+"."+item.arrayPath);
    if (item.displayField && val && val.constructor == Array) {
      val = jp.query(val, `$..${item.displayField}`);
    }
    if (this.props.readonly === "true") {
      return (
        <div>
          <Col xs={12}>
            <label>
              <span style={{ fontWeight: 500, fontSize: "13px" }}>
                {translate(item.label)}
              </span>
            </label>
          </Col>
          <Col xs={12}>
            {val && val.constructor == Array ? val.join(", ") : ""}
          </Col>
        </div>
      );
    } else {
      return (
        <Row>
          {this.renderTable(item, val)}

          <Col xs={12} md={6}>
            <FloatingActionButton
              style={{ marginTop: 39 }}
              mini={true}
              onClick={() => {
                this.handleOpen();
              }}
            >
              <i className="material-icons">add</i>
            </FloatingActionButton>
          </Col>
        </Row>
      );
    }
  };

  renderArrayField = item => {
    let { mockData } = this.state;
    let { fieldErrors } = this.props;
    let { handleChange, getValueFn } = this;
    var self = this;
    switch (this.props.ui) {
      case "google":
        return (
          <div>
            {this.renderField(item)}
            <Dialog
              title={this.props.item.label}
              modal={true}
              actions={[
                <FlatButton
                  label={translate("pt.create.groups.ownerDetails.fields.add")}
                  disabled={_.isEmpty(this.state.valuesObj)}
                  secondary={true}
                  style={{ marginTop: 39 }}
                  onClick={e => {debugger;
                    var oldData = self.props.getVal(self.props.item.jsonPath+"."+self.props.item.arrayPath);
					if(self.state.index>=0){
						oldData[self.state.index] = self.state.valuesObj;
					}else{
                      _.isArray(oldData)
                      ? oldData.push(self.state.valuesObj)
                      : (oldData = [self.state.valuesObj]);
					}
                   
                    self.props.handler(
                      { target: { value: oldData } },
                      self.props.item.jsonPath+"."+self.props.item.arrayPath,
                      self.props.item.isRequired ? true : false,
                      "",
                      self.props.item.requiredErrMsg,
                      self.props.item.patternErrMsg
                    );
                    self.setState({
                      valuesObj: {},
					  open:false,
					  index:-1
                    });
                  }}
                />,
                <FlatButton
                  label={translate("pt.create.button.viewdcb.close")}
                  primary={true}
                  onClick={this.handleClose}
                />
              ]}
              modal={false}
              open={this.state.open}
              contentStyle={{ "width": "80%", "max-width": "80%" }}
              onRequestClose={this.handleClose}
              autoScrollBodyContent={true}
            >
              {" "}
              <div>
                {!_.isEmpty(mockData) &&
                mockData["legal.create"] && (
                  <ShowFields
                    groups={mockData["legal.create"].groups}
                    noCols={mockData["legal.create"].numCols}
                    ui="google"
                    handler={handleChange}
                    getVal={getValueFn}
                    fieldErrors={fieldErrors}
                    useTimestamp={
                      mockData["legal.create"].useTimestamp || false
                    }
                    addNewCard={""}
                    removeCard={""}
                  />
                )}
              </div>
            </Dialog>
          </div>
        );
    }
  };

  handleChange = (
    e,
    property,
    isRequired,
    pattern,
    requiredErrMsg,
    patternErrMsg
  ) => {
    var newObj = _.set(this.state.valuesObj, property, e.target.value);
    this.setState({
      valuesObj: newObj
    });

    // dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  };

  getValueFn = path => {
    return typeof _.get(this.state.valuesObj, path) != "undefined"
      ? _.get(this.state.valuesObj, path)
      : "";
  };
  handleOpen = () => {
    this.setState({
      open: true
    });
  };

  handleClose = () => {
    this.setState({
      open: false
    });
  };

  render() {

    return <div>{this.renderArrayField(this.props.item)}</div>;
  }
}
const mapStateToProps = state => ({
  fieldErrors: state.frameworkForm.fieldErrors,
  formData: state.frameworkForm.form
});

export default connect(mapStateToProps)(UiWindowForm);
