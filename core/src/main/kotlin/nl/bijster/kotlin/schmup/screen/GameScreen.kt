package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.OrthographicCamera
import ktx.app.KtxScreen
import ktx.graphics.use
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.attackwave.AttackWave
import nl.bijster.kotlin.schmup.attackwave.drops.Drops
import nl.bijster.kotlin.schmup.player.Player
import nl.bijster.kotlin.schmup.types.HiScoreTable


class GameScreen(private val shmup: Shmup) : KtxScreen {

    private val player: Player = Player(3)
    private val drops: AttackWave = Drops()

    // load the drop sound effect and the rain background music
    private val rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3")).apply {
        isLooping = true
    }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera: OrthographicCamera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 600f)
    }


    private var score: Int = 0



    override fun render(delta: Float) {
        // Alle updates

        // 1. Player first!!
        player.update(delta)

        // 2. Player Bullets

        // 3. Attack-waves
        drops.update(delta)

        drops.detectAndHandleCollision(player.playerGameObject)

        if (player.playerGameObject.hasCollided) {
            val gameOver = player.die()
            if (gameOver) {
                switchScreens()
                HiScoreTable.add(score, 1.0)
            }

        }

        // begin a new batch and draw all
        shmup.batch.use {
            // 0. Text
            shmup.font.draw(it, "Score: $score", 0f, 600f)
            shmup.font.draw(it, "Nr. Lives: ${player.nrOfLives}", 500f, 600f)
            // 1. Draw player first
            player.draw(it)

            drops.draw(it)

            // 3. Bullets

            // 4. Player Hit-box, must be upper-level!

        }

    }

    private fun switchScreens() {
        shmup.addScreen(MainMenuScreen(shmup))
        shmup.setScreen<MainMenuScreen>()
        shmup.removeScreen<GameScreen>()
        dispose()
    }

    override fun show() {

        player.init()
        drops.init()

        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        shmup.batch.projectionMatrix = camera.combined


        shmup.font.data.setScale(3f)

        // start the playback of the background music when the screen is shown
        // TODO: rainMusic.play()
    }

    override fun dispose() {
        rainMusic.dispose()

        player.dispose()
        drops.cleanup()
    }
}
