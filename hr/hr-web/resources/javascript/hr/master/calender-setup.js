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



    handleChange(e,name)
    {
        this.setState({
            calenderSet:{
                ...this.state.calenderSet,
                [name]:e.target.value
            }
        })

    }

    componentDidMount(){
      var type=getUrlVars()["type"];
      var id=getUrlVars()["id"];

      if(getUrlVars()["type"]==="view")
      {
        for (var variable in this.state.calenderSet)
          document.getElementById(variable).disabled = true;
        }

        if(type==="view"||type==="update")
        {
            console.log("fired");
            console.log(getCommonMasterById("egov-common-masters","calendaryears","CalendarYear",id).responseJSON["CalendarYear"][0]);
            this.setState({
              calenderSet:getCommonMasterById("egov-common-masters","calendaryears","CalendarYear",id).responseJSON["CalendarYear"][0]
            })
        }

    }

    close(){
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e,mode){
         console.log(this.state.calenderSet);
        e.preventDefault();
        // console.log(mode);
        if (mode==="update") {
            console.log("update");
        } else {
          this.setState({calenderSet:{
              name:"",
              startDate:"",
              endDate:"",
              active:""
          } })
        }
      }


  render(){

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
                                  <input type="date" name="startDate" id="startDate" value={startDate}
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
                                    <input type="date" name="endDate" id="endDate"value={endDate}
                                    onChange={(e)=>{handleChange(e,"endDate")}}required/>
                                  </div>
                              </div>
                          </div>
                      </div>
                </div>
            <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 col-sm-offset-6">
                            <label className="radioUi">
                              <input type="checkbox" name="active" id="active" value={active} value="true"
                                  onChange={(e)=>{ handleChange(e,"active")}}required/> Active
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
