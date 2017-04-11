class CreateAgreement extends React.Component {

    render() {

    return (
                  <div>
                  <div className="form-section">
                  <form>
                      <h3 > Acknowledgement of Agreement </h3>
                    <h4> <center><font color="ass"> Acknowledgement  Created </font> </center></h4>
                    <h3> <center><font color="ass"><strong> {getUrlVars()["ackNo"]}</strong>  </font> </center></h3>
                    <h3><center> <font color="ass"> Successfully Forward to {getUrlVars()["name"]} </font></center></h3>


                    <div className="text-center">

                        <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
            </form>
              </div>

              </div>










    );
  }
}


ReactDOM.render(
  <CreateAgreement />,
  document.getElementById('root')
);
//
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="shoppingComplexNo">Shopping Complex No</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="shoppingComplexNo" name="shoppingComplexNo" value={shoppingComplexNo}
//         onChange={(e)=>{handleChange(e,"shoppingComplexNo")}}> 123456</label>
// </div>
// </div>
// </div>
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="shopNo">Shop No</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="shopNo" name="shopNo" value={shopNo}
//         onChange={(e)=>{handleChange(e,"shopNo")}}> NZ1SC007</label>
// </div>
// </div>
// </div>
// </div>
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="address">Shop Address</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="address" name="address" value={address}
//         onChange={(e)=>{handleChange(e,"address")}}>Bangalore </label>
// </div>
// </div>
// </div>
//
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="name">Name of the Allotee/Lessee</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="alloteeName" name="alloteeName" value={alloteeName}
//         onChange={(e)=>{handleChange(e,"alloteeName")}}>Murli M </label>
// </div>
// </div>
// </div>
// </div>
//
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="alloteeAddress"> Address of the allotee/Lessee</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="alloteeAddress" name="alloteeAddress" value={alloteeAddress}
//         onChange={(e)=>{handleChange(e,"alloteeAddress")}}> Bnagalore </label>
// </div>
// </div>
// </div>
//
// <div className="col-sm-6">
//     <div className="row">
//         <div className="col-sm-6 label-text">
//             <label for="mobileNumber">Contact No  +91 </label>
//         </div>
//         <div className="col-sm-6">
//
//                 <label for=""  name="mobileNumber" id="mobileNumber"  value={mobileNumber}
//                 onChange={(e)=>{handleChange(e,"mobileNumber")}}> 9831350363</label>
//         </div>
//     </div>
// </div>
// </div>
//
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//             <label for="emailid">Email Id  </label>
// </div>
// <div className="col-sm-6">
//             <label for="" name="emailid" id="emailid" type="email" value={emailId}
//             onChange={(e)=>{handleChange(e,"emailId")}}>abc@gmail.com </label>
// </div>
// </div>
// </div>
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="shopRent">Shop Rent</label>
// </div>
// <div className="col-sm-6">
//
//         <label for="" id="shopRent" name="shopRent" value={shopRent}
//         onChange={(e)=>{handleChange(e,"shopRent")}}> 50000</label>
//
// </div>
// </div>
// </div>
// </div>
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="securityDeposit">Security Deposit</label>
// </div>
// <div className="col-sm-6">
//
//         <label for="" id="securityDeposit" name="securityDeposit" value={securityDeposit}
//         onChange={(e)=>{handleChange(e,"securityDeposit")}}> 250000 </label>
//
// </div>
// </div>
// </div>
//
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="advancePayment">Advance Payment</label>
// </div>
// <div className="col-sm-6">
//
//         <label for="" id="advancePayment" name="advancePayment" value={advancePayment}
//         onChange={(e)=>{handleChange(e,"advancePayment")}}>20000 </label>
//
// </div>
// </div>
// </div>
// </div>
//
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//         <label for="tradeLicense">Trade License No.</label>
// </div>
// <div className="col-sm-6">
//         <label for="" id="tradeLicense" name="tradeLicense" value={tradeLicense}
//         onChange={(e)=>{handleChange(e,"tradeLicense")}}>15687Z </label>
// </div>
// </div>
// </div>
//   <div className="col-sm-6">
//   <div className="row">
//   <div className="col-sm-6 label-text">
//             <label for="DateofCommencement"> Date of Commencement </label>
//     </div>
//     <div className="col-sm-6">
//
//             <label for="" name="DateofCommencement" id="DateofCommencement" value={DateofCommencement}
//             onChange={(e)=>{handleChange(e,"DateofCommencement")}}>28/03/2017 </label>
//
//     </div>
//     </div>
//     </div>
// </div>
//
//
// <div className="row">
// <div className="col-sm-6">
// <div className="row">
// <div className="col-sm-6 label-text">
//     <label for="methodCalculated">Method by increase in Rent is calculated during Renewal</label>
// </div>
// <div className="col-sm-6">
//     <label for="" id="methodCalculated" name="methodCalculated" value={methodCalculated}
//     onChange={(e)=>{handleChange(e,"methodCalculated")}}> Bangalore</label>
// </div>
// </div>
// </div>
// </div>
//
//
// </form>
// </div>
