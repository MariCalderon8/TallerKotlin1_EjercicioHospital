package models

class Hospital(var name:String, val NIT:String, address: Address) {

    val internedPatients = mutableListOf<Patient>()
    val doctors = mutableListOf<Patient>()

}