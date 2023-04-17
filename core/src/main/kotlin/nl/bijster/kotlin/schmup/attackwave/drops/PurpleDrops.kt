package nl.bijster.kotlin.schmup.attackwave.drops

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.FloatArray
import com.badlogic.gdx.utils.TimeUtils
import nl.bijster.kotlin.schmup.attackwave.AttackWave
import nl.bijster.kotlin.schmup.scores.Score
import nl.bijster.kotlin.schmup.types.GameObject

private const val PURPLE_DROP_SPEED = 150

class PurpleDrops : AttackWave {

    // create the raindrops array and spawn the first raindrop
    private lateinit var raindrops: MutableList<Drop>

    private lateinit var dropSound: Sound

    private var lastDropTime: Long = 0L

    override fun init() {
        raindrops = mutableListOf()
        dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"))
    }

    private fun spawnRaindrop(): Drop {
        val drop = Drop().apply {
            x = MathUtils.random(0f, 800f - width)
            y = 600f
            sprite.color = Color.RED
            setScale(0.8f, 2.5f)
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
            raindrop.y -= PURPLE_DROP_SPEED * Gdx.graphics.deltaTime
            raindrop.rotation -= 0.7f

            if (raindrop.y + raindrop.height >= 0 || !raindrop.isVisible) {
                newDropsList.add(raindrop)
            } else {
                Score += 2
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
        dropSound.dispose()

    }

}
