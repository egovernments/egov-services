class Leavetype extends React.Component{

  constructor(props) {
    super(props);
    this.state={leaveType:{
    name:"",
    pay_eligible:"",
    encashable:"",
    halfday_allowed:"",
    accumulative:""
  }}
    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
  }

  handleChange(e,name)
  {

      this.setState({
          leaveType:{
              ...this.state.leaveType,
              [name]:e.target.value
          }
      })

  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

addOrUpdate(e,mode)
{
  e.preventDefault();
  console.log(this.state.leaveType);
  if (mode==="update") {
        console.log("update");
  } else {
    this.setState({leaveType:{
    name:"",
    pay_eligible:"",
    encashable:"",
    halfday_allowed:"",
    accumulative:""
  } })
  }


}

componentDidMount()
{

 if (getUrlVars()["type"]==="view")
  {
  for (var variable in this.state.leaveType)
    {
      // console.log($(`#${variable}`).length);

      document.getElementById(variable).disabled= true ;

    }

  }


}
  render(){
    let {handleChange,addOrUpdate}=this;
    let{name,pay_eligible,encashable,halfday_allowed,accumulative}=this.state.leaveType;
    let mode=getUrlVars()["type"];

      const showActionButton=function() {
      if((!mode)|| mode==="update")
      {
          ;return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    }

    return(<div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="">Leave Type Name <span> * </span></label>
                </div>
                <div className="col-sm-6">
                    <input type="text" name="name" id="name" value={name}
                    onChange={(e)=>{handleChange(e,"name")}} required/>

                </div>
            </div>
          </div>

          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Is Half Day <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                        <label className="radio-inline radioUi">
                          <input type="radio" name="halfday_allowed" id="halfday_allowed"  value="yes"
                           onChange={(e)=>{handleChange(e,"halfday_allowed")}} /> Yes
                        </label>
                        <label className="radio-inline radioUi">
                          <input type="radio" name="halfday_allowed" id="halfday_allowed" value="no"
                          onChange={(e)=>{handleChange(e,"halfday_allowed")}}/> No
                        </label>
                  </div>
              </div>
            </div>
          </div>
            <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">Pay Eligible <span> *</span> </label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="pay_eligible" id="pay_eligible" value="yes"
                                      onChange={(e)=>{handleChange(e,"pay_eligible")  }}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="pay_eligible" id="pay_eligible" value="no"
                                      onChange={(e)=>{handleChange(e,"pay_eligible")}}/> No
                                </label>
                          </div>
                      </div>
                  </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                                <label for="">Accumulative <span> *</span></label>
                            </div>
                            <div className="col-sm-6">
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" id="accumulative" value="yes"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> Yes

                                  </label>
                                  <label className="radio-inline radioUi">
                                    <input type="radio" name="accumulative" id="accumulative" value="no"
                                        onChange={(e)=>{handleChange(e,"accumulative")}}/> No
                                  </label>
                            </div>
                        </div>
                    </div>
                  </div>

              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="">enCashAble <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="yes"
                                   onChange={(e)=>{handleChange(e,"encashable")}}/> Yes
                                </label>
                                <label className="radio-inline radioUi">
                                  <input type="radio" name="encashable" id="encashable" value="no"
                                  onChange={(e)=>{handleChange(e,"encashable")}}/> No
                                </label>
                          </div>
                      </div>
                    </div>
                </div>

                <div className="text-center">
                    {showActionButton()}  &nbsp;&nbsp;
                    <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
                </div>
                </fieldset>
                </form>
      </div>
    );
  }
}

ReactDOM.render(
  <Leavetype />,
  document.getElementById('root')
);
