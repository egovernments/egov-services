update eg_action set url='/lcms-services/legalcase/summon/_create'  where name = 'New Case Create';
update eg_action set url='/lcms-services/legalcase/case/_dataentry'  where name = 'Legacy Case Create';
update eg_action set url='/lcms-services/legalcase/case/_search'  where name = 'Case Search';

update eg_action set url='/lcms-services/legalcase/advocate/_create'  where name = 'Advocate Create';
update eg_action set url='/lcms-services/legalcase/advocate/_update'  where name = 'Advocate Update';
update eg_action set url='/lcms-services/legalcase/advocate/_search'  where name = 'Advocate Search';

update eg_action set url='/lcms-services/legalcase/opinion/_create'  where name = 'Opinion Create';
update eg_action set url='/lcms-services/legalcase/opinion/_update'  where name = 'Opinion Update';
update eg_action set url='/lcms-services/legalcase/opinion/_search'  where name = 'Opinion Search';

update eg_action set url='/lcms-services/legalcase/advocatepayment/_create'  where name = 'AdvocatePayment Create';
update eg_action set url='/lcms-services/legalcase/advocatepayment/_update'  where name = 'AdvocatePayment Update';
update eg_action set url='/lcms-services/legalcase/advocatepayment/_search'  where name = 'AdvocatePayment Search';