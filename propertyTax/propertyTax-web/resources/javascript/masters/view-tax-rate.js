
class ViewTaxRate extends React.Component {

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
                    <th >Name</th>
                    <th >Percentage</th>
                    <th >From Date</th>
                    <th> To Date </th>
                    </tr>
                    </thead>

                    <tr>
                    <td> 1 </td>
                    <td> Drainage Tax</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 2 </td>
                    <td> Education Cess</td>
                    <td>2.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>
                    <tr>
                    <td> 3 </td>
                    <td> Genarel Tax Non Residential</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 4 </td>
                    <td> Genarel Tax Residential</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 5 </td>
                    <td> Library Cess</td>
                    <td>8.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 6 </td>
                    <td> Lighting Tax</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 7 </td>
                    <td> Scavenging Tax</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 8 </td>
                    <td> Vacent Land Tax</td>
                    <td>0.50 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
                    </tr>

                    <tr>
                    <td> 9 </td>
                    <td> Water Tax</td>
                    <td>0.00 </td>
                    <td>1-10-2016 </td>
                    <td>31-03-2050</td>
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
  <ViewTaxRate />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
