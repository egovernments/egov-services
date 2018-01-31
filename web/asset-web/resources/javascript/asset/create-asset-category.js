let typeList=[{
    id: "Text",
    name: "Text",
    active: true
  },
  {
    id: "Number",
    name: "Number",
    active: true
  },
  {
    id: "Email",
    name: "Email",
    active: true
  },
  // {
  //   id: "Check Box",
  //   name: "Check Box",
  //   active: true
  // },
  // {
  //   id: "Radio",
  //   name: "Radio",
  //   active: true
  // },
  {
    id: "Select",
    name: "Select",
    active: true
  },
  {
    id: "Multiselect",
    name: "Multiselect",
    active: true
  },
  {
    id: "Date",
    name: "Date",
    active: true
  },
  {
    id: "File",
    name: "File",
    active: true
  },
  {
    id: "Table",
    name: "Table",
    active: true
  }
];

let typeListWithoutTable=[{
    id: "Text",
    name: "Text",
    active: true
  },
  {
    id: "Number",
    name: "Number",
    active: true
  },
  {
    id: "Email",
    name: "Email",
    active: true
  },
  // {
  //   id: "Check Box",
  //   name: "Check Box",
  //   active: true
  // },
  // {
  //   id: "Radio",
  //   name: "Radio",
  //   active: true
  // },
  {
    id: "Select",
    name: "Select",
    active: true
  },
  {
    id: "Multiselect",
    name: "Multiselect",
    active: true
  },
  {
    id: "Date",
    name: "Date",
    active: true
  },
  {
    id: "File",
    name: "File",
    active: true
  }
  // ,
  // {
  //   id: "Table",
  //   name: "Table",
  //   active: true
  // }
];

let customFieldData={
     "name": null,
     "type": null,
     "isActive": false,
     "isMandatory": false,
     "values": null,
     "localText": null,
     "regExFormate": null,
     "url": null,
     "order": null,
     "columns": []
};

class CreateAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showMsg: false,
      list: [],
      readonly: false,
      assetCategory: {
        tenantId,
        "name": "",
        "assetCategoryType": "",
        "parent": "",
        "depreciationMethod": "",
        "assetAccount": "",
        "accumulatedDepreciationAccount": "",
        "revaluationReserveAccount": "",
        "depreciationExpenseAccount": "",
        "unitOfMeasurement": "",
        "depreciationRate": null,
        "assetFieldsDefination": [],
        "version": "",
        "depreciationRate": "",
        usedForLease : false
      },
      customField:{
           "name": null,
           "type": null,
           "isActive": false,
           "isMandatory": false,
           "values": null,
           "localText": null,
           "regExFormate": null,
           "url": null,
           "order": null,
           "columns": []
      },
      column:{
           "name": null,
           "type": null,
           "isActive": false,
           "isMandatory": false,
           "values": null,
           "localText": null,
           "regExFormate": null,
           "url": null,
           "order": null,
           "columns": []
      },
      asset_category_type: [],
      assetCategories: [],
      depreciationMethod: [],
      assetAccount: [],
      accumulatedDepreciationAccount: [],
      revaluationReserveAccount: [],
      depreciationExpenseAccount: [],
      assignments_unitOfMeasurement: [],
      dataType: [],
      isEdit: false,
      index: -1,
      typeList,
      isCustomFormVisible:false
    }
    this.handleChange = this.handleChange.bind(this);
    this.addAsset = this.addAsset.bind(this);
    this.renderDelEvent = this.renderDelEvent.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
    this.showCustomFieldForm=this.showCustomFieldForm.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    var _this = this;
    if(window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if(logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
      }
    }
    $("#calHoiday").on("hidden.bs.modal", function () {
      _this.setState({
        column:{
             "name": "",
             "type": "",
             "isActive": false,
             "isMandatory": false,
             "values": "",
             "localText": "",
             "regExFormate": "",
             "url": "",
             "order": "",
             "columns": []
        }
      })
    });


    if(getUrlVars()["type"]) $('#hpCitizenTitle').text(titleCase(getUrlVars()["type"]) + " Asset Category")
    else $('#hpCitizenTitle').text("Create Asset Category")
    var asset_category_type, assetCategories, depreciationMethod, assetAccount, accumulatedDepreciationAccount, revaluationReserveAccount, depreciationExpenseAccount, assignments_unitOfMeasurement;
    var count = 8, _this = this, _state = {};
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if(count == 0)
        _this.setInitialState(_state);
    };

    getDropdown("asset_category_type", function(res) {
      checkCountNCall("asset_category_type", res);
    });
    getDropdown("assetCategories", function(res) {
      checkCountNCall("assetCategories", res);
    });
    getDropdown("depreciationMethod", function(res) {
      checkCountNCall("depreciationMethod", res);
    });
    commonApiPost("egf-masters","accountcodepurposes","_search",{tenantId,name:"Fixed Assets"},function(err,res2){
      if(res2){
        getDropdown("assetAccount", function(res) {
          for(var i= 0; i<res.length; i++) {
            res[i].name = res[i].glcode + "-" + res[i].name;
          }
          checkCountNCall("assetAccount", res);
        }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
      } else {
        checkCountNCall("assetAccount", []);
      }
    })
    commonApiPost("egf-masters","accountcodepurposes","_search",{tenantId,name:"Accumulated Depreciation"},function(err,res2){
      if(res2){
        getDropdown("accumulatedDepreciationAccount", function(res) {
          for(var i= 0; i<res.length; i++) {
            res[i].name = res[i].glcode + "-" + res[i].name;
          }
          checkCountNCall("accumulatedDepreciationAccount", res);
        }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
      } else {
        checkCountNCall("accumulatedDepreciationAccount", []);
      }
    })
    commonApiPost("egf-masters","accountcodepurposes","_search",{tenantId,name:"Revaluation Reserve Account"},function(err,res2){
      if(res2) {
        getDropdown("revaluationReserveAccount", function(res) {
          for(var i= 0; i<res.length; i++) {
            res[i].name = res[i].glcode + "-" + res[i].name;
          }
          checkCountNCall("revaluationReserveAccount", res);
        }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
      } else {
        checkCountNCall("revaluationReserveAccount", []);
      }
    })


    commonApiPost("egf-masters","accountcodepurposes","_search",{tenantId,name:"Depreciation Expense Account"},function(err,res2){
      if(res2){
        getDropdown("depreciationExpenseAccount", function(res) {
          for(var i= 0; i<res.length; i++) {
            res[i].name = res[i].glcode + "-" + res[i].name;
          }
          checkCountNCall("depreciationExpenseAccount", res);
        }, {accountCodePurpose: res2["accountCodePurposes"][0].id});
      } else {
        checkCountNCall("depreciationExpenseAccount", []);
      }
    })


    getDropdown("assignments_unitOfMeasurement", function(res) {
      checkCountNCall("assignments_unitOfMeasurement", res);
    });

    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    if (getUrlVars()["type"] === "view") {
      $("input,select").prop("disabled", true);
    }
    if (type === "view" || type === "update") {
      setTimeout(function(){
        getCommonMasterById("asset-services", "assetCategories", id, function(err, res) {
          if(res && res["AssetCategory"])
            _this.setState({
              assetCategory: res["AssetCategory"][0]
            })
        })
      }, 100);
    }
  }

  close() {
    // widow.close();
    open(location, '_self').close();
  }

  handleChange(e, name) {
    if(name == "name" && !e.target.value.trim() && e.target.value == " ") {
      e.preventDefault();
    } else {
      this.setState({
        assetCategory: {
          ...this.state.assetCategory,
          [name]: name === 'usedForLease' ? !this.state.assetCategory.usedForLease : e.target.value
        }
      })
    }
  }

  handleChangeTwoLevel(e, pName, name, isCheckBox) {
    if(isCheckBox) {
      this.setState({
        [pName]: {
          ...this.state[pName],
          [name]: e.target.checked
        }
      })
    } else {
      if(pName == "customField" && name == "type") {
        var bool;
        if(e.target.value == "Table") {
          bool = true;
        } else {
          bool = false;
        }

        this.setState({
          readonly: bool,
          [pName]: {
            ...this.state[pName],
            [name]: e.target.value
          }
        });

      } else {
        this.setState({
          [pName]: {
            ...this.state[pName],
            [name]: e.target.value
          }
        })
      }
    }
  }

  addOrUpdate(e){
        // var finalPost={
        //   "RequestInfo":requestInfo,
        //
        // }
        e.preventDefault();
        // console.log(zone);
        // console.log(this.state.assetCategory);
        var tempInfo=this.state.assetCategory, type = getUrlVars()["type"];
        // tempInfo["assetSet"]["assetCategory"]["id"]=parseInt(tempInfo["assetSet"]["assetCategory"]["id"])
        var body = {
            "RequestInfo":requestInfo,
            "AssetCategory":tempInfo
        };

        $.ajax({
            url:baseUrl+"/asset-services/assetCategories/" + (type == "update" ? "_update" : "_create") + "?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data:JSON.stringify(body),
            contentType: 'application/json',
            headers:{
              'auth-token': authToken
            },
            success: function(res) {
              localStorage.removeItem("assetCategories");
              window.location.href=`app/asset/create-asset-ack.html?name=${tempInfo.name}&type=category&value=${type}`;
            },
            error: function(err) {
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

  addAsset(to="") {
    var {
      isEdit,
      index,
      list,
      customField,
      column,
      assetCategory
    } = this.state;

    if(!to && (!customField.name || !customField.type)) {
      return this.setState({
        showMsg: true,
        readonly: false
      })
    } else {
      var _this = this;
      setTimeout(function() {
        _this.setState({
          showMsg: false,
          readonly: false
        });
      }, 300);
    }

    if (isEdit) {
      // console.log(isEdit,index);
      //update holidays with current holiday
      assetCategory["assetFieldsDefination"][index] = customField
      this.setState({
        assetCategory,
        isEdit: false,
        readonly: false
      })
      //this.setState({isEdit:false})
    } else {
      //get asset Category from state
      // customFieldData["columns"]=[];

      if (to=="column") {
        var temp = customField;
        temp.columns.push(column);
        this.setState({
          customField: temp,
          readonly: false,
          column:{
               "name": null,
               "type": null,
               "isActive": false,
               "isMandatory": false,
               "values": null,
               "localText": null,
               "regExFormate": null,
               "url": null,
               "order": null,
               "columns": []
          }
        })
      } else {
        var temp = Object.assign({}, assetCategory);
        temp.assetFieldsDefination.push(customField);
        this.setState({
          assetCategory: temp,
          readonly: false,
          customField: {
               "name": null,
               "type": null,
               "isActive": false,
               "isMandatory": false,
               "values": null,
               "localText": null,
               "regExFormate": null,
               "url": null,
               "order": null,
               "columns": []
          } ,
          isCustomFormVisible:false,
          column: {
               "name": null,
               "type": null,
               "isActive": false,
               "isMandatory": false,
               "values": null,
               "localText": null,
               "regExFormate": null,
               "url": null,
               "order": null,
               "columns": []
          }
        })
      }

      //use push to add new customField inside assetCategory
      //set back assetCategory to state
    }
  }

  renderDelEvent(index,to="") {
    if (to==="column") {
      var columns = this.state.customField.columns;
      columns.splice(index, 1);
      this.setState({
        customField:{
          ...this.state.customField,
          columns
        }
      });
    } else {
      var assetFieldsDefination = this.state.assetCategory.assetFieldsDefination;
      assetFieldsDefination.splice(index, 1);
      this.setState({
        assetFieldsDefination
      });
    }
  }

  showCustomFieldForm(isShow)
  {
    this.setState({isCustomFormVisible:isShow})
  }

  render() {
    let {handleChange,addOrUpdate,renderDelEvent,addAsset,handleChangeTwoLevel,showCustomFieldForm}=this;
    let {isSearchClicked,list,customField,column,isEdit,index,assetCategory,isCustomFormVisible, readonly, showMsg}=this.state;
    let {assetCategoryType,AssetCategory,parent,name,assetFieldsDefination,isMandatory,depreciationMethod,assetAccount,accumulatedDepreciationAccount,revaluationReserveAccount,depreciationExpenseAccount,unitOfMeasurement, version, depreciationRate, usedForLease}=assetCategory;
    let mode = getUrlVars()["type"];

    console.log(this.state.assetCategory);

    const showActionButton = function() {
        if((!mode) ||mode==="update") {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Create"}</button>);
        }
    };

    const renderOption=function(list)
    {
        if(list)
        {
            if (list.length) {

              return list.map((item)=>
              {

                  return (<option key={item.id} value={item.id}>
                    {!item.name ? item.description : item.name}

                    </option>)
              })

            } else {
              return Object.keys(list).map((k, index)=>
              {
                return (<option key={index} value={k}>
                        {list[k]}
                  </option>)

               })
            }

        }
    }

    const renderBody=function(to="")
    {
        if (to=="column") {
          if(customField.columns.length>0) {
              return customField.columns.map((item,index)=> {
                  return (<tr  key={index} className="text-center">
                  <td>{index+1}</td>
                    <td  >
                  {item.name}
                    </td>
                    <td  >
                      {item.type}
                    </td>
                    <td  >
                  {item.isActive?"true":"false"}
                    </td>
                    <td  >
                  {item.isMandatory?"true":"false"}
                    </td>
                    <td  >
                  {item.values}
                    </td>
                    <td  >
                  {item.order}
                    </td>

                  {/*  <td  >
                  {item.columns.length>0?item.columns.length:""}
                    </td>
                  */}

                    <td data-label="Action">
                                <button type="button" className="btn btn-default btn-action" onClick={(e)=>{renderDelEvent(index,"column")}} disabled={getUrlVars()["type"] == "view"}><span className="glyphicon glyphicon-trash"></span></button>
                  </td></tr>)
              })
          }
        } else {
          if(assetFieldsDefination.length>0) {
              return assetFieldsDefination.map((item,index)=> {
                  return (<tr  key={index} className="text-center">
                  <td>{index+1}</td>
                    <td  >
                  {item.name}
                    </td>
                    <td  >
                      {item.type}
                    </td>
                    <td>
                  {item.isActive?"true":"false"}
                    </td>
                    <td  >
                  {item.isMandatory?"true":"false"}
                    </td>
                    <td  >
                  {item.values}
                    </td>

                    <td  >
                  {item.order}
                    </td>

                    <td  >
                  {item.columns.length>0?item.columns.length:""}
                    </td>

                    <td data-label="Action">
                    <button type="button" className="btn btn-default btn-action" onClick={(e)=>{renderDelEvent(index)}} disabled={getUrlVars()["type"] == "view"}><span className="glyphicon glyphicon-trash"></span></button>
                  </td></tr>)
              })
          }
        }


    }

    const showNoteMsg = function() {
      if(showMsg) {
        return (<p className="text-danger">Name and Data Type is mandatory.</p>)
      } else
        return "";
    }

    const showAddNewBtn = function() {
      if(getUrlVars()["type"] != "view") {
        return (
            <button type="button" className="btn btn-primary btn-action pull-right" onClick={()=>{showCustomFieldForm(true)}}>Add New</button>
          )
      }
    }

    const showCustomFieldsTable=function()
    {
      return (
        <div className="form-section">
          <h3 className="categoryType">Custom Fields</h3>
          <div className="land-table table-responsive">
              <table className="table table-bordered">
                  <thead>
                  <tr>
                    <th>Sl No.</th>
                     <th>Name</th>
                     <th>Data Type</th>
                     <th>Active</th>
                     <th>Mandatory</th>
                     <th>Values</th>
                     <th>Order</th>
                     <th>Columns</th>
                     <th>Action</th>
                   </tr>

                  </thead>

                  <tbody>
                  {renderBody()}
                  </tbody>
              </table>
          </div>
          <div className="row" style={{"padding-right": "18px"}}>
            {showAddNewBtn()}
            <br/>
          </div>
            {showCustomFieldAddForm()}
        </div>

      )
    }

    const showCustomFieldAddForm=function()
    {
        if(isCustomFormVisible)
        {
          return (
          <div>
            <h3 className="categoryType">Add</h3>

            <div className="form-section">
              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Name <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="name"  value={customField.name} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","name")}}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="type"> Data Type <span>*</span>  </label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select  name="type" value={customField.type} onChange={(e)=>{
                            handleChangeTwoLevel(e,"customField","type")
                            }}required>
                              <option>Select Type</option>
                              {renderOption(typeList)}
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
                      <label htmlFor="">Mandatory</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="checkbox" name="isMandatory" value={customField.isMandatory} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","isMandatory", true)}} checked={customField.isMandatory ? true : false}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Active</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="checkbox" name="isActive" value={customField.isActive} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","isActive", true)}} checked={customField.isActive ? true : false} />
                    </div>
                  </div>
                </div>
              </div>

              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Order</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="order" disabled={readonly} value={customField.order} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","order")}}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Value</label>
                    </div>
                    <div className="col-sm-6">
                      <textarea  name="values" disabled={readonly} value={ customField.values} onChange={(e)=>{handleChangeTwoLevel(e,"customField","values")}} max="1024"></textarea>
                    </div>
                  </div>
                </div>
              </div>

              {showNestedCustomFieldTable()}
              {showNoteMsg()}
              <div className="text-center">
                <button type="button" className="btn btn-primary" onClick={(e)=>{addAsset()}}>Add/Edit</button>
              </div>
              </div>
            </div>
          )
        }
    }

    const showNestedCustomFieldTable=function()
    {
      if(customField.type=="Table")
      {
        return (
          <div className="form-section">
            <h4>Add Columns</h4>
            <button type="button" className="btn btn-default btn-action pull-right" data-toggle="modal" data-target="#calHoiday"><span className="glyphicon glyphicon-plus"></span></button>
              <div className="land-table table-responsive">
                  <table className="table table-bordered">
                      <thead>
                      <tr>
                        <th>Sl No.</th>
                         <th>Name</th>
                         <th>Data Type</th>
                         <th>Active</th>
                         <th>Mandatory</th>
                         <th>Values</th>
                         <th>Order</th>
                        {/* <th>Columns</th> */}
                         <th>Action</th>
                       </tr>

                      </thead>

                      <tbody>
                      {renderBody("column")}

                      </tbody>

                  </table>
              </div>
            {/*showCustomFieldsTable()*/}
          </div>
        )
      }
    }

    return (
      <div>
        <form onSubmit={(e)=>{addOrUpdate(e)}}>
          <div className="form-section-inner">

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="name">Name <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text"  name="name" value={name} onChange={(e)=>{handleChange(e,"name")}} required/>
                  </div>
                </div>
              </div>

              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="assetCategoryType"> Asset Category Type <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="assetCategoryType" required="true" value={assetCategoryType} onChange={(e)=>{
                            handleChange(e,"assetCategoryType")}}>
                            <option value="">Select Asset Category Type</option>
                            {renderOption(this.state.asset_category_type)}
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
                    <label for="parent"> Parent Category  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="parent" value={parent} onChange={(e)=>{
                          handleChange(e,"parent")}}>
                          <option value="">Select Parent Category</option>
                            {renderOption(this.state.assetCategories)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="depreciationMethod"> Depreciation Method  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="depreciationMethod" value={depreciationMethod} onChange={(e)=>{
                        handleChange(e,"depreciationMethod")}}>
                        <option value="">Select Depreciation Method </option>
                        {renderOption(this.state.depreciationMethod)}
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
                    <label for="assetAccount"> Asset Account Code  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="assetAccount" value={assetAccount} onChange={(e)=>{
                        handleChange(e,"assetAccount")}}>
                        <option value="">Select Asset Account </option>
                        {renderOption(this.state.assetAccount)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="accumulatedDepreciationAccount"> Accumulated Depreciation Code  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="accumulatedDepreciationAccount" value={accumulatedDepreciationAccount} onChange={(e)=>{
                          handleChange(e,"accumulatedDepreciationAccount")}}>
                        <option value="">Select Accumulated Account </option>
                        {renderOption(this.state.accumulatedDepreciationAccount)}
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
                    <label for="revaluationReserveAccount"> Revaluation Reserve Account Code  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="revaluationReserveAccount" value={revaluationReserveAccount} onChange={(e)=>{
                          handleChange(e,"revaluationReserveAccount")}}>
                        <option value="">Select Reserve Account </option>
                        {renderOption(this.state.revaluationReserveAccount)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="depreciationExpenseAccount"> Depreciation Expense Account  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="depreciationExpenseAccount" value={depreciationExpenseAccount} onChange={(e)=>{
                          handleChange(e,"depreciationExpenseAccount")}}>
                         <option value="">Select Expense Account </option>
                         {renderOption(this.state.depreciationExpenseAccount)}
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
                    <label for="unitOfMeasurement"> UOM   </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select  name="unitOfMeasurement" value={unitOfMeasurement} onChange={(e)=>{
                          handleChange(e,"unitOfMeasurement")}}>
                        <option value="">Select Unit Of Measurment </option>
                        {renderOption(this.state.assignments_unitOfMeasurement)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="version"> Version   </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" name="version" value={version} onChange={(e)=>{handleChange(e,"version")}}/>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="depreciationRate"> Depreciation Rate </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="number" name="depreciationRate" value={depreciationRate} onChange={(e)=>{handleChange(e,"depreciationRate")}}/>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> Used for Lease and Agreement </label>
                  </div>
                  <div className="col-sm-6">
                    <input type="checkbox" name="usedForLease" { ...( usedForLease ? { checked: 'checked' } :  {} ) } onChange={(e)=>{handleChange(e,"usedForLease")}} />
                  </div>
                </div>
              </div>
            </div>


            {showCustomFieldsTable()}



            <div className="modal fade" id="calHoiday" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
              <div className="modal-dialog" role="document">
                <div className="modal-content">
                  <form>
                    <div className="modal-header">
                      <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                      <h4 className="modal-title" id="myModalLabel">Custom Fields</h4>
                    </div>
                    <div className="modal-body">
                      <div className="row">
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Name</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" name="name"  value={column.name} onChange={(e)=>{ handleChangeTwoLevel(e,"column","name")}}/>
                            </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="type"> Data Type  </label>
                            </div>
                            <div className="col-sm-6">
                              <div className="styled-select">
                                <select id="type" name="type" value={column.type} onChange={(e)=>{
                                    handleChangeTwoLevel(e,"column","type")
                                    }}required>
                                      <option>Select Type</option>
                                      {renderOption(typeListWithoutTable)}
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
                              <label htmlFor="">Mandatory</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="checkbox" name="isMandatory" id="isMandatory" value={column.isMandatory} onChange={(e)=>{ handleChangeTwoLevel(e,"column","isMandatory", true)}} checked={column.isMandatory ? true : false}/>
                            </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Active</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="checkbox" name="isActive" id="isActive" value={column.isActive} onChange={(e)=>{ handleChangeTwoLevel(e,"column","isActive", true)}} checked={column.isActive ? true : false} />
                            </div>
                          </div>
                        </div>
                      </div>

                      <div className="row">
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Order</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" name="order" id="order" value={column.order} onChange={(e)=>{ handleChangeTwoLevel(e,"column","order")}}/>
                            </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Value</label>
                            </div>
                            <div className="col-sm-6">
                              <textarea id="values" name="values" value={ column.values} onChange={(e)=>{handleChangeTwoLevel(e,"column","values")}} max="1024"></textarea>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div className="row">
                        {/*showCustomFieldsTable()*/}
                      </div>


                    </div>

                    <div className="modal-footer">
                      <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                      <button type="button" data-dismiss="modal" id="" className="btn btn-primary" onClick={(e)=>{addAsset("column")}}>Add/Edit</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>



            <div className="text-center">
              {showActionButton()} &nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>

          </div>
        </form>
      </div>
        );
  }
}


ReactDOM.render(
  <CreateAsset />,
  document.getElementById('root')
);
