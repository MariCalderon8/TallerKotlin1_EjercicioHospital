package models

import java.time.LocalDate

class Hospital(var name:String, val NIT:String, address: Address) {

    val internedPatients: MutableList<Patient> = mutableListOf()
    val doctors: MutableList<Doctor> = mutableListOf()

    // Gestión de doctores

    fun findDoctorById(id:String): Doctor? {
        for (doctor in doctors){
            if (doctor.identificationNumber == id){
                return doctor
            }
        }
        return null
    }

    fun findDoctorByProfessionalLicense(license:String): Doctor? {
        for (doctor in doctors){
            if (doctor.professionalLicense == license){
                return doctor
            }
        }
        return null
    }

    fun addNewDoctor(fullName:String, identificationNumber:String, gender:String, email:String, professionalLicense:String, specialty:String, salary: Double, startYear: Int, isActive: Boolean = true){
        if (fullName.isEmpty() || identificationNumber.isEmpty() || gender.isEmpty() || email.isEmpty() || professionalLicense.isEmpty() || specialty.isEmpty() || salary == 0.0){
            throw IllegalArgumentException("No pueden haber campos vacíos")
        }

        if(gender != "M" && gender != "F"){
            throw IllegalArgumentException("El género de la persona debe ser diferente a 'M' o 'F'")
        }

        if(startYear > LocalDate.now().year || startYear < 1900) {
            throw IllegalArgumentException("El año de ingreso no puede ser superior al actual o menor a 1900") // Esto me lo inventé yo xd
        }

        val existingDoctorById:Doctor? = findDoctorById(identificationNumber)
        if (existingDoctorById != null){
            throw IllegalArgumentException("Ya existe un doctor con esta identificación")
        }
        val existingDoctorByLicense = findDoctorByProfessionalLicense(professionalLicense)
        if (existingDoctorByLicense != null){
            throw IllegalArgumentException("Ya existe un doctor identificado con esta licensia profesional")
        }

        val newDoctor = Doctor(fullName, identificationNumber, gender, email, professionalLicense, specialty, salary, startYear, isActive)
        doctors.add(newDoctor)
    }

    /*fun deleteDoctor(id:String){
        val existingDoctor:Doctor? = findDoctorById(id)
        if (existingDoctor == null){
            throw IllegalArgumentException("El doctor no existe")


        }
        doctors.remove(existingDoctor)
    }*
     */
    fun deleteDoctor(id: String) {
        val existingDoctor: Doctor? = findDoctorById(id)

        if (existingDoctor == null) {
            throw IllegalArgumentException("El doctor no existe")
        }
        if (existingDoctor.isActive && doctors.count { it.isActive } == 1) {
            throw IllegalStateException("No se puede eliminar: debe haber al menos un médico activo")
        }
        doctors.remove(existingDoctor)
    }


    fun updateDoctor(identificationNumber:String, fullName:String?, gender:String?, email:String?, professionalLicense:String?, specialty:String?, salary: Double?, startYear: Int?, isActive: Boolean?){
        val existingDoctor:Doctor? = findDoctorById(identificationNumber)

        if (existingDoctor == null){
            throw IllegalArgumentException("El doctor no existe")
        }

        if (isActive == false && existingDoctor.isActive && doctors.count { it.isActive } == 1) {
            throw IllegalStateException("No se puede desactivar: debe haber al menos un médico activo")
        }

        if (fullName != null && fullName.isNotEmpty()) existingDoctor.fullName = fullName
        if (gender != null && gender.isNotEmpty() && (gender.uppercase() == "M" || gender.uppercase() == "F")) existingDoctor.gender = gender
        if (email != null && email.isNotEmpty()) existingDoctor.email = email
        if (professionalLicense != null && professionalLicense.isNotEmpty()) existingDoctor.professionalLicense = professionalLicense
        if (specialty != null && specialty.isNotEmpty()) existingDoctor.specialty = specialty
        if (salary != null && salary > 0.0) existingDoctor.salary = salary
        if (startYear != null && (startYear < LocalDate.now().year && startYear > 1900)) existingDoctor.startYear = startYear
        if (isActive != null) existingDoctor.isActive = isActive
    }

