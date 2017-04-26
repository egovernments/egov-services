class CreateFloorType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:""}
      }
    // this.handleChange=this.handleChange.bind(this);
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
          for (var variable in this.state.gradeSet)
            document.getElementById(variable).disabled = true;
          }


        if(type==="view"||type==="update")
        {
            this.setState({
              // gradeSet:getCommonMasterById("hr-masters","grades","Grade",id).responseJSON["Grade"][0]
            })
        }



      }



  render() {
    let {handleChange}=this;
    let {list}=this.state;
    let {name}=this.state.searchSet;
    let mode=getUrlVars()["type"];

    const showActionButton=function() {
      if(mode==="create")
        return (<button type="submit" className="btn btn-submit">Add</button>);

     else if(mode==="update")
          return (<button type="submit" className="btn btn-submit">Update</button>);


    }




    return (
    <div>

          <form>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="name">Name <span> * </span> </label>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                              <input type="text" name="name" id="name" />
                            </div>
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
  <CreateFloorType />,
  document.getElementById('root')
);
//
// <button type="submit" className="btn btn-submit">Add</button>
// &nbsp;
