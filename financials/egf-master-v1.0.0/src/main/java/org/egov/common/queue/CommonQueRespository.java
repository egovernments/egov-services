package org.egov.common.queue;

import org.egov.common.web.contract.CommonRequest;

public interface CommonQueRespository<T> {
	void add(CommonRequest<T> request);

}
