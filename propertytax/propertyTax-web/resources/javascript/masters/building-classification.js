class BuildingClassification extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
    code:"",
    active:"",
    fromDate: "",
    toDate:"",
    description:""}
      }
     this.handleChange=this.handleChange.bind(this);
     this.addOrUpdate=this.addOrUpdate.bind(this);

  }

  handleChange(e,name)
      {
          this.setState({
              searchSet:{
                  ...this.state.searchSet,
                  [name]:e.target.value
              }
          })
      }

  close(){
      // widow.close();
      open(location, '_self').close();
  }


  componentDidMount(){
        var type=getUrlVars()["type"];
        var id=getUrlVars()["id"];

        if(getUrlVars()["type"]==="View")
        {
           $("input").prop("disabled", true);

          }


          // console.log($('#fromDate').length);
          $('#fromDate').datepicker({
              format: 'dd/mm/yyyy',
              autoclose:true

          });
          $('#fromDate').on("change", function(e) {
            var from = $('#fromDate').val();
            var to = $('#toDate').val();
            var dateParts = from.split("/");
            var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
            var date1 = new Date(newDateStr);

            var dateParts = to.split("/");
            var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
            var date2 = new Date(newDateStr);
            if (date1 > date2) {
                showError("From date must be before of End date");
                $('#fromDate').val("");
            }
            _this.setState({
                  searchSet: {
                      ..._this.state.searchSet,
                      "fromDate":$("#fromDate").val()
                  }
            })

            });

            $('#toDate').datepicker({
                format: 'dd/mm/yyyy',
                autoclose:true

            });
            $('#toDate').on("change", function(e) {
              var from = $('#fromDate').val();
              var to = $('#toDate').val();
              var dateParts = from.split("/");
              var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
              var date1 = new Date(newDateStr);

              var dateParts = to.split("/");
              var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
              var date2 = new Date(newDateStr);
              if (date1 > date2) {
                  showError("End date must be after of From date");
                  $('#toDate').val("");
              }

              _this.setState({
                    searchSet: {
                        ..._this.state.searchSet,
                        "toDate":$("#toDate").val()
                    }

              })
        });

      }


      addOrUpdate(e,mode){
        e.preventDefault();
         console.log(this.state.searchSet);
        // console.log(mode);
        if (mode==="update") {
            console.log("update");
        } else {

          this.setState({searchSet:{
            name:"",code:"",description:"",active:"",fromDate:"",toDate:""

          } })
        }
      }




  render() {
    let {handleChange,addOrUpdate}=this;
    let {list}=this.state;
    let {name,code,description,active,fromDate,toDate}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if((!mode)||mode==="Update")
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
    }





    return (
    <div>
   <h3>Add Building Type</h3>
    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>

        <div className="row">
            <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="">Name  <span> * </span></label>
                </div>
                <div className="col-sm-6">
                    <input  type="text" id="name" name="name" value={name} required
                    onChange={(e)=>{  handleChange(e,"name")}}/>

                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for=""> Code <span> * </span></label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" id="code" name="code" value={code} required
                            onChange={(e)=>{  handleChange(e,"code")}}/>
                      </div>
                  </div>
              </div>

          </div>

    <div className="row">
      <div className="col-sm-6">
        <div className="row">
            <div className="col-sm-6 label-text">
              <label for="">Description <span> * </span></label>
            </div>
            <div className="col-sm-6">
                <input  type="text" id="description" name="description" value={description} required
                onChange={(e)=>{  handleChange(e,"description")}}/>

              </div>
            </div>
        </div>
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="">From Date <span>*</span></label>
                </div>
                <div className="col-sm-6">
                <div className="text-no-ui">
                <span><i className="glyphicon glyphicon-calendar"></i></span>
                <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                onChange={(e)=>{handleChange(e,"fromDate")}}required/>

                </div>
            </div>
          </div>
          </div>

      </div>
                 <div className="row">
                 <div className="col-sm-6">
                     <div className="row">
                         <div className="col-sm-6 label-text">
                           <label for="">To Date <span>*</span> </label>
                         </div>
                         <div className="col-sm-6">
                         <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text"  id="toDate" name="toDate" value={toDate}
                            onChange={(e)=>{
                              handleChange(e,"toDate")}}required/>
                              </div>
                              </div>
                              </div>
                              </div>
                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                <label htmlFor="">Active</label>
                              </div>
                              <div className="col-sm-6">
                              <input type="checkbox" name="active" id="active" value={active} onChange={(e)=>{
                          handleChange(e,"active")}} />
                              </div>
                          </div>
                          </div>
                    </div>




                    <div className="text-center">
                    {showActionButton()} &nbsp;&nbsp;
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>



          </div>
          );
      }
}


ReactDOM.render(
  <BuildingClassification />,
  document.getElementById('root')
);
//
// <button type="submit" className="btn btn-submit">Add</button>
// &nbsp;
