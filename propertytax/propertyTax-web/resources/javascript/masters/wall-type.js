class WallType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:""},isSearchClicked:false,type:"",wallTypeList:[],selectValue:""}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.updateType=this.updateType.bind(this);

  }

  search(e)
  {
    e.preventDefault();

   if(this.state.type==="Update")

        return (  window.location.href='app/create/create-wall-type.html?type=Update&&value='+this.state.selectValue)

      else
      return (  window.location.href='app/create/create-wall-type.html?type=View&&value='+this.state.selectValue)
    }

  componentWillMount()
  {


  }



  componentDidMount()
  {

      this.setState({

        wallTypeList: [{
                id: "Bricks With Cement",
                name: "Bricks With Cement",

            },
            {
                id: "Bricks With Mud",
                name: "Bricks With Mud",

            },
            {
              id: "Mud",
              name: "MUD",
            },
            {
              id: "Bcw",
              name: "BCW",

            },
            {
              id:"bmc",
              name:"BMC",
            }]


  })


  }


  handleChange(e,name)
  {
      this.setState({
          selectValue : e.target.value
      })
      console.log(e.target.value);
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })

  }
  updateType(e, type){

  //  this.setState({selectValue:e.target.value});
    console.log(e.target.value)
    // console.log("hi");
    this.setState({
      type
    })
  }




  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    let {handleChange,search,updateType}=this;
    let {isSearchClicked,wallTypeList}=this.state;
    let {name}=this.state.searchSet;

    const renderOption=function(wallTypeList)
    {
        if(wallTypeList)
        {
            return wallTypeList.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }


    return (
    <div>
            <form onSubmit={(e)=>{search(e)}}>
            <div className="row">
                <div className="col-sm-9">
                    <div className="row">
                        <div className="col-sm-4 label-text">
                            <label for="name">Name <span> * </span></label>
                        </div>
                        <div className="col-sm-8">
                            <div className="styled-select">
                                <select id="name" name="name" value={name}
                                  onChange={(e)=>{  handleChange(e,"name")}} required>

                                  <option value="">Choose name</option>
                                  {renderOption(this.state.wallTypeList)}

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                </div>



                                      <br/>
                                      <div className="text-center">
                                      <button type="button" className="btn btn-danger" onClick={(e)=>{window.location.href='app/create/create-wall-type.html?type=Create'}}>Create</button>
                                      &nbsp;
                                      <button type="submit" className="btn btn-submit" id="Update" value="Update" onClick={(e)=>{  updateType(e,"Update")}}>Update</button>
                                      &nbsp;

                                          <button type="submit" className="btn btn-submit" value="View" onClick={(e)=>{  updateType(e,"View")}} >View</button>
                                          &nbsp;
                                          <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                                      </div>
                                      </form>


          </div>
          );
      }
}


ReactDOM.render(
  <WallType />,
  document.getElementById('root')
);
