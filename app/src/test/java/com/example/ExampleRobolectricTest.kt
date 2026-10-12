package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.ScaleTier
import com.example.model.ZenoState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Zeno's Paradox", appName)
    }

    @Test
    fun `zeno scale tier classification and formatting`() {
        // Macro scale
        val macro = ZenoState(gapMeters = 0.020, zenoStep = 0, tier = ScaleTier.MACRO)
        assertEquals("20.00 mm", macro.formattedDistance())
        assertEquals(ScaleTier.MACRO, ScaleTier.fromGap(0.020))

        // Fingerprint scale
        val print = ZenoState(gapMeters = 5.0e-4, zenoStep = 5, tier = ScaleTier.FINGERPRINT)
        assertEquals("500.0 µm", print.formattedDistance())
        assertEquals(ScaleTier.FINGERPRINT, ScaleTier.fromGap(5.0e-4))

        // Cellular scale
        val cell = ZenoState(gapMeters = 2.0e-6, zenoStep = 13, tier = ScaleTier.CELLULAR)
        assertEquals("2.0 µm", cell.formattedDistance())
        assertEquals(ScaleTier.CELLULAR, ScaleTier.fromGap(2.0e-6))

        // Atomic scale
        val atom = ZenoState(gapMeters = 1.0e-10, zenoStep = 28, tier = ScaleTier.ATOMIC)
        assertEquals("100.0 pm", atom.formattedDistance())
        assertEquals(ScaleTier.ATOMIC, ScaleTier.fromGap(1.0e-10))
    }
}
