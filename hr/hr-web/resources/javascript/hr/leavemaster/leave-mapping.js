function getUrlVars() {
    var vars = [],
        hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}



class LeaveMaster extends React.Component{
constructor(props){
  super(props);
  this.state={leave:
      {designation:"",
      leaveType:"",
      noOfDay:""},
        designation:[]}

  this.addOrUpdate=this.addOrUpdate.bind(this);
  this.handleChange=this.handleChange.bind(this);

}
componentWillMount(){

  var designation=[{id:"1",name:"ASSISTANT ENGINEER"},{id:"2",name:"ASSISTANT OFFICER"},{id:"3",name:"JUNIOR ENGINEER"},{id:"4",name:"SENIOR MANAGER"}];
  this.setState({designation})
}
    handleChange(e,name)
    {
        this.setState({
            leave:{
                ...this.state.leave,
                [name]:e.target.value
            }
        })

    }
    close(){
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e,mode){
      e.preventDefault();
      console.log(this.state.leave);
      if (mode==="update") {
        console.log("update");
      } else {
        this.setState({leave:
            {designation:"",
            leaveType:"",
            noOfDay:""},designation:""  })
      }
    }


    componentDidMount(){
      if(getUrlVars()["type"]==="view"){
        for (var variable in this.state.leave)
          document.getElementById(variable).disabled=true;
      }
    }


  render(){
    let {handleChange,addOrUpdate}=this;
    let {leaveType,designation,noOfDay}=this.state.leave;
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

    const showActionButton=function(){
      if((!mode)||mode==="update"){
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    }


      return(
      <div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
        <div className="row">
          <div className="col-sm-6 col-sm-offset-3">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Designation </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="designation" name="designation" onChange={(e)=>{
                        handleChange(e,"designation")
                    }}required>
                    <option>Select Designation</option>
                    {renderOption(this.state.designation)}
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
                        <label for=""> Leave Type<span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                          <input type="text" name="leaveType" value={leaveType} id="leaveType" onChange={(e)=>{
                            handleChange(e,"leaveType")}}required/>

                    </div>
                </div>
            </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="">No of day <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                      <input type="number" name="noOfDay" value={noOfDay} id="noOfDay" onChange={(e)=>{
                          handleChange(e,"noOfDay")}} required/>

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
  <LeaveMaster />,
  document.getElementById('root')
);
