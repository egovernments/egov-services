function getUrlVars() {
    var vars = [],
        hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}


class Community extends React.Component{

    constructor(props){
      super(props);
      this.state={communitySet:{
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
        for (var variable in this.state.communitySet)
          document.getElementById(variable).disabled = true;
        }
    }

    handleChange(e,name)
    {
        this.setState({
            communitySet:{
                ...this.state.communitySet,
                [name]:e.target.value
            }
        })

    }

    close(){
        // widow.close();
        open(location, '_self').close();
    }
    addOrUpdate(e,mode){

        console.log(this.state.communitySet);
        e.preventDefault();
        if (mode==="update") {
            console.log("update");
        } else {
        this.setState({communitySet:{
            name:"",
            description:"",
            active:""
        } })
    }
  }

  render(){

    let {handleChange,addOrUpdate}=this;
    let {name,description,active}=this.state.communitySet;
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
                    <label for=""> Community Name <span> *</span></label>
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
  <Community />,
  document.getElementById('root')
);
