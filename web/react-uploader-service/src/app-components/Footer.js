import React from "react";

const footerStyles = {
  position: "fixed",
  bottom: "0",
  width: "100%",
  background: "rgb(0, 188, 212)",
  padding: "15px",
  color: "white",
  textAlign: "center"
};

const Footer = () => {
  return <footer style={footerStyles}>© 2017 eGovernments Foundation</footer>;
};
export default Footer;
