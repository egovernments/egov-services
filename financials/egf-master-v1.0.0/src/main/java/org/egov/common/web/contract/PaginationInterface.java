package org.egov.common.web.contract;

public interface PaginationInterface {

	public static int DEFAULT_PAGE_SIZE = 500;
	public static int DEFAULT_PAGE_OFFSET = 0;

	public Integer pageSize = Integer.valueOf(DEFAULT_PAGE_SIZE);

	public Integer offSet = Integer.valueOf(DEFAULT_PAGE_OFFSET);

}
