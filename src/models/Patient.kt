package models

class Patient(fullName:String, identificationNumber:String, gender:String, email:String, var phoneNumber:String, var address: Address, var isIntern: Boolean): Person(fullName, identificationNumber, gender, email) {

    override fun toString(): String {
        return """
        Paciente: $fullName
        ID: $identificationNumber
        Género: $gender
        Email: $email
        Teléfono: $phoneNumber
        Dirección: ${address.toString()}
        Internado: ${if (isIntern) "Sí" else "No"}
    """.trimIndent()
    }
}