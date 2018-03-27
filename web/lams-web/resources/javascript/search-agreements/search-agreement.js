var flag = 0;
class AgreementSearch extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            agreements: [],
            searchSet: {
                locality: "",
                agreementNumber: "",
                assetCategory: "",
                mobileNumber: "",
                allotteeName: "",
                revenueWard: "",
                electionWard: "",
                assetCode: "",
                tenderNumber: "",
                fromDate: "",
                toDate: "",
                acknowledgementNumber: "",
                shopNumber: "",
                shoppingComplexName: "",
                referenceNumber:"",
                tenantId
            },
            isSearchClicked: false,
            assetCategories: [],
            locality: [],
            revenueWards: [],
            electionwards: [],
            modify: false,
            users: [],
            hideCollectTaxOption: true
        }
        this.handleChange = this.handleChange.bind(this);
        this.search = this.search.bind(this);
        this.handleMobileValidation = this.handleMobileValidation.bind(this);
    }

    handleMobileValidation(e) {
        if (e.target.value && !(/[0-9]{10}/).test(e.target.value)) {
            e.target.setCustomValidity("Please enter 10 digits.");
        } else {
            e.target.setCustomValidity("");
        }
    }
    search(e) {
        e.preventDefault();
        var _this = this;
        try {
            var searchSet = Object.assign({}, this.state.searchSet);
            if (searchSet.allottee)
                delete searchSet.allotteeName;
            else
                delete searchSet.allottee;

            //call api call
            var agreements = commonApiPost("lams-services", "agreements", "_search", searchSet).responseJSON["Agreements"] || [];
            flag = 1;
            if (agreements && agreements.length) {
                agreements.sort(function (d1, d2) {
                    var date1 = d1.createdDate.split("/");
                    var date2 = d2.createdDate.split("/");
                    if (new Date(date1[2], date1[1] - 1, date1[0]).getTime() > new Date(date2[2], date2[1] - 1, date2[0]).getTime()) {
                        return -1;
                    } else if (new Date(date1[2], date1[1] - 1, date1[0]).getTime() < new Date(date2[2], date2[1] - 1, date2[0]).getTime()) {
                        return 1;
                    } else
                        return 0;
                })
            }

            _this.setState({
                isSearchClicked: true,
                agreements,
                modify: true
            });
            setTimeout(function () {
                _this.setState({
                    modify: false
                })
            }, 1200);
        } catch (e) {
            console.log(e);
        }
    }

    componentWillMount() {
        try {
            var locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality"));
        } catch (e) {
            console.log(e);
            var locality = [];
        }

        try {
            var electionwards = !localStorage.getItem("ward") || localStorage.getItem("ward") == "undefined" ? (localStorage.setItem("ward", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("ward"))) : JSON.parse(localStorage.getItem("ward"));
        } catch (e) {
            console.log(e);
            var electionwards = [];
        }

        try {
            var revenueWards = !localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined" ? (localStorage.setItem("revenueWard", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueWard"))) : JSON.parse(localStorage.getItem("revenueWard"));
        } catch (e) {
            console.log(e);
            var revenueWards = [];
        }

        try {
            var assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {usedForLease:true, tenantId }).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories"));
        } catch (e) {
            console.log(e);
            var assetCategories = [];
        }

        var res = commonApiPost("asset-services", "assetCategories", "_search", { tenantId });
        var bool = true;

        if (res && res.getResponseHeader("userInfo")) {
            try {
                var roles = JSON.parse(res.getResponseHeader("userInfo")).roles;
                for (var i = 0; i < roles.length; i++) {
                    if (roles[i].name == "Collection Operator") {
                        bool = false;
                        break;
                    }
                }
            } catch (e) {
                console.log(e);
            }
        }
        /*if(res && res.responseJSON && res.responseJSON.Employee && res.responseJSON.Employee[0]) {
          var res2 = commonApiPost("hr-employee", "employees/" + res.responseJSON.Employee[0].id, "_search", {tenantId});
          if(res2 && res2.responseJSON && res2.responseJSON.Employee && res2.responseJSON.Employee.user && res2.responseJSON.Employee.user.roles) {
            for(var i=0; i<res2.responseJSON.Employee.user.roles.length; i++) {
              if(res2.responseJSON.Employee.user.roles[i].name == "Collection Operator") {
                bool = true;
                break;
              }
            }
          }
        }*/

        this.setState({
            assetCategories,
            locality,
            electionwards,
            revenueWards,
            hideCollectTaxOption: bool
        });

    }

    componentDidMount() {
        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }

        let _this = this;


        //Fetch allottee name suggestions
        $("#name").autocomplete({
            source: function (request, response) {
                $.ajax({
                    url: baseUrl + "/user/_search?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: "json",
                    data: JSON.stringify({
                        RequestInfo: requestInfo,
                        name: request.term,
                        fuzzyLogic: true,
                        tenantId: tenantId
                    }),
                    contentType: 'application/json',
                    success: function (data) {
                        if (data && data.user && data.user.length) {
                            let users = [];
                            for (let i = 0; i < data.user.length; i++)
                                users.push(data.user[i].name);
                            response(users);
                            _this.setState({
                                users: data.user
                            })
                        }
                    }
                });
            },
            minLength: 3,
            change: function (event, ui) {
                if (ui.item && ui.item.value) {
                    var id;
                    if (_this.state.users && _this.state.users.constructor == Array) {
                        for (var i = 0; i < _this.state.users.length; i++) {
                            if (_this.state.users[i].name == ui.item.value) {
                                id = _this.state.users[i].id;
                            }
                        }
                    }

                    _this.setState({
                        searchSet: {
                            ..._this.state.searchSet,
                            allotteeName: ui.item.value,
                            allottee: id || ""
                        }
                    })
                }
            }
        });

        $(".date-picker").datepicker({
            format: "dd/mm/yyyy",
            autoclose: true
        });

        $(".date-picker").on("change", function (e) {
            _this.setState({
                searchSet: {
                    ..._this.state.searchSet,
                    [e.target.id]: e.target.value
                }
            })
            if (_this.state.searchSet.fromDate != "" && _this.state.searchSet.toDate == "") {
                $('#toDate').prop("required", true);
            } else if (_this.state.searchSet.fromDate == "" && _this.state.searchSet.toDate != "") {
                $('#fromDate').prop("required", true);
            } else {
                $('#toDate').prop("required", false);
                $('#fromDate').prop("required", false);
            }
        })
    }

    componentWillUpdate() {
        if (flag == 1) {
            flag = 0;
            $('#agreementTable').dataTable().fnDestroy();
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.modify) {
            $('#agreementTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'copy', 'csv', 'excel', 'pdf', 'print'
                ],
                ordering: false,
                bDestroy: true,
                language: {
                    "emptyTable": "No Records"
                }
            });
        }
    }

    handleChange(e, name) {
        if (name == "mobileNumber") {
            if (/[^0-9]/.test(e.target.value)) {
                return this.setState({
                    searchSet: {
                        ...this.state.searchSet,
                        [name]: e.target.value.substring(0, e.target.value.length - 1)
                    }
                });
            }
        }

        if (name == "allotteeName") {
            return this.setState({
                searchSet: {
                    ...this.state.searchSet,
                    [name]: e.target.value,
                    allottee: ""
                }
            })
        }

        this.setState({
            searchSet: {
                ...this.state.searchSet,
                [name]: e.target.value
            }
        })
    }

    handleSelectChange(type, id, number, assetCategory, acknowledgementNumber, status) {
        switch (type) {
            case "renew":
                window.open("app/search-agreement/view-renew-agreement.html?view=renew&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes")
                break;
            case "collTax":
                $.ajax({
                    url: "/lams-services/payment/_create?tenantId=" + tenantId + "&" + (acknowledgementNumber ? "acknowledgementNumber=" + acknowledgementNumber : "agreementNumber=" + number),
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        RequestInfo: requestInfo
                    }),
                    success: function (data) {
                        jQuery('<form>.').attr({
                            method: 'post',
                            action: '/collection/receipts/receipt-newform.action',
                            target: '_self'
                        }).append(jQuery('<input>').attr({
                            type: 'hidden',
                            id: 'collectXML',
                            name: 'collectXML',
                            value: data
                        })).appendTo(document.body).submit();
                    },
                    error: function (data) {
                        console.log(data);
                        showError(data.responseJSON.error ? data.responseJSON.error.message : data.responseJSON.message);
                    }
                });
                break;
            case "view":
                window.open("app/search-agreement/view-agreement-details.html?&" + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + (status ? "&status=" + status : "") + "&assetId=" + id, "fs", "fullscreen=yes");
                break;
            case "cancel":
                window.open("app/search-agreement/view-renew-agreement.html?view=cancel&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes");
                break;
            case "eviction":
                window.open("app/search-agreement/view-renew-agreement.html?view=eviction&type=" + assetCategory + (number ? "&agreementNumber=" + number : "&acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes");
                break;
            case "addeditdemand":
                window.open("app/dataentry/edit-demand.html?" + (number ? "agreementNumber=" + number : "acknowledgementNumber=" + acknowledgementNumber) + "&assetId=" + id, "fs", "fullscreen=yes");
                break;
        }
    }


    close() {
        // widow.close();
        open(location, '_self').close();
    }



    render() {
        //console.log(this.state.searchSet);
        let { handleChange, search, updateTable, handleSelectChange, handleMobileValidation } = this;
        var self = this;
        let { isSearchClicked, agreements, assetCategories, hideCollectTaxOption } = this.state;
        let { locality,
            agreementNumber,
            shopNumber,
            shoppingComplexName,
            assetCategory,
            mobileNumber,
            allotteeName,
            revenueWard,
            electionWard,
            code,
            oldAgreementNumber,
            tenderNumber, fromDate, toDate, shopComplexNumber, acknowledgementNumber, referenceNumber } = this.state.searchSet;
        const showCollectTaxOption = function () {
            if (!hideCollectTaxOption) {
                return (<option value="collTax">Collect Tax</option>);
            }
        }

        const getValueByName = function (name, id) {
            for (var i = 0; i < assetCategories.length; i++) {
                if (assetCategories[i].id == id) {
                    return assetCategories[i][name];
                }
            }
        }

        const renderOption = function (list) {
            if (list) {
                return list.map((item) => {
                    return (<option key={item.id} value={item.id}>
                        {item.name}
                    </option>)
                })
            }
        }
        const showTable = function () {
            if (isSearchClicked) {
                return (
                    <table id="agreementTable" className="table table-bordered">
                        <thead>
                            <tr>
                                <th>Sl No </th>
                                <th>Agreement Number </th>
                                <th>Allottee Name </th>
                                <th>Allottee contact No </th>
                                <th>Locality </th>
                                <th>Asset Category </th>
                                <th>Asset Code </th>
                                <th>Agreement Created Date </th>
                                <th>Agreement Type</th>
                                <th>Status </th>
                                <th>Type</th>
                                <th>Action </th>
                            </tr>
                        </thead>

                        <tbody id="agreementSearchResultTableBody">
                            {
                                renderBody()
                            }
                        </tbody>

                    </table>

                )


            }

        }
        const renderBody = function () {
            if (agreements.length > 0) {

                return agreements.map((item, index) => {
                    var category_name = getValueByName("name", item.asset.assetCategory.id);
                    return (<tr key={index}>
                        <td>{index + 1}</td>
                        <td>{item.agreementNumber} </td>
                        <td>{item.allottee.name}</td>
                        <td>{item.allottee.mobileNumber}</td>
                        <td>{item.asset.locationDetails && item.asset.locationDetails.locality ? getNameById(self.state.locality, item.asset.locationDetails.locality) : ""}</td>
                        <td>{item.asset.assetCategory.id ? category_name : "-"}</td>
                        <td>{item.asset.code}</td>
                        <td>{item.createdDate}</td>
                        <td>{item.action == "CREATE" ? "NEW" : item.action}  </td>
                        <td>{item.status}  </td>
                        <td>{item.source == "DATA_ENTRY" ? "Data Entry" : "System"}</td>
                        <td>
                            <div className="styled-select">
                                {getOption((category_name == "Land" || category_name == "shop"), item)};
                                      </div>
                        </td>
                    </tr>
                    );

                })
            }

        }

        /*const showCancelNEvict = function(status) {
          if(status == "APPROVED") {
            var values = [{label: "Cancel Agreement", value: "cancel"}, {label: "Evict Agreement", value: "eviction"}];
            return values.map(function(val, ind) {
              return (
                <option value={val.value}>{val.label}</option>
              )
            })
          }  else {
            return;
          }
        }*/

        const getDemandListing = function (agreement) {
            if (agreement.source === "DATA_ENTRY" && agreement.status === "ACTIVE") {
                return (<option value="addeditdemand">Add / Edit Demand </option>);
            }
        }

        const getOption = function (isShopOrLand, item) {
            if (isShopOrLand) {
                return (
                    <select id="myOptions" onChange={(e) => {
                        handleSelectChange(e.target.value, item.asset.id, item.agreementNumber, getValueByName("name", item.asset.assetCategory.id), item.acknowledgementNumber, item.status)
                    }}>
                        <option value="">Select Action</option>
                        <option value="view">View</option>
                        {/*<option value="renew">Renew</option>*/}
                        {showCollectTaxOption()}
                        {getDemandListing(item)}
                    </select>
                )
            } else {
                return (
                    <select id="myOptions" onChange={(e) => {
                        handleSelectChange(e.target.value, item.asset.id, item.agreementNumber, getValueByName("name", item.asset.assetCategory.id), item.acknowledgementNumber, item.status)
                    }}>
                        <option value="">Select Action</option>
                        <option value="view">View</option>
                        {showCollectTaxOption()}
                        {getDemandListing(item)}

                    </select>
                )

            }
        }
        const disbaled = function (type) {
            if (type === "view") {
                return "ture";
            } else {
                return "false";
            }
        }
        return (
            <div>
                <div className="form-section">
                    <h3>Search Agreement </h3>
                    <div className="form-section-inner">
                        <form onSubmit={(e) => { search(e) }}>
                            <div className="">
                                <div className="form-section">
                                    <div className="row">
                                        <div className="col-sm-3 col-sm-offset-5">
                                            <label for="asset_category">Asset category<span> *</span></label>
                                            <div className="styled-select">
                                                <select id="asset_category" name="asset_category" required="true" value={assetCategory} onChange={(e) => {
                                                    handleChange(e, "assetCategory")
                                                }}>
                                                    <option value="">Select Asset Category</option>
                                                    {renderOption(this.state.assetCategories)}
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="form-section">
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label for="agreement_number">Agreement Number </label>
                                            </div>
                                            <div className="col-sm-6">
                                                <input type="text" name="agreement_number" id="agreement_number" value={agreementNumber} onChange={(e) => {
                                                    handleChange(e, "agreementNumber")
                                                }} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label for="referenceNumber">Shop Number </label>
                                            </div>
                                            <div className="col-sm-6">
                                                <input type="text" name="referenceNumber" id="referenceNumber" value={referenceNumber} onChange={(e) => {
                                                    handleChange(e, "referenceNumber")
                                                }} />
                                            </div>
                                        </div>
                                    </div>

                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label for="oldAgreementNumber">Old Agreement Number </label>
                                            </div>
                                            <div className="col-sm-6">
                                                <input type="text" name="oldAgreementNumber" id="oldAgreementNumber" value={oldAgreementNumber} onChange={(e) => {
                                                    handleChange(e, "oldAgreementNumber")
                                                }} />
                                            </div>
                                        </div>
                                    </div>

                                    <button type="button" className="btn btn-default btn-action pull-right" style={{ marginRight: "2%" }} data-toggle="collapse" data-target="#demo"><span className="glyphicon glyphicon-plus"></span></button>
                                </div>
                                <div id="demo" className="collapse">
                                    <div className="row">
                                        <br />
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="contact_no">Allottee contact </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="text-no-ui">
                                                        <span>+91</span>
                                                        <input type="text" id="contact_no" onInput={(e) => { handleMobileValidation(e) }} onInvalid={(e) => { handleMobileValidation(e) }} name="contact_no" value={mobileNumber} maxLength="10" onChange={(e) => {
                                                            handleChange(e, "mobileNumber")
                                                        }} />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="name">Allottee Name </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <input type="text" id="name" name="name" value={allotteeName} onChange={(e) => {
                                                        handleChange(e, "allotteeName")
                                                    }} />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="locality">Locality</label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="styled-select">
                                                        <select id="locality" name="locality" value={locality} onChange={(e) => {
                                                            handleChange(e, "locality")
                                                        }}>
                                                            <option value="">Choose locality</option>
                                                            {renderOption(this.state.locality)}

                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="ward">Revenue Ward no</label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="styled-select">
                                                        <select id="ward" name="ward" value={revenueWard} onChange={(e) => {
                                                            handleChange(e, "revenueWard")
                                                        }}>
                                                            <option value="">Choose Revenue Wards</option>
                                                            {renderOption(this.state.revenueWards)}

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
                                                    <label for="electionWard">Election Ward no </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="styled-select">
                                                        <select id="electionWard" name="electionWard" value={electionWard} onChange={(e) => {
                                                            handleChange(e, "electionWard")
                                                        }}>
                                                            <option value="">Choose Election Wards</option>
                                                            {renderOption(this.state.electionwards)}
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="code">Asset Code </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="search-ui">
                                                        <input type="text" name="code" id="code" pattern="[A-Za-z0-9]*" value={code} onChange={(e) => {
                                                            handleChange(e, "assetCode")
                                                        }} />

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="fromDate">Agreement Created from </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="text-no-ui">
                                                        <span className="glyphicon glyphicon-calendar"></span>
                                                        <input className="date-picker" type="text" name="fromDate" id="fromDate" value={fromDate} onChange={(e) => {
                                                            handleChange(e, "fromDate")
                                                        }} />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="toDate">Agreement Created To </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <div className="text-no-ui">
                                                        <span className="glyphicon glyphicon-calendar"></span>
                                                        <input className="date-picker" type="text" name="toDate" id="toDate" value={toDate} onChange={(e) => {
                                                            handleChange(e, "toDate")
                                                        }} />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div className="row">
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="">Acknowledgement Number </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <input type="text" name="acknowledgementNumber" id="acknowledgementNumber" value={acknowledgementNumber} onChange={(e) => {
                                                        handleChange(e, "acknowledgementNumber")
                                                    }} />
                                                </div>
                                            </div>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="row">
                                                <div className="col-sm-6 label-text">
                                                    <label for="shoppingComplexName">Shopping Complex Name </label>
                                                </div>
                                                <div className="col-sm-6">
                                                    <input type="text" name="shoppingComplexName" id="shoppingComplexName" value={shoppingComplexName} onChange={(e) => {
                                                        handleChange(e, "shoppingComplexName")
                                                    }} />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center">
                                <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                      <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                            </div>
                        </form>
                    </div>
                </div>



                <div className="table-cont" id="table">
                    {showTable()}

                </div>

            </div>
        );
    }
}






ReactDOM.render(
    <AgreementSearch />,
    document.getElementById('root')
);


// <div className="col-sm-6" id="shopping_complex_number">
//     <div className="row">
//         <div className="col-sm-6 label-text">
//             <label for="shopping_complex_no">Shopping Complex Number </label>
//         </div>
//         <div className="col-sm-6">
//             <input  type="text" name="shopping_complex_no" id="shopping_complex_no" value={shopComplexNumber} onChange={(e)=>{
//     handleChange(e,"shopComplexNumber")
// }}/>
//         </div>
//     </div>
// </div>
