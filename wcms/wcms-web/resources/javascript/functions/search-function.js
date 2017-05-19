
class SearchFunction extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "list": [],
        "searchSet": {
            "name": "",
            "code": "",
            "level": "",
            "active":"",
            "parentId":"",
            "isParent":"",
            tenantId
        },
        "assetCategories": [],
        "electionwards": [],
        "localityList":[],

        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
  }


  componentDidMount() {
     this.setState({
         ...this.state,
        assetCategories:commonApiPost("asset-services","assetCategories","_search",{tenantId}).responseJSON["AssetCategory"],
        electionwards: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
            boundaryTypeName: "WARD",
            hierarchyTypeName: "ADMINISTRATION",
            tenantId
        }).responseJSON["Boundary"] || [],
        localityList: commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", {
            boundaryTypeName: "LOCALITY",
            hierarchyTypeName: "LOCATION",
            tenantId
        }).responseJSON["Boundary"] || []

     });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {
          // $('#agreementTable').DataTable().draw();
          // alert(prevState.list.length);
          // alert(this.state.list.length);
          // alert('updated');
          $('#agreementTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
      else {
        $('#agreementTable').DataTable({
          dom: 'Bfrtip',
          buttons: [
                   'copy', 'csv', 'excel', 'pdf', 'print'
           ],
           ordering: false
        });
      }
  }


  handleChange(e, name)
  {
      this.setState({
          ...this.state,
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  closeWindow ()
  {
      open(location, '_self').close();
  }

  search(e)
  {
    e.preventDefault();
    //call api call
    var list=commonApiPost("asset-services","assets","_search",this.state.searchSet).responseJSON["Assets"];
    this.setState({
      isSearchClicked:true,
      list
    })

    // $('#agreementTable').DataTable().draw();
    // console.log($('#agreementTable').length);

  }


  render() {
    let {handleChange, search, closeWindow} = this;
    let {assetCategories, electionwards,list,localityList,isSearchClicked} = this.state;
    let {name,code,level,active,parentId,isParent} = this.state.searchSet;

    const renderOptions = function(list)
    {
        if(list)
        {
            if (list.length) {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.id}>
                          {item.name}
                    </option>)
              })

            } else {
              return Object.keys(list).map((k, index)=>
              {
                return (<option key={index} value={k}>
                        {list[k]}
                  </option>)

               })
            }

        }
    }


    const renderBody=function()
    {
      if(list)
      {
        return list.map((item,index)=>
        {
              return (<tr key={index}>


                                  <td>{item.name}</td>
                                  <td>{item.code}</td>
                                  <td>{item.level}</td>
                                  <td>{item.active}</td>
                                  <td>{item.parent}</td>
                                  <td>{item.isParent}</td>

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

    const showTable = () => {
        if(isSearchClicked) {
            return (
                <div className="form-section" >
                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>
                    <div className="view-table">
                    <table>
                    <thead>
                                             <tr>
                                                 <th > Name</th>
                                                 <th >Code</th>
                                                 <th >Level</th>
                                                 <th >Actiive</th>
                                                 <th >Parent Type</th>
                                                 <th > Is Parent</th>
                                              </tr>


                                             </thead>

                            <tbody id="agreementSearchResultTableBody">
                                {
                                    renderBody()
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            )
        }
    }

    return (
        <div>
            <div className="form-section">
            <h3 className="pull-left">Search Function </h3>
            <div className="clearfix"></div>

                <form onSubmit={(e)=>{search(e)}}>
                    <fieldset>
                    <div className="row">
                    <div className="col-sm-6">
                    <div className="row">
                    <div className="col-sm-6 label-text">
                            <label for="name">Name </label>
                    </div>
                    <div className="col-sm-6">
                            <input type="text" id="name" name="name" value={name}
                            onChange={(e)=>{handleChange(e,"name")}}/>
                    </div>
                    </div>
                    </div>
                    <div className="col-sm-6">
                    <div className="row">
                    <div className="col-sm-6 label-text">
                            <label for="code">Code </label>
                    </div>
                    <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                            onChange={(e)=>{handleChange(e,"code")}}/>
                    </div>
                    </div>
                    </div>
                    </div>

                    <div className="row">
                    <div className="col-sm-6">
                    <div className="row">
                    <div className="col-sm-6 label-text">
                            <label for="level">Level </label>
                    </div>
                    <div className="col-sm-6">
                            <input type="number" id="level" name="level" value={level}
                            onChange={(e)=>{handleChange(e,"level")}}/>
                    </div>
                    </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label htmlFor="">Active</label>
                            </div>
                            <div className="col-sm-6">
                            <input type="checkbox" name="active" id="active" value={active} onChange={(e)=>{
                        handleChange(e,"active")}} />
                            </div>
                        </div>
                        </div>
                        </div>
                        <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                  <label htmlFor="">Is Parent</label>
                                </div>
                                <div className="col-sm-6">
                                <input type="checkbox" name="isParent" id="isParent" value={isParent} onChange={(e)=>{
                            handleChange(e,"isParent")}} />
                                </div>
                            </div>
                            </div>

                        <div className="col-sm-6">
                        <div className="row">
                        <div className="col-sm-6 label-text">
                              <label for="parent"> Parent Type  </label>
                        </div>
                        <div className="col-sm-6">
                        <div className="styled-select">
                        <select id="parentID" name="parentId"  value={parentId} onChange={(e)=>{
                        handleChange(e,"parentId")}}>
                            <option value="">Select Parent Category</option>
                            {renderOptions(this.state.parentIds)}
                          </select>
                        </div>
                        </div>
                        </div>
                        </div>
                                          </div>



                        <div className="text-center">
                        <button type="submit" className="btn btn-submit">Search</button>  &nbsp;&nbsp;
                            <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>


                        </div>
                    </fieldset>
                </form>
            </div>
            <div className="table-cont" id="table">
                {showTable()}

            </div>
            </div>
    );
  }
}






ReactDOM.render(
  <SearchFunction />,
  document.getElementById('root')
);
