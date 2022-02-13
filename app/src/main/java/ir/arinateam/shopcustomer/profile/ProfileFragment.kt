package ir.arinateam.shopcustomer.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.databinding.ProfileFragmentBinding


class ProfileFragment : Fragment() {

    private lateinit var bindingFragment: ProfileFragmentBinding

    private lateinit var imgBack: ImageView
    private lateinit var llhMyComment: LinearLayout
    private lateinit var llhMyFavorites: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        openCommentFragment()

        openFavoritesFragment()

        back()

    }

    private fun initView() {

        imgBack = bindingFragment.imgBack
        llhMyComment = bindingFragment.llhMyComment
        llhMyFavorites = bindingFragment.llhMyFavorites

    }

    private fun openCommentFragment() {

        llhMyComment.setOnClickListener {



        }

    }

    private fun openFavoritesFragment() {

        llhMyFavorites.setOnClickListener {

            Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_favoriteFragment)

        }

    }

    private fun back() {

        imgBack.setOnClickListener {

            Navigation.findNavController(it).popBackStack()

        }

    }

}