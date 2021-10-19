package org.opensourceframework.component.mq.helper;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * 本地地址操作类
 *
 * @author yu.ce@foxmail.com
 * @since 1.0.0
 *
 */
public class LocalIpAddress {
    public LocalIpAddress() {
    }

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
}
