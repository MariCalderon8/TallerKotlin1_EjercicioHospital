package models

class Doctor(fullName:String, identificationNumber:String, gender:String, email:String, var professionalLicense:String, var specialty:String, var salary: Double, var startYear: Int, var isActive: Boolean = true)
    : Person(fullName, identificationNumber, gender, email) {

        val assignedPatients: MutableList<Patient> = mutableListOf()

    override fun toString(): String {
        return """
            Doctor: $fullName
            Identificación: $identificationNumber
            Género: $gender
            Email: $email
            Licencia profesional: $professionalLicense
            Especialidad: $specialty
            Salario: $salary
            Año de ingreso: $startYear
            Estado: ${if (isActive) "Activo" else "Inactivo"}
        """.trimIndent()
    }

}