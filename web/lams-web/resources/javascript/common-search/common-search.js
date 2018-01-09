class CommonSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      agreementNumber:"",
    isSearchClicked:false
  }
     this.handleChange=this.handleChange.bind(this);
     this.search=this.search.bind(this);
  }

  search(e) {
    var _this = this;
    var agreementNumber = _this.state.agreementNumber;
    var action = getUrlVars()["action"].toUpperCase();
    var assetId = "";
    var agreement = "";
    var assetType = "";
    e.preventDefault();
    //call api call
    var response = $.ajax({
        url: baseUrl + "/lams-services/agreements/_commonsearch?tenantId=" + tenantId + "&agreementNumber=" +agreementNumber+"&action="+action,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({
            RequestInfo: requestInfo,
            //Agreement: _agreement
        }),
        async: false,
        headers: {
            'auth-token': authToken
        },
        contentType: 'application/json'
    });
    console.log(JSON.stringify(response));
    //var agreement = response
    if (response["status"] === 200) {
          agreement = response.responseJSON.Agreements[0];
          assetId=agreement.asset.id;
          assetType=agreement.asset.assetCategory.name;
        if (typeof(response["responseJSON"]["Error"]) != "undefined") {
            showError(response["responseJSON"]["Error"]["message"]);
        } else {

            if(window.opener){
              window.opener.location.reload();
              if(action.toLowerCase()==='renewal'){
            window.location.href = "app/renewal/create-renewal.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='cancellation'){
            window.location.href = "app/cancellation/create-cancellation.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='eviction'){
            window.location.href = "app/eviction/create-eviction.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='objection'){
            window.location.href = "app/objection/create-objection.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='judgement'){
            window.location.href = "app/judgement/create-judgement.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='remission'){
            window.location.href = "app/remission/create-remission.html?&agreementNumber=" +agreementNumber+"&tenantId="+tenantId;
          }else if(action.toLowerCase()==='modify'){
            window.location.href = "app/dataentry/data-entry.html?type="+assetType+"&assetId="+assetId+"&agreementNumber="+agreementNumber+"&tenantId="+tenantId;
          }
           }
        }

    } else if(response["responseJSON"] && response["responseJSON"].Error) {
        showError(response["responseJSON"].Error.description);

    } else {
      var err = response["responseJSON"].error.message;
      showError(err);
    }

  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#common-search-title').text(titleCase(getUrlVars()["action"]) + " Of Agreement" );
    var action = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    var count = 5, _state = {}, _this = this;

}

  handleChange(e,name) {
      this.setState({
            [name]:e.target.value
      })
  }

  close(){
      open(location, '_self').close();
  }

  render() {

    let {handleChange,search}=this;
    let {isSearchClicked,agreementNumber}=this.state;
    return (
      <div>
        <h3>Search Agreement for {titleCase(getUrlVars()["action"])}</h3>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>

          <div className="row">
            <div className="col-sm-7">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Agreement Number <span>*</span>
                      </label>
                    </div>
                    <div className="col-sm-6">
                        <input type="text" name="agreementNumber" id="agreementNumber" onChange={(e)=>{
                            handleChange(e,"agreementNumber")
                        }}  required/>

                    </div>
                </div>
              </div>
        </div>

          <div className="text-right text-danger">
                          Note: Agreement Number is mandatory.
                    </div>
            <div className="text-center">
              <div className="row">
              <button id="sub" type="submit"  className="btn btn-submit">Search</button>&nbsp;&nbsp;
              <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
              </div>
            </div>
          </fieldset>
          </form>
          <br/>
      </div>
    );
  }
}

ReactDOM.render(
  <CommonSearch />,
  document.getElementById('root')
);
