update eg_action set servicecode = 'AbstractEstimate' where name in ('Estimate Create', 'Estimate Update', 'Estimate Search');
update eg_action set servicecode = 'WMS' where name in ('Project Code Create', 'Project Code Update', 'Project Code Search', 
'Estimate Appropriation Create', 'Estimate Appropriation Update', 'Estimate Appropriation Search', 'Document Detail Create', 
'Document Detail Update', 'Document Detail Search');