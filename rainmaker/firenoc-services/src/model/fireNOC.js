const fireNOC = {
  definitions: {
    Buildings: {
      type: "object",
      description: "It will contains building details",
      properties: {
        id: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description:
            "Unique Identifier of the Fire NOC building details (UUID)",
          readOnly: true
        },
        tenantId: {
          type: "string",
          description: "Unique Identifier of ULB",
          maxLength: 128,
          minLength: 2
        },
        name: {
          type: "string",
          description: "name of the building"
        },
        usageType: {
          type: "string",
          description: "building usage type"
        },
        uoms: {
          type: "array",
          items: {
            $ref: "#/definitions/BuildingUOM"
          }
        },
        applicationDocuments: {
          description:
            "1. List of all the required documents. 2. Application can be submitted without required document 3. Once all the document submitted then only application submition process will be completed. 4. Mandatry application documents for a fireNOC type and fireNOC subtype are defined under ApplicationDocument master which is defined under MDMS.",
          type: "array",
          items: {
            $ref: "#/definitions/Document"
          }
        }
      },
      required: ["tenantId", "usageType", "name", "uoms"]
    },
    PropertyDetails: {
      type: "object",
      description: "It will have fire noc related entities",
      properties: {
        id: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description: "Unique Identifier of the property details (UUID)",
          readOnly: true
        },
        propertyId: {
          type: "string"
        },
        address: {
          $ref: "#/definitions/Address"
        }
      },
      required: ["noOfBuildings", "buildingDetails"]
    },
    OwnerInfo: {
      type: "object",
      properties: {
        isPrimaryOwner: {
          type: "boolean",
          description: "The owner is primary or not"
        },
        ownerShipPercentage: {
          type: "number",
          format: "double",
          description: "Ownership percentage."
        },
        ownerType: {
          type: "string",
          description:
            "Type of owner, based on this option Exemptions will be applied. This is master data defined in mdms.",
          maxLength: 256,
          minLength: 4
        },
        relationship: {
          type: "string",
          description: "Relationship with owner.",
          enum: ["FATHER", "HUSBAND"]
        },
        documents: {
          description: "Document of the owner.",
          items: {
            $ref: "#/definitions/Document"
          }
        }
      }
    },
    Document: {
      type: "object",
      description: "A Object holds the basic data for a Fire NOC",
      properties: {
        tenantId: {
          type: "string",
          description: "Unique Identifier of ULB",
          maxLength: 128,
          minLength: 2
        },
        documentType: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description:
            "1.Unique document type code from common master. 2. This is defined under mdms common master. 3. Object defination is defined under 'https://raw.githubusercontent.com/egovernments/egov-services/master/docs/common/contracts/v1-1-1.yml#/definitions/DocumentType'"
        },
        fileStoreId: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description: "Unique file store id of uploaded document."
        },
        documentUid: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description: "document number(eg.Pan number, aadhar number)."
        },
        auditDetails: {
          $ref: "#/definitions/AuditDetails"
        }
      },
      required: ["tenantId", "documentType", "fileStoreId"]
    },
    FireNOC: {
      type: "object",
      description: "A Object holds the basic data for a Fire NOC",
      properties: {
        id: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description: "Unique Identifier of the Fire NOC (UUID)",
          readOnly: true
        },
        tenantId: {
          type: "string",
          description: "Unique Identifier of ULB",
          maxLength: 128,
          minLength: 2
        },
        fireNOCNumber: {
          type: "string",
          description:
            "Unique Fire NOC Number of the Fire NOC. This is  unique in system for a tenant. This is mandatory but always be generated on the final approval.",
          maxLength: 64,
          minLength: 2,
          readOnly: true
        },
        provisionFireNOCNumber: {
          type: "string",
          description:
            "Unique Fire NOC Number of the Provision Fire NOC number that will be used for linking provision fire NOC number with new fire NOC number. This is  unique in system for a tenant. This is mandatory but always be generated on the final approval.",
          maxLength: 64,
          minLength: 2
        },
        oldFireNOCNumber: {
          type: "string",
          description:
            "Unique Old License Number of the Fire NOC. This is  unique in system for a tenant. This is mandatory  for legacy license(DataEntry).",
          maxLength: 64,
          minLength: 2
        },
        dateOfApplied: {
          type: "integer",
          format: "int64",
          description: "Applied Date of the fire NOC as epoch"
        },
        fireNOCDetails: {
          $ref: "#/definitions/FireNOCDetails"
        },
        auditDetails: {
          $ref: "#/definitions/AuditDetails"
        }
      },
      required: ["tenantId", "fireNOCType", "dateOfApplied", "fireNOCDetails"]
    },
    FireNOCDetails: {
      type: "object",
      description: "A Object holds the basic data for a Fire NOC",
      properties: {
        id: {
          type: "string",
          minLength: 2,
          maxLength: 64,
          description: "Unique Identifier of the Fire FireNOC Details (UUID)",
          readOnly: true
        },
        applicationNumber: {
          type: "string",
          description:
            "Unique Application FireNOC Number of the Fire FireNOC. This is  unique in system for a tenant. This is mandatory but always be generated on the final approval.",
          maxLength: 64,
          minLength: 2,
          readOnly: true
        },
        fireNOCType: {
          type: "string",
          description: "type of fire NOC from mdms"
        },
        firestationId: {
          type: "string",
          description: "Fire station id where we are applying fire fireNOC."
        },
        applicationDate: {
          type: "integer",
          format: "int64",
          description:
            "date on which applicaiton has been generated for new Fire NOC.",
          readOnly: true
        },
        financialYear: {
          type: "string",
          description: "Fire NOC applicable for financial year.",
          maxLength: 64,
          minLength: 2
        },
        issuedDate: {
          type: "integer",
          format: "int64",
          description:
            "1. License issued Date of the Fire NOC as epoch. 2. Application approval date.",
          readOnly: true
        },
        validFrom: {
          type: "integer",
          format: "int64",
          description: "Date from when Fire NOC is valid as epoch"
        },
        validTo: {
          type: "integer",
          format: "int64",
          description: "Expiry Date of the Fire NOC as epoch"
        },
        action: {
          type: "string",
          description:
            "1. Perform action to change the state of the Fire NOC. 2. INITIATE, if application is getting submitted without required document. 3. APPLY, if application is getting submitted with application documents, in that case api will validate all the required application document. 4. APPROVE action is only applicable for specific role, that role has to be configurable at service level. Employee can approve a application only if application is in APPLIED state and Fire NOC fees is paid.",
          enum: ["INITIATE", "APPLY", "APPROVE", "REJECT", "CANCEL"]
        },
        channel: {
          type: "string",
          description: "License can be created from different channels",
          maxLength: 64,
          minLength: 2,
          enum: ["COUNTER", "CITIZEN", "DATAENTRY"]
        },
        noOfBuildings: {
          type: "string",
          description: "it might be single or multiple"
        },
        buildings: {
          type: "array",
          items: {
            $ref: "#/definitions/Buildings"
          }
        },
        propertyDetails: {
          $ref: "#/definitions/PropertyDetails"
        },
        applicantDetails: {
          type: "object",
          description: "This will have details about applicant details",
          properties: {
            ownerShipType: {
              description: "type pf the owner ship",
              type: "string"
            },
            owners: {
              description:
                "Fire NOC owners, these will be citizen users in system.",
              type: "array",
              items: {
                $ref: "#/definitions/OwnerInfo"
              }
            },
            additionalDetail: {
              type: "object",
              description:
                "Json object to store additional details about license, this will be used when ownership is intitution or others"
            }
          }
        },
        additionalDetail: {
          type: "object",
          description: "Json object to store additional details about license"
        },
        auditDetails: {
          $ref: "#/definitions/AuditDetails"
        }
      },
      required: [
        "tenantId",
        "fireNOCType",
        "financialYear",
        "propertyDetails",
        "applicantDetails",
        "action",
        "channel",
        "firestationId"
      ]
    },
    FireNOCRequest: {
      description:
        "Contract class to receive request. Array of Noc items are used",
      properties: {
        RequestInfo: {
          $ref: "#/definitions/RequestInfo"
        },
        FireNOCs: {
          description: "Used for search result and create only",
          type: "array",
          minimum: 1,
          maximum: 100,
          items: {
            $ref: "#/definitions/FireNOC"
          }
        }
      },
      required: ["RequestInfo", "FireNOCs"]
    },
    FireNOCResponse: {
      description:
        "Contract class to send response. Array of Fire NOC items are used in case of search results or response for create, whereas single Fire NOC item is used for update",
      properties: {
        ResponseInfo: {
          $ref: "#/definitions/ResponseInfo"
        },
        FireNOCs: {
          description: "Used for search result and create only",
          type: "array",
          maximum: 500,
          items: {
            $ref: "#/definitions/FireNOC"
          }
        }
      }
    },
    BuildingType: {
      type: "object",
      description: "This is mdms defination for BuildingType object",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        UoMs: {
          description: "List of UoMs based building type",
          type: "array",
          items: {
            type: "string"
          }
        },
        active: {
          type: "boolean"
        }
      }
    },
    FireNOCType: {
      type: "object",
      description: "Type of FireNOC Provisional and New",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    OwnershipType: {
      type: "object",
      description: "Owner ship type Individual/multiple/Insti",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    OwnerSpecialCategory: {
      type: "object",
      description: "Owner special category Widow/freedom fighter",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    FireStations: {
      type: "object",
      description: "Fire staion where we apply for fire NOC",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        baseTenantId: {
          type: "string",
          description:
            "When multiple city sharing single firestation that time we can mention base tenant id for the firestation."
        },
        active: {
          type: "boolean"
        }
      }
    },
    FireStationsToULBMapping: {
      type: "object",
      description: "Fire staion to ulb mapping",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        fireStationCode: {
          type: "string",
          description: "Unique value reffered to FireStations master"
        },
        boundaryCode: {
          type: "string",
          description:
            "Unique value reffered to boundary master, it will be dot seperated eg- pb.amritsar.SC4"
        },
        active: {
          type: "boolean"
        }
      }
    },
    FireNOCMetaInfo: {
      type: "object",
      description: "This master used to maintain metadata of fire fireNOC",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        firestationMapping: {
          type: "string",
          description:
            "Here we will mention whether its a fire to ulb mapping or fire to mohalla mapping, depends on this configuration we will filter firestation to ulb mapping."
        },
        certificateValidity: {
          type: "string",
          description:
            "Here we will mention validity of mode eg- days/months/years."
        },
        certificateEndOfValidity: {
          type: "string",
          description:
            "Here we will mention end of validity eg- calendar/financial year."
        },
        overallSLA: {
          type: "number",
          description: "It will contain overall SLA for process"
        }
      }
    },
    UOM: {
      type: "object",
      description: "This master will have list of UOM's",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    BuildingUOM: {
      type: "object",
      description: "This master will have list of UOM's",
      properties: {
        code: {
          type: "string",
          description: "Code of the unit code"
        },
        value: {
          type: "string",
          description: "Value entered for the uom"
        },
        isActiveUom: {
          type: "boolean",
          description: "Active uom for current usage type"
        },
        active: {
          type: "boolean"
        }
      }
    },
    Status: {
      type: "object",
      description: "This master will have list of Status",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    PropertyType: {
      type: "object",
      description:
        "This master will have list of property type Single/Multiple building",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        active: {
          type: "boolean"
        }
      }
    },
    SLA: {
      type: "object",
      description: "This master will have mapping between Status to SLA",
      properties: {
        code: {
          type: "string",
          description: "Unique value for each entry in mdms data"
        },
        statusCode: {
          type: "string",
          description: "This is will be referene to status master"
        },
        noOfDays: {
          type: "string",
          description: "It will have status completion days."
        },
        active: {
          type: "boolean"
        }
      }
    },
    RequestInfo: {
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
        ts: {
          type: "integer",
          format: "int64",
          description: "time in epoch"
        },
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
        userInfo: {
          $ref: "#/definitions/UserInfo"
        },
        correlationId: {
          type: "string",
          readOnly: true
        }
      },
      required: ["apiId", "ver", "ts", "msgId", "action"]
    },
    UserInfo: {
      type: "object",
      description:
        "This is acting ID token of the authenticated user on the server. Any value provided by the clients will be ignored and actual user based on authtoken will be used on the server.",
      readOnly: true,
      properties: {
        tenantId: {
          type: "string",
          description:
            "Unique Identifier of the tenant to which user primarily belongs"
        },
        uuid: {
          type: "string",
          description: "System Generated User id of the authenticated user."
        },
        userName: {
          type: "string",
          description: "Unique user name of the authenticated user"
        },
        password: {
          type: "string",
          description: "password of the user."
        },
        idToken: {
          type: "string",
          description: "This will be the OTP."
        },
        mobile: {
          type: "string",
          description: "mobile number of the autheticated user"
        },
        email: {
          type: "string",
          description: "email address of the authenticated user"
        },
        primaryrole: {
          type: "array",
          description: "List of all the roles for the primary tenant",
          items: {
            $ref: "#/definitions/Role"
          }
        },
        additionalroles: {
          type: "array",
          description:
            "array of additional tenantids authorized for the authenticated user",
          items: {
            $ref: "#/definitions/TenantRole"
          }
        }
      },
      required: ["tenantId", "userName", "primaryrole"]
    },
    Role: {
      type: "object",
      description:
        "minimal representation of the Roles in the system to be carried along in UserInfo with RequestInfo meta data. Actual authorization service to extend this to have more role related attributes\n",
      properties: {
        name: {
          type: "string",
          description: "Unique name of the role",
          maxLength: 64
        },
        code: {
          type: "string",
          description: "Unique code of the role",
          maxLength: 64
        },
        description: {
          type: "string",
          description: "brief description of the role"
        }
      },
      required: ["name"]
    },
    TenantRole: {
      type: "object",
      description:
        "User role carries the tenant related role information for the user. A user can have multiple roles per tenant based on the need of the tenant. A user may also have multiple roles for multiple tenants.",
      properties: {
        tenantId: {
          type: "string",
          description: "tenantid for the tenant"
        },
        roles: {
          type: "array",
          description:
            "Roles assigned for a particular tenant - array of role codes/names",
          items: {
            $ref: "#/definitions/Role"
          }
        }
      },
      required: ["tenantId", "roles"]
    },
    ResponseInfo: {
      type: "object",
      readOnly: true,
      description:
        "ResponseInfo should be used to carry metadata information about the response from the server. apiId, ver and msgId in ResponseInfo should always correspond to the same values in respective request's RequestInfo.",
      properties: {
        apiId: {
          type: "string",
          description: "unique API ID",
          maxLength: 128
        },
        ver: {
          type: "string",
          description: "API version",
          maxLength: 32
        },
        ts: {
          type: "integer",
          format: "int64",
          description: "response time in epoch"
        },
        resMsgId: {
          type: "string",
          description:
            "unique response message id (UUID) - will usually be the correlation id from the server",
          maxLength: 256
        },
        msgId: {
          type: "string",
          description: "message id of the request",
          maxLength: 256
        },
        status: {
          type: "string",
          description:
            "status of request processing - to be enhanced in futuer to include INPROGRESS",
          enum: ["SUCCESSFUL", "FAILED"]
        }
      },
      required: ["apiId", "ver", "ts", "status"]
    },
    Error: {
      type: "object",
      description:
        "Error object will be returned as a part of reponse body in conjunction with ResponseInfo as part of ErrorResponse whenever the request processing status in the ResponseInfo is FAILED. HTTP return in this scenario will usually be HTTP 400.",
      properties: {
        code: {
          type: "string",
          description:
            "Error Code will be module specific error label/code to identiffy the error. All modules should also publish the Error codes with their specific localized values in localization service to ensure clients can print locale specific error messages. Example for error code would be User.NotFound to indicate User Not Found by User/Authentication service. All services must declare their possible Error Codes with brief description in the error response section of their API path."
        },
        message: {
          type: "string",
          description:
            "English locale message of the error code. Clients should make a separate call to get the other locale description if configured with the service. Clients may choose to cache these locale specific messages to enhance performance with a reasonable TTL (May be defined by the localization service based on tenant + module combination)."
        },
        description: {
          type: "string",
          description:
            "Optional long description of the error to help clients take remedial action. This will not be available as part of localization service."
        },
        params: {
          type: "array",
          description:
            "Some error messages may carry replaceable fields (say $1, $2) to provide more context to the message. E.g. Format related errors may want to indicate the actual field for which the format is invalid. Client's should use the values in the param array to replace those fields.",
          items: {
            type: "string"
          }
        }
      },
      required: ["code", "message"]
    },
    ErrorRes: {
      type: "object",
      description:
        "All APIs will return ErrorRes in case of failure which will carry ResponseInfo as metadata and Error object as actual representation of error. In case of bulk apis, some apis may chose to return the array of Error objects to indicate individual failure.",
      properties: {
        ResponseInfo: {
          $ref: "#/definitions/ResponseInfo"
        },
        Errors: {
          description:
            "Error response array corresponding to Request Object array. In case of single object submission or _search related paths this may be an array of one error element",
          type: "array",
          minLength: 1,
          items: {
            $ref: "#/definitions/Error"
          }
        }
      },
      required: ["ResponseInfo"]
    },
    Address: {
      type: "object",
      description:
        "Representation of a address. Indiavidual APIs may choose to extend from this using allOf if more details needed to be added in their case.\n",
      properties: {
        tenantId: {
          type: "string",
          description:
            "Unique Identifier of the tenant to which user primarily belongs"
        },
        doorNo: {
          type: "string",
          description: "House number or door number."
        },
        latitude: {
          type: "number",
          format: "double",
          description: "latitude of the address"
        },
        longitude: {
          type: "number",
          format: "double",
          description: "longitude of the address"
        },
        addressId: {
          type: "string",
          description: "System generated id for the address",
          readOnly: true
        },
        addressNumber: {
          description: "House, Door, Building number in the address",
          type: "string"
        },
        type: {
          type: "string",
          description: "Blood group of the user.",
          items: {
            type: "string",
            enum: ["PERMANENT", "CORRESPONDENCE"]
          }
        },
        addressLine1: {
          description: "Apartment, Block, Street of the address",
          type: "string"
        },
        addressLine2: {
          description: "Locality, Area, Zone, Ward of the address",
          type: "string"
        },
        landmark: {
          description: "additional landmark to help locate the address",
          type: "string"
        },
        city: {
          description:
            "City of the address. Can be represented by the tenantid itself",
          type: "string"
        },
        pincode: {
          type: "string",
          description:
            "PIN code of the address. Indian pincodes will usually be all numbers."
        },
        detail: {
          type: "string",
          description: "more address detail as may be needed"
        },
        buildingName: {
          type: "string",
          description: "Name of the building",
          maxLength: 64,
          minLength: 2
        },
        street: {
          type: "string",
          description: "Street Name",
          maxLength: 64,
          minLength: 2
        }
      }
    },
    AuditDetails: {
      type: "object",
      description: "Collection of audit related fields used by most models",
      readOnly: true,
      properties: {
        createdBy: {
          type: "string",
          description:
            "username (preferred) or userid of the user that created the object"
        },
        lastModifiedBy: {
          type: "string",
          description:
            "username (preferred) or userid of the user that last modified the object"
        },
        createdTime: {
          type: "integer",
          format: "int64",
          description: "epoch of the time object is created"
        },
        lastModifiedTime: {
          type: "integer",
          format: "int64",
          description: "epoch of the time object is last modified"
        }
      }
    }
  }
};

module.export=fireNOC;
