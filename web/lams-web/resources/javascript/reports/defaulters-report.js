var flag = 0;
class DefaultersReport extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            "result": [],
            "searchSet": {
                "recruitmentType": "",
                "employeeType": "",
                "employeeStatus": ""
            },
            "employeeTypes": [],
            "recruitmentTypes": [],
            "employeeStatusTypes": [],
            "employeeGroups": [],
            "motherTongueTypes": [],
            "recruitmentModes": [],
            "recruitmentQuota": [],
            "bank": [],
            "branch": [],
            "isSearchClicked": false
        };
        this.handleChange = this.handleChange.bind(this);
        this.search = this.search.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.closeWindow = this.closeWindow.bind(this);
        this.formatDob = this.formatDob.bind(this);


    }

    setInitialState(initState) {
        this.setState(initState);
    }

    componentWillMount() {


    }

    componentDidUpdate(prevProps, prevState) {
        if (flag === 1) {
            flag = 0;
            $('#employeeTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'copy', 'csv', 'excel'
                ],
                ordering: false,
                language: {
                    "emptyTable": "No Records"
                }
            });
        }
    }

    handleChange(e, name) {
        this.setState({
            ...this.state,
            searchSet: {
                ...this.state.searchSet,
                [name]: e.target.value
            }
        })
    }

    closeWindow() {
        open(location, '_self').close();
    }

    formatDob(date) {
        if (date) {
            var dateParts1 = date.split("-");
            var newDateStr = dateParts1[2] + "/" + dateParts1[1] + "/" + dateParts1[0];
            return newDateStr;
        }
    }

    search(e) {
        e.preventDefault();
        $('#employeeTable').DataTable().destroy();
        var _this = this
        var result;
        try {
            flag = 1;
            result = commonApiPost("hr-employee", "employees", "_baseregisterreport", { ..._this.state.searchSet, tenantId, pageSize: 500 }, function (err, res) {
                if (res && res.Employee) {
                    _this.setState({
                        ..._this.state,
                        isSearchClicked: true,
                        result: res.Employee
                    })
                } else {
                    _this.setState({
                        ..._this.state,
                        isSearchClicked: true,
                        result: []
                    })
                }
            });
        } catch (e) {
            result = [];
            console.log(e);
        }
    }

    render() {
        let { handleChange, search, formatDob, closeWindow } = this;
        let { result, employeeTypes, employeeStatusTypes, recruitmentTypes, employeeGroups, motherTongueTypes, recruitmentModes, recruitmentQuota, bank, branch } = this.state;
        let { recruitmentType, employeeStatus, employeeType } = this.state.searchSet;

        const renderOptions = function (list) {
            if (list && list.constructor == Array) {
                return list.map((item) => {
                    return (<option key={item.id} value={item.id}>
                        {item.name}
                    </option>)
                })
            }
        }
        const renderOptionsForDesc = function (list) {
            if (list && list.constructor == Array) {
                return list.map((item) => {
                    return (<option key={item.id} value={item.id}>
                        {item.code}
                    </option>)
                })
            }
        }

        const renderTr = () => {
            return result.map((item, ind) => {
                return (
                    <tr key={ind}>
                        <td>{ind + 1}</td>
                        <td>{item.code}</td>
                        <td>{item.salutation ? item.salutation + " " + item.name : item.name}</td>
                        <td>{getNameById(employeeTypes, item.employeeType)}</td>
                        <td>{getNameById(employeeStatusTypes, item.employeeStatus, "code")}</td>
                        <td>{getNameById(employeeGroups, item.group)}</td>
                        <td>{formatDob(item.dob)}</td>
                        <td>{item.gender}</td>
                        <td>{item.maritalStatus}</td>
                        <td>{item.userName}</td>
                        <td>{item.active ? "Yes" : "No"}</td>
                        <td>{item.mobileNumber}</td>
                    </tr>
                )
            })
        }

        const showTable = () => {
            if (this.state.isSearchClicked) {
                return (
                    <div className="form-section" >
                        <h3 className="pull-left">Employee Details </h3>
                        <div className="clearfix"></div>
                        <div className="table-responsive">
                            <table id="employeeTable" className="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>S.No</th>
                                        <th>Allottee Number</th>
                                        <th>Agreement Name</th>
                                        <th>Mobile Number</th>
                                        <th>Locality</th>
                                        <th>Allotte address</th>
                                        <th>Outstanding Total Rent</th>
                                        <th>Current rent pending</th>
                                        <th>Arrears Pending</th>
                                        <th>Total pending</th>
                                        <th>Agreement created date</th>
                                        <th>Agreement expiry date</th>
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
                <div className="form-section">
                    <h3 className="pull-left">Employee Search </h3>
                    <div className="clearfix"></div>
                    <form onSubmit={(e) => { search(e) }}>
                        <fieldset>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Revenue ward </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="revenueward" value={recruitmentType} onChange={(e) => { handleChange(e, "revenueward") }}>
                                                    <option value="">Select RevenueWard</option>
                                                    {renderOptionsForDesc(employeeStatusTypes)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Not paid from </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="employeeType" value={employeeType} onChange={(e) => { handleChange(e, "employeeType") }}>
                                                    <option value="">Select Number of years</option>
                                                    {renderOptions(employeeTypes)}
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
                                            <label for="">From Rent </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input id="noOfDaysInMonth" type="number" value={employeeStatus} onChange={(e) => { handleChange(e, "noOfDaysInMonth") }} />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">To Rent </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input id="noOfDaysInMonth" type="number" value={employeeStatus} onChange={(e) => { handleChange(e, "noOfDaysInMonth") }} />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Top Defaulters </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="recruitmentType" value={recruitmentType} onChange={(e) => { handleChange(e, "recruitmentType") }}>
                                                    <option value="">Select Top Defaulters</option>
                                                    {renderOptions(recruitmentTypes)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Asset category </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="styled-select">
                                                <select id="recruitmentType" value={recruitmentType} onChange={(e) => { handleChange(e, "recruitmentType") }}>
                                                    <option value="">Select Asset category</option>
                                                    {renderOptions(recruitmentTypes)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center">
                                <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                                <button type="button" className="btn btn-close" onClick={(e) => { this.closeWindow() }}>Close</button>
                            </div>
                        </fieldset>
                    </form>
                </div>
                <br />
                {showTable()}
            </div>
        );
    }
}






ReactDOM.render(
    <DefaultersReport />,
    document.getElementById('root')
);
