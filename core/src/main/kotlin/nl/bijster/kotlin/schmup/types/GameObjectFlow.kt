package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface GameObjectFlow {

    fun init()

    fun update(deltaTime: Float)

    fun draw(batch: SpriteBatch)

    fun dispose()

}
