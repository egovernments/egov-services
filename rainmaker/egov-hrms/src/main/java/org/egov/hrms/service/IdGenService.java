package org.egov.hrms.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdGenService {
	
	public String getId() {
		return UUID.randomUUID().toString();
	}
}
