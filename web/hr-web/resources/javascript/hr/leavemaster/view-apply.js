class ViewApply extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      list: [],
      _status: [],
      modified: false
    };
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentDidMount() {
    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName(
        "homepage_logo"
      );
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src =
          window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    var count = 1,
      _this = this,
      _state = {};
    var checkCountNCall = function(key, res) {
      count--;
      _state[key] = res;
      if (count == 0) _this.setInitialState(_state);
    };
    getDropdown("leaveStatus", function(res) {
      checkCountNCall("_status", res);
    });

    var employee = getUrlVars()["id"];
    var leaveApp = [];
    commonApiPost(
      "hr-leave",
      "leaveapplications",
      "_search",
      { employee, tenantId, pageSize: 500 },
      function(err, res) {
        if (res) {
          var leaveApp = res["LeaveApplication"];
          if (leaveApp == 0) {
            _this.setState({
              list: leaveApp,
              modified: true
            });
          }
          var empIds = [];
          for (var i = 0; i < leaveApp.length; i++) {
            if (empIds.indexOf(leaveApp[i].employee) == -1)
              empIds.push(leaveApp[i].employee);
          }
          if (empIds.length > 0) {
            commonApiPost(
              "hr-employee",
              "employees",
              "_search",
              {
                tenantId,
                id: empIds.join(",")
              },
              function(err, res2) {
                if (res2) {
                  var employees = res2["Employee"];
                  for (var i = 0; i < leaveApp.length; i++) {
                    employees.map(function(item, ind) {
                      if (item.id == leaveApp[i].employee) {
                        leaveApp[i].name = item.name;
                      }
                    });
                  }
                  _this.setState({
                    list: leaveApp,
                    modified: true
                  });
                  setTimeout(function() {
                    _this.setState({
                      modified: false
                    });
                  }, 1200);
                }
              }
            );
          }
        }
      }
    );
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.state.modified) {
      $("#viewleaveTable").DataTable({
        dom: "Bfrtip",
        buttons: ["copy", "csv", "excel", "pdf", "print"],
        ordering: false,
        bDestroy: true,
        language: {
          emptyTable: "No Records"
        }
      });
    }
  }

  close() {
    open(location, "_self").close();
  }

  render() {
    let { list, _status } = this.state;
    console.log(this.state, "state");
    console.log(_status, "_status");
    console.log(list, "list");

    const renderBody = function() {
      if (list.length > 0) {
        return list.map((item, index) => {
          console.log(item, "item");
          return (
            <tr key={index}>
              <td>{index + 1}</td>
              <td data-label="name">{item.name}</td>
              <td data-label="fromDate">{item.fromDate}</td>
              <td data-label="toDate">{item.toDate}</td>
              <td data-label="availableDays">{item.availableDays}</td>
              <td data-label="leaveDays">{item.leaveDays}</td>
              <td data-label="status">
                {getNameById(_status, item.status, "code")}
              </td>
              <td data-label="action">
                <a
                  href={`app/hr/leavemaster/apply-leave.html?id=${
                    item.id
                  }&type=view`}
                >
                  View-Details
                </a>
              </td>
            </tr>
          );
        });
      }
    };

    return (
      <div>
        <table id="viewleaveTable" className="table table-bordered">
          <thead>
            <tr>
              <th>Sl No.</th>
              <th>Name</th>
              <th>From Date</th>
              <th>To Date</th>
              <th>Available Days</th>
              <th>Leave Days</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody id="listsearchResultTableBody">{renderBody()}</tbody>
        </table>
      </div>
    );
  }
}

ReactDOM.render(<ViewApply />, document.getElementById("root"));
