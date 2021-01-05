package com.stkent.bottomkeypadavoidance.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stkent.bottomkeypadavoidance.databinding.FragmentKeypadBinding

class KeypadFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentKeypadBinding.inflate(inflater, container, false).root
    }

}
