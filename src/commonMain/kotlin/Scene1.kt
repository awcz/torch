import korlibs.event.Key
import korlibs.image.color.Colors
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.KeepOnReload
import korlibs.korge.box2d.registerBodyWithFixture
import korlibs.korge.input.onClick
import korlibs.korge.ldtk.view.LDTKWorldView
import korlibs.korge.ldtk.view.readLDTKWorld
import korlibs.korge.scene.Scene
import korlibs.korge.view.*
import korlibs.math.geom.Point
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size
import korlibs.time.hz
import org.jbox2d.dynamics.BodyType

class Scene1 : Scene() {

    @KeepOnReload
    var playerPosition = Point(0, 100)

    private lateinit var player: View
    override suspend fun SContainer.sceneMain() {
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world, showCollisions = true)
        mapView.scale = 2.0
        this += mapView
        player = this.findViewByName("Player")!!
        this += player

        fixedSizeContainer(Size(stage!!.width, stage!!.height)) {
            onClick {
                val position = it.currentPosLocal
                for (i in 1..10) {
                    fastRoundRect(Size(30, 30), RectCorners(1, 2), Colors.RED)
                        .position(position.x, position.y)
                        .registerBodyWithFixture(type = BodyType.DYNAMIC)
                }
            }
        }

        mapView.addUpdater {
            if (input.keys[Key.LEFT]) {
                playerPosition = player.pos + Point(-10, 0)
            }
            if (input.keys[Key.RIGHT]) {
                playerPosition = player.pos + Point(10, 0)
            }
            if (input.keys[Key.UP]) {
                playerPosition = player.pos + Point(0, -10)
            }
            if (input.keys[Key.DOWN]) {
                playerPosition = player.pos + Point(0, 10)
            }

            if (input.keys.justPressed(Key.ESCAPE)) views.gameWindow.close(0)
        }


        addFixedUpdater(60.hz) {
            run {
                player.x = playerPosition.x
                player.y = playerPosition.y
            }
        }
    }
}