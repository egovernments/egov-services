import React, { Component } from "react";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import UserJobsCodeFilter from "./UserJobsCodeFilter";

export default class Filters extends Component {
  render() {
    return (
      <div style={{ position: "fixed" }}>
        <UserJobsCodeFilter />
        <JobStatusFilter />
        <DateFilter />
      </div>
    );
  }
}
