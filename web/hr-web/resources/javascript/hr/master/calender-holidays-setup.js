class Calenderholiday extends React.Component{
constructor(props){
  super(props);
  this.state = {
      list: [],
      Holiday: {
          calendarYear: {
                            name: ""
                          },
          name: "",
          holidayType: {
                          id:""
                        },
          applicableOn: "",
          tenantId: tenantId
      },
      year: [],
      holidays: [],
      types: [],
      dataType: []
  }

  this.handleChange = this.handleChange.bind(this);
  this.addOrUpdate = this.addOrUpdate.bind(this);
  this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
  this.setInitialState = this.setInitialState.bind(this);
}

setInitialState(initState) {
  this.setState(initState);
}

handleChange(e,name){
  if(name==="holidayType")
  {
    this.setState({
      Holiday:{
        ...this.state.Holiday,
        holidayType:{
          id : e.target.value
        }
      }
    })

  }else {
    this.setState({
      Holiday:{
        ...this.state.Holiday,
        [name]:e.target.value
      }
    })
  }

}

componentWillMount() {
  var _this = this;
  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }

  if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Holiday");

  var count = 2, _this = this, _state = {};
  var checkCountNCall = function(key, res) {
  count--;
  _state[key] = res;
  if(count == 0)
    _this.setInitialState(_state);
  }

  getDropdown("futureyears", function(res) {
    checkCountNCall("year", res);
  });

  getDropdown("holidayTypes", function(res) {
    checkCountNCall("types", res);
  });

  var type = getUrlVars()["type"];
  var id = getUrlVars()["id"];
  $('#applicableOn').datepicker({
      format: 'dd/mm/yyyy',
      autoclose:true

  });
	$('#applicableOn').on("change", function(e) {
				_this.setState({
		          Holiday: {
		              ..._this.state.Holiday,
		              "applicableOn":$("#applicableOn").val()
		          }
	      })
		});

  if(getUrlVars()["type"]==="view")
  {
    $("input,select").prop("disabled", true);
  }

  if(type==="view"||type==="update")
  {
    getCommonMasterById("egov-common-masters","holidays", id, function(err, res) {
      if(res) {
        var Holiday = res["Holiday"][0];
        _this.setState({
          Holiday
        })
      }
    })
  }

}


close(){
  open(location, '_self').close();
}


addOrUpdate(e,mode) {
  e.preventDefault();
  var tempInfo=Object.assign({},this.state.Holiday) , type = getUrlVars()["type"];
  var body={
      "RequestInfo":requestInfo,
      "Holiday":tempInfo
    },_this=this;
      if(type == "update") {
        delete tempInfo.calendarYear.id;
        delete tempInfo.calendarYear.active;
        delete tempInfo.calendarYear.endDate;
        delete tempInfo.calendarYear.startDate;
        $.ajax({
              url:baseUrl+"/egov-common-masters/holidays/" + _this.state.Holiday.id + "/" + "_update?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),

              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                      showSuccess("Holiday Modified successfully.");
                      window.location.href = 'app/hr/common/show-calender-holiday.html?type=update';
              },
              error: function(err) {
                  showError("Something went wrong. Please try again later.");

              }
          });
      }
      else{
        $.ajax({
              url: baseUrl+"/egov-common-masters/holidays/_create?tenantId=" + tenantId,
              type: 'POST',
              dataType: 'json',
              data:JSON.stringify(body),

              contentType: 'application/json',
              headers:{
                'auth-token': authToken
              },
              success: function(res) {
                      showSuccess("Holiday Created successfully.");
                      _this.setState({
                        Holiday:{
                          "calendarYear": {
                            "name": ""
                          },
                          "name": "",
                          "holidayType": {
                            "id":""
                          },
                          "applicableOn": "",
                          "tenantId": tenantId

                        }
                      })
              },
              error: function(err) {
                  showError("Holiday already defined on present date");

              }
          });
      }
    }

handleChangeThreeLevel(e,pName,name) {
  $('#applicableOn').data("datepicker").setStartDate(false);
  $('#applicableOn').data("datepicker").setEndDate(false);
  $('#applicableOn').data("datepicker").setStartDate(new Date(e.target.value, 0, 1));
  $('#applicableOn').data("datepicker").setEndDate(new Date(e.target.value, 11, 31));

  var str = this.state.Holiday.applicableOn;
  var value = e.target.value;
  var year = str.split("/").pop();

  if(value==year){
    this.setState({
      Holiday:{
        ...this.state.Holiday,
        [pName]:{
            ...this.state.Holiday[pName],
            [name]:e.target.value
        }
      }

    })
  }
  else {
    this.setState({
      Holiday:{
        ...this.state.Holiday,
        "applicableOn": "",
        [pName]:{
            ...this.state.Holiday[pName],
            [name]:e.target.value
      }
    }

    })
  }


}


render(){
  let {handleChange,addOrUpdate,handleChangeThreeLevel}=this;
  let {name,calendarYear,applicableOn,holidayType}=this.state.Holiday;
  let {year,types,dataType,holidays,Holiday}=this.state;
  let mode=getUrlVars()["type"];

  const renderOption=function(list)
  {
      if(list)
      {
          return list.map((item)=>
          {
              return (<option key={item.name} value={item.name}>
                      {item.name}
                </option>)
          })
      }
  }

  const renderOptionForTypes=function(list)
  {
      if(list)
      {
          return list.map((item,ind)=>
          {
              return (<option key={ind} value={item.id}>
                      {item.name}
                </option>)
          })
      }
  }

  const showActionButton=function() {
    if((!mode) ||mode==="update")
    {
      return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
    }
  };

    return(
    <div>
    <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) :  "Create"} Calender Holiday Setup</h3>
    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
    <fieldset>
      <div className="row">
        <div className="col-sm-6 ">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for=""> Calendar Year <span>*</span></label>
                </div>
                <div className="col-sm-6">
                  <div className="styled-select">
                  <select id="name" name="name"  value= {calendarYear.name}
                    onChange={(e)=>{handleChangeThreeLevel(e,"calendarYear","name")}} required>
                  <option value="">Select Year</option>
                  {renderOption(this.state.year)}
                 </select>
                  </div>
                </div>
            </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for=""> Holiday Date<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                  <div className="text-no-ui">
                      <span>
                          <i className="glyphicon glyphicon-calendar"></i>
                      </span>
                      <input type="text" name="applicableOn" value={applicableOn} id="applicableOn"
                          onChange={(e)=>{ handleChange(e,"applicableOn")}}required/>
                  </div>
                  </div>
              </div>
          </div>
        </div>
          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Holiday Name <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                    <input type="text" name="name" value={name} id="name" onChange={(e)=>{
                        handleChange(e,"name")}} required/>

                    </div>
                </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Holiday Type </label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                      <select id="holidayType" name="holidayType"  value= {holidayType.id}
                        onChange={(e)=>{handleChange(e,"holidayType")}} >
                      <option value="">Select Type</option>
                      {renderOptionForTypes(this.state.types)}
                     </select>
                      </div>
                    </div>
                </div>
            </div>
          </div>


  <div className="text-center">
      {showActionButton()} &nbsp;&nbsp;
      <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

  </div>
  </fieldset>
  </form>

</div>
    );
}
}
ReactDOM.render(
<Calenderholiday />,
document.getElementById('root')
);
