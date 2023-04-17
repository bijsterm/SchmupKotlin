package nl.bijster.kotlin.schmup.attackwave.drops

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import nl.bijster.kotlin.schmup.types.EnemyShip
import nl.bijster.kotlin.schmup.types.GameObject

class Drop() : GameObject(dropImage), EnemyShip {

    companion object {
        // load the images for the droplet, 64x64 pixels each
        var dropImage: Texture = Texture(Gdx.files.internal("images/drop.png"))
    }

}
