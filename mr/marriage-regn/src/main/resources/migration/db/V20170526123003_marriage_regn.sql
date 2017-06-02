CREATE SEQUENCE marriageregn_application_number;

CREATE TABLE egmr_marriage_regn(
        regnunitid INTEGER,
		marriageDate BIGINT NOT NULL,
		venue CHARACTER VARYING(250) NOT NULL,
		street CHARACTER VARYING(250) NOT NULL,
		placeOfMarriage CHARACTER VARYING(250),
		locality CHARACTER VARYING(250) NOT NULL,
		city CHARACTER VARYING(250) NOT NULL,
		marriagePhoto CHARACTER VARYING(250) NOT NULL,
		fee INTEGER NOT NULL, 
        bridegroomid BIGINT,
        brideid BIGINT,
        priestname CHARACTER VARYING(250),
        priestreligion INTEGER,
        priestaddress CHARACTER VARYING(250),
		serialNo CHARACTER VARYING(250),
		volumeNo CHARACTER VARYING(250),
		applicationNumber CHARACTER VARYING(250),
        regnNumber CHARACTER VARYING(250),
		status CHARACTER VARYING(250),
		source CHARACTER VARYING(250),
		stateId CHARACTER VARYING(250),
        approvaldepartment INTEGER,
        approvaldesignation INTEGER,
        approvalassignee INTEGER,
        approvalaction CHARACTER VARYING(250),
		approvalstatus CHARACTER VARYING(250),
        aprovalcomments CHARACTER VARYING(250),
		tenantId CHARACTER VARYING(250) NOT NULL
)

