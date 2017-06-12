package entities.responses.userServices.getUser;

public class ResponseInfo {
    private Object ver;
    private Object resMsgId;
    private Object msgId;
    private Object apiId;
    private Object ts;
    private String status;

    public Object getVer() {
        return this.ver;
    }

    public void setVer(Object ver) {
        this.ver = ver;
    }

    public Object getResMsgId() {
        return this.resMsgId;
    }

    public void setResMsgId(Object resMsgId) {
        this.resMsgId = resMsgId;
    }

    public Object getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Object msgId) {
        this.msgId = msgId;
    }

    public Object getApiId() {
        return this.apiId;
    }

    public void setApiId(Object apiId) {
        this.apiId = apiId;
    }

    public Object getTs() {
        return this.ts;
    }

    public void setTs(Object ts) {
        this.ts = ts;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
