class EditDemand extends React.Component {
  constructor(props) {
    super(props);
    this.state={demands:{
      "arrears":"",
      "arrearsDemand":"",
      "paymentCycle":"",
      "arrearesCollection":"",
      "current":"",
      "currentDemand":"",
      "currentCollection":"",

}
}
}
close(){
    // widow.close();
    open(location, '_self').close();
}

render() {
  let{demands}=this.state;
let{arrears,arrearsDemand,paymentCycle,arrearesCollection,current,currentDemand,currentCollection}=demands;

return (
  <div>


  <form >
        <div className="form-section-inner">

        <div className="row">
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="arrears">Arrears</label>
        </div>
        <div className="col-sm-6">
                <input type="text" id="arrears" name="arrears" value={arrears}/>
        </div>
        </div>
        </div>

        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="arrearsDemand">Arrears Demand <span>*</span></label>
        </div>
        <div className="col-sm-6">
        <div className="text-no-ui">
          <span>₹</span>
                 <input type="number" min="0" name="arrearsDemand" id="arrearsDemand" value={arrearsDemand} required/>
        </div>
        </div>
        </div>
        </div>
        </div>

        <div className="row">
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="arrearesCollection">Arrears Collection <span>*</span></label>
        </div>
        <div className="col-sm-6">
        <div className="text-no-ui">
          <span>₹</span>
                 <input type="number" min="0" name="arrearesCollection" id="arrearesCollection" value={arrearesCollection} required/>
        </div>
        </div>
        </div>
        </div>
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="current">Current</label>
        </div>
        <div className="col-sm-6">
                <input type="text" id="current" name="current" value={current}/>
        </div>
        </div>
        </div>
        </div>


        <div className="row">
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="currentDemand">Current Demand <span>*</span></label>
        </div>
        <div className="col-sm-6">
        <div className="text-no-ui">
          <span>₹</span>
                 <input type="number" min="0" name="currentDemand" id="currentDemand" value={currentDemand} required/>
        </div>
        </div>
        </div>
        </div>
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="currentCollection">Current Collection <span>*</span></label>
        </div>
        <div className="col-sm-6">
        <div className="text-no-ui">
          <span>₹</span>
                 <input type="number" min="0" name="currentCollection" id="currentCollection" value={currentCollection} required/>
        </div>
        </div>
        </div>
        </div>
        </div>


        <div className="row">
        <div className="col-sm-6">
        <div className="row">
        <div className="col-sm-6 label-text">
                <label for="paymentCycle">Payment Cycle <span>*</span></label>
        </div>
        <div className="col-sm-6">
                         <input type="radio"  name="paymentCycle" id="paymentCycle" value={paymentCycle} required/> Yearly
                         &nbsp;
                          <input type="radio"  name="paymentCycle" id="paymentCycle" value={paymentCycle} required/> Monthly


        </div>
        </div>
        </div>
        </div>

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
