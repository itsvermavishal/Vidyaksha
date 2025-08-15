package com.example.vidyaksha.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vidyaksha.R
import com.example.vidyaksha.domain.model.Subject
import com.example.vidyaksha.presentation.components.CountCard
import com.example.vidyaksha.presentation.components.SubjectCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import com.example.vidyaksha.presentation.components.tasksList

@Composable
fun DashboardScreen() {

    val subjects = listOf(
        Subject(name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0]),
        Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1]),
        Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2]),
        Subject(name = "Geology", goalHours = 10f, colors = Subject.subjectCardColors[3]),
        Subject(name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[4]),
    )

    Scaffold (
        topBar = { DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            item{
                CountCardSelection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    subjectCount = 5,
                    studiedHours = "10",
                    goalStudyHours = "15"
                )
            }
            item{
                SubjectCardSelection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjects
                )
            }
            item {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }
            tasksList(
                sectionTitle = "UPCOMIN TASKS",
                emptyListText = "You don't have any upcoming tasks. \n" + "Click the + button in subjects to add new task.",
                tasks = emptyList()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Vidyaksha",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardSelection(
    modifier: Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalStudyHours: String
){
    Row{
        Row(modifier = modifier){
            CountCard(
                modifier = Modifier.weight(1f),
                headingText = "Subject Count",
                count = "$subjectCount"
            )
            Spacer(modifier = Modifier.width(10.dp))
            CountCard(
                modifier = Modifier.weight(1f),
                headingText = "studied Hours",
                count = studiedHours
            )
            Spacer(modifier = Modifier.width(10.dp))
            CountCard(
                modifier = Modifier.weight(1f),
                headingText = "Goal Study Hours",
                count = goalStudyHours
            )
        }
    }
}

@Composable
private fun SubjectCardSelection(
    modifier: Modifier,
    subjectList: List<Subject>,
    emptyListText: String = "You don't have any subjects. \n Click the + button to add new subject."
){
    Column(modifier = modifier){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "Subject",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = {/*TODO*/}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if (subjectList.isEmpty()){
            Image(
                modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy (12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(subjectList){subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors,
                    onClick = {}
                )
            }
        }
    }
}