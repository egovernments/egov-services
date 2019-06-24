const FireNOCSearchRequest = {
  summary: "Get the list of Fire NOC(s) defined in the system.",
  description:
    "1. Search and get Fire NOC(s)  based on defined search criteria.",
  properties: {
    tenantId: {
      type: "string"
    },
    pageNumber: {
      type: "integer",
    },
    status: {
      type: "string"
    },
    ids: {
      type: "array",
      items: {
        type: "integer"
        // format: "int64"
      },
      maxItems: 50
    },
    applicationNumber: {
      type: "string",
      minLength: 2,
      maxLength: 64
    },
    fireNOCNumber: {
      type: "string",
      minLength: 2,
      maxLength: 64
    },
    mobileNumber: {
      type: "string",
      pattern: "[0-9]"
    },
    fireNOCType: {
      type: "string"
    }
  },
  required: ["tenantId"]
};

module.exports = FireNOCSearchRequest;
