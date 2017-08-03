package org.egov.user.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.ToString;

@Configuration
@Getter
@ToString
public class UserConfigurationUtil {

	@Value("${kafka.topics.save.user}")
	private String createUserTopic;
	@Value("${kafka.topics.update.user}")
	private String updateUserTopic;
	@Value("${egov.default.page.size}")
	private Integer pageSize;
}
