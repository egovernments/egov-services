

class EmployeeSearchCommon extends React.Component {
  constructor(props) {
    super(props);
    this.state={result:[],searchSet:{employeeStatusCode:"",
    name:"",
    code:"",
    departmentCode:"",
    designationCode:"",
    employeeTypeCode:"",
    activeCode:"",
    functionaryCode:"",
    functionCode:"",
    drawingOfficerCode:"",userName:" "},isSearchClicked:false,mployeeType:[],department:[],designation:[],employeeStatus:[],active:[],function:[],functionary:[],drawingOfficer:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
  }

  search(e)
  {
    // let {employeeStatusCode,
    // name,
    // code,
    // departmentCode,
    // designationCode,
    // employeeTypeCode,
    // activeCode,
    // functionaryCode,
    // functionCode,
    // drawingOfficerCode,userName}=this.state.searchSet;
    // e.preventDefault();
    // //call api call
    // var result=[];
    // for(var i=1;i<=100;i++)
    // {
    //     result.push({
    //         code:i,name:"murali"+1,designation:'xyz',department:"yxs",userName:"avs"
    //     })
    // }
    // this.setState({
    //   isSearchClicked:true,
    //   result
    // })

    // $('#employeeTable').DataTable().draw();
    // console.log($('#employeeTable').length);

  }

  componentWillMount()
  {
    // var result=[];
    // for(var i=1;i<=100;i++)
    // {
    //     result.push({
    //         code:i,name:"murali"+i,designation:'xyz',department:"yxs",userName:"avs"
    //     })
    // }
    // this.setState({
    //   isSearchClicked:true,
    //   result
    // })

    this.setState({
      employeeType:[{
              id: 1,
              name: "Deputation",
              chartOfAccounts: ""
          },
          {
              id: 2,
              name: "Permanent",
              chartOfAccounts: ""
          },
          {
              id: 3,
              name: "Daily Wages`",
              chartOfAccounts: ""
          },
          {
              id: 4,
              name: "Temporary",
              chartOfAccounts: ""
          },
          {
              id: 5,
              name: "Contract",
              chartOfAccounts: ""
          }],
      departments:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      designation:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      active:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      employeeStatus:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      function:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      functionary:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }],
      drawingOfficer:[{
              id: 1,
              name: "Juniour Engineer",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Assistance Engineer",
              description: "",
              orderno: "1",
              active: true
          }]
    })
  }

  componentDidMount()
  {
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

  }



  handleChange(e,name)
  {

      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }

  // updateTable()
  // {
  //   $('#employeeTable').DataTable({
  //     dom: 'Bfrtip',
  //     buttons: [
  //              'copy', 'csv', 'excel', 'pdf', 'print'
  //      ],
  //      ordering: false
  //   });
  //
  // }

  render() {
    console.log(this.state.searchSet);
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,result}=this.state;
    let {employeeStatusCode,
    name,
    code,
    departmentCode,
    designationCode,
    employeeTypeCode,
    activeCode,
    functionaryCode,
    functionCode,
    drawingOfficerCode,userName}=this.state.searchSet;
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
    // const showTable=function()
    // {
    //   if(isSearchClicked)
    //   {
    //       return (
    //         <table id="employeeTable" className="table table-bordered">
    //             <thead>
    //                 <tr>
    //                     <th>Employee Code</th>
    //                     <th>Employee Name</th>
    //                     <th>Employee Designation</th>
    //                     <th>Employee Department</th>
    //                     <th>Employee Username</th>
    //                     <th>Action</th>
    //
    //                 </tr>
    //             </thead>
    //
    //             <tbody id="employeeSearchResultTableBody">
    //                 {
    //                     renderBody()
    //                 }
    //             </tbody>
    //
    //         </table>
    //
    //       )
    //
    //       // updateTable();
    //       // $('#employeeTable').DataTable({
    //       //   dom: 'Bfrtip',
    //       //   buttons: [
    //       //            'copy', 'csv', 'excel', 'pdf', 'print'
    //       //    ],
    //       //    ordering: false
    //       // });
    //         // alert("hai");
    //   }
    //
    // }
    // const renderBody=function()
    // {
    //   return result.map((item,index)=>
    //   {
    //         return (<tr key={index}>
    //                 <td data-label="code">{item.code}</td>
    //                 <td data-label="name">{item.name}</td>
    //                 <td data-label="designation">{item.designation}</td>
    //                 <td data-label="department">{item.department}</td>
    //                 <td data-label="userName">{item.userName}</td>
    //                 <td data-label="action">
    //                             <a href={`../../../../app/hr/employee/create.html?code=${item.code}`}>view/edit</a>
    //                 </td>
    //             </tr>
    //         );
    //
    //   })
    // }
    // const disbaled=function(type) {
    //     if (type==="view") {
    //           return "ture";
    //     } else {
    //         return "false";
    //     }
    // }
    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
            <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code} onChange={(e)=>{
                                handleChange(e,"code")
                            }}/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Type  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="employeeTypeCode" name="employeeTypeCode" value={employeeTypeCode} onChange={(e)=>{
                                  handleChange(e,"employeeTypeCode")
                              }}>
                                  <option>Select Type</option>
                                  {renderOption(this.state.employeeType)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
              </div>

                <div className="row">

                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Employee Name</label>
                            </div>
                            <div className="col-sm-6">

                                <input  type="text" id="name" name="name" onChange={(e)=>{
                                    handleChange(e,"name")
                                }}  value={name}/>

                            </div>
                        </div>
                      </div>

                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="">Status</label>
                              </div>
                              <div className="col-sm-6">
                              <div className="styled-select">
                                  <select  id="employeeStatusCode" name="employeeStatusCode" value={employeeStatusCode} onChange={(e)=>{
                                      handleChange(e,"employeeStatusCode")
                                  }}>
                                      <option value="">Select Status</option>
                                      {renderOption(this.state.employeeStatus)}
                                 </select>
                              </div>
                              </div>
                          </div>
                        </div>
                </div>

                <div className="row">

                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Type</label>
                            </div>
                            <div className="col-sm-6">

                            <div className="styled-select">
                                <select id="type" name="type" value={type} onChange={(e)=>{
                                    handleChange(e,"type")
                                }} >
                                    <option value="">Select Type</option>
                                    {renderOption(this.state.active)}
                               </select>
                            </div>

                            </div>
                        </div>
                      </div>

                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for="">Month</label>
                              </div>
                              <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="month" name="month" value={month} onChange={(e)=>{
                                      handleChange(e,"month")
                                  }} >
                                      <option value="">Select Month</option>
                                      {renderOption(this.state.active)}
                                 </select>
                              </div>
                              </div>
                          </div>
                        </div>
                </div>


                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Department  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="department" name="department" value={departmentCode} onChange={(e)=>{
                                  handleChange(e,"department")
                              }}>
                                <option>Select Department</option>
                                {renderOption(this.state.department)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Designation  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="styled-select">
                                <select id="designation" name="designation" value={designationCode} onChange={(e)=>{
                                    handleChange(e,"designationCode")
                                }}>
                                <option>Select Designation</option>
                                {renderOption(this.state.designation)}
                               </select>
                            </div>
                            </div>
                        </div>
                      </div>
                </div>

                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Calender Year  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="calenderYear" name="calenderYear" value={calenderYear} onChange={(e)=>{
                                  handleChange(e,"calenderYear")
                              }}>
                                <option>Select Calender Year</option>
                                {renderOption(this.state.calenderYear)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Functionary  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="styled-select">
                                <select id="functionaryCode" name="functionaryCode" value={functionaryCode} onChange={(e)=>{
                                    handleChange(e,"functionaryCode")
                                }}>
                                <option>Select Functionary</option>
                                {renderOption(this.state.functionary)}
                               </select>
                            </div>
                            </div>
                        </div>
                      </div>
                </div>

                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Leave Type  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="leaveType" name="leaveType" value={leaveType} onChange={(e)=>{
                                  handleChange(e,"leaveType")
                              }}>
                                <option>Select Leave Type</option>
                                {renderOption(this.state.leaveType)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Leave Status  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="styled-select">
                                <select id="leaveStatus" name="leaveStatus" value={leaveStatus} onChange={(e)=>{
                                    handleChange(e,"leaveStatus")
                                }}>
                                  <option>Select Leave Status</option>
                                  {renderOption(this.state.leaveStatus)}
                               </select>
                            </div>
                            </div>
                        </div>
                      </div>
                    </div>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for=""> Date From <span>*</span></label>
                                  </div>
                                      <div className="col-sm-6">
                                          <div className="text-no-ui">
                                            <span>
                                                <i className="glyphicon glyphicon-calendar"></i>
                                            </span>
                                            <input type="date" name="DateFrom" id="DateFrom"value={DateFrom}
                                            onChange={(e)=>{handleChange(e,"DateFrom")}}/>
                                          </div>
                                      </div>
                                  </div>
                              </div>
                              <div className="col-sm-6">
                                  <div className="row">
                                      <div className="col-sm-6 label-text">
                                          <label for="">Date TO <span>*</span></label>
                                        </div>
                                            <div className="col-sm-6">
                                                <div className="text-no-ui">
                                                  <span>
                                                      <i className="glyphicon glyphicon-calendar"></i>
                                                  </span>
                                                  <input type="date" name="DateTo" id="DateTo"value={DateTO}
                                                  onChange={(e)=>{handleChange(e,"DateTO")}}/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for=""> Asson Date <span>*</span></label>
                                      </div>
                                          <div className="col-sm-6">
                                              <div className="text-no-ui">
                                                <span>
                                                    <i className="glyphicon glyphicon-calendar"></i>
                                                </span>
                                                <input type="date" name="AssonDate" id="AssonDate"value={AssonDate}
                                                onChange={(e)=>{handleChange(e,"AssonDate")}}/>
                                              </div>
                                          </div>
                                      </div>
                                  </div>
                                  <div className="col-sm-6">
                                      <div className="row">
                                          <div className="col-sm-6 label-text">
                                              <label for="">Enter Date  <span>*</span></label>
                                            </div>
                                                <div className="col-sm-6">
                                                    <div className="text-no-ui">
                                                      <span>
                                                          <i className="glyphicon glyphicon-calendar"></i>
                                                      </span>
                                                      <input type="date" name="EnterDate" id="EnterDate"value={EnterDate}
                                                      onChange={(e)=>{handleChange(e,"EnterDate")}}/>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                            </div>






            <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
                <button type="submit"  className="btn btn-submit">Search</button>
            </div>
          </fieldset>
          </form>


      </div>
    );
  }
}



// <br/>
//
// {showTable()}






// ReactDOM.render(
//   <EmployeeSearch />,
//   document.getElementById('root')
// );
