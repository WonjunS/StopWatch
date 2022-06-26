package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var timerTask: Timer? = null
    private var isRunning = false
    private var lap = 1

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            isRunning = !isRunning // isRunning 변수의 값을 반전 시킨다

            if (isRunning) { // 실행시 start()
                start()
            } else {         // 정지시 pause()
                pause()
            }
        }

        binding.lapButton.setOnClickListener {
            recordLapTime()
        }

        binding.resetFab.setOnClickListener {
            reset()
        }
    }

    private fun pause() {
        binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24) // FAB를 클릭하면 시작 이미지로 변경
        timerTask?.cancel() // 실행중인 타이머가 있다면 취소
    }

    private fun start() {
        binding.fab.setImageResource(R.drawable.ic_baseline_pause_24)      // FAB를 클릭 일시정지 이미지로 변경
        timerTask = timer(period = 10) {
            time++ // 0.01초 마다 이 변수를 증가시킴
            val sec = time / 100
            val milli = time % 100
            runOnUiThread { // UI 갱신
                binding.secTextView.text = "$sec"
                if(milli < 10)
                    binding.milliTextView.text = "0$milli"
                else
                    binding.milliTextView.text = "$milli"
            }
        }
    }

    private fun recordLapTime() {
        val lapTime = this.time // 현재 시간을 지역 변수에 저장
        val textView = TextView(this) // 동적으로 TextView를 생성
        if(time % 100 < 10)
            textView.text = "$lap LAP : ${lapTime / 100}.0${lapTime % 100}"
        else
            textView.text = "$lap LAP : ${lapTime / 100}.${lapTime % 100}"

        // 맨 위에 랩타임 추가
        binding.lapLayout.addView(textView, 0)
        lap++
    }

    private fun reset() {
        timerTask?.cancel() // 실행중인 타이머가 있다면 취소

        // 모든 변수 초기화
        time = 0
        isRunning = false

        binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.secTextView.text = "0"
        binding.milliTextView.text = "00"

        // 모든 랩타임을 제거
        binding.lapLayout.removeAllViews()
        lap = 1
    }
}