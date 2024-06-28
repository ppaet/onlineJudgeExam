package com.zeke.bean;

import java.util.List;
import java.util.Map;

public class Cmd {
    private List<String> args;
    private List<String> env;
    private List<Object> files;
    private Boolean tty;
    private Long cpuLimit;
    private Long clockLimit;
    private Long memoryLimit;
    private Long stackLimit;
    private Integer procLimit;
    private Long cpuRateLimit;
    private String cpuSetLimit;
    private Boolean strictMemoryLimit;
    private Boolean dataSegmentLimit;
    private Boolean addressSpaceLimit;
    private Map<String, Object> copyIn;
    private List<String> copyOut;
    private List<String> copyOutCached;
    private Long copyOutMax;

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public List<String> getEnv() {
        return env;
    }

    public void setEnv(List<String> env) {
        this.env = env;
    }

    public List<Object> getFiles() {
        return files;
    }

    public void setFiles(List<Object> files) {
        this.files = files;
    }

    public Boolean getTty() {
        return tty;
    }

    public void setTty(Boolean tty) {
        this.tty = tty;
    }

    public Long getCpuLimit() {
        return cpuLimit;
    }

    public void setCpuLimit(Long cpuLimit) {
        this.cpuLimit = cpuLimit;
    }

    public Long getClockLimit() {
        return clockLimit;
    }

    public void setClockLimit(Long clockLimit) {
        this.clockLimit = clockLimit;
    }

    public Long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(Long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public Long getStackLimit() {
        return stackLimit;
    }

    public void setStackLimit(Long stackLimit) {
        this.stackLimit = stackLimit;
    }

    public Integer getProcLimit() {
        return procLimit;
    }

    public void setProcLimit(Integer procLimit) {
        this.procLimit = procLimit;
    }

    public Long getCpuRateLimit() {
        return cpuRateLimit;
    }

    public void setCpuRateLimit(Long cpuRateLimit) {
        this.cpuRateLimit = cpuRateLimit;
    }

    public String getCpuSetLimit() {
        return cpuSetLimit;
    }

    public void setCpuSetLimit(String cpuSetLimit) {
        this.cpuSetLimit = cpuSetLimit;
    }

    public Boolean getStrictMemoryLimit() {
        return strictMemoryLimit;
    }

    public void setStrictMemoryLimit(Boolean strictMemoryLimit) {
        this.strictMemoryLimit = strictMemoryLimit;
    }

    public Boolean getDataSegmentLimit() {
        return dataSegmentLimit;
    }

    public void setDataSegmentLimit(Boolean dataSegmentLimit) {
        this.dataSegmentLimit = dataSegmentLimit;
    }

    public Boolean getAddressSpaceLimit() {
        return addressSpaceLimit;
    }

    public void setAddressSpaceLimit(Boolean addressSpaceLimit) {
        this.addressSpaceLimit = addressSpaceLimit;
    }

    public Map<String, Object> getCopyIn() {
        return copyIn;
    }

    public void setCopyIn(Map<String, Object> copyIn) {
        this.copyIn = copyIn;
    }

    public List<String> getCopyOut() {
        return copyOut;
    }

    public void setCopyOut(List<String> copyOut) {
        this.copyOut = copyOut;
    }

    public List<String> getCopyOutCached() {
        return copyOutCached;
    }

    public void setCopyOutCached(List<String> copyOutCached) {
        this.copyOutCached = copyOutCached;
    }

    public Long getCopyOutMax() {
        return copyOutMax;
    }

    public void setCopyOutMax(Long copyOutMax) {
        this.copyOutMax = copyOutMax;
    }
}