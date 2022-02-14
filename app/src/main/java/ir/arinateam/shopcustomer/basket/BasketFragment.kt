package ir.arinateam.shopcustomer.basket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arinateam.shopcustomer.R
import ir.arinateam.shopcustomer.basket.adapter.AdapterRecBasket
import ir.arinateam.shopcustomer.basket.model.ModelRecBasket
import ir.arinateam.shopcustomer.databinding.BasketFragmentBinding
import ir.arinateam.shopcustomer.home.adapter.AdapterRecProduct
import ir.arinateam.shopcustomer.home.model.ModelRecHomeProduct

class BasketFragment : Fragment() {

    private lateinit var bindingFragment: BasketFragmentBinding

    private lateinit var recProducts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFragment =
            DataBindingUtil.inflate(inflater, R.layout.basket_fragment, container, false)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView() {

        recProducts = bindingFragment.recProducts

    }


    private lateinit var adapterRecBasket: AdapterRecBasket
    private lateinit var lsModelRecBasket: ArrayList<ModelRecBasket>

    private fun setRecBasket() {

        adapterRecBasket = AdapterRecBasket(requireActivity(), lsModelRecBasket)

        val linearLayoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)
        recProducts.layoutManager = linearLayoutManager
        recProducts.adapter = adapterRecBasket

    }


}