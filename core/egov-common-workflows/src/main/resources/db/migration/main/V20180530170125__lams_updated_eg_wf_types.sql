
update eg_wf_types set type='New LeaseAgreement' where type in ('Create Corporation Agreement','Create Municipality Agreement');

update eg_wf_types set type='Renewal LeaseAgreement',displayname='Renewal Agreement' where type in ('Renew Corporation Agreement','Renew Municipality Agreement');

update eg_wf_types set type='Cancellation LeaseAgreement',displayname='Cancellation Agreement' where type in ('Cancel Corporation Agreement','Cancel Municipality Agreement');

update eg_wf_types set type='Eviction LeaseAgreement',displayname='Eviction Agreement' where type in ('Evict Corporation Agreement','Evict Municipality Agreement');

update eg_wf_types set type='Objection LeaseAgreement' where type in ('Objection Corporation Agreement','Objection Municipality Agreement');

update eg_wf_types set type='Judgement LeaseAgreement' where type in ('Judgement Corporation Agreement','Judgement Municipality Agreement');
