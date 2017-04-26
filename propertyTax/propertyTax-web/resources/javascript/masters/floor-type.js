class FloorType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
        },isSearchClicked:false,FloorType:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
  }

  search(e)
  {
    e.preventDefault();
    //call api call
    //  var list=commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"] ||[];
      this.setState({
      isSearchClicked:true,
      list
    })

    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

  }

  componentWillMount()
  {


  }



  componentDidMount()
  {

      this.setState({

        FloorType: [{
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



  render() {
    let {handleChange,search,updateTable,handleSelectChange}=this;
    let {isSearchClicked,list}=this.state;
    let {name}=this.state.searchSet;

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
                  onChange={(e)=>{  handleChange(e,"name")}} >

                                  <option value="">Choose name</option>
                                  {renderOption(this.state.FloorType)}

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                </div>




                    <div className="text-center">
                    <button type="submit" className="btn btn-submit" onClick={(e)=>{window.location.href='app/create/create-floor-type.html'}}>Create</button>
                    &nbsp;
                    <button type="submit" className="btn btn-submit">Update</button>
                    &nbsp;
                        <button type="submit" className="btn btn-submit">View</button>
                        &nbsp;
                        <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>


          </div>
          );
      }
}


ReactDOM.render(
  <FloorType />,
  document.getElementById('root')
);
