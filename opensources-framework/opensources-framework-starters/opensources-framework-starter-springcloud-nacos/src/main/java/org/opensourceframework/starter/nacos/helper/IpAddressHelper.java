package org.opensourceframework.starter.nacos.helper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * IpAddress 工具帮助类
 *
 * @author  yu.ce@foxmail.com
 * 
 * @since   1.0.0
 */
public class IpAddressHelper {
    private static final Logger logger = LoggerFactory.getLogger(IpAddressHelper.class);

    public static List<InetAddress> resolveLocalAddresses() {
        List<InetAddress> addrs = new ArrayList();
        Enumeration ns = null;

        try {
            ns = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
        }

        while(ns != null && ns.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface)ns.nextElement();
            Enumeration is = n.getInetAddresses();

            while(is.hasMoreElements()) {
                InetAddress i = (InetAddress)is.nextElement();
                if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() && !i.isMulticastAddress() && !isSpecialIp(i.getHostAddress())) {
                    addrs.add(i);
                }
            }
        }

        return addrs;
    }

    public static String resolveLocalIp() {
        List<InetAddress> addrs = resolveLocalAddresses();
        String ip = null;
        if (!addrs.isEmpty()) {
            ip = addrs.get(0).getHostAddress();
        }else {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static List<String> resolveLocalIps() {
        List<InetAddress> addrs = resolveLocalAddresses();
        List<String> ret = new ArrayList();
        Iterator iterator = addrs.iterator();

        while(iterator.hasNext()) {
            InetAddress addr = (InetAddress)iterator.next();
            ret.add(addr.getHostAddress());
        }

        return ret;
    }

    private static boolean isSpecialIp(String ip) {
        if(StringUtils.isNotBlank(ip)){
            if (ip.contains(":")) {
                return true;
            } else if (ip.startsWith("127.")) {
                return true;
            } else if (ip.startsWith("169.254.")) {
                return true;
            } else {
                return ip.equals("255.255.255.255");
            }
        }else {
            return false;
        }
    }

    /**
     * 获取本机ip地址，并自动区分Windows还是linux操作系统
     *
     * @return String
     */
    public static String getLocalHost() {
        String sIP = null;
        InetAddress ip = null;
        try {
            // 如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            } else {
                // 如果是Linux操作系统
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = netInterfaces.nextElement();
                    // 特定情况，可以考虑用ni.getName判断 遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        ip = ips.nextElement();
                        // 127.开头的都是lookback地址
                        String indexChar = ":";
                        if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                                && ip.getHostAddress().indexOf(indexChar) == -1) {
                            bFindIP = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

    /**
     * @return boolean    返回类型
     * @Title: isWindowsOS
     * @Description: 判断是否是windows操作系统
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
}
