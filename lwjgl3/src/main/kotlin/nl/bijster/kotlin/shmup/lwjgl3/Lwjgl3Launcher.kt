package nl.bijster.kotlin.shmup.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import nl.bijster.kotlin.schmup.Schmup


fun main() {
    createApplication()
}

private fun createApplication(): Lwjgl3Application {
    return Lwjgl3Application(Schmup(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("SchmupKotlin")
        useVsync(true)
        setWindowedMode(1280, 960)
        setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")
    })
}
