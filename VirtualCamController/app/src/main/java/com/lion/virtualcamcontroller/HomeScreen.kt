package com.lion.virtualcamcontroller
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lion.virtualcamreceiver.utils.AppEnv

@Composable
fun HomeScreen(){
    val homeController =  viewModel<HomeViewModel>()
    val context = LocalContext.current
    Card(modifier = Modifier
        .fillMaxWidth()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(16.dp),
            content = {
                if (homeController.loading.value) {
                    CircularProgressIndicator()
                }else {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
//                            Toast.makeText(context,AppEnv.API_URL,Toast.LENGTH_LONG).show()
                            homeController.handleChangePaused()
                        },
                        enabled = !homeController.loading.value
                    ) {
                        if (homeController.isPaused.value) {
                            Text(text = "恢复摄像头")
                        } else {
                            Text(text = "暂停摄像头")
                        }
                    }
                }

            }
        )
    }
}