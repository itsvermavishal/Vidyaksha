package com.example.vidyaksha.presentation.spark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun SparkScreen(
    viewModel: SparkViewModel = hiltViewModel()
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Spark")
        // Add your Spark UI + ViewModel integration here
    }
}