
class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      demands: {},
      agreement: {},
      legacyDemand: {}
    }

    this.close = this.close.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChange=this.handleChange.bind(this);
    this.handleCheckAll=this.handleCheckAll.bind(this);
  }
  close() {
    // widow.close();
    open(location, '_self').close();
  }

  addOrUpdate(e) {
    e.preventDefault();
    //call update method
  }

  handleChange(e,name,auctualDemand,k) {

  }

  handleCheckAll(e, name){
  // console.log(JSON.stringify(this.state.demands));
  //   var keys = Object.keys(this.state.demands);
  //   if(this.state.demands[0] && this.state.demands[0]["isActualDemand"]) {
  //     var demands = this.state.demands;
  //     for(key in demands) {
  //       if(!demands[key]) demands[key] = {};
  //       demands[key]["isActualDemand"] = this.state.demands[0]["isActualDemand"];
  //     }
  //
  //     this.setState({
  //       demands
  //     })
  //   } else {
  //     e.preventDefault();
  //   }
  }

  componentWillMount() {
    var demands={};
   var agreementDetail={};
   agreementDetail["timePeriod"]=5;
   agreementDetail["paymentCycle"]="MONTH";

   var date = new Date();
   var endDate = new Date((date.getFullYear()+agreementDetail["timePeriod"]),date.getMonth(),date.getDate());
   var monthNameList = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];






   switch (agreementDetail["paymentCycle"]) {
     case "MONTH":
         // demands=getDemandRows(agreementDetail["paymentCycle"],agreementDetail["timePeriod"]);
         var i=0;
         while (date <= endDate)
         {
             // var stringDate = monthNameList[date.getMonth()] + " " + date.getFullYear();
             // resultList.push(stringDate);
             demands[i]={

                   "taxAmount" : null,
                   "collectionAmount" : null,
                   "rebateAmount" : null,
                   "taxReason" : null,
                   "taxPeriod" : monthNameList[date.getMonth()] + " " + date.getFullYear(),
                   "glCode" : null,
                   "isActualDemand" : null,
                   tenantId
             }

              date.setMonth(date.getMonth() + 1);
          }
       break;

     case "QUARTER":

       break;
     case "HALFYEAR":

       break;
     default: break

  }


   this.setState({
     demands
   })
  }


  render() {
    let {demands} = this.state;
    // let {month, demand, collection} = demands;
    let {handleCheckAll, handleChange, save} = this;

    const renderBody = function() {

      return Object.keys(demands).map((k, index)=>
      {
        return (<tr key={demands[k].code}>
                    <td>{demands[k]["taxPeriod"]}</td>
                    <td data-label="demand">
                      <input type="number" name={demands[k]["isActualDemand"]} value={demands[k]["isActualDemand"]} onChange={(e) => {
                        handleChange(e, "demands","isActualDemand", k)
                      }}/>
                    </td>
                    <td data-label="collection">
                      <input type="number" name={demands[k]["collectionAmount"]} value={demands[k]["collectionAmount"]} onChange={(e) => {
                        handleChange(e, "demands","collectionAmount", k)
                      }}/>
                    </td>
                </tr>)
      })


    }
    return (
      <div>

          <form onSubmit={(e) => {
            this.addOrUpdate(e);
          }}>
            <div className="form-section-inner">

              <table id="editDemand" className="table table-bordered">
                <thead>
                  <tr>
                    <th className="text-center">
                      Month
                    </th>
                    <th className="text-center">Demand

                      <div className="checkbox">
                          <label>
                            <input type="checkbox" onChange={(e)=>{handleCheckAll(e,"Demand")}}/>
                          </label>
                      </div>


                    </th>
                    <th>
                      Collection
                      <div className="checkbox">
                          <label>
                            <input type="checkbox" onChange={(e)=>{handleCheckAll(e,"Collection")}}/>
                          </label>
                      </div>
                    </th>
                    </tr>

                    {renderBody()}

                </thead>
                <tbody>

                </tbody>
              </table>
              <div className="text-center">
                <button type="button" className="btn btn-default" onClick={(e) => {
                  this.close()
                }}>Close</button>

                <button type="Submit" className="btn btn-submit">Submit</button>
              </div>
            </div>
          </form>
        </div>

    );
  }
}

ReactDOM.render(
  <EditDemand/>, document.getElementById('root'));
