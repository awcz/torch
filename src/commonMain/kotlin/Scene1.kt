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
import korlibs.time.TimeSpan
import korlibs.time.hz
import org.jbox2d.dynamics.BodyType

class Scene1 : Scene() {

    @KeepOnReload
    var playerPosition = Point(0, 10)

    override suspend fun SContainer.sceneMain() {
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world, showCollisions = true)
        mapView.scale = 2.0
        this += mapView

        fixedSizeContainer(Size(stage!!.width, stage!!.height)) {
//            solidRect(520, 30, Colors.DARKGREEN).position(0, 260).registerBodyWithFixture(
//                type = BodyType.STATIC,
//                friction = 1
//            )
            onClick {
                val position = it.currentPosLocal
                for (i in 1..10) {
                    fastRoundRect(Size(30, 30), RectCorners(1, 2), Colors.RED)
                        .position(position.x, position.y)
                        .registerBodyWithFixture(type = BodyType.DYNAMIC)
                }
            }
//
//            val db = KorgeDbFactory()
//            db.loadKorgeMascots()

            mapView.addUpdater { duration: TimeSpan ->
                if (input.keys[Key.LEFT]) {
                    val player = this.findViewByName("Player")
                    player!!.x -= 1;
                }
                if (input.keys.pressing(Key.RIGHT)) {
                    val player = this.findViewByName("Player")
                    player!!.x += 1;
                }
                if (input.keys.justPressed(Key.ESCAPE)) views.gameWindow.close(0)
            }
        }

        addFixedUpdater(60.hz) {
            run {
                val player = this.findViewByName("Player")!!
                playerPosition = player.pos + Point(2.0, 0)
                player.x = playerPosition.x
                player.y = playerPosition.y
            }
        }

    }
}
