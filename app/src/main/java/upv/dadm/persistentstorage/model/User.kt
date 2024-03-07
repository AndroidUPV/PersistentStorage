package upv.dadm.persistentstorage.model

data class User(
    val id: String,
    val name: String,
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val email: String,
    val phone: String
)
