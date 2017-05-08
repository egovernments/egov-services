// const months = [
//   {
//     id: 0,
//     name: "January"
//   }, {
//     id: 1,
//     name: "February"
//   }, {
//     id: 2,
//     name: "March"
//   }, {
//     id: 3,
//     name: "April"
//   }, {
//     id: 4,
//     name: "May"
//   }, {
//     id: 5,
//     name: "June"
//   }, {
//     id: 6,
//     name: "July"
//   }, {
//     id: 7,
//     name: "August"
//   }, {
//     id: 8,
//     name: "September"
//   }, {
//     id: 9,
//     name: "October"
//   }, {
//     id: 10,
//     name: "November"
//   }, {
//     id: 11,
//     name: "December"
//   }
// ];

class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      demands: {},
      agreement: {},
      legacyDemand: {}
    }
  }
  close() {
    // widow.close();
    open(location, '_self').close();
  }

  addOrUpdate(e) {
    e.preventDefault();
    //call update method
  }

  componentWillMount()
  {
    var demands={};
    var agreementDetail={};
    //uncommet once its up

    // try {
    //   if (getUrlVars()["agreementNumber"]) {
    //     agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
    //       agreementNumber: getUrlVars()["agreementNumber"],
    //       tenantId
    //     }).responseJSON["Agreements"][0] || {};
    //   } else if (getUrlVars()["acknowledgementNumber"]) {
    //     agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
    //       acknowledgementNumber: getUrlVars()["acknowledgementNumber"],
    //       tenantId
    //     }).responseJSON["Agreements"][0] || {};
    //   } else {
    //     agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
    //       stateId: getUrlVars()["state"],
    //       tenantId
    //     }).responseJSON["Agreements"][0] || {};
    //   }
    //
    // } catch (e) {
    //   console.log(e);
    // }
    agreementDetail["timePeriod"]=5;
    agreementDetail["paymentCycle"]="MONTH";

    var date = new Date();
    var endDate = new Date((date.getFullYear()+agreementDetail["timePeriod"]),date.getMonth(),date.getDate());
    var monthNameList = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];




    //uncomment this if else when api is ready
    // if (agreementDetail["demands"]>0 || !agreementDetail["demands"]>0) {
    //
    // } else {
    //
    // }


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
              date.setDate(date.getMonth() + 1);
          }
        break;

      case "QUARTER":

        break;
      case "HALFYEAR":

        break;
      default:

    }

    // var currentMonthId = new Date().getMonth();
    // var _months = months.filter(val => {
    //   return (val.id <= currentMonthId);
    // })
    // this.setState({months: _months})

    this.setState({
      demands
    })
  }


  // function getDemandRows(mode,years) {
  //
  // }

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
              </thead>
              <tbody>
                {renderBody()}
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
