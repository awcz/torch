import korlibs.image.color.Colors
import korlibs.korge.Korge
import korlibs.korge.scene.sceneContainer
import korlibs.math.geom.Size

suspend fun main() = Korge(
    title = "Torch 2D",
    windowSize = Size(800, 400),
    backgroundColor = Colors["#1f255d"],
) {
    println("Hello KorGE")
    sceneContainer().changeTo {
        Scene1()
    }
}