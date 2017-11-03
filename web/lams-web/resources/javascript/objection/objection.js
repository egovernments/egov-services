class ObjectionAgreement extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      asset:{
          id:"",
          name:"",
          code:""
      },
      locationDetails : {},
      assetCategory : {},
      allottee : {},
      objection : {},
      agreement:
        {
            "id": "",
            "tenantId": "",
            "agreementNumber": "",
            "acknowledgementNumber": "",
            "stateId": "",
            "action": "",
            "agreementDate": "",
            "timePeriod": "",
            "allottee": {
                "id": "",
                "name": "",
                "permanentAddress": "",
                "mobileNumber": "",
                "aadhaarNumber": "",
                "pan": "",
                "emailId": "",
                "userName": "",
                "password": "",
                "active": "",
                "type": "",
                "gender": "",
                "tenantId": "",
                    },
            "asset": {
                "id": "",
                "assetCategory": {
                    "id": "",
                    "name": "",
                    "code": ""
                },
                "name": "",
                "code": "",
                "locationDetails": {
                    "locality": "",
                    "zone": "",
                    "revenueWard": "",
                    "block": "",
                    "street": "",
                    "electionWard": "",
                    "doorNo": "",
                    "pinCode": ""
                }
            },
            "tenderNumber": "",
            "tenderDate": "",
            "councilNumber": "",
            "councilDate": "",
            "bankGuaranteeAmount": "",
            "bankGuaranteeDate": "",
            "securityDeposit": "",
            "collectedSecurityDeposit": "",
            "securityDepositDate": "",
            "status": "",
            "natureOfAllotment": "",
            "registrationFee": "",
            "caseNo": "",
            "commencementDate": "",
            "expiryDate": "",
            "orderDetails": "",
            "rent": "",
            "tradelicenseNumber": "",
            "paymentCycle": "",
            "rentIncrementMethod": {
                "id": "",
                "type": null,
                "assetCategory": null,
                "fromDate": null,
                "toDate": null,
                "percentage": "",
                "flatAmount": null,
                "tenantId": null
            },
            "orderNumber": null,
            "orderDate": null,
            "rrReadingNo": null,
            "remarks": "",
            "solvencyCertificateNo": "",
            "solvencyCertificateDate": "",
            "tinNumber": null,
            "documents": null,
            "demands": [
                ""
            ],
            "workflowDetails": {"department":null,"designation":null,"assignee":"","action":null,"status":null,"initiatorPosition":"","comments":""},
            "goodWillAmount": "",
            "collectedGoodWillAmount": "",
            "source": "",
            "legacyDemands": "",
            "cancellation": "",
            "renewal": null,
            "eviction": null,
            "objection": {
            	"courtCaseNo":"",
                "courtCaseDate":"",
                "courtFixedRent":"",
                "effectiveDate":""
            },
            "judgement": null,
            "remission": null,
            "createdDate": "",
            "createdBy": "",
            "lastmodifiedDate": "",
            "lastmodifiedBy": "",
            "isAdvancePaid": "",
            "adjustmentStartDate": null
        }

  }
     //this.handleChange=this.handleChange.bind(this);
     this.componentDidMount=this.componentDidMount.bind(this);
     //this.componentWillUpdate = this.componentWillUpdate.bind(this);
      this.setInitialState = this.setInitialState.bind(this);
  }
  setInitialState(initState) {
    this.setState(initState);
  }
  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#objection-title').text("Objection On Agreement");
    $('.datepicker').datepicker({
          format: 'dd/mm/yyyy',
          endDate: new Date(),
          maxDate: new Date(),
          autoclose:true

    });
    var action = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    var count = 5,_state={}, _this = this;
    _this.setInitialState(_state);

    let asset = {...this.state.asset};
    asset = commonApiPost("asset-services", "assets", "_search", { id: getUrlVars()["assetId"], tenantId }).responseJSON["Assets"][0] || {};
    console.log(asset);
    console.log(asset.id);
    var locationDetails = asset.locationDetails;
    var assetCategory = asset.assetCategory;
    var allottee = "";
    var agreement = "";
    console.log(locationDetails);
    _this.setState({
      asset,
      locationDetails,
      assetCategory,
      allottee,
      agreement
    })

  }

  render(){
    let {handleChange,search}=this;
    let {agreement}=this.state;
    let {allottee} = this.state;
    let {objection,legacyDemands,agreementNumber,natureOfAllotment,paymentCycle,rent,timePeriod,commencementDate,expiryDate} = this.state.agreement;
    let {asset,locationDetails,assetCategory} = this.state;

  const renderAssetDetails=function(){
       return(
       <div className="form-section" id="assetDetailsBlock">
           <h3>Asset Details </h3>
           <div className="form-section-inner">
                 <div className="row">
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="aName">Asset Name :</label>
                           </div>
                            {asset.name ? (<div className="col-sm-6 label-view-text">
                            <label id="code" name="code">
                            {asset.name}
                            </label>
                            </div>):(<div className="col-sm-6 label-view-text">
                            <label id="code" name="code">
                            N/A
                            </label>
                            </div>)}

                       </div>
                   </div>
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="code">Asset Code:</label>
                           </div>
                           {asset.code? (<div className="col-sm-6 label-view-text">
                               <label id="code" name="code">
                               {asset.code}
                               </label>
                           </div>):(<div className="col-sm-6 label-view-text">
                               <label id="code" name="code">
                               N/A
                               </label>
                           </div>)}

                       </div>
                   </div>
               </div>
               <div className="row">
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="categoryType">Asset Category Type :</label>
                           </div>
                           {assetCategory.name ? (
                             <div className="col-sm-6 label-view-text">
                                 <label id="assetCategoryType" name="assetCategoryType">
                                 {assetCategory.name}
                                 </label>
                             </div>
                           ): (<div className="col-sm-6 label-view-text">
                               <label id="assetCategoryType" name="assetCategoryType">
                               N/A
                               </label>
                           </div>)}

                       </div>
                   </div>
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="assetArea">Asset Area :</label>
                           </div>
                          {asset.totalArea ? (<div className="col-sm-6 label-view-text">
                              <label id="assetArea" name="assetArea" >
                              {asset.totalArea}?    </label>
                          </div>) : (<div className="col-sm-6 label-view-text">
                              <label id="assetArea" name="assetArea" >
                              N/A    </label>
                          </div>)}


                       </div>
                   </div>
               </div>
               <div className="row">
                     <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="locationDetails.locality">Locality :</label>
                           </div>
                          {locationDetails.locality ? (<div className="col-sm-6 label-view-text">
                              <label id="locationDetails.locality" name="locationDetails.locality">
                              {locationDetails.locality}
                              </label>
                          </div>): (<div className="col-sm-6 label-view-text">
                              <label id="locationDetails.locality" name="locationDetails.locality">
                              N/A
                              </label>
                          </div>)}

                       </div>
                   </div>
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="locationDetails.revenueWard">Revenue Ward :</label>
                           </div>
                           {locationDetails.revenueWard ? (<div className="col-sm-6 label-view-text">
                               <label id="locationDetails.revenueWard" name="locationDetails.revenueWard">
                               {locationDetails.revenueWard}
                               </label>
                           </div>) : (<div className="col-sm-6 label-view-text">
                               <label id="locationDetails.revenueWard" name="locationDetails.revenueWard">
                               N/A
                               </label>
                           </div>)}

                       </div>
                   </div>
               </div>
               <div className="row">
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="block">Block :</label>
                           </div>
                           {locationDetails.block ? (<div className="col-sm-6 label-view-text">
                               <label id="Block" name="Block">
                               {locationDetails.block}
                               </label>
                           </div>):(<div className="col-sm-6 label-view-text">
                               <label id="Block" name="Block">
                               N/A
                               </label>
                           </div>)}

                       </div>
                   </div>
                   <div className="col-sm-6">
                       <div className="row">
                           <div className="col-sm-6 label-text">
                               <label for="locationDetails.zone">Revenue Zone :</label>
                           </div>
                            {locationDetails.zone ? (<div className="col-sm-6 label-view-text">
                                <label id="locationDetails.zone" name="locationDetails.zone">
                                {locationDetails.zone}
                                </label>
                            </div>):(<div className="col-sm-6 label-view-text">
                                <label id="locationDetails.zone" name="locationDetails.zone">
                                N/A
                                </label>
                            </div>)}

                       </div>
                   </div>
               </div>
           </div>
       </div>);
     }
const renderAllottee = function(){
  return(
    <div className="form-section" id="allotteeDetailsBlock">
        <h3>Allottee Details </h3>
        <div className="form-section-inner">
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="allotteeName"> Name :</label>
                        </div>
                         {allottee.name ? (<div className="col-sm-6 label-view-text">
                         <label id="allotteeName" name="allotteeName">
                         {allottee.name}
                         </label>
                         </div>):(<div className="col-sm-6 label-view-text">
                         <label id="allotteeName" name="allotteeName">
                         N/A
                         </label>
                         </div>)}

                    </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="mobileNumber">Mobile Number:</label>
                        </div>
                        {allottee.mobileNumber? (<div className="col-sm-6 label-view-text">
                            <label id="mobileNumber" name="mobileNumber">
                            {allottee.mobileNumber}
                            </label>
                        </div>):(<div className="col-sm-6 label-view-text">
                            <label id="mobileNumber" name="mobileNumber">
                            N/A
                            </label>
                        </div>)}

                    </div>
                </div>
            </div>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="aadhaarNumber">AadhaarNumber :</label>
                        </div>
                        {allottee.email ? (
                          <div className="col-sm-6 label-view-text">
                              <label id="aadhaarNumber" name="aadhaarNumber">
                              {allottee.aadhaarNumber}
                              </label>
                          </div>
                        ): (<div className="col-sm-6 label-view-text">
                            <label id="aadhaarNumber" name="aadhaarNumber">
                            N/A
                            </label>
                        </div>)}

                    </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="panNo">PAN No:</label>
                        </div>
                       {allottee.panNo ? (<div className="col-sm-6 label-view-text">
                           <label id="panNo" name="panNo" >
                           {allottee.panNo}?    </label>
                       </div>) : (<div className="col-sm-6 label-view-text">
                           <label id="panNo" name="panNo" >
                           N/A    </label>
                       </div>)}


                    </div>
                </div>
            </div>
            <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="emailId">EmailId :</label>
                        </div>
                       {allottee.emailId ? (<div className="col-sm-6 label-view-text">
                           <label id="emailId" name="emailId">
                           {allottee.emailId}
                           </label>
                       </div>): (<div className="col-sm-6 label-view-text">
                           <label id="emailId" name="emailId">
                           N/A
                           </label>
                       </div>)}

                    </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="address">Address :</label>
                        </div>
                        {allottee.address ? (<div className="col-sm-6 label-view-text">
                            <label id="address" name="address">
                            {allottee.address}
                            </label>
                        </div>) : (<div className="col-sm-6 label-view-text">
                            <label id="address" name="address">
                            N/A
                            </label>
                        </div>)}

                    </div>
                </div>
            </div>

        </div>
    </div>
  );
}

 const renderAgreementDetails = function(){

   return(
     <div className="form-section" id="agreementDetailsBlock">
         <h3>Agreement Details </h3>
         <div className="form-section-inner">
               <div className="row">
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="agreementNumber"> Agreement Number :</label>
                         </div>
                          {agreement.agreementNumber ? (<div className="col-sm-6 label-view-text">
                          <label id="agreementNumber" name="agreementNumber">
                          {agreement.agreementNumber}
                          </label>
                          </div>):(<div className="col-sm-6 label-view-text">
                          <label id="agreementNumber" name="agreementNumber">
                          N/A
                          </label>
                          </div>)}

                     </div>
                 </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="agreementDate">Agreement Date:</label>
                         </div>
                         {agreement.agreementDate? (<div className="col-sm-6 label-view-text">
                             <label id="agreementDate" name="agreementDate">
                             {agreement.agreementDate}
                             </label>
                         </div>):(<div className="col-sm-6 label-view-text">
                             <label id="agreementDate" name="agreementDate">
                             N/A
                             </label>
                         </div>)}

                     </div>
                 </div>
             </div>
             <div className="row">
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="rent">Rent :</label>
                         </div>
                         {agreement.rent ? (
                           <div className="col-sm-6 label-view-text">
                               <label id="rent" name="rent">
                               {agreement.rent}
                               </label>
                           </div>
                         ): (<div className="col-sm-6 label-view-text">
                             <label id="rent" name="rent">
                             N/A
                             </label>
                         </div>)}

                     </div>
                 </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="securityDeposit">Advace Collection:</label>
                         </div>
                        {agreement.securityDeposit ? (<div className="col-sm-6 label-view-text">
                            <label id="securityDeposit" name="securityDeposit">
                            {agreement.securityDeposit}?    </label>
                        </div>) : (<div className="col-sm-6 label-view-text">
                            <label id="securityDeposit" name="securityDeposit">
                            N/A    </label>
                        </div>)}


                     </div>
                 </div>
             </div>
             <div className="row">
                   <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="paymentCycle">PaymentCycle :</label>
                         </div>
                        {agreement.paymentCycle ? (<div className="col-sm-6 label-view-text">
                            <label id="paymentCycle" name="paymentCycle">
                            {agreement.paymentCycle}
                            </label>
                        </div>): (<div className="col-sm-6 label-view-text">
                            <label id="paymentCycle" name="paymentCycle">
                            N/A
                            </label>
                        </div>)}

                     </div>
                 </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="natureOfAllotment">Allotment Type :</label>
                         </div>
                         {agreement.natureOfAllotment ? (<div className="col-sm-6 label-view-text">
                             <label id="natureOfAllotment" name="natureOfAllotment">
                             {agreement.natureOfAllotment}
                             </label>
                         </div>) : (<div className="col-sm-6 label-view-text">
                             <label id="natureOfAllotment" name="natureOfAllotment">
                             N/A
                             </label>
                         </div>)}

                     </div>
                 </div>
             </div>

         </div>
     </div>
   );

 }
 const renederObjectionDetails = function(){
   return(
     <div className="form-section hide-sec" id="agreementObjectionDetails">
         <h3 className="categoryType">Objection Details </h3>
         <div className="form-section-inner">

             <div className="row">
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="courtCaseNo">Court CaseNo
                              <span>*</span>
                             </label>
                         </div>
                         <div className="col-sm-6">
                             <input type="text" name="courtCaseNo" id="courtCaseNo" />
                         </div>
                     </div>
                 </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="courtCaseDate">CourtCase Date
                              <span>*</span>
                             </label>
                         </div>
                         <div className="col-sm-6">
                           <div className="text-no-ui">
                               <span className="glyphicon glyphicon-calendar"></span>
                             <input type="text" className="datepicker" name="courtCaseDate" id="courtCaseDate"/>
                           </div>
                         </div>
                     </div>
                 </div>
             </div>

             <div className="row">
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">

                             <label for="courtFixedRent" className="categoryType">Court Fixed Rent
                              <span>*</span>
                              </label>
                         </div>
                         <div className="col-sm-6">
                           <div className="text-no-ui">
                               <span>₹</span>
                             <input type="number" min="0" name="courtFixedRent" id="courtFixedRent"/>
                           </div>
                         </div>
                     </div>
                 </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="effectiveDate">Effective Date
                              <span>*</span>
                             </label>
                         </div>
                         <div className="col-sm-6">
                           <div className="text-no-ui">
                               <span className="glyphicon glyphicon-calendar"></span>
                             <input type="text" className="datepicker" name="effectiveDate" id="effectiveDate"/>
                           </div>
                         </div>
                     </div>
                 </div>

             </div>
        <div className="row">

            </div>
             <div className="row">
               <div className="col-sm-6">
                   <div className="row">
                       <div className="col-sm-6 label-text">
                           <label>Attach Document </label>
                       </div>
                       <div className="col-sm-6">
                           <div className="styled-file">
                               <input type="file" multiple id="documents"/>
                           </div>
                       </div>
                   </div>
               </div>
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                             <label for="remarks">Remarks </label>
                         </div>
                         <div className="col-sm-6">
                             <textarea name="remarks" id="remarks"></textarea>
                         </div>
                     </div>
                 </div>
             </div>

         </div>
     </div>
   );
 }
    return(
      <div>
      <h3>Objection On Agreement </h3>
      <form  id="objectAgreementForm" name="objectAgreementForm" >
      <fieldset>
              {renderAssetDetails()}
              {renderAllottee()}
              {renderAgreementDetails()}
              {renederObjectionDetails()}
              
              <div className="form-section" id="workFlowDetails">
                  <h3 className="categoryType">Workflow Details </h3>
                  <div className="form-section-inner">
                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label for="approverDepartment">Approver Department</label>
                                  </div>
                                  <div className="col-sm-6">
                                          <select id="approverDepartment" className="selectStyle">

                                      </select>
                                  </div>
                              </div>
                          </div>
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label for="approverDesignation">Approver Designation</label>
                                  </div>
                                  <div className="col-sm-6">
                                          <select id="approverDesignation" className="selectStyle">

                                      </select>
                                  </div>
                              </div>
                          </div>
                        </div>

                        <div className="row">
                          <div className="col-sm-6">
                              <div className="row">
                                  <div className="col-sm-6 label-text">
                                      <label for="approverPositionId">Approver Name</label>
                                  </div>
                                  <div className="col-sm-6">
                                          <select id="approverPositionId" name="approverPositionId" className="selectStyle">
                                              <option value="">Select Approver Name</option>
                                          </select>
                                  </div>
                              </div>
                          </div>
                        </div>
                  </div>
              </div>
              <div className="text-center">
                <button type="button" className="btn btn-submit" id="close">Close</button>&nbsp;&nbsp;
                  <button type="button" id="onjectionAgreement" className="btn btn-submit">Submit</button>
              </div>


      </fieldset>
      </form>
      </div>
    );
  }

}
ReactDOM.render(
  <ObjectionAgreement />,
  document.getElementById('root')
);
