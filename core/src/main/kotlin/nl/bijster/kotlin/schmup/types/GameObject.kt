package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class GameObject {

    abstract fun update(deltaTime: Float)

    abstract fun draw(batch: SpriteBatch)

    abstract fun dispose()
}
