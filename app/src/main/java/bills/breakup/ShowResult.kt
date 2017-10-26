package bills.breakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.*
import java.io.IOException

class ShowResult : AppCompatActivity() {
    private val LOG_TAG = "OkHttp"
    private val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_result)

        val data: String = this.intent.getStringExtra("Data")

        /*
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("https://breakups.herokuapp.com/paymentChain")
                .post(RequestBody.create(JSON, data))
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                response!!.body()!!.string()
                Log.i(LOG_TAG, response.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.e(LOG_TAG, e.toString())
            }
        })
        */

        request(data, "paymentChain")
    }

    private fun request(data:String, type: String) : String {
        var toReturn = ""
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("https://breakups.herokuapp.com/" + type)
                .post(RequestBody.create(JSON, data))
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                toReturn = response!!.body()!!.string()
                Log.i(LOG_TAG, response.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {
                toReturn = "Failed"
                Log.e(LOG_TAG, e.toString())
            }
        })

        return toReturn
    }
}
