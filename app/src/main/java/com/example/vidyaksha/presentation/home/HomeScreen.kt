package com.example.vidyaksha.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Collect the subjects flow exposed by HomeViewModel
    val subjects = viewModel.subjects.collectAsState(initial = emptyList())

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Home (${subjects.value.size} subjects)")
        // TODO: wire your HomeViewModel here and real UI
    }
}