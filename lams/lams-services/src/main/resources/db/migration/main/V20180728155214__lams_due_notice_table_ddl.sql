
create sequence seq_eglams_duenotice;
create table eglams_duenotice(
id bigint,
noticeno character varying,
noticedate timestamp without time zone,
agreementnumber character varying,
assetcode character varying ,
assetcategory bigint,
categoryname character varying,
allotteename character varying,
mobilenumber character varying,
commencementdate timestamp without time zone,
expirydate timestamp without time zone,
duefromdate timestamp without time zone,
duetodate timestamp without time zone,
action character varying,
status character varying,
createddate timestamp without time zone,
createdby bigint, 
lastmodifieddate timestamp without time zone,
lastmodifiedby bigint,
ward bigint,
noticetype character varying,
filestore character varying, 
tenantId character varying
);
