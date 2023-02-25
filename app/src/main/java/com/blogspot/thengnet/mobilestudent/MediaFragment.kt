package com.blogspot.thengnet.mobilestudent

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.blogspot.thengnet.mobilestudent.databinding.FragmentMediaBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

/**
 * A simple [Fragment] subclass.
 */
class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    // arbitrary code value to match permission request code
    private val EXTERNAL_STORAGE_PERMISSION_CODE = 12
    var pageAdapter: MediaCategoryAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageAdapter = MediaCategoryAdapter(fragmentManager, context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hasStorageAccess = checkStorageAccess(view.context)

        // find the views explaining reason for, and enabling permission grant
        if (hasStorageAccess) {
            hideRationale()
            setupMediaFragments(view)
        } else {
            showRationale()
            binding.btnExternalStorageAccess.setOnClickListener(View.OnClickListener {
                requestStorageAccess(
                    view.context
                )
            })
        }
    }

    private fun showRationale() {
        // make the views available if app has storage access permission denied -- not granted
        binding.btnExternalStorageAccess.visibility = View.VISIBLE
        binding.tvExternalStorageAccess.visibility = View.VISIBLE
    }

    private fun setupMediaFragments(rootView: View?) {
        val mediaPager = rootView!!.findViewById<View>(R.id.media_pager) as ViewPager
        mediaPager.adapter = pageAdapter
        val mediaTabs = rootView.findViewById<View>(R.id.tab_layout) as TabLayout
        mediaTabs.setupWithViewPager(mediaPager)
    }

    private fun hideRationale() {
        // make the views unavailable if app has storage access permission granted
        binding.btnExternalStorageAccess.visibility = View.GONE
        binding.tvExternalStorageAccess.visibility = View.GONE
    }

    private fun checkStorageAccess(appContext: Context): Boolean {
        val permissionStatus =
            ContextCompat.checkSelfPermission(appContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return when (permissionStatus) {
            PackageManager.PERMISSION_GRANTED -> true
            PackageManager.PERMISSION_DENIED -> {
                Snackbar.make(
                    binding.mediaSnackbarFrame,
                    "Permission not granted!",
                    Snackbar.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                Snackbar.make(
                    binding.mediaSnackbarFrame,
                    "Permission not granted!",
                    Snackbar.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

    private fun requestStorageAccess(appContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionSet = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissionSet, EXTERNAL_STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO: Use a SnackBar to notify permission grant
                    Snackbar.make(
                        binding.mediaSnackbarFrame,
                        "Permission Granted!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    hideRationale()
                    setupMediaFragments(view)
                } else {
                    showRationale()
                    Snackbar.make(
                        binding.mediaSnackbarFrame,
                        "Permission Denied!",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            // TODO: Check if user 'denied' permission more than once, and use #Intent to get user
            //  to Settings screen to enable permission manually.
            return
        }
    }
}