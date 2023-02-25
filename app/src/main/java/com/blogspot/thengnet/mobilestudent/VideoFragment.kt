package com.blogspot.thengnet.mobilestudent

import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.blogspot.thengnet.mobilestudent.databinding.FragmentVideoBinding

/**
 * A simple [Fragment] subclass for video files.
 */
class VideoFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var _binding: FragmentVideoBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val LOG_TAG = VideoFragment::class.java.name
    var controlsFragment: Fragment? = null
    var childrenManager: FragmentManager? = null
    var channel: FragmentTransaction? = null
    private val mVideoFiles: ArrayList<Audio>? = null
    private var videoAdapter: CursorAdapter? = null
    private lateinit var mVideoTableColumns: Array<String>
    private val mVideoSortOrder =
        MediaStore.Audio.Media.TITLE + " ASC" // TODO: present option in preferences
    private val mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mVideoTableColumns = arrayOf(
            MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.DATA,
            MediaStore.Video.Media.HEIGHT, MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.RESOLUTION
        )
        loaderManager.initLoader(VIDEO_LOADER_ID, null, this)

        // instantiate the {@link mVideoPlayer} object
        mVideoPlayer = MediaPlayer()

        // register callback methods for the {@link mVideoPlayer} object
//        mVideoPlayer.setOnCompletionListener(this);
//        mVideoPlayer.setOnErrorListener(this);
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        // create and initialise the ArrayAdapter<Video> object
        // to parse the {@link Video} list item views
        videoAdapter = AudioAdapter(context, null)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the {@link audioAdapter} ArrayAdapter on the ListView
        val gvAudio = view.findViewById<View>(R.id.gv_video) as GridView
        gvAudio.adapter = videoAdapter
        gvAudio.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val vid = Intent(context, VideoActivity::class.java)
            vid.data = ContentUris.withAppendedId(mVideoUri, id)
            startActivity(vid)
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(), mVideoUri, mVideoTableColumns, null,
            null, mVideoSortOrder
        )
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor) {
        mVideoCursor = cursor
        if (mVideoCursor != null && mVideoCursor!!.count > 0) {
            // release the device resources after the {@link mVideoCursor} object has been utilised
//            mVideoCursor.close();
            videoAdapter!!.swapCursor(cursor)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        videoAdapter!!.swapCursor(null)
    }

    companion object {
        private const val VIDEO_LOADER_ID = 5
        private var mVideoCursor: Cursor? = null
        private var mVideoPlayer: MediaPlayer? = null
    }
}