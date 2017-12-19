import React from "react";
import CircularProgress from "material-ui/CircularProgress";

const circularProgressStyle = {
  position: "fixed",
  top: "45%",
  left: "50%"
};

const LoadingIndicator = () => {
  return <CircularProgress style={circularProgressStyle} />;
};

export default LoadingIndicator;
