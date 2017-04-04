class CreateAsset extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],assetCategory:{
      "tenantId": "ap.kurnool",
      "name": "",
      "assetCategoryType": "",
      "parent":"",
      "depreciationMethod": "",
      "assetAccount": "",
      "accumulatedDepreciationAccount": "",
      "revaluationReserveAccount": "",
      "depreciationExpenseAccount": "",
      "unitOfMeasurement": "",
      "depreciationRate": null,
      "customFields":[]


    },
    customField:
     {
              "name": "",
              "type": "",
              "isActive": "",
              "isMandatory": "",
              "values": "",
              "localText": "",
              "regExFormate": ""
            },
    asset_category_type:[],
    assetCategories:[],
    depreciationMethod:{},
    assetAccount:[],
    accumulatedDepreciationAccount:[],
    revaluationReserveAccount:[],
    depreciationExpenseAccount:[],
    assignments_unitOfMeasurement:[],
    dataType:[],isEdit:false,index:-1,typeList:[]

  }
  this.handleChange=this.handleChange.bind(this);
  this.addAsset=this.addAsset.bind(this);
  this.renderDelEvent=this.renderDelEvent.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);
  this.handleChangeTwoLevel=this.handleChangeTwoLevel.bind(this);



}


  close(){
      // widow.close();
      open(location, '_self').close();
  }


  handleChange(e,name)
  {

      this.setState({
          assetCategory:{
              ...this.state.assetCategory,
              [name]:e.target.value
          }
      })

  }
  handleChangeTwoLevel(e,pName,name)
  {
    // console.log(pName);
    // console.log(name);


      this.setState({
          [pName]:{
              ...this.state[pName],
              [name]:e.target.value
          }
      })

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
              url:baseUrl+"/asset-services/assetCategories/_create?tenantId=ap.kurnool",
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),
              async: false,
              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              }
          });

        // console.log(response);
        if(response["statusText"]==="OK")
        {
          alert("Successfully added");
        }
        else {
          alert(response["statusText"]);
          this.setState({
            assetCategory:{
              "tenantId": "ap.kurnool",
              "name": "",
              "assetCategoryType": "",
              "parent":"",
              "depreciationMethod": "",
              "assetAccount": "",
              "accumulatedDepreciationAccount": "",
              "revaluationReserveAccount": "",
              "depreciationExpenseAccount": "",
              "unitOfMeasurement": "",
              "depreciationRate": null,
              "customFields":[]


            },
            customField:
             {
                      "name": "",
                      "type": "",
                      "isActive": "",
                      "isMandatory": "",
                      "values": "",
                      "localText": "",
                      "regExFormate": ""
                    },
            asset_category_type:[],
            assetCategories:[],
            depreciationMethod:{},
            assetAccount:[],
            accumulatedDepreciationAccount:[],
            revaluationReserveAccount:[],
            depreciationExpenseAccount:[],
            assignments_unitOfMeasurement:[],
            typeList:[]

          })
}
}
  componentWillMount(){
      this.setState({
        typeList:[{
                id: 1,
                name: "Text",
                active: true
            },
            {
                id: 2,
                name: "Number",
                active: true
            },
            {
                id: 3,
                name: "Email",
                active: true
            },
            {
                id: 4,
                name: "Check Box",
                active: true
            },
            {
                id: 5,
                name: "Radio",
                active: true
            },
            {
                id: 6,
                name: "Select",
                active: true
            }]
      })
    }


  componentDidMount()
  {
    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];

    if(getUrlVars()["type"]==="view")
    {
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

     this.setState({

      assetCategories,
      asset_category_type,
      depreciationMethod,
      assetAccount,
      accumulatedDepreciationAccount,
      revaluationReserveAccount,
      depreciationExpenseAccount,
      assignments_unitOfMeasurement
    })
  }


  addAsset(){
    var {isEdit,index,list,customField,assetCategory}= this.state;
      if (isEdit) {
        // console.log(isEdit,index);
        //update holidays with current holiday
        assetCategory["customFields"][index]=customField
        this.setState({
          assetCategory,isEdit:false
        })
        //this.setState({isEdit:false})

      } else {
        //get asset Category from state
        var temp =assetCategory;
        temp.customFields.push(customField);
        this.setState({
          assetCategory:temp,
          customField:{

                   "name": "",
                   "type": "",
                   "isActive": "",
                   "isMandatory": "",
                   "values": "",
                   "localText": "",
                   "regExFormate": ""
                 }
        })
        //use push to add new customField inside assetCategory

        //set back assetCategory to state

      }
    }




  renderDelEvent (index) {
        var customFields = this.state.assetCategory.customFields;
          customFields.splice(index,1);
            this.setState({customFields});

     }



  render() {
    // console.log(this.state);
    let {handleChange,addOrUpdate,renderDelEvent,addAsset,handleChangeTwoLevel}=this;
    let {isSearchClicked,list,customField,isEdit,index,assetCategory}=this.state;

    let {assetCategoryType,AssetCategory,parent,name,customFields,isMandatory,depreciationMethod,assetAccount,accumulatedDepreciationAccount,revaluationReserveAccount,depreciationExpenseAccount,unitOfMeasurement}=assetCategory;
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
    const renderBody=function()
    {
        if(customFields.length>0)
        {
            return customFields.map((item,index)=>
            {
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
                {item.isActive?item.isActive:"true"}
                  </td>
                  <td  >
                {item.isMandatory?item.isMandatory:"true"}
                  </td>
                  <td  >
                {item.values}
                  </td>
                  <td  >
                {item.localText}
                  </td>

                  <td data-label="Action">
                  <button type="button" className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"onClick={(e)=>{renderDelEvent(index)}}></span></button>
                </td></tr>)
            })
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
                                <label for="name">Name</label>
                        </div>
                        <div className="col-sm-6">
                                <input type="text" id="name" name="name" value={name}
                                onChange={(e)=>{handleChange(e,"name")}}/>
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
                        <select id="assetCategoryType" name="assetCategoryType" required="true" value={assetCategoryType} onChange={(e)=>{
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
                        <select id="parent" name="parent"  value={parent} onChange={(e)=>{
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
                        <select id="depreciationMethod" name="depreciationMethod" value={depreciationMethod} onChange={(e)=>{
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
                        <select id="assetAccount" name="assetAccount"  value={assetAccount} onChange={(e)=>{
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
                        <select id="accumulatedDepreciationAccount" name="accumulatedDepreciationAccount"  value={accumulatedDepreciationAccount} onChange={(e)=>{
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
                              <label for="revaluationReserveAccount"> Revaluation Reserve Account Code <span> *</span>  </label>
                        </div>
                        <div className="col-sm-6">
                        <div className="styled-select">
                        <select id="revaluationReserveAccount" name="revaluationReserveAccount" required="true" value={revaluationReserveAccount} onChange={(e)=>{
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
                         <select id="depreciationExpenseAccount" name="depreciationExpenseAccount"  value={depreciationExpenseAccount} onChange={(e)=>{
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
                        <select id="unitOfMeasurement" name="unitOfMeasurement"  value={unitOfMeasurement} onChange={(e)=>{
                        handleChange(e,"unitOfMeasurement")}}>
                          <option value="">Select Unit Of Measurment </option>
                          {renderOption(this.state.assignments_unitOfMeasurement)}
                        </select>
                        </div>
                        </div>
                        </div>
                        </div>
                        </div>


          <h3 className="categoryType">Custom Fields</h3>
          <a href="#" className="btn btn-default btn-action pull-right" data-toggle="modal" data-target="#calHoiday"><span className="glyphicon glyphicon-plus"></span></a>
              <div className="land-table">
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
                         <th>Action</th>
                       </tr>

                      </thead>

                      <tbody>
                      {renderBody()}

                      </tbody>

                  </table>
              </div>
              <div className="modal fade" id="calHoiday" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                  <div className="modal-dialog" role="document">
                      <div className="modal-content">
                          <form className="jurisdictionDetail" id="jurisdictionDetail">
                              <div className="modal-header">
                                  <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                  <h4 className="modal-title" id="myModalLabel">Add/Edit</h4>
                              </div>
                              <div className="modal-body">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                          <label htmlFor="">Name</label>
                                        </div>
                                        <div className="col-sm-6">
                                        <input type="text" name="name" id="name" value={customField.name} onChange={(e)=>{
                                          handleChangeTwoLevel(e,"customField","name")}}/>
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
                                  <select id="type" name="type" value={customField.type} onChange={(e)=>{
                            handleChangeTwoLevel(e,"customField","type")
                          }}required>
                              <option>Select Type</option>
                              {renderOption(this.state.typeList)}
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
                                          <input type="text" name="regExFormate" id="regExFormate" value={customField.regExFormate} onChange={(e)=>{
                                              handleChangeTwoLevel(e,"customField","regExFormate")}}/>
                                          </div>
                                      </div>
                                      </div>
                                      <div className="col-sm-6">
                                          <div className="row">
                                              <div className="col-sm-6 label-text">
                                                <label htmlFor="">Active</label>
                                              </div>
                                              <div className="col-sm-6">
                                              <input type="checkbox" name="isActive" id="isActive" value={customField.isActive} onChange={(e)=>{
                                          handleChangeTwoLevel(e,"customField","isActive")}} />
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
                          <input type="checkbox" name="isMandatory" id="isMandatory" value={customField.isMandatory} onChange={(e)=>{
                    handleChangeTwoLevel(e,"customField","isMandatory")}} />
                          </div>
                      </div>
                      </div>
                        <div className="col-sm-6">
                        <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="values">Value</label>
                  </div>
                  <div className="col-sm-6">
                    <textarea id="values" name="values" value= {customField.values}
                    onChange={(e)=>{handleChangeTwoLevel(e,"customField","values")}} max="1024"></textarea>
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
                                <input type="text" name="localText" id="localText" value={customField.localText} onChange={(e)=>{
                                  handleChangeTwoLevel(e,"customField","localText")}}/>
                                </div>
                            </div>
                          </div>
                        </div>
                  </div>
                                  <div className="modal-footer">
                                  <button type="button" className="btn btn-default" data-dismiss="modal">Close</button>
                                  <button type="button"  data-dismiss="modal"id="" className="btn btn-primary"  onClick={(e)=>{addAsset()}}>Add/Edit</button>
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
