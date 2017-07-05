create table egasset_yearwisedepreciation
(	
	depreciationrate  numeric NOT NULL ,
	financialyear     character varying(20) NOT NULL,
	assetid           bigint NOT NULL ,
	usefullifeinyears bigint 
);