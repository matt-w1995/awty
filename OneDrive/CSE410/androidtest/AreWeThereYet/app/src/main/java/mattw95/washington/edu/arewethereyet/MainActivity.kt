package mattw95.washington.edu.arewethereyet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var PENDING_INTENT = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.buttonStart)
        val message = findViewById<EditText>(R.id.editTextMessage)
        val phone = findViewById<EditText>(R.id.editTextPhone)
        val delay = findViewById<EditText>(R.id.editTextDelay)

        var messageFilled = false
        var phoneFilled = false
        var delayFilled = false

        btnStart.isEnabled = false

        message.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                messageFilled = true
                if(messageFilled && phoneFilled && delayFilled){
                    btnStart.isEnabled = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        phone.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                phoneFilled = true
                if(messageFilled && phoneFilled && delayFilled){
                    btnStart.isEnabled = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        delay.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                delayFilled = true
                if(messageFilled && phoneFilled && delayFilled){
                    btnStart.isEnabled = true
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnStart.setOnClickListener {

            if(checkValues(phone.text.toString(), message.text.toString(), delay.text.toString())) {
                val phoneString = phone.text.toString()
                val formatted = "(${phoneString.substring(0, 3)}) ${phoneString.substring(3, 6)}-${phoneString.substring(6, 10)}"

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, MyReceiver::class.java)
                intent.putExtra("message", editTextMessage.text.toString())
                intent.putExtra("phone", formatted)
                val pendingIntent = PendingIntent.getBroadcast(this, PENDING_INTENT, intent, 0)

                if (btnStart.text.toString() == "Start") {
                    btnStart.text = "Stop"

                    val delay = editTextDelay.text.toString().toLong() * 1000 * 60
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, delay, pendingIntent)
                } else {
                    btnStart.text = "Start"
                    if (alarmManager != null) {
                        alarmManager.cancel(pendingIntent)
                    }
                    PENDING_INTENT = Random().nextInt()
                }
            }
        }
    }

    fun checkValues(phone : String, message : String, delay : String) : Boolean{
        if(phone.length != 10){
            Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
            return false
        }
        if(message == "" || message == null){
            Toast.makeText(this, "Invalid message", Toast.LENGTH_SHORT).show()
            return false
        }
        if(delay == ""){
            Toast.makeText(this, "Invalid delay", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent!!.extras
        val message = bundle.getString("message")
        val phone = bundle.getString("phone")
        Toast.makeText(context, "$phone: $message", Toast.LENGTH_SHORT).show()
    }
}
