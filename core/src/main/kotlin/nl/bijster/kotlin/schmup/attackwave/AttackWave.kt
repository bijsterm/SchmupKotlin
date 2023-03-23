package nl.bijster.kotlin.schmup.attackwave

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import nl.bijster.kotlin.schmup.types.GameObject

interface AttackWave {

    /**
     * Do your initializing here
     */
    fun init()

    /**
     * Update all GameObject and return them for collision detection
     */
    fun update(dt: Float)

    fun detectAndHandleCollision(player: GameObject)

    /**
     * Draw all GameObjects
     */
    fun draw(batch: SpriteBatch)

    /**
     * Cleanup all GameObjects
     */
    fun cleanup()
}
