package app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("sync")
public class SyncConfig {
    private List<SyncInfo> info;

    public List<SyncInfo> getInfo() {
        return info;
    }

    public void setInfo(List<SyncInfo> info) {
        this.info = info;
    }
}
