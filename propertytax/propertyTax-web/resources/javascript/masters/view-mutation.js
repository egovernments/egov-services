
class ViewMutation extends React.Component {

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
                    <th >Slab Name</th>
                    <th >From Value</th>
                    <th >To Value</th>
                    <th >Flat Rate</th>
                    <th> Percentage Rate</th>
                    <th> Recursive value </th>
                    <th> Additional Rate </th>
                    <th> Effective From </th>
                    <th> Effective To </th>
                    </tr>
                    </thead>

                    <tr>
                    <td> 1 </td>
                    <td> 1_50000</td>
                    <td>1 </td>
                    <td>50000 </td>
                    <td> 750</td>
                    <td> N/A</td>
                    <td> N/A</td>
                    <td> N/A</td>
                    <td> 1/04/2016</td>
                    <td> 31/03/2020</td>
                    </tr>
                    <tr>
                    <td> 2 </td>
                    <td> 50001_100000</td>
                    <td>50001 </td>
                    <td>100000 </td>
                    <td> 1250</td>
                    <td> N/A</td>
                    <td> N/A</td>
                    <td> N/A</td>
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
  <ViewMutation />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
