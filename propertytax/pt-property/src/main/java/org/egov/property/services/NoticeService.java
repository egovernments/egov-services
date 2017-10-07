package org.egov.property.services;

import org.egov.models.NoticeRequest;
import org.egov.property.repository.NoticeMessageQueueRepository;
import org.egov.property.repository.NoticeRepository;
import org.springframework.stereotype.Service;

@Service
public class NoticeService {

    private NoticeRepository noticeRepository;

    private NoticeMessageQueueRepository noticeMessageQueueRepository;

    public NoticeService(NoticeRepository noticeRepository, NoticeMessageQueueRepository noticeMessageQueueRepository) {
        this.noticeRepository = noticeRepository;
        this.noticeMessageQueueRepository = noticeMessageQueueRepository;
    }

    public void pushToQueue(NoticeRequest noticeRequest){
        noticeMessageQueueRepository.save(noticeRequest);
    }

    public void create(NoticeRequest noticeRequest){
        noticeRepository.save(noticeRequest.getNotice());
    }
}
