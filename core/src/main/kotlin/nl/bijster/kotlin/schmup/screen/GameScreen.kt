package nl.bijster.kotlin.schmup.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.TimeUtils
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.logger
import nl.bijster.kotlin.schmup.Shmup
import nl.bijster.kotlin.schmup.player.Player
import nl.bijster.kotlin.schmup.types.HiScoreTable

private val log = logger<GameScreen>()

class GameScreen(private val shmup: Shmup) : KtxScreen {

    private val player: Player = Player(3)


    // load the drop sound effect and the rain background music
    private val dropSound: Sound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"))
    private val rainMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("music/rain.mp3")).apply {
        isLooping = true
    }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera: OrthographicCamera = OrthographicCamera().apply {
        setToOrtho(false, 800f, 600f)
    }


    // load the images for the droplet, 64x64 pixels each
    private val dropImage: Texture = Texture(Gdx.files.internal("images/drop.png"))

    // create the raindrops array and spawn the first raindrop
    private var raindrops: Array<Rectangle> = Array<Rectangle>()

    private var lastDropTime: Long = 0L
    private var score: Int = 0

    private fun spawnRaindrop() {
        raindrops.add(Rectangle(MathUtils.random(0f, 800f - 64f), 600f, 64f, 64f))
        lastDropTime = TimeUtils.nanoTime()
    }

    override fun render(delta: Float) {
        // Alle updates

        // 1. Player first!!
        player.update(delta)

        // 2. Player Bullets

        // 3. Attack-waves


        // begin a new batch and draw all
        shmup.batch.use {
            // 0. Text
            shmup.font.draw(it, "Score: $score", 0f, 600f)
            shmup.font.draw(it, "Nr. Lives: ${player.nrOfLives}", 500f, 600f)
            // 1. Draw player first
            player.draw(it)

            // 2. Attachwaves
            raindrops.forEach { raindrop ->
                shmup.batch.draw(dropImage, raindrop.x, raindrop.y)
            }

            // 3. Bullets

            // 4. Player Hit-box, must be upper-level!

        }


        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            spawnRaindrop()

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also
        val iter = raindrops.iterator()
        while (iter.hasNext()) {
            val raindrop = iter.next()
            raindrop.y -= 200 * Gdx.graphics.deltaTime
            if (raindrop.y + 64 < 0) {
                iter.remove()
                score++
                log.debug { "Score is: $score" }
            }
            if (raindrop.overlaps(player.playerSprite.boundingRectangle)) {
                dropSound.play()
                iter.remove()

                val gameOver = player.die()
                if (gameOver) {
                    switchScreens()
                    HiScoreTable.add(score, 1.0)
                }
            }
        }
    }

    private fun switchScreens() {
        shmup.addScreen(MainMenuScreen(shmup))
        shmup.setScreen<MainMenuScreen>()
        shmup.removeScreen<GameScreen>()
        dispose()
    }

    override fun show() {

        // generally good practice to update the camera's matrices once per frame
        camera.update()
        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        shmup.batch.projectionMatrix = camera.combined


        shmup.font.data.setScale(3f)

        // start the playback of the background music when the screen is shown
        // TODO: rainMusic.play()
    }

    override fun dispose() {
        dropImage.dispose()
        dropSound.dispose()
        rainMusic.dispose()

        player.dispose()
    }
}
