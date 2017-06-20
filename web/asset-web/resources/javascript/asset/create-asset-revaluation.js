class Revaluation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
        	assetSet: {},
        	functions: [],
        	funds: [],
          schemes: [],
          subSchemes: [],
          fixedAssetAccount: [],
          "revaluationSet": {
            "tenantId": tenantId,
            "id": "",
            "assetId": "",
            "currentCapitalizedValue": "",
            "typeOfChange": "",
            "revaluationAmount": "",
            "valueAfterRevaluation": "",
            "revaluationDate": "",
            "reevaluatedBy": "",
            "reasonForRevaluation": "",
            "fixedAssetsWrittenOffAccount": "",
            "function": "",
            "fund": "",
            "scheme": "",
            "subScheme": "",
            "comments": "",
            "status": "ACTIVE",
            "auditDetails": ""
          }
        };
        this.handleChange = this.handleChange.bind(this);
        this.close = this.close.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.createRevaluation = this.createRevaluation.bind(this);
    }

    setInitialState(initState) {
    	this.setState(initState);
    }

    componentDidMount() {
      if(window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if(logo_ele && logo_ele[0]) {
          document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
        }
      }

      let id = getUrlVars()["id"], _this = this, count = 4, _state = {};
    	$("#revaluationDate").datepicker({
    		format: "dd/mm/yyyy",
    		autoclose: true
    	});

      $("#revaluationDate").on("change", function(e) {
        _this.setState({
          revaluationSet: {
            ..._this.state.revaluationSet,
            "revaluationDate": e.target.value
          }
        })
      })

    	const checkCountAndCall = function(key, res) {
    		_state[key] = res;
        if(key == "assetSet") {
          _this.setState({
            revaluationSet: {
              ..._this.revaluationSet,
              assetId: res.id
            }
          })
        }
    		count--;
    		if(count == 0)
    			_this.setInitialState(_state);
    	}

    	getCommonMasterById("asset-services", "assets", id, function(err, res) {
          if(res) {
          	checkCountAndCall("assetSet", res["Assets"] && res["Assets"][0] ? res["Assets"][0] : {});
            commonApiPost("asset-services", "assets", "currentValue/_search", {tenantId, assetId: res["Assets"][0].id}, function(er, res) {
              if(res && res.AssetCurrentValue) {
                _this.setState({
                  revaluationSet: {
                    ..._this.state.revaluationSet,
                    currentCapitalizedValue: res.AssetCurrentValue.currentAmmount
                  }
                })
              }
            });
          } else {
          	console.log(err);
          }
      });

      getDropdown("assignments_fund", function(res) {
      	checkCountAndCall("funds", res);
      });

      getDropdown("assignments_function", function(res) {
      	checkCountAndCall("functions", res);
      });

      commonApiPost("egf-masters", "accountcodepurposes", "_search", {tenantId, name:"Fixed Assets Written off"}, function(err, res2){
        if(res2){
          getDropdown("fixedAssetAccount", function(res) {
            for(var i= 0; i<res.length; i++) {
              res[i].name = res[i].glcode + "-" + res[i].name;
            }
            checkCountAndCall("fixedAssetAccount", res);
          }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
        } else {
          checkCountAndCall("fixedAssetAccount", []);
        }
      })

      commonApiPost("hr-employee", "employees", "_loggedinemployee", {tenantId}, function(err, res) {
        if(res && res.Employee && res.Employee[0] && res.Employee[0].user && res.Employee[0].user.userName) {
          _this.setState({
            revaluationSet: {
              ..._this.revaluationSet,
              reevaluatedBy: res.Employee[0].user.userName
            }
          })
        }
      })
    }

    handleChange(e, name) {
      var _this = this;
      if(name == "scheme") {
        commonApiPost("egf-masters", "subschemes", "_search", {tenantId, scheme: e.target.value}, function(err, res) {
          if(res) {
            _this.setState({
              subSchemes: res["subSchemes"] || []
            })
          }
        })
      } else if(name == "fund") {
        commonApiPost("egf-masters", "schemes", "_search", {tenantId, fund: e.target.value}, function(err, res) {
          if(res) {
            _this.setState({
              schemes: res["schemes"] || []
            })
          }
        })
      } else if(name == "revaluationAmount" || name == "typeOfChange" && this.state.assetSet.grossValue) {
        switch(name) {
          case 'revaluationAmount':
            if(this.state.typeOfChange) {
              switch (this.state.typeOfChange) {
                case 'INCREASE':
                  setTimeout(function() {
                    _this.setState({
                      revaluationSet: {
                        ..._this.state.revaluationSet,
                        "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) + Number(e.target.value)
                      }
                    })
                  }, 200);
                  break;
                case 'DECREASE':
                  setTimeout(function() {
                    _this.setState({
                      revaluationSet: {
                        ..._this.state.revaluationSet,
                        "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) - Number(e.target.value)
                      }
                    });
                  }, 200);
                  break;
              }
            }
            break;
          case 'typeOfChange':
            if(this.state.revaluationAmount) {
              switch (this.state.typeOfChange) {
                case 'INCREASE':
                  setTimeout(function() {
                    _this.setState({
                      revaluationSet: {
                        ..._this.state.revaluationSet,
                        "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) + Number(e.target.value)
                      }
                    })
                  }, 200);
                  break;
                case 'DECREASE':
                  setTimeout(function() {
                    _this.setState({
                      revaluationSet: {
                        ..._this.state.revaluationSet,
                        "valueAfterRevaluation": Number(_this.state.assetSet.grossValue) - Number(e.target.value)
                      }
                    });
                  }, 200);
                  break;
              }
            }
            break;
        }
      }

      this.setState({
        revaluationSet: {
          ...this.state.revaluationSet,
          [name]: e.target.value
        }
      })
    }

    close() {
      open(location, '_self').close();
  	}

    createRevaluation(e) {
      e.preventDefault();
      var tempInfo = Object.assign({}, this.state.revaluationSet), _this = this;
      var body = {
        RequestInfo: requestInfo,
        Revaluation: tempInfo
      };

      //return console.log(JSON.stringify(body));
       $.ajax({
            url: baseUrl + "/assets/reevaluate/_create",
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(body),
            contentType: 'application/json',
            headers:{
                'auth-token': authToken
            },
            success: function(res) {
                  window.location.href=`app/asset/create-asset-ack.html?name=${_this.state.assetSet.name}&type=&value=revaluate&code=${_this.state.assetSet.code}`;
            },
            error: function(err) {
              console.log(err);
              var _err = err["responseJSON"].Error.message || "";
              if(err["responseJSON"].Error.fields && Object.keys(err["responseJSON"].Error.fields).length) {
                for(var key in err["responseJSON"].Error.fields) {
                  _err += "\n " + key + "- " + err["responseJSON"].Error.fields[key] + " "; //HERE
                }
                showError(_err);
              } else if(_err) {
                showError(_err);
              } else {
                showError(err["statusText"]);
              }
            }
        })
    }

  	render() {
      let {handleChange, close} = this;
      let {assetSet, functions, funds, revaluationSet, schemes, subSchemes, fixedAssetAccount} = this.state;

      const renderOptions = function(list) {
      	if(list) {
      		if(list.constructor == Array) {
      			return list.map((item, ind)=> {
                  if(typeof item == "object") {
                    return (<option key={ind} value={item.id}>
                          {item.name}
                    </option>)
                  } else {
                    return (<option key={ind} value={item}>
                          {item}
                    </option>)
                  }
              	})
      		} else {
      			return Object.keys(list).map((k, index)=> {
	                return (<option key={index} value={k}>
	                        {list[k]}
	                  </option>)
               	})
      		}
      	}
      }

      return (
      	<div>
      		<h3 > Create Asset Revaluation </h3>
      		<form onSubmit={(e) => createRevaluation(e)}>
	            <div className="form-section">
	              <div className="row">
	                <div className="col-md-8 col-sm-8">
	                  <h3 className="categoryType">Asset Details </h3>
	                </div>
	                <div className="col-md-4 col-sm-4 text-right">
	                  
	                </div>
	              </div>
	              <div className="form-section-inner">
		            <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="assetCode">Asset Code </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.code} </label>
                          	</div>
                          </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Name </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.name}</label>
                          	</div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
	                  <div className="col-sm-6">
	                      <div className="row">
	                        <div className="col-sm-6 label-text">
	                          <label>Current Capitalized Value </label>
	                        </div>
	                        <div className="col-sm-6 label-view-text">
	                          <label>{revaluationSet.currentCapitalizedValue}</label>
	                        </div>
	                    </div>
	                  </div>
	                </div>
		          </div>
		        </div>
		        <div className="form-section">
	              <div className="row">
	                <div className="col-md-8 col-sm-8">
	                  <h3 className="categoryType">Asset Revaluation Details </h3>
	                </div>
	                <div className="col-md-4 col-sm-4 text-right">
	                  
	                </div>
	              </div>
	              <div className="form-section-inner">
		            <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Type Of Change </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.typeOfChange} onChange={(e) => handleChange(e, "typeOfChange")}>
                                <option value="">Select Type Of Change</option>
                                <option value="INCREASE">Increase</option>
                                <option value="DECREASE">Decrease</option>
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Revaluation Amount <span>*</span> </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input type="number" value={revaluationSet.revaluationAmount} onChange={(e) => handleChange(e, "revaluationAmount")} required/>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Revaluation Date </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <input id="revaluationDate" className="datepicker" type="text" value={revaluationSet.revaluationDate} onChange={(e) => handleChange(e, "revaluationDate")}/>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Revaluated By </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{revaluationSet.reevaluatedBy}</label>
                            </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Reason For Revaluation </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <textarea value={revaluationSet.reasonForRevaluation} onChange={(e) => handleChange(e, "reasonForRevaluation")}></textarea>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Value After Reevaluation </label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" value={revaluationSet.valueAfterRevaluation} disabled/>
                          </div>
                        </div>
                      </div>
                    </div>
		          </div>
		        </div>
		        <div className="form-section">
	              <div className="row">
	                <div className="col-md-8 col-sm-8">
	                  <h3 className="categoryType">Accounting Details </h3>
	                </div>
	                <div className="col-md-4 col-sm-4 text-right">
	                  
	                </div>
	              </div>
	              <div className="form-section-inner">
		              <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Asset Account Code </label>
                            </div>
                            <div className="col-sm-6 label-view-text">
                              <label>{assetSet.assetCategory && assetSet.assetCategory.assetAccount}</label>
                          	</div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Revaluation Reserve Account Code </label>
                            </div>
                            <div className="col-sm-6 display-span">
                              <span>{assetSet.assetCategory && assetSet.assetCategory.revaluationReserveAccount}</span>
                          	</div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Fixed Assets Written Off Account Code </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.fixedAssetsWrittenOffAccount} onChange={(e) => handleChange(e, "fixedAssetsWrittenOffAccount")}>
                                <option value="">Select Account Code</option>
                                {renderOptions(fixedAssetAccount)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Function <span>*</span> </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.function} onChange={(e) => handleChange(e, "function")} required>
                                <option value="">Select Function</option>
                              	{renderOptions(functions)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Fund <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.fund} onChange={(e) => handleChange(e, "fund")} required>
                                <option value="">Select Fund</option>
                              	{renderOptions(funds)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Scheme </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.scheme} onChange={(e) => handleChange(e, "scheme")}>
                                <option value="">Select Scheme</option>
                                {renderOptions(schemes)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Sub Scheme </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <select value={revaluationSet.subScheme} onChange={(e) => handleChange(e, "subScheme")}>
                                <option value="">Select Sub Scheme</option>
                                {renderOptions(subSchemes)}
                              </select>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
		          </div>
		        </div>
		        <div className="form-section">
	              <div className="row">
	                <div className="col-md-8 col-sm-8">
	                  <h3 className="categoryType">Approved Details </h3>
	                </div>
	                <div className="col-md-4 col-sm-4 text-right">
	                  
	                </div>
	              </div>
	              <div className="form-section-inner">
		            <div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label>Comments </label>
                            </div>
                            <div className="col-sm-6">
                            <div>
                              <textarea value={revaluationSet.comments} onChange={(e) => handleChange(e, "comments")}></textarea>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
		          </div>
		        </div>
		        <div className="text-center">
	              <button type="submit" className="btn btn-submit">Save</button>&nbsp;&nbsp;
	              <button type="button" className="btn btn-close" onClick={(e)=>{close()}}>Close</button>
	          </div>
		    </form>
      	</div>
      )
    }
}

ReactDOM.render(
  <Revaluation />,
  document.getElementById('root')
);
