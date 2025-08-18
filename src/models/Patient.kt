package models

class Patient(fullName:String, identificationNumber:String, gender:String, email:String, var phoneNumber:String, var address: Address, var isIntern: Boolean): Person(fullName, identificationNumber, gender, email) {
}