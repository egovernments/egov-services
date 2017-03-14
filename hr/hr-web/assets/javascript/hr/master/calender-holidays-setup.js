class Calenderholiday extends React.Component{
constructor(props){
  super(props);
  this.state={holiday:
      {CalendarYear:"",
      name:"",
      applicableOn:"", active:""
    },
        year:[], holidays:[]
      }

  this.handleChange=this.handleChange.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);
}



componentWillMount(){

  var year=[{id:"2017",name:"2017"},{id:"2016",name:"2016"},{id:"2015",name:"2015"},{id:"2014",name:"2014"}];
  this.setState({year})
}
    handleChange(e,name)
    {
        this.setState({
            holiday:{
                ...this.state.holiday,
                [name]:e.target.value
            }
        })

    }

    componentDidMount(){
      if(getUrlVars()["type"]==="view")
      {
        for (var variable in this.state.holiday)
          document.getElementById(variable).disabled = true;
        }
    }


    close(){
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e,mode){
      console.log(this.state.holiday);
      e.preventDefault();
      if(mode==="update"){
        console.log("update");
      }
      else{
        this.setState({holiday:{
          name:"",
          applicableOn:"",
          CalendarYear:"",active:""},year:""})
      }

    }

  render(){
    let {handleChange,addOrUpdate}=this;
    let {name,CalendarYear,applicableOn,active}=this.state.holiday;
    let holidays=this.state.holidays;
    let mode=getUrlVars()["type"];

    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
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
                    <select id="CalendarYear" name="CalendarYear" onChange={(e)=>{
                        handleChange(e,"CalendarYear")  }}required>
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
                        <input type="date" name="applicableOn" value={applicableOn} id="applicableOn" onChange={(e)=>{
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

            <div className="row">
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
            </div>


    <div className="text-center">
        {showActionButton()}
        <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>

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
