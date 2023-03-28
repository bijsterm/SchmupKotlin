package nl.bijster.kotlin.schmup.types

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface GameObjectFlow {

    fun init()

    fun update(deltaTime: Float)

    fun draw(batch: SpriteBatch, rectBatch: ShapeRenderer)


    fun dispose()

}
