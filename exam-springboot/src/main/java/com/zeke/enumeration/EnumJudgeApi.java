package com.zeke.enumeration;

public enum EnumJudgeApi {
    RUN(1, "在受限制的环境中运行程序", "/run", "POST"),
    FILE(2, "得到所有在文件存储中的文件 ID 到原始命名映射", "/file", "GET"),
    UPLOAD_FILE(3, "上传一个文件到文件存储，返回一个文件 ID 用于提供给 /run 接口", "/file", "POST"),
    DOWNLOA_FILE(4, "下载文件 ID 指定的文件", "/file/%s", "GET"),
    DELETE_FILE(5, "删除文件 ID 指定的文件","/file/%s", "DELETE"),
    WS(6, "/run 接口的 WebSocket 版", "/ws", "GET"),
    STREAM(7, "运行交互式命令", "/stream", "GET"),
    VERSION(8, " 得到本程序编译版本和 go 语言运行时版本", "/version", "get"),
    CONFIG(9, " 得到本程序部分运行参数，包括沙箱详细参数", "/config", "GET");


    EnumJudgeApi(Integer code, String name, String api, String method) {
        this.code = code;
        this.name = name;
        this.api = api;
        this.method = method;
    }

    private Integer code;

    private String name;

    private String api;

    private String method;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
