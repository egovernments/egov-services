class ApplyLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],leaveSet:{
    name:"",
    employee:"",
    workingDay:"",
    availableDays:"",
    fromDate:"",
    toDate:"",
    reason:"",
    leaveType:""},leave:[]}
    this.handleChange=this.handleChange.bind(this);
  }

  componentWillMount()
  {
    this.setState({
      leave:[{
              id: 1,
              name: "Casual",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Gazette",
              description: "",
              orderno: "1",
              active: true
          }]
    })
  }


  // componentDidMount()
  // {
    // if(getUrlVars()["type"]==="view")
    // {
    //   for (var obj in this.state.searchSet) {
    //       document.getElementById(obj).disabled = true;
    //   }
    // }

    // $('#employeeTable').DataTable({
    //   dom: 'Bfrtip',
    //   buttons: [
    //            'copy', 'csv', 'excel', 'pdf', 'print'
    //    ],
    //    ordering: false
    // });

    // console.log($('#employeeTable').length);

  // }
  //



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

  Add(e){
    e.preventDefault();
      console.log(this.state.leaveSet);
      this.setState({leaveSet:{
      name:"",
      employee:"",
      workingDay:"",
      availableDays:"",
      fromDate:"",
      toDate:"",
      reason:"",
      leaveType:""},leave:[] })
  }

  render() {
    let {handleChange}=this;
    let {name,employee,workingDay,availableDays,fromDate,toDate,reason,leaveType}=this.state.leaveSet;


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
                              <input type="text" id="name" name="name" readonly="true" value={name}
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
                                <input type="text" id="employee" readonly="true" name="employee" value={employee}
                                onChange={(e)=>{handleChange(e,"employee")}}/>
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
                          <input type="date" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
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
                          <input type="date"  id="toDate" name="toDate" value={toDate}
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
                            <select id="leaveType" name="leaveType" value={leaveType}
                              onChange={(e)=>{ handleChange(e,"leaveType")
                            }}>
                            <option>Select Leave Type</option>
                            {renderOption(this.state.leave)}
                           </select>
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
                              <input type="number" id="workingDay" name="workingDay" value={workingDay}
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
                                <input  type="number" id="availableDays" name="availableDays" value={availableDays}
                                onChange={(e)=>{handleChange(e,"availableDays")}}/>
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
