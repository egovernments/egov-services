CREATE SEQUENCE seq_marriageregn_id;

CREATE TABLE marriage_regn(
		id BIGINT PRIMARY KEY,  
        regnunitid INTEGER,
		marriageDate BIGINT,
		venue CHARACTER VARYING(250),
		street CHARACTER VARYING(250),
		placeOfMarriage CHARACTER VARYING(250),
		locality CHARACTER VARYING(250),
		city CHARACTER VARYING(250),
		marriagePhoto CHARACTER VARYING(250),
		fee INTEGER, 
        bridegroomid CHARACTER VARYING(250),
        brideid CHARACTER VARYING(250),
        priestname CHARACTER VARYING(250),
        priestreligion INTEGER,
        priestaddress CHARACTER VARYING(250),
		serialNo CHARACTER VARYING(250),
		volumeNo CHARACTER VARYING(250),
		applicationNumber CHARACTER VARYING(250),
		status CHARACTER VARYING(250),
		source CHARACTER VARYING(250),
		stateId CHARACTER VARYING(250),
		tenantId CHARACTER VARYING(250)
)