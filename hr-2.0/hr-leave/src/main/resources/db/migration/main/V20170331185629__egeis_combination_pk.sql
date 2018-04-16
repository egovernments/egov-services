---------------------------- DROP FOREIGN KEYS ----------------------------

ALTER TABLE egeis_leaveallotment DROP CONSTRAINT fk_egeis_leaveallotment_leavetypeid;
ALTER TABLE egeis_leaveapplication DROP CONSTRAINT fk_egeis_leaveapplication_leavetypeid;
ALTER TABLE egeis_leaveopeningbalance DROP CONSTRAINT fk_egeis_leaveopeningbalance_leavetypeid;


--------------------------- UPDATE PRIMARY KEYS ---------------------------

-- EGEIS_LEAVETYPE TABLE
ALTER TABLE egeis_leavetype DROP CONSTRAINT pk_egeis_leavetype;
ALTER TABLE egeis_leavetype ADD CONSTRAINT pk_egeis_leavetype PRIMARY KEY (id, tenantId);

-- EGEIS_LEAVEALLOTMENT TABLE
ALTER TABLE egeis_leaveallotment DROP CONSTRAINT pk_egeis_leaveallotment;
ALTER TABLE egeis_leaveallotment ADD CONSTRAINT pk_egeis_leaveallotment PRIMARY KEY (id, tenantId);

-- EGEIS_LEAVEAPPLICATION TABLE
ALTER TABLE egeis_leaveapplication DROP CONSTRAINT pk_egeis_leaveapplication;
ALTER TABLE egeis_leaveapplication ADD CONSTRAINT pk_egeis_leaveapplication PRIMARY KEY (id, tenantId);

-- EGEIS_LEAVEOPENINGBALANCE TABLE
ALTER TABLE egeis_leaveopeningbalance DROP CONSTRAINT pk_egeis_leaveopeningbalance;
ALTER TABLE egeis_leaveopeningbalance ADD CONSTRAINT pk_egeis_leaveopeningbalance PRIMARY KEY (id, tenantId);


-------------------------- RECREATE FOREIGN KEYS --------------------------

ALTER TABLE egeis_leaveallotment ADD CONSTRAINT fk_egeis_leaveallotment_leavetypeid FOREIGN KEY (leavetypeid, tenantId)
	REFERENCES egeis_leavetype (id, tenantId);
ALTER TABLE egeis_leaveapplication ADD CONSTRAINT fk_egeis_leaveapplication_leavetypeid FOREIGN KEY (leavetypeid, tenantId)
	REFERENCES egeis_leavetype (id, tenantId);
ALTER TABLE egeis_leaveopeningbalance ADD CONSTRAINT fk_egeis_leaveopeningbalance_leavetypeid FOREIGN KEY (leavetypeid, tenantId)
	REFERENCES egeis_leavetype (id, tenantId);

