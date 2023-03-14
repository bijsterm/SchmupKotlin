package nl.bijster.kotlin.schmup

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import nl.bijster.kotlin.schmup.screen.MainMenuScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Shmup : KtxGame<KtxScreen>() {
    val batch by lazy {SpriteBatch()}      // public is the default modifier
    val font by lazy {BitmapFont()}

    override fun create() {
        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
        super.create()
    }


    override fun dispose() {

        batch.dispose()
        font.dispose()
        super.dispose()
    }
}
