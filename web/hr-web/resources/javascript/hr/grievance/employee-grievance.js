class EmployeeGrievance extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            list: [], grievanceSet: {
                "employee": "",
                "name": "",
                "code": "",
                "grievanceType": "",
                "description": "",
                "documents": "",
                "tenantId": tenantId

            }, grievanceTypeList: [{id:1,name:"Work Location"}, {id:2,name:"Job"}]
        }
        this.handleChange = this.handleChange.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.getPrimaryAssigmentDep = this.getPrimaryAssigmentDep.bind(this);

    }

    componentDidMount() {

        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
            }
        }

        $('#hp-citizen-title').text("Employee Grievance");

        commonApiPost("hr-employee", "employees", "_loggedinemployee", { tenantId }, function (err, res) {
            if (res && res.Employee && res.Employee[0]) {
                var obj = res.Employee[0];
                _this.setState({
                    grievanceSet: {
                        ..._this.state.grievanceSet,
                        name: obj.name,
                        code: obj.code,
                        employee: obj.id
                    },
                    departmentId: _this.getPrimaryAssigmentDep(obj, "department")
                })
            } else {
                showError("Something went wrong. Please contact Administrator.");
            }
        });


    }

    getPrimaryAssigmentDep(obj, type) {
        for (var i = 0; i < obj.assignments.length; i++) {
            if (obj.assignments[i].isPrimary) {
                return obj.assignments[i][type];
            }
        }
    }

    componentWillMount() {

    }

    handleChange(e, name) {
        this.setState({
            grievanceSet: {
                ...this.state.grievanceSet,
                [name]: e.target.value
            }
        })

    }

    close() {
        // widow.close();
        open(location, '_self').close();
    }


    addOrUpdate(e, mode) {
        e.preventDefault();
        var _this = this;
        window.location.reload(true);
        showSuccess("Successfully Sumbited")
    }


    render() {
        let { handleChange, addOrUpdate, handleChangeThreeLevel } = this;
        let { employee, name, code, grievanceType, description, documents } = this.state.grievanceSet;

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
            <div className="form-section">
                <h3>Employee Grievance </h3>
                <form onSubmit={(e) => { addOrUpdate(e) }}>
                    <fieldset>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Employee Name</label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" id="name" name="name" value={name}
                                            onChange={(e) => { handleChange(e, "name") }} />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Employee Code</label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" id="code" name="code" value={code}
                                            onChange={(e) => { handleChange(e, "code") }} />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="grievanceType">Grievance Type<span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="grievanceType" name="grievanceType" value={grievanceType} required="true" onChange={(e) => {
                                                handleChange(e, "grievanceType") }}>
                                                <option value=""> Select Grievance Type</option>
                                                {renderOption(this.state.grievanceTypeList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="Reason">Description <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea rows="4" cols="50" id="descriptions" name="description" value={description}
                                            onChange={(e) => { handleChange(e, "description") }} required></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Attach Documents <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <input type="file" id="documents" name="documents" value="documents" value={documents}
                                                onChange={(e) => { handleChange(e, "documents") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="text-center">
                            <button type="submit" className="btn btn-submit">Submit</button> &nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>

                        </div>
                    </fieldset>
                </form>
            </div>);

    }
}






ReactDOM.render(
    <EmployeeGrievance />,
    document.getElementById('root')
);
