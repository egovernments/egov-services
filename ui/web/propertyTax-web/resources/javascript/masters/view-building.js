
class ViewBuilding extends React.Component {

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
                    <th >Code</th>
                    <th >Name</th>
                    <th >Description</th>
                    <th >From Date</th>
                    <th > To Date</th>
                    </tr>
                    </thead>

                    <tr>
                    <td> 1 </td>
                    <td> Country Tiles</td>
                    <td>Country Tiles </td>
                    <td>COUNTRYTILES </td>
                    <td> 1-04-2004</td>
                    <td> 1-04-2099</td>
                    </tr>
                    <tr>
                    <td> 2 </td>
                    <td> huts</td>
                    <td>huts </td>
                    <td>HUTS</td>
                    <td> 1-04-2004</td>
                    <td> 1-04-2099</td>
                    </tr>
                    <tr>
                    <td> 3 </td>
                    <td> Mangalore Tiles</td>
                    <td>Mangalore Tiles </td>
                    <td>MANGALORETILES</td>
                    <td> 1-04-2004</td>
                    <td> 1-04-2099</td>
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
  <ViewBuilding />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
