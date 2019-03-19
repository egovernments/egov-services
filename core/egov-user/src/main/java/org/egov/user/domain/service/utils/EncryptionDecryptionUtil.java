package org.egov.user.domain.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.User;
import org.egov.encryption.EncryptionService;
import org.egov.encryption.audit.AuditService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EncryptionDecryptionUtil
{
    private EncryptionService encryptionService;
    @Autowired
    private AuditService auditService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value(("${egov.state.level.tenant.id}"))
    private String stateLevelTenantId;

    public EncryptionDecryptionUtil(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public <T>T encryptObject(Object objectToEncrypt, String key,Class<T> classType)
    {
        try {
            return encryptionService.encryptJson(objectToEncrypt,key,stateLevelTenantId,classType);
        } catch (IOException e) {
            log.error("IO error occurred while decrypting",e);
            throw new CustomException("DECRYPTION_ERROR","IO error occurred while decrypting");
        }
    }

    public <E,P>P decryptObject(Object objectToDecrypt, String key, Class<E> classType, User userInfo)
    {

        try {
            final User encrichedUserInfo=getEncrichedandCopiedUserInfo(userInfo);
            key = getKeyToDecrypt(objectToDecrypt, encrichedUserInfo);
            P decryptedObject =  (P)encryptionService.decryptJson(objectToDecrypt,key,encrichedUserInfo,classType);
            auditTheDecryptRequest(objectToDecrypt, key, encrichedUserInfo);
            return decryptedObject;
        } catch (IOException e) {
            log.error("IO error occurred while decrypting",e);
            throw new CustomException("DECRYPTION_ERROR","IO error occurred while decrypting");
        }
    }

    public boolean isUserDecryptingForSelf(Object objectToDecrypt, User userInfo) {
        org.egov.user.domain.model.User userToDecrypt = null;
        if(objectToDecrypt instanceof List) {
            if(((List) objectToDecrypt).isEmpty())
                return false;
            if(((List) objectToDecrypt).size() > 1)
                return false;
            userToDecrypt = (org.egov.user.domain.model.User) ((List) objectToDecrypt).get(0);
        } else if(objectToDecrypt instanceof org.egov.user.domain.model.User) {
            userToDecrypt = (org.egov.user.domain.model.User) objectToDecrypt;
        } else {
            throw new CustomException(objectToDecrypt + " is not of type User", objectToDecrypt + " is not of type User");
        }

        if(userToDecrypt.getUuid().equalsIgnoreCase(userInfo.getUuid()))
            return true;
        else
            return false;
    }

    public String getKeyToDecrypt(Object objectToDecrypt, User userInfo) {
        boolean isSelf = isUserDecryptingForSelf(objectToDecrypt, userInfo);
        if(objectToDecrypt instanceof List) {
            if(isSelf)
                return "UserListSelf";
            else
                return "UserListOther";
        } else {
            if(isSelf)
                return "UserSelf";
            else
                return "UserOther";
        }
    }

    public void auditTheDecryptRequest(Object objectToDecrypt, String key, User userInfo) {
        String purpose = null;
        if(isUserDecryptingForSelf(objectToDecrypt, userInfo))
            purpose = "Self";
        else
            purpose = "";

        ObjectNode abacParams = objectMapper.createObjectNode();
        abacParams.set("key", TextNode.valueOf(key));

        List<String> decryptedUserUuid = new ArrayList<>();

        if(objectToDecrypt instanceof List) {
            decryptedUserUuid = (List<String>) ((List) objectToDecrypt).stream()
                            .map(user -> ((org.egov.user.domain.model.User) user).getUuid()).collect(Collectors.toList());
        } else if(objectToDecrypt instanceof org.egov.user.domain.model.User) {
            decryptedUserUuid.add(((org.egov.user.domain.model.User) objectToDecrypt).getUuid());
        }
        ObjectNode auditData = objectMapper.createObjectNode();
        auditData.set("entityType", TextNode.valueOf(User.class.getName()));
        auditData.set("decryptedEntityIds", objectMapper.valueToTree(decryptedUserUuid));
        auditService.audit(userInfo.getUuid(), System.currentTimeMillis(), purpose, abacParams, auditData);
    }

    private User getEncrichedandCopiedUserInfo(User userInfo)
    {
        List<Role>newRoleList=new ArrayList<>();
        if(userInfo.getRoles()!=null)
        {
            for(Role role:userInfo.getRoles())
            {
                Role newRole=Role.builder().code(role.getCode()).name(role.getName()).id(role.getId()).build();
                newRoleList.add(newRole);
            }
        }

        if(newRoleList.stream().filter(role -> role.getCode().equals(userInfo.getType())).count()==0)
        {
            Role roleFromtype=Role.builder().code(userInfo.getType()).name(userInfo.getType()).build();
            newRoleList.add(roleFromtype);
        }

        User newuserInfo=User.builder().id(userInfo.getId()).userName(userInfo.getUserName()).name(userInfo.getName())
                        .type(userInfo.getType()).mobileNumber(userInfo.getMobileNumber()).emailId(userInfo.getEmailId())
                        .roles(newRoleList).tenantId(userInfo.getTenantId()).uuid(userInfo.getUuid()).build();
        return  newuserInfo;
    }
}
