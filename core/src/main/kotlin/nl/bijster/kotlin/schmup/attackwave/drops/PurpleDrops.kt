package nl.bijster.kotlin.schmup.attackwave.drops

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.TimeUtils
import nl.bijster.kotlin.schmup.attackwave.AttackWave
import nl.bijster.kotlin.schmup.scores.Score
import nl.bijster.kotlin.schmup.types.GameObject

class PurpleDrops : AttackWave {

    // load the images for the droplet, 64x64 pixels each
    private lateinit var dropImage: Texture

    // create the raindrops array and spawn the first raindrop
    private lateinit var raindrops: MutableList<Drop>

    private lateinit var dropSound: Sound

    private var lastDropTime: Long = 0L

    override fun init() {
        dropImage = Texture(Gdx.files.internal("images/drop.png"))
        raindrops = mutableListOf()
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"))
    }

    private fun spawnRaindrop(): Drop {
        val dropSprite = Sprite(dropImage).apply {
            x = MathUtils.random(0f, 800f - 64f)
            y = 600f
            color = Color.RED
        }
        val newDrop = Drop().apply {
            sprite = dropSprite
        }
//        log.debug { "DropX: ${newDrop.sprite.x} DropY: ${newDrop.sprite.y}" }
        lastDropTime = TimeUtils.nanoTime()
        return newDrop
    }

    override fun update(dt: Float) {
        val newDropsList: MutableList<Drop> = mutableListOf()

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            raindrops.add(spawnRaindrop())

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also

        for (raindrop in raindrops) {
            raindrop.sprite.y -= 200 * Gdx.graphics.deltaTime
            if (raindrop.sprite.y + 64 >= 0 || !raindrop.isVisible) {
                newDropsList.add(raindrop)
            } else {
                Score += 1
            }

        }
//        log.debug { "Nr of drops: ${newDropsList.size}" }
        raindrops = newDropsList
    }

    override fun detectAndHandleCollision(player: GameObject) {
        val raindropsIterator = raindrops.iterator()
        while (raindropsIterator.hasNext()) {
            val drop = raindropsIterator.next()
            if (drop.sprite.boundingRectangle.overlaps(player.sprite.boundingRectangle)) {
                player.hasCollided = true
                dropSound.play()

                raindropsIterator.remove()  // Remove current element
            }
        }
    }

    override fun draw(batch: SpriteBatch) {

        // 2. Attachwaves
        raindrops.filter { raindrop ->
            raindrop.isVisible
        }.forEach { raindrop ->
            raindrop.sprite.draw(batch)
        }
    }

    override fun cleanup() {
        dropImage.dispose()
        dropSound.dispose()


    }

}
