import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TableUi from "../atomic-components/TableUi";
import { fetchUserJobs } from "../actions/userJobs";
import UserJobFilters from "./UserJobFilters";

// todo map the header fields with the data keys
class UserJobsContainer extends Component {
  static propTypes = {
    fetchUserJobs: PropTypes.func.isRequired,
    userJobs: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string,
        status: PropTypes.oneOf(["completed", "new", "in-progress", "failed"]),
        moduleName: PropTypes.string,
        moduleDefiniton: PropTypes.string,
        download: PropTypes.shape({
          label: PropTypes.string,
          href: PropTypes.string
        })
      })
    )
  };

  tableHeader = {
    fields: [
      "Job ID",
      "Module Name",
      "Module Definition",
      "Status",
      "Download"
    ],
    fieldsType: ["label", "label", "label", "label", "hyperlink"]
  };
  componentDidMount() {
    this.props.fetchUserJobs();
  }

  render() {
    const { tableHeader } = this;
    const { userJobs } = this.props;
    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-3">
            <h4>Filter Jobs</h4>
            <UserJobFilters />
          </div>
          <div className="col-lg-9">
            <TableUi tableHeader={tableHeader} tableBody={userJobs} />
          </div>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  fetchUserJobs: filter => dispatch(fetchUserJobs(filter))
});

const mapStateToProps = (state, ownProps) => ({
  userJobs: state.userJobs.items
});

export default connect(mapStateToProps, mapDispatchToProps)(UserJobsContainer);
