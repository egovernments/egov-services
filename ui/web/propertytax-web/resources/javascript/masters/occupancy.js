class Occupancy extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
  },isSearchClicked:false,occupancyList:[],type:""}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.updateType=this.updateType.bind(this);

  }

  search(e)
  {
    e.preventDefault();
    // var type = getUrlVars()["id"];

    console.log(e);
    console.log(this.state.type);
      if(this.state.type==="Update")

        return (  window.location.href='app/create/create-occupancy.html?type=Update')
        //console.log(type);
      //
      //
      else


        return (  window.location.href='app/create/create-occupancy.html?type=View')



    //call api call
    //  var list=commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"] ||[];


    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

  }

  componentWillMount()
  {


  }



  componentDidMount()
  {

      this.setState({

        occupancyList: [{
                id: "cement",
                name: "Cement",

            },
            {
                id: "MUD",
                name: "MUD",

            },
            {
              id: "sw",
              name: "SW",
            },
            {
              id: "TILES",
              name: "TILES",

            },
            {
              id:"MARBEL",
              name:"MARBEL",
            }]


  })

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
  updateType(e, type){
    // console.log("hi");
    this.setState({
      type
    })
  }



  render() {
    let {handleChange,search,updateType}=this;
    let {isSearchClicked,list}=this.state;
    let {name}=this.state.searchSet;

    const renderOption=function(occupancyList)
    {
        if(occupancyList)
        {
            return occupancyList.map((item)=>
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
                                  {renderOption(this.state.occupancyList)}

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                </div>




                <div className="text-center">
                <button type="button" className="btn btn-danger" onClick={(e)=>{window.location.href='app/create/create-occupancy.html?type=Create'}}>Create</button>
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
  <Occupancy />,
  document.getElementById('root')
);
