
class ViewExemption extends React.Component {

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
                    <th > Name </th>
                    <th >Code</th>
                  </tr>
                    </thead>

                    <tr>
                    <td> Ex Service Man /In- Service Man</td>
                    <td> Ex Service</td>

                    </tr>
                    <tr>
                    <td> Choultries </td>
                    <td> CHOULTRIES</td>

                    </tr>
                    <tr>
                    <td> NGO Homes </td>
                    <td> NGO</td>
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
  <ViewExemption />,
  document.getElementById('root')
);


// <tbody id="agreementSearchResultTableBody">
//     {
//         renderBody()
//     }
// </tbody>
