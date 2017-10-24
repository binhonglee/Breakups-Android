package bills.breakup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class GetInfo : AppCompatActivity() {
    private var userCount: EditText? = null
    private var feedback: TextView? = null
    private var layout: LinearLayout? = null
    private val LOG_TAG = "OkHttp"
    private var startBtn: Button? = null
    private var confirmBtn: Button? = null
    private var amounts = ArrayList<EditText>()
    private var res: String = "fail"

    private val JSON = MediaType.parse("application/json; charset=utf-8")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_info)

        userCount = findViewById(R.id.userCountEditTxt) as EditText
        layout = findViewById(R.id.forInputs) as LinearLayout
        startBtn = findViewById(R.id.startBtn) as Button
        startBtn!!.setOnClickListener {
            createMoreGUIS(Integer.valueOf(userCount!!.text.toString()))
        }
        confirmBtn = findViewById(R.id.confirmBtn) as Button
        confirmBtn!!.setOnClickListener {
            layout!!.removeAllViewsInLayout()
            layout!!.addView(feedback)
            feedback!!.visibility = View.VISIBLE
            get()
            feedback!!.text = res
        }

        feedback = TextView(this)
        feedback!!.visibility = View.GONE
    }

    private fun createMoreGUIS(amount: Int) {
        layout!!.removeAllViewsInLayout()
        for (i in 0..(amount - 1)) {
            val thisTxtView = TextView(this)
            val thisEditTxt = EditText(this)
            thisTxtView.setText("User" + (i+1) + ": ")
            thisTxtView.textSize = 18F
            thisEditTxt.textSize = 15F
            thisEditTxt.minWidth = 100
            layout!!.addView(thisTxtView)
            layout!!.addView(thisEditTxt)
            amounts.add(thisEditTxt)
        }
    }

    private fun get() {
        val inputs = ArrayList<Int>()
        (0..(amounts.size - 1)).mapTo(inputs) { Integer.valueOf(amounts[it].text.toString()) }

        var json: String = "{\n" +
                "  \"users\": [\n"

        for (i in 0..(inputs.size - 2)) {
            json += "    {\n" +
                    "      \"name\": \"User" + (i+1) + "\",\n" +
                    "      \"amount\": " + inputs[i] + "\n" +
                    "    },\n"
        }

        json += "    {\n" +
                "      \"name\": \"User" + (inputs.size) + "\",\n" +
                "      \"amount\": " + inputs[inputs.size - 1] + "\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        val client = OkHttpClient()
        val request = Request.Builder()
                .url("https://breakups.herokuapp.com/paymentChain")
                .post(RequestBody.create(JSON, json))
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                res = response!!.body()!!.string()
                Log.i(LOG_TAG, response.toString())
            }

            override fun onFailure(call: Call?, e: IOException?) {
                Log.e(LOG_TAG, e.toString())
            }
        })
    }
}
