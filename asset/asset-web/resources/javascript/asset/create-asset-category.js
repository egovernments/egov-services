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
        "version": ""
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
      asset_category_type,
      assetCategories,
      depreciationMethod,
      assetAccount,
      accumulatedDepreciationAccount,
      revaluationReserveAccount,
      depreciationExpenseAccount,
      assignments_unitOfMeasurement,
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
  }

  componentDidMount() {
    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    if (getUrlVars()["type"] === "view") {
      // for (var variable in this.state.assetCategory)
      //   // document.getElementById(variable).disabled = true;
      //   console.log($('#'+variable).length);
      $("input,select").prop("disabled", true);
    }
    if (type === "view" || type === "update") {
      // console.log(getCommonMasterById("asset-services", "assetCategories", "AssetCategory", id).responseJSON);
      this.setState({
        assetCategory: getCommonMasterById("asset-services", "assetCategories", "AssetCategory", id).responseJSON["AssetCategory"][0]
      })
    }
    // console.log(commonApiPost("egf-masters","chartofaccounts","_search",{tenantId}).responseJSON["chartOfAccounts"]);
    // this.setState({
    //   assetCategories,
    //   asset_category_type,
    //   depreciationMethod,
    //   assetAccount,
    //   accumulatedDepreciationAccount,
    //   revaluationReserveAccount,
    //   depreciationExpenseAccount,
    //   assignments_unitOfMeasurement
    // })
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
          [name]: e.target.value
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
        var tempInfo=this.state.assetCategory;
        // tempInfo["assetSet"]["assetCategory"]["id"]=parseInt(tempInfo["assetSet"]["assetCategory"]["id"])
        var body={
            "RequestInfo":requestInfo,
            "AssetCategory":tempInfo
          };

        var response=$.ajax({
              url:baseUrl+"/asset-services/assetCategories/_create?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              async: false,
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              }
          });

        if(response["status"]==  201 || response["status"]==  200) {
          // this.setState({
          //   assetCategory:{
          //     "tenantId": tenantId,
          //     "name": "",
          //     "assetCategoryType": "",
          //     "parent":"",
          //     "depreciationMethod": "",
          //     "assetAccount": "",
          //     "accumulatedDepreciationAccount": "",
          //     "revaluationReserveAccount": "",
          //     "depreciationExpenseAccount": "",
          //     "unitOfMeasurement": "",
          //     "depreciationRate": null,
          //     "assetFieldsDefination":[]
          //   },
          //   customField: {
          //     "name": null,
          //     "type": null,
          //     "isActive": false,
          //     "isMandatory": false,
          //     "values": null,
          //     "localText": null,
          //     "regExFormate": null,
          //     "url": null,
          //     "order": null,
          //     "columns": null
          //   },
          //   asset_category_type:[],
          //   assetCategories:[],
          //   depreciationMethod:{},
          //   assetAccount:[],
          //   accumulatedDepreciationAccount:[],
          //   revaluationReserveAccount:[],
          //   depreciationExpenseAccount:[],
          //   assignments_unitOfMeasurement:[],
          //   typeList:[]
          //
          // })
          window.location.href=`app/asset/create-asset-ack.html?name = ${tempInfo.name}&type=category&value=${getUrlVars()["type"]}`;
        } else {
          showError(response["statusText"]);
        }
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
        showMsg: true
      })
    } else {
      var _this = this;
      setTimeout(function() {
        _this.setState({
          showMsg: false
        });
      }, 300);
    }

    if (isEdit) {
      // console.log(isEdit,index);
      //update holidays with current holiday
      assetCategory["assetFieldsDefination"][index] = customField
      this.setState({
        assetCategory,
        isEdit: false
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
    if (to=="column") {
      var columns = this.state.customField.columns;
      columns.splice(index, 1);
      this.setState({
        ...this.state.customField,
        columns
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

    let {assetCategoryType,AssetCategory,parent,name,assetFieldsDefination,isMandatory,depreciationMethod,assetAccount,accumulatedDepreciationAccount,revaluationReserveAccount,depreciationExpenseAccount,unitOfMeasurement, version}=assetCategory;
    let mode = getUrlVars()["type"];

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
                    {typeof(item.name)=="undefined"?item.uom:item.name}

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
                    <td  >
                  {item.name}
                    </td>
                    <td  >
                      {item.type}
                    </td>
                    <td  >
                    {item.regExFormate}
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
                  {item.localText}
                    </td>
                    <td  >
                  {item.url}
                    </td>

                    <td  >
                  {item.order}
                    </td>

                  {/*  <td  >
                  {item.columns.length>0?item.columns.length:""}
                    </td>
                  */}

                    <td data-label="Action">
                    <button type="button" className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"onClick={(e)=>{renderDelEvent(index,column)}}></span></button>
                  </td></tr>)
              })
          }
        } else {
          if(assetFieldsDefination.length>0) {
              return assetFieldsDefination.map((item,index)=> {
                  return (<tr  key={index} className="text-center">
                    <td  >
                  {item.name}
                    </td>
                    <td  >
                      {item.type}
                    </td>
                    <td  >
                    {item.regExFormate}
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
                  {item.localText}
                    </td>
                    <td  >
                  {item.url}
                    </td>

                    <td  >
                  {item.order}
                    </td>

                    <td  >
                  {item.columns.length>0?item.columns.length:""}
                    </td>

                    <td data-label="Action">
                    <button type="button" className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"onClick={(e)=>{renderDelEvent(index)}}></span></button>
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

    const showCustomFieldsTable=function()
    {
      return (
        <div className="form-section">
          <h3 className="categoryType">Custom Fields</h3>
          <button type="button" className="btn btn-default btn-action pull-right" onClick={()=>{showCustomFieldForm(true)}}><span className="glyphicon glyphicon-plus"></span></button>

          <div className="land-table table-responsive">
              <table className="table table-bordered">
                  <thead>
                  <tr>
                     <th>Name</th>
                     <th>Data Type</th>
                     <th>RegEx format</th>
                     <th>Active</th>
                     <th>Mandatory</th>
                     <th>Values</th>
                     <th>Local Text</th>
                     <th>Url</th>
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
                      <label htmlFor="">RegEx Format</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="regExFormate" value={customField.regExFormate} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","regExFormate")}} disabled={readonly}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Active</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="checkbox" name="isActive" disabled={readonly}  value={customField.isActive} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","isActive", true)}} checked={customField.isActive ? true : false} />
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
                      <input type="checkbox" name="isMandatory" disabled={readonly}  value={customField.isMandatory} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","isMandatory", true)}} checked={customField.isMandatory ? true : false}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="values">Value</label>
                    </div>
                    <div className="col-sm-6">
                      <textarea  name="values" disabled={readonly} value={ customField.values} onChange={(e)=>{handleChangeTwoLevel(e,"customField","values")}} max="1024"></textarea>
                    </div>
                  </div>
                </div>
              </div>

              <div className="row">
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Local Text</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="localText" disabled={readonly}  value={customField.localText} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","localText")}}/>
                    </div>
                  </div>
                </div>
                <div className="col-sm-6">
                  <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Url</label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="url" disabled={readonly}  value={customField.url} onChange={(e)=>{ handleChangeTwoLevel(e,"customField","url")}}/>
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
              </div>

              {showNestedCustomFieldTable()}
              {showNoteMsg()}
              {/*  <button type="button" className="btn btn-default" >reset</button>*/}
                <button type="button" className="btn btn-primary" onClick={(e)=>{addAsset()}}>Add/Edit</button>
              </div>

                  {/**/}
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
                         <th>Name</th>
                         <th>Data Type</th>
                         <th>RegEx format</th>
                         <th>Active</th>
                         <th>Mandatory</th>
                         <th>Values</th>
                         <th>Local Text</th>
                         <th>Url</th>
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
                    <input type="text"  name="version" value={version} onChange={(e)=>{handleChange(e,"version")}} required/>
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
                              <label htmlFor="">RegEx Format</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" name="regExFormate" id="regExFormate" value={column.regExFormate} onChange={(e)=>{ handleChangeTwoLevel(e,"column","regExFormate")}}/>
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
                              <label for="values">Value</label>
                            </div>
                            <div className="col-sm-6">
                              <textarea id="values" name="values" value={ column.values} onChange={(e)=>{handleChangeTwoLevel(e,"column","values")}} max="1024"></textarea>
                            </div>
                          </div>
                        </div>
                      </div>

                      <div className="row">
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Local Text</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" name="localText" id="localText" value={column.localText} onChange={(e)=>{ handleChangeTwoLevel(e,"column","localText")}}/>
                            </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                          <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Url</label>
                            </div>
                            <div className="col-sm-6">
                              <input type="text" name="url" id="url" value={column.url} onChange={(e)=>{ handleChangeTwoLevel(e,"column","url")}}/>
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
