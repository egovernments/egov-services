package org.egov.egf.domain.util;

import org.egov.egf.persistence.queue.contract.User;

public class EgovThreadLocals {

	private static ThreadLocal<String> tenantId = new ThreadLocal<>();
	private static ThreadLocal<User> user = new ThreadLocal<>();

	public static ThreadLocal<String> getTenantId() {
		return tenantId;
	}

	public static void setTenantId(String tenantId) {
		ThreadLocal threadLocal = new ThreadLocal<String>();
		threadLocal.set(tenantId);
		EgovThreadLocals.tenantId = threadLocal;
	}

	public static ThreadLocal<User> getUser() {
		return user;
	}

	public static void setUser(ThreadLocal<User> user) {
		EgovThreadLocals.user = user;
	}

}
