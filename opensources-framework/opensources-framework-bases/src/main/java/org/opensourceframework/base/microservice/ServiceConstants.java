package org.opensourceframework.base.microservice;

/**
 * 系统常量定义
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public interface ServiceConstants {
    String APP_ID = "appId";
    String APP_SECRET = "appSecret";

    String REQ_APPLICATION_ID = "req.applicationId";

    String REQ_REMOTEHOST = "req.remoteHost";
    String REQ_REMOTEADDR = "req.remoteAddr";
    String REQ_REMOTEPORT = "req.remotePort";
    String REQ_LOCALADDR = "req.localAddr";
    String REQ_LOCALPORT = "req.localPort";
    String REQ_REQUESTURI = "req.requestURI";
    String REQ_REQUESTURL = "req.requestURL";
    String REQ_METHOD = "req.method";
    String REQ_QUERYSTRING = "req.queryString";
    String REQ_USERAGENT = "req.userAgent";
    String REQ_XFORWARDEDFOR = "req.xForwardedFor";
    String LOG_VERSION = "log.version";

    String SERVER_ROOTCONTEXTPATH = "server.rootContextPath";
    String SERVER_BASEPATH = "server.basePath";
    String SERVER_LOGWORKDIR = "server.logWorkDir";
    String SERVER_MODULE = "server.module";
    String SERVER_LOCALHOST = "server.localHost";


    String REQ_REQUESTSESSION = "req.requestSession";


    String REQ_USERID = "req.userId";
    String REQ_USERCODE = "req.userCode";
    String REQ_USERTYPE = "req.userType";
    String REQ_OPENID = "req.openId";
    String REQ_TERMINAL_TYPE = "req.terminal.type";
    String REQ_REQUESTID = "req.requestId";
    String REQ_TRENCH = "req.trench";
    String REQ_REMOTEIP = "req.remoteIp";

    String REQ_HEADER_AUTH = "auth";
    String REQ_USERID_DEFAULT = "anonymous";

    String LOCAL_HOST_IP = "127.0.0.1";
}
