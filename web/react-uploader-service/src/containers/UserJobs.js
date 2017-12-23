import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import TableUi from "../atomic-components/TableUi";
import { fetchUserJobs } from "../actions/userJobs";
import UserJobFilters from "./UserJobFilters";
import LoadingIndicator from "../atomic-components/LoadingIndicator";

// todo map the header fields with the data keys
class UserJobsContainer extends Component {
  static propTypes = {
    fetchUserJobs: PropTypes.func.isRequired,
    isFetching: PropTypes.bool.isRequired,
    userJobs: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string,
        status: PropTypes.oneOf(["completed", "new", "InProgress", "failed"]),
        moduleName: PropTypes.string,
        moduleDefiniton: PropTypes.string,
        download: PropTypes.shape({
          label: PropTypes.string,
          href: PropTypes.string
        })
      })
    )
  };

  // ordering of row in a schema is important to preserve the ordering in the table
  tableSchema = [
    { key: "id", label: "Job ID", fieldType: "label" },
    { key: "moduleName", label: "Module", fieldType: "label" },
    { key: "status", label: "Status", fieldType: "label" },
    { key: "download", label: "Download", fieldType: "hyperlink" }
  ];

  componentDidMount() {
    this.props.fetchUserJobs();
  }

  render() {
    const { tableSchema } = this;
    const { userJobs, isFetching } = this.props;
    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-3">
            <UserJobFilters />
          </div>
          <div className="col-lg-9">
            {isFetching ? (
              <LoadingIndicator />
            ) : (
              <TableUi tableSchema={tableSchema} tableBody={userJobs} />
            )}
          </div>
        </div>
      </div>
    );
  }
}

const filterUserJobs = userJobs => {
  return userJobs.filter(userJob => !userJob.softDelete);
};

const mapDispatchToProps = dispatch => ({
  fetchUserJobs: () => dispatch(fetchUserJobs())
});

const mapStateToProps = (state, ownProps) => ({
  isFetching: state.userJobs.isFetching,
  userJobs: filterUserJobs(state.userJobs.items)
});

export default connect(mapStateToProps, mapDispatchToProps)(UserJobsContainer);
