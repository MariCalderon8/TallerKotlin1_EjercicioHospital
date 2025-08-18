package models

class Doctor(fullName:String, identificationNumber:String, gender:String, email:String, var professionalLicense:String, var specialty:String, var isActive: Boolean, var salary: Double, var startYear: Int)
    : Person(fullName, identificationNumber, gender, email) {

        val assignedPatients = mutableListOf<Patient>()

}