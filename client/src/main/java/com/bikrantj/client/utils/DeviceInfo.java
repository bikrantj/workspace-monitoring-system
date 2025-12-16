package com.bikrantj.client.utils;

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
}
