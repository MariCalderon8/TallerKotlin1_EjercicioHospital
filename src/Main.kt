import models.Address
import models.Hospital
import models.Patient

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val hospitalAddress: Address = Address("Carrera", 12, "San José", "Armenia", "345678")
    val hospital: Hospital = Hospital("Sana que sana Colita de Rana", "2345678", hospitalAddress)

    println("""
        !BIENVIENIDO AL SISTEMA CLÍNICO DE ${hospital.name.uppercase()}!
        ¿Cómo podemos ayudarte hoy? :D
        
    """.trimIndent())
    printMainMenu()
    var userResponse : String?
    while(true){
        userResponse = readLine()
        when(userResponse) {
            "1" -> {
                doctorsManagement(hospital)
            }
            "2" -> {
                patientsManagement(hospital)
            }
            "3" -> println("Bye bye :D")
            else -> println("Lo sentimos, no tenemos esta opcion en nuestro menu.")
        }
        printMainMenu()
    }
}

fun doctorsManagement(hospital: Hospital){
    printDoctorsMenu()
    var otherResponse: String?
    while (true){
        otherResponse = readLine()
        when(otherResponse){
            "1" ->{
                println("Ingrese el numero de identificación")
                var identificationNumber = readLine()!!
                var doctor = hospital.findDoctorById(identificationNumber)
                if (doctor != null) {
                    println(doctor.toString())
                } else {
                    println("Lo siento, no pudimos encontrar al medico que buscas. Revisa que la identificación esté bien escrita")
                }
            }
            "2" ->{
                try {
                    println("Ingrese el nombre completo")
                    val fullName = readLine()!!
                    println("Ingrese el numero de identificación")
                    val identificationNumber = readLine()!!
                    println("Ingrese género (M / F)")
                    val gender = readLine()!!
                    println("Ingrese la dirección de email")
                    val email = readLine()!!
                    println("Ingrese el numero de la licencia profesional")
                    val professionalLicense = readLine()!!
                    println("Ingrese la especialidad")
                    val specialty = readLine()!!
                    println("Ingrese el salario")
                    val salaryString = readLine()
                    val salary: Double = if (salaryString == null || salaryString.isEmpty()) 0.0 else salaryString.toDouble()
                    println("Ingrese el año de ingreso")
                    val startYearString = readLine()
                    val startYear: Int = if (startYearString == null || startYearString.isEmpty()) 0 else startYearString.toInt()
                    println("¿Está activo? (S / N)")
                    val isActiveString = readLine()!!
                    val isActive = if (isActiveString.isNotEmpty() && isActiveString.uppercase() == "S") true else false

                    hospital.addNewDoctor(fullName,identificationNumber, gender, email, professionalLicense, specialty, salary, startYear, isActive)
                    println("MÉDICO CREADO CORRECTAMENTE")
                } catch (e: IllegalArgumentException) {
                    println(e.message)
                } catch (e: NumberFormatException) {
                    println("El año y el salario deben ser un dato numérico")
                } catch (e: Exception){
                    println("ERROR CREANDO AL MÉDICO")
                }
            }
            "3" ->{
                try {
                    println("Ingrese el numero de identificación del doctor que desea editar")
                    val identificationNumber = readLine()!!
                    val doctor = hospital.findDoctorById(identificationNumber)
                    if (doctor != null) {
                        println("Ingrese el nuevo nombre completo")
                        val fullName = readLine()
                        println("Ingrese género (M / F)")
                        val gender = readLine()
                        println("Ingrese la dirección de email")
                        val email = readLine()
                        println("Ingrese el numero de la licencia profesional")
                        val professionalLicense = readLine()
                        println("Ingrese la especialidad")
                        val specialty = readLine()
                        println("Ingrese el salario")
                        val salaryString = readLine()
                        val salary: Double? = if (salaryString != null && salaryString.isNotEmpty()) salaryString.toDouble() else null
                        println("Ingrese el año de ingreso")
                        val startYearString = readLine()
                        val startYear: Int? = if (startYearString != null && startYearString.isNotEmpty()) startYearString.toInt() else null
                        println("¿Está activo? (S / N)")
                        val isActiveString = readLine()
                        var isActive: Boolean? = null
                        if (isActiveString != null && isActiveString.uppercase() == "S"){
                            isActive = true
                        } else if (isActiveString != null && isActiveString.uppercase() == "N"){
                            isActive = false
                        }

                        hospital.updateDoctor(identificationNumber, fullName, gender, email, professionalLicense, specialty, salary, startYear, isActive)
                        println("MÉDICO ACTUALIZADO CORRECTAMENTE")
                    } else {
                        println("Lo siento, no pudimos encontrar al medico que buscas. Revisa que la identificación esté bien escrita")
                    }

                } catch (e: IllegalArgumentException) {
                    println(e.message)
                } catch (e: NumberFormatException) {
                    println("El año y el salario deben ser un dato numérico")
                } catch (e: Exception){
                    println("ERROR EDITANDO AL MÉDICO")
                }
            }
            "4" ->{
                try {
                    println("Ingrese el numero de identificación del doctor que desea eliminar")
                    val identificationNumber = readLine()
                    if (identificationNumber != null && identificationNumber.isNotEmpty()){
                        hospital.deleteDoctor(identificationNumber)
                        println("MÉDICO ELIMINADO CORRECTAMENTE")
                    }
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            "5" ->{
                val total= hospital.calculateTotalSalaries()
                println("TOTAL DE SALARIOS: $${"%.2f".format(total)}")
            }
            "6" -> {
                val salariesBySpecialty = hospital.calculateSalariesBySpecialty()
                println("TOTAL POR ESPECIALIDAD:")
                salariesBySpecialty.forEach { println(it) }
            }
            "7" -> {
                val counts = hospital.getDoctorsCountBySpecialty()
                if (counts.isEmpty()) {
                    println("No hay médicos registrados")
                } else {
                    println("\nCANTIDAD DE MÉDICOS POR ESPECIALIDAD:")
                    for (item in counts) {
                        println(item)
                    }
                }
            }
            "8" ->{
                println("\n${hospital.getMostSeniorDoctor()}")
            }
            "9" ->{
                hospital.printAllDoctors()
            }
            "10" -> {
                return
            }
            else ->{

            }

        }
        printDoctorsMenu()
    }

}

fun patientsManagement(hospital: Hospital) {
    printPatientMenu()
    while (true) {
        when (readLine()) {
            "1" -> {
                println("Ingrese ID del paciente:")
                val id = readLine()!!
                hospital.findPatientById(id)?.let { println(it) } ?: println("Paciente no encontrado")
            }

            "2" -> {
                try {
                    println("Nombre completo:")
                    val name = readLine()!!

                    println("ID:")
                    val id = readLine()!!

                    println("Género (M/F):")
                    val gender = readLine()!!.uppercase()

                    println("Email:")
                    val email = readLine()!!

                    println("Teléfono:")
                    val phone = readLine()!!

                    println("Calle:")
                    val street = readLine()!!

                    println("Número:")
                    val number = readLine()!!.toInt()

                    println("Barrio:")
                    val neighborhood = readLine()!!

                    println("Ciudad:")
                    val city = readLine()!!

                    println("Código postal:")
                    val postalCode = readLine()!!
                    val address = Address(street, number, neighborhood, city, postalCode)

                    println("¿Está internado? (S/N):")
                    val isInternInput = readLine()!!.uppercase()
                    val isIntern = when (isInternInput) {
                        "S" -> true
                        "N" -> false
                        else -> false
                    }

                    hospital.addPatient(name, id, gender, email, phone, address, isIntern)
                    println("Paciente agregado exitosamente!")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }

            "3" -> {
                try {
                    println("Ingrese ID del paciente a editar:")
                    val id = readLine()!!

                    println("Nuevo nombre (deje vacío para no cambiar):")
                    val name = readLine()?.takeIf { it.isNotEmpty() }

                    println("Nuevo género (M/F) (deje vacío para no cambiar):")
                    val gender = readLine()?.takeIf { it.isNotEmpty() }

                    println("Nuevo email (deje vacío para no cambiar):")
                    val email = readLine()?.takeIf { it.isNotEmpty() }

                    println("Nuevo teléfono (deje vacío para no cambiar):")
                    val phone = readLine()?.takeIf { it.isNotEmpty() }

                    println("¿Cambiar dirección? (S/N):")
                    val changeAddress = readLine()?.uppercase() == "S"
                    val address = if (changeAddress) {
                        println("Calle:");
                        val street = readLine()!!
                        println("Número:");
                        val number = readLine()!!.toInt()
                        println("Barrio:");
                        val neighborhood = readLine()!!
                        println("Ciudad:");
                        val city = readLine()!!
                        println("Código postal:");
                        val postalCode = readLine()!!
                        Address(street, number, neighborhood, city, postalCode)
                    } else null
                    println("¿Cambiar estado de internamiento? (S/N):")
                    val internInput = readLine()?.uppercase()
                    val isIntern: Boolean? = when (internInput) {
                        "S" -> !hospital.findPatientById(id)!!.isIntern  
                        "N" -> null   // no cambia nada
                        else -> null
                    }

                    hospital.updatePatient(identificationNumber = id, fullName = name, gender = gender, email = email, phoneNumber = phone, address = address, isIntern = isIntern)
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }

            "4" -> {
                println("Ingrese ID del paciente a eliminar:")
                val id = readLine()!!
                try {
                    hospital.deletePatient(id)
                    println("Paciente eliminado")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }

            "5" -> {
                val percentages = hospital.getPatientGenderPercentage()
                if (percentages.isEmpty()) {
                    println("No hay pacientes registrados")
                } else {
                    println("\nPORCENTAJE POR GÉNERO:")
                    percentages.forEach { (gender, percent) ->
                        println("$gender: ${"%.2f".format(percent)}%")
                    }
                }
            }

            "6" -> {
                println("\nLISTA DE TODOS LOS PACIENTES:")
                if (hospital.generalPatients.isEmpty()) {
                    println("No hay pacientes registrados")
                } else {
                    hospital.generalPatients.forEach { println(it) }
                }
            }

            "7" -> {
                println("\nLISTA DE PACIENTES INTERNADOS:")
                if (hospital.internedPatients.isEmpty()) {
                    println("No hay pacientes internados")
                } else {
                    hospital.internedPatients.forEach { println(it) }
                }
            }

            "8" -> return
            else -> println("Opción inválida")
        }
        printPatientMenu()
    }
}

fun printMainMenu() {
    println("""
        ---------------------------------------
        1. Gestión de Médicos
        2. Gestión de Pacientes
        3. Salir
        ---------------------------------------
        
    """.trimIndent())
}

fun printDoctorsMenu(){
    println("""
        ---------------------------------------
        GESTIÓN DE MÉDICOS
        1. Buscar
        2. Agregar
        3. Editar
        4. Eliminar
        5. Calcular el total de salarios de todos los médicos
        6. Calcular el total de salarios de todos los médicos por especialidad
        7. Obtener la cantidad de médicos según su especialidad
        8. Identificar el médico con más antigüedad e indicar su especialidad
        9. Imprimir todos los doctores
        10. Volver
        ---------------------------------------
        
        """.trimIndent())
}

fun printPatientMenu(){
    println("""
        ---------------------------------------
        GESTIÓN DE PACIENTES
        1. Buscar
        2. Agregar
        3. Editar
        4. Eliminar
        5. Obtener el porcentaje de pacientes según su género
        6. Ver todos los pacientes
        7. Ver paciente internados
        8. Volver
        ---------------------------------------
        
    """.trimIndent())
}