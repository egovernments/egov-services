const FireNOCSearchRequest = {
  summary: "Get the list of Fire NOC(s) defined in the system.",
  description:
    "1. Search and get Fire NOC(s)  based on defined search criteria.",
  parameters: {
    tenantId: {
      name: "tenantId",
      in: "query",
      description: "Unique id for a tenant.",
      required: true,
      type: "string",
      format: "varchar"
    },
    pageNumber: {
      name: "pageNumber",
      in: "query",
      description: "Page number",
      type: "integer",
      default: 1
    },
    status: {
      name: "status",
      in: "query",
      description: "Search based on status.",
      type: "string",
      allowEmptyValue: true
    },
    ids: {
      name: "ids",
      type: "array",
      items: {
        type: "integer",
        format: "int64"
      },
      in: "query",
      maxItems: 50,
      description: "unique identifier of Fire NOC"
    },
    applicationNumber: {
      name: "applicationNumber",
      in: "query",
      description: "Unique application number for a Fire NOC application.",
      type: "string",
      minLength: 2,
      maxLength: 64
    },
    fireNOCNumber: {
      name: "fireNOCNumber",
      in: "query",
      description: "The unique  license number  for a Fire NOC.",
      type: "string",
      minLength: 2,
      maxLength: 64
    },
    mobileNumber: {
      name: "mobileNumber",
      in: "query",
      description: "The mobile number of a Fire NOC owner.",
      type: "string",
      pattern: "[0-9]"
    },
    fireNOCType: {
      name: "fireNOCType",
      in: "query",
      description: "Fire NOC type.",
      type: "string"
    }
  },
  required: ["tenantId"]
};

module.exports = FireNOCSearchRequest;
