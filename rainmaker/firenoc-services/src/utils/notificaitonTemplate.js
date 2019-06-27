import get from "lodash/get";

const templateInterface = (fireNOC = {}) => {
  let payload = {};
  const topic = "egov.core.notification.sms";
  let smsRequest = {
    mobileNumber: get(
      fireNOC.fireNOCDetails,
      "applicantDetails.owners.0.mobileNumber"
    )
  };
  const applicantName = get(
    fireNOC.fireNOCDetails,
    "applicantDetails.owners.0.name"
  );
  const fireNOCType = get(fireNOC.fireNOCDetails, "fireNOCType");
  const applicationNumber = get(fireNOC.fireNOCDetails, "applicationNumber");
  switch (fireNOC.fireNOCDetails.action) {
    case "INITIATE":
      {
        smsRequest[
          "message"
        ] = `Dear ${applicantName},Your application for ${fireNOCType} has been generated. Your application no. is ${applicationNumber}`;
      }
      break;

    case "APPLY":
      {
        smsRequest[
          "message"
        ] = `Dear ${applicantName},Your application for ${fireNOCType} has been submitted. Your application no. is ${applicationNumber}.Please pay your NoC Fees online or at your applicable fire office`;
      }
      break;

      case "PAY":
        {
          smsRequest[
            "message"
          ] = `Dear <Applicant Name>,
                A Payment of <Payment Amount> has been collected successfully.
                Your receipt no. <Receipt Number>.
               `;
        }
        break;
  }
  payload = {
    topic,
    messages: JSON.stringify(smsRequest)
  };
  return payload;
};

export default templateInterface;
