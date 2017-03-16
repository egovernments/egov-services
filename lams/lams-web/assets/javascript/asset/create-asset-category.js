




class CreateAsset extends React.Component {

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    return (
                  <div>
                                            <div role="tabpanel" className="tab-pane" id="assignmentDetails">
                                              <div className="form-section" >
                                                <a href="#" className="btn btn-default btn-action pull-right" data-toggle="modal" data-target="#assignmentDetailModal"><span className="glyphicon glyphicon-plus"></span></a>
                                                <div className="clearfix"></div>

                                                <div className="land-table">
                                                    <table className="table table-bordered">
                                                        <thead>
                                                            <tr>
                                                                <th>Name</th>
                                                                <th>Data Type</th>
                                                                <th>RegEx format</th>
                                                                <th>Active</th>
                                                                <th>Mandatory</th>
                                                                <th>Values</th>
                                                                <th>Local Text</th>
                                                                <th>Action</th>
                                                            </tr>
                                                        </thead>

                                                        <tbody>
                                                          <tr>
                                                            <td> Kumaresh </td>
                                                            <td>   String</td>
                                                          <td> cdccdbch </td>
                                                          <td>  Yes</td>
                                                          <td> yes</td>
                                                          <td> 100 </td>
                                                          <td> vcavckjaa</td>
                                                          <td> <button className="btn btn-default btn-action"><span className="glyphicon glyphicon-trash"></span></button></td>
                                                          </tr>

                                                        </tbody>

                                                    </table>

                                                </div>
                                                </div>
                                                </div>


                                                <div className="modal fade" id="assignmentDetailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                                                    <div className="modal-dialog" role="document">
                                                        <div className="modal-content">
                                                            <form className="assignmentDetail" id="assignmentDetail">
                                                                <div className="modal-header">
                                                                    <button type="button" className="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                                    <h4 className="modal-title" id="myModalLabel">Asset Category</h4>
                                                                </div>
                                                                          <div className="modal-body">

                                                                            <div className="row">
                                                                            <div className="col-sm-6">
                                                                            <div className="row">
                                                                            <div className="col-sm-6 label-text">
                                                                                    <label for="name">Name</label>
                                                                            </div>
                                                                            <div className="col-sm-6">
                                                                                    <input type="text" id="name" name="name" />
                                                                            </div>
                                                                            </div>
                                                                            </div>
                                                                            </div>

                                                                                                      <div className="row">
                                                                                                      <div className="col-sm-6">
                                                                                                      <div className="row">
                                                                                                      <div className="col-sm-6 label-text">
                                                                                                          <label for="categoryType"> Asset Category Type <span> * </span> </label>
                                                                                                      </div>
                                                                                                      <div className="col-sm-6">
                                                                                                      <div className="styled-select">
                                                                                                              <select id="categoryType" name="categoryType" required>
                                                                                                              <option>Land </option>
                                                                                                              <option>Movable </option>
                                                                                                              <option>Immovable </option>
                                                                                                          </select>
                                                                                                      </div>
                                                                                                      </div>
                                                                                                      </div>
                                                                                                      </div>
                                                                                                      <div className="col-sm-6">
                                                                                                      <div className="row">
                                                                                                      <div className="col-sm-6 label-text">
                                                                                                              <label for="parentCategory"> Parent Category  </label>
                                                                                                      </div>
                                                                                                      <div className="col-sm-6">
                                                                                                      <div className="styled-select">
                                                                                                                  <select id="parentCategory" name="parentCategory">
                                                                                                                  <option>Land</option>
                                                                                                                  <option>Market</option>
                                                                                                                  <option>Fish Tank</option>
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
                                                                                                                                       <label for="descriptionMethod"> Description Method  </label>
                                                                                                                                   </div>
                                                                                                                                   <div className="col-sm-6">
                                                                                                                                       <div className="styled-select">
                                                                                                                                           <select id="descriptionMethod" name="descriptionMethod">
                                                                                                                                             <option> Select</option>
                                                                                                                                           <option>Straight Line Method</option>
                                                                                                                                           <option>Written Down Value Method</option>
                                                                                                                                       </select>
                                                                                                                                       </div>
                                                                                                                                   </div>
                                                                                                                               </div>
                                                                                                                             </div>
                                                                                                                               <div className="col-sm-6">
                                                                                                                                   <div className="row">
                                                                                                                                       <div className="col-sm-6 label-text">
                                                                                                                                           <label for="assetAccount"> Asset Account <span> *</span>  </label>
                                                                                                                                       </div>
                                                                                                                                       <div className="col-sm-6">
                                                                                                                                           <div className="styled-select">
                                                                                                                                               <select id="assetAccount" name="assetAccount" reqired>
                                                                                                                                               <option>Open Source</option>
                                                                                                                                               <option>Ground</option>
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
                                                                                                                                                  <label for="depreciationAccount"> Accumulated Depreciation Account  </label>
                                                                                                                                              </div>
                                                                                                                                              <div className="col-sm-6">
                                                                                                                                                  <div className="styled-select">
                                                                                                                                                      <select id="depreciationAccount" name="depreciationAccount">
                                                                                                                                                        <option> Select</option>
                                                                                                                                                      <option>Commercial Complex</option>
                                                                                                                                                      <option>Tanker</option>
                                                                                                                                                      <option>Computer</option>
                                                                                                                                                  </select>
                                                                                                                                                  </div>
                                                                                                                                              </div>
                                                                                                                                          </div>
                                                                                                                                        </div>
                                                                                                                                            <div className="col-sm-6">
                                                                                                                                              <div className="row">
                                                                                                                                                  <div className="col-sm-6 label-text">
                                                                                                                                                      <label for="reserveAccount"> Revaluation Reserve Account <span> *</span>  </label>
                                                                                                                                                  </div>
                                                                                                                                                  <div className="col-sm-6">
                                                                                                                                                      <div className="styled-select">
                                                                                                                                                          <select id="reserveAccount" name="reserveAccount" required>
                                                                                                                                                            <option> Select </option>
                                                                                                                                                          <option>Special Development Fund</option>
                                                                                                                                                          <option>City Challange Fund</option>
                                                                                                                                                          <option>Fixed Assets</option>
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
                                                                                                                                                                         <label for="expenseAccount"> Depreciation Expense Account  </label>
                                                                                                                                                                     </div>
                                                                                                                                                                     <div className="col-sm-6">
                                                                                                                                                                         <div className="styled-select">
                                                                                                                                                                             <select id="expenseAccount" name="expenseAccount">
                                                                                                                                                                             <option>B-143 </option>
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
                                                                                                                                                                         <label for="unitMeasurment"> Unit Of Measurment <span> *</span>  </label>
                                                                                                                                                                     </div>
                                                                                                                                                                     <div className="col-sm-6">
                                                                                                                                                                         <div className="styled-select">
                                                                                                                                                                             <select id="unitMeasurment" name="unitMeasurment" required>
                                                                                                                                                                             <option>Sq. Ft</option>

                                                                                                                                                                         </select>
                                                                                                                                                                         </div>
                                                                                                                                                                     </div>
                                                                                                                                                                 </div>
                                                                                                                                                               </div>
                                                                                                                                                             </div>
                                                                                                                                                             <div className="modal-footer">
                                                                                                                                                                                 <button type="button" className="btn btn-primary" data-dismiss="modal">Close</button>
                                                                                                                                                                                 <button type="button" className="btn btn-primary" >Add Row</button>
                                                                                                                                                                             </div>










                                                      </div>
                                                      </form>
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



//<div className="text-center">
//   <button type="button" className="btn btn-primary">Close</button>
// </div>
