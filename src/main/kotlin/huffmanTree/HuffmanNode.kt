package huffmanTree

data class HuffmanNode(val frecuency: Float,
                       val data: String,
                       var left: HuffmanNode?,
                       var right: HuffmanNode?): Comparable<HuffmanNode>{
    override fun compareTo(other: HuffmanNode): Int {
        return frecuency.compareTo(other.frecuency)
    }
}
