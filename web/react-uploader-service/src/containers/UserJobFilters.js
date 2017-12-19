import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import RaisedButton from "material-ui/RaisedButton";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import { fetchUserJobs } from "../actions/userJobs";

class Filters extends Component {
  static propTypes = {
    codes: PropTypes.array,
    statuses: PropTypes.array,
    startDate: PropTypes.instanceOf(Date),
    endDate: PropTypes.instanceOf(Date),
    fetchUserJobs: PropTypes.func.isRequired
  };

  handleClick = event => {
    const { codes, statuses, startDate, endDate } = this.props;
    // do validation if all are empty then disable
    this.props.fetchUserJobs(codes, statuses, startDate, endDate);
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

const mapStateToProps = state => ({
  codes: null
});

const mapDispatchToProps = dispatch => ({
  fetchUserJobs: (codes, statuses, startDate, endDate) =>
    dispatch(fetchUserJobs(codes, statuses, startDate, endDate))
});

export default connect(null, mapDispatchToProps)(Filters);
