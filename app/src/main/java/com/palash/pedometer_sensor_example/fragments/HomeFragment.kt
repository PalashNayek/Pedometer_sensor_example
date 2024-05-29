package com.palash.pedometer_sensor_example.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.palash.pedometer_sensor_example.R
import com.palash.pedometer_sensor_example.databinding.FragmentHomeBinding
import com.palash.pedometer_sensor_example.view_models.StepViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val stepViewModel by viewModels<StepViewModel>()

    companion object {
        private const val PERMISSION_REQUEST_BODY_SENSORS = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACTIVITY_RECOGNITION)
            == PackageManager.PERMISSION_GRANTED) {
            stepViewModel.startStepUpdates()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_REQUEST_BODY_SENSORS
            )
        }
        stepViewModel.stepData.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_BODY_SENSORS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    stepViewModel.startStepUpdates()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied to access body sensors",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    private fun updateUI(steps: Int) {
        if (steps != null) {
            binding.textViewSteps.text = "Steps: $steps"
        } else {
            binding.textViewSteps.text = "Pedometer not support"
        }
    }

    override fun onResume() {
        super.onResume()
        stepViewModel.startStepUpdates()
    }

    override fun onPause() {
        super.onPause()
        stepViewModel.stopStepUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}