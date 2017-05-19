class AppartmentSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:"",
        },isSearchClicked:false}
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
                            <label for="name">Select Appartment Type <span> * </span> </label>
                        </div>
                        <div className="col-sm-8">
                            <div className="styled-select">
                                <select id="name" name="name" value={name}
                  onChange={(e)=>{  handleChange(e,"name")}} required>

                                  <option value="">Select Appartment Type </option>
                                  {renderOption(this.state.name)}

                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                </div>



                                      
                    <div className="text-center">
                        <button type="submit" className="btn btn-submit">Search</button>
                        <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>


          </div>
          );
      }
}


ReactDOM.render(
  <AppartmentSearch />,
  document.getElementById('root')
);
