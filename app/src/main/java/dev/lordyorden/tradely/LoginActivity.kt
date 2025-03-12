package dev.lordyorden.tradely

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dev.lordyorden.tradely.models.Profile
import dev.lordyorden.tradely.models.ProfileManager
import dev.lordyorden.tradely.models.StockManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(FirebaseAuth.getInstance().currentUser == null){
            signIn()
        }else{
            transactToNextScreen()
        }
    }



    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_stock_explore)
            .build()
        signInLauncher.launch(signInIntent)
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        //val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            transactToNextScreen()
        } else {
            Toast.makeText(this, "Error: failed logging in.", Toast.LENGTH_LONG).show()
            signIn()
        }
    }

    private fun transactToNextScreen() {
        loadProfile()
        StockManager.getInstance().registerStocks()
        ProfileManager.getInstance().addListener()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val profile: Profile = Profile.Builder()
                .id(it.uid)
                .name(it.displayName.toString())
                .profilePic(it.photoUrl.toString())
                .build()
            ProfileManager.getInstance().loadOrCreateProfile(profile)
        }
    }
}