    fun printAllDoctors() {
        if (doctors.isEmpty()) {
            println("No hay doctores registrados")
            return
        }

        println("Lista de doctores en el hospital $name:")
        for (doctor in doctors) {
            println()
            println(doctor.toString())
            println()
        }
    }

    fun calculateTotalSalaries(): Double {
        var total=0.0
        for (doctor in doctors){
            total += doctor.salary
        }
        return total
    }

    fun calculateSalariesBySpecialty(): List<String> {
        val specialties = mutableSetOf<String>()
        for (doctor in doctors) {
            specialties.add(doctor.specialty)
        }

        //Calcular el total por especialidad
        val results = mutableListOf<String>()
        for (specialty in specialties) {
            var total = 0.0
            for (doctor in doctors) {
                if (doctor.specialty == specialty) {
                    total += doctor.salary
                }
            }
            results.add("$specialty: $${"%,.2f".format(total)}")
        }
        return results
    }

    fun getDoctorsCountBySpecialty(): List<String> {
        val result = mutableListOf<String>()

        val specialties = mutableListOf<String>()
        for (doctor in doctors) {
            if (!specialties.contains(doctor.specialty)) {
                specialties.add(doctor.specialty)
            }
        }

        //médico por cada especialidad
        for (specialty in specialties) {
            var count = 0
            for (doctor in doctors) {
                if (doctor.specialty == specialty) {
                    count++
                }
            }
            result.add("$specialty: $count médico(s)")
        }

        return result
    }

    fun getMostSeniorDoctor(): String {
        if (doctors.isEmpty()) {
            return "No hay médicos registrados"
        }

        var oldestDoctor = doctors[0]
        for (doctor in doctors) {
            if (doctor.startYear < oldestDoctor.startYear) {
                oldestDoctor = doctor
            }
        }

        return "Médico más antiguo: ${oldestDoctor.fullName} " +
                "(Año de ingreso: ${oldestDoctor.startYear}), " +
                "Especialidad: ${oldestDoctor.specialty}"
    }


    // Gestión de pacientes

    fun addPatient(patient: Patient) {
        if (internedPatients.any { it.identificationNumber == patient.identificationNumber }) {
            throw IllegalArgumentException("Ya existe un paciente con esta identificación")
        }
        internedPatients.add(patient)
    }

    fun findPatientById(id: String): Patient? {
        return internedPatients.find { it.identificationNumber == id }
    }

    fun updatePatient(
        id: String,
        newPhone: String? = null,
        newAddress: Address? = null,
        newIsIntern: Boolean? = null
    ) {
        val patient = findPatientById(id) ?: throw IllegalArgumentException("Paciente no encontrado")
        newPhone?.let { patient.phoneNumber = it }
        newAddress?.let { patient.address = it }
        newIsIntern?.let { patient.isIntern = it }
    }

    fun deletePatient(id: String) {
        if (!internedPatients.removeIf { it.identificationNumber == id }) {
            throw IllegalArgumentException("Paciente no encontrado")
        }
    }

    fun printAllPatients() {
        if (internedPatients.isEmpty()) {
            println("No hay pacientes registrados")
            return
        }
        println("\nLISTA DE PACIENTES:")
        internedPatients.forEach { println(it) }
    }

    // Porcentaje por género
    fun getPatientGenderPercentage(): Map<String, Double> {
        if (internedPatients.isEmpty()) return emptyMap()

        val (male, female) = internedPatients.partition { it.gender.uppercase() == "M" }
        val total = internedPatients.size.toDouble()

        return mapOf(
            "Masculino" to (male.size * 100 / total),
            "Femenino" to (female.size * 100 / total)
        )
    }

    // Ultimo medico activo
    fun hasAtLeastOneActiveDoctor(): Boolean {
        return doctors.any { it.isActive }
    }

    //TODO
}

