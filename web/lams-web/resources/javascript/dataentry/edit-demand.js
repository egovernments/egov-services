
class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      demands: {},
      agreementDetail: {},
      paymentCycle:"",
      commonDemand:null,
      commonCollection:null
      //isEditable :false
      //this code should be used to make collection editable or not
    }

    this.close = this.close.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChange=this.handleChange.bind(this);
    // this.handleCheckAll=this.handleCheckAll.bind(this);
    this.handleChangeAll=this.handleChangeAll.bind(this);

  }
  close() {
    // widow.close();
    open(location, '_self').close();
  }

validateOnSubmit(e){
  var rent =null;
  var collection =null;
  var isValid =true;
  var demands = this.state.demands;

  for(var demand in demands){
      rent = demands[demand].taxAmount;
      collection = demands[demand].collectionAmount;
      if(collection>rent){
        isValid = false;
        break;
      }
  }
  return isValid;
}
  addOrUpdate(e)
  {
    e.preventDefault();
    var agreementDetail=this.state.agreementDetail;
    var demands=this.state.demands;
    var tempt=[];

    for (var variable in demands) {
        console.log(demands[variable]);
        tempt.push(demands[variable]);
    }

    agreementDetail["legacyDemands"][0]["demandDetails"]=tempt;

    // api call update method
    var response = $.ajax({
        url: baseUrl + `/lams-services/agreements/_update/${agreementDetail.agreementNumber}?tenantId=` + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({
            RequestInfo: requestInfo,
            Agreement: agreementDetail
        }),
        async: false,
        headers: {
            'auth-token': authToken
        },
        contentType: 'application/json'
    });
    if (response["status"] === 201) {
        showError("Demand updated successfully");
        setTimeout(function(){
          window.open(location, '_self').close();
        }, 5000);
        // window.location.href = "app/search-assets/create-agreement-ack.html?name=" + getNameById(employees, _agrmntDet["assignee"]) + "&ackNo=" + responseJSON["Agreements"][0]["acknowledgementNumber"];
    } else {
        showError(response["statusText"]);
    }

    // console.log(agreementDetail);



  }

  handleChange(e,name,k) {

      if(e.target.value<0){
        e.preventDefault();
        showError("amount can't be negative.");
      }
       else{
        this.setState({
            demands:{
              ...this.state.demands,
              [k]:{
                  ...this.state.demands[k],
                  [name]:e.target.value
              }
            }
        })
      }
  }

    handleChangeAll(e,whichProperty)
    {
        var demands=this.state.demands;
        if(e.target.value<0){
          e.target.value=0;
          e.preventDefault();
          showError("amount can't be negative.");
        }
         else{
        for (var variable in demands) {
              demands[variable][whichProperty]=e.target.value;
        }

        this.setState({
          demands
        })
      }
    }

  // handleCheckAll(e, name){
  // // console.log(JSON.stringify(this.state.demands));
  // //   var keys = Object.keys(this.state.demands);
  // //   if(this.state.demands[0] && this.state.demands[0]["isActualDemand"]) {
  // //     var demands = this.state.demands;
  // //     for(key in demands) {
  // //       if(!demands[key]) demands[key] = {};
  // //       demands[key]["isActualDemand"] = this.state.demands[0]["isActualDemand"];
  // //     }
  // //
  // //     this.setState({
  // //       demands
  // //     })
  // //   } else {
  // //     e.preventDefault();
  // //   }
  // }
  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = logo_ele[0].getAttribute("src");
       }
     }
  }

  componentWillMount()
  {
         var demands={};
         var tempDemands={};
         var agreementDetail={};
         //api call

         try {
             if (getUrlVars()["agreementNumber"]) {
                agreementDetail = commonApiPost("lams-services", "agreements", "demands/_prepare", {
                     agreementNumber: getUrlVars()["agreementNumber"],
                     tenantId
                 }).responseJSON["Agreements"][0] || {};
             }



         } catch (e) {
             console.log(e);
         }
        //  agreementDetail["timePeriod"]=5;

        //api call for prepare demands

        //  try {
        //
        //         agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
        //              agreementNumber: getUrlVars()["agreementNumber"],
        //              tenantId
        //          }).responseJSON["Agreements"][0] || {};
        //
        //  } catch (e) {
        //      console.log(e);
        //  }


        //  agreementDetail["paymentCycle"]="HALFYEAR";
        //  agreementDetail["commencementDate"]="01/01/2016";
        //  agreementDetail["expiryDate"]="01/10/2017";
        //  agreementDetail["demands"]=null;
        //  agreementDetail["legacyDemands"]=[];

        // agreementDetail["legacyDemands"].push({demandDetails:[]});

         var monthNameList = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

         var date = new Date(agreementDetail["commencementDate"].split("/")[2],agreementDetail["commencementDate"].split("/")[1]-1,agreementDetail["commencementDate"].split("/")[0]);
         var currentDate=new Date();
         var endDate = new Date(agreementDetail["expiryDate"].split("/")[2],agreementDetail["expiryDate"].split("/")[1]-1,agreementDetail["expiryDate"].split("/")[0])<currentDate?new Date(agreementDetail["expiryDate"].split("/")[2],agreementDetail["expiryDate"].split("/")[1]-1,agreementDetail["expiryDate"].split("/")[0]):new Date();



          var k=0;
          for (var variable in agreementDetail["legacyDemands"][0]["demandDetails"]) {
              if (agreementDetail["legacyDemands"][0]["demandDetails"][k].taxReason.toLowerCase()=="rent") {
                tempDemands[k]=agreementDetail["legacyDemands"][0]["demandDetails"][k];
              }

              k++;
          }

          //  switch (agreementDetail["paymentCycle"]) {
          //    case "MONTH":
           //
           //
          //        // demands=getDemandRows(agreementDetail["paymentCycle"],agreementDetail["timePeriod"]);
          //        var i=0;
          //       //  console.log(date);
          //       //  console.log(endDate);
          //        while (date <= endDate)
          //        {
          //            // var stringDate = monthNameList[date.getMonth()] + " " + date.getFullYear();
          //            // resultList.push(stringDate);
          //            demands[i]=tempDemands[i];
          //           //  {
          //            //
          //           //        "taxAmount" : null,
          //           //        "collectionAmount" : null,
          //           //        "rebateAmount" : null,
          //           //        "taxReason" : null,
          //           //        "taxPeriod" : monthNameList[date.getMonth()] + " " + date.getFullYear(),
          //           //        "glCode" : null,
          //           //        "isActualDemand" : null,
          //           //        tenantId
          //           //  }
           //
          //           date.setMonth(date.getMonth() + 1);
          //           i++;
          //       }
          //      break;
           //
          //    case "QUARTER":
          //    // demands=getDemandRows(agreementDetail["paymentCycle"],agreementDetail["timePeriod"]);
           //
          //    var i=0;
          //   //  console.log(date);
          //   //  console.log(endDate);
          //    while (date <= endDate)
          //    {
          //        // var stringDate = monthNameList[date.getMonth()] + " " + date.getFullYear();
          //        // resultList.push(stringDate);
           //
          //         demands[i]=tempDemands[i];
           //
           //
          //       date.setMonth(date.getMonth() + 3);
          //       i++;
          //     }
          //      break;
          //    case "HALFYEAR":
          //    // demands=getDemandRows(agreementDetail["paymentCycle"],agreementDetail["timePeriod"]);
           //
          //    var i=0;
          //   //  console.log(date);
          //   //  console.log(endDate);
          //    while (date <= endDate)
          //    {
          //        // var stringDate = monthNameList[date.getMonth()] + " " + date.getFullYear();
          //        // resultList.push(stringDate);
           //
          //       demands[i]=tempDemands[i];
           //
          //       date.setMonth(date.getMonth() + 6);
          //       i++;
          //     }
          //      break;
          //    default:
          //    // demands=getDemandRows(agreementDetail["paymentCycle"],agreementDetail["timePeriod"]);
           //
          //    var i=0;
          //   //  console.log(date);
          //   //  console.log(endDate);
          //    while (date <= endDate)
          //    {
          //        // var stringDate = monthNameList[date.getMonth()] + " " + date.getFullYear();
          //        // resultList.push(stringDate);
          //       demands[i]=tempDemands[i];
           //
          //       date.setMonth(date.getMonth() + 12);
          //       i++;
          //     }
          //    break;
           //
          //     }


          demands=tempDemands;



    // console.log(demands);
     this.setState({
       demands,
       paymentCycle:agreementDetail["paymentCycle"],
       agreementDetail
     })
  }


  render()
  {
    let {demands,paymentCycle,commonDemand,commonCollection} = this.state;
    // let {month, demand, collection} = demands;
    // isEditable to be added to make collection is editable or not
    let {handleCheckAll,handleChangeAll, handleChange, save} = this;

    const renderBody = function() {

      return Object.keys(demands).map((k, index)=>
      {
        return (<tr key={demands[k].code}>
                    <td>{demands[k]["taxPeriod"]+"["+demands[k]["taxReason"]+"]"}</td>
                    <td data-label="demand">
                      <input type="number" name={demands[k]["taxAmount"]} value={demands[k]["taxAmount"]} onChange={(e) => {
                        handleChange(e, "taxAmount", k)
                      }}/>
                    </td>
                    <td data-label="collection">
                      <input type="number" name={demands[k]["collectionAmount"]} value={demands[k]["collectionAmount"]} onChange={(e) => {
                        handleChange(e, "collectionAmount", k)
                      }}/>
                    </td>
                </tr>)
      })


    }
    return (
      <div>

          <form onSubmit={(e) => {

         var valid = this.validateOnSubmit(e);
         if(valid){
            this.addOrUpdate(e);
          }else {
            e.preventDefault();
            showError("Collection should not be greater than rent!");

          }
          }}>
            <div className="form-section-inner">

              <table id="editDemand" className="table table-bordered">
                <thead>
                  <tr>
                    <th className="text-center">
                      {paymentCycle +"LY Period"}
                    </th>
                    <th className="text-center">Demand

                    <input type="number" min="0"  style={{color:"black"}} name="commonDemand" value={commonDemand}  onChange={(e) => {
                      handleChangeAll(e, "taxAmount")
                    }}/>

                    </th>
                    <th>
                      Collection
                      <input type="number" min="0" style={{color:"black"}} name="commonCollection" value={commonCollection}   onChange={(e) => {
                        handleChangeAll(e, "collectionAmount")
                      }}/>

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


  // {/*
  //
  //   <div className="checkbox">
  //       <label>
  //         <input type="checkbox" onChange={(e)=>{handleCheckAll(e,"Demand")}}/>
  //       </label>
  //   </div>
  //
  //   */}
