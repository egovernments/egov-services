ALTER TABLE egeis_leaveAllotment ALTER COLUMN noofdays TYPE REAL USING noofdays::REAL;
ALTER TABLE egeis_leaveapplication ALTER COLUMN leavedays TYPE REAL USING leavedays::REAL;
ALTER TABLE egeis_leaveapplication ALTER COLUMN availabledays TYPE REAL USING availabledays::REAL;
ALTER TABLE egeis_leaveopeningbalance ALTER COLUMN noofdays TYPE REAL USING noofdays::REAL;