ALTER TABLE eglams_document DROP CONSTRAINT fk_eglams_document, ADD CONSTRAINT fk_eglams_document FOREIGN KEY (documenttype, tenantid)
      REFERENCES eglams_documenttype (id, tenantid);
