package com.inbedroom.pokedexter.features.model

import android.graphics.Color
import androidx.annotation.ColorInt

enum class PokemonType(val shortCode: String, @ColorInt val bgColor: Int, @ColorInt val strokeColor: Int) {
    NORMAL("nor", Color.parseColor("#b2b2a2"), Color.parseColor("#88887b")),
    FIRE("fir", Color.parseColor("#fe4422"), Color.parseColor("#d2371b")),
    WATER("wat", Color.parseColor("#3399fe"), Color.parseColor("#3b84cf")),
    GRASS("gra", Color.parseColor("#76cc55"), Color.parseColor("#5ea444")),
    ELECTRIC("ele", Color.parseColor("#ffcc33"), Color.parseColor("#cca329")),
    ICE("ice", Color.parseColor("#65ccff"), Color.parseColor("#51a7cf")),
    FIGHTING("fig", Color.parseColor("#ba5544"), Color.parseColor("#954436")),
    POISON("poi", Color.parseColor("#ab579a"), Color.parseColor("#8a467c")),
    GROUND("gro", Color.parseColor("#dfbf61"), Color.parseColor("#b59c51")),
    FLYING("fly", Color.parseColor("#8898ff"), Color.parseColor("#6c7acb")),
    PSYCHIC("psy", Color.parseColor("#ff68a4"), Color.parseColor("#d35889")),
    BUG("bug", Color.parseColor("#aaba23"), Color.parseColor("#87951c")),
    ROCK("roc", Color.parseColor("#bcad6b"), Color.parseColor("#998c57")),
    GHOST("gho", Color.parseColor("#6666ba"), Color.parseColor("#525295")),
    DRAGON("dra", Color.parseColor("#7666ee"), Color.parseColor("#5f51be")),
    DARK("dar", Color.parseColor("#775444"), Color.parseColor("#5f4437")),
    STEEL("ste", Color.parseColor("#adadbd"), Color.parseColor("#8c8c9a")),
    FAIRY("fai", Color.parseColor("#ee99ee"), Color.parseColor("#be7bbe")),
    UNKNOWN("unk", Color.parseColor("#b2b2a2"), Color.parseColor("#88887b")),
}