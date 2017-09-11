
CREATE TABLE egmr_marriage_certificate(
        id character varying(250) NOT NULL,        
        certificateno CHARACTER VARYING(250),
	certificateDate BIGINT,
	certificatetype CHARACTER VARYING(250),
        regnnumber CHARACTER VARYING(250),
	bridegroomphoto CHARACTER VARYING(250),
	bridephoto CHARACTER VARYING(250),
	husbandname CHARACTER VARYING(250),
	husbandaddress CHARACTER VARYING(250),
	wifename CHARACTER VARYING(250),
	wifeaddress CHARACTER VARYING(250),
        marriagedate BIGINT,
        marriagevenueaddress CHARACTER VARYING(250),
        regndate BIGINT,
        regnserialno CHARACTER VARYING(250), 
        regnvolumeno CHARACTER VARYING(250),
        certificateplace CHARACTER VARYING(250),
	templateversion CHARACTER VARYING(250),
	applicationnumber CHARACTER VARYING(250),
        tenantId CHARACTER VARYING(250),
        createdby character varying(250) NOT NULL,
        lastmodifiedby character varying(250) NOT NULL,
        createdtime bigint NOT NULL,
        lastmodifiedtime bigint NOT NULL,
        CONSTRAINT pk_egmr_marriage_certificate PRIMARY KEY (id, tenantid)
)



