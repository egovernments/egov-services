




class CreateAsset extends React.Component {

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    return (
      <div>
              <div className="form-section-inner">
                <div className="row">
                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for="Name">Name  <span> * </span></label>
                      </div>
                      <div className="col-sm-6">
                        <input id="shoppingComplexName" name="shoppingComplexName" type="text" required/>
                      </div>
                    </div>
                  </div>

                  <div className="col-sm-6">
                    <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="shopAreaUom">Asset Category <span> *</span> </label>
                        </div>
                      <div className="col-sm-6">
                         <div className="styled-select">
                          <select id="shopAreaUom" name="shopAreaUom" required>
                          <option>Land</option>
                          <option>Shopping Complex</option>
                          <option>Park</option>
                          </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="row">
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="department">Department  </label>
                </div>
                <div className="col-sm-6">
                <div className="styled-select">
                    <select id="department" name="department" >
                    <option>Accounts</option>
                    <option>Education</option>
                    <option>Administration</option>
                    </select>
                </div>
                </div>
                </div>
                </div>

                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                    <label for="shopAreaUom">Mode Of Acquisition <span> * </span>  </label>
                </div>
                <div className="col-sm-6">
                <div className="styled-select">
                    <select id="shopAreaUom" name="shopAreaUom">
                    <option>mts</option>
                    <option>feet</option>
                    <option>yards</option>
                    </select>
                </div>
                </div>
                </div>
                </div>
                </div>
                </div>
                <div className="row">
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="shopAreaUom"> Date Of Creation </label>
                </div>
                <div className="col-sm-6">
                <div className="text-no-ui">
                  <span>
                  <i className="glyphicon glyphicon-calendar"></i>
                  </span>
                  <input type="date" name="dateofcreation" id="dateofcreation"/>
                </div>
                </div>
                </div>
                </div>
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="assetAssetAddress">Asset Details</label>
                </div>
                <div className="col-sm-6">
                  <textarea id="assetAssetAddress" name="assetAssetAddress" type="text"></textarea>
                </div>
                </div>
                </div>
                </div>

                <div className="row">
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="length">Length  </label>
                </div>
                <div className="col-sm-6">
                  <input id="length" name="length" type="text" />
                </div>
                </div>
                </div>
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="width">Width  </label>
                </div>
                <div className="col-sm-6">
                  <input id="width" name="width" type="text" />
                </div>
                </div>
                </div>
                </div>

                <div className="row">
                <div className="col-sm-6">
                <div className="row">
                <div className="col-sm-6 label-text">
                <label for="area">TOtal Area  </label>
                </div>
                <div className="col-sm-6">
                <input id="area" name="area" type="text" />
                </div>
                </div>
                </div>
                </div>
                </div>

                <div className="form-section" id="allotteeDetailsBlock">
                <h3 className="categoryType">Location Details </h3>
                <div className="form-section-inner">

                  <div className="row">
                  <div className="col-sm-6">
                  <div className="row">
                  <div className="col-sm-6 label-text">
                        <label for="shopAreaUom"> Location <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                            <select id="shopAreaUom" name="shopAreaUom">
                            <option>Bangalore</option>
                            <option>Kolkata</option>
                            <option>Mumbai</option>
                        </select>
                  </div>
                  </div>
                  </div>
                  </div>

                    <div className="col-sm-6">
                    <div className="row">
                    <div className="col-sm-6 label-text">
                            <label for="shopAreaUom"> Revenue Ward <span> * </span> </label>
                     </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                                <select id="shopAreaUom" name="shopAreaUom">
                                <option>121</option>
                                <option>199</option>
                                <option>122</option>
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
                                <label for="shopAreaUom"> Block Number  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="styled-select">
                                  <select id="shopAreaUom" name="shopAreaUom">
                                  <option>Bangalore</option>
                                  <option>Kolkata</option>
                                  <option>Mumbai</option>
                              </select>
                            </div>
                            </div>
                            </div>
                            </div>

                            <div className="col-sm-6">
                            <div className="row">
                            <div className="col-sm-6 label-text">
                                  <label for="shopAreaUom"> Street  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="styled-select">
                                      <select id="shopAreaUom" name="shopAreaUom">
                                      <option>Bangalore</option>
                                      <option>Kolkata</option>
                                      <option>Mumbai</option>
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
                                        <label for="shopAreaUom"> Election Ward No  </label>
                                  </div>
                                  <div className="col-sm-6">
                                  <div className="styled-select">
                                      <select id="shopAreaUom" name="shopAreaUom">
                                      <option>22</option>
                                      <option>23</option>
                                      <option>24</option>
                                </select>
                              </div>
                              </div>
                              </div>
                              </div>

                                <div className="col-sm-6">
                                <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for="shopAreaUom"> Door Number  </label>
                                </div>
                                <div className="col-sm-6">
                                <input type="text" name="doorno" />
                                </div>
                                </div>
                                </div>
                                </div>

                                <div className="row">
                                    <div className="col-sm-6">
                                    <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="shopAreaUom"> Zone Number  </label>
                                  </div>
                                  <div className="col-sm-6">
                                  <div className="styled-select">
                                          <select id="shopAreaUom" name="shopAreaUom">
                                          <option>B-143</option>
                                          <option>K-152</option>
                                          <option>M-461</option>
                                      </select>
                                  </div>
                                  </div>
                                  </div>
                                  </div>

                                  <div className="col-sm-6">
                                  <div className="row">
                                  <div className="col-sm-6 label-text">
                                        <label for="pin">PIN No.</label>
                                  </div>
                                  <div className="col-sm-6">
                                        <input type="number" name="numberr" id="number" />
                                  </div>
                                  </div>
                                  </div>
                                  </div>


</div>
</div>



</div>


    );
  }
}


