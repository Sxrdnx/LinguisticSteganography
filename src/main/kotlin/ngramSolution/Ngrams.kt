package ngramSolution

import StupidRankingModel
import huffmanTree.HuffmanSolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.collections.HashMap
import kotlin.math.min

typealias LanguajeModel = HashMap<String,MutableMap<String,Int>>
class Ngrams {
    suspend fun train(data: String,order: Int): LanguajeModel {
        val result= coroutineScope {
            (1..order).map{ async(Dispatchers.IO) { training(data,it) }}.awaitAll()
        }
        return LanguajeModel().apply {
            result.map { putAll(it) }
        }
    }

    private fun training(data: String, order: Int): LanguajeModel {
        println("Training for order: $order")
        val words = data.split(" ")// lista de palabras
        val model = LanguajeModel()// modelo (palabras, (palabra,frecuencia))
        for (i in 0 until words.size-order){
            val ultimoIndice = min(i+order,words.size-1)// ecoje el valor minimo (0+5,10000-1)=5
            val ngram = words.slice(IntRange(i,ultimoIndice - 1)).toString()//parte la lista de palabras en un rango dado(0,5-1)=4(0,1,2,3,4)
            val aWord = words[ultimoIndice]// palabra siguiente del ngrama=listaWord(0,1,2,3,4/5*)
            val entry = model.getOrElse(ngram){ mutableMapOf(aWord to 0)}//retorna el valor, dado una llave pero si no lo encuentra lo crea(tambien evita la repeticion)
            val count = entry.getOrDefault(aWord,0)//retorna el valor correspondiente a una llave dada si no da el valor por default
            entry[aWord] = count.plus(1)//aumenta el valor de la pa
            model[ngram]=entry
        }
        println("END Training for order $order")
        return model
    }
    fun testGenerateText(languajeModel: LanguajeModel, order: Int, hidenData: String, seed:String = ""){
        //history debe ser un ngrma del orden dado
        var history = "- ".repeat(order).split(" ").dropLast(1)
        val listSeed = seed.split(" ")
        var data =hidenData
        var length =hidenData.length
        var i=0
        var message = ""
        //prueba en el rango de palabras(el orden debe ser mallor al size de la seed->[10>7])
        while (length > 1){
            val aWord = if(i < listSeed.size){
                val seedTemp=listSeed[i]//retorna las palabra semilla
                i++
                seedTemp
            }else{
                val can = generateCandidates(languajeModel,order,history)//- - - - - -
                if(can.isNotEmpty()){
                  val(word,actualHidenData)= HuffmanSolution().encode(can as HashMap<String, Float>, data)
                    length=actualHidenData.length
                    data = actualHidenData
                    word                    
                }else{
                    ""
                }
            }
            /*elimina primer elemento y añade la sig palabra pero lo maneja
            * caracter por caracter hay que eliminar y agregar como palabra*/
            history = history.drop(1).plus(aWord)//elimina un numero de elementos de la lista y retorna la lista con la palabra siguiente
            message += "$aWord "
        }
        println(message)
    }
/*
* this is a very/4
* this is a very/sunny
* p(sunny|this is a very)=0
* p(garrafon|this is very)*0.4=
* */
    fun generateCandidates(languajeModel: LanguajeModel, order: Int, history: List<String>):Map<String, Float>{
        var candidates = mutableMapOf<String,Float>()
        var backoffOrder = order
        while (candidates.isEmpty() && backoffOrder > 0){
            candidates = StupidRankingModel().rank(languajeModel,history,order,backoffOrder--).toMutableMap()
            //candidatos <- rankeoModel.ranking(lenguaje model,history,orden--)
        }
        return candidates
    }
}