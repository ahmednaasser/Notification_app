package com.example.notification_app

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.notification_app.ui.theme.Notification_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val handler = handlePermissionResponse(this)
        createNotificationChannel()

        setContent {
            Notification_AppTheme {
                Box (contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                    ){

                    val context = LocalContext.current

                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            handler.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        else {

                        }

                    }) {
                        Text(text = "Notify Me")
                    }
                }

            }
        }
    }

    private fun handlePermissionResponse (c:Context):ActivityResultLauncher<String>{
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
                if(isGranted){
                    sendNotification(c)
                }else {

                //show alert dialog
                }

            }
        return permissionLauncher
    }

    //implement Notification channe;

    private fun createNotificationChannel(){
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("1" , "notificationChannel" , importance)
        channel.description = "displays general notifications for applications "

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }


    // shkow the Notification

    @SuppressLint("MissingPermission")
    private fun sendNotification(c: Context){

        val bitmap = BitmapFactory.decodeResource(c.resources, R.drawable.spider)
        val spiderUri  = Uri.parse("https://www.marvel.com/characters/spider-man-peter-parker/on-screen/profile")
        val intent = Intent(Intent.ACTION_VIEW, spiderUri)

        val pendingIntent =
            PendingIntent.getActivity(c , 0 , intent ,PendingIntent.FLAG_IMMUTABLE)

        val builder  = NotificationCompat.Builder(c, "1")
            .setSmallIcon(R.drawable.ic_flower)
            .setContentTitle("New Notification")
            .setContentText("I'm your friendly neighborhood Spider-Man ")
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
            .setContentIntent(pendingIntent)
            .build()



        //to make the notification appear
        NotificationManagerCompat.from(c).notify(66,builder)

    }
}


