package utils

fun HashMap<String,Float>.toFormat(formatString : String ="%.3f"){
    val tempK = this.keys
    for (c in tempK){
        this[c] = formatString.format(this[c]).toFloat()
    }
}