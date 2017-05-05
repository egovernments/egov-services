class CreateWallType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:""},selectValue:""
      }
     this.handleChange=this.handleChange.bind(this);
     this.addOrUpdate=this.addOrUpdate.bind(this);

  }
  handleChange(e,name)
      {
        console.log(name);
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
        var val=getUrlVars()["value"];
        this.setState({
            selectValue : val
        })

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
            name:"",

          } })
        }
      }




  render() {
    let {handleChange,addOrUpdate}=this;
    let {list,selectValue}=this.state;
    let {name}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if(mode==="Create")
        return (<button type="submit" className="btn btn-submit">Add</button>);

     else if(mode==="Update")
          return (<button type="submit" className="btn btn-submit">Update</button>);


    }

    return (
    <div>
        <h3> {mode} Wall Type </h3>
          <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="name">Name <span> * </span> </label>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                              <input type="text" name="name" id="name" value={selectValue}
                onChange={(e)=>{  handleChange(e,"name")}} />
                            </div>
                        </div>
                    </div>
                </div>
                </div>




                    <div className="text-center">
                      {showActionButton()} &nbsp;&nbsp;
                    &nbsp;
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>



          </div>
          );
      }
}


ReactDOM.render(
  <CreateWallType />,
  document.getElementById('root')
);
