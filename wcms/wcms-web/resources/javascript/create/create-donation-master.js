class DonationMaster extends React.Component{
    constructor(props){
      super(props);
      this.state={list:[],donationSet:{
      "id": "",
      "department":"",
      "designation":"",
      "fromDate":"",
      "toDate":"",
     "tenantId":tenantId
    },
      departmentsList:[],designationList:[]}
      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);

}
    // componentWillMount()
    // {
    //   try {
    //     var assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [])), JSON.parse(localStorage.getItem("assignments_designation"))) : JSON.parse(localStorage.getItem("assignments_designation"));
    //   } catch (e) {
    //       console.log(e);
    //        var assignments_designation = [];
    //   }
    //   try {
    //     var assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
    //   } catch (e) {
    //       console.log(e);
    //     var  assignments_department = [];
    //   }
    //   this.setState({
    //     departmentsList: assignments_department,
    //     designationList: assignments_designation
    // })
    // }
    //

    handleChange(e,name){
      if(name === "active"){
      this.setState({
        donationSet:{
            ...this.state.donationSet,
            active: !this.state.donationSet.active

        }
      })
    }else{
      this.setState({
        donationSet:{
          ...this.state.donationSet,
          [name]:e.target.value
        }
      })
    }
  }




    close(){
        // widow.close();
        open(location, '_self').close();
    }



    componentDidMount(){
      if(window.opener && window.opener.document) {
         var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
         if(logo_ele && logo_ele[0]) {
           document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
         }
       }

       $('#fromDate, #toDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose:true

        });
       if(getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Position");

      var type=getUrlVars()["type"];
      var id=getUrlVars()["id"];

      if(getUrlVars()["type"]==="view")
      {
          $("input,select").prop("disabled", true);
        }

        if(type==="view"||type==="update")
        {
            this.setState({
              donationSet:getCommonMasterById("hr-masters","positions","Position",id).responseJSON["Position"][0]
            })
        }
    }




  addOrUpdate(e){
  
  }




    render(){

      let {handleChange,addOrUpdate,handleChangeTwoLevel,handleChangeThreeLevel}=this;
      let {department,designation,fromDate,toDate}=this.state.donationSet;
      let mode =getUrlVars()["type"];


      const renderOption=function(list)
      {
          if(list)
          {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.id}>
                          {item.name}
                    </option>)
              })
          }
      }
      const showActionButton=function() {
        if((!mode) ||mode==="update")
        {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
        }
      };


      return (<div>
        <h3>{ getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Donation Master</h3>
        <form  onSubmit={(e)=>{addOrUpdate(e,mode)}}>
        <fieldset>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Property Type <span> * </span>  </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                      <select id="department" name="department" value={department} >
                        <option>Select Property type</option>
                        {renderOption(this.state.departmentsList)}
                     </select>
                  </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Category <span> * </span> </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="designation" name="designation" value={designation}
                        >
                        <option>Select Category</option>
                        {renderOption(this.state.designationList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Usage Type <span> * </span> </label>
                  </div>
                  <div className="col-sm-6">
                  <div className="styled-select">
                      <select id="department" name="department" value={department}
                      >
                        <option>Select Usage Type</option>
                        {renderOption(this.state.departmentsList)}
                     </select>
                  </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Max H.S.C Pipe Size <span> * </span>  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="designation" name="designation" value={designation}
                        >
                        <option>Select From Below</option>
                        {renderOption(this.state.designationList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
        </div>

        <div className="row">

            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Min. Pipe Size <span> *</span>  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="designation" name="designation" value={designation}
                        >
                        <option>Select From Below</option>
                        {renderOption(this.state.designationList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="">Donation Amount <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                      <input type="number" name="name"  id= "name" required/>
                      </div>
                  </div>
                </div>
        </div>
        <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">From Date <span>*</span></label>
                        </div>
                        <div className="col-sm-6">
                        <div className="text-no-ui">
                        <span><i className="glyphicon glyphicon-calendar"></i></span>
                        <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                        onChange={(e)=>{handleChange(e,"fromDate")}} required/>

                        </div>
                    </div>
                  </div>
              </div>
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
          </div>            <div className="text-center">
            {showActionButton()} &nbsp;&nbsp;
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

        </div>
        </fieldset>
        </form>
      </div>);

    }
  }
  ReactDOM.render(
    <DonationMaster/>,
    document.getElementById('root')
  );
