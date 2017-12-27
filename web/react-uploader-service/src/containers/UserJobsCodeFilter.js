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
        <h5>By Job Code(Comma Seperated)</h5>
        <TextField
          onChange={e =>
            applyUserJobFilters({
              codes: e.target.value
                .trim()
                .split(",")
                .map(value => value.trim())
            })}
          hintText="Job Codes"
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: filter => dispatch(applyUserJobFilters(filter))
});

export default connect(null, mapDispatchToProps)(UserJobsCodeFilterContainer);
