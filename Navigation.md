Voyager: A multiplatform navigation library built specifically for Compose projects that works for Android, desktop, and potentially other platforms.
Here’s how you can use Voyager:

Add Voyager dependencies in your build.gradle.kts:
kotlin
Copy code
dependencies {
implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc04") // or latest version
implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.0-rc04")
}
Use Voyager's Navigator to manage navigation in Compose Multiplatform:
kotlin
Copy code
import cafe.adriel.voyager.navigator.*
import cafe.adriel.voyager.core.screen.Screen

@Composable
fun MyApp() {
Navigator(HomeScreen())
}

object HomeScreen : Screen {
@Composable
override fun Content() {
Column {
Text("Home Screen")
Button(onClick = { Navigator.current.push(DetailScreen) }) {
Text("Go to Detail Screen")
}
}
}
}

object DetailScreen : Screen {
@Composable
override fun Content() {
Column {
Text("Detail Screen")
Button(onClick = { Navigator.current.pop() }) {
Text("Go back")
}
}
}
}
Voyager provides a navigation structure similar to NavHost and NavController but designed for multiplatform usage.

3. Compose for Web Navigation
   For Compose targeting web, you might also need routing support (for example, URL-based navigation). Some libraries like Compose for Web Router handle basic web navigation:

kotlin
Copy code
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.routing.*

@Composable
fun MyApp() {
BrowserRouter {
Route("/", HomeScreen)
Route("/details", DetailScreen)
}
}

@Composable
fun HomeScreen() {
Div {
Text("Home")
A(href = "/details") {
Text("Go to Details")
}
}
}

@Composable
fun DetailScreen() {
Div {
Text("Details")
A(href = "/") {
Text("Back to Home")
}
}
}
This setup is useful for web-based navigation, where the URL and routing system are more appropriate.

Conclusion
navigation.compose is not fully supported for Compose Multiplatform (desktop, web) outside Android.
For desktop or web, you can either implement your own navigation using state-based composables or use third-party libraries like Voyager.
For Compose Web, a browser-based router system is often useful to manage URL-based navigation.
These alternatives provide flexibility to manage navigation on desktop, web, and Android in a Kotlin Multiplatform project.