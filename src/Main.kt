import models.Address
import models.Hospital

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
                printPatientMenu()
            }
            "3" -> println("Bye bye :D")
            else -> println("¿Que no vio que ese numero no está en el menú? >:V")
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
            //FIXME: No sé si validar cada campo uno a la vez
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
        GESTIÓN DE MÉDICOS
        1. Buscar
        2. Agregar
        3. Editar
        4. Eliminar
        5. Obtener el porcentaje de pacientes según su género
        6. Volver
        ---------------------------------------
        
    """.trimIndent())
}