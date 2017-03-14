class PositionMaster extends React.Component{
    constructor(props){
      super(props);
      this.state={positionSet:{
      departmentCode:"",
      designationCode:"",
      name:"",
      isPostOutsourced:""},
      departments:[],designation:[]}
      this.handleChange=this.handleChange.bind(this);
      this.addOrUpdate=this.addOrUpdate.bind(this);
}
    componentWillMount()
    {
      this.setState({
        departments:[{
                id: 1,
                name: "Juniour Engineer",
                description: "",
                orderno: "1",
                active: true
            },
            {
                id: 2,
                name: "Assistance Engineer",
                description: "",
                orderno: "1",
                active: true
            }],
        designation:[{
                id: 1,
                name: "Juniour Engineer",
                description: "",
                orderno: "1",
                active: true
            },
            {
                id: 2,
                name: "Assistance Engineer",
                description: "",
                orderno: "1",
                active: true
            }]
      })
    }

    handleChange(e,name){
      this.setState({
        positionSet:{
          ...this.state.positionSet,
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

      if(getUrlVars()["type"]==="view")
      {
        for (var variable in this.state.positionSet)
          document.getElementById(variable).disabled = true;
        }

        if(type==="view"||type==="update")
        {
            console.log("fired");
            console.log(getCommonMasterById("hr-masters","positions","Position",id).responseJSON["Position"][0]);
            this.setState({
              positionSet:getCommonMasterById("hr-masters","positions","Position",id).responseJSON["Position"][0]
            })
        }

    }



    addOrUpdate(e,mode){
      e.preventDefault();
      console.log({name:this.state.positionSet.name,deptdesig:{designation:this.state.positionSet.designationCode,department:this.state.positionSet.departmentCode},isPostOutsourced:this.state.positionSet.isPostOutsourced});
      if (mode==="update") {
          console.log("update");
      } else {
      this.setState({positionSet:{
      departmentCode:"",
      designationCode:"",
      name:"",
      isPostOutsourced:""},departments:"",designation:""})
    }
  }

    render(){

      let {handleChange,addOrUpdate}=this;
      let {departments,designation,name,isPostOutsourced,designationCode,departmentCode}=this.state.positionSet;
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
        <form  onSubmit={(e)=>{addOrUpdate(e,mode)}}>
        <fieldset>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Department<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="departmentCode" name="departmentCode" value={departmentCode} onChange={(e)=>{
                        handleChange(e,"departmentCode")
                    }} required>
                      <option>Select Department</option>
                      {renderOption(this.state.departments)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                        <label for="">Designation<span>*</span> </label>
                    </div>
                    <div className="col-sm-6">
                        <div className="styled-select">
                        <select id="designationCode" name="designationCode" value={designationCode} onChange={(e)=>{
                            handleChange(e,"designationCode")
                        }}required>
                        <option>Select Designation</option>
                        {renderOption(this.state.designation)}
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
                      <label for="">Position <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                  <input type="text" name="name" value={name} id= "name" onChange={(e)=>{
                      handleChange(e,"name")}}required/>
                  </div>
              </div>
            </div>
        </div>
        <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Outsourced post </label>
                  </div>
                  <div className="col-sm-6">
                        <label className="radio-inline radioUi">
                          <input type="radio" name="isPostOutsourced" id="isPostOutsourced" value="yes"  onChange={(e)=>{
                              handleChange(e,"isPostOutsourced")}}/> Yes
                        </label>
                        <label className="radio-inline radioUi">
                          <input type="radio" name="isPostOutsourced" id="isPostOutsourced" value="No" onChange={(e)=>{
                              handleChange(e,"isPostOutsourced")}}/> No
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
      </div>);

    }
  }
  ReactDOM.render(
    <PositionMaster/>,
    document.getElementById('root')
  );
