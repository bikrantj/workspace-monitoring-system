package com.bikrantj.client.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public final class DeviceInfo {
    private DeviceInfo() {
    }

    public static String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "Unknown Device";
        }
    }

    public static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();

                if (network.isLoopback() || network.isVirtual() || !network.isUp()) {
                    continue;
                }

                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }

                StringBuilder macAddress = new StringBuilder();
                for (byte b : mac) {
                    macAddress.append(String.format("%02X:", b));
                }

                return macAddress.substring(0, macAddress.length() - 1);
            }
        } catch (Exception ignored) {
        }
        return "Unavailable";
    }

    public static String getOsInfo() {
        try {
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");

            if (osName == null) {
                return "Unknown OS";
            }

            return osVersion != null
                    ? osName + " " + osVersion
                    : osName;

        } catch (SecurityException e) {
            return "OS Info Restricted";
        }
    }

    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces =
                    NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();

                if (network.isLoopback() || network.isVirtual() || !network.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    // Prefer IPv4
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "Unavailable";
    }
}
