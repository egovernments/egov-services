class LeaveMaster extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            searchSet: {
                year: "",
                leaveType: "",
                tenantId: tenantId
            },
            leaveTypeList: [],
            years: []
        }

        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(e, name) {
        this.setState({
            searchSet: {
                ...this.state.leave,
                [name]: e.target.value
            }
        })
    }

    close() {
        open(location, '_self').close();
    }

    addOrUpdate(e) {
        e.preventDefault();
        var tempInfo = Object.assign({}, this.state.leave), type = getUrlVars()["type"];


        commonApiPost("hr-employee", "employees", "_baseregisterreport", { ..._this.state.searchSet, tenantId, pageSize: 500 }, function (err, res) {
            if (res && res.Employee) {
                showSuccess("Opening leave balance carry forward successfully.");
                _this.setState({
                    searchSet: {
                        "year": "",
                        "leaveType": "",
                        "tenantId": tenantId
                    }
                })
            } else {
                showError(err["statusText"]);
            }
        });
    }


    componentDidMount() {
        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }

        if (getUrlVars()["type"]) $('#hp-citizen-title').text("Opening Balance Carry Forward");

        var _this = this, count = 2, _state = {};
        const checkCountAndCall = function (key, res) {
            count--;
            _state[key] = res;
            if (count == 0)
                _this.setInitialState(_state);
        }
        getDropdown("years", function (res) {
            checkCountAndCall("years", res);
        });
        getDropdown("leaveTypes", function (res) {
            checkCountAndCall("leaveTypeList", res);
        });

    }


    render() {
        let { handleChange, addOrUpdate } = this;
        let { leaveType, year } = this.state.searchSet;

        const renderOption = function (list) {
            if (list) {
                return list.map((item) => {
                    return (<option key={item.id} value={item.id}>
                        {item.name}
                    </option>)
                })
            }
        }


        return (
            <div>
                <h3>Opening Balance Carry Forward</h3>
                <form onSubmit={(e) => { addOrUpdate(e, mode) }}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Calender Year </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="year" name="year" value={year} onChange={(e) => {
                                                handleChange(e, "year")
                                            }}>
                                                <option>Select Designation</option>
                                                {renderOption(this.state.years)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for=""> Leave Type<span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="leaveType" name="leaveType" value={leaveType.id} required="true" onChange={(e) => {
                                                handleChange(e, "leaveType")
                                            }}>
                                                <option value=""> Select Leave Type</option>
                                                {renderOption(this.state.leaveTypeList)}
                                            </select>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="text-center">
                            <button id="sub" type="submit" className="btn btn-submit">Carry Forward</button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
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
