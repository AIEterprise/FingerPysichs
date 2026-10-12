package com.example.model

import java.util.Locale
import kotlin.math.log10
import kotlin.math.pow

enum class Language(val code: String, val label: String) {
    EN("en", "EN"),
    JA("ja", "日本語")
}

enum class ScaleTier(
    val id: String,
    val exponent: String,
    val titleEn: String,
    val titleJa: String,
    val descriptionEn: String,
    val descriptionJa: String,
    val benchmarkEn: String,
    val benchmarkJa: String
) {
    MACRO(
        id = "macro",
        exponent = "10⁻² ~ 10⁻³ m",
        titleEn = "Macro Fingertips",
        titleJa = "巨視スケール (指先)",
        descriptionEn = "Human index finger and thumb delicately closing a 20mm gap.",
        descriptionJa = "人差し指と親指が20mmの間隔を縮めていく人間の日常スケール。",
        benchmarkEn = "Coin thickness (~1.5 mm)",
        benchmarkJa = "硬貨の厚み (~1.5 mm)"
    ),
    FINGERPRINT(
        id = "print",
        exponent = "10⁻³ ~ 10⁻⁵ m",
        titleEn = "Epidermal Ridges",
        titleJa = "紋理スケール (指紋)",
        descriptionEn = "Skin surface reveals microscopic friction whorls, ridges, and sweat pores.",
        descriptionJa = "皮膚の微細構造、指紋の渦巻き隆線、汗孔が拡大される。",
        benchmarkEn = "Human hair (~70 µm)",
        benchmarkJa = "人間の毛髪の太さ (~70 µm)"
    ),
    CELLULAR(
        id = "cell",
        exponent = "10⁻⁵ ~ 10⁻⁸ m",
        titleEn = "Cellular Matrix",
        titleJa = "微視スケール (細胞)",
        descriptionEn = "Keratinocyte boundary, phospholipid membranes, and glowing nuclei.",
        descriptionJa = "表皮角化細胞、脂質二重層膜、クロマチンを宿す細胞核。",
        benchmarkEn = "Red blood cell (~7 µm)",
        benchmarkJa = "赤血球の直径 (~7 µm)"
    ),
    ATOMIC(
        id = "atom",
        exponent = "10⁻⁸ ~ 10⁻¹⁰ m",
        titleEn = "Quantum Atom",
        titleJa = "量子スケール (単一原子)",
        descriptionEn = "An isolated atom with nucleus and probabilistic electron cloud.",
        descriptionJa = "原子核と回転する確率的電子雲をもつ単一の原子を挟み込む。",
        benchmarkEn = "Bohr radius (~0.053 nm)",
        benchmarkJa = "ボーア半径 (~0.053 nm)"
    );

    companion object {
        fun fromGap(gapMeters: Double): ScaleTier {
            return when {
                gapMeters > 1.0e-3 -> MACRO
                gapMeters > 3.0e-5 -> FINGERPRINT
                gapMeters > 1.0e-8 -> CELLULAR
                else -> ATOMIC
            }
        }
    }
}

data class ZenoState(
    val gapMeters: Double = 0.020, // 20 mm initial
    val zenoStep: Int = 0,
    val tier: ScaleTier = ScaleTier.MACRO,
    val language: Language = Language.EN,
    val showGoldenRatio: Boolean = true,
    val showHud: Boolean = true,
    val isAutoHalving: Boolean = false
) {
    fun formattedDistance(): String {
        return when {
            gapMeters >= 1.0e-3 -> String.format(Locale.US, "%.2f mm", gapMeters * 1e3)
            gapMeters >= 1.0e-6 -> String.format(Locale.US, "%.1f µm", gapMeters * 1e6)
            gapMeters >= 1.0e-9 -> String.format(Locale.US, "%.2f nm", gapMeters * 1e9)
            else -> String.format(Locale.US, "%.1f pm", gapMeters * 1e12)
        }
    }

    fun formattedScientific(): String {
        if (gapMeters <= 0) return "0.00 m"
        val exp = log10(gapMeters).toInt()
        val mantissa = gapMeters / 10.0.pow(exp.toDouble())
        return String.format(Locale.US, "%.2f × 10%s m", mantissa, formatSuperscript(exp))
    }

    private fun formatSuperscript(exp: Int): String {
        val s = exp.toString()
        val map = mapOf(
            '-' to "⁻", '0' to "⁰", '1' to "¹", '2' to "²", '3' to "³",
            '4' to "⁴", '5' to "⁵", '6' to "⁶", '7' to "⁷", '8' to "⁸", '9' to "⁹"
        )
        return s.map { map[it] ?: it.toString() }.joinToString("")
    }

    fun getParadoxCommentary(): String {
        return if (language == Language.EN) {
            when {
                zenoStep == 0 ->
                    "Zeno's Dichotomy Paradox: Before reaching your destination, you must first traverse half the distance. Can movement ever truly complete?"
                zenoStep in 1..4 ->
                    "Step $zenoStep: The gap bisects (d = d₀ / 2^$zenoStep). An infinite sequence of finite intervals remains between the fingers."
                zenoStep in 5..10 ->
                    "Fingerprints reveal macroscopic contours. At ${formattedDistance()}, millions of microscopic divides still prevent contact."
                zenoStep in 11..18 ->
                    "Cellular scale unlocked. Electron repulsion forces begin resisting. Mathematically, halving can proceed infinitely without touching."
                else ->
                    "Single atom scale (${formattedDistance()}). In quantum mechanics, physical 'touching' is Pauli exclusion repulsion, not geometric zero."
            }
        } else {
            when {
                zenoStep == 0 ->
                    "ゼノンの二分法の逆説：目的地へ達する前に、まずその半分の距離を進まねばならない。終わりなき分割の中で、真の到達はあり得るか？"
                zenoStep in 1..4 ->
                    "第$zenoStep 分割：間隔が半分に縮小 (d = d₀ / 2^$zenoStep)。有限の距離の中に無限個の部分区間が存在し続けます。"
                zenoStep in 5..10 ->
                    "指紋の紋理が露出。残り${formattedDistance()}。幾何学的にはいくら二等分を繰り返しても、間隔が完全なゼロになることはありません。"
                zenoStep in 11..18 ->
                    "細胞膜スケール。静電反発力が働き始めます。無限のステップを重ねても、数学上の隙間は永遠に消失しません。"
                else ->
                    "原子スケール到達 (${formattedDistance()})。物理的な『接触』とは幾何学的ゼロではなく、パウリの排他律による電子雲の斥力です。"
            }
        }
    }
}
