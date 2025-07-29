package vn.minh_nguyen.vkey.view_mover

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import vn.minh_nguyen.vkey.view_mover.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnA.setOnClickListener {
            ViewMover.move(binding.gifTab).from(binding.btnA).to(binding.btnB).duration(1500)
                .start()
        }

        binding.btnB.setOnClickListener {
            ViewMover.move(binding.gifTab).from(binding.btnB).to(binding.btnC).duration(1500)
                .start()
        }

        binding.btnC.setOnClickListener {
            binding.gifTab.visibility = View.GONE
        }
    }
}