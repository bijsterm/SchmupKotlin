package nl.bijster.kotlin.shmup.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.constants.SCREEN_HEIGHT
import nl.bijster.kotlin.schmup.constants.SCREEN_WIDTH


fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("SchmupKotlin")
        useVsync(true)
        setWindowedMode(SCREEN_WIDTH.toInt(), SCREEN_HEIGHT.toInt())
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    }
    Lwjgl3Application(Shmup(), config)
}

