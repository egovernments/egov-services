class Category extends React.Component{

    constructor(props){
      super(props);
      this.state={categorySet:{
          name:"",
          description:"",
          active:""
      }    }
        this.handleChange=this.handleChange.bind(this);
        this.addOrUpdate=this.addOrUpdate.bind(this);
    }


    componentDidMount(){
      if(getUrlVars()["type"]==="view")
      {
        for (var variable in this.state.categorySet)
          document.getElementById(variable).disabled = true;
        }
    }

    handleChange(e,name)
    {
        this.setState({
            categorySet:{
                ...this.state.categorySet,
                [name]:e.target.value
            }
        })

    }

    close(){
        // widow.close();
        open(location, '_self').close();
    }
    addOrUpdate(e,mode){

        console.log(this.state.categorySet);
        e.preventDefault();
        if (mode==="update") {
            console.log("update");
        } else {
        this.setState({categorySet:{
            name:"",
            description:"",
            active:""
        } })
    }
  }

  render(){

    let {handleChange,addOrUpdate}=this;
    let {name,description,active}=this.state.categorySet;
      let mode=getUrlVars()["type"];


      const showActionButton=function() {
        if((!mode) ||mode==="update")
        {
          return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
        }
      };

    return (<div>
      <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
      <fieldset>
      <div className="row">
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                    <label for=""> Category Name <span> *</span></label>
                </div>
                  <div className="col-sm-6">
                    <input type="text" name="name" id="name" value={name}
                        onChange={(e)=>{handleChange(e,"name")}} required/>
                  </div>
              </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                      <label for="">Description<span> *</span> </label>
                      </div>
                          <div className="col-sm-6">
                          <input type="text" name="description" id="description" value={description} onChange={(e)=>{
                              handleChange(e,"description")}} required/>
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
  <Category />,
  document.getElementById('root')
);
