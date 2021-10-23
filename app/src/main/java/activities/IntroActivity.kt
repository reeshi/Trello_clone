package activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.trello_clone.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )

        val buttonSignIn = findViewById<Button>(R.id.btn_sign_in_intro)
        buttonSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        val buttonSignUp = findViewById<Button>(R.id.btn_sign_up_intro)
        buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}