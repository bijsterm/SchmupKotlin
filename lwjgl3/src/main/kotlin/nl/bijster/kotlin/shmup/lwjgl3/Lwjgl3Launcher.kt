package nl.bijster.kotlin.shmup.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import nl.bijster.kotlin.schmup.Shmup


fun main() {
    val config = Lwjgl3ApplicationConfiguration().apply {
        setTitle("SchmupKotlin")
        useVsync(true)
        setWindowedMode(1280, 960)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    }
    Lwjgl3Application(Shmup(), config)
}

