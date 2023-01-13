package com.example.android.guesstheword.screens.game

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel

        binding.correctButton.setOnClickListener {
            viewModel.onCorrect()
           // updateWordText()
        }
        binding.skipButton.setOnClickListener {
            viewModel.onSkip()
           // updateWordText()
        }
        viewModel.word.observe(this, Observer { newWord ->
            binding.wordText.text = newWord
        })
        viewModel.score.observe(this, Observer{ newScore ->
            binding.scoreText.text = newScore.toString()
        })

        viewModel.currentTime.observe(this, Observer { newTime ->
            binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

       //Sets up event listening to navigate the player when the game is finished
        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished){
                val currentScore = viewModel.score.value ?: 0
                val currentWordSize:String = viewModel.wordSize.value.toString()
                val action = GameFragmentDirections.actionGameToScore(currentScore,currentWordSize)
                findNavController(this).navigate(action)
                viewModel.onGameFinishComplete()
            }
        })

        viewModel.eventBuzz.observe(viewLifecycleOwner, Observer { buzzType ->
            if (buzzType != GameViewModel.BuzzType.NO_BUZZ){
                buzz(buzzType.pattern)
                viewModel.onBuzzComplete()
            }
        })

       // updateWordText()
        return binding.root

    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?:0)
        findNavController(this).navigate(action)
    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray){
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                buzzer.vibrate(VibrationEffect.createWaveform(pattern,-1))
            }else{
                buzzer.vibrate(pattern,-1)
            }
        }
    }
    /** Methods for updating the UI **/

//    private fun updateWordText() {
//        binding.wordText.text = viewModel.word
//
//    }

}
