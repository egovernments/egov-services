var flag = 0
class LeaveSummaryView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            "result": { leaveType: "" },
            "leaveStatuses": [],
            "employeeList": []
        };
        this.closeWindow = this.closeWindow.bind(this);

    }

    setInitialState(initState) {
        this.setState(initState);
    }


    componentDidMount() {

        var _this = this;

        var leaveStatus = leaveStatuses.forEach(function (item) {
            if (item.code === "APPROVED") {
                return item.id;
            }
        });




        try {
            flag = 1;
            commonApiPost("hr-leave", "leaveapplications", "_leavereport", {
                code: getUrlVars()["code"],
                // departmentId: getUrlVars()["departmentId"]?getUrlVars()["departmentId"]:"",
                // designationId: getUrlVars()["designationId"]?getUrlVars()["designationId"]:"",
                // employeeType: getUrlVars()["employeeType"]?getUrlVars()["employeeType"]:"",
                // employeeStatus: getUrlVars()["employeeStatus"]?getUrlVars()["employeeStatus"]:"",
                leaveType: getUrlVars()["leaveType"],
                fromDate: "01/01/" + new Date().getFullYear(),//starting date of current year
                toDate: getUrlVars()["toDate"],//asondate in leave summary page
                leaveStatus: leaveStatus,
                tenantId,
                pageSize: 500
            }, function (err, res) {

                if (res && res.LeaveApplication) {

                    _this.setState({
                        ...this.state,
                        result: res.LeaveApplication
                    })
                } else {
                    _this.setState({
                        ...this.state,
                        result: []
                    })
                }
            });
        } catch (e) {
            console.log(e);
        }




        commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            pageSize: 500
        }, function (err, res) {
            if (res && res.Employee) {
                res.Employee.forEach(function (item, index, theArray) {
                    theArray[index] = {
                        "id": item.id,
                        "name": item.code + " - " + item.name
                    };
                });

                _this.setState({
                    ..._this.state,
                    employeeList: res.Employee
                });

            }
        });


        getDropdown("leaveStatus", function (res) {
            _this.setState({
                ..._this.state,
                leaveStatuses: res
            });

        });



    }

    componentDidUpdate(prevProps, prevState) {
        if (flag === 1) {
          flag = 0;
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
              'copy', 'csv', 'excel', 'pdf', 'print'
            ],
            ordering: false,
            language: {
              "emptyTable": "No Records"
            }
          });
        }
      }


    closeWindow() {
        open(location, '_self').close();
    }



    render() {

        let { closeWindow } = this;
        let { result, leaveStatuses, employeeList } = this.state;

        const renderTr = () => {
            if(result){
              return result.map((item, ind) => {
                  return (
                      <tr key={ind}>
                      <td>{getNameById(employeeList, item.employee)}</td>
                      <td>{item.applicationNumber}</td>
                      <td>{item.leaveType.name}</td>
                      <td>{item.fromDate +"-"+ item.toDate}</td>
                      <td>{item.leaveDays}</td>
                      <td>{getNameById(leaveStatuses,item.status,"code")}</td>
                      <td>{item.reason}</td>
                      </tr>
                  )
              })
            }
          }

        const showTable = () => {
            if (this.state.isSearchClicked) {
                return (
                    <div>
                        <div className="land-table">
                            <table id="employeeTable" className="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Employee Code-Name</th>
                                        <th>Application Number </th>
                                        <th>Leave Type</th>
                                        <th>Date Range</th>
                                        <th>Number Of Days</th>
                                        <th>Status</th>
                                        <th>Comments</th>


                                    </tr>
                                </thead>
                                <tbody>
                                    {renderTr()}
                                </tbody>
                            </table>
                        </div>
                    </div>
                )
            }
        }


        return (
            <div>
                <fieldset>

                    {showTable()}

                    <div className="text-center">
                        <button type="button" className="btn btn-close" onClick={(e) => { this.closeWindow() }}>Close</button>
                    </div>
                </fieldset>
            </div>
        );
    }
}






ReactDOM.render(
    <LeaveSummaryView />,
    document.getElementById('root')
);
