import korlibs.event.Key
import korlibs.image.color.Colors
import korlibs.io.file.std.resourcesVfs
import korlibs.korge.box2d.registerBodyWithFixture
import korlibs.korge.input.keys
import korlibs.korge.ldtk.view.LDTKCollisions
import korlibs.korge.ldtk.view.LDTKWorldView
import korlibs.korge.ldtk.view.createCollisionMaps
import korlibs.korge.ldtk.view.readLDTKWorld
import korlibs.korge.scene.Scene
import korlibs.korge.ui.uiText
import korlibs.korge.view.*
import korlibs.math.geom.Point
import korlibs.math.geom.RectCorners
import korlibs.math.geom.Size
import korlibs.time.hz
import korlibs.time.seconds
import org.jbox2d.dynamics.BodyType
import kotlin.random.Random

private const val COLLISION_MARKER = 1
private const val GRAVITY = 10
private const val JUMP_STEP_Y = 5
private const val MOVE_STEP_X = 1.5
private const val REFRESHING_HZ = 50

class Scene1 : Scene() {

    private lateinit var collisions: LDTKCollisions
    private lateinit var player: View
    private var points: Long = 0
    override suspend fun SContainer.sceneMain() {
        val world = resourcesVfs["ldtk/torch_map.ldtk"].readLDTKWorld()
        val mapView = LDTKWorldView(world)
        // player = mapView.findViewByName("Player") ?: throw IllegalStateException()
        player = fastRoundRect(Size(20, 20), RectCorners(1, 2), Colors.RED)
            .position(0, 0)
            .registerBodyWithFixture(type = BodyType.KINEMATIC)

        collisions = world.createCollisionMaps()
        this += mapView
        this += player

        val refreshing = REFRESHING_HZ.hz
        var moveInput = 0.0
        var displayDebugLabels = false
        var currentMove = Point(0, 0)
        var health = 100

        val healthLabel = mapView.uiText("")
        val playerPositionLabel = mapView.uiText("")
        playerPositionLabel.x = 200.0
        val good = mutableListOf<FastRoundRect>()
        val bad = mutableListOf<FastRoundRect>()

        addFixedUpdater(refreshing) {
            moveIfPossible(Point(MOVE_STEP_X, 0) * moveInput)
            currentMove += Point(0, GRAVITY) * refreshing.timeSpan.seconds
            if (!moveIfPossible(currentMove)) {
                currentMove = Point.ZERO
            }

            if (Random.nextFloat() < 0.015) {
                val item = fastRoundRect(Size(5, 5), RectCorners(1, 2), Colors.RED)
                    .position(player.pos.x + 100 * Random.nextFloat(), 0)
                    .registerBodyWithFixture(type = BodyType.DYNAMIC)
                bad.add(item)
            }

            if (Random.nextFloat() < 0.015) {
                val item = fastRoundRect(Size(5, 5), RectCorners(1, 2), Colors.GREEN)
                    .position(player.pos.x + 100 * Random.nextFloat(), 0)
                    .registerBodyWithFixture(type = BodyType.DYNAMIC)

                good.add(item)
            }

            healthLabel.text = " health: $health | points: $points"
            playerPositionLabel.x = sceneContainer.width - 100
            playerPositionLabel.text = "[x:${player.pos.x.toInt()}, y:${player.pos.y.toInt()}] "
            healthLabel.visible = displayDebugLabels
            playerPositionLabel.visible = displayDebugLabels
            checkCollisions(good) { points = points.plus(1) }
            checkCollisions(bad) { health = health.minus(15) }
        }

        mapView.keys {
            val moveSpeed = 1.2
            justDown(Key.LEFT) { moveInput = -MOVE_STEP_X * moveSpeed }
            justDown(Key.RIGHT) { moveInput = MOVE_STEP_X * moveSpeed }
            justDown(Key.SPACE, Key.UP) {
                if (currentMove.y == 0.0)
                    currentMove += Point(0, -JUMP_STEP_Y)
            }
            up(Key.LEFT, Key.RIGHT) { moveInput = 0.0 }
            justDown(Key.D) { displayDebugLabels = !displayDebugLabels }
        }
    }

    private fun checkCollisions(items: MutableList<FastRoundRect>, fn: () -> Unit) {
       val toRemove = mutableListOf<FastRoundRect>()
        items.forEach {
            if (it.pos.distanceTo(player.pos) < 15) {
                it.removeFromParent()
                extracted()
                toRemove.add(it)
                fn.invoke()
            }
        }
        items.removeAll(toRemove)
        // TODO remove items from outside the map
    }

    private fun extracted() {
        this.points = points.plus(1)
    }

    private fun moveIfPossible(move: Point): Boolean {
        val destination = player.pos + move
        val isValidMove = collisions.getPixel(destination) != COLLISION_MARKER
        if (isValidMove) {
            player.pos = destination
        }
        return isValidMove
    }
}