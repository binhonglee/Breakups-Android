package bills.breakup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout

class GetInfo : AppCompatActivity() {
    private var userCount: EditText? = null
    private var layout: LinearLayout? = null
    private var startBtn: Button? = null
    private var confirmBtn: Button? = null
    private var amounts = ArrayList<EditText>()
    private var names = ArrayList<EditText>()

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
            get()
        }
    }

    private fun createMoreGUIS(amount: Int) {
        layout!!.removeAllViewsInLayout()
        for (i in 0..(amount - 1)) {
            val name = EditText(this)
            val amount = EditText(this)
            name.textSize = 15F
            name.maxWidth = 50
            name.hint = "Name"
            amount.textSize = 15F
            amount.minWidth = 100
            amount.inputType = InputType.TYPE_CLASS_NUMBER
            amount.hint = "Amount paid"
            layout!!.addView(name)
            layout!!.addView(amount)
            names.add(name)
            amounts.add(amount)
        }
    }

    private fun get() {
        var json: String = "{\n" +
                "  \"users\": [\n"

        for (i in 0..(amounts.size - 2)) {
            json += "    {\n" +
                    "      \"name\": \"" + names[i].text.toString() + "\",\n" +
                    "      \"amount\": " + Integer.valueOf(amounts[i].text.toString()) + "\n" +
                    "    },\n"
        }

        json += "    {\n" +
                "      \"name\": \"User" + names[(names.size - 1)].text.toString() + "\",\n" +
                "      \"amount\": " + Integer.valueOf(amounts[(names.size - 1)].text.toString()) + "\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        val registerIntent = Intent(this@GetInfo, ShowResult::class.java)
        registerIntent.putExtra("data", json)
        startActivity(registerIntent)
    }
}
