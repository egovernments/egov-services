class CreateRoofType extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],searchSet:{
    name:""}
      }
    // this.handleChange=this.handleChange.bind(this);
  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }



  render() {
    let {handleChange}=this;
    let {list}=this.state;
    let {name}=this.state.searchSet;



    return (
    <div>

          <form>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="name">Name <span> * </span> </label>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                              <input type="text" name="name" id="name" />
                            </div>
                        </div>
                    </div>
                </div>
                </div>




                    <div className="text-center">
                    <button type="submit" className="btn btn-submit">Add</button>
                    &nbsp;
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
                    </div>
                    </form>



          </div>
          );
      }
}


ReactDOM.render(
  <CreateRoofType />,
  document.getElementById('root')
);
