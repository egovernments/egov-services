package org.egov.lams.repository;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.egov.lams.model.Notice;
import org.egov.lams.repository.builder.NoticeQueryBuilder;
import org.egov.lams.web.contract.NoticeRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class NoticeRepositoryTest {
	
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Mock
	private NoticeQueryBuilder noticeQueryBuilder;
	
	@InjectMocks
	private NoticeRepository noticeRepository;
	
	@Test
	public void createNoticeTest(){
		
		Notice notice = new Notice();
		notice.setNoticeNo("0000000001");
		NoticeRequest noticeRequest = new NoticeRequest();
		noticeRequest.setNotice(notice);
		
		RequestInfo info = new RequestInfo();
		noticeRequest.setRequestInfo(info);
		
		Object[] obj = new Object[]{};
		when(jdbcTemplate.update("insert",obj)).thenReturn(1);
		when(jdbcTemplate.queryForObject("next val",Integer.class)).thenReturn(1);
		
		assertTrue(notice.equals(noticeRepository.createNotice(noticeRequest)));
	}
	
	@Test
	public void getNoticesTest(){
		
	}
	
	
	

}
