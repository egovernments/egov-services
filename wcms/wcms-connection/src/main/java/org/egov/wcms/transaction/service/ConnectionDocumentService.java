package org.egov.wcms.transaction.service;

import java.util.List;

import org.egov.wcms.transaction.model.ConnectionDocument;
import org.egov.wcms.transaction.repository.ConnectionDocumentRepository;
import org.egov.wcms.transaction.web.contract.ConnectionDocumentGetReq;
import org.egov.wcms.transaction.web.contract.ConnectionDocumentReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionDocumentService {
    public static final Logger logger = LoggerFactory.getLogger(ConnectionDocumentService.class);

    @Autowired
    private ConnectionDocumentRepository connectionDocumentRepository;

    public List<ConnectionDocument> create(final ConnectionDocumentReq connectionDocumentReq) {
        logger.info("ConnectionDocumentReq ::" + connectionDocumentReq);
        return connectionDocumentRepository.create(connectionDocumentReq);
    }

    public List<ConnectionDocument> search(final ConnectionDocumentGetReq connectionDocGetReq) {
        return connectionDocumentRepository.search(connectionDocGetReq);
    }

}
