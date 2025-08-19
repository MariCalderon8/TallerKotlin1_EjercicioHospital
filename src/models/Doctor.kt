package models

class Doctor(fullName:String, identificationNumber:String, gender:String, email:String, var professionalLicense:String, var specialty:String, var salary: Double, var startYear: Int, var isActive: Boolean = true)
    : Person(fullName, identificationNumber, gender, email) {

        val assignedPatients: MutableList<Patient> = mutableListOf()

    fun assignPatient(patient: Patient){
        if (!isActive){
            throw IllegalArgumentException("El doctor ${fullName} se encuentra inactivo y no puede recibir pacientes ahora")
        }
        for(existingPatient in assignedPatients){
            if (existingPatient.identificationNumber == patient.identificationNumber){
                throw IllegalArgumentException("No se puede agregar un paciente más de una vez a un mismo doctor")
            }
        }
        assignedPatients.add(patient)
    }

    override fun toString(): String {
        val pacientesNombres = if (assignedPatients.isEmpty()) {
            "No tiene pacientes asignados"
        } else {
            assignedPatients.joinToString(" — ") { "ID: ${it.identificationNumber} Nombre: ${it.fullName}" }
        }
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
            Pacientes: 
            $pacientesNombres
        """.trimIndent()
    }

}