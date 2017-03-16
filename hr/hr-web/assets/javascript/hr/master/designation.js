class Designation extends React.Component{
  constructor(props){
    super(props);
    this.state={designationSet:{name:"",
    code:"",description:"", chartOfAccounts:"",active:""},
    descriptionList:[]  }

      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);
    }

    componentWillMount(){
      this.setState({
      descriptionList:commonApiPost("hr-masters","designations","_search",{tenantId}).responseJSON["Designation"]
    })

    }
      handleChange(e,name){
        this.setState({
          designationSet:{
            ...this.state.designationSet,
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

        // if(getUrlVars()["type"]==="view")
        // {
        //   for (var variable in this.state.designationSet)
        //     document.getElementById(variable).disabled = true;
        //   }

          if(type==="update"||type==="view")
          {
               console.log("fired");
              console.log(getCommonMasterById("hr-masters","designations","Designation",id).responseJSON["Designation"][0]);
              this.setState({
                designationSet:getCommonMasterById("hr-masters","designations","Designation",id).responseJSON["Designation"][0]
              })
          }
      }

      addOrUpdate(e,mode){
           console.log(this.state.designationSet);
          e.preventDefault();
          // console.log(mode);
          if (mode==="update") {
              console.log("update");
          } else {
            this.setState({designationSet:{
            name:"",
            code:"",
            description:"",
            chartOfAccounts:"",
            active:""},
            descriptionList:""  })
          }
        }


      render(){
        let {handleChange,addOrUpdate}=this;
        let mode=getUrlVars()["type"];
        let {name,code,description,chartOfAccounts,active}=this.state.designationSet;
        const renderOption=function(list)
        {
          if(list)
          {
            return list.map((item)=>
            {
                return (<option key={item.description} value={item.description}>
                        {item.description}
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


        return(
        <div>
          <form  onSubmit={(e)=>{addOrUpdate(e,mode)}}>
          <fieldset>
          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Name<span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <input type="text" name="name" value={name} id="name" onChange={(e)=>{
                          handleChange(e,"name")
                      }} required/>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                          <label for="">Code <span>*</span></label>
                      </div>
                      <div className="col-sm-6">
                      <input type="text" name="code" value={code} id="code"
                        onChange={(e)=>{  handleChange(e,"code")}}required/>
                      </div>
                  </div>
              </div>
          </div>
        <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Description <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                        <div className="styled-select">
                          <select id="description" name="description" value={description} required="true"
                            onChange={(e)=>{  handleChange(e,"description")}}>

                              <option>Select description</option>
                              {renderOption(this.state.descriptionList)}
                         </select>
                        </div>
                    </div>
                </div>
              </div>
          </div>

          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 col-sm-offset-6">
                          <label className="radioUi">
                            <input type="checkbox" name="active" id="active" value="true" onChange={(e)=>{
                                handleChange(e,"active")}}required/> Active
                          </label>
                    </div>
                </div>
              </div>
          </div>

          <div className="text-center">
                {showActionButton()}
                <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
        </div>
        </fieldset>
        </form>
    </div>
        );
    }

  }
  ReactDOM.render(
    <Designation />,
    document.getElementById('root')
  );
