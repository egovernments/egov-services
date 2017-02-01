 
/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.egov.boundary.service;

import static org.junit.Assert.assertNotNull;

import org.egov.boundary.model.HierarchyType;
import org.egov.boundary.repository.HierarchyTypeRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.autoconfigure.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author Dave Syer
 */
 
// FIXME until spring data commons 1.6.0, jpa 1.5.0 available
public class JpaWebAutoConfigurationTests {

	private AnnotationConfigWebApplicationContext context;

	@Test
	public void testDefaultRepositoryConfiguration() throws Exception {
		this.context = new AnnotationConfigWebApplicationContext();
		this.context.setServletContext(new MockServletContext());
		this.context.register(TestConfiguration.class,
				DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
				JpaRepositoriesAutoConfiguration.class,
				PropertyPlaceholderAutoConfiguration.class);
		this.context.refresh();
		assertNotNull(this.context.getBean(HierarchyTypeRepository.class));
		assertNotNull(this.context.getBean(DomainClassConverter.class));
	}

	@Configuration
	// @EnableSpringDataWebSupport
	@ComponentScan(basePackageClasses = HierarchyType.class)
	protected static class TestConfiguration {

	}

}