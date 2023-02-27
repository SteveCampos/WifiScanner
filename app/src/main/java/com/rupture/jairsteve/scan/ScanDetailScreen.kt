package com.rupture.jairsteve.scan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rupture.jairsteve.rupture.R
import com.rupture.jairsteve.scan.entity.MyScanResult

@Preview
@Composable
fun ScanDetailScreen(
    item: MyScanResult = MyScanResult(
        bssid = "E8:94:F6:C2:5F:CE",
        ssid = "WLAN_XXXX",
        capabilities = "[WPA-PSKP-TKIP+CCMP]",
        vendorName = "TP-LINK TECHNOLOGIES"
    )
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(bottom = 24.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_2),
                modifier = Modifier.size(48.dp),
                contentDescription = null
            )
            Text(
                text = item.ssid,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = colorResource(R.color.Dark5),
            )
            Text(
                text = "Lima, Perú",
                color = colorResource(R.color.Dark1),
            )
            Spacer(Modifier.size(2.dp))
            LinearProgressIndicator(
                progress = .5f,
                color = colorResource(id = R.color.azul),
                modifier = Modifier.fillMaxWidth(.6f)
            )
            Text(
                text = item.vendorName.orEmpty(),
                color = colorResource(R.color.Dark5),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(8.dp)
            )
        }
        Column(
            Modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(top = 32.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScanAttribute(label = "BBSID", content = item.bssid)
            ScanAttribute(label = "Capabilities", content = item.capabilities)
            ScanAttribute(label = "Frecuencia", content = "2400 GHz")
            ScanAttribute(label = "Level", content = "88")
            ScanAttribute(label = "Timestamp", content = "985689547258684")
        }
    }
}

@Composable
fun ScanAttribute(label: String, content: String) {
    Row(Modifier.padding(8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = colorResource(R.color.Dark1),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        )
        Text(
            text = content,
            fontSize = 12.sp,
            color = colorResource(R.color.Dark1),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        )
    }
}