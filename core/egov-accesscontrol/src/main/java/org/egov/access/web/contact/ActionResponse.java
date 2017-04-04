package org.egov.access.web.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ActionResponse {

    private  ResponseInfo responseInfo;

    private List<ActionContract> actions;

}
