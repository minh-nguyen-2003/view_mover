package vn.minh_nguyen.vkey.demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import vn.minh_nguyen.vkey.demo.databinding.ActivityMainBinding
import vn.minh_nguyen.vkey.view_mover.ViewMover

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnA.setOnClickListener {
            ViewMover
                .move(binding.gifTab)
                .from(binding.btnA)
                .to(binding.btnB)
                .duration(1500)
                .start()
        }

        binding.btnB.setOnClickListener {
            ViewMover
                .move(binding.gifTab)
                .from(binding.btnB)
                .to(binding.btnC)
                .duration(1500)
                .start()
        }

        binding.btnC.setOnClickListener {
            binding.gifTab.visibility = View.GONE
        }
    }
}