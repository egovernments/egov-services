package org.egov.tradelicense.notification.util;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.trimou.engine.MustacheEngine;

@Service
/**
 * 
 * @author Shubham
 *
 */
public class TemplateUtil {

	private MustacheEngine templateEngine;

	public TemplateUtil(MustacheEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	/**
	 * 
	 * @param templateName
	 * @param map
	 * @return {@link String}
	 */
	public String loadByName(String templateName, Map<Object, Object> map) {
		return templateEngine.getMustache(templateName).render(map);
	}
}
