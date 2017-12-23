import React, { Component } from "react";
import PropTypes from "prop-types";
import RaisedButton from "material-ui/RaisedButton";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import UserJobsCodeFilter from "./UserJobsCodeFilter";
import { applyUserJobFilters } from "../actions/userJobs";

export default class Filters extends Component {
  render() {
    const { handleClick } = this;
    return (
      <div>
        <UserJobsCodeFilter />
        <JobStatusFilter />
        <DateFilter />
      </div>
    );
  }
}
