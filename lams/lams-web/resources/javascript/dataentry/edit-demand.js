const months = [{
        id: 0,
        name: "January"
    }, {
        id: 1,
        name: "February"
    }, {
        id: 2,
        name: "March"
    }, {
        id: 3,
        name: "April"
    }, {
        id: 4,
        name: "May"
    }, {
        id: 5,
        name: "June"
    }, {
        id: 6,
        name: "July"
    }, {
        id: 7,
        name: "August"
    }, {
        id: 8,
        name: "September"
    }, {
        id: 9,
        name: "October"
    }, {
        id: 10,
        name: "November"
    }, {
        id: 11,
        name: "December"
    }
];


class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state={demands:{
      "month":"",
      "demand":"",
      "collection":"",
      "year": "",
},demands:[],
}
    // this.handleCheckAll = this.handleCheckAll.bind(this);
    // this.handleChange = this.handleChange.bind(this);
    // this.markDemand = this.markDemand.bind(this);
    // this.markBulkDemand = this.markBulkDemand.bind(this);
    this.handleChangeSrchRslt=this.handleChangeSrchRslt.bind(this);
}
close(){
    // widow.close();
    open(location, '_self').close();
}
handleChangeSrchRslt(e, name, ind) {
  var _emps = Object.assign([], this.state.demands);
  _emps[ind][name] = e.target.value;
  this.setState({
      ...this.state,
      demands: _emps
  })
}

componentWillMount()
  {
        var currentMonthId = new Date().getMonth();
        var _months = months.filter(val => {
         return (val.id <= currentMonthId);
      })
      this.setState({
          months: _months
      })
    }


render() {
let{demands,months}=this.state;
let{month,demand,collection}=demands;
let {handleCheckAll,handleChange,save,handleChangeSrchRslt,}=this;

    const renderBody = function()
    {

      return months.map((item, index)=>
      {

            return (<tr key={index}>

                    <td data-label="month">{item.name}</td>
                    <td data-label="demand">
                    <input type="number" id={item.id} name="demand"  value={item.demand}
                      onChange={(e)=>{handleChangeSrchRslt(e, "demand", index)}} />
                    </td>
                    <td data-label="collection">
                    <input type="number" id={item.id} name="collection"  value={item.collection}
                      onChange={(e)=>{handleChangeSrchRslt(e, "collection", index)}} />
                    </td>
                </tr>
            );

      })
    }

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
              <tbody id="employeeSearchResultTableBody">
              {
                  renderBody()
              }
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
