const RequestInfo = {
  type: "object",
  description:
    "RequestInfo should be used to carry meta information about the requests to the server as described in the fields below. All eGov APIs will use requestinfo as a part of the request body to carry this meta information. Some of this information will be returned back from the server as part of the ResponseInfo in the response body to ensure correlation.",
  properties: {
    apiId: {
      type: "string",
      description: "unique API ID",
      maxLength: 128
    },
    ver: {
      type: "string",
      description:
        "API version - for HTTP based request this will be same as used in path",
      maxLength: 32
    },
    // ts: {
    //   type: "integer",
    //   // format: "int64",
    //   description: "time in epoch"
    // },
    action: {
      type: "string",
      description:
        "API action to be performed like _create, _update, _search (denoting POST, PUT, GET) or _oauth etc",
      maxLength: 32
    },
    did: {
      type: "string",
      description: "Device ID from which the API is called",
      maxLength: 1024
    },
    key: {
      type: "string",
      description:
        "API key (API key provided to the caller in case of server to server communication)",
      maxLength: 256
    },
    msgId: {
      type: "string",
      description: "Unique request message id from the caller",
      maxLength: 256
    },
    requesterId: {
      type: "string",
      description: "UserId of the user calling",
      maxLength: 256
    },
    authToken: {
      type: "string",
      description:
        "//session/jwt/saml token/oauth token - the usual value that would go into HTTP bearer token"
    },
    // userInfo: UserInfo,
    correlationId: {
      type: "string",
      readOnly: true
    }
  },
  required: ["apiId", "ver", "ts", "msgId", "action"]
};

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
