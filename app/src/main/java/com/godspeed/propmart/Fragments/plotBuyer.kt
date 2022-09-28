package com.godspeed.propmart.Fragments

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.godspeed.propmart.Adapters.BidsAdapter
import com.godspeed.propmart.Adapters.BookmarkViewPagerAdapter
import com.godspeed.propmart.Adapters.PropertyCardAdapter
import com.godspeed.propmart.Models.Bidscardmodel
import com.godspeed.propmart.Models.PropertyCardModel
import com.godspeed.propmart.Models.sellerhomepageModel
import com.godspeed.propmart.R
import com.godspeed.propmart.databinding.FragmentBidsfragBinding
import com.godspeed.propmart.databinding.FragmentBookmarkBinding
import com.godspeed.propmart.databinding.FragmentPlotbuyerBinding
import com.godspeed.propmart.ui.Bookmarks.BookmarksViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class plotBuyer : Fragment() {
    private var _binding: FragmentPlotbuyerBinding? = null
    private val db = Firebase.firestore
    private val binding get() = _binding!!
    private lateinit var adapter: PropertyCardAdapter;
    private lateinit var cards:MutableList<PropertyCardModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        cards = ArrayList<PropertyCardModel>()
        adapter = PropertyCardAdapter(requireActivity(), cards);
        _binding = FragmentPlotbuyerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.plotRv.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false);
        binding.plotRv.adapter = adapter;

        db.collection("Plots").get().addOnSuccessListener{
            it.documents.iterator().forEach { documentSnapshot ->
                    val title:String = documentSnapshot.get("Area") as String;
                    val seller:String = documentSnapshot.get("Owner Name") as String;
                    val plotnumber:Long = documentSnapshot.get("Plot No").toString().toLong();
                    val address:String = documentSnapshot.get("District") as String;
//                val longitude:String = documentSnapshot.get("longitude") as String;
//                val latitude:String = documentSnapshot.get("latitude") as String;
                    val plotImage:String = documentSnapshot.get("Taluka") as String;
                    val card =
                        PropertyCardModel(documentSnapshot.id, title, seller, address,
                            plotImage, plotnumber, "", "");
                    cards.add(card);
                    adapter.notifyDataSetChanged();
            }
        }.addOnFailureListener {
                Toast.makeText(requireContext(), "No Plots Found !", Toast.LENGTH_SHORT).show()
            }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}