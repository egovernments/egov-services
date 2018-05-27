class ShowPayScale extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      payscale: []
    }

  }


  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Payscale");
    var _this = this;


    getDropdown("payscale", function(res){
      _this.setState({
        payscale: res
      });
    });

  }


  componentDidUpdate(prevProps, prevState) {
    if (prevState.payscale.length != this.state.payscale.length) {
      $('#payscaleTable').DataTable({
        dom: 'Bfrtip',
        buttons: [
          'copy', 'csv', 'excel', 'pdf', 'print'
        ],
        ordering: false
      });
    }
  }



  close() {
    open(location, '_self').close();
  }


  render() {
    let { payscale } = this.state;

    const renderAction = function (type, id) {
      if (type === "update") {

        return (
          <a href={`app/hr/employee/payscale.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
        );

      } else {
        return (
          <a href={`app/hr/employee/payscale.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
        );
      }
    }


    const renderBody = function () {
      return payscale.map((item, index) => {
        return (<tr key={index}>
          <td>{index + 1}</td>
          <td data-label="paycommission">{item.paycommission}</td>
          <td data-label="payscale">{item.payscale}</td>
          <td data-label="amountFrom">{item.amountFrom}</td>
          <td data-label="amountTo">{item.amountTo}</td>
          <td data-label="action">
            {renderAction(getUrlVars()["type"], item.id)}
          </td>
        </tr>
        );

      })
    }

    return (<div>
      <h3>{titleCase(getUrlVars()["type"])} Payscale</h3>
      <table id="payscaleTable" className="table table-bordered">
        <thead>
          <tr>
            <th>Sl No.</th>
            <th>Pay Commission</th>
            <th>Pay scale</th>
            <th>From</th>
            <th>To</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody id="searchResultTableBody">
          {
            renderBody()
          }
        </tbody>

      </table>
      <div className="text-center">
        <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
      </div>
    </div>
    );
  }
}

ReactDOM.render(
  <ShowPayScale />,
  document.getElementById('root')
);
