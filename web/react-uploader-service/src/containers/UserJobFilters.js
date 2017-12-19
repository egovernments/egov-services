import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import RaisedButton from "material-ui/RaisedButton";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import { fetchUserJobs } from "../actions/userJobs";

class Filters extends Component {
  handleClick = event => {
    const { jobStatus, fromDate, toDate } = this.props;
    // do validation if all are empty then disable
    const filters = {
      jobStatus,
      fromDate,
      toDate
    };
    this.props.fetchUserJobs(filters);
  };

  render() {
    const { handleClick } = this;
    return (
      <div>
        <JobStatusFilter />
        <DateFilter />
        <RaisedButton
          onClick={handleClick}
          label="Filter"
          primary={true}
          fullWidth={true}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  fetchUserJobs: filters => dispatch(fetchUserJobs())
});

export default connect(null, mapDispatchToProps)(Filters);
