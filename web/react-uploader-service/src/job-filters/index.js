import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { applyUserJobFilters, resetUserJobFilters } from "../jobs/actions";
import JobStatusFilter from "./UserJobsStatusFilter";
import DateFilter from "./UserJobsDateFilter";
import UserJobsCodeFilter from "./UserJobsCodeFilter";
import RequesterNamesFilter from "./RequesterNamesFilter";
import RequesterFileNamesFilter from "./RequesterFileNamesFilter";
import CardUi from "../components/CardUi";
import ButtonUi from "../components/ButtonUi";

class Filters extends Component {
  static propTypes = {
    applyUserJobFilters: PropTypes.func.isRequired,
    resetUserJobFilters: PropTypes.func.isRequired
  };

  handleApplyFilter = e => {
    this.props.applyUserJobFilters();
  };

  handleResetFilter = e => {
    this.props.resetUserJobFilters();
  };

  render() {
    const { handleApplyFilter, handleResetFilter } = this;
    return (
      <div>
        <div className="row">
          <CardUi>
            <div className="col-lg-4">
              <UserJobsCodeFilter />
            </div>
            <div className="col-lg-4">
              <RequesterFileNamesFilter />
            </div>
            <div>
              <RequesterNamesFilter />
            </div>
          </CardUi>

          <CardUi cardTitle="By Date">
            <div>
              <DateFilter />
            </div>
          </CardUi>

          <CardUi cardTitle="By Job Completion Status">
            <JobStatusFilter />
          </CardUi>
        </div>
        <div className="row">
          <div
            style={{ textAlign: "center", width: "100%", margin: "15px 0px" }}
          >
            <ButtonUi
              onClick={handleApplyFilter}
              style={{ marginRight: "15px" }}
              type="button"
              primary={true}
              label="Filter"
              icon={{ style: { color: "white" }, name: "search" }}
            />
            <ButtonUi
              onClick={handleResetFilter}
              type="button"
              label="Reset"
              icon={{ style: { color: "black" }, name: "backspace" }}
            />
          </div>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyUserJobFilters: () => dispatch(applyUserJobFilters()),
  resetUserJobFilters: () => dispatch(resetUserJobFilters())
});

export default connect(null, mapDispatchToProps)(Filters);
