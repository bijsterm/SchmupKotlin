package nl.bijster.kotlin.schmup

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxGame
import ktx.app.KtxScreen
import nl.bijster.kotlin.schmup.screen.MainMenuScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Shmup : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }      // public is the default modifier
    val rectBatch by lazy { ShapeRenderer() }      // public is the default modifier

    val font by lazy { BitmapFont() }

    override fun create() {

        Gdx.app.logLevel = Application.LOG_DEBUG

        rectBatch.color = Color.WHITE

        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
        super.create()
    }


    override fun dispose() {

        batch.dispose()
        rectBatch.dispose()
        font.dispose()
        super.dispose()
    }
}
