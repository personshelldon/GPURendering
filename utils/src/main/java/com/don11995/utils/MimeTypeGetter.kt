package com.don11995.utils

import android.net.Uri
import android.text.TextUtils
import android.webkit.MimeTypeMap
import java.util.*
import java.util.regex.Pattern

@Suppress("unused")
object MimeTypeGetter {

    private val EXTENSION_PATTERN = Pattern.compile("[A-z0-9]{1,5}")
    private const val EXTENSION_GIF = "gif"
    private const val EXTENSION_M3U = "m3u"
    private const val EXTENSION_PLS = "pls"
    private const val EXTENSION_TXT = "txt"

    private val sAdditionalMimeTypes = HashMap<String, String>()
    private val specialExtensions = listOf("iso.wv")

    init {
        sAdditionalMimeTypes["wtv"] = "video/x-ms-wtv"
        sAdditionalMimeTypes["ts"] = "video/ts"
        sAdditionalMimeTypes["m2ts"] = "video/m2ts"
        sAdditionalMimeTypes["mkv"] = "video/mkv"
        sAdditionalMimeTypes["flv"] = "video/x-flv"
        sAdditionalMimeTypes["ogv"] = "video/ogg"
        sAdditionalMimeTypes["vob"] = "video/x-ms-vob"
        sAdditionalMimeTypes["divx"] = "video/divx"
        sAdditionalMimeTypes["m2v"] = "video/mpeg"
        sAdditionalMimeTypes["tif"] = "image/tif"
        sAdditionalMimeTypes["tiff"] = "image/tif"
        sAdditionalMimeTypes["srt"] = "text/plain"
        sAdditionalMimeTypes["ape"] = "audio/x-monkeys-audio"
        sAdditionalMimeTypes["wwc"] = "audio/x-wavpack"
        sAdditionalMimeTypes["wv"] = "audio/x-wavpack"
        sAdditionalMimeTypes["iso.wv"] = "audio/x-wavpack"
        sAdditionalMimeTypes["dff"] = "audio/x-dsf"
        sAdditionalMimeTypes["dsf"] = "audio/x-dsd"
        sAdditionalMimeTypes["dsd"] = "audio/x-dsd"
        sAdditionalMimeTypes["ac3"] = "audio/ac3"
        sAdditionalMimeTypes["cue"] = "audio/x-cue"
        sAdditionalMimeTypes["ogg"] = "audio/ogg"
    }

    /**
     * Detects the mime type by format in the uri
     *
     * @param uri path to retrieve the mime type
     * @return detected mime type or null if unknown
     */
    @JvmStatic
    fun getMimeType(uri: String?): String? {
        if (uri == null) return null
        var result: String? = null
        val extension = getExtension(uri)
        if (!TextUtils.isEmpty(extension)) {
            result = sAdditionalMimeTypes[extension]
        }
        if (!TextUtils.isEmpty(extension) && TextUtils.isEmpty(result)) {
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        if (TextUtils.isEmpty(result)) {
            result = null
        }
        return result
    }

    /**
     * Returns true if the uri is a video, audio, picture or playlist
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has a media extension
     */
    @JvmStatic
    fun isMediaFile(uri: String?): Boolean {
        return isVideoFile(uri)
                || isAudioFile(uri)
                || isImageFile(uri)
                || isPlaylistFile(uri)
    }

    /**
     * Returns true if the uri is a video
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has a video extension
     */
    @JvmStatic
    fun isVideoFile(uri: String?): Boolean {
        val mimeType = getMimeType(uri)
        return !TextUtils.isEmpty(mimeType) && mimeType!!.startsWith("video")
    }

    /**
     * Returns true if the uri is an audio, but not a playlist
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has an audio extension
     */
    @JvmStatic
    fun isAudioFile(uri: String?): Boolean {
        val mimeType = getMimeType(uri)
        return !TextUtils.isEmpty(mimeType)
                && mimeType!!.startsWith("audio")
                && !isPlaylistFile(uri)
    }

    /**
     * Returns true if the uri is an image
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has an image extension
     */
    @JvmStatic
    fun isImageFile(uri: String?): Boolean {
        val mimeType = getMimeType(uri)
        return !TextUtils.isEmpty(mimeType) && mimeType!!.startsWith("image")
    }

    /**
     * Returns true if the uri has a PLS or M3U extension
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has a PLS or M3U extension
     */
    @JvmStatic
    fun isPlaylistFile(uri: String?): Boolean {
        val format = getExtension(uri)
        return EXTENSION_M3U == format || EXTENSION_PLS == format
    }

    /**
     * Returns true if the uri has a TXT extension
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has a TXT extension
     */
    @JvmStatic
    fun isTextFile(uri: String?): Boolean {
        val format = getExtension(uri)
        return EXTENSION_TXT == format
    }

    /**
     * Returns true if the uri has a GIF extension
     *
     * @param uri path to retrieve the extension
     * @return true if the uri has a GIF extension
     */
    @JvmStatic
    fun isGifFile(uri: String?): Boolean {
        return EXTENSION_GIF == getExtension(uri)
    }

    /**
     * Returns true if the file extension is a video, audio, picture or playlist
     *
     * @param uriToGet path to retrieve the extension
     * @return format in lower case or null
     */
    @JvmStatic
    fun getExtension(uriToGet: String?): String? {
        var uri = uriToGet
        if (TextUtils.isEmpty(uri)) return null
        if (!isLocalFile(uri)) {
            uri = Uri.parse(uri).path
        }
        if (uri == null) return null
        uri = uri.toLower()
        var index = uri.lastIndexOf('?')
        if (index >= 0) {
            uri = uri.substring(0, index)
        }
        index = uri.lastIndexOf('#')
        if (index >= 0) {
            uri = uri.substring(0, index)
        }
        val specialExtension = checkSpecialExtensions(uri)
        if (specialExtension != null) return specialExtension
        val lastDot = uri.lastIndexOf('.')
        if (lastDot >= 0 && lastDot + 1 < uri.length) {
            uri = uri.substring(lastDot + 1)
            if (EXTENSION_PATTERN.matcher(uri).matches()) {
                return uri.toLower()
            }
        }
        return null
    }

    private fun isLocalFile(uri: String?): Boolean = !TextUtils.isEmpty(uri)
            && (uri!!.startsWith("file://")
            || uri.startsWith("/")
            || !uri.contains("://"))

    private fun checkSpecialExtensions(path: String): String? {
        return specialExtensions.firstOrNull { path.endsWith(".$it") }
    }
}
