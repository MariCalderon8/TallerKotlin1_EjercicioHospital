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

    fun deleteDoctor(id:String){
        val existingDoctor:Doctor? = findDoctorById(id)
        if (existingDoctor == null){
            throw IllegalArgumentException("El doctor no existe")
        }
        doctors.remove(existingDoctor)
    }

    fun updateDoctor(identificationNumber:String, fullName:String?, gender:String?, email:String?, professionalLicense:String?, specialty:String?, salary: Double?, startYear: Int?, isActive: Boolean?){
        val existingDoctor:Doctor? = findDoctorById(identificationNumber)

        if (existingDoctor == null){
            throw IllegalArgumentException("El doctor no existe")
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

    //TODO
}

