class CreateFunction extends React.Component {
  constructor(props) {
    super(props);
    this.state={list:[],functionCreate:{
       "tenantId": 1,
      "name": "",
      "code":"",
      "level":"",
      "active":"",
      "parentId":"",
      "isParent":""

  },
  parentIds:[]
}

  this.handleChange=this.handleChange.bind(this);
  this.addOrUpdate=this.addOrUpdate.bind(this);

}


  handleChange(e,name)
  {

      this.setState({
          functionCreate:{
              ...this.state.functionCreate,
              [name]:e.target.value
          }
      })

  }
  close(){
      // widow.close();
      open(location, '_self').close();
  }

  componentDidMount()
  {
    // console.log(getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"]);
    this.setState({

     parentIds:getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"] || []

     });
  }

  addOrUpdate(e,mode){
          e.preventDefault();
           console.log(this.state.functionCreate);
          // console.log(mode);
          if (mode==="update") {
              console.log("update");
          } else {

            this.setState({functionCreate:{
              name:"",
              code:"",
              level:"",
              active:"",
              isParent:"",
              parentId:""
            } })
          }
        }


render() {
  let {handleChange,addOrUpdate}=this;
  let{functionCreate,parentIds}=this.state;
  let{name,code,level,active,isParent,parentId}=functionCreate;
  let mode = getUrlVars()["type"];

  const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };
  const renderOption=function(list)
  {
      if(list)
      {
          if (list.length) {

            return list.map((item)=>
            {

                return (<option key={item.id} value={item.id}>
                  {typeof(item.name)=="undefined"?item.uom:item.name}

                  </option>)
            })

          } else {
            return Object.keys(list).map((k, index)=>
            {
              return (<option key={index} value={k}>
                      {list[k]}
                </option>)

             })
          }

      }
  }


  return(
    <div>
    <form onSubmit={(e)=>{addOrUpdate(e,mode)}}>


          <div className="row">
          <div className="col-sm-6">
          <div className="row">
          <div className="col-sm-6 label-text">
                  <label for="name">Name <span> * </span></label>
          </div>
          <div className="col-sm-6">
                  <input type="text" id="name" name="name" required="true" value={name}
                  onChange={(e)=>{handleChange(e,"name")}}/>
          </div>
          </div>
          </div>
          <div className="col-sm-6">
          <div className="row">
          <div className="col-sm-6 label-text">
                  <label for="code">Code <span> * </span></label>
          </div>
          <div className="col-sm-6">
                  <input type="text" id="code" name="code" required="true" value={code}
                  onChange={(e)=>{handleChange(e,"code")}}/>
          </div>
          </div>
          </div>
          </div>

          <div className="row">
          <div className="col-sm-6">
          <div className="row">
          <div className="col-sm-6 label-text">
                  <label for="level">Level </label>
          </div>
          <div className="col-sm-6">
                  <input type="number" id="level" name="level" value={level}
                  onChange={(e)=>{handleChange(e,"level")}}/>
          </div>
          </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Active</label>
                  </div>
                  <div className="col-sm-6">
                  <input type="checkbox" name="active" id="active" value={active} onChange={(e)=>{
              handleChange(e,"active")}} />
                  </div>
              </div>
              </div>
              </div>


              <div className="row">
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label htmlFor="">Is Parent</label>
                      </div>
                      <div className="col-sm-6">
                      <input type="checkbox" name="isParent" id="active" value={isParent} onChange={(e)=>{
                  handleChange(e,"isParent")}} />
                      </div>
                  </div>
                  </div>

              <div className="col-sm-6">
              <div className="row">
              <div className="col-sm-6 label-text">
                    <label for="parent"> Parent Type  </label>
              </div>
              <div className="col-sm-6">
              <div className="styled-select">
              <select id="parentID" name="parentId"  value={parentId} onChange={(e)=>{
              handleChange(e,"parentId")}}>
                  <option value="">Select Parent Category</option>
                  {renderOption(this.state.parentIds)}
                </select>
              </div>
              </div>
              </div>
              </div>
                                </div>

                  <div className="text-center">
        {showActionButton()} &nbsp;&nbsp;
        <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>

    </div>


    </form>
          </div>

  );
}
}
ReactDOM.render(
  <CreateFunction />,
  document.getElementById('root')
);


  //
