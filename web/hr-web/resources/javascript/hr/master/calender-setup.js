class Calender_setup extends React.Component{
  constructor(props){
    super(props);
    this.state={calenderSet:{
        name:"",
        startDate:"",
        endDate:"",
        active:""
    }
  }
  this.handleChange=this.handleChange.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);
}

handleChange(e,name) {
  this.setState({
      calenderSet:{
          ...this.state.calenderSet,
          [name]:e.target.value
        }
    })
}

componentDidMount() {
  var _this = this;
  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }

  if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Calender");
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"];
    var _this = this;

    $('#fromDate, #toDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose:true

    });

    $('#fromDate, #toDate').on("change", function(e) {
      var _from = $('#fromDate').val();
      var _to = $('#toDate').val();
      var _triggerId = e.target.id;
      if(_from && _to) {
        var dateParts1 = _from.split("/");
        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
        var date1 = new Date(newDateStr);
        var dateParts2 = _to.split("/");
        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
        var date2 = new Date(newDateStr);
        if (date1 > date2) {
          showError("From date must be before End date.");
          $('#' + _triggerId).val("");
        }
      }
    });

  if(getUrlVars()["type"]==="view")
  {
    for (var variable in this.state.calenderSet)
      document.getElementById(variable).disabled = true;
  }

  if(type==="view"||type==="update") {

    getCommonMasterById("egov-common-masters","calendaryears", id, function(err, res) {
      if(res) {
           var calenderSet = res["CalendarYear"][0];
          _this.setState({
            calenderSet
          })
        }
    })
  }

}

close() {
    // widow.close();
    open(location, '_self').close();
}

addOrUpdate(e,mode) {
     console.log(this.state.calenderSet);
    e.preventDefault();
      this.setState({calenderSet:{
          name:"",
          startDate:"",
          endDate:"",
          active:""
      }
    })
  }

render() {
    let {handleChange,addOrUpdate}=this;
    let mode=getUrlVars()["type"];
    let {name,startDate,endDate,active}=this.state.calenderSet;

    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };


  return (<div>
      <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) :  "Create"} Calendar Setup</h3>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Year <span>*</span></label>
                </div>
                  <div className="col-sm-6">
                    <input type="number" min= "1000" max="9999" name="name" id="name" value={name} onChange={(e)=>{handleChange(e,"name")}}required/>
                  </div>
              </div>
          </div>

          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">From Date <span>*</span></label>
                      </div>
                          <div className="col-sm-6">
                              <div className="text-no-ui">
                                  <span>
                                      <i className="glyphicon glyphicon-calendar"></i>
                                  </span>
                                  <input type="text" name="startDate" id="startDate" value={startDate}
                                  onChange={(e)=>{handleChange(e,"startDate")}}required/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="">To Date <span>*</span></label>
                          </div>
                              <div className="col-sm-6">
                                  <div className="text-no-ui">
                                    <span>
                                        <i className="glyphicon glyphicon-calendar"></i>
                                    </span>
                                    <input type="text" name="endDate" id="endDate"value={endDate}
                                    onChange={(e)=>{handleChange(e,"endDate")}}required/>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="">Active</label>
                      </div>
                          <div className="col-sm-6">
                                <label className="radioUi">
                                  <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                      handleChange(e,"active")}} checked={active == "true" || active  ==  true }/>
                                </label>
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
  <Calender_setup />,
  document.getElementById('root')
);
