class ApplyLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],leaveSet:{
      "employee": "",
       "leaveType": {
       	"id" : ""
       },
       "fromDate" : "",
       "toDate": "",
       "leaveDays": "",
       "reason": "",
       "status": "",
       "stateId": "",
       "tenantId" : tenantId
     },leaveList:[]}
    this.handleChange=this.handleChange.bind(this);
    this.handleChangeThreeLevel=this.handleChangeThreeLevel.bind(this);
  }

  componentDidMount(){
    var type = getUrlVars()["type"], _this = this, duration,to,from;
    var id = getUrlVars()["id"];
    $('#fromDate').datepicker({
        format: 'dd/mm/yyyy',
        autclose:true

    });
    $('#fromDate').on("change", function(e) {
      _this.setState({
            leaveSet: {
                ..._this.state.leaveSet,
                "fromDate":$("#fromDate").val()
            }
      })

      });
      $('#toDate').datepicker({
          format: 'dd/mm/yyyy',
          autclose:true

      });
      $('#toDate').on("change", function(e) {

        _this.setState({
              leaveSet: {
                  ..._this.state.leaveSet,
                  "toDate":$("#toDate").val()
              }

        })
        var start = $('#fromDate').datepicker('getDate');
        var end   = $('#toDate').datepicker('getDate');
        var days   = (end - start)/1000/60/60/24;
        $('#workingDay').val(days + ' days');

        });

          this.setState({
            leaveSet:getCommonMasterById("hr-employee","employees","Employee",id).responseJSON["Employee"][0]
          })
      }

  componentWillMount()
  {
    this.setState({
      leaveList:getCommonMaster("hr-leave","leavetypes","LeaveType").responseJSON["LeaveType"]
    })
  }

  handleChange(e,name)
  {

      this.setState({
          leaveSet:{
              ...this.state.leaveSet,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
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


  Add(e){
    e.preventDefault();
      console.log(this.state.leaveSet);
      this.setState({leaveSet:{
      name:"",
      code:"",
      workingDay:"",
      noOfDays:"",
      fromDate:"",
      toDate:"",
      reason:"",
      leaveType:""},leaveList:[] })
  }

  render() {
    let {handleChange,handleChangeThreeLevel}=this;
    let {name,code,workingDay,noOfDays,fromDate,toDate,reason,leaveType}=this.state.leaveSet;


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


    return (
      <div>
        <form onSubmit={(e)=>{this.Add(e)}}>
          <fieldset>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
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
                              <label for="">Employee Code</label>
                            </div>
                            <div className="col-sm-6">
                                <input type="text" id="code" name="code" value={code}
                                onChange={(e)=>{handleChange(e,"code")}}/>
                            </div>
                        </div>
                      </div>
                    </div>


                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">From Date <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                          <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                          onChange={(e)=>{handleChange(e,"fromDate")}}required/>

                          </div>
                      </div>
                    </div>
                </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">To Date <span>*</span> </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text"  id="toDate" name="toDate" value={toDate}
                          onChange={(e)=>{
                              handleChange(e,"toDate")}}required/>
                          </div>
                      </div>
                  </div>
                </div>
            </div>


            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="leaveType">Leave Type</label>
                        </div>
                        <div className="col-sm-6">
                            <div className="styled-select">
                          //  
                            </div>
                        </div>
                    </div>
                </div>


                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                              <label for="Reason">Reason <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                          <textarea rows="4" cols="50" id="reason" name="reason" value={reason}
                          onChange={(e)=>{handleChange(e,"reason")}}required></textarea>
                          </div>
                      </div>
                  </div>
              </div>


              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Working Days</label>
                          </div>
                          <div className="col-sm-6">

                              <input type="text" id="workingDay" name="workingDay" value=""
                              onChange={(e)=>{handleChange(e,"workingDay")}}/>
                          </div>
                      </div>
                  </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Available Leave</label>
                            </div>
                            <div className="col-sm-6">
                                <input  type="number" id="noOfDays" name="noOfDays" value={noOfDays}
                                onChange={(e)=>{handleChange(e,"noOfDays")}}/>
                            </div>
                        </div>
                      </div>
                  </div>



            <div className="text-center">
               <button type="submit"  className="btn btn-submit">Approve</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
      </div> );

  }
}






ReactDOM.render(
  <ApplyLeave />,
  document.getElementById('root')
);
