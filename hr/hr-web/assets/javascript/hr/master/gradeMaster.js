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

class GradeMaster extends React.Component{

    constructor(props){
      super(props);
      this.state={gradeSet:{
          name:"",
          description:"",
          orderno:"",
          active:""
      }   }
        this.handleChange=this.handleChange.bind(this);
        this.addOrUpdate=this.addOrUpdate.bind(this);
    }


    handleChange(e,name)
    {
        this.setState({
            gradeSet:{
                ...this.state.gradeSet,
                [name]:e.target.value
            }
        })
    }


    componentDidMount(){
      if(getUrlVars()["type"]==="view")
      {
        for (var variable in this.state.gradeSet)
          document.getElementById(variable).disabled = true;
        }
    }

    close(){
        // widow.close();
        open(location, '_self').close();
    }


    addOrUpdate(e,mode){
         console.log(this.state.gradeSet);
        e.preventDefault();
        // console.log(mode);
        if (mode==="update") {
            console.log("update");
        } else {

          this.setState({gradeSet:{
            name:"",
            description:"",
            orderno:"",
            active:""
          } })
        }
      }



  render(){

    let {handleChange,addOrUpdate}=this;
    let mode=getUrlVars()["type"];
    let {name,description,orderno,active}=this.state.gradeSet;
    let {isClicked}=this.state;



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
                    <label for="Grade">Grade Name <span>* </span></label>
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
                            <input type="text" name="description" id="description" value={description}
                                onChange={(e)=>{handleChange(e,"description")}} required/>
                            </div>
                      </div>
                </div>
            </div>


    <div className="row">
      <div className="col-sm-6">
          <div className="row">
              <div className="col-sm-6 label-text">
                  <label for="">Order No <span>* </span></label>
              </div>
              <div className="col-sm-6">
                  <input type="number" name="orderno" id="orderno"  value={orderno}
                      onChange={(e)=>{ handleChange(e,"orderno")}}required/>
              </div>
          </div>
      </div>
    </div>
    <div className="row">
      <div className="col-sm-6">
          <div className="row">
              <div className="col-sm-6 col-sm-offset-6">
                    <label className="radioUi">
                      <input type="checkbox" name="active" id="active" value="true"
                          onChange={(e)=>{ handleChange(e,"active")}}required/> Active
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
  <GradeMaster />,
  document.getElementById('root')
);
