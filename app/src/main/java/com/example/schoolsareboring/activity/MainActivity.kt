package com.example.schoolsareboring.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolsareboring.PreferenceManager
import com.example.schoolsareboring.R
import com.example.schoolsareboring.models.HomeContent
import com.example.schoolsareboring.ui.theme.SchoolsAreBoringTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                Scaffold(modifier = Modifier.fillMaxSize().padding(WindowInsets.systemBars.asPaddingValues()),
                    ) { innerPadding ->
                    val context= LocalContext.current
                    val preferenceManager= remember { PreferenceManager(context) }

                    if (preferenceManager.isLoggedIn()) {
                    Welcome(
                        modifier = Modifier.padding(innerPadding)
                    )}
                    else{
                        context.startActivity(Intent(context, UsertypeActivity::class.java))
                        (context as? Activity)?.finish()
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Welcome(modifier: Modifier = Modifier) {
    val context= LocalContext.current
    val preferenceManager= remember { PreferenceManager(context) }

    MainActivityScreen(preferenceManager)

}

@Composable
fun MainActivityScreen(preferenceManager: PreferenceManager) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
            Column(Modifier.padding(10.dp)) {
                Text("Hello", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(bottom = 2.dp))
                Text(preferenceManager.getData("name"), color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
            IconButton(onClick = {
                context.startActivity(Intent(context, LoginActivity::class.java))
                (context as Activity).finish()
                preferenceManager.logOut()
            },
                modifier = Modifier.padding(10.dp).width(45.dp).height(45.dp)) {
                Icon(Icons.Default.ExitToApp, contentDescription = "logOut")
            }
        }
        Spacer(Modifier.height(1.dp).fillMaxWidth().background(Color.DarkGray).border(1.dp,Color.DarkGray))

        MainContent()
    }
}

@Composable
fun MainContent() {
    val context = LocalContext.current
    val preferenceManager = remember { PreferenceManager(context) }

    val userType = preferenceManager.getData("userType") ?: ""

    // Full list of items
    val allItems = listOf(
        HomeContent(R.drawable.student, "Students", "students"),
        HomeContent(R.drawable.teacher, "Teachers", "teacher"),
        HomeContent(R.drawable.attendence, "Attendance", "attendance"),
        HomeContent(R.drawable.syllabus, "Syllabus", "syllabus"),
        HomeContent(R.drawable.time_table, "Time Table", "time_table"),
        HomeContent(R.drawable.assignments, "Assignments", "assignments"),
        HomeContent(R.drawable.exam, "Exams", "exam"),
        HomeContent(R.drawable.results, "Results", "result"),
        HomeContent(R.drawable.fees, "Fees", "fees"),
        HomeContent(R.drawable.events, "Events", "events"),
        HomeContent(R.drawable.mail, "Inbox", "inbox"),
    )

    // Define what a student can see
    val studentVisibleRoutes = listOf("attendance", "syllabus", "time_table", "assignments", "exam", "result", "fees", "events", "inbox")

    // Filter list based on userType
    val screenItems = if (userType.lowercase() == "student") {
        allItems.filter { it.route in studentVisibleRoutes }
    } else {
        allItems
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 12.dp, end = 12.dp, bottom = 8.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxHeight()
        ) {
            items(screenItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            when (item.route) {
                                "students" -> context.startActivity(Intent(context, Students::class.java))
                                "teacher" ->/* context.startActivity(Intent(context, TeachersActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "attendance" -> /*context.startActivity(Intent(context, AttendanceActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "syllabus" -> /*context.startActivity(Intent(context, SyllabusActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "time_table" -> /*context.startActivity(Intent(context, TimeTableActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "assignments" -> /*context.startActivity(Intent(context, AssignmentsActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "exam" -> /*context.startActivity(Intent(context, ExamActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "result" -> /*context.startActivity(Intent(context, ResultsActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "fees" -> /*context.startActivity(Intent(context, FeesActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "events" -> /*context.startActivity(Intent(context, EventsActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                                "inbox" -> /*context.startActivity(Intent(context, InboxActivity::class.java))*/ Toast.makeText(context,"Coming soon!!",Toast.LENGTH_SHORT).show()
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = item.img),
                            contentDescription = item.title,
                            modifier = Modifier.size(70.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.title,
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
