package com.example.schoolsareboring.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.schoolsareboring.R
import com.example.schoolsareboring.activity.ui.theme.SchoolsAreBoringTheme

class UsertypeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SchoolsAreBoringTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.systemBars,
                    ) { innerPadding ->
                    SelectUserType(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SelectUserType(modifier: Modifier = Modifier) {
    val context= LocalContext.current
   Column (modifier = Modifier.padding(top=15.dp,start=5.dp,end=5.dp)
       .fillMaxSize()){

       Column(modifier=Modifier.padding(top=30.dp, start = 20.dp)
           .fillMaxWidth()) {
//           Text("Welcome !",
//               fontSize = 32.sp,
//               fontFamily = FontFamily.SansSerif,
//               color = Color.Black,
//               fontWeight = FontWeight.Bold
//               )
           Spacer(Modifier.height(10.dp))
           Text("Select User Type",
               fontSize = 32.sp,
               color=Color.Black,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.padding(top=10.dp))


           Text("Please choose your profession",
               fontSize = 18.sp,
               color=Color.Gray,
               modifier = Modifier.padding(top=10.dp))
       }

       Spacer(Modifier.height(25.dp))

       Column(Modifier.padding(2.dp)./*fillMaxWidth()*/ fillMaxSize().verticalScroll(rememberScrollState()).imePadding(),
           horizontalAlignment = Alignment.CenterHorizontally,
           /*verticalArrangement = Arrangement.Center*/) {

//           Admin
           Box(
               Modifier.padding(10.dp)
                   .width(200.dp)
                   .height(200.dp)
                   .shadow(3.dp,
                       shape = RoundedCornerShape(8.dp)
                   )
                   .clickable {
                      context.startActivity(Intent(context,SignupActivity::class.java))
                   },
               contentAlignment = Alignment.Center,

           ) {
               Column(verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally) {
                   Image(
                       painter = painterResource(id = R.drawable.admin),
                       contentDescription = "Admin",
                       modifier = Modifier
                           .width(100.dp)
                           .height(100.dp)
                           .padding(bottom = 10.dp)
                           .clip(CircleShape)
                   )

                   Text("Admin", fontSize = 26.sp, textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth())
               }
           }

//           Teacher
           Box(
               Modifier.padding(10.dp)
                   .width(200.dp)
                   .height(200.dp)
                   .shadow(3.dp,
                       shape = RoundedCornerShape(8.dp))
                   .clickable {
                       Toast.makeText(context,"Coming soon",Toast.LENGTH_SHORT).show()
                   },
               contentAlignment = Alignment.Center,

               ) {
               Column(Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
               Image(
                   painter = painterResource(id= R.drawable.teacher),
                   contentDescription = "Teacher",
                   modifier = Modifier
                       .width(100.dp)
                       .height(100.dp)
                       .padding(bottom = 10.dp)
                       .clip(CircleShape)
               )

               Text("Teacher", fontSize = 26.sp, textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth())
               }
           }

//           Student
           Box(
               Modifier.padding(10.dp)
                   .width(200.dp)
                   .height(200.dp)
                   .shadow(3.dp,
                       shape = RoundedCornerShape(8.dp))
                   .clickable {
                      context.startActivity(Intent(context,StudentLogin::class.java))
                   },
               contentAlignment = Alignment.Center,

               ) {
               Column(Modifier, verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                   Image(
                       painter = painterResource(id= R.drawable.student),
                       contentDescription = "Student",
                       modifier = Modifier
                           .width(100.dp)
                           .height(100.dp)
                           .padding(bottom = 10.dp)
                           .clip(CircleShape)
                   )


                   Text("Student", fontSize = 26.sp, textAlign = TextAlign.Center,modifier=Modifier.fillMaxWidth())
               }
           }
       }
   }
}

