class LeaveSummaryView extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
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



        try {
            
            commonApiPost("hr-leave", "leaveapplications", "_leavereport", {
                code : getUrlVars()["code"],
                departmentId: getUrlVars()["departmentId"]?getUrlVars()["departmentId"]:"",
                designationId: getUrlVars()["designationId"]?getUrlVars()["designationId"]:"",
                employeeType: getUrlVars()["employeeType"]?getUrlVars()["employeeType"]:"",
                employeeStatus: getUrlVars()["employeeStatus"]?getUrlVars()["employeeStatus"]:"",
                leaveType: getUrlVars()["leaveType"],
                fromDate: getUrlVars()["fromDate"],
                toDate: getUrlVars()["toDate"],
                leaveStatus: getUrlVars()["leaveStatus"],
                tenantId,
                pageSize: 500
            }, function (err, res) {

                if (res && res.LeaveApplication) {

                    _this.setState({
                        ...this.state,
                        result: res.LeaveApplication[0]
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


    closeWindow() {
        open(location, '_self').close();
    }



    render() {

        let { closeWindow } = this;
        let { result, leaveStatuses, employeeList } = this.state;

        return (
            <div>
                <form>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Employee Code-Name </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{getNameById(employeeList, result.employee)}</label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Application Number </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{result.applicationNumber}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Leave Type </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{result.leaveType.name}</label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Date Range </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{result.fromDate + "-" + result.toDate}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Number Of Days </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{result.leaveDays}</label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Status </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{getNameById(leaveStatuses, result.status, "code")}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Comments </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <label htmlFor="">{result.reason}</label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="text-center">
                            <button type="button" className="btn btn-close" onClick={(e) => { this.closeWindow() }}>Close</button>
                        </div>
                    </fieldset>
                </form>
            </div>
        );
    }
}






ReactDOM.render(
    <LeaveSummaryView />,
    document.getElementById('root')
);
