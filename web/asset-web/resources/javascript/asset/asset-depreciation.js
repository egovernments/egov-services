var current;

class CreateDepreciation extends React.Component {

  constructor(props) {
    super(props);
    this.state={};
    this.create=this.create.bind(this);
    this.close = this.close.bind(this);
    this.handleChange=this.handleChange.bind(this);
    this.showError=this.showError.bind(this);
  }

  componentDidMount(){
    $('.datepicker').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    });

    $('.fromDate, .toDate').datepicker().on('changeDate', function(ev){
      current.handleChange(ev.date.getTime(), $(this).attr('name'));
    });

  }

  handleChange(value, name){
    this.setState({ [name] : value });
  }

  create(e){
    var body = {
      RequestInfo: requestInfo,
      Depreciation: {
        tenantId: tenantId,
        financialYear: this.state.financialYear ? this.state.financialYear : null,
        fromDate: this.state.fromDate ? this.state.fromDate : null,
        toDate: this.state.toDate ? this.state.toDate : null,
        assetIds: []
      }
    };
    $.ajax({
      url : '/asset-services/assets/depreciations/_create',
      type: 'POST',
      dataType: 'json',
      data: JSON.stringify(body),
      contentType: 'application/json',
      headers:{
          'auth-token': authToken
      },
      success: function(response) {
        if(response.depreciation.depreciationDetails.length > 0){
          current.setState({error : false});
          var ep=new ExcelPlus();
          ep.createFile("Depreciation");
          ep.write({ "content":[ ["Asset ID", "Status", "Depreciation Rate", 'Value before Depreciation', 'Depreciation', 'Value after Depreciation', "Voucher Reference", "Reason for Failure"] ] });
          for (var j = 0; j < response.depreciation.depreciationDetails.length; j++){
            var obj = response.depreciation.depreciationDetails[j];
            ep.writeNextRow([obj.assetId, obj.status, obj.depreciationRate, obj.valueBeforeDepreciation, obj.depreciationValue, obj.valueAfterDepreciation, obj.voucherReference, obj.reasonForFailure]);
          }
          ep.saveAs("Asset Depreciation.xlsx");
        }else{
          current.setState({error : true});
        }
      },
      error : function(){

      }
    });
  }

  showError(){
    return (
      <div className="alert alert-danger alert-dismissible alert-toast" role="alert">
        <button type="button" className="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <strong>No Depreciation Found!</strong>
      </div>
    )
  }

  close() {
    open(location, '_self').close();
  }

  render(){
    current = this;
    let {handleChange} = this;
    return(
      <div className="form-section-inner">
      {this.state.error ? this.showError() : ''}
        <div className="row">
          <label className="col-sm-3 control-label text-right"> Financial Year</label>
          <div className="col-sm-3 add-margin">
            <select className="form-control" onChange={(e)=>{handleChange(e.target.value,"financialYear")}}>
              <option value="null">Select</option>
              <option value="2016-17">2016-17</option>
              <option value="2017-18">2017-18</option>
            </select>
          </div>
          <label className="col-sm-2 control-label text-right"> From Date</label>
          <div className="col-sm-3 add-margin">
            <input type="text" className="form-control fromDate datepicker" name="fromDate"
            onChange={(e)=>{handleChange(e.target.value,"fromDate")}} />
          </div>
        </div>
        <div className="row">
          <label className="col-sm-3 control-label text-right"> To Date</label>
          <div className="col-sm-3 add-margin">
            <input type="text" className="form-control toDate datepicker" name="toDate"
            onChange={(e)=>{handleChange(e.target.value,"toDate")}}/>
          </div>
        </div>
        <div className="row">
          <div className="text-center">
            <button type="button" className="btn btn-submit" onClick={(e)=>this.create()}>Search</button>&nbsp;&nbsp;
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
          </div>
        </div>
      </div>
    )
  }
}

ReactDOM.render(
  <CreateDepreciation />,
  document.getElementById('root')
);
