import kotlin.math.max
import kotlin.math.pow

/*Suavisado para determinar la probabilidad de
cada candidato*/
interface  RankingModel{
    fun rank(
        languageModel: LanguajeModel,
        history: List<String>,
        modelOrder:Int,
        order: Int,
    ): Map<String,Float>

}

class StupidRankingModel: RankingModel {
    override fun rank(languageModel: LanguajeModel, history: List<String>, modelOrder: Int, order: Int): Map<String, Float> {
        /*hay dividir el hisotorial en palabras , aki lo divide en letras*/
        val currentHistory = history.drop(max(history.size-order-1, 0))//0, como etas
        val lesserOrderHistory = currentHistory.dropLast(1)
        return if (order == 1){//pregunta por si es 1 pues vamos bajando de nivel de n-gram
            val orderOneHistory = if ( currentHistory.size > 1) currentHistory.drop(1) else currentHistory
            val total = languageModel[orderOneHistory.toString()]?.values?.sum() ?: 1
            languageModel[orderOneHistory.toString()]?.mapValues {  it.value.toFloat().div(total)} ?: mapOf()
        }else{
            languageModel[currentHistory.toString()]?.let { distribution ->
                val lessOrderCount = languageModel[lesserOrderHistory.toString()]?.values?.sum()?: 1
                val lambdaCorrection = 0.4.pow(modelOrder - order).toFloat()
                distribution.map {
                    val orderCount = lambdaCorrection * it.value.toFloat().div(lessOrderCount)
                    Pair(it.key,orderCount)
                }.toMap()
            }?:mapOf()
        }
    }
}