ReactDOM.render(
  <CreateAsset />,
  document.getElementById('root')
);


// //
// //
//
//
//
// //
//
//
//
//     //
//   <div className="form-section" id="agreementDetailsBlockTemplateThree">
//   <h3 className="categoryType">Shopping Complex  Details </h3>
//   <div className="form-section-inner">
//
//         <div className="row">
//         <div className="col-sm-6">
//         <div className="row">
//         <div className="col-sm-6 label-text">
//                   <label for="acuquisition">MOde Of Acquisition <span> * </span> </label>
//          </div>
//          <div className="col-sm-6">
//           <div className="styled-select">
//                       <select name="acuquisition" id="acuquisition">
//                     <option>Purchase</option>
//                     <option>Tender</option>
//                     <option>Connstruction</option>
//                     <option value="Direct">Donation</option>
//
//                     </select>
//             </div>
//             </div>
//             </div>
//             </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="name">Shopping COmplex Name.</label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="name" id="name" />
//               </div>
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="complexNo">Shopping Complex No  </label>
//               </div>
//               <div className="col-sm-6">
//                   <input type="text" name="complexNO" id="complexNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                       <label for="doorNo">Door No  </label>
//               </div>
//               <div className="col-sm-6">
//                 <input type="number" name="doorNO" id="doorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="row">
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                     <label for="complexNo"> Number of floor  </label>
//               </div>
//               <div className="col-sm-6">
//                     <input type="text" name="floorNO" id="floorNO" />
//               </div>
//               </div>
//               </div>
//
//               <div className="col-sm-6">
//               <div className="row">
//               <div className="col-sm-6 label-text">
//                   <label for="noofShop">Total Number of Shop  </label>
//             </div>
//             <div className="col-sm-6">
//                 <input type="text" name="noofShop" id="noofShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="floorNo">Floor No </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="floorNo" id="floorNo">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//             </div>
//             </div>
//             </div>
//             </div>
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                   <label for="noShop">Number Of Shop.</label>
//             </div>
//             <div className="col-sm-6">
//                   <input type="text" name="noShop" id="noShop" />
//             </div>
//             </div>
//             </div>
//             </div>
//
//             <div className="row">
//             <div className="col-sm-6">
//             <div className="row">
//             <div className="col-sm-6 label-text">
//                 <label for="status">Status <span> * </span> </label>
//             </div>
//             <div className="col-sm-6">
//             <div className="styled-select">
//                   <select name="status" id="status">
//                   <option>1</option>
//                   <option>2</option>
//                   <option>3</option>
//                   <option value="Direct">4</option>
//
//                 </select>
//           </div>
//           </div>
//           </div>
//           </div>
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//                   <label for="value">Value</label>
//            </div>
//            <div className="col-sm-6">
//                   <input type="text" name="value" id="value" />
//           </div>
//           </div>
//           </div>
//           </div>
//
//           <div className="row">
//           <div className="col-sm-6">
//           <div className="row">
//           <div className="col-sm-6 label-text">
//               <label for="remarks">Remarks </label>
//         </div>
//         <div className="col-sm-6">
//               <textarea name="remarks" id="remarks"></textarea>
//       </div>
//       </div>
//       </div>
//       </div>
//
//       <div className="text-center">
//           <button type="button" className="btn btn-submit" >Create</button>
//           <button type="button" className="btn btn-submit">close</button>
//       </div>
//       </div>
//       </div>
