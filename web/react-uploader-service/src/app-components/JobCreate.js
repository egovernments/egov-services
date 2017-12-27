import React from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import RaisedButton from "material-ui/RaisedButton";
import UploadDefinitionsContainer from "../containers/UploadDefinitions";
import { Card, CardActions, CardText } from "material-ui/Card";
import FlatButton from "material-ui/FlatButton";

const styles = {
  fileInput: {
    padding: "20px"
  },
  cardHeaderStyle: {
    textAlign: "center",
    padding: "5px",
    textTransform: "uppercase"
  },
  jobCreationAck: {
    padding: "10px",
    color: "red",
    textAlign: "center"
  }
};

const JobCreate = ({ handleOnChange, handleSubmit, message }) => {
  return (
    <Card>
      <div style={styles.cardHeaderStyle} className="card-header">
        <h3>Create Job</h3>
      </div>
      <CardText>
        <UploadDefinitionsContainer />
        <div className="file-input" style={styles.fileInput}>
          <input type="file" onChange={handleOnChange} />
        </div>
        <RaisedButton
          onClick={handleSubmit}
          label="Create"
          secondary={true}
          fullWidth={true}
        />
      </CardText>
      <CardActions>
        <FlatButton
          href="#/user-jobs"
          fullWidth={true}
          primary={true}
          label="My Jobs"
        />
      </CardActions>
      {message.length ? <h3 style={styles.jobCreationAck}>{message}</h3> : ""}
    </Card>
  );
};

JobCreate.propTypes = {
  handleOnChange: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  message: PropTypes.string
};

export default JobCreate;
