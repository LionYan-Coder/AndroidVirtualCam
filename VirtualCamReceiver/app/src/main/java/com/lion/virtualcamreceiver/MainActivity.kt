package com.lion.virtualcamreceiver


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lion.virtualcamreceiver.ui.theme.VirtualCamReceiverTheme
import com.lion.virtualcamreceiver.utils.AppInstance


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VirtualCamReceiverTheme {
                Card(modifier = Modifier
                    .fillMaxWidth()){
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        content = {
                            AppInstance.ClientId?.let { Text(text = it) }
                        }
                    )
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
