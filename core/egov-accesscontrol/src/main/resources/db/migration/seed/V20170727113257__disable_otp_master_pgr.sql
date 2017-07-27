---OTP CONFIGURATION---
update eg_action set enabled =false where name = 'Create OTP Config' and url = '/pgr-master/OTPConfig/_create'  and servicecode = 'OTPCONFIG' ;
update eg_action set enabled =false where name = 'Update OTP Config' and url = '/pgr-master/OTPConfig/_update'  and servicecode = 'OTPCONFIG' ;
update eg_action set enabled =false where name = 'Search OTP Config' and url = '/pgr-master/OTPConfig/_search'  and servicecode = 'OTPCONFIG' ;

---
update eg_action set enabled =false where name = 'Update Escalation' and url = '/pgr-master/escalation/_update'  and servicecode = 'ESCL' ;