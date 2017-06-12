package entities.responses.login;

public class ResponseInfo {

    private String api_id;

    private String ts;

    private String status;

    private String msg_id;

    private String ver;

    private String res_msg_id;

    public String getApi_id() {
        return api_id;
    }

    public void setApi_id(String api_id) {
        this.api_id = api_id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public String getMsg_id() {
//        return msg_id;
//    }
//
//    public void setMsg_id(String msg_id) {
//        this.msg_id = msg_id;
//    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getRes_msg_id() {
        return res_msg_id;
    }

    public void setRes_msg_id(String res_msg_id) {
        this.res_msg_id = res_msg_id;
    }
}
