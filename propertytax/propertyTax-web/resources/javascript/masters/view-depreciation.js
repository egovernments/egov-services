
class ViewDepreciation extends React.Component {

  constructor(props) {
    super(props);
        this.closeWindow = this.closeWindow.bind(this);
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



  render() {


    return (
        <div>
            <div className="table-cont" id="table">

                <div className="view-table">
                <table>
                <thead>
                   <tr>
                    <th > Sl. NO</th>
                    <th >Depreciation Name</th>
                    <th >Depreciation Percentage</th>
                    <th>From Date </th>
                    <th>To Date </th>
                    </tr>
                    </thead>

                    <tr>
                    <td> 1 </td>
                    <td> 0- 25</td>
                    <td>10 </td>
                    <td> 1/04/2016</td>
                    <td> 31/03/2020</td>
                    </tr>
                    <tr>
                    <td> 2 </td>
                    <td> 26- 40</td>
                    <td>20 </td>
                    <td> 1/04/2016</td>
                    <td> 31/03/2020</td>
                    </tr>

                </table>

            </div>
            </div>
            <div className="text-center">
            <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>

            </div>

            </div>

    );
  }
}






ReactDOM.render(
  <ViewDepreciation />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
