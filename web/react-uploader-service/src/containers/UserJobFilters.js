import React, { Component } from "react";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import UserJobsCodeFilter from "./UserJobsCodeFilter";
import RequesterNamesFilter from "./RequesterNamesFilter";
import RequesterFileNamesFilter from "./RequesterFileNamesFilter";

export default class Filters extends Component {
  render() {
    return (
      <div>
        <UserJobsCodeFilter />
        <RequesterFileNamesFilter />
        <RequesterNamesFilter />
        <JobStatusFilter />
        <DateFilter />
      </div>
    );
  }
}
