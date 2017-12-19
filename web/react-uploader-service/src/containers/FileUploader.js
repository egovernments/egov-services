import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Link } from "react-router-dom";
import { setInputFile } from "../actions/fileUpload";
import { createJob } from "../actions/createJob";
import RaisedButton from "material-ui/RaisedButton";
import UploadDefinitionsContainer from "./UploadDefinitions";
import LoadingIndicator from "../atomic-components/LoadingIndicator";

// Material UI Components
import { Card, CardActions, CardHeader, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";

const styles = {
  fileInput: {
    padding: "20px"
  },
  cardHeaderStyle: {
    textAlign: "center",
    padding: "5px",
    textTransform: "uppercase"
  }
};

class FileUploaderContainer extends Component {
  static propTypes = {
    file: PropTypes.object,
    moduleName: PropTypes.string,
    moduleDefinition: PropTypes.string,
    isLoading: PropTypes.bool,
    jobId: PropTypes.string,
    createJob: PropTypes.func.isRequired,
    setInputFile: PropTypes.func.isRequired
  };

  state = {
    message: ""
  };

  handleOnChange = e => {
    const file = e.target.files[0];
    this.props.setInputFile(file);
  };
  componentWillReceiveProps(nextProps) {
    const currentJobId = this.props.jobId;
    const nextJobId = nextProps.jobId;
    if (currentJobId == null && nextJobId) {
      const message = `New Job with reference ${nextJobId} created`;
      this.setState({ message });
    }
  }

  handleSubmit = e => {
    const { file, moduleName, moduleDefinition } = this.props;
    const formData = { file, moduleName };
    this.props.createJobRequest(moduleName, moduleDefinition, file);
  };

  render() {
    const { handleSubmit, handleOnChange } = this;
    const { isLoading } = this.props;
    const { message } = this.state;

    return (
      <div className="container">
        <div className="row">
          <div className="col-lg-4 col-md-4 col-lg-offset-4 col-md-offset-4">
            {isLoading ? (
              <LoadingIndicator />
            ) : (
              <Card>
                <div style={styles.cardHeaderStyle} className="card-header">
                  <h3>Create Job</h3>
                </div>
                <CardText>
                  <UploadDefinitionsContainer />
                  <div className="file-input" style={styles.fileInput}>
                    <input type="file" onChange={this.handleOnChange} />
                  </div>
                  <RaisedButton
                    onClick={handleSubmit}
                    label="Save"
                    secondary={true}
                    fullWidth={true}
                  />
                </CardText>
                <CardActions>
                  <Link to="/user-jobs">
                    <FlatButton
                      fullWidth={true}
                      primary={true}
                      label="My Jobs"
                    />
                  </Link>
                </CardActions>
                {message.length ? <h2>{message}</h2> : ""}
              </Card>
            )}
          </div>
        </div>
      </div>
    );
  }
}

const mapDispatchToProps = dispatch => ({
  createJob: (moduleName, moduleDefinition, file) =>
    dispatch(createJob(moduleName, moduleDefinition, file)),
  setInputFile: file => dispatch(setInputFile(file))
});

const mapStateToProps = (state, ownProps) => ({
  file: state.fileUpload.inputFile,
  moduleName: state.uploadDefinitions.selectedModule,
  moduleDefinition: state.uploadDefinitions.selectedModuleDefinition,
  isLoading: state.fileUpload.isFetching,
  jobId: state.jobCreate.jobId
});

export default connect(mapStateToProps, mapDispatchToProps)(
  FileUploaderContainer
);
