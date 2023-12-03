package com.example.repairpal

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnSubmit: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etPrice = findViewById(R.id.etPrice)
        btnSubmit = findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()
            val price = etPrice.text.toString().toDouble()

            // Here you can call the Daraja API to initiate the payment
            getAccessToken { accessToken ->
                initiateSTKPush(accessToken, phoneNumber, price)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAccessToken(callback: (String) -> Unit) {
        /*val properties = Properties().apply {
            val inputStream = this@PaymentActivity.resources.openRawResource(R.config)
            load(inputStream)
        }*/

        val consumerKey = "AeUdfOmoearGvNtAWgQTcW4e1FgRvAcg"
        val consumerSecret = "XhYLoAXGcrHl6BZU"
        val credentials = "$consumerKey:$consumerSecret"
        val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray(Charsets.UTF_8))

        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
            .addHeader("Authorization", "Basic $encodedCredentials")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Log the error or show a message to the user
                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonObject = JSONObject(response.body?.string())
                val accessToken = jsonObject.getString("access_token")
                callback(accessToken)
            }
        })
    }

    private fun initiateSTKPush(accessToken: String, phoneNumber: String, price: Double) {
        val client = OkHttpClient()
        val jsonObject = JSONObject()
        jsonObject.put("BusinessShortCode", 174379)
        jsonObject.put("Password", "MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTYwMjE2MTY1NjI3")
        jsonObject.put("Timestamp", "20160216165627")
        jsonObject.put("TransactionType", "CustomerPayBillOnline")
        jsonObject.put("Amount", price)
        jsonObject.put("PartyA", phoneNumber)
        jsonObject.put("PartyB", 174379)
        jsonObject.put("PhoneNumber", phoneNumber)
        jsonObject.put("CallBackURL", "https://mydomain.com/path")
        jsonObject.put("AccountReference", "CompanyXLTD")
        jsonObject.put("TransactionDesc", "Payment of X")

        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())
        val request = Request.Builder()
            .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
            .addHeader("Authorization", "Bearer $accessToken")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Log the error or show a message to the user
                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // The request was successful
                    val jsonObject = JSONObject(response.body?.string())
                    // Parse the jsonObject to get the information you need
                    val resultCode = jsonObject.getString("ResultCode")
                    if (resultCode == "0") {
                        // The transaction was successful
                        println("Transaction successful")
                    } else {
                        // The transaction failed
                        val resultDesc = jsonObject.getString("ResultDesc")
                        println("Transaction failed: $resultDesc")
                    }
                } else {
                    // The request failed
                    println("Request failed: ${response.code}")
                }
            }
        })
    }
}