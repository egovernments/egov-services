class Calenderholiday extends React.Component{
constructor(props){
  super(props);
  this.state={list:[],Holiday:{

    "calendarYear": {
      "name": ""
    },
    "name": "",
    "applicableOn": "",
    "tenantId": tenantId
  },
        year:[], holidays:[],dataType:[]
      }

  this.handleChange=this.handleChange.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);
  this.handleChangeThreeLevel=this.handleChangeThreeLevel.bind(this);
}



componentWillMount(){
  this.setState({
    year:getCommonMaster("egov-common-masters","calendaryears","CalendarYear").responseJSON["CalendarYear"] || []
})
}
    handleChange(e,name)
    {

        this.setState({
            Holiday:{
                ...this.state.Holiday,
                [name]:e.target.value
            }
        })

    }

    componentDidMount(){
      var type = getUrlVars()["type"], _this = this;
      var id = getUrlVars()["id"];
      $('#applicableOn').datetimepicker({
          format: 'DD/MM/YYYY',

      });
			$('#applicableOn').on("dp.change", function(e) {
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
              console.log(getCommonMasterById("egov-common-masters","holidays","Holiday",id).responseJSON["Holiday"][0]);
                this.setState({
                  Holiday:getCommonMasterById("egov-common-masters","holidays","Holiday",id).responseJSON["Holiday"][0]
                })
            }

    }


    close(){
        // widow.close();
        open(location, '_self').close();
    }


    addOrUpdate(e,mode)
    {

            e.preventDefault();
          console.log(this.state.Holiday);

            var tempInfo=Object.assign({},this.state.Holiday) , type = getUrlVars()["type"];
              // tempInfo.splice(tempInfo.calendarYear.id, tempInfo.calendarYear.active.tempInfo.calendarYear.endDatetempInfo.calendarYear.startDate,tempInfo.calendarYear.tenantId);
              if(type==="update"){
              var date = new Date(tempInfo.applicableOn);
              tempInfo.applicableOn = ( date.getDate() + '/' + (date.getMonth() + 1) + '/' +  date.getFullYear());

                delete tempInfo.calendarYear.id;
                delete tempInfo.calendarYear.active;
                delete tempInfo.calendarYear.endDate;
                delete tempInfo.calendarYear.startDate;
                delete tempInfo.calendarYear.tenantId;
              }

            console.log(tempInfo);
            // tempInfo["assetSet"]["assetCategory"]["id"]=parseInt(tempInfo["assetSet"]["assetCategory"]["id"])
            var body={
                "RequestInfo":requestInfo,
                "Holiday":tempInfo
              };
            var response=$.ajax({
                  // url: baseUrl+"/egov-common-masters/holidays/_create",
                  url:baseUrl+"/egov-common-masters/holidays/" + (type == "update" ? (this.state.Holiday.id + "/" + "_update/") : "_create"),
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
              this.setState({
                Holiday:{
                  "calendarYear": {
                    "name": ""
                  },
                  "name": "",
                  "applicableOn": "",
                  "tenantId": "",
                  "active": true
                }
              })
            }
            else {
              alert(response["statusText"]);
              this.setState({
                Holiday:{
                  "calendarYear": {
                    "name": ""
                  },
                  "name": "",
                  "applicableOn": "",
                  "tenantId": "",
                  "active": true
                }
              })
          }
      }

      handleChangeThreeLevel(e,pName,name)
      {console.log(this.state.Holiday[pName]);
        $('#applicableOn').data("DateTimePicker").minDate(false);
        $('#applicableOn').data("DateTimePicker").maxDate(false);
        $('#applicableOn').data("DateTimePicker").minDate(new Date(e.target.value, 0, 1));
        $('#applicableOn').data("DateTimePicker").maxDate(new Date(e.target.value, 11, 31));

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


  render(){
    let {handleChange,addOrUpdate,handleChangeThreeLevel}=this;
    let {name,calendarYear,applicableOn,active}=this.state.Holiday;
    let holidays=this.state.holidays;
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

    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };

      return(
      <div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
        <div className="row">
          <div className="col-sm-6 col-sm-offset-3">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> Calendar Year <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="name" name="name"  value= {calendarYear.name}
                        onChange={(e)=>{handleChangeThreeLevel(e,"calendarYear","name")  }}>
                    <option>Select Year</option>
                    {renderOption(this.state.year)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
        </div>
        <br/>
        <br/>
        <br/>
        <div className="row">
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
                        <input type="text" name="applicableOn" value={applicableOn} id="applicableOn" onChange={(e)=>{
                            handleChange(e,"applicableOn")}}required/>
                    </div>
                    </div>
                </div>
            </div>
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
            </div>

            {/*}<div className="row">
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 col-sm-offset-6">
                            <label className="radioUi">
                              <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                  handleChange(e,"active")}}required/> Active
                            </label>
                      </div>
                  </div>
                </div>
            </div>*/}


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
