package huffmanTree

import utils.toFormat
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap


class HuffmanSolution {
    private val charPrefixHashmap: MutableMap<String, String> = HashMap()
    private lateinit var root: HuffmanNode
    private lateinit var wordSelected: Pair<String,String>

    fun encode(candidates: HashMap<String,Float>,hiddenData:String):Pair<String,String>{
        candidates.toFormat("%.2f")
        root = buildTree(candidates)//crea el arbol
        setPrefixCodes(root,StringBuilder())//crea los codigos para cada candidato
        return selectWord(hiddenData)
    }

    private fun buildTree(data: Map<String, Float>): HuffmanNode {
        val priorityQueue = PriorityQueue<HuffmanNode>()
        val mediumNode = "-"
        val keySet = data.keys//freq.keys
        for (c in keySet) {
            val huffmanNode = HuffmanNode(data[c]!!,c,null,null)
            priorityQueue.offer(huffmanNode)//se crea la cola de prioridad
        }
        while (priorityQueue.size > 1) {
            val x = priorityQueue.peek()//retorna la cabeza de la cola
            priorityQueue.poll()//elimina la cabeza de la cola
            val y = priorityQueue.peek()
            priorityQueue.poll()
            val sum = HuffmanNode(
                x.frecuency + y.frecuency,
                mediumNode,
                x,
                y)
            root = sum
            priorityQueue.offer(sum)
        }
        return priorityQueue.poll()
    }

    private fun setPrefixCodes(node: HuffmanNode?, prefix: StringBuilder) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                charPrefixHashmap[node.data] = prefix.toString()
            } else {
                prefix.append('0')
                setPrefixCodes(node.left, prefix)
                prefix.deleteCharAt(prefix.length - 1)

                prefix.append('1')
                setPrefixCodes(node.right, prefix)
                prefix.deleteCharAt(prefix.length - 1)
            }
        }
    }

    private fun selectWord(data:String): Pair<String,String>{
        //ordenamos los valores de las llaves con respecto a su lenght
        val lengthComparator = Comparator { str1: Pair<String,String>, str2: Pair<String,String> -> str1.second.length - str2.second.length }
        val sort= charPrefixHashmap.toList().sortedWith(lengthComparator).reversed()//lista de candidatos ordenada
        var tempLenghComparetor = sort[0].second.length
        wordSelected = Pair("",data)//no pasar palabras que no existen o su combinacion no existe
        sort.forEachIndexed { index, pair ->
            if (tempLenghComparetor > data.length){
                tempLenghComparetor = sort[index+1].second.length
            }else{
                val subData = data.slice(IntRange(0,pair.second.length-1))
                if (subData == pair.second){
                    println(pair)//<- palabra que contienen el codigo binario que empata con el codigo a ocultar
                    wordSelected=Pair(pair.first,data.drop(pair.second.length))//quitamos los n elementos encontrados en la palbra
                }
            }
        }
        return wordSelected
    }
    fun decode(s: String){
        val stringBuilder = StringBuilder()
        var temp: HuffmanNode? = root
        println("Encode: $s")
        s.forEachIndexed{ _, ch ->
            val j = ch.toInt()
            if (j== 0){
                temp = temp?.left
                if (temp?.left == null && temp?.right == null){
                    stringBuilder.append(temp?.data)
                    temp = root
                }
            }

            if (j==1){
               temp= temp?.right
                if (temp?.left == null && temp?.right == null){
                    stringBuilder.append(temp?.data)
                    temp = root
                }
            }
        }
        println("String decodificado es : ${stringBuilder.toString()}")
    }


}