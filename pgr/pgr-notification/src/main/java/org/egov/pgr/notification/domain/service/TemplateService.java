package org.egov.pgr.notification.domain.service;

import org.springframework.stereotype.Service;
import org.trimou.engine.MustacheEngine;

import java.util.Map;

@Service
public class TemplateService {

	private MustacheEngine templateEngine;

	public TemplateService(MustacheEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

    public String loadByName(String templateName, Map<Object, Object> map) {
        return templateEngine.getMustache(templateName).render(map);
    }

}
