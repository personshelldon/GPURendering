package com.don11995.utils

import android.os.Build
import android.webkit.MimeTypeMap
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*")
@PrepareForTest(MimeTypeGetter::class)
class MimeTypeGetterTest {

    @Rule
    @JvmField
    val rule: PowerMockRule = PowerMockRule()

    @Before
    fun setup() {
        val shadow = Shadows.shadowOf(MimeTypeMap.getSingleton())
        shadow.addExtensionMimeTypMapping("mp3", "audio/mp3")
    }

    @Test
    fun testGetExtension() {
        var result = MimeTypeGetter.getExtension("http://test.com")
        assertEquals(null, result)
        result = MimeTypeGetter.getExtension("http://test.com/01.mp3")
        assertEquals("mp3", result)
        result = MimeTypeGetter.getExtension("http://test.com/01.mp3/")
        assertEquals(null, result)
        result = MimeTypeGetter.getExtension("http://test.com/01.mp3#fragment")
        assertEquals("mp3", result)
        result = MimeTypeGetter.getExtension("http://test.com/01.mp3?a=2")
        assertEquals("mp3", result)
        result = MimeTypeGetter.getExtension("http://test.com/01.mp3#fragment?a=2")
        assertEquals("mp3", result)
        result = MimeTypeGetter.getExtension("http://test.iso.wv")
        assertEquals(null, result)
        result = MimeTypeGetter.getExtension("http://test.iso.wv/01.iso.wv")
        assertEquals("iso.wv", result)
    }

    @Test
    fun testGetMimeType() {

        var result = MimeTypeGetter.getMimeType(null)
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("")
        assertEquals(null, result)

        // return null
        result = MimeTypeGetter.getMimeType("file://test")
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("file:///test")
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("/test")
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("test")
        assertEquals(null, result)

        // return real mp3 mime type
        result = MimeTypeGetter.getMimeType("file://test.mp3")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("file:///test.mp3")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("/test.mp3")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("test.mp3")
        assertEquals("audio/mp3", result)

        // test different behaviours
        result = MimeTypeGetter.getMimeType("http://test.com")
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("http://test.com/01.mp3")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("http://test.com/01.mp3/")
        assertEquals(null, result)
        result = MimeTypeGetter.getMimeType("http://test.com/01.mp3#fragment")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("http://test.com/01.mp3?a=2")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("http://test.com/01.mp3#fragment?a=2")
        assertEquals("audio/mp3", result)
        result = MimeTypeGetter.getMimeType("http://test.iso.wv")
        assertEquals(null, result)
        result = MimeTypeGetter.getExtension("http://test.iso.wv/01.iso.wv")
        assertEquals("iso.wv", result)
    }

}
