package com.blogspot.thengnet.mobilestudent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blogspot.thengnet.mobilestudent.data.TimeConverter
import com.blogspot.thengnet.mobilestudent.databinding.FragmentMediaControlsBinding

/**
 * A simple [Fragment] subclass.
 * Use the empty constructor to create an instance of this fragment.
 */
class MediaControlsFragment : Fragment(), View.OnClickListener, OnLongClickListener {

    private var _binding: FragmentMediaControlsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this.arguments != null) {
            trackTitle = requireArguments().getString(TRACK_TITLE_KEY)
            trackDuration = requireArguments().getLong(TRACK_DURATION_KEY)
        }
        audioFrag = parentFragment as AudioFragment?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMediaControlsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTrackTitle.text = trackTitle
        binding.tvMediaLength.text =
            TimeConverter.convertTime(trackDuration.toString())

        // Attach event-handler - #View.OnClickListener - to the ImageView controllers.
        binding.btnPlayPause.setOnClickListener(this)
        binding.btnPrevTrack.setOnClickListener(this)
        binding.btnNextTrack.setOnClickListener(this)
        binding.btnRewind.setOnClickListener(this)
        binding.btnFastForward.setOnClickListener(this)

        // Attach event-handler - #View.OnLongClickListener - to the ImageView controllers.
        binding.btnPlayPause.setOnLongClickListener(this)
        binding.btnPrevTrack.setOnLongClickListener(this)
        binding.btnNextTrack.setOnLongClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_play_pause -> pausePlayBack()
            R.id.btn_prev_track -> audioFrag!!.previousTrack()
            R.id.btn_next_track -> audioFrag!!.nextTrack()
            R.id.btn_rewind -> audioFrag!!.rewind()
            R.id.btn_fastForward -> audioFrag!!.fastForward()
            else -> {}
        }
    }

    override fun onLongClick(v: View): Boolean {
        when (v.id) {
            R.id.btn_play_pause -> {
                audioFrag!!.stopPlayback()
                return true
            }
            R.id.btn_prev_track, R.id.btn_next_track -> {}
            else -> {}
        }
        return false
    }

    protected fun setAudioMetrics(title: String?, position: Long) {
        // TODO: reset old/new title on TextView and position SeekBar accordingly
        //  consider Data binding or View binding for the purpose.
    }

    private fun pausePlayBack() {
        if (audioFrag!!.isPlaying) {
            audioFrag!!.pausePlayback()
            binding.btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_black_48)
        } else {
            audioFrag!!.playAudioFile()
            binding.btnPlayPause.setImageResource(R.drawable.baseline_pause_black_48)
        }
    }

    companion object {
        // track controller initialiser keys
        private const val TRACK_TITLE_KEY = "track_title"
        private const val TRACK_DURATION_KEY = "track_duration"
        private var trackTitle: String? = null
        private var trackDuration: Long = 0
        private var audioFrag: AudioFragment? = null

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title    Title of the audio file.
         * @param duration Duration of the audio file.
         * @return A new instance of fragment MediaControlsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(title: String?, duration: Long?): MediaControlsFragment {
            val mediaControlsFragment = MediaControlsFragment()
            val mediaMetrics = Bundle()
            mediaMetrics.putString(TRACK_TITLE_KEY, title)
            mediaMetrics.putLong(TRACK_DURATION_KEY, duration!!)
            mediaControlsFragment.arguments = mediaMetrics
            return mediaControlsFragment
        }
    }
}