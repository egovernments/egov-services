import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TextField from "material-ui/TextField";
import { applyUserJobFilters } from "../actions/userJobs";

class RequesterFileNamesFilterContainer extends Component {
  static propTypes = {
    applyUserJobFilters: PropTypes.func.isRequired
  };

  // if the value has a comma, send it as an array by splitting it
  render() {
    const { applyUserJobFilters } = this.props;

    return (
      <div>
        <h5>By Requester File Names</h5>
        <TextField
          onChange={e =>
            applyUserJobFilters({
              requesterFileNames: e.target.value
                .trim()
                .split(",")
                .map(value => value.trim())
            })}
          hintText="Requester Names"
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: filter => dispatch(applyUserJobFilters(filter))
});

export default connect(null, mapDispatchToProps)(
  RequesterFileNamesFilterContainer
);
