package models;

class Converter {

    fun fromDecimalToBinary(num: Int): String {
        if (num <= 1) return num.toString()
        val remainder = num % 2
        val newValue = num / 2
        return "${fromDecimalToBinary(newValue)}$remainder"
    }

    fun fromBinaryToDecimal(binary: String): Int {
        val exponent = binary.length - 1
        var power = 0
        if (binary[0] == '1') {
            power = (Math.pow(2.0, exponent.toDouble())).toInt()
        }
        if (binary.length == 1) return power
        val newBinary = binary.substring(1)
        return fromBinaryToDecimal(newBinary) + power
    }
}

