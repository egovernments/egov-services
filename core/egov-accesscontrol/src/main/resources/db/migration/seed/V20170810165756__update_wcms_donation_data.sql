update service set name='Security Deposit' where name='Donation' and tenantId='default';

update eg_action set displayname='Create Security Deposit' where name='CreatDonationApi';
update eg_action set displayname='View Security Deposit' where name='SearchDonationApi';
update eg_action set displayname='Update Security Deposit' where name='ModifyDonationApi';

