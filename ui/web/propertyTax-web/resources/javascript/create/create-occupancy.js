class CreateOccupancy extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
    code:"",
    factor:""}
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


        if(type==="View"||type==="Update")
        {
            this.setState({
              // gradeSet:getCommonMasterById("hr-masters","grades","Grade",id).responseJSON["Grade"][0]
            })
        }



      }


      addOrUpdate(e,mode){
        e.preventDefault();
         console.log(this.state.searchSet);
        // console.log(mode);
        if (mode==="update") {
            console.log("update");
        } else {

          this.setState({searchSet:{
            name:"",code:"",factor:""

          } })
        }
      }




  render() {
    let {handleChange,addOrUpdate}=this;
    let {list}=this.state;
    let {name,code,factor}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if(mode==="Create")
        return (<button type="submit" className="btn btn-submit">Add</button>);

     else if(mode==="Update")
          return (<button type="submit" className="btn btn-submit">Update</button>);


    }




    return (
    <div>
    <h3>{mode} Occupancy </h3>
    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>

    <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> Code </label>
                  </div>
                  <div className="col-sm-6">
                      <input type="text" id="code" name="code" value={code}
                        onChange={(e)=>{  handleChange(e,"code")}}/>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Name</label>
                    </div>
                    <div className="col-sm-6">
                        <input  type="text" id="name" name="name" value={name}
                        onChange={(e)=>{  handleChange(e,"name")}}/>

                      </div>
                    </div>
                  </div>
              </div>
                 <div className="row">
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for=""> Factor Value </label>
                            </div>
                            <div className="col-sm-6">
                                <input type="number" id="factor" name="factor" value={factor}
                                  onChange={(e)=>{  handleChange(e,"factor")}}/>
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
  <CreateOccupancy />,
  document.getElementById('root')
);
//
// <button type="submit" className="btn btn-submit">Add</button>
// &nbsp;
