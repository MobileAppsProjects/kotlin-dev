data class Restaurant(
    val name: String,
    val address: String,
    val deliveryCharge: Int,
    val hours: Map<String, String>
)