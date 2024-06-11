package com.example.myappdecomida.pedidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.myappdecomida.R
import com.example.myappdecomida.databinding.FragmentPedidosBinding

class Pedidos : Fragment() {

    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmButton = view.findViewById<Button>(R.id.confirm_button) // Asegúrate de que este es el ID correcto para tu botón "Confirmar pedido"
        confirmButton.setOnClickListener {
            val action = R.id.action_confirmarPedido_to_pago
            findNavController().navigate(action)
        }
    }}