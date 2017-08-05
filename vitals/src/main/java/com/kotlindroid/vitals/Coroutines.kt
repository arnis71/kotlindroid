package com.kotlindroid.vitals

/**
 * Created by arnis on 05/08/2017.
 */

//suspend fun bgAwait(body: EmptyBlock) = bg(body).await()
//
//suspend fun CoroutineScope.delayMilis(milis: Long) = delay(milis, TimeUnit.MILLISECONDS)
//
//fun seq(jobs :Seq.() -> Unit): Seq {
//    val seq = Seq()
//    seq.jobs()
//    return seq
//}
//
//class Seq {
//    var op1: EmptyBlock = {}
//    var op2: EmptyBlock = {}
//    var op3: EmptyBlock = {}
//    var op4: EmptyBlock = {}
//    var op5: EmptyBlock = {}
//
//    fun run() {
//        async(UI) {
//            bg(op1).await()
//            bg(op2).await()
//            bg(op3).await()
//            bg(op4).await()
//            bg(op5).await()
//        }
//    }
//}