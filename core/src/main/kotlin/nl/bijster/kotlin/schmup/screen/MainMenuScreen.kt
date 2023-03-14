package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.app.KtxScreen
import nl.bijster.kotlin.schmup.Shmup

/** First screen of the application. Displayed after the application is created.  */
class MainMenuScreen(val shmup: Shmup) : KtxScreen {
    private val camera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 480f)
    }

    override fun render(delta: Float) {
        camera.update()
        shmup.batch.setProjectionMatrix(camera.combined)

        shmup.batch.begin()
        shmup.font.draw(shmup.batch, "Welcome to Drop!!! ", 100f, 150f)
        shmup.font.draw(shmup.batch, "Tap anywhere or press any key to begin!", 100f, 100f)
        shmup.batch.end()

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            shmup.addScreen(GameScreen(shmup))
            shmup.setScreen<GameScreen>()
            shmup.removeScreen<MainMenuScreen>()
            dispose()
        }
    }

    override fun show() {
        shmup.font.data.setScale(1f)
    }

}
