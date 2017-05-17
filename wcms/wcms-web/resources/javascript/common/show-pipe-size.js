
class ShowPipeSize extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{inputmm:"",
    code:"",inputinc:"",active:""}}

  }


  componentDidMount()
  {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
    // try {
    //     var _UsageType = commonApiPost("wcms-masters","usagetype","_search",{tenantId}).responseJSON["UsageType"] || [];
    // } catch(e) {
    //     var _UsageType = [];
    // }
    // this.setState({
    //   list:_UsageType
    // });

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
    let {inputinc,inputmm,code,active}=this.state.searchSet;
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
                    <td>{index+1}</td>
                    <td data-label="inputmm">{item.inputmm}</td>
                    <td data-label="code">{item.code}</td>
                    <td data-label="inputinc">{item.inputinc}</td>
                    <td data-label="active">{item.active?"true":"false"}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Pipe  Size </h3>
        <table id="pipeSizeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Sl No.</th>
                    <th>H.S.C Pipe Size (mm)</th>
                    <th>H.S.C Pipe Size(inch)</th>
                    <th>Code</th>
                    <th>Active</th>
                    <th>Action</th>

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
