package com.thiha.android4k.criminalintent

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thiha.android4k.criminalintent.Adapter.Callbacks
import java.util.*

class CrimeListFragment : Fragment(), Callbacks {

    /**
     * Required interface for hosting activities
     */
    interface CrimeListFragmentCallbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this.requireActivity()).get(CrimeListViewModel::class.java)
    }
    private lateinit var crimeRecyclerView: RecyclerView
    private var mCallbacks: CrimeListFragmentCallbacks? = null
    private val adapter: Adapter by lazy {
        Adapter(this)
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }

        private const val TAG = "CrimeListFragment"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mCallbacks = context as CrimeListFragmentCallbacks
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = adapter
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return view
    }

    override fun onCrimeSelected(crimeId: UUID) {
        mCallbacks?.onCrimeSelected(crimeId)
    }
}