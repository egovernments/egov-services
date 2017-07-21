package org.egov.pgr.notification.persistence.repository.contract;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFilesByTagResponse {
    private List<FileRecord> files;
}
