class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state={demands:{
      "month":"",
      "demand":"",
      "collection":"",
      "month": "",
      "year": "",


}
}
}
close(){
    // widow.close();
    open(location, '_self').close();
}

render() {
  let{demands}=this.state;
let{month,demand,collection}=demands;

return (
  <div>


  <form >
        <div className="form-section-inner">

        <table id="editDemand" className="table table-bordered">
          <thead>
          <tr>
              <th> Month </th>
              <th>&nbsp; &nbsp; &nbsp; &nbsp; Demand <tr> &nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;<input type="checkbox" value="demand" /> </tr></th>
              <th> &nbsp; &nbsp;&nbsp; &nbsp; Collection <tr> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp;<input type="checkbox" value="collection" /> </tr></th>

          </tr>
          </thead>
                    <tbody id="attendanceTableBody">

          </tbody>


          </table>



        <div className="text-center">

        <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>

        </div>





</div>
</form>
</div>
);
}
}
ReactDOM.render(
  <EditDemand />,
  document.getElementById('root')
);
