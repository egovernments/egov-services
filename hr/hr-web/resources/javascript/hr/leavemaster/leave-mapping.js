class LeaveMaster extends React.Component{
constructor(props){
  super(props);
  this.state={list:[],leave:{

    "designation": "",
    "leaveType":{
      "name":""
    },
    "noOfDays":"",

    "tenantId":"ap.public"
    },
        designationList:[],leaveTypeList:[]}

  this.addOrUpdate=this.addOrUpdate.bind(this);
  this.handleChange=this.handleChange.bind(this);
  this.handleChangeThreeLevel=this.handleChangeThreeLevel.bind(this);

}
componentWillMount()
{
  this.setState({
    designationList:assignments_designation,
    leaveTypeList:getCommonMaster("hr-leave","leavetypes","LeaveType").responseJSON["LeaveType"],

})
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


    // addOrUpdate(e,mode){
    //   var tempObj={
    //                 "designation": this.state.leave.designation,
    //                 "leaveType":
    //                   {
    //                       "id":this.state.leave.leaveType
    //                     },
    //                   "noOfDays": this.state.leave.noOfDay,
    //                   "tenantId": "ap.public"
    //               };
    //
    //     e.preventDefault();
    //     //  console.log(this.state.leave);
    //     // console.log(mode);
    //     if (mode==="update") {
    //         console.log("update");
    //     } else {
    //
    //           console.log(tempObj);
    //
    //       }
        // }
        addOrUpdate(e){
          // var finalPost={
          //   "RequestInfo":requestInfo,
          //
          // }
          var tempObj={
                          "designation": this.state.leave.designation,
                          "leaveType":
                            {
                                "id":this.state.leave.leaveType,
                                "name":this.state.leave.leaveType.name
                              },
                            "noOfDays": this.state.leave.noOfDays,
                            "tenantId": "ap.public"
                        };

          e.preventDefault();
          //  console.log(tempObj);
          // var tempInfo=this.state.leave;
          var body={
              "RequestInfo":requestInfo,
              "LeaveAllotment":[tempObj]
            };

          var response=$.ajax({
                url:baseUrl+"/hr-leave/leaveallotments/_create",
                type: 'POST',
                dataType: 'json',
                data:JSON.stringify(body),
                async: false,
                contentType: 'application/json',
                headers:{
                  'auth-token': authToken
                }
            });

          //  console.log(response);
          if(response["statusText"]==="OK")
          {
            alert("Successfully added");
          }
          else {
            // console.log(tempObj);
            // alert(response["statusText"]);
            // this.setState({
            //   leave:{
            //
            //     "designation": "",
            //     "leaveType":"",
            //     "noOfDays":"",
            //
            //     "tenantId":"1",
            //
            //   },
            //   leaveType:
            //    {
            //             "id": "",
            //
            //     },
            //   designationList:[],
            //   LeaveAllotmentTypeList:[]
            // })
     }
    }

    handleChangeThreeLevel(e,pName,name)
    {
      this.setState({
        leave:{
          ...this.state.leave,
          [pName]:{
              ...this.state.leave[pName],
              [name]:e.target.value
          }
        }

      })

}



    componentDidMount(){
      var type=getUrlVars()["type"];
      var id=getUrlVars()["id"];

      if(getUrlVars()["type"]==="view")
      {
        $("input,select,radio,textarea").prop("disabled", true);
        }

        if(type==="view"||type==="update")
        {
          // console.log(getCommonMasterById("hr-leave","leaveallotments","LeaveAllotment",id).responseJSON["LeaveAllotment"][0]);
            this.setState({
              leave:getCommonMasterById("hr-leave","leaveallotments","LeaveAllotment",id).responseJSON["LeaveAllotment"][0]
            })
        }
    }

  render(){
    let {handleChange,addOrUpdate,handleChangeThreeLevel}=this;
    let {leaveType,designation,noOfDays}=this.state.leave;
    let mode=getUrlVars()["type"];
    console.log(this.state.leave);
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
                    <select id="designation" name="designation" value={designation} onChange={(e)=>{
                        handleChange(e,"designation")
                    }}required>
                    <option>Select Designation</option>
                    {renderOption(this.state.designationList)}
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
                    <div className="styled-select">
                    <select id="leaveType" name="leaveType" value={leaveType.name} required="true" onChange={(e)=>{
                        handleChangeThreeLevel(e,"leaveType","name")
                    }}>
                    <option> select Leave Type</option>
                    {renderOption(this.state.leaveTypeList)}
                   </select>
                    </div>

                    </div>
                </div>
            </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="">No of day <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                      <input type="number" name="noOfDays" value={noOfDays} id="noOfDays" onChange={(e)=>{
                          handleChange(e,"noOfDays")}} required/>

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
  <LeaveMaster />,
  document.getElementById('root')
);
