package models

class Address(var street:String, var number: Int, var neighborhood: String, var city: String, var postalCode:String) {

    override fun toString(): String {
        return "$street $number, $neighborhood, $city (Código Postal: $postalCode)"
    }

}