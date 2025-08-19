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

    fun patientsStringList(): String {
        var patientsList = ""
        for(patient in assignedPatients){
            patientsList = patientsList + """   ID:${patient.identificationNumber} Nombre: ${patient.fullName}\n"""
        }
        if (patientsList == "") {
            return """  No tiene pacientes asignados"""
        }
        return patientsList
    }

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
            Pacientes: 
            ${patientsStringList().prependIndent("      ")}
        """.trimIndent()
    }

}