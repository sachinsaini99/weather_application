data class Model(
    val base: String,
    val clouds: CloudsXX,
    val cod: Int,
    val coord: CoordXX,
    val dt: Int,
    val id: Int,
    val main: MainXXXX,
    val name: String,
    val sys: SysXX,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherXX>,
    val wind: WindXX
)