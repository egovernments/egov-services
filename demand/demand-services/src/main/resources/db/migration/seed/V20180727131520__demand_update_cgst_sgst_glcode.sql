
  UPDATE eg_demand_reason SET glcode ='3502035' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='CENTRAL_GST' AND tenantid='default' );

  UPDATE eg_demand_reason SET glcode ='3502035' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='ADV_CGST' AND tenantid='default' );

  UPDATE eg_demand_reason SET glcode ='3502035' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='GW_CGST' AND tenantid='default' );

  UPDATE eg_demand_reason SET glcode ='3502034' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='STATE_GST' AND tenantid='default');

  UPDATE eg_demand_reason SET glcode ='3502034' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='ADV_SGST' AND tenantid='default');

  UPDATE eg_demand_reason SET glcode ='3502034' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='GW_SGST' AND tenantid='default');

  UPDATE eg_demand_reason SET glcode ='3502027' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='SERVICE_TAX' AND tenantid='default');

  UPDATE eg_demand_reason SET glcode ='3502027' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='ADV_ST' AND tenantid='default');

  UPDATE eg_demand_reason SET glcode ='3502027' WHERE id_demand_reason_master =(
  SELECT id FROM eg_demand_reason_master WHERE  module='Leases And Agreements' AND code='GW_ST' AND tenantid='default');

