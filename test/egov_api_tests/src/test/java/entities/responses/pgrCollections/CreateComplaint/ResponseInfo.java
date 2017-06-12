package entities.responses.pgrCollections.CreateComplaint;

public class ResponseInfo {
    private String ver;
    private String resMsgId;
    private Object msgId;
    private Object apiId;
    private String ts;
    private String status;

    public String getVer() {
        return this.ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getResMsgId() {
        return this.resMsgId;
    }

    public void setResMsgId(String resMsgId) {
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

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
