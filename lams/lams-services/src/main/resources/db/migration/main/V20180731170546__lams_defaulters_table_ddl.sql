
drop sequence if exists seq_eglams_defaulters;
drop table if exists  eglams_defaulters;

-------Defaulters DDL
create sequence seq_eglams_defaulters;
create table eglams_defaulters(id bigint, 
agreementnumber character varying(24),      
acknowledgenumber     character varying(24),      
rent            numeric(12,2),              
securitydeposit numeric(12,2),              
paymentcycle    character varying(16),      
commencementdate      timestamp without time zone,
expirydate      timestamp without time zone,
action          character varying(24),      
status          character varying(24),      
lastpaid        timestamp without time zone,
asset           bigint,
assetname       character varying,          
shopno          character varying,
assetcode       character varying(16),      
assetcategory   bigint, 
categoryname    character varying,          
allotteename    character varying(256),     
allotteeaddress character varying(1024),    
allotteemobilenumber  character varying(64),      
zone            bigint, 
ward            bigint ,
street          bigint ,
electionward    bigint, 
locality        bigint ,
block           bigint ,
tenantid        character varying(64)  
); 
-------Defaulter details DDL
drop sequence if exists seq_eglams_defaulters_details;
drop table if exists  eglams_defaulters_details;
create sequence seq_eglams_defaulters_details;
create table eglams_defaulters_details(
id                bigint  ,
agreementnumber   character varying(24),       
installment       character varying(24), 
fromdate          timestamp without time zone, 
todate            timestamp without time zone, 
amount            numeric(12,2 ) ,           
collection        numeric(12,2 ) ,           
balance           numeric(12,2 ) ,           
tenantid          character varying(64)      
);            

