package org.egov.pgrrest.read.domain.factory;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import org.egov.pgrrest.read.domain.factory.utility.DateDifferenceInDays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class JSScriptEngineFactory {

    private long maxCPUTimeForScriptExecutionInMilliSeconds;
    private ExecutorService executorService;

    public JSScriptEngineFactory(@Value("${max.js.script.cpu.time}")String maxCPUTimeForScriptExecutionInMilliSeconds,
                                 @Value("${js.script.thread.count}")String jsEngineThreadPoolCount) {
        this.maxCPUTimeForScriptExecutionInMilliSeconds = Long.parseLong(maxCPUTimeForScriptExecutionInMilliSeconds);
        this.executorService = Executors.newFixedThreadPool(Integer.parseInt(jsEngineThreadPoolCount));
    }

    public NashornSandbox create() {
        final NashornSandbox nashornSandbox = NashornSandboxes.create();
        nashornSandbox.setMaxCPUTime(maxCPUTimeForScriptExecutionInMilliSeconds);
        nashornSandbox.setExecutor(executorService);
        nashornSandbox.inject(DateDifferenceInDays.NAME, new DateDifferenceInDays());
        return nashornSandbox;
    }

}
