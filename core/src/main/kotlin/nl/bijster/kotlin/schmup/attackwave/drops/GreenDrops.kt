package nl.bijster.kotlin.schmup.attackwave.drops

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.FloatArray
import com.badlogic.gdx.utils.TimeUtils
import ktx.log.logger
import nl.bijster.kotlin.schmup.attackwave.AttackWave
import nl.bijster.kotlin.schmup.scores.Score
import nl.bijster.kotlin.schmup.types.GameObject

private val log = logger<GreenDrops>()

private const val GREEN_DROP_SPEED = 200

class GreenDrops : AttackWave {

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
        val drop = Drop(dropImage).apply {
            x = MathUtils.random(0f, 800f - width)
            y = 600f
            sprite.color = Color.YELLOW
        }
//        log.debug { "DropX: ${newDrop.sprite.x} DropY: ${newDrop.sprite.y}" }
        lastDropTime = TimeUtils.nanoTime()
        return drop
    }

    override fun update(dt: Float, playerX: Float, playerY: Float) {
        val newDropsList: MutableList<Drop> = mutableListOf()

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000L)
            raindrops.add(spawnRaindrop())

        // move the raindrops, remove any that are beneath the bottom edge of the
        //    screen or that hit the bucket.  In the latter case, play back a sound
        //    effect also

        for (raindrop in raindrops) {
            raindrop.y -= GREEN_DROP_SPEED * Gdx.graphics.deltaTime
            raindrop.rotation += 1

            if (raindrop.y + raindrop.height >= 0 || !raindrop.isVisible) {
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
            if (Intersector.intersectPolygons(
                    FloatArray(drop.hitbox.transformedVertices),
                    FloatArray(player.hitbox.transformedVertices)
                )
            ) {
                player.collidedWith.add(drop)
                dropSound.play()

                raindropsIterator.remove()  // Remove current element
            }
        }
    }

    override fun drawAttackWave(batch: SpriteBatch, rectBatch: ShapeRenderer) {

        // 2. Attachwaves
        raindrops.filter { raindrop ->
            raindrop.isVisible
        }.forEach { raindrop ->
            raindrop.draw(batch)
            raindrop.draw(rectBatch)
        }
    }


    override fun cleanup() {
        dropImage.dispose()
        dropSound.dispose()
    }

}
