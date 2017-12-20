import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import DatePicker from "material-ui/DatePicker";
import { applyFromDateFilter, applyToDateFilter } from "../actions/filter";

class UserJobsDateFilterContainer extends Component {
  static propTypes = {
    applyFromDateFilter: PropTypes.func.isRequired,
    applyToDateFilter: PropTypes.func.isRequired,
    fromDate: PropTypes.instanceOf(Date),
    toDate: PropTypes.instanceOf(Date)
  };

  maxDate = new Date();
  render() {
    const {
      applyFromDateFilter,
      applyToDateFilter,
      fromDate,
      toDate
    } = this.props;
    const { maxDate } = this;

    return (
      <div>
        <h5>By Date</h5>
        <DatePicker
          onChange={(event, date) => {
            applyFromDateFilter(date);
          }}
          floatingLabelText="From Date"
          value={fromDate}
          maxDate={maxDate}
        />
        <DatePicker
          onChange={(event, date) => {
            applyToDateFilter(date);
          }}
          floatingLabelText="To Date"
          value={toDate}
        />
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  applyFromDateFilter: fromDate => dispatch(applyFromDateFilter(fromDate)),
  applyToDateFilter: toDate => dispatch(applyToDateFilter(toDate))
});

const mapStateToProps = (state, ownProps) => ({
  fromDate: state.filter.fromDate,
  toDate: state.filter.toDate
});

export default connect(mapStateToProps, mapDispatchToProps)(
  UserJobsDateFilterContainer
);
