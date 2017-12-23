import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TextField from "material-ui/TextField";
import { applyUserJobFilters } from "../actions/userJobs";

class UserJobsCodeFilterContainer extends Component {
  static propTypes = {
    applyUserJobFilters: PropTypes.func.isRequired
  };

  render() {
    const { applyUserJobFilters } = this.props;

    return (
      <div>
        <h5>By Job Code</h5>
        <TextField
          onChange={e => applyUserJobFilters({ codes: [e.target.value] })}
          hintText="Job Code"
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: filter => dispatch(applyUserJobFilters(filter))
});

export default connect(null, mapDispatchToProps)(UserJobsCodeFilterContainer);
