package com.robox.galaxy//package com.robox.galaxy
//
//import com.google.firebase.firestore.FirebaseFirestore
//
//data class AppData (val EnrNo : String ,val courses : List<String> = emptyList())
//
//fun main(){
//    val studentData = listOf(
//        AppData("20001012024",  listOf("MAD", "DS")),
//    )
//    fun uploadingDataToFirestore(){
//        val db =FirebaseFirestore.getInstance()
//        val studentCollection= db.collection("STUDENTS")
//        studentData.forEach{student->
//            studentCollection.document(student.EnrNo)
//                .set(student)
//                .addOnSuccessListener {
//                    println("Uploaded student")
//                }
//                .addOnFailureListener { e ->
//                    println("Error uploading : $e")
//                }
//        }
//    }
//}
