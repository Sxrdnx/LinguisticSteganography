package processData

class ProcessorHiddenData {
    fun processData(data: String):String{
        val dot = "\\.".toRegex()
        var finalData =""
        val matCh: Boolean = dot.find(data)?.let{ true } == true
        if (matCh){//si encuentra al menos un "." significa que es una ip o cordenada
            val listData = data.split(".")//divide la variable por puntos
            listData.forEach{
                finalData += toBinary(it.toInt())//combierte cada elemento en entero para poder hacer su reprecentacion Bin
            }
        }else{
            data.forEach {
                finalData += toBinary(it.toInt())
            }
        }
        return finalData
    }

   private fun toBinary(x: Int):String{
        return String.format(
            "%" + LEN_BINARY + "s",
            Integer.toBinaryString(x)
        ).replace(" ".toRegex(), "0")
    }
    //tamaño en octetos para cuando desencripten seapan en que separarlos
    companion object{
        const val LEN_BINARY = 8
    }
}