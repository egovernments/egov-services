var locality, electionwards, revenueWards, revenueZone, revenueBlock;
class AgreementDetails extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            reservationCategory:[],
            agreement: {
                id: "",
                tenantId: tenantId,
                agreementNumber: "",
                acknowledgementNumber: "",
                stateId: "",
                action: "",
                agreementDate: "",
                timePeriod: "",
                allottee: {
                    id: "",
                    name: "",
                    permanentAddress: "",
                    mobileNumber: "",
                    aadhaarNumber: "",
                    pan: "",
                    emailId: "",
                    userName: "",
                    password: "",
                    active: "",
                    type: "",
                    gender: "",
                    tenantId: tenantId,
                },
                asset: {
                    id: "",
                    assetCategory: {
                        id: "",
                        name: "",
                        code: ""
                    },
                    name: "",
                    code: "",
                    locationDetails: {
                        locality: "",
                        zone: "",
                        revenueWard: "",
                        block: "",
                        street: "",
                        electionWard: "",
                        doorNo: "",
                        pinCode: ""
                    }
                },
                tenderNumber: "",
                tenderDate: "",
                councilNumber: "",
                councilDate: "",
                bankGuaranteeAmount: "",
                bankGuaranteeDate: "",
                securityDeposit: "",
                collectedSecurityDeposit: "",
                securityDepositDate: "",
                status: "",
                natureOfAllotment: "",
                registrationFee: "",
                caseNo: "",
                commencementDate: "",
                expiryDate: "",
                orderDetails: "",
                rent: "",
                tradelicenseNumber: "",
                paymentCycle: "",
                rentIncrementMethod: {
                    id: "",
                    type: "",
                    assetCategory: "",
                    fromDate: "",
                    toDate: "",
                    percentage: "",
                    flatAmount: "",
                    tenantId: tenantId
                },
                orderNumber: "",
                orderDate: "",
                rrReadingNo: "",
                remarks: "",
                solvencyCertificateNo: "",
                solvencyCertificateDate: "",
                tinNumber: "",
                documents: {},
                demands: [],
                workflowDetails: {
                    department: "",
                    designation: "",
                    assignee: "",
                    action: "",
                    status: "",
                    initiatorPosition: "",
                    comments: ""
                },
                goodWillAmount: "",
                collectedGoodWillAmount: "",
                source: "",
                legacyDemands: "",
                cancellation: "",
                rdivenewal: "",
                eviction: "",
                objection: "",
                judgement: "",
                remission: "",
                createdDate: "",
                createdBy: "",
                lastmodifiedDate: "",
                lastmodifiedBy: "",
                isAdvancePaid: "",
                adjustmentStartDate: "",
                subSeqRenewals :{

                }
            }

        }
        this.viewDCB = this.viewDCB.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
    }

    setInitialState(initState) {
        this.setState(initState);
    }

    close() {
        open(location, '_self').close();
    }
    viewDCB(e) {
        let _this = this;
        var agreementNumber = _this.state.agreement.agreementNumber;
        var acknowledgementNumber = _this.state.agreement.acknowledgementNumber;
        var status = _this.state.agreement.status;
        e.preventDefault();
        window.open(`app/dcb/view-dcb.html?`+(agreementNumber ? "&agreementNumber=" + agreementNumber : "&acknowledgementNumber=" + acknowledgementNumber)+(status ? "&status="+status:"")+'&tenantId='+tenantId, "pop", "width=800, height=600, scrollbars=yes");

    }

    componentDidMount() {

        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }
        $('#lams-title').text("View Agreement ");

        try { locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality")); } catch (e) {
            console.log(e);
            locality = [];
        }
        try { electionwards = !localStorage.getItem("ward") || localStorage.getItem("ward") == "undefined" ? (localStorage.setItem("ward", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("ward"))) : JSON.parse(localStorage.getItem("ward")); } catch (e) {
            console.log(e);
            electionwards = [];
        }
        try { revenueWards = !localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined" ? (localStorage.setItem("revenueWard", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueWard"))) : JSON.parse(localStorage.getItem("revenueWard")); } catch (e) {
            console.log(e);
            revenueWards = [];
        }
        try { revenueZone = !localStorage.getItem("revenueZone") || localStorage.getItem("revenueZone") == "undefined" ? (localStorage.setItem("revenueZone", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "ZONE", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueZone"))) : JSON.parse(localStorage.getItem("revenueZone")); } catch (e) {
            console.log(e);
            revenueZone = [];
        }
        try { revenueBlock = !localStorage.getItem("revenueBlock") || localStorage.getItem("revenueBlock") == "undefined" ? (localStorage.setItem("revenueBlock", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueBlock"))) : JSON.parse(localStorage.getItem("revenueBlock")); } catch (e) {
            console.log(e);
            revenueBlock = [];
        }

        var requestParam;
        var paramValue;
        var agreementNumber = getUrlVars()["agreementNumber"];
        var acknowledgementNumber = getUrlVars()["acknowledgementNumber"];
        var status = getUrlVars()["status"];

        var promise1 = new Promise(function(resolve, reject) {
          var reservationCategory = commonApiPost("lams-services", "getreservations", "", {tenantId}).responseJSON;
          var agreement = commonApiPost("lams-services","agreements","_search", { agreementNumber,status,acknowledgementNumber,action:"View",tenantId}).responseJSON["Agreements"][0] || {};
          resolve({reservationCategory:reservationCategory,agreement:agreement});
        });

        promise1.then((response) => {
          this.setState({
              ...this.state,
              reservationCategory:response.reservationCategory,
              agreement: response.agreement,
              subSeqRenewals: response.agreement.subSeqRenewals,
              documents:response.agreement.documents
          });
        });
    }
    render() {
        var _this = this;
        let { viewDCB } = this;
        let { agreement, reservationCategory} = this.state;
        let { allottee, asset,subSeqRenewals,documents } = this.state.agreement;
        let { assetCategory, locationDetails } = this.state.agreement.asset;

        console.log(this.state);

        const renderOption = function (data) {
            if (data) {
                return data.map((item, ind) => {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? item.name : item}
                    </option>)
                })
            }
        }

        const getNamebyIdFromMaster = function(arr, id){
          let validObj = arr && arr.find((obj)=>{return obj.id == id});
          return validObj && validObj.name || 'N/A';
        }

        const renderAssetDetails = function () {
            return (
                <div className="form-section" id="assetDetailsBlock">
                    <h3>Asset Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="categoryType">Asset Category :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="assetCategoryType" name="assetCategoryType">
                                            { assetCategory.name ? assetCategory.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="aName">Asset Name :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="code" name="code">
                                            { asset.name ? asset.name :"N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="code">Asset Code:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="code" name="code">
                                            { asset.code ? asset.code :"N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="assetArea">Asset Area :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="assetArea" name="assetArea" >
                                            { "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="locationDetails.locality">Locality :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="locationDetails.locality" name="locationDetails.locality">
                                            {locationDetails.locality ? getNamebyIdFromMaster(locality, locationDetails.locality) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="locationDetails.street">Street :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="locationDetails.street" name="locationDetails.street">
                                            {locationDetails.street || "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="zone">Revenue Zone :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="zone" name="zone">
                                            {locationDetails.zone ? getNamebyIdFromMaster(revenueZone, locationDetails.zone) :"N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="revenueWard">Revenue Ward :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="revenueWard" name="revenueWard">
                                            {locationDetails.revenueWard? getNamebyIdFromMaster(revenueWards, locationDetails.revenueWard) :"N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="block">Revenue Block :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="Block" name="Block">
                                        {locationDetails.block? getNamebyIdFromMaster(revenueBlock, locationDetails.block) :"N/A"}
                                      </label>
                                  </div>

                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="electionWard">Election Ward :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="electionWard" name="electionWard">
                                          {locationDetails.electionWard ? getNamebyIdFromMaster(electionwards, locationDetails.electionWard) :"N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="surveyNumber">Survey Number :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="surveyNumber" name="surveyNumber">
                                        {agreement.surveyNumber || "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="marketValue">Market value :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="marketValue" name="marketValue">
                                          {agreement.marketValue || "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                    </div>
                </div>);
        }

        const renderAllottee = function () {
            return (
                <div className="form-section" id="allotteeDetailsBlock">
                    <h3>Allottee Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Name :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="allotteeName" name="allotteeName">
                                            {allottee.name ? allottee.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Mobile Number:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="mobileNumber" name="mobileNumber">
                                            {allottee.mobileNumber ? maskAlloteeDetails(allottee.mobileNumber) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="aadhaarNumber">AadhaarNumber :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="aadhaarNumber" name="aadhaarNumber">
                                            {allottee.aadhaarNumber ? maskAlloteeDetails(allottee.aadhaarNumber) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="panNo">PAN No:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="panNo" name="panNo" >
                                            {allottee.pan ? allottee.pan : "N/A"}   </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="emailId">EmailId :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="emailId" name="emailId">
                                            {allottee.emailId ? allottee.emailId : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="address">Address :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="address" name="address">
                                            {allottee.permanentAddress ? allottee.permanentAddress : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="address">Trade license number :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="tradelicenseNumber" name="tradelicenseNumber">
                                          {agreement.tradelicenseNumber ? agreement.tradelicenseNumber : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="tinNumber">TIN :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="tinNumber" name="tinNumber">
                                          {agreement.tinNumber ? agreement.tinNumber : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renderMinimalAllottee = function () {
            return (
                <div className="form-section" id="allotteeDetailsBlock">
                    <h3>Allottee Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Name :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="allotteeName" name="allotteeName">
                                            {allottee.name ? allottee.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Mobile Number:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="mobileNumber" name="mobileNumber">
                                            {allottee.mobileNumber ? allottee.mobileNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="aadhaarNumber">AadhaarNumber :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="aadhaarNumber" name="aadhaarNumber">
                                            {allottee.aadhaarNumber ? allottee.aadhaarNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="panNo">PAN No:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="panNo" name="panNo" >
                                            {allottee.pan ? allottee.pan : "N/A"}   </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="emailId">EmailId :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="emailId" name="emailId">
                                            {allottee.emailId ? allottee.emailId : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="status">GSTIN :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="status" name="status">
                                            {agreement.gstin ? agreement.gstin : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="address">Address :</label>
                                </div>
                                <div className="col-sm-6 label-view-text">
                                    <label id="address" name="address">
                                        {allottee.permanentAddress ? allottee.permanentAddress : "N/A"}
                                    </label>
                                </div>
                            </div>
                        </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renderAgreementDetails = function () {
            return (
                <div className="form-section" id="agreementDetailsBlock">
                    <h3>Agreement Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="agreementNumber"> Agreement Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="agreementNumber" name="agreementNumber">
                                            {agreement.agreementNumber ? agreement.agreementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="acknowledgementNumber">Acknowledgement Number:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="acknowledgementNumber" name="acknowledgementNumber">
                                            {agreement.acknowledgementNumber ? agreement.acknowledgementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="oldAgreementNumber"> Old Agreement Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="oldAgreementNumber" name="oldAgreementNumber">
                                            {agreement.oldAgreementNumber ? agreement.oldAgreementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="referenceNumber">Shop No./Reference No:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="referenceNumber" name="referenceNumber">
                                            {agreement.referenceNumber ? agreement.referenceNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="councilNumber"> Council/standing committee Resolution Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="councilNumber" name="councilNumber">
                                            {agreement.councilNumber ? agreement.councilNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="councilDate">Council/standing committee Resolution date :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="councilDate" name="councilDate">
                                            {agreement.councilDate ? agreement.councilDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="tenderNumber"> Tender/Auction No. :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="tenderNumber" name="tenderNumber">
                                            {agreement.tenderNumber ? agreement.tenderNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="tenderDate">Tender/Auction Date :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="tenderDate" name="tenderDate">
                                            {agreement.tenderDate ? agreement.tenderDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="reservationCategory">Reservation Category :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="reservationCategory" name="reservationCategory">
                                            {reservationCategory && agreement.reservationCategory ? getNamebyIdFromMaster(reservationCategory,agreement.reservationCategory) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="status">Basis of Allotment :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="status" name="status">
                                            {agreement.basisOfAllotment ? agreement.basisOfAllotment : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="natureOfAllotment">Nature of allotment :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="natureOfAllotment" name="natureOfAllotment">
                                            {agreement.natureOfAllotment ? agreement.natureOfAllotment : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="caseNo">Court Case Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="caseNo" name="caseNo">
                                            {agreement.caseNo ? agreement.caseNo : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="orderDetails">Order details :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="orderDetails" name="orderDetails">
                                          {agreement.orderDetails ? agreement.orderDetails : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="registrationFee">Registration fee paid :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="registrationFee" name="status">
                                          &#8377; {String(agreement.registrationFee) || "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="rent">Rent :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="rent" name="rent">
                                          &#8377; {String(agreement.rent) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="paymentCycle">Payment Cycle :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="paymentCycle" name="paymentCycle">
                                          {agreement.paymentCycle ? agreement.paymentCycle : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="cgst">CGST :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="cgst" name="CGST">
                                            &#8377; {String(Math.round(agreement.cgst)) || 0}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="sgst">SGST :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="sgst" name="sgst">
                                            &#8377; {String(Math.round(agreement.sgst)) || 0}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="serviceTax">ServiceTax :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="serviceTax" name="serviceTax">
                                            &#8377; {String(agreement.serviceTax) || 0}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="status">GSTIN :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="status" name="status">
                                            {agreement.gstin ? agreement.gstin : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="bankGuaranteeAmount">Bank Guarantee Amount :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="bankGuaranteeAmount" name="bankGuaranteeAmount">
                                          &#8377; {String(agreement.bankGuaranteeAmount) || 0}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="bankGuaranteeDate">Bank Guarantee Date :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="bankGuaranteeDate" name="bankGuaranteeDate">
                                          {agreement.bankGuaranteeDate ? agreement.bankGuaranteeDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="solvencyCertificateNo">Solvency Certificate No :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="solvencyCertificateNo" name="solvencyCertificateNo">
                                          {agreement.solvencyCertificateNo ? agreement.solvencyCertificateNo : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="solvencyCertificateDate">Solvency Certificate Date :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="solvencyCertificateDate" name="solvencyCertificateDate">
                                          {agreement.solvencyCertificateDate ? agreement.solvencyCertificateDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="securityDeposit">Security Deposit :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="securityDeposit" name="securityDeposit">
                                          &#8377; {String(agreement.securityDeposit) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="collectedSecurityDeposit">Collected Security Deposit :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="collectedSecurityDeposit" name="collectedSecurityDeposit">
                                          &#8377; {String(agreement.collectedSecurityDeposit) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="securityDepositDate"> Security Deposit Received Date  :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="securityDepositDate" name="securityDepositDate">
                                            {agreement.securityDepositDate ? agreement.securityDepositDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="remarks">Remarks :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="remarks" name="remarks">
                                                {agreement.remarks || 'N/A'}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="goodWillAmount">Goodwill Amount :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="goodWillAmount" name="goodWillAmount">
                                            &#8377; {String(agreement.goodWillAmount) || 'N/A'}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="collectedGoodWillAmount">Collected Goodwill Amount :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="collectedGoodWillAmount" name="collectedGoodWillAmount">
                                            &#8377; {String(agreement.collectedGoodWillAmount) || 'N/A'}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="timePeriod">Time Period :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="timePeriod" name="timePeriod">
                                          {agreement.timePeriod ? agreement.timePeriod : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="status">Current Status :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="status" name="status">
                                            {agreement.status ? agreement.status : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="rentIncrementMethod">Method for Renewal of Rent :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="rentIncrementMethod" name="rentIncrementMethod">
                                            {agreement.rentIncrementMethod.percentage} %
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="floorNo">Floor Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="floorNo" name="floorNo">
                                            {agreement.floorNumber || 'N/A'}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="commencementDate"> Date of Allotment :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="commencementDate" name="commencementDate">
                                            {agreement.commencementDate ? agreement.commencementDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="expiryDate">Expiry Date:</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="expiryDate" name="expiryDate">
                                          {agreement.expiryDate ? agreement.expiryDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                    </div>
                </div>
            );

        }

        const renderMinimalAgreementDetails = function () {
            return (
                <div className="form-section" id="agreementDetailsBlock">
                    <h3>Agreement Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="agreementNumber"> Agreement Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="agreementNumber" name="agreementNumber">
                                            {agreement.agreementNumber ? agreement.agreementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="oldAgreementNumber"> Old Agreement Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="oldAgreementNumber" name="oldAgreementNumber">
                                            {agreement.oldAgreementNumber ? agreement.oldAgreementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="councilNumber"> Council/standing committee Resolution Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="councilNumber" name="councilNumber">
                                            {agreement.councilNumber ? agreement.councilNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="councilDate">Council/standing committee Resolution date :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="councilDate" name="councilDate">
                                            {agreement.councilDate ? agreement.councilDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="tenderNumber"> Tender/Auction No. :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="tenderNumber" name="tenderNumber">
                                            {agreement.tenderNumber ? agreement.tenderNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="tenderDate">Tender/Auction Date :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="tenderDate" name="tenderDate">
                                            {agreement.tenderDate ? agreement.tenderDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="tenderOpeningDate">Tender Opening Date/Auction Date :</label>
                                </div>
                                <div className="col-sm-6 label-view-text">
                                    <label id="tenderOpeningDate" name="tenderOpeningDate">
                                        {agreement.tenderOpeningDate ? agreement.tenderOpeningDate : "N/A"}
                                    </label>
                                </div>
                            </div>
                        </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="natureOfAllotment">Nature of allotment :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="natureOfAllotment" name="natureOfAllotment">
                                            {agreement.natureOfAllotment ? agreement.natureOfAllotment : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="auctionAmount">Auction Amount :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="auctionAmount" name="auctionAmount">
                                          &#8377; {String(agreement.auctionAmount) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="paymentCycle">Payment Cycle :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="paymentCycle" name="paymentCycle">
                                          {agreement.paymentCycle ? agreement.paymentCycle : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="bankGuaranteeAmount">Bank Guarantee Amount :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="bankGuaranteeAmount" name="bankGuaranteeAmount">
                                          &#8377; {String(agreement.bankGuaranteeAmount) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="bankGuaranteeDate">Bank Guarantee Date :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="bankGuaranteeDate" name="bankGuaranteeDate">
                                          {agreement.bankGuaranteeDate ? agreement.bankGuaranteeDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="solvencyCertificateNo">Solvency Certificate No :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="solvencyCertificateNo" name="solvencyCertificateNo">
                                          {agreement.solvencyCertificateNo ? agreement.solvencyCertificateNo : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="solvencyCertificateDate">Solvency Certificate Date :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="solvencyCertificateDate" name="solvencyCertificateDate">
                                          {agreement.solvencyCertificateDate ? agreement.solvencyCertificateDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="solvencyAmount">Solvency Amount :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="solvencyAmount" name="solvencyAmount">
                                          &#8377; {String(agreement.solvencyAmount) || 0}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="securityDepositDate"> Security Deposit Received Date  :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="securityDepositDate" name="securityDepositDate">
                                          {agreement.securityDepositDate ? agreement.securityDepositDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="securityDeposit">Security Deposit :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="securityDeposit" name="securityDeposit">
                                          &#8377; {String(agreement.securityDeposit) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="collectedSecurityDeposit">Collected Security Deposit :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="collectedSecurityDeposit" name="collectedSecurityDeposit">
                                          &#8377; {String(agreement.collectedSecurityDeposit) || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>
                        <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="timePeriod">Time Period :</label>
                                </div>
                                <div className="col-sm-6 label-view-text">
                                    <label id="timePeriod" name="timePeriod">
                                        {agreement.timePeriod ? agreement.timePeriod : "N/A"}
                                    </label>
                                </div>
                            </div>
                        </div>

                        </div>

                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="expiryDate">Expiry Date:</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="expiryDate" name="expiryDate">
                                          {agreement.expiryDate ? agreement.expiryDate : "N/A"}
                                      </label>
                                  </div>
                              </div>
                          </div>
                        </div>

                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label htmlFor="remarks">Remarks :</label>
                                  </div>
                                  <div className="col-sm-6 label-view-text">
                                      <label id="remarks" name="remarks">
                                          {agreement.remarks || 'N/A'}
                                      </label>
                                  </div>
                              </div>
                          </div>

                        </div>

                    </div>
                </div>
            );

        }

        const renderHistoryDetails=function(){
                return (
                    <div className="form-section" id="historyDetails">
                        <h3 className="categoryType">History Details </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="firstAllotment">Month/Year of First Allotment to the Current
                                                Lessee :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="status" name="status">
                                                {agreement.firstAllotment ? agreement.firstAllotment : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                )
        }

        const renderRemissionOfAgreementDetails = function () {
            if(agreement.remission != null) {
                return (
                    <div className="form-section" id="remissionDetailsBlock">
                        <h3>Remission Details </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Council/standing committee Resolution Number :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.councilNumber ? agreement.councilNumber : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="crDate"> Council/standing committee Resolution Date :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="crDate" name="crDate">
                                                    {agreement.councilDate ? agreement.councilDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="remFromDate"> Remission From Date :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="remFromDate" name="remFromDate">
                                                {agreement.remission.remissionFromDate ? agreement.remission.remissionFromDate : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="remToDate"> Remission To Date:</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="remToDate" name="remToDate">
                                                    {agreement.remission.remissionToDate ? agreement.remission.remissionToDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="reason"> Remission Reason :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="reason" name="reason">
                                                {agreement.remission.remissionReason ? agreement.remission.remissionReason : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="remRent"> Remission Rent :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="remRent" name="remRent">
                                                    {agreement.remission.remissionRent ? agreement.remission.remissionRent : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            }else{
                return null;
            }

        }

        const renderCancellationOfAgreementDetails = function () {
            if(agreement.cancellation != null){
                return (
                    <div className="form-section" id="cancellationDetailsBlock">
                        <h3>Cancellation Details </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Council/standing committee Resolution Number :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.cancellation.orderNo ? agreement.cancellation.orderNo : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="agreementNumber"> Council/standing committee Resolution Date :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="agreementNumber" name="agreementNumber">
                                                    {agreement.cancellation.orderDate ? agreement.cancellation.orderDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="date">Reason For Cancellation :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="date" name="date">
                                                {agreement.cancellation.reasonForCancellation ? agreement.cancellation.reasonForCancellation : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="agreementNumber"> Termination Date :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="agreementNumber" name="agreementNumber">
                                                    {agreement.cancellation.terminationDate ? agreement.cancellation.terminationDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                );
            }else{
                return null;
            }

        }

        const renderRenewalOfAgreementDetails= function () {
            if(agreement.renewal != null) {
                return (
                    <div className="form-section" id="renewalDetailsBlock">
                        <h3>Renewal Details </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Council/standing committee Resolution Number :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.renewal.renewalOrderNumber ? agreement.renewal.renewalOrderNumber : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="agreementNumber"> Council/standing committee Resolution Date :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="agreementNumber" name="agreementNumber">
                                                    {agreement.renewal.renewalOrderDate ? agreement.renewal.renewalOrderDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Renewal Reason :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.renewal.reasonForRenewal ? agreement.renewal.reasonForRenewal : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>

                );
            }else{
                return null;
            }
        }

        const renderEvictionOfAgreementDetails= function () {
            if(agreement.eviction != null) {
                return (
                    <div className="form-section" id="evictionDetailsBlock">
                        <h3>Eviction Details </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Council/standing committee Resolution Number :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.eviction.evictionProceedingNumber ? agreement.eviction.evictionProceedingNumber : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="agreementNumber"> Council/standing committee Resolution Date :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="agreementNumber" name="agreementNumber">
                                                    {agreement.eviction.evictionProceedingDate ? agreement.eviction.evictionProceedingDate : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="crNumber"> Eviction Reason :</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <label id="crNumber" name="crNumber">
                                                {agreement.eviction.reasonForEviction ? agreement.eviction.reasonForEviction : "N/A"}
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col-sm-6">
                                        <div className="row">
                                            <div className="col-sm-6 label-text">
                                                <label htmlFor="agreementNumber"> Court Reference Number :</label>
                                            </div>
                                            <div className="col-sm-6 label-view-text">
                                                <label id="agreementNumber" name="agreementNumber">
                                                    {agreement.eviction.courtReferenceNumber ? agreement.eviction.courtReferenceNumber : "N/A"}
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                );
            }else{
                return null;
            }
        }

        const renderSubSeqRenewals=function()
        {
             if(subSeqRenewals)
              return (
                <div className="form-section" id="subSeqRenewalsBlock">
                    <h3>Sub Sequent Renewal Details </h3>
                <table id="subSeqRenewalble" className="table table-bordered">
                    <thead>
                    <tr>
                        <th>Sr.no</th>
                        <th >Council/standing committee Resolution Number</th>
                        <th>Council/standing committee Resolution date</th>
                        <th >From Date</th>
                        <th>To Date</th>
                        <th>Years</th>
                        <th>Monthly Rent(Rs.)</th>

                    </tr>
                    </thead>
                    <tbody id="renewalDetailsTableBody">
                        {
                            renderBody()
                        }
                    </tbody>
                </table>
                </div>
              )

        }
        const renderBody=function()
        {
          if (subSeqRenewals.length>0) {

            return subSeqRenewals.map((item,index)=>
            {                   return (<tr key={index}>
                                      <td>{index+1}</td>
                                      <td>{item.resolutionNumber || 'N/A'}</td>
                                      <td>{item.resolutionDate || 'N/A'}</td>
                                      <td>{item.fromDate} </td>
                                      <td>{item.toDate}</td>
                                      <td>{item.years}</td>
                                      <td>{item.rent}</td>


                      </tr>
                  );

            })
          }

        }


        const renderDocuments=function()
        {
             if(documents)
              return (
                <div className="form-section" id="documentsBlock">
                    <h3>Attached Documents </h3>
                <table id="documentsTable" className="table table-bordered">
                    <thead>
                    <tr>
                        <th>S.No</th>
                        <th>Document Name</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody id="documentsTableBody">
                        {
                           renderDocumentsList()
                        }
                    </tbody>
                </table>
                </div>
              )

        }

        const renderDocumentsList=function()
        {
          if (documents && documents.length>0) {
             var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
            return documents.map((item,index)=>
            {                   return (<tr key={index}>
                                      <td>{index+1}.</td>
                                      <td>{item.fileName || 'N/A'}</td>
                                      <td>  <a href={window.location.origin + CONST_API_GET_FILE + item.fileStore} target="_self">
                                          Download
                                           </a>
                                      </td>
                                   </tr>
                  );

            })
          }else {
            return (<tr><td></td>
               <td>No Documents</td>
               <td></td>
              </tr>)
          }
        }
        const renederDetails = function(){
          if(agreement.showDetails){
          return (<div>
            {renderAssetDetails()}
            {renderAllottee()}
            {renderAgreementDetails()}
            {renderDocuments()}
            {renderHistoryDetails()}
            {renderSubSeqRenewals()}
            {renderRemissionOfAgreementDetails()}
            {renderCancellationOfAgreementDetails()}
            {renderRenewalOfAgreementDetails()}
            {renderEvictionOfAgreementDetails()}
          </div>)
          }else{
            return (<div>
              {renderAssetDetails()}
              {renderMinimalAllottee()}
              {renderMinimalAgreementDetails()}
              {renderDocuments()}
            </div>)
          }
        }
        
        if ( agreement.status == 'HISTORY') {

            return (<div>
                <h3>Agreement Details</h3>
                <form onSubmit={(e) => {
                    viewDCB(e)
                }}>
                    <fieldset>
                        {renederDetails()}
                        <br/>
                        <div className="text-center">
                            <button type="button" className="btn btn-close" onClick={(e) => {
                                this.close()
                            }}>Close
                            </button>
                        </div>

                    </fieldset>
                </form>
            </div>);
        }else{
            return (
                <div>
                    <h3>Agreement Details</h3>
                    <form onSubmit={(e) => {
                        viewDCB(e)
                    }}>
                        <fieldset>
                            {renederDetails()}
                            <br/>
                            <div className="text-center">
                                <button id="sub" type="submit" className="btn btn-submit">View DCB</button>
                                &nbsp;&nbsp;
                                <button type="button" className="btn btn-close" onClick={(e) => {
                                    this.close()
                                }}>Close
                                </button>
                                &nbsp;&nbsp;

                            </div>

                        </fieldset>
                    </form>
                </div>
            );
        }
    }

}
ReactDOM.render(
    <AgreementDetails />,
    document.getElementById('root')
);
