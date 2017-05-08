
let months=[
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December"
];

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
    this.handleCheckAll = this.handleCheckAll.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.markDemand = this.markDemand.bind(this);
    this.markBulkDemand = this.markBulkDemand.bind(this);
}
close(){
    // widow.close();
    open(location, '_self').close();
}

  handleCheckAll(e,date,type)
  {
      this.markBulkDemand(date,type);
  }

  markBulkDemand(oDate,type)
  {
    var demands=this.state.demands;
    var date=`${oDate.getMonth()}-${oDate.getDate().toString().length===1?"0"+oDate.getDate():oDate.getDate()}`;
    for(var emp in employees)
    {
      var now = new Date();
      employees[emp].attendance[date]=type;
      // this.markDemand(type,emp,date);
    }
    this.setState({
        employees:employees
    })
  }

  handleChange(e,empCode,date)
  {
            this.markDemand(e.target.value,empCode,date);


  }

  markDemand(value,empCode,date)
  {

      this.setState({
        demands:{
            ...this.state.demands,
            [empCode]:{
              ...this.state.demands[empCode],
              ["attendance"]:{
                  ...this.state.demands[empCode]["attendance"],
                  [date]:value
              }
            }
          }
      })

  }


render() {
let{demands}=this.state;
let{month,demand,collection}=demands;
let {handleCheckAll,handleChange,save}=this;

return (
  <div>


  <form >
        <div className="form-section-inner">

        <table id="editDemand" className="table table-bordered">
          <thead>
          <tr>
              <th> Month &nbsp; &nbsp; &nbsp; &nbsp; </th>
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
