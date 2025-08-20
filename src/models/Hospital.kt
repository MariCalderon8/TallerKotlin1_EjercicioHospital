package models

import java.time.LocalDate

class Hospital(var name:String, val NIT:String, address: Address) {

    val internedPatients: MutableList<Patient> = mutableListOf()
    val doctors: MutableList<Doctor> = mutableListOf()
    val generalPatients: MutableList<Patient> = mutableListOf()

    //Dcotors management

    fun findPersonById(id: String): Person? {
        for (doctor in doctors){
            if (doctor.identificationNumber == id){
                return doctor
            }
        }

        for (patient in generalPatients) {
            if (patient.identificationNumber == id) {
                return patient
            }
        }

        return null
    }

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

        if(gender.uppercase() != "M" && gender.uppercase() != "F"){
            throw IllegalArgumentException("El género de la persona debe ser diferente a 'M' o 'F'")
        }

        if(startYear > LocalDate.now().year || startYear < 1900) {
            throw IllegalArgumentException("El año de ingreso no puede ser superior al actual o menor a 1900")
        }

        val existingPerson:Person? = findPersonById(identificationNumber)
        if (existingPerson != null){
            throw IllegalArgumentException("Esta identificación ya se encuentra registrada")
        }
        val existingDoctorByLicense = findDoctorByProfessionalLicense(professionalLicense)
        if (existingDoctorByLicense != null){
            throw IllegalArgumentException("Ya existe un doctor identificado con esta licensia profesional")
        }

        val newDoctor = Doctor(fullName, identificationNumber, gender.uppercase(), email, professionalLicense, specialty, salary, startYear, isActive)
        doctors.add(newDoctor)
    }

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

    // Last active doctor
    fun hasAtLeastOneActiveDoctor(): Boolean {
        return doctors.any { it.isActive }
    }

    fun assignPatientToDoctor(doctorID: String, patientID: String){
        if (doctorID == ""){
            throw IllegalArgumentException("El ID del médico no puede estár vacío")
        }
        if (patientID == ""){
            throw IllegalArgumentException("El ID del paciente no puede estár vacío")
        }
        val doctor = findDoctorById(doctorID)
        if (doctor == null){
            throw IllegalArgumentException("Lo sentimos, no pudimos encontrar al médico que buscas. Revisa que el ID esté bien escrito")
        }
        val patient = findPatientById(patientID)
        if (patient == null){
            throw IllegalArgumentException("Lo sentimos, no pudimos encontrar al paciente que buscas. Revisa que el ID esté bien escrito")
        }
        if (!doctor.isActive){
            throw IllegalArgumentException("El doctor ${doctor.fullName} no se encuentra activo y no puede recibir pacientes ahora")
        }
        doctor.assignPatient(patient)
    }


    // Patients Management

    fun findPatientById(id: String): Patient? {
        for (patient in generalPatients) {
            if (patient.identificationNumber == id) {
                return patient
            }
        }
        return null
    }

    fun addPatient(  fullName: String, identificationNumber: String, gender: String, email: String, phoneNumber: String, address: Address, isIntern: Boolean? = false) {
        if (fullName.isEmpty() || identificationNumber.isEmpty() || gender.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
            throw IllegalArgumentException("No pueden haber campos vacíos")
        }

        if (address.street.isEmpty() || address.city.isEmpty() || address.postalCode.isEmpty() || address.neighborhood.isEmpty() || address.number < 1){
            throw IllegalArgumentException("Hay campos vacíos o no válidos en la dirección. Vuelva a intentar")
        }

        if (gender.uppercase() != "M" && gender.uppercase() != "F") {
            throw IllegalArgumentException("El género del paciente debe ser 'M' o 'F'")
        }

        val existingPerson: Person? = findPersonById(identificationNumber)
        if (existingPerson != null) {
            throw IllegalArgumentException("Esta identificación ya se encuentra registrada")
        }

        val internStatus = isIntern ?: false
        val newPatient = Patient(fullName, identificationNumber, gender.uppercase(), email, phoneNumber, address, internStatus)

        generalPatients.add(newPatient)

        if(internStatus){
            internedPatients.add(newPatient)
        }

    }

    fun deletePatient(identificationNumber: String){
        val existingPatient: Patient? = findPatientById(identificationNumber)

        if(existingPatient == null){
            throw IllegalArgumentException("El paciente no existe")
        }

        generalPatients.remove(existingPatient)
        internedPatients.remove(existingPatient)
    }

    fun updatePatient(identificationNumber: String, fullName: String?, gender: String?, email: String?, phoneNumber: String?, address: Address?, isIntern: Boolean?) {
        val existingPatient: Patient? = findPatientById(identificationNumber)

        if (existingPatient == null) {
            throw IllegalArgumentException("El paciente no existe")
        }

        if (fullName != null && fullName.isNotEmpty()) existingPatient.fullName = fullName
        if (gender != null && gender.isNotEmpty() && (gender.uppercase() == "M" || gender.uppercase() == "F")) existingPatient.gender = gender
        if (email != null && email.isNotEmpty()) existingPatient.email = email
        if (phoneNumber != null && phoneNumber.isNotEmpty()) existingPatient.phoneNumber = phoneNumber
        if (address != null) existingPatient.address = address
        if (isIntern != null){
            val wasIntern = existingPatient.isIntern
            existingPatient.isIntern = isIntern

            if(wasIntern && !isIntern){
                internedPatients.remove(existingPatient)
            }else if(!wasIntern && isIntern){
                internedPatients.add(existingPatient)
            }
        }

    }

    fun printAllPatients() {
        if (generalPatients.isEmpty()) {
            println("No hay pacientes registrados")
            return
        }

        println("Lista de pacientes en el hospital $name:")
        for (patient in generalPatients) {
            println()
            println(patient.toString())
            println()
        }

        println("\nPacientes internados:")
        for (patient in internedPatients) {
            println()
            println(patient.toString())
            println()
        }
    }

    //Special functions

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

    fun getPatientGenderPercentage(): Map<String, Double> {
        if (generalPatients.isEmpty()){
            return emptyMap()
        }

        val total = generalPatients.size.toDouble()
        val males = generalPatients.count { it.gender.uppercase() == "M" }
        val females = generalPatients.count { it.gender.uppercase() == "F" }

        return mapOf(
            "Masculino" to (males * 100 / total),
            "Femenino" to (females * 100 / total)
        )
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

}

