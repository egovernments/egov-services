class ShowPipeSize extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{sizeInMilimeter:"",code:"",
    sizeInInch:"",active:""}}

  }


  componentDidMount()
  {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
    try {
        var _PipeSize = commonApiPost("wcms-masters","pipesize","_search",{tenantId}).responseJSON["PipeSize"] || [];
    } catch(e) {
        var _PipeSize = [];
    }
    this.setState({
      list:_PipeSize
    });

  }


  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.list.length!=this.state.list.length) {

          $('#pipeSizeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                      'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }

  render() {
    let {list}=this.state;
    let {sizeInInch,sizeInMilimeter,code,active}=this.state.searchSet;
    var mode = getUrlVars()["type"];

    const renderAction=function(type,id){
      if (type==="Update") {

              return (
                      <a href={`app/create/create-pipe-size.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/create/create-pipe-size.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return list.map((item,index)=>
      {
            return (<tr key={index}>
                    <td><center>{index+1}</center></td>
                    <td data-label="code"><center>{item.code}</center></td>
                    <td data-label="sizeInMilimeter"><center>{item.sizeInMilimeter}</center></td>


                    <td data-label="active"><center>{item.active?"ACTIVE":"INACTIVE"}</center></td>
                    <td data-label="action"><center>
                    {renderAction(getUrlVars()["type"],item.id)}
                  </center>  </td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Pipe  Size </h3>
        <table id="pipeSizeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th><center>Sl No.</center></th>
                    <th><center>Code</center></th>
                    <th><center>H.S.C Pipe Size (mm)</center></th>
                    <th><center>Status</center></th>
                    <th><center>Action</center></th>

                </tr>
            </thead>

            <tbody id="pipeSizeTableResultBoday">
                {
                    renderBody()
                }
            </tbody>

        </table>
        <div className="text-center">
            <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
        </div>
      </div>
    );
  }
}


ReactDOM.render(
  <ShowPipeSize />,
  document.getElementById('root')
);
