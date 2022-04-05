package processData

import java.util.*

interface PreProcessor {
    fun processData(data:String):String{
        val r2= "\\.".toRegex()
        val r4= "\\[".toRegex()
        val r5= "\\(".toRegex()
        val r6= "\\)".toRegex()
        val r8= "\\?".toRegex()
        var temp = r2.replace(data," $r2")
        temp = r4.replace(temp," $r4")
        temp = r5.replace(temp," $r5")
        temp = r6.replace(temp," $r6")
        temp = r8.replace(temp," $r8")
        ":,;!]¡¿".forEach{
            val tempTxt = it.toString().toRegex()
            temp = tempTxt.replace(temp," $tempTxt")
        }
        return temp.toLowerCase(Locale.getDefault())
    }
}