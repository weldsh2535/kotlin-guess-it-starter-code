package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

private val EXCELENT = "Excelent"
private val VERYGOOD = "Very good"
private val GOOD = "Good"
private val LOW = "Low"
/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {

    enum class rewardType{
        EXCELENT ,
        VERYGOOD,
        GOOD,
        LOW
    }
    private lateinit var viewModel: ScoreViewModel
    private lateinit var viewModelFactory: ScoreViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )

        // Get args using by navArgs property delegate
        val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

        viewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score,scoreFragmentArgs.reward)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(ScoreViewModel::class.java)
        binding.scoreViewModel = viewModel

        //Add observer for score
        viewModel.score.observe(this, Observer {
            binding.scoreText.text = it.toString()
            val score = it
            val wordSize = (viewModel.wordSize.value)?.toInt()?:0
            if (15 < score && score <= wordSize){
                binding.rewardText.text = rewardType.EXCELENT.toString()
                Log.i("MyTag12",rewardType.EXCELENT.toString())
            }else if (10 < score && score <= 15){
                binding.rewardText.text = rewardType.GOOD.toString()
            }else{
                binding.rewardText.text = rewardType.LOW.toString()
            }
            Log.i("MyTag2",scoreFragmentArgs.score.toString())
        })
       // binding.scoreText.text = scoreFragmentArgs.score.toString()
        //Navigates back to title when button is pressed
        viewModel.eventPlayAgain.observe(this, Observer {
            if (it){
                findNavController().navigate(ScoreFragmentDirections.actionRestart())
                viewModel.onPlayAgainComplete()
            }
        })
      ///  binding.playAgainButton.setOnClickListener { onPlayAgain() }

        return binding.root
    }

    private fun onPlayAgain() {
        findNavController().navigate(ScoreFragmentDirections.actionRestart())
    }

}
