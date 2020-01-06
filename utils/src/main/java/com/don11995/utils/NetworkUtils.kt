package com.don11995.utils

import android.content.Context
import android.net.ConnectivityManager
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.regex.Pattern

@Suppress("unused")
object NetworkUtils {

    private val VALID_IPV4_PATTERN = Pattern.compile("(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])")
    private val VALID_IPV6_PATTERN = Pattern.compile("([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}")

    @JvmStatic
    fun getLocalIPv4Address(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ignored: SocketException) {
        }
        return null
    }

    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetwork = cm?.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    @JvmStatic
    fun isValidIP(text: String): Boolean {
        return isValidIPv4(text) || isValidIPv6(text)
    }

    @JvmStatic
    fun isValidIPv4(text: String): Boolean {
        val matcher = VALID_IPV4_PATTERN.matcher(text)
        return matcher.matches()
    }

    @JvmStatic
    fun isValidIPv6(text: String): Boolean {
        val matcher = VALID_IPV6_PATTERN.matcher(text)
        return matcher.matches()
    }

}
