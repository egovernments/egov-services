create table egmr_marriageregn_witness(
       id BIGINT PRIMARY KEY NOT NULL,
       applicationnumber CHARACTER VARYING(250) NOT NULL,  
       tenantid CHARACTER VARYING(250) NOT NULL, 
       witnessno INTEGER NOT NULL,
       name CHARACTER VARYING(250) NOT NULL, 
       relation CHARACTER VARYING(250) NOT NULL, 
       relatedto CHARACTER VARYING(250) NOT NULL,
       age INTEGER NOT NULL, 
       address CHARACTER VARYING(250) NOT NULL, 
       relationship CHARACTER VARYING(250) NOT NULL, 
       occupation CHARACTER VARYING(250), 
       aadhaar CHARACTER VARYING(250),
       mobileno CHARACTER VARYING(250),
       email CHARACTER VARYING(250),
       
       CONSTRAINT fk_egmr_witness_applicationnumber FOREIGN KEY (applicationnumber)
	   REFERENCES egmr_marriage_regn (applicationnumber)
)  
