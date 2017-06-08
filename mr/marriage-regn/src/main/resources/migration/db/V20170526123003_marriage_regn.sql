CREATE SEQUENCE seq_egmr_marriageregn_application_number;
create sequence seq_egmr_regn_number;


CREATE TABLE egmr_marriage_regn(
        regnunitid INTEGER,
		marriageDate BIGINT NOT NULL,
		venue CHARACTER VARYING(250) NOT NULL,
		street CHARACTER VARYING(250) NOT NULL,
		placeofmarriage CHARACTER VARYING(250),
		locality CHARACTER VARYING(250) NOT NULL,
		city CHARACTER VARYING(250) NOT NULL,
		marriagephoto CHARACTER VARYING(250) NOT NULL,
		fee INTEGER NOT NULL, 
        bridegroomid BIGINT,
        brideid BIGINT,
        priestname CHARACTER VARYING(250),
        priestreligion INTEGER,
        priestaddress CHARACTER VARYING(250),
        priestaadhaar CHARACTER VARYING(250),
        priestmobileno CHARACTER VARYING(250),
        priestemail CHARACTER VARYING(250),
		serialno CHARACTER VARYING(250),
		volumeno CHARACTER VARYING(250),
		applicationnumber CHARACTER VARYING(250),
        regnnumber CHARACTER VARYING(250),
        regndate BIGINT,
		status CHARACTER VARYING(250),
		source CHARACTER VARYING(250),
		stateid CHARACTER VARYING(250),
		isactive BOOLEAN,
        approvaldepartment INTEGER,
        approvaldesignation INTEGER,
        approvalassignee INTEGER,
        approvalaction CHARACTER VARYING(250),
		approvalstatus CHARACTER VARYING(250),
        aprovalcomments CHARACTER VARYING(250),
        createdby CHARACTER VARYING(250) NOT NULL,
        lastmodifiedby CHARACTER VARYING(250) NOT NULL,
        createdtime BIGINT NOT NULL,
        lastmodifiedtime BIGINT NOT NULL,
		tenantId CHARACTER VARYING(250) NOT NULL,
		
        CONSTRAINT pk_egmr_applicationnumber PRIMARY KEY (applicationnumber),
  		CONSTRAINT uk_egmr_bridegroomid UNIQUE (bridegroomid),
		CONSTRAINT uk_egmr_brideid UNIQUE (brideid),
		CONSTRAINT uk_egmr_regnnumber UNIQUE (regnnumber)
)

