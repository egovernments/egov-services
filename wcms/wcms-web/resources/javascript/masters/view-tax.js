class DcbReport extends React.Component {

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
                    <th >Tax Type</th>
                    <th >Rate</th>
                    <th >Period</th>
                    </tr>
                    </thead>

                    <tr>
                    <td> 1 </td>
                    <td> Genarel Tax Residential</td>
                    <td>9.73 </td>
                    <td>Half Yearly </td>
                    </tr>

                    <tr>
                    <td> 2 </td>
                    <td> Genarel Tax non Residential</td>
                    <td>13.36</td>
                    <td>Half Yearly</td>

                    </tr>
                    <tr>
                    <td> 3 </td>
                    <td>Education Cess</td>
                    <td>2.00 </td>
                    <td>Half Yearly</td>
                    </tr>

                    <tr>
                    <td> 4 </td>
                    <td>Vacent land Tax</td>
                    <td>0.50 </td>
                    <td> Yearly</td>
                    </tr>

                    <tr>
                    <td> 5 </td>
                    <td>Library Cess</td>
                    <td>8.00 </td>
                    <td>Half Yearly</td>
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
  <DcbReport />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
//
