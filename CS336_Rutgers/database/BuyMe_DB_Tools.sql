use cs336buyme;

-- In RDS instance parameter group:
-- Set set log_bin_trust_function_creators to 1 
-- Set event_scheduler to "ON"

Show events;

Show processlist;

-- SET GLOBAL event_scheduler = ON;

SELECT * FROM INFORMATION_SCHEMA.events;

select * from Offer;


show triggers;
