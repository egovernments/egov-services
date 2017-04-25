class AssetSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
        tenantId},isSearchClicked:false}
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
      name,


    })
  }

  // componentDidUpdate(prevProps, prevState)
  // {
  //     if (prevState.list.length!=this.state.list.length) {
  //         // $('#agreementTable').DataTable().draw();
  //         // alert(prevState.list.length);
  //         // alert(this.state.list.length);
  //         // alert('updated');
  //         $('#agreementTable').DataTable({
  //           dom: 'Bfrtip',
  //           buttons: [
  //                    'copy', 'csv', 'excel', 'pdf', 'print'
  //            ],
  //            ordering: false,
  //            bDestroy: true
  //         });
  //     }
      // else {
      //   $('#agreementTable').DataTable({
      //     dom: 'Bfrtip',
      //     buttons: [
      //              'copy', 'csv', 'excel', 'pdf', 'print'
      //      ],
      //      ordering: false
      //   });
      // }
  // }

  handleChange(e,name)
  {

      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })

  }

  handleSelectChange(type,id,category)
  {
    console.log(type);
    console.log(category);
    if (type === "create") {
                  window.open("app/agreements/new.html?type="+category+"&assetId="+id, "fs", "fullscreen=yes")
             }

    // window.open("app/agreements/new.html?type="+category+"&assetId="+id, "fs", "fullscreen=yes")
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
    const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="agreementTable" className="table table-bordered">
                <thead>
                <tr>
                    <th>Sl No </th>
                    <th>Asset Category </th>
                    <th>Asset Name </th>
                    <th>Asset Code </th>
                    <th>Election Ward No </th>
                    <th>Action </th>
                  </tr>
                </thead>

                <tbody id="agreementSearchResultTableBody">
                    {
                        renderBody()
                    }
                </tbody>
            </table>

          )


      }

    }
    const renderBody=function()
    {
      if(list.length>0)
      {
        return list.map((item,index)=>
        {
              return (<tr key={index}>
                <td>{index+1}</td>
                                  <td>{item.assetCategory.name}</td>
                                  <td>{item.name}</td>
                                  <td>{item.code}</td>
                                  <td>{item.locationDetails.electionWard}</td>
                                  <td>
                                      <div className="styled-select">
                                          <select id="myOptions" onChange={(e)=>{
                                            handleSelectChange(e.target.value,item.id,item.assetCategory.name)
                                          }}>
                                              <option value="">Select Action</option>
                                              <option value="create">Create</option>

                                          </select>
                                      </div>
                                  </td>

                  </tr>
              );

        })
      }
      else {
          return (
              <tr>
                  <td colSpan="6">No records</td>
              </tr>
          )
      }


    }
    const disbaled=function(type) {
        if (type==="view") {
              return "ture";
        } else {
            return "false";
        }
    }

    return (
    <div>
            <form onSubmit={(e)=>{search(e)}}>
            <div className="row">
                <div className="col-sm-9">
                    <div className="row">
                        <div className="col-sm-4 label-text">
                            <label for="name">Slab Name <span> * </span></label>
                        </div>
                        <div className="col-sm-8">
                            <div className="styled-select">
                                <select id="name" name="name" value={name}
                  onChange={(e)=>{  handleChange(e,"name")}}>

                                  <option value="">Choose name</option>
                                  {renderOption(this.state.name)}

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                </div>



                                      <br/>
                    <div className="text-center">
                        <button type="submit" className="btn btn-submit">Search</button>
                        <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>

            <div className="table-cont" id="table">
                {showTable()}

            </div>

          </div>
          );
      }
}


ReactDOM.render(
  <AssetSearch />,
  document.getElementById('root')
